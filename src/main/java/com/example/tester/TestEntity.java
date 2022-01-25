package com.example.tester;


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

    public TestEntity() {
    }

    public TestEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
