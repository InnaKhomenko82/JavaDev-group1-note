package ua.goit.validation.deleteRole;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ua.goit.users.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserExistValidator implements ConstraintValidator<UserExistValidation, UUID> {

    UserService userService;

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext cont) {
        return !userService.isExistByRoleId(value);
    }
}
