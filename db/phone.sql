create table phone
(
    id    integer primary key autoincrement,
    name  varchar(100) not null,
    brand varchar(100) not null,
    price float(100)   not null
);

insert into phone
    (name, brand, price)
values ('P30', '华为', 3333),
       ('IphoneX', '苹果', 4444);


select *
from phone;