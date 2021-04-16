package com.test.rickandmorty.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonContent {
    private Object info;
    private List<Person> results;
}
