package ua.goit.notes;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.goit.base.BaseService;
import ua.goit.users.User;
import ua.goit.users.UserRepository;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class NoteService extends BaseService<Note, NoteDto> implements HtmlConverter {

    int SHORT_TEXT_LENGTH = 100;
    NoteRepository repository;
    UserRepository userRepository;

    @Override
    public NoteDto create(NoteDto dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByName(userName);
        Note note = modelMapper.map(dto, Note.class);
        note.setUser(user);
        repository.save(note);
        return modelMapper.map(note, NoteDto.class);
    }

    @Override
    public List<NoteDto> findAll() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<NoteDto> dtoList = new ArrayList<>();
        repository.findByUser_NameIgnoreCase(userName).forEach(item -> {
            NoteDto map = modelMapper.map(item, NoteDto.class);
            String text = markdownToText(map.getText());
            if(text.length() > SHORT_TEXT_LENGTH){
                map.setText(text.substring(0,SHORT_TEXT_LENGTH) + "...");
            }else {
                map.setText(text);
            }
            dtoList.add(map);
        });
        return dtoList;
    }

    @Override
    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    @Override
    public String markdownToText(String markdown) {
        String s = markdownToHtml(markdown);
        return Jsoup.parse(s).wholeText();
    }
}