package com.nguyenthanhduy.yoga_d.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ClassInstance implements Serializable {
    public String classInstanceID;
    public String date;
    public String teacher;
    public String comment;
    public String yogaClassID;

    public ClassInstance() {
        this.classInstanceID = String.valueOf(Calendar.getInstance().getTime().getTime());
    }

    public ClassInstance(String classInstanceID) {
        this.classInstanceID = classInstanceID;
    }
}
