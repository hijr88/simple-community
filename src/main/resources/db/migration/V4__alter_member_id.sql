drop sequence if exists member_seq;

alter table member
drop column member_id;

alter table if exists member
    drop constraint UK_member_username;

alter table member
drop column username;

alter table member
add member_id varchar(50) primary key;