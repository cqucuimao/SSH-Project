update carservicesupertype set superTitle=title;
alter table carservicesupertype drop column title;
insert into privilege(id,name,url,parent_id) values(435,'','/priceTable_deleteCarServiceSuperType',360);
insert into privilege(id,name,url,parent_id) values(436,'','/priceTable_deleteCarServiceType',360);
insert into privilege(id,name,url,parent_id) values(437,'','/priceTable_detail',360);
insert into privilege(id,name,url,parent_id) values(438,'','/priceTable_addUI',360);
insert into privilege(id,name,url,parent_id) values(439,'','/priceTable_edit',360);
insert into privilege(id,name,url,parent_id) values(440,'','/priceTable_add',360);
insert into privilege(id,name,url,parent_id) values(441,'','/priceTable_editUI',360);
insert into privilege(id,name,url,parent_id) values(442,'','/priceTable_delete',360);
insert into privilege(id,name,url,parent_id) values(443,'','/priceTable_getCarServiceTypeOption',360);

