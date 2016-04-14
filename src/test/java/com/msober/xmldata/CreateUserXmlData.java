package com.msober.xmldata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.msober.base.BaseTest;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.receipt.ReceiptService;
import com.yuqincar.utils.TestUtils;

public class CreateUserXmlData extends BaseTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private CustomerOrganizationService customerOrganizationService;

    @Test
    public void createUserInsertData() throws FileNotFoundException, SQLException,
                                       DatabaseUnitException, IOException {
        /*List<String> tables = new ArrayList<String>();

        tables.add("car");
        tables.add("carcare");
        tables.add("carexamine");
        tables.add("carrepair");
        tables.add("order_");
        TestUtils.exportData(dataSource.getConnection(), tables, "D:\\getCarTask_inserts.xml");*/
    	
    	OrderStatement orderStatement=new OrderStatement();
    	orderStatement.setName("安监局20160101");
    	orderStatement.setDate(new Date());
    	orderStatement.setFromDate(new Date());
    	orderStatement.setToDate(new Date());
    	orderStatement.setCustomerOrganization(customerOrganizationService.getById(4L));
    	orderStatement.setOrderNum(3);
    	orderStatement.setTotalMoney(new BigDecimal("66000.23"));
    	
    	//receiptService.save(orderStatement);
    }
}


