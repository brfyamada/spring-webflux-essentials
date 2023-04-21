package com.brfyamada.webflux.controller;

import com.brfyamada.webflux.Teste;
import com.brfyamada.webflux.domain.Anime;
import com.brfyamada.webflux.service.AnimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("anime")
@Slf4j
public class AnimeController {

    private static final Logger logger = LoggerFactory.getLogger(AnimeController.class);

    private final AnimeService animeService;
    private final QueueMessagingTemplate queueMessagingTemplate;

    private final Teste test;

    @Value("${aws.sqs.end-point}")
    private String sqsServiceEndpoint;

    @GetMapping
    public Flux<Anime> listAll(){
        return animeService.findAll();
    }


    @GetMapping(path = "{id}")
    public Mono<Anime> findById(@PathVariable int id){
        return animeService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Anime> save(@Valid @RequestBody Anime anime){
        return animeService.save(anime);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> batchAnime(@RequestBody List<Anime> animes) {
        logger.info("SQS- queue Anime - {}, animes");

        return Mono.just(animes)
                .map(anime -> {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
                        String json = ow.writeValueAsString(anime);
                        queueMessagingTemplate.send(sqsServiceEndpoint, MessageBuilder.withPayload(json).build());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return Mono.empty();
                }).thenEmpty(Mono.empty());
    }

    @GetMapping("/test")
    public Mono<List<String>> test() {
        return test.test();

    }

    @GetMapping("/sindicato")
    public Mono<Teste.Sindicato> test2() {
        return test.test2();

    }

    @GetMapping("/anime")
    public Flux<Anime> findById(@RequestParam String name){
        return animeService.findByName(name)
                .switchIfEmpty(Flux.empty());
    }




}
