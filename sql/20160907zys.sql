insert into privilege(id,url) values(342,'/materialReceive_editNormalInfo');
insert into privilege(id,url) values(343,'/materialReceive_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,343);
insert into role_privilege(roles_id,privileges_id) values(13,342);
delete from role_noprivilege where role_id='13' and privilege_id='208';

insert into privilege(id,url) values(344,'/tollCharge_editNormalInfo');
insert into privilege(id,url) values(345,'/tollCharge_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,345);
insert into role_privilege(roles_id,privileges_id) values(12,344);
delete from role_noprivilege where role_id='12' and privilege_id='198';
delete from role_noprivilege where role_id='12' and privilege_id='203';


insert into privilege(id,url) values(346,'/carRefuel_editNormalInfo');
insert into privilege(id,url) values(347,'/carRefuel_editKeyInfo');
insert into privilege(id,url,parent_id) values(348,'/carRefuel_delete',171);
insert into privilege(id,url,parent_id) values(349,'/carRefuel_edit',171);
insert into role_privilege(roles_id,privileges_id) values(4,347);
insert into role_privilege(roles_id,privileges_id) values(10,346);
insert into role_privilege(roles_id,privileges_id)
values
(4,172),
(4,349),
(4,173),
(4,348),
(4,176);
insert into role_noprivilege(role_id,privilege_id) values (10,348);