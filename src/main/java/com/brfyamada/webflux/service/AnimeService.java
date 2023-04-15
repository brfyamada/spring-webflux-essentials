package com.brfyamada.webflux.service;

import com.brfyamada.webflux.repository.AnimeRepository;
import com.brfyamada.webflux.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Mono<Anime> findById(Integer id){
        return animeRepository.findById(id);
    }

    public Mono<Anime> save(Anime anime) {
        return animeRepository.save(anime);
    }
}
