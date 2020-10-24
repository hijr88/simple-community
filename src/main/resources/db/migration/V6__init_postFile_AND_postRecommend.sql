drop table if exists post_file CASCADE;
drop sequence if exists post_file_seq;

create sequence post_file_seq start with 1 increment by 1;
create table post_file (
       post_file_id bigint              not null,
       post_id bigint                   not null ,
       file_name varchar(1000)          not null,
       original_file_name varchar(1000) not null ,
       file_size integer                not null,
       primary key (post_file_id)
);

alter table post_file
add constraint fk_post_file_post_id foreign key (post_id) references post;

drop table if exists post_recommend CASCADE;
create table post_recommend (
        member_id varchar(50)   not null,
        post_id bigint          not null,
        primary key (member_id, post_id)
);
alter table post_recommend
add constraint FK_post_recommend_member_id foreign key(member_id) references member;

alter table post_recommend
add constraint FK_post_recommend_post_id foreign key (post_id) references post;