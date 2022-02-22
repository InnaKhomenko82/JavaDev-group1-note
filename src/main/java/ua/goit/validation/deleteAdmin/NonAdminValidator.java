package ua.goit.validation.deleteAdmin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.context.ApplicationContext;
import ua.goit.base.BaseService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NonAdminValidator implements ConstraintValidator<NonAdminValidation, UUID> {

    @NonFinal private BaseService<?, ?> modelService;
    ApplicationContext context;

    @Override
    public void initialize(NonAdminValidation constraintAnnotation) {
        modelService = context.getBean(constraintAnnotation.classService());
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext cont) {
        return !modelService.find(value)
                .getName().toLowerCase(Locale.ROOT)
                .equals("admin");
    }
}
