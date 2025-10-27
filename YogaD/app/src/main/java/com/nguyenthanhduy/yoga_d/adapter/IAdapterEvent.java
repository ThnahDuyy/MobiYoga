package com.nguyenthanhduy.yoga_d.adapter;

public interface IAdapterEvent<T> {
    void onObjectClicked(T model);

    void onEditClicked(T model);

    void onRemoveClicked(String id);
}
