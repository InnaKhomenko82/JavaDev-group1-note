package ua.goit.validation.unique;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import ua.goit.base.BaseDto;
import ua.goit.base.BaseService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<UniqueValidation, BaseDto> {

    private BaseService<?, ?> modelService;
    private final ApplicationContext context;

    @Override
    public void initialize(UniqueValidation constraintAnnotation) {
        modelService = context.getBean(constraintAnnotation.classService());
    }

    @Override
    public boolean isValid(BaseDto value, ConstraintValidatorContext cont) {
        return !modelService.isExist(value);
    }
}
