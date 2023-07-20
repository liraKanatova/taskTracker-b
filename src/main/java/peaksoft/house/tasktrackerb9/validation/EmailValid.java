package peaksoft.house.tasktrackerb9.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EmailValidatorses.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmailValid {
    String message() default "{ email must be unique! }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
