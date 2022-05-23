create table cart
(
    cart_id        serial
        constraint cart_pk
            primary key,
    total_price    integer,
    count_of_goods integer
);


create table cart_goods
(
    cart_goods_id serial
        constraint cart_goods_pk
            primary key,
    cart_id integer
        constraint cart_goods_cart_cart_id_fk
            references cart,
    goods_id      integer not null
        constraint cart_goods_goods_goods_id_fk
            references goods
);


create unique index cart_goods_cart_goods_id_uindex
    on cart_goods (cart_goods_id);

