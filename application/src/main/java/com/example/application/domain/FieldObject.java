package com.example.application.domain;

import javax.persistence.Embeddable;

@Embeddable
public class FieldObject {

    private String age;

    public FieldObject() {
    }

    public FieldObject(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
