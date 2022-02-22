package ua.goit.notes;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ua.goit.base.BaseDto;
import ua.goit.users.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor(force = true)
public class NoteDto implements BaseDto {

    private UUID id;

    @NotBlank
    @Length(min = 5, max = 100)
    private String name;

    @NotBlank
    @Length(min = 5, max = 10000)
    private String text;

    @NotNull
    private AccessType accessType;

    private UserDto user;
}