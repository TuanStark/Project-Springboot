package com.stark.webbanhang.helper.base.construct;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public interface IBaseRepository<T, ID extends Serializable> {

    T get(ID id);

    List<T> getAll();

    T save(T entity);

    T save(T entity, Function<T, T> callback);

    T update(ID id, T entity);

    T update(ID id, T entity, Function<T, T> callback);

    T delete(ID id);

    T delete(ID id, Function<T, T> callback);

}