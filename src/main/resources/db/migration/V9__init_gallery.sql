drop table if exists gallery CASCADE;
drop sequence if exists gallery_seq;

create sequence gallery_seq start with 1 increment by 1;
create table gallery (
     gallery_id bigint not null,
     title varchar(80) not null,
     writer varchar(50) not null,
     create_date timestamp,
     pub boolean default '1',
     primary key (gallery_id)
);

alter table gallery
add constraint FK_GALLERY_member_id
foreign key (writer) references member on delete cascade;