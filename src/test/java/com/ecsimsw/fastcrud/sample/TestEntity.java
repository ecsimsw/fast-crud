package com.ecsimsw.fastcrud.sample;

import com.ecsimsw.fastcrud.annotation.CRUD;
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

    public TestEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestEntity(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
