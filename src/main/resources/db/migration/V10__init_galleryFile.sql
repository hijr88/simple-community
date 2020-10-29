drop table if exists gallery_file CASCADE;
drop sequence if exists gallery_file_seq;

create sequence gallery_file_seq start with 1 increment by 1;
create table gallery_file (
       gallery_file_id bigint               not null,
       gallery_id bigint                    not null ,
       file_name varchar(1000)              not null,
       original_file_name varchar(1000)     not null ,
       file_size integer                    not null,
       primary key (gallery_file_id)
);

alter table gallery_file
add constraint fk_gallery_file_gallery_id
foreign key (gallery_id) references gallery;