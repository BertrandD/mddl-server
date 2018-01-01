package com.middlewar.core.annotations;

import org.hibernate.validator.constraints.NotEmpty;

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
@Size(min = 8, max = 50)
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
public @interface Password {
}
