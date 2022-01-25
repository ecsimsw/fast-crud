package com.example.tester;

import com.ecsimsw.fastcrud.CRUD;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@CRUD
@Entity
public class Sample {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    public Sample() {
    }

    public Sample(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
