package org.example.realworldapi.infrastructure.web.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import org.example.realworldapi.infrastructure.web.validation.validator.AtLeastOneFieldMustBeNotNullValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldMustBeNotNullValidator.class)
@Documented
public @interface AtLeastOneFieldMustBeNotNull {

  String message() default "At least one field must be not null";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] fieldNames() default {};
}
