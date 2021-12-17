package com.mediscreen.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoteValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNote {

    String message() default "Merci de saisir votre note ou annuler";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}