package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.ExportParamsDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.SortAndPageDTO;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.JoinType;
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

    public Page<NoteDTO> getSortedNotes(SortAndPageDTO sortAndPage) {
        String userEmail = userService.getCurrentUserUsername();

        Sort sort = getSortByNoteSortOptionAndDirection(sortAndPage.getSort(), sortAndPage.isAsc());
        Specification<Note> userEmailSpecification = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(Note_.USER).get(User_.EMAIL), userEmail);

        Pageable pageable = PageRequest.of(sortAndPage.getPage(), sortAndPage.getSize(), sort);
        Page<Note> notes = noteRepository.findAll(userEmailSpecification, pageable);

        return notes.map(noteMapper::toNoteDTO);
    }

    public List<NoteDTO> getSortedNotesForExport(ExportParamsDTO exportParams) {
        String userEmail = userService.getCurrentUserUsername();

        Sort sort = getSortByNoteSortOptionAndDirection(exportParams.getSort(), exportParams.isAsc());
        Specification<Note> userEmailFetchLabels = ((root, query, criteriaBuilder) -> {
            root.fetch(Note_.LABEL, JoinType.LEFT);

            return criteriaBuilder
                    .equal(root.get(Note_.USER).get(User_.EMAIL), userEmail);
        });

        List<Note> sortedNotes = noteRepository.findAll(userEmailFetchLabels, sort);

        return noteMapper.toNoteDTOList(sortedNotes);
    }

    public Page<NoteDTO> getSortedNotesByLabelName(String labelName, SortAndPageDTO sortAndPage) {
        String userEmail = userService.getCurrentUserUsername();
        Sort sort = getSortByNoteSortOptionAndDirection(sortAndPage.getSort(), sortAndPage.isAsc());
        Specification<Note> notesByLabelNameForCurrentUser = (root, query, criteriaBuilder) -> {
            var labelJoin = root.join(Note_.LABEL, JoinType.INNER);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            root.get(Note_.USER).get(User_.EMAIL), userEmail),
                    criteriaBuilder.equal(
                            labelJoin.get(Label_.NAME), labelName));
        };

        Pageable pageable = PageRequest.of(sortAndPage.getPage(), sortAndPage.getSize(), sort);
        Page<Note> notes = noteRepository.findAll(notesByLabelNameForCurrentUser, pageable);

        return notes.map(noteMapper::toNoteDTO);
    }

    public Page<NoteDTO> searchNotesByKeywordAndSort(String keyword, SortAndPageDTO sortAndPage) {
        String userEmail = userService.getCurrentUserUsername();
        Sort sort = getSortByNoteSortOptionAndDirection(sortAndPage.getSort(), sortAndPage.isAsc());
        var noteSpecification = getSpecificationForSearchNotesByKeywordAndUserEmail(keyword, userEmail);

        Pageable pageable = PageRequest.of(sortAndPage.getPage(), sortAndPage.getSize(), sort);


        Page<Note> notes = noteRepository.findAll(noteSpecification, pageable);

        return notes.map(noteMapper::toNoteDTO);
    }

    private Specification<Note> getSpecificationForSearchNotesByKeywordAndUserEmail(
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

    @Transactional
    public void deleteNote(String id) {
        Note note = getNoteById(id);
        note.removeLabel();

        noteRepository.delete(note);
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
    }

    public NoteDTO getNoteDTOById(String id) {
        Note note = getNoteById(id);

        return noteMapper.toNoteDTO(note);
    }

}

