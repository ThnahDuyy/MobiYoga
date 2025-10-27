package com.nguyenthanhduy.yoga_d.adapter;

import java.util.List;

public interface IAdapter<T> {
    void add(T model);

    void update(T model);

    void remove(String id);

    void updateDataList(List<T> modelList);

    int indexOfObject(String id);

    void setAdapterEvent(IAdapterEvent<T> event);
}
