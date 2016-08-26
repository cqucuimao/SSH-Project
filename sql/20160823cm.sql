alter table carrepair drop payDate;
alter table carinsurance drop payDate;
alter table carinsurance drop compulsoryCoverageMoney;

insert into privilege(id,name,url,parent_id) values(329,'添加商业保险','/carInsurance_addCommercialInsuranceUI',144);
insert into privilege(id,name,url,parent_id) values(330,'','/carInsurance_addCommercialInsurance',144);
insert into privilege(id,name,url,parent_id) values(331,'外派员工','/user_dispatchUserList',1);
insert into privilege(id,name,url,parent_id) values(332,'','/user_addDispatchUI',331);
insert into privilege(id,name,url,parent_id) values(333,'','/user_addDispatchUser',331);
insert into privilege(id,name,url,parent_id) values(334,'','/user_queryDispatchList',331);
insert into privilege(id,name,url,parent_id) values(335,'','/user_editDispatchUI',331);
insert into privilege(id,name,url,parent_id) values(336,'','/user_editDispatchUser',331);
insert into privilege(id,name,url,parent_id) values(337,'','/user_deleteDispatch',331);

insert into role_privilege(roles_id,privileges_id) values(1,331);