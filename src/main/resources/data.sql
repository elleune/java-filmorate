MERGE INTO PUBLIC.RATING
  (RATING_ID, NAME)
  KEY(RATING_ID)
VALUES
	 (1,'G'),
	 (2,'PG'),
	 (3,'PG-13'),
	 (4,'R'),
	 (5,'NC-17');

MERGE INTO PUBLIC.GENRE
    (GENRE_ID, NAME)
    KEY(GENRE_ID)
    VALUES
        (1,'Комедия'),
        (2,'Драма'),
        (3,'Мультфильм'),
        (4,'Триллер'),
        (5,'Документальный'),
        (6,'Боевик');

MERGE INTO PUBLIC.STATUS
    (STATUS_ID, NAME)
    KEY(STATUS_ID)
    VALUES
        (0,'UNCONFIRMED'),
        (1,'CONFIRMED');