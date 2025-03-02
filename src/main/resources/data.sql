insert into MPA_RATINGS (name)
select 'G' from dual
where not exists (select 1 from MPA_RATINGS where name = 'G');

insert into MPA_RATINGS (name)
select 'PG' from dual
where not exists (select 1 from MPA_RATINGS where name = 'PG');

insert into MPA_RATINGS (name)
select 'PG-13' from dual
where not exists (select 1 from MPA_RATINGS where name = 'PG-13');

insert into MPA_RATINGS (name)
select 'R' from dual
where not exists (select 1 from MPA_RATINGS where name = 'R');

insert into MPA_RATINGS (name)
select 'NC-17' from dual
where not exists (select 1 from MPA_RATINGS where name = 'NC-17');

insert into GENRES (name)
select 'Комедия' from dual
where not exists (select 1 from GENRES where LOWER(name) = 'комедия');

insert into GENRES (name)
select 'Драма' from dual
where not exists (select 1 from GENRES where LOWER(name) = 'драма');

insert into GENRES (name)
select 'Мультфильм' from dual
where not exists (select 1 from GENRES where LOWER(name) = 'мультфильм');

insert into GENRES (name)
select 'Триллер' from dual
where not exists (select 1 from GENRES where LOWER(name) = 'триллер');

insert into GENRES (name)
select 'Документальный' from dual
where not exists (select 1 from GENRES where LOWER(name) = 'документальный');

insert into GENRES (name)
select 'Боевик' from dual
where not exists (select 1 from GENRES where LOWER(name) = 'боевик');