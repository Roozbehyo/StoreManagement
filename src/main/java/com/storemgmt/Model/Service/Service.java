package com.storemgmt.Model.Service;

import java.util.List;

public interface Service<T, I> {
    void save(T t);

    void update(T t);

    void delete(I id);

    List<T> findAll();

    T findById(I id);
}
