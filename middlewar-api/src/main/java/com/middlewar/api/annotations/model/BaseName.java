package com.middlewar.api.annotations.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author LEBOC Philippe
 */
@Retention(RUNTIME)
@Target({PARAMETER, ANNOTATION_TYPE, FIELD})
@NotEmpty
@Size(min = 4, max = 16)
public @interface BaseName {
}
