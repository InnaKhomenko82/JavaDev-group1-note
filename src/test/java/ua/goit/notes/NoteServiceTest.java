package ua.goit.notes;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@SpringBootTest
class NoteServiceTest {

    private final NoteService noteService;

    private NoteDto noteDto;

    @Autowired
    public NoteServiceTest(NoteService noteService) {
        this.noteService = noteService;
    }

    @BeforeEach
    private void init() {
        noteDto = new NoteDto();
        noteDto.setName("test note");
        noteDto.setText("bla bla bla");
    }

    @WithMockUser("user1")
    @Test
    public void insertNote() {
        NoteDto saved = noteService.create(noteDto);
        NoteDto found = noteService.find(saved.getId());

        assertThat(found).isEqualTo(saved);
    }

    @WithMockUser("user1")
    @Test
    public void updateNote() {
        NoteDto created = noteService.create(noteDto);
        noteDto.setText("la la la");
        noteService.update(created.getId(), noteDto);
        NoteDto updated = noteService.find(created.getId());

        assertThat(updated).isEqualTo(noteDto);
    }

    @WithMockUser("user1")
    @Test
    public void getNote() {
        NoteDto created = noteService.create(noteDto);
        noteDto.setText("la la la");
        noteService.update(created.getId(), noteDto);
        NoteDto updated = noteService.find(created.getId());

        assertThat(updated.getId()).isEqualTo(noteDto.getId());
    }

    @WithMockUser("user1")
    @Test
    public void deleteNote() {
        NoteDto created = noteService.create(noteDto);
        UUID uuid = created.getId();
        noteService.delete(uuid);
        NoteDto found = noteService.find(uuid);

        assertThat(found).isEqualTo(new NoteDto());
    }

}