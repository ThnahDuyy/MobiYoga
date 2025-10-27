package com.nguyenthanhduy.yoga_d.adapter;

import androidx.annotation.NonNull;

public class UnitArrayAdapter<T> {
    private String label;
    private T value;

    public UnitArrayAdapter(String label, T value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public T getValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return label;
    }
}
