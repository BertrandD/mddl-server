package com.middlewar.api.services;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface CrudService<T, ID> {
    T find(ID id);
    List<T> findAll();
    T save(@NotNull @Valid T object);
    T update(@NotNull @Valid T object);
    void updateAsync(@NotNull @Valid T object);
    void delete(@NotNull @Valid T object);
    void deleteAsync(@NotNull @Valid T object);
    void deleteAsyncById(ID id);
    void deleteAll();
}
