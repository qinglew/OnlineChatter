create table user (
    phone char(11) primary key,
    username varchar(20),
    image int not null,
    password varchar(18) not null,
    description varchar(50)
);

