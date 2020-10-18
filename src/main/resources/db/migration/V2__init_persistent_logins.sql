drop table if exists persistent_logins cascade;

create table persistent_logins (
   username varchar(50) not null,
   series varchar(64) not null,
   token varchar(64) not null,
   last_used varchar(64) not null,
   primary key (series)
)
