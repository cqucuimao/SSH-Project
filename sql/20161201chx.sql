drop table businessparameter_reservecarapplyorderapplyuser;
insert into businessparameter_reservecarapplyorderapproveuser(businessparameter,user) values(1,105);
insert into businessparameter_reservecarapplyordercarapproveuser(businessparameter,user) values(1,93);
insert into businessparameter_reservecarapplyorderdriverapproveuser(businessparameter,user) values(1,5);
delete from privilege where id=429;
insert into privilege(id,name,url,parent_id) values(446,'','/reserveCarApplyOrder_freshList',405);
update privilege set parent_id=18 where id=405;
/*将人力资源科领导配给卢娟*/