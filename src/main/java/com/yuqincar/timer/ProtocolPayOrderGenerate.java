package com.yuqincar.timer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.ProtocolOrderPayOrderService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Component
public class ProtocolPayOrderGenerate {
	@Autowired
	public OrderService orderService;
	
	@Autowired
	public ProtocolOrderPayOrderService popoService;
	
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void generateProtocolPayOrder(){
		QueryHelper helper=new QueryHelper(Order.class,"o");
		helper.addWhereCondition("o.chargeMode=? && (o.status=? || o.status=? || o.status=? || o.status=? || o.status=?)", 
				ChargeModeEnum.PROTOCOL,OrderStatusEnum.SCHEDULED,OrderStatusEnum.ACCEPTED,OrderStatusEnum.BEGIN,OrderStatusEnum.GETON,OrderStatusEnum.GETOFF);
		for(Order order:orderService.queryAllOrder(helper)){
			if(order.getPlanEndDate().before(order.getLastPayDate()))
				continue;
			if(DateUtils.getYMDString(order.getNextPayDate()).equals(DateUtils.getYMDString(new Date()))){
				ProtocolOrderPayOrder popo=new ProtocolOrderPayOrder();
				popo.setOrder(order);
				popo.setFromDate(order.getLastPayDate());
				popo.setToDate(order.getNextPayDate());
				popo.setMoney(order.getMoneyForPeriodPay());
				popo.setPaid(false);
				popo.setCompany(order.getCompany());
				popoService.saveProtocolOrderPayOrder(popo);
				
				int period=0;
				switch(order.getPayPeriod()){
				case MONTH:
					period=DateUtils.PERIOD_MONTH;
					break;
				case QUARTER:
					period=DateUtils.PERIOD_QUARTER;
					break;
				case YEAR:
					period=DateUtils.PERIOD_YEAR;
					break;
				}
				Date nextPayDate=DateUtils.getPeriodDate(order.getNextPayDate(), period);
				order.setLastPayDate(new Date());
				order.setNextPayDate(nextPayDate);
				orderService.update(order);
			}
		}
	}
}
