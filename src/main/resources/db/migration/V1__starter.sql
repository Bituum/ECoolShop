create table goods
(
    goods_id          serial,
    price             int          not null,
    photo_path        varchar(255) not null,
    characteristic_id int          not null
);
create table description
(
    description_id serial,
    description    varchar(500) not null
);
alter table description
    add constraint description_goods_goods_id_fk
        foreign key (description_id) references goods;

create table characteristic
(
    characteristic_id int          not null
        constraint characteristic_pk
            primary key
        constraint characteristic_goods_goods_id_fk
            references goods,
    characteristic    varchar(255) not null
);

create table "user"
(
    user_id       serial,
    username      varchar(255) not null,
    email         varchar(50)  not null,
    user_password varchar(100) not null,
    role_id       int          not null
);

create unique index user_user_id_uindex
    on "user" (user_id);

alter table "user"
    add constraint user_pk
        primary key (user_id);

create table role
(
    role_id serial,
    role    varchar(10) not null
);

create table user_role
(
    user_role_id serial,
    user_id      int not null,
    role_id      int not null
        constraint user_role_role_role_id_fk
            references role
);

create unique index user_role_user_role_id_uindex
    on user_role (user_role_id);

alter table user_role
    add constraint user_role_user_user_id_fk
        foreign key (user_id) references "user";

alter table "user"
    add birthday date not null;