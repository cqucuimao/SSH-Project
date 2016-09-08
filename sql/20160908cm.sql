insert into privilege(id,url) values(350,'/carInsurance_editNormalInfo');
insert into privilege(id,url) values(351,'/carInsurance_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,351);
insert into role_privilege(roles_id,privileges_id) values(8,350);

insert into privilege(id,name,url,parent_id) values(352,'','/carInsurance_edit',144);

insert into role_privilege(roles_id,privileges_id)
values (4,145)
,(4,146)
,(4,149)
, (4,150)
, (4,151)
, (4,352);

delete from role_noprivilege where role_id='7' and privilege_id='136';
delete from role_noprivilege where role_id='7' and privilege_id='137';

insert into privilege(id,url) values(353,'/carCare_editNormalInfo');
insert into privilege(id,url) values(354,'/carCare_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,354);
insert into role_privilege(roles_id,privileges_id) values(7,353);

insert into privilege(id,url) values(355,'/carRepair_editNormalInfo');
insert into privilege(id,url) values(356,'/carRepair_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,356);
insert into role_privilege(roles_id,privileges_id) values(9,355);
delete from role_noprivilege where role_id='9' and privilege_id='165';
delete from role_noprivilege where role_id='9' and privilege_id='166';

insert into privilege(id,url) values(357,'/carExamine_editNormalInfo');
insert into privilege(id,url) values(358,'/carExamine_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,358);
insert into role_privilege(roles_id,privileges_id) values(11,357);
delete from role_noprivilege where role_id='11' and privilege_id='185';
delete from role_noprivilege where role_id='11' and privilege_id='186';

insert into privilege(id,url,parent_id) values(359,'/baiduAddress',68);
