package com.nguyenthanhduy.yoga_d.database;

import java.util.List;

public interface IDatabase<T> {
    void add(T t);

    void update(T t);

    void remove(String id);

    T getOne(String id);

    List<T> getAll();

    void clearData();
}
