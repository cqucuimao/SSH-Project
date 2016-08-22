insert into privilege(id,name,url,parent_id) values(320,'车辆详细查询页面','/car_queryUI',107);
insert into privilege(id,name,url,parent_id) values(321,'车辆详细查询','/car_moreQuery',107);
insert into privilege(id,name,url,parent_id) values(322,'订单详细查询页面','/order_queryUI',39);
insert into privilege(id,name,url,parent_id) values(323,'订单详细查询','/order_moreQuery',39);

insert into role_privilege(roles_id,privileges_id) values (18,320),(18,321)