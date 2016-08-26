insert into privilege(id,name,url,parent_id) values(328,'','/user_addUI',11);
insert into role(id,name) values(20,'人力资源管理员');

insert into role_privilege(roles_id,privileges_id) values(20,11);