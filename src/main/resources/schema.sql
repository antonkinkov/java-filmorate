drop TABLE IF EXISTS films CASCADE;
drop TABLE IF EXISTS users CASCADE;
drop TABLE IF EXISTS genres CASCADE;
drop TABLE IF EXISTS mpa_ratings CASCADE;
drop TABLE IF EXISTS films_likes CASCADE;
drop TABLE IF EXISTS user_friends CASCADE;
drop TABLE IF EXISTS films_genres CASCADE;

create table if not exists mpa_ratings (
	id LONG auto_increment primary key,
	name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
	id LONG auto_increment PRIMARY KEY,
	name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
	id LONG auto_increment PRIMARY KEY,
	name varchar(255) NOT NULL,
	description varchar(255) not null,
	release_date date not null,
	duration integer,
	mpa_id LONG references mpa_ratings(id) on delete restrict
);

create table if not exists films_genres (
	film_id LONG references films (id) on delete cascade,
    genre_id LONG references genres (id) on delete cascade,
    primary key (film_id, genre_id)
);

create table if not exists users (
	id LONG auto_increment primary key,
	login varchar(255) not null,
	email varchar(255) not null,
	name varchar(255),
	birthday timestamp
);

create unique index if not exists users_email_uindex on users(email);

create unique index if not exists users_login_uindex on users(login);

create table if not exists user_friends (
	user_id LONG references users (id) on delete cascade,
    friend_id LONG  references users (id) on delete cascade,
	status boolean not null default 0,
	primary key (user_id, friend_id)
);

create table if not exists films_likes (
	film_id LONG references films (id) on delete cascade,
    user_id LONG  references users (id) on delete cascade,
	primary key (user_id, film_id)
);

