package com.middlewar.api.annotations.authentication;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author LEBOC Philippe
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@PreAuthorize("hasRole('ROLE_USER')")
public @interface User {
}
