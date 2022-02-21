package ua.goit.notes;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.goit.configuration.MvcConfiguration;
import ua.goit.configuration.SpringSecurityConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@ComponentScan("ua.goit")
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@AutoConfigureDataJpa
@ActiveProfiles("dev")
@SpringBootTest
//@Import({SpringSecurityConfiguration.class, MvcConfiguration.class})
//@EnableWebMvc
class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Test
    public void insertNote() {
        NoteDto noteDto = new NoteDto();
        noteDto.setName("test note");
        noteDto.setText("bla bla bla");

        NoteDto saved = noteService.create(noteDto);
        NoteDto found = noteService.find(saved.getId());

        assertThat(found).isEqualTo(saved);
    }

    @Test
    public void updateNote() {

        NoteDto noteDto = new NoteDto();
        noteDto.setName("test note");
        noteDto.setText("bla bla bla");

        NoteDto created = noteService.create(noteDto);
        noteDto.setText("la la la");
        noteService.update(created.getId(), noteDto);
        NoteDto updated = noteService.find(created.getId());

        assertThat(updated).isEqualTo(noteDto);
    }

    @Test
    public void getNote() {

        NoteDto noteDto = new NoteDto();
        noteDto.setName("test note");
        noteDto.setText("bla bla bla");

        NoteDto created = noteService.create(noteDto);
        noteDto.setText("la la la");
        noteService.update(created.getId(), noteDto);
        NoteDto updated = noteService.find(created.getId());

        assertThat(updated.getId()).isEqualTo(noteDto.getId());
    }

    @Test
    public void deleteNote() {

        NoteDto noteDto = new NoteDto();
        noteDto.setName("test note");
        noteDto.setText("bla bla bla");

        NoteDto created = noteService.create(noteDto);
        UUID uuid = created.getId();
        noteService.delete(uuid);
        NoteDto found = noteService.find(uuid);

        assertThat(found).isEqualTo(new NoteDto());
    }

}