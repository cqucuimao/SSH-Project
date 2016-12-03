update privilege set parent_id=395 where id=360;
insert into role_privilege(roles_id,privileges_id) values(22,360);
insert into role_privilege(roles_id,privileges_id) values(17,444);
/*将“运营科领导”角色配置给张信春*/
/*将“技术保障科领导”角色配置给徐斌*/
/*新建一个大类“其它”，将目前的小类“其它”，通过数据库改到大类中。*/