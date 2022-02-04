package com.ecsimsw.fastcrud.sample;

import com.ecsimsw.fastcrud.CRUD;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@CRUD
@Entity
public class TestEntity {

    @GeneratedValue
    @Id
    private Long id;
    private String name;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
