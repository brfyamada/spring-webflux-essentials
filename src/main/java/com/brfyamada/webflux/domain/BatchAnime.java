package com.brfyamada.webflux.domain;

import java.util.HashSet;
import java.util.Set;

public class BatchAnime {

    private int messagesCount;
    private Set<Anime> animeSet = new HashSet<>();

    public BatchAnime(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    public Set<Anime> getAnimeSet() {
        return animeSet;
    }
}
