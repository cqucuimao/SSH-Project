insert into privilege(id,name,url,parent_id) values(324,'登记外调车辆','/car_borrowedUI',107);
insert into privilege(id,name,url,parent_id) values(325,'登记外调车辆','/car_borrowed',107);

alter table car modify brand varchar(255) null;
alter table car modify plateType int null;
alter table car modify registDate datetime null;
alter table car modify seatNumber int null;
alter table car modify VIN varchar(255) null;


insert into role_privilege(roles_id,privileges_id)
values (1,108),
(1,109),
(1,313),
(1,320),
(1,321),
(1,324),
(1,325);