package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.mapper.NoteMapper;
import com.mikhalov.taskonaut.model.*;
import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import com.mikhalov.taskonaut.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.JoinType;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.mikhalov.taskonaut.model.enums.NoteSortOption.LAST_MODIFIED;


@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final NoteMapper noteMapper;

    public void createNote(NoteDTO noteDTO) {
        Note note = noteMapper.toNote(noteDTO);
        User user = userService.getCurrentUser();
        note.setUser(user);

        noteRepository.save(note);
    }

    public Page<NoteDTO> getSortedNotes(NoteSortOption sortOption, boolean ascending, int page, int size) {
        String userEmail = userService.getCurrentUserUsername();

        Sort sort = getSortByNoteSortOptionAndDirection(sortOption, ascending);
        Specification<Note> userEmailSpecification = (root, query, criteriaBuilder) -> {
            root.join(Note_.LABEL, JoinType.LEFT);

            return criteriaBuilder
                    .equal(root.get(Note_.USER).get(User_.EMAIL), userEmail);
        };

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Note> notes = noteRepository.findAll(userEmailSpecification, pageable);

        return notes.map(noteMapper::toNoteDTO);
    }

    public Page<NoteDTO> getSortedNotesByLabelName(String labelName, NoteSortOption sortOption,
                                                   boolean ascending, int page, int size) {
        String userEmail = userService.getCurrentUserUsername();
        Sort sort = getSortByNoteSortOptionAndDirection(sortOption, ascending);
        Specification<Note> notesByLabelNameForCurrentUser = (root, query, criteriaBuilder) -> {
            var labelJoin = root.join(Note_.LABEL, JoinType.INNER);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            root.get(Note_.USER).get(User_.EMAIL), userEmail),
                    criteriaBuilder.equal(
                            labelJoin.get(Label_.NAME), labelName));
        };

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Note> notes = noteRepository.findAll(notesByLabelNameForCurrentUser, pageable);

        return notes.map(noteMapper::toNoteDTO);
    }

    public Page<NoteDTO> searchNotesByKeywordAndSort(String keyword, NoteSortOption sortOption,
                                                     boolean ascending, int page, int size) {
        String userEmail = userService.getCurrentUserUsername();
        Sort sort = getSortByNoteSortOptionAndDirection(sortOption, ascending);
        var noteSpecification = getSpecificationForSearchNotesByKeywordAndUserEmail(keyword, userEmail);

        Pageable pageable = PageRequest.of(page, size, sort);


        Page<Note> notes = noteRepository.findAll(noteSpecification, pageable);

        return notes.map(noteMapper::toNoteDTO);
    }

    private static Specification<Note> getSpecificationForSearchNotesByKeywordAndUserEmail(
            String keyword, String userEmail) {
        String searchPattern = "%" + keyword.toLowerCase() + "%";

        return (root, query, criteriaBuilder) -> {
            var labelJoin = root.join(Note_.LABEL, JoinType.LEFT);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get(Note_.USER).get(User_.EMAIL), userEmail),
                    criteriaBuilder.or(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get(Note_.TITLE)), searchPattern),
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get(Note_.CONTENT)), searchPattern),
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(labelJoin.get(Label_.NAME)), searchPattern)
                    )
            );
        };
    }

    private Sort getSortByNoteSortOptionAndDirection(NoteSortOption sortOption, boolean ascending) {
        String sortProperty = Optional.ofNullable(sortOption)
                .orElse(LAST_MODIFIED)
                .getSortProperty();
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;

        return Sort.by(direction, sortProperty);
    }

    public void updateNote(NoteDTO noteDTO) {
        Note note = getNoteById(noteDTO.getId());
        noteMapper.updateNote(noteDTO, note);
        updateNote(note);
    }

    public void updateNote(Note note) {
        noteRepository.save(note);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
    }

    public NoteDTO getNoteDTOById(String id) {
        Note note = getNoteById(id);

        return noteMapper.toNoteDTO(note);
    }

    public List<NoteDTO> getSortedNotesByLabelNameUsingQuery(String labelName, NoteSortOption sortOption, boolean ascending) {
        String currentUserEmail = userService.getCurrentUserUsername();

        List<Note> notes = noteRepository.findByLabelNameAndUserEmail(labelName, currentUserEmail);

        Comparator<Note> comparator = Comparator.comparing((Note note) ->
                switch (Optional.ofNullable(sortOption).orElse(LAST_MODIFIED)) {
                    case TITLE -> note.getTitle();
                    case CREATION_DATE -> note.getCreationDate().toString();
                    case LAST_MODIFIED -> note.getLastModifiedDate().toString();
                    default -> throw new IllegalArgumentException("Unsupported sort option: " + sortOption);
                });

        if (!ascending) {
            comparator = comparator.reversed();
        }

        notes.sort(comparator);

        return noteMapper.toNoteDTOList(notes);
    }
}

