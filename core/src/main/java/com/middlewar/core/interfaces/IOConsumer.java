package com.middlewar.core.interfaces;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Leboc Philippe.
 */
public interface IOConsumer<T> extends Consumer<T> {

    @Override
    default void accept(T t) {
        try { doIO(t); } catch(IOException e) { throw new UncheckedIOException(e); }
    }

    void doIO(T t) throws IOException;

    static <E> void forEach(Stream<? extends E> s, IOConsumer<? super E> c) throws IOException {
        try{ s.forEach(c); } catch(UncheckedIOException e){
            throw e.getCause();
        }
    }
}
