package com.middlewar.core.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Leboc Philippe.
 */
@NotEmpty
@Size(min = 8)
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@Constraint(validatedBy = {  })
public @interface Password {

    String message() default "{org.hibernate.validator.constraints.Password.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
