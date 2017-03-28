insert into privilege(id,name,url) values(474,'修改车辆默认司机','/car_editDefaultDriver');
insert into role_privilege(roles_id,privileges_id) values(1,474);
insert into privilege(id,name,url,parent_id) values(475,'','/car_editDefaultDriverUI',474);