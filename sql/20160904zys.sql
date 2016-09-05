insert into privilege(id,url) values(340,'/carWash_editNormalInfo');
insert into privilege(id,url) values(341,'/carWash_editKeyInfo');
insert into role_privilege(roles_id,privileges_id) values(4,341);
insert into role_privilege(roles_id,privileges_id) values(14,340);
delete from role_noprivilege where role_id='14' and privilege_id='220';
delete from role_noprivilege where role_id='14' and privilege_id='216';