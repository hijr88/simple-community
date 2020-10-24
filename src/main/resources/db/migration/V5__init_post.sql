drop table if exists post CASCADE;
drop sequence if exists post_seq;

create sequence post_seq start with 1 increment by 1;

create table post (
   post_id bigint        not null,
   title varchar(80)     not null,
   content varchar(4000) not null,
   writer varchar(50)    not null,
   create_date timestamp not null default now(),
   update_date timestamp not null,
   hit bigint            not null default 0,
   parent bigint         not null default 0,
   group_no bigint       not null default 0,
   group_order int       not null default 0,
   dept int              not null default 0,
   pub boolean           not null default '1',
   primary key (post_id)
);

alter table post
add constraint FK_member_id
foreign key (writer) references member;