package com.test.rickandmorty.mapper;

import com.test.rickandmorty.model.entity.Person;
import com.test.rickandmorty.model.entity.PersonContent;
import com.test.rickandmorty.model.repo.PersonRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class CharacterService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private PersonRepo personRepo;

    public Person getPersonage(final String id) {
        return webClient
                .get()
                .uri(String.join("","/character/", id))
                .retrieve().onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Person.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new Person()))
                .block();
    }

    public List<Person> getByName(final String name) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/character/")
                        .queryParam("name", name)
                        .build())
                .retrieve().onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))

                .bodyToMono(PersonContent.class)
                .map(PersonContent::getResults)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    public List<Person> getAllPersonage() {
        return webClient
                .get()
                .uri("/character")
                .retrieve().onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))

                .bodyToMono(PersonContent.class)
                .map(PersonContent::getResults)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }
    @Scheduled(cron = "0 0 0/1 1/1 * ?")
    public void synchronised() {
        cacheAllCharactersToDataBase();
    }

    private void cacheAllCharactersToDataBase() {
       List<Person> personages = getAllPersonage();
       if(!personages.isEmpty()){
        personRepo.saveAll(personages);
       }
    }

}
