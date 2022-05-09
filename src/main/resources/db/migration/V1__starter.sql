/*starter sql script*/

create table goods
(
    goods_id   serial
        constraint goods_pk
            primary key,
    price      integer      not null,
    photo_path varchar(255),
    nomination varchar(255) not null
);



create unique index goods_goods_id_uindex
    on goods (goods_id);

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



create unique index description_description_id_uindex
    on description (description_id);

create table custom_user
(
    user_id       integer default nextval('user_user_id_seq'::regclass) not null
        constraint user_pk
            primary key,
    username      varchar(255)                                          not null,
    email         varchar(50)                                           not null,
    user_password varchar(100)                                          not null,
    birthday      date                                                  not null
);



create unique index user_user_id_uindex
    on custom_user (user_id);

create table db_roles
(
    role_id integer default nextval('role_role_id_seq'::regclass) not null
        constraint role_pk
            primary key,
    role    varchar(20)                                           not null
);


create unique index role_role_id_uindex
    on db_roles (role_id);

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

create unique index user_role_user_role_id_uindex
    on user_role (user_role_id);

create table cart
(
    cart_id        serial
        constraint cart_pk
            primary key
        constraint cart_custom_user_user_id_fk
            references custom_user,
    total_price    integer,
    count_of_goods integer
);



create unique index cart_cart_id_uindex
    on cart (cart_id);

create table cart_goods
(
    cart_goods_id serial
        constraint cart_goods_pk
            primary key,
    cart_id       integer not null
        constraint cart_goods_cart_cart_id_fk
            references cart,
    goods_id      integer not null
        constraint cart_goods_goods_goods_id_fk
            references goods
);



create unique index cart_goods_cart_goods_id_uindex
    on cart_goods (cart_goods_id);

