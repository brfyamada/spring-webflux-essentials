package com.brfyamada.webflux.repository;

import com.brfyamada.webflux.domain.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

    Mono<Anime> findById(Integer id);

    Flux<Anime> findByName(String name);



}
