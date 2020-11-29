create table card
(
    id           varchar(255)     not null
        constraint card_pkey primary key,
    card_type    varchar(255),
    damage       DOUBLE PRECISION not null,
    weakness     DOUBLE PRECISION,
    element_type varchar(255),
    name         varchar(255)
);

create table bundle
(
    id varchar(255) not null
        constraint bundle_pkey primary key
);


create table bundle_card
(
    bundle_id varchar(255) not null
        constraint fkplvc2a9jrvp4ocjw1dvynoa06
            references bundle on delete cascade,
    card_id   varchar(255) not null
        constraint uk_3gfs03j1evhkapeolruvt3hox
            unique
        constraint fkhmyts8ihb23kfv53iler73vl
            references card on delete cascade
);

create table player
(
    id                  varchar(255) not null
        constraint player_pkey primary key,
    number_of_coins     integer      not null,
    password            varchar(255) not null,
    username            varchar(255) unique,
    bio                 varchar(255),
    image               varchar(255),
    numberOfGamesPlayed integer,
    elo                 integer
);


create table player_card
(
    player_id varchar(255) not null
        constraint fkde21dwj10h5g5bfd1m4sdxiw9
            references player on delete cascade,
    card_id   varchar(255) not null
        constraint uk_eu14jwktj3wqbooslor0xd0p0
            unique
        constraint fkqaek75wpycyve467ss52ve2u8
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

create table trading_deal
(
    id             serial primary key,
    card_id        varchar(255) references card (id),
    card_type      varchar(255),
    minimum_damage integer,
    creator_id     varchar(255) references player (id)
);


