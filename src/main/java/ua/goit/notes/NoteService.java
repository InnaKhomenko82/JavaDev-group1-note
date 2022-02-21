package ua.goit.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.goit.base.BaseService;
import ua.goit.users.User;
import ua.goit.users.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService extends BaseService<Note, NoteDto> {

    protected NoteRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public void setRepository(NoteRepository repository) {
        this.repository = repository;
    }

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
        repository.findByUser_NameIgnoreCase(userName).forEach(item -> dtoList.add(modelMapper.map(item, NoteDto.class)));
        return dtoList;
    }
}