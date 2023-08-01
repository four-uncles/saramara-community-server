/*
create table attach
(
    attach_id  bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    deleted_at datetime(6)  null,
    updated_at datetime(6)  null,
    ids        bigint       not null,
    img_path   varchar(255) not null,
    seq        bigint       not null,
    type       varchar(255) null
);

create table member_image
(
    member_image_id bigint auto_increment
        primary key,
    image_name      varchar(255) not null,
    path            varchar(255) not null,
    uuid            varchar(255) not null
);

create table member
(
    member_id                    bigint auto_increment
        primary key,
    created_at                   datetime(6)  null,
    deleted_at                   datetime(6)  null,
    updated_at                   datetime(6)  null,
    email                        varchar(100) not null,
    nickname                     varchar(10)  null,
    password                     varchar(100) null,
    type                         varchar(255) not null,
    member_image_member_image_id bigint       null,
    constraint UK_hh9kg6jti4n1eoiertn2k6qsc
        unique (nickname),
    constraint UK_mbmcqelty0fbrvxp1q58dn57t
        unique (email),
    constraint FKeu9iks9lqcab4ofejthe2dfat
        foreign key (member_image_member_image_id) references member_image (member_image_id)
);

create table board
(
    board_id       bigint auto_increment
        primary key,
    created_at     datetime(6)   null,
    deleted_at     datetime(6)   null,
    updated_at     datetime(6)   null,
    board_cnt      int default 0 not null,
    category_board varchar(255)  not null,
    content        tinytext      null,
    dead_line      datetime(6)   null,
    like_cnt       int default 0 not null,
    title          varchar(100)  not null,
    member_id      bigint        null,
    constraint FKsds8ox89wwf6aihinar49rmfy
        foreign key (member_id) references member (member_id)
);

create table comment
(
    comment_id bigint auto_increment
        primary key,
    created_at datetime(6) null,
    deleted_at datetime(6) null,
    updated_at datetime(6) null,
    content    tinytext    not null,
    pick       bigint      null,
    board_id   bigint      null,
    member_id  bigint      null,
    constraint FKlij9oor1nav89jeat35s6kbp1
        foreign key (board_id) references board (board_id),
    constraint FKmrrrpi513ssu63i2783jyiv9m
        foreign key (member_id) references member (member_id)
);

create table member_role
(
    member_member_id bigint       not null,
    role             varchar(255) not null,
    primary key (member_member_id, role),
    constraint FKm0p1u26rvyjjh2e4x0c9cg6a1
        foreign key (member_member_id) references member (member_id)
);
*/
