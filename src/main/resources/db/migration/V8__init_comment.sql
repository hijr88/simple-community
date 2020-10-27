drop table if exists comment CASCADE;
drop sequence if exists comment_seq;

create sequence comment_seq start with 1 increment by 1;

create table comment (
     comment_id bigint not null,
     post_id    bigint not null,
     content varchar(1000) not null,
     member_id varchar(50),
     create_date timestamp default now(),
     parent bigint not null default 0,
     delete boolean default '0',
     primary key (comment_id),
     foreign key (post_id) references post(post_id) on delete cascade,
     foreign key (member_id) references member(member_id) on delete cascade
)