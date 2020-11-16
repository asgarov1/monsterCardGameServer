create sequence my_sequence;

alter sequence my_sequence owner to admin;

create table card
(
	id varchar(255) not null constraint card_pkey primary key,
	card_type varchar(255),
	damage integer not null,
	element_type varchar(255),
	name varchar(255)
);

alter table card owner to admin;

create table bundle
(
	id varchar(255) not null constraint bundle_pkey primary key
);

alter table bundle owner to admin;

create table bundle_cards
(
	bundle_id varchar(255) not null
		constraint fkplvc2a9jrvp4ocjw1dvynoa06
			references bundle on delete cascade,
	cards_id varchar(255) not null
		constraint uk_3gfs03j1evhkapeolruvt3hox
			unique
		constraint fkhmyts8ihb23kfv53iler73vl
			references card on delete cascade
);

alter table bundle_cards owner to admin;

create table player
(
	id varchar(255) not null constraint player_pkey primary key,
	number_of_coins integer not null,
	password varchar(255),
	username varchar(255)
);

alter table player owner to admin;

create table player_cards
(
	player_id varchar(255) not null
		constraint fkde21dwj10h5g5bfd1m4sdxiw9
			references player on delete cascade,
	cards_id varchar(255) not null
		constraint uk_eu14jwktj3wqbooslor0xd0p0
			unique
		constraint fkqaek75wpycyve467ss52ve2u8
			references card on delete cascade
);

alter table player_cards owner to admin;

