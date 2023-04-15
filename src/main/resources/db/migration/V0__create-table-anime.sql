--[FLYWAY] [step 3] adicionar um arquivo do tipo sql para ser executado pelo flyway
CREATE TABLE Anime (
    id bigserial NOT NULL,
    name varchar(255) NOT NULL
    --dat_creation TIMESTAMP DEFAULT NOW() NOT NULL,
    --dat_update TIMESTAMP
);

CREATE UNIQUE INDEX id_idx ON Anime(id);

ALTER TABLE anime.anime ADD CONSTRAINT anime_pk PRIMARY KEY USING INDEX id_idx;
