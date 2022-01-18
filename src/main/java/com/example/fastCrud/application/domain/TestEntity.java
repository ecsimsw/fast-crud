package com.example.fastCrud.application.domain;

import com.example.fastCrud.framework.CRUD;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@CRUD(rootPath = "test")
@Entity
public class TestEntity {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @Enumerated
    private FieldObject fieldObject;

    public TestEntity() {
    }

    public TestEntity(String name, FieldObject fieldObject) {
        this.name = name;
        this.fieldObject = fieldObject;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FieldObject getFieldObject() {
        return fieldObject;
    }
}
