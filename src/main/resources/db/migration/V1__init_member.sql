drop table if exists member cascade;
drop sequence if exists member_seq;

create sequence member_seq start 1 increment 1;

create table member (
    member_id int8 not null,
    username varchar(50) not null,
    password varchar(100) not null,
    nickname varchar(50) not null,
    email varchar(300) not null,
    zone_code varchar(30),
    address varchar(300),
    extra_address varchar(300),
    detail_address varchar(500),
    create_date timestamp not null default now(),
    profile_image varchar(500),
    enable boolean not null default '1',
    role varchar(50) not null default 'ROLE_USER',
    primary key (member_id)
);

alter table if exists member
    add constraint UK_member_username unique (username);
alter table if exists member
    add constraint UK_member_email unique (email);
