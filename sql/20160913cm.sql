insert into privilege(id,name,url,parent_id) values(360,'价格表','/priceTable_list',106);
insert into role_privilege(roles_id,privileges_id)
values (15,360);
insert into privilege(id,name,url,parent_id) values(361,'价格表','/priceTable_serviceTypeList',360);
insert into privilege(id,name,url,parent_id) values(362,'','/priceTable_addServiceTypeUI',360);
insert into privilege(id,name,url,parent_id) values(363,'','/priceTable_addServiceType',360);
insert into privilege(id,name,url,parent_id) values(364,'','/priceTable_editServiceTypeUI',360);
insert into privilege(id,name,url,parent_id) values(365,'','/priceTable_editServiceType',360);

insert into privilege(id,name,url,parent_id) values(366,'保养预约','/carCareAppointment_list',132);
insert into privilege(id,name,url,parent_id) values(367,'','/carCareAppointment_addUI',132);
insert into privilege(id,name,url,parent_id) values(368,'','/carCareAppointment_add',132);
insert into privilege(id,name,url,parent_id) values(369,'','/carCareAppointment_editUI',132);
insert into privilege(id,name,url,parent_id) values(370,'','/carCareAppointment_edit',132);
insert into privilege(id,name,url,parent_id) values(371,'','/carCareAppointment_delete',132);
insert into privilege(id,name,url,parent_id) values(372,'','/carCareAppointment_queryForm',132);
insert into privilege(id,name,url,parent_id) values(373,'','/carCareAppointment_freshList',132);

insert into privilege(id,name,url,parent_id) values(374,'维修预约','/carRepairAppointment_list',160);
insert into privilege(id,name,url,parent_id) values(375,'','/carRepairAppointment_queryList',160);
insert into privilege(id,name,url,parent_id) values(376,'','/carRepairAppointment_freshList',160);
insert into privilege(id,name,url,parent_id) values(377,'','/carRepairAppointment_addUI',160);
insert into privilege(id,name,url,parent_id) values(378,'','/carRepairAppointment_add',160);
insert into privilege(id,name,url,parent_id) values(379,'','/carRepairAppointment_editUI',160);
insert into privilege(id,name,url,parent_id) values(380,'','/carRepairAppointment_edit',160);
insert into privilege(id,name,url,parent_id) values(381,'','/carRepairAppointment_delete',160);

insert into privilege(id,name,url,parent_id) values(382,'年审预约','/carExamineAppointment_list',181);
insert into privilege(id,name,url,parent_id) values(383,'','/carExamineAppointment_queryList',181);
insert into privilege(id,name,url,parent_id) values(384,'','/carExamineAppointment_freshList',181);
insert into privilege(id,name,url,parent_id) values(385,'','/carExamineAppointment_addUI',181);
insert into privilege(id,name,url,parent_id) values(386,'','/carExamineAppointment_add',181);
insert into privilege(id,name,url,parent_id) values(387,'','/carExamineAppointment_editUI',181);
insert into privilege(id,name,url,parent_id) values(388,'','/carExamineAppointment_edit',181);
insert into privilege(id,name,url,parent_id) values(389,'','/carExamineAppointment_delete',181);