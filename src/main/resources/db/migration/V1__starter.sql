create table goods
(
    goods_id       serial
        constraint goods_pk
            primary key,
    price          integer      not null,
    photo_path     varchar(255),
    nomination     varchar(255) not null,
    description_id integer
);

alter table goods
    owner to dummy;

create unique index goods_goods_id_uindex
    on goods (goods_id);

create unique index goods_nomination_uindex
    on goods (nomination);

create table description
(
    description_id serial
        constraint description_pk
            primary key
        constraint description_goods_goods_id_fk
            references goods,
    description    varchar(500) not null,
    characteristic varchar(500) not null
);

alter table description
    owner to dummy;

alter table goods
    add constraint goods_description_description_id_fk
        foreign key (description_id) references description;

create unique index description_description_id_uindex
    on description (description_id);

create table db_roles
(
    role_id serial
        constraint role_pk
            primary key,
    role    varchar(20) not null
);

alter table db_roles
    owner to dummy;

create unique index role_role_id_uindex
    on db_roles (role_id);

create table user_registration_apply
(
    user_apply_id serial
        constraint user_apply_pk
            primary key,
    expired_at    timestamp    not null,
    username      varchar(255) not null,
    created_at    timestamp,
    digit_code    varchar(6)   not null
);

alter table user_registration_apply
    owner to dummy;

create unique index user_apply_user_apply_id_uindex
    on user_registration_apply (user_apply_id);

create table cart
(
    cart_id        serial
        constraint cart_pk
            primary key,
    total_price    integer,
    count_of_goods integer
);

alter table cart
    owner to dummy;

create table custom_user
(
    user_id       serial
        constraint user_pk
            primary key,
    username      varchar(255) not null,
    email         varchar(50)  not null,
    user_password varchar(100) not null,
    birthday      date         not null,
    cart_id       integer
        constraint custom_user_cart_cart_id_fk
            references cart
);

alter table custom_user
    owner to dummy;

create table user_role
(
    user_role_id serial
        constraint user_role_pk
            primary key,
    user_id      integer not null
        constraint user_role_user_user_id_fk
            references custom_user,
    role_id      integer not null
        constraint user_role_role_role_id_fk
            references db_roles
);

alter table user_role
    owner to dummy;

create unique index user_role_user_role_id_uindex
    on user_role (user_role_id);

create table cart_goods
(
    cart_goods_id serial
        constraint cart_goods_pk
            primary key,
    cart_id       integer
        constraint cart_goods_cart_cart_id_fk
            references cart,
    goods_id      integer not null
        constraint cart_goods_goods_goods_id_fk
            references goods
);

alter table cart_goods
    owner to dummy;

create unique index cart_goods_cart_goods_id_uindex
    on cart_goods (cart_goods_id);

