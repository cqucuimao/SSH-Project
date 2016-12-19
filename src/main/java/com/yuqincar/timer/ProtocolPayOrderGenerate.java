package com.yuqincar.timer;

import java.util.Date;
import java.util.List;

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
	
	@Scheduled(cron = "30 12 14 * * ?")
	@Transactional
	public void generateProtocolPayOrder(){
		QueryHelper helper=new QueryHelper("order_","o");
		helper.addWhereCondition("o.chargeMode=? and (o.status=? or o.status=? or o.status=? or o.status=? or o.status=?)", 
				ChargeModeEnum.PROTOCOL,OrderStatusEnum.SCHEDULED,OrderStatusEnum.ACCEPTED,OrderStatusEnum.BEGIN,OrderStatusEnum.GETON,OrderStatusEnum.GETOFF);
		List<Order> orders=orderService.queryAllOrder(helper);
		for(Order order:orders){
			if(order.getPayPeriod()==null || order.getFirstPayDate()==null || 
					order.getMoneyForPeriodPay()==null || order.getNextPayDate()==null)  //排除历史数据中没有设置周期收款信息的订单
				continue;
			if(order.getLastPayDate()!=null && order.getPlanEndDate().before(order.getLastPayDate()))
				continue;
			System.out.println("SN="+order.getSn());
			if(DateUtils.getMinDate(order.getNextPayDate()).before(DateUtils.getMaxDate(new Date()))){
				ProtocolOrderPayOrder popo=new ProtocolOrderPayOrder();
				popo.setOrder(order);
				popo.setFromDate(order.getLastPayDate()!=null ? order.getLastPayDate() : order.getPlanBeginDate());
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
