package com.middlewar.api.annotations;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Leboc Philippe.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("prod")
public @interface Prod {
}
