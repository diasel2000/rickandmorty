package com.test.rickandmorty.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "person")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @Id
    Integer id;
    String name;
    String status;
    String species;
    String type;
    String gender;
    String image;
}
