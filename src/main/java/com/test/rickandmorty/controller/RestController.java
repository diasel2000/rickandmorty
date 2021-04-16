package com.test.rickandmorty.controller;

import com.test.rickandmorty.mapper.CharacterService;
import com.test.rickandmorty.model.entity.Person;
import com.test.rickandmorty.model.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
public class RestController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private PersonRepo personRepo;

    @RequestMapping(path = "/getPersonage", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Person characterWiki() {
        Random rand = new Random();
        int randomId = rand.nextInt(671);
        Person person = characterService.getPersonage(String.valueOf(randomId));
        return person;
    }

    @RequestMapping(path = "/getByName", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Person> searchCharacterByName(@RequestParam String name) {
        return characterService.getByName(name);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Person> getAll() {
        return characterService.getAllPersonage();
    }
}
