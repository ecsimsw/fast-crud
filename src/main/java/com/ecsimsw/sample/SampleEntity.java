package com.ecsimsw.sample;

import com.ecsimsw.fastcrud.annotation.CRUD;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@CRUD(repositoryType = SampleRepository.class)
@Entity
public class SampleEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public Long id;

    public SampleEntity() {
    }
}
