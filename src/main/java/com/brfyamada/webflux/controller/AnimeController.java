package com.brfyamada.webflux.controller;

import com.brfyamada.webflux.domain.Anime;
import com.brfyamada.webflux.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("anime")
@Slf4j
public class AnimeController {

    private AnimeService animeService;

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

}
