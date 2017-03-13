/*package com.yuqincar.activiti.test;


import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;



@RunWith(SpringJUnit4ClassRunner.class)                     
@ContextConfiguration(locations="classpath:applicationContext.xml")  //Spring配置文件位置
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  //默认不回滚数据库
public abstract class BaseTest {
	@Autowired
	public DataSource dataSource;
	
}
*/