drop table if exists users,friends,mpa_rating,films,likes,genres,films_genres;

create table if not exists users
(
    user_id  bigint primary key auto_increment,
    email    varchar not null,
    login    varchar not null,
    name     varchar,
    birthday date
);

create table if not exists friends
(
    user_id  bigint references users (user_id) on delete cascade,
    friend_id bigint references users (user_id) on delete cascade,
    status    enum ('confirmed','unconfirmed') default 'unconfirmed',
    CONSTRAINT friends UNIQUE (user_id, friend_id)
);

create table if not exists mpa_rating
(
    rating_id   int primary key,
    rating_code varchar
);

create table if not exists films
(
    film_id      bigint primary key auto_increment,
    name         varchar not null,
    description  varchar(200),
    release_date date,
    duration     int,
    rating_id    int references mpa_rating (rating_id)
);

create table if not exists likes
(
    user_id bigint references users (user_id) on delete cascade,
    film_id bigint references films (film_id) on delete cascade,
    CONSTRAINT likes UNIQUE (user_id, film_id)
);

create table if not exists genres
(
    genre_id int primary key,
    name     varchar not null
);

create table if not exists films_genres
(
    film_id  bigint references films (film_id),
    genre_id bigint references genres (genre_id),
    CONSTRAINT films_genres UNIQUE (film_id, genre_id)
);