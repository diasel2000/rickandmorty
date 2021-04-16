package com.test.rickandmorty.model.repo;

import com.test.rickandmorty.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Integer>{
}
