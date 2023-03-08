--[FLYWAY] [step 3] adicionar um arquivo do tipo sql para ser executado pelo flyway
CREATE TABLE animes (
    idt_anime_id BIGSERIAL NOT NULL,
    des_name varchar(255) NOT NULL,
    dat_creation TIMESTAMP DEFAULT NOW() NOT NULL,
    dat_update TIMESTAMP
)