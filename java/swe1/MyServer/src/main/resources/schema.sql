create table card
(
    id           varchar(255) not null primary key,
    card_type    varchar(255),
    damage       DOUBLE PRECISION not null,
    weakness     DOUBLE PRECISION,
    element_type varchar(255),
    name         varchar(255)
);

create table bundle
(
    id varchar(255) not null primary key
);


create table bundle_card
(
    bundle_id varchar(255) not null
        references bundle on delete cascade,
    card_id   varchar(255) not null unique references card on delete cascade
);

create table player
(
    id              varchar(255) not null
        constraint player_pkey primary key,
    number_of_coins integer      not null,
    password        varchar(255) not null,
    username        varchar(255) unique,
    name            varchar(255),
    bio             varchar(255),
    image           varchar(255),
    games_played    integer,
    elo             integer
);


create table player_card
(
    player_id varchar(255) not null
        references player on delete cascade,
    card_id   varchar(255) not null
        references card on delete cascade
);

create table player_deck_card
(
    player_id varchar(255) not null
        references player on delete cascade,
    card_id   varchar(255) not null
        unique
        references card on delete cascade
);

create table player_locked_card
(
    player_id varchar(255) not null
        references player on delete cascade,
    card_id   varchar(255) not null
        unique
        references card on delete cascade
);

create table trading_deal
(
    id             varchar(255) primary key,
    card_id        varchar(255) references card (id) on delete cascade,
    card_type      varchar(255),
    minimum_damage DOUBLE PRECISION,
    creator_id     varchar(255) references player (id) on delete cascade
);


