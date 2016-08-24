alter table carrepair drop payDate;
alter table carinsurance drop payDate;
alter table carinsurance drop compulsoryCoverageMoney;

insert into privilege(id,name,url,parent_id) values(326,'添加商业保险','/carInsurance_addCommercialInsuranceUI',144);
insert into privilege(id,name,url,parent_id) values(327,'','/carInsurance_addCommercialInsurance',144);
insert into privilege(id,name,url,parent_id) values(328,'外派员工','/user_dispatchUserList',1);
insert into privilege(id,name,url,parent_id) values(329,'','/user_addDispatchUI',328);
insert into privilege(id,name,url,parent_id) values(330,'','/user_addDispatchUser',328);
insert into privilege(id,name,url,parent_id) values(331,'','/user_queryDispatchList',328);
insert into privilege(id,name,url,parent_id) values(332,'','/user_editDispatchUI',328);
insert into privilege(id,name,url,parent_id) values(333,'','/user_editDispatchUser',328);
insert into privilege(id,name,url,parent_id) values(334,'','/user_deleteDispatch',328);