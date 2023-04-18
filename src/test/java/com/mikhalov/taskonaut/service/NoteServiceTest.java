package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.SortAndPageDTO;
import com.mikhalov.taskonaut.mapper.NoteMapper;
import com.mikhalov.taskonaut.model.Note;
import com.mikhalov.taskonaut.model.Note_;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.model.User_;
import com.mikhalov.taskonaut.model.enums.NoteSortOption;
import com.mikhalov.taskonaut.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.mikhalov.taskonaut.model.enums.NoteSortOption.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserService userService;

    @Mock
    private NoteMapper noteMapper;

    @Captor
    private ArgumentCaptor<Specification<Note>> specCaptor;

    @Captor
    private ArgumentCaptor<PageRequest> pageRequestCaptor;

    private static Comparator<Note> getNoteComparator(NoteSortOption sort, boolean ascending) {
        Comparator<Note> comparator = Comparator.comparing((Note note) ->
                switch (Optional.ofNullable(sort).orElse(LAST_MODIFIED)) {
                    case TITLE -> note.getTitle();
                    case CREATION_DATE -> note.getCreationDate().toString();
                    case LAST_MODIFIED -> note.getLastModifiedDate().toString();
                });
        if (!ascending) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    @Test
    void testCreateNote() {
        NoteDTO noteDTO = mock(NoteDTO.class);
        Note note = mock(Note.class);
        User user = mock(User.class);

        when(noteMapper.toNote(noteDTO)).thenReturn(note);
        when(userService.getCurrentUser()).thenReturn(user);
        when(noteRepository.save(note)).thenReturn(note);

        noteService.createNote(noteDTO);


        verify(noteMapper, times(1)).toNote(noteDTO);
        verify(userService, times(1)).getCurrentUser();
        verify(note, times(1)).setUser(user);
        verify(noteRepository, times(1)).save(note);

    }

    @Test
    void testUpdateNote() {
        NoteDTO noteDTO = mock(NoteDTO.class);
        Note note = mock(Note.class);

        when(noteDTO.getId()).thenReturn("noteId");
        when(noteRepository.findById("noteId")).thenReturn(Optional.of(note));
        doNothing().when(noteMapper).updateNote(noteDTO, note);
        when(noteRepository.save(note)).thenReturn(note);

        noteService.updateNote(noteDTO);

        verify(noteRepository, times(1)).findById("noteId");
        verify(noteMapper, times(1)).updateNote(noteDTO, note);
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testDeleteNote() {
        String noteId = "noteId";
        Note note = mock(Note.class);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        doNothing().when(note).removeLabel();
        doNothing().when(noteRepository).delete(note);

        noteService.deleteNote(noteId);

        verify(noteRepository, times(1)).findById(noteId);
        verify(note, times(1)).removeLabel();
        verify(noteRepository, times(1)).delete(note);
    }

    @Test
    void testGetNoteById() {
        String noteId = "noteId";
        Note note = mock(Note.class);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById(noteId);

        assertEquals(note, result);
        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    void testGetNoteDTOById() {
        String noteId = "noteId";
        Note note = mock(Note.class);
        NoteDTO noteDTO = mock(NoteDTO.class);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(noteMapper.toNoteDTO(note)).thenReturn(noteDTO);

        NoteDTO result = noteService.getNoteDTOById(noteId);

        assertEquals(noteDTO, result);
        verify(noteRepository, times(1)).findById(noteId);
        verify(noteMapper, times(1)).toNoteDTO(note);
    }

    @Test
    void testGetSortedNotes_whenNotesIsEmpty_handleDefaultSorting() {
        getSortedNotes(0, 0, 5);
    }

    @Test
    void testGetSortedNotes_whenLessThanPageSize_handleDefaultSorting() {
        getSortedNotes(4, 0, 5);
    }

    @Test
    void testGetSortedNotes_whenMoreThanPageSize_handleDefaultSorting() {
        getSortedNotes(9, 0, 5);
    }

    @Test
    void testGetSortedNotes_whenLessThanPageSize_handleDefaultSortingForSecondPage() {
        getSortedNotes(9, 1, 5);
    }

    @Test
    void testGetSortedNotes_whenMoreThanPageSize_handleDefaultSortingForSecondPage() {
        getSortedNotes(15, 1, 7);
    }

    @Test
    void testGetSortedNotes_whenSingePageAndEqualsToPageSize_sortingByTitle() {
        getSortedNotes(10, 0, 10, TITLE);
    }

    @Test
    void testGetSortedNotes_forSecondPage_sortingByCreationDate() {
        getSortedNotes(15, 1, 10, CREATION_DATE);
    }

    private void getSortedNotes(int notesTotalCount, int page, int size) {
        getSortedNotes(notesTotalCount, page, size, LAST_MODIFIED);
    }

    private void getSortedNotes(int notesTotalCount, int page, int size, NoteSortOption sort) {
        int firstIndexOnCurrentPage = page * size;

        int totalPages = (int) Math.ceil((double) notesTotalCount / size);
        totalPages = Math.max(totalPages, 1);
        int countOnCurrentPage;
        // Check if it's the last page
        boolean isLastPage = (page == totalPages - 1);
        // Calculate the number of notes on the last page
        int notesOnLastPage = (notesTotalCount % size == 0 && notesTotalCount != 0)
                ? size
                : notesTotalCount % size;
        // If it's the last page, use the notes on the last page, otherwise use the full page size
        countOnCurrentPage = isLastPage ? notesOnLastPage : size;

        SortAndPageDTO sortAndPageDTO = new SortAndPageDTO();
        sortAndPageDTO.setSize(size);
        sortAndPageDTO.setPage(page);
        sortAndPageDTO.setSort(sort);
        String userEmail = "test@example.com";
        List<Note> noteList = new ArrayList<>();
        List<NoteDTO> noteDTOList = new ArrayList<>();


        createSampleNotesAndNoteDTOs(notesTotalCount, noteList, noteDTOList,
                sortAndPageDTO.getSort(), sortAndPageDTO.isAsc());

        setupMocks(userEmail, noteList, noteDTOList, sortAndPageDTO);
        Page<NoteDTO> result = noteService.getSortedNotes(sortAndPageDTO);
        verifyMocks(countOnCurrentPage);
        verifySpecificationAndPageRequest(sortAndPageDTO, userEmail);


        assertEquals(noteDTOList.subList(firstIndexOnCurrentPage, firstIndexOnCurrentPage + countOnCurrentPage), result.getContent());
        assertEquals(noteDTOList.size(), result.getTotalElements());
    }

    private void createSampleNotesAndNoteDTOs(
            int count, List<Note> noteList, List<NoteDTO> noteDTOList,
            NoteSortOption sort, boolean ascending) {

        for (int i = 1; i <= count; i++) {
            Note note = new Note();
            note.setTitle("Sample Note " + i);
            note.setContent("Sample content " + i);
            note.setCreationDate(LocalDateTime.now().minusDays(i));
            note.setLastModifiedDate(LocalDateTime.now().minusHours(i));
            noteList.add(note);
        }

        Comparator<Note> comparator = getNoteComparator(sort, ascending);

        noteDTOList.addAll(noteList.stream()
                .sorted(comparator)
                .map(this::mapperToNoteDTO)
                .toList());

    }

    private NoteDTO mapperToNoteDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle(note.getTitle());
        noteDTO.setContent(note.getContent());
        noteDTO.setId(note.getId());

        return noteDTO;
    }

    private void setupMocks(String userEmail, List<Note> noteList, List<NoteDTO> noteDTOList, SortAndPageDTO sortAndPageDTO) {
        when(userService.getCurrentUserUsername()).thenReturn(userEmail);
        when(noteRepository.findAll(any(Specification.class), any(PageRequest.class))).thenAnswer(invocation -> {
            PageRequest pageRequest = invocation.getArgument(1);
            int start = pageRequest.getPageNumber() * pageRequest.getPageSize();
            Comparator<Note> comparator = getNoteComparator(sortAndPageDTO.getSort(), sortAndPageDTO.isAsc());
            List<Note> resultList = noteList.stream()
                    .sorted(comparator)
                    .skip(start)
                    .limit(sortAndPageDTO.getSize())
                    .toList();

            return new PageImpl<>(resultList, pageRequest, noteList.size());
        });
        if (!noteList.isEmpty()) {
            when(noteMapper.toNoteDTO(any(Note.class))).thenAnswer(invocation -> {
                Note inputNote = invocation.getArgument(0);
                return noteDTOList.stream().filter(dto -> dto.getTitle().equals(inputNote.getTitle())).findFirst().orElse(null);
            });
        }
    }

    private void verifyMocks(int size) {
        verify(userService, times(1)).getCurrentUserUsername();
        verify(noteRepository, times(1)).findAll(specCaptor.capture(), pageRequestCaptor.capture());
        verify(noteMapper, times(size)).toNoteDTO(any(Note.class));
    }

    private void verifySpecificationAndPageRequest(SortAndPageDTO sortAndPageDTO, String userEmail) {
        Sort sort = Sort.by(
                sortAndPageDTO.isAsc() ? ASC : DESC,
                sortAndPageDTO.getSort().getSortProperty()
        );

        Specification<Note> capturedSpec = specCaptor.getValue();
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Note> cq = mock(CriteriaQuery.class);
        Root<Note> root = mock(Root.class);
        Path<Object> userPath = mock(Path.class);
        Path<Object> emailPath = mock(Path.class);

        when(root.get(Note_.USER)).thenReturn(userPath);
        when(userPath.get(User_.EMAIL)).thenReturn(emailPath);

        capturedSpec.toPredicate(root, cq, cb);

        verify(cb, times(1)).equal(emailPath, userEmail);

        PageRequest capturedPageRequest = pageRequestCaptor.getValue();
        assertEquals(sortAndPageDTO.getPage(), capturedPageRequest.getPageNumber());
        assertEquals(sortAndPageDTO.getSize(), capturedPageRequest.getPageSize());
        assertEquals(sort, capturedPageRequest.getSort());
    }

}