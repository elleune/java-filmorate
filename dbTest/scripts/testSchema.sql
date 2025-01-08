DROP TABLE IF EXISTS CLIENTS CASCADE;
DROP TABLE IF EXISTS FRIENDSHIP CASCADE;
DROP TABLE IF EXISTS FILMS CASCADE;
DROP TABLE IF EXISTS GENRES CASCADE;
DROP TABLE IF EXISTS COMB_FILMS_GENRES CASCADE;
DROP TABLE IF EXISTS LIKES CASCADE;
DROP TABLE IF EXISTS MPA CASCADE;

create table CLIENTS
(
    CLIENT_ID    INTEGER               not null
        primary key,
    CLIENT_EMAIL CHARACTER VARYING(50) not null,
    LOGIN        CHARACTER VARYING(50) not null,
    CLIENT_NAME  CHARACTER VARYING(50),
    BIRTHDAY     DATE                  not null
);

create table FRIENDSHIP
(
    FRIENDSHIP_ID INTEGER auto_increment
        primary key,
    FRIEND1_ID    INTEGER not null,
    FRIEND2_ID    INTEGER not null,
    constraint FRIENDSHIP_CLIENT2_FK
        foreign key (FRIEND2_ID) references CLIENTS,
    constraint FRIENDSHIP_CLIENTS_FK
        foreign key (FRIEND1_ID) references CLIENTS

);

create table MPA
(
    MPA_ID   INTEGER               not null
        primary key,
    MPA_INFO CHARACTER VARYING(50) not null
);
create table FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    FILM_NAME    CHARACTER VARYING(50)  not null,
    RELEASE_DATE DATE                   not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    DURATION     BIGINT,
    MPA_ID       INTEGER                not null,
    constraint FILMS_MPA_FK
        foreign key (MPA_ID) references MPA
);
create table GENRES
(
    GENRE_ID   INTEGER               not null
        primary key,
    GENRE_NAME CHARACTER VARYING(50) not null
);

create table COMB_FILMS_GENRES
(
    COMB_ID INTEGER auto_increment
        primary key,
    GENRE_ID INTEGER not null,
    FILM_ID  INTEGER not null,
    constraint FILMS_GENRES2_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILMS_GENRES_FK
        foreign key (GENRE_ID) references GENRES
);

create table LIKES
(
    LIKE_ID   INTEGER auto_increment
        primary key,
    CLIENT_ID INTEGER not null,
    FILM_ID   INTEGER not null,
    constraint LIKES_CLIENTS_FK
        foreign key (CLIENT_ID) references CLIENTS,
    constraint LIKES_FILMS_FK
        foreign key (FILM_ID) references FILMS
);



