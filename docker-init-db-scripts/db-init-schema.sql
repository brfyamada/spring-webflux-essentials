DROP SCHEMA IF EXISTS anime;
CREATE SCHEMA anime;

CREATE EXTENSION pgcrypto SCHEMA anime;

CREATE TABLESPACE tsdanime01 OWNER postgres LOCATION '/var/lib/postgresql/data/pg_tblspc';
CREATE TABLESPACE tsianime01 OWNER postgres LOCATION '/var/lib/postgresql/data/';

