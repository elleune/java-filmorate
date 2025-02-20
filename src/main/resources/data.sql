MERGE INTO mpa_ratings (name) KEY(name) VALUES ('G');
MERGE INTO mpa_ratings (name) KEY(name) VALUES ('PG');
MERGE INTO mpa_ratings (name) KEY(name) VALUES ('PG-13');
MERGE INTO mpa_ratings (name) KEY(name) VALUES ('R');
MERGE INTO mpa_ratings (name) KEY(name) VALUES ('NC-17');

MERGE INTO genres (name) KEY(name) VALUES ('Комедия');
MERGE INTO genres (name) KEY(name) VALUES ('Драма');
MERGE INTO genres (name) KEY(name) VALUES ('Мультфильм');
MERGE INTO genres (name) KEY(name) VALUES ('Триллер');
MERGE INTO genres (name) KEY(name) VALUES ('Документальный');
MERGE INTO genres (name) KEY(name) VALUES ('Боевик');