package com.yuqincar.action.receipt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.MoneyGatherInfo;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.OrderStatementStatusEnum;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.receipt.ReceiptService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class OrderStatementAction extends BaseAction implements ModelDriven<OrderStatement> {
	
	private OrderStatement model=new OrderStatement();
	//获取新增orderStatement的相应订单的id序列
	private String orderIds;
	//新增的orderStatement的名称
	private String orderStatementName;

	@Autowired
	ReceiptService receiptService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	private DiskFileService diskFileService;
	//导出PDF流
	private InputStream pdfStream;
	//PDF文件名
	private String fileName;
	//图片流
    private ByteArrayInputStream imageStream;
    private Long orderStatementId;
    private BigDecimal money;
    private Date date;
    private String memo;
	
	public String invoice(){
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		ActionContext.getContext().getValueStack().push(orderStatement);
		return "invoice";
	}
	
	public String invoiceDo(){
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		orderStatement.setInvoiceNumber(model.getInvoiceNumber());
		orderStatement.setInvoiceMoney(model.getInvoiceMoney());
		orderStatement.setInvoiceDate(model.getInvoiceDate());
		orderStatement.setStatus(OrderStatementStatusEnum.INVOICED);
		receiptService.updateOrderStatement(orderStatement);
		
		return newList();
	}
	
	public String gatherMoney(){
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		ActionContext.getContext().getValueStack().push(orderStatement);
		return "gatherMoney";
	}
	
	public String gatherMoneyDo(){
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		MoneyGatherInfo mgi=new MoneyGatherInfo();
		mgi.setOrderStatement(orderStatement);
		mgi.setMoney(money);
		mgi.setDate(date);
		mgi.setMemo(memo);
		receiptService.saveMoneyGatherInfo(mgi);
		return gatherMoney();
	}
	
	public String gatherComplete(){
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		receiptService.moneyGatherComplete(orderStatement);
		return invoicedDetail();
	}
	
	/**
	 * 取消对账单
	 */
	public void cancelOrderStatement(){
		receiptService.cancelOrderStatement(orderStatementId);
		this.writeJson("{\"status\":\"1\"}");
	}
	
	/**
	 * 对账单具体信息及相应操作页面
	 * @return
	 */
	public String newDetail(){		
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		//排除订单后，该对账单已经被删除，所以为null
		if(orderStatement!=null){
		   List<Order> orderList=orderStatement.getOrders();
		   ActionContext.getContext().put("orderList", orderList);
		   ActionContext.getContext().put("orderStatement", orderStatement);
		   return "newDetail";
		}else{
			//如果该对账单中所有订单都被排除，则该对账单被取消，返回到对账单列表页面
			return newList();
		}
		
	}
	
	public String invoicedDetail(){
		OrderStatement orderStatement=receiptService.getOrderStatementById(orderStatementId);
		ActionContext.getContext().getValueStack().push(orderStatement);
		return "invoicedDetail";
	}
	
	/**
	 * 取消对账单上的若干订单
	 */
	public void cancelOrders(){
		String[] idStrArray=orderIds.split(",");
		Long[] ids=new Long[idStrArray.length];
		for(int i=0;i<idStrArray.length;i++){
			ids[i]=Long.parseLong(idStrArray[i]);
		}
		receiptService.excludeOrdersFromOrderStatement(orderStatementId,ids);
		this.writeJson("{\"status\":\"1\"}");
	}
	
	/**
	 * 显示未付款orderStatement数据
	 * @return
	 */
	public String newList(){		
		List<OrderStatement> orderStatementList=receiptService.getAllNewOrderStatement();
		ActionContext.getContext().put("orderStatementList", orderStatementList);
		return "newList";
	}
	
	public String invoicedList(){		
		List<OrderStatement> orderStatementList=receiptService.getAllInvoicedOrderStatement();
		ActionContext.getContext().put("orderStatementList", orderStatementList);
		return "invoicedList";
	}
	
	public void isOrderStatementExist(){
		boolean result=receiptService.isOrderStatementExist(orderStatementName);
		if(result)
			this.writeJson("{\"status\":\"1\"}");
		else
			this.writeJson("{\"status\":\"0\"}");
	}
	
	public String paidList() {
	    QueryHelper helper = new QueryHelper(OrderStatement.class, "os");
		helper.addWhereCondition("os.status=?", OrderStatementStatusEnum.PAID);
		PageBean<OrderStatement> pageBean = receiptService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("orderStatementHelper", helper);
		return "paidList";
	}
	
	public String freshPaidList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("orderStatementHelper");
		PageBean<OrderStatement> pageBean = receiptService.getPageBean(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String paidListQueryForm(){
	    QueryHelper helper = new QueryHelper(OrderStatement.class, "os");
		helper.addWhereCondition("os.status=?", OrderStatementStatusEnum.PAID);
		//设置单位名称
	    if(model.getCustomerOrganization()!=null&&!"".equals(model.getCustomerOrganization().getName())){
	    	helper.addWhereCondition("os.customerOrganization.name=?", model.getCustomerOrganization().getName());
	    }
	    //设置开始时间
	    if(model.getFromDate()!=null&&!"".equals(model.getFromDate())){
	    	//将开始时间设置成当天的00:00:00
	    	Date newFromDate=DateUtils.getMinDate(model.getFromDate());
	    	helper.addWhereCondition("os.fromDate>=?", newFromDate);
	    }
	    //设置结束时间
	    if(model.getToDate()!=null&&!"".equals(model.getToDate())){
	    	//将结束事件设置成当天的11:59:59
	    	Date newToDate=DateUtils.getMaxDate(model.getToDate());
	    	helper.addWhereCondition("os.toDate<=?", newToDate);
	    }
	    PageBean<OrderStatement> pageBean = receiptService.getPageBean(pageNum, helper);
	    ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("orderStatementHelper", helper);
	    
		return "paidList";
	}

	/**
	 * 返回页面订单名称的下拉列表json数据
	 */
	public void orderStatementList(){
		List<OrderStatement> orderStatements=receiptService.getAllNewOrderStatement();
		List<String> orderStatementNames=new ArrayList<String>(orderStatements.size());
		for(int i=0;i<orderStatements.size();i++){
			orderStatementNames.add(orderStatements.get(i).getName());
		}
	    this.writeJson(JSON.toJSONString(orderStatementNames));
		
	}
	
	//新增对账单
	public void newOrderStatement(){
		String[] idStrArray=orderIds.split(",");
		Long[] ids=new Long[idStrArray.length];
		for(int i=0;i<idStrArray.length;i++){
			ids[i]=Long.parseLong(idStrArray[i]);
		}
		receiptService.newOrderStatementByOrderIds(orderStatementName,ids);
		this.writeJson("{\"status\":\"1\"}");
	}
	
	//添加对账单
	public void addOrderStatement(){
		String[] idStrArray=orderIds.split(",");
		Long[] ids=new Long[idStrArray.length];
		for(int i=0;i<idStrArray.length;i++){
			ids[i]=Long.parseLong(idStrArray[i]);
		}
		receiptService.addOrderStatementByOrderIds(orderStatementName,ids);
		this.writeJson("{\"status\":\"1\"}");
	}
	//导出对账单 生成PDF
	public String generatePDF() throws Exception{
		
	   OrderStatement orderStatement = receiptService.getOrderStatementById(model.getId());
	   List<Order> orders = receiptService.getOrderStatementById(model.getId()).getOrders();
	   SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间格式
	   SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");//时间格式
	   DecimalFormat df = new DecimalFormat("###");//浮点数显示格式
	   DecimalFormat df1 = new DecimalFormat("0.0");//浮点数显示格式
	   String tempName = orderStatement.getName()+".pdf";
	   fileName = new String(tempName.getBytes("GB2312"), "ISO8859-1");
	   System.out.println("fileName="+fileName);
	   //手动画表格
	   Document doc = new Document (PageSize.A4);
	   ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	   PdfWriter.getInstance (doc, buffer);
	   
	   doc.setMargins(10, 10, 40, 20);
	   doc.open (); 
	   //标题字体
	   BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	   Font topFont = new Font(baseFont, 28, Font.BOLD);
	   Font titleFont = new Font(baseFont, 20, Font.BOLD);
       //内容字体
	   Font font = new Font(baseFont, 10, Font.NORMAL);
	   //首页内容
	   doc.newPage();
	   Paragraph t = new Paragraph("\n渝勤公司对账单",topFont);
	   t.setAlignment(1);
	   doc.add(t);
	   Paragraph p = new Paragraph("\n\n",titleFont);
	   doc.add(p);
	   Paragraph customerOrganization = new Paragraph("单位："+orderStatement.getCustomerOrganization().getName(),titleFont);
	   customerOrganization.setIndentationLeft(60); 
	   doc.add(customerOrganization);
	   doc.add(Chunk.NEWLINE);
	   String fromDate = sdf1.format(orderStatement.getFromDate());
	   String toDate = sdf1.format(orderStatement.getToDate());
	   Paragraph beginAndEndDate = new Paragraph("起止时间："+fromDate+" — "+toDate,titleFont);
	   beginAndEndDate.setIndentationLeft(60);
	   doc.add(beginAndEndDate);
	   doc.add(Chunk.NEWLINE);
	   Paragraph orderNum = new Paragraph("订单数："+orderStatement.getOrderNum(),titleFont);
	   orderNum.setIndentationLeft(60);
	   doc.add(orderNum);
	   doc.add(Chunk.NEWLINE);
	   String money = df.format(orderStatement.getTotalMoney());
	   Paragraph totalMoney = new Paragraph("总金额："+money,titleFont);
	   totalMoney.setIndentationLeft(60);
	   doc.add(totalMoney);
	   doc.add(Chunk.NEWLINE);
	   String date = sdf2.format(orderStatement.getDate());
	   Paragraph orderDate = new Paragraph("订单生成日期："+date,titleFont);
	   orderDate.setIndentationLeft(60);
	   doc.add(orderDate);
	   for(int k=0;k<orders.size();k++)
	   {
		   //设置派车单标题
		   doc.newPage();
		   Paragraph titleParagragh = new Paragraph("重庆市渝勤汽车服务有限公司派车单\n\n",titleFont);
		   titleParagragh.setAlignment(titleParagragh.ALIGN_CENTER);
		   doc.add(titleParagragh);
		   /*****生成8列的表格*****/
		   //表格第0行
		   PdfPTable table = new PdfPTable (8);
		   table.setTotalWidth(670f);
		   table.setWidths(new float[]{90f,80f,100f,70f,100f,70f,80f,80f});
		   
		   PdfPCell cell = new PdfPCell ();
		   cell.setBorder(0);
		   cell.setColspan (6);
		   table.addCell (cell);		   
		   cell = new PdfPCell (new Paragraph ("渝勤运"+orders.get(k).getSn(),font));
		   cell.setColspan(2);
		   cell.setBorder(0);
		   table.addCell (cell);
		   //表格第1行
		   cell = new PdfPCell(new Paragraph ("用车单位",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph (orders.get(k).getCustomerOrganization().getAbbreviation(),font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell(new Paragraph ("联系电话",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph (orders.get(k).getCustomer().getName()+"："+orders.get(k).getPhone(),font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   //表格第2行
		   cell = new PdfPCell(new Paragraph ("定车时间",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   String scheduleTime = sdf1.format(orders.get(k).getScheduleTime());
		   cell = new PdfPCell (new Paragraph (scheduleTime,font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell(new Paragraph ("上车地点",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph (orders.get(k).getFromAddress(),font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   //表格第3行
		   cell = new PdfPCell(new Paragraph ("车型",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph (orders.get(k).getServiceType().getTitle(),font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell(new Paragraph ("车牌号",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph (orders.get(k).getCar().getPlateNumber(),font));
		   cell.setColspan(2);
		   table.addCell (cell);		   
		   //表格第4行
		   cell = new PdfPCell(new Paragraph ("联系人/电话",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   if(orders.get(k).getDriver()!=null)
			   cell = new PdfPCell (new Paragraph (orders.get(k).getDriver().getName()+"："+orders.get(k).getDriver().getPhoneNumber(),font));
		   else
			   cell = new PdfPCell (new Paragraph ("",font));
		   cell.setColspan (6);
		   table.addCell (cell);
		   //表格第5行
		   table.addCell (new Paragraph ("日期",font));
		   table.addCell (new Paragraph ("上车时间",font));
		   table.addCell (new Paragraph ("下车时间",font));
		   cell = new PdfPCell (new Paragraph ("经过地点摘要",font));
		   cell.setColspan (3);
		   table.addCell (cell);
		   table.addCell (new Paragraph ("实际公里",font));
		   table.addCell (new Paragraph ("收费公里",font));
		   //表格6到13行
		   List<DayOrderDetail> dayDetails = orders.get(k).getDayDetails();
		   if(dayDetails != null){
			   for(int i=0;i<dayDetails.size();i++){
				   table.addCell(new Paragraph(DateUtils.getYMDString(dayDetails.get(i).getGetonDate()),font));
				   table.addCell(new Paragraph(DateUtils.getYMDHMSString(dayDetails.get(i).getGetonDate()),font));
				   table.addCell(new Paragraph(DateUtils.getYMDHMSString(dayDetails.get(i).getGetoffDate()),font));
				   cell = new PdfPCell(new Paragraph(dayDetails.get(i).getPathAbstract(),font));
				   cell.setColspan(3);
				   table.addCell(cell);
				   table.addCell(new Paragraph(dayDetails.get(i).getActualMile()+"",font));
				   table.addCell(new Paragraph(dayDetails.get(i).getChargeMile()+"",font));
			   }
			   if(dayDetails.size()<8){
				   for(int i=0;i<8-dayDetails.size();i++){
					   table.addCell(new Paragraph(" ",font));
					   table.addCell(new Paragraph(" ",font));
					   table.addCell(new Paragraph(" ",font));
					   cell = new PdfPCell(new Paragraph(" ",font));
					   cell.setColspan(3);
					   table.addCell(cell);
					   table.addCell(new Paragraph(" ",font));
					   table.addCell(new Paragraph(" ",font));
				   }
			   }
		   }else{
			   for(int i=0;i<8;i++){
				   table.addCell(new Paragraph(" ",font));
				   table.addCell(new Paragraph(" ",font));
				   table.addCell(new Paragraph(" ",font));
				   cell = new PdfPCell(new Paragraph(" ",font));
				   cell.setColspan(3);
				   table.addCell(cell);
				   table.addCell(new Paragraph(" ",font));
				   table.addCell(new Paragraph(" ",font));
			   }
		   }
		   //表格第14行
		   table.addCell (new Paragraph ("出库路码",font));
		   table.addCell (new Paragraph (orders.get(k).getBeginMile()+"",font));
		   table.addCell (new Paragraph ("客户上车路码",font));
		   table.addCell (new Paragraph (orders.get(k).getCustomerGetonMile()+"",font));
		   table.addCell (new Paragraph ("客户下车路码",font));
		   table.addCell (new Paragraph (orders.get(k).getCustomerGetoffMile()+"",font));
		   table.addCell (new Paragraph ("回库路码",font));
		   table.addCell (new Paragraph (orders.get(k).getEndMile()+"",font));
		   //表格第15行
		   table.addCell (new Paragraph ("油费",font));
		   table.addCell (new Paragraph (orders.get(k).getRefuelMoney()+"",font));
		   table.addCell (new Paragraph ("洗车费",font));
		   table.addCell (new Paragraph (orders.get(k).getWashingFee()+"",font));
		   table.addCell (new Paragraph ("停车费",font));
		   table.addCell (new Paragraph (orders.get(k).getParkingFee()+"",font));
		   table.addCell (new Paragraph ("计费路码",font));
		   table.addCell (new Paragraph (orders.get(k).getTotalChargeMile()+"",font));
		   //表格第16行
		   cell = new PdfPCell(new Paragraph ("过路费（客户自理）",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph (orders.get(k).getToll()+"",font));
		   cell.setColspan(2);
		   table.addCell (cell);	
		   table.addCell (new Paragraph ("食宿",font));
		   table.addCell (new Paragraph (orders.get(k).getRoomAndBoardFee()+"",font));
		   table.addCell (new Paragraph ("其他费用",font));
		   table.addCell (new Paragraph (orders.get(k).getOtherFee()+"",font));
		   //表格第17行
		   table.addCell (new Paragraph ("核算金额",font));
		   //String actualMoney = df1.format(orders.get(k).getActualMoney());
		   cell = new PdfPCell (new Paragraph(orders.get(k).getOrderMoney()+"",font));
		   cell.setColspan(3);
		   table.addCell (cell);
		   cell = new PdfPCell (new Paragraph ("实收金额",font));
		   table.addCell(cell);
		   cell = new PdfPCell (new Paragraph(orders.get(k).getActualMoney()+"",font));
		   cell.setColspan(3);
		   table.addCell (cell);
		   //表格第18行
		   cell = new PdfPCell(new Paragraph ("请为本次服务评价：",font));
		   cell.setColspan(2);
		   table.addCell(cell);
		   String gradeString = null;
		   int grade = orders.get(k).getGrade();
		   if(grade == 0){
			   gradeString = " ";
		   }if(grade == 1){
			   gradeString = "不满意";
		   }if(grade == 2){
			   gradeString = "一般满意";
		   }if(grade == 3){
			   gradeString = "满意";
		   }if(grade == 4){
			   gradeString = "非常满意";
		   }
		   cell = new PdfPCell(new Paragraph(gradeString,font));
		   cell.setColspan(6);
		   table.addCell(cell);
		   //表格第19行
		   table.addCell (new Paragraph ("驾驶员签字",font));
		   cell = new PdfPCell ();
		   cell.setColspan(2);
		   table.addCell (cell);		   
		   cell = new PdfPCell (new Paragraph ("用车人签字及电话",font));
		   cell.setColspan(2);
		   table.addCell (cell);
		   if(orders.get(k).getSignature() != null){
			   InputStream fileStream = diskFileService.getInputStream(orders.get(k).getSignature());
			   int count = (int) fileStream.available();
		       byte[] bt = new byte[count];  
		       fileStream.read(bt, 0, count);
			   Image image = Image.getInstance(bt);
			   image.setWidthPercentage(50);
			   image.setAlignment(Image.MIDDLE);
			   cell.setColspan(3);
			   cell.addElement(image);
			   table.addCell (cell);
		   }else{
			   cell.setColspan(3);
			   table.addCell (cell);
			   cell = new PdfPCell();
		   }
		   //表格第20行
		   table.addCell (new Paragraph ("意见及建议",font));
		   cell = new PdfPCell (new Paragraph(orders.get(k).getOptions()+"",font));
		   cell.setColspan(7);
		   table.addCell (cell);
		   //表格第21行
		   cell = new PdfPCell (new Paragraph ("",font));
		   cell.setBorder(0);
		   table.addCell (cell);
		   
		   Calendar c=Calendar.getInstance();
		   int months = c.get(Calendar.MONTH)+1;
		   String today = c.get(Calendar.YEAR)+"年"+months+"月"+c.get(Calendar.DATE)+"日";
		   cell = new PdfPCell (new Paragraph (today,font));
		   cell.setColspan(2);
		   cell.setBorder(0);
		   table.addCell (cell);
		   
		   cell = new PdfPCell (new Paragraph ("",font));
		   cell.setColspan(3);
		   cell.setBorder(0);
		   table.addCell (cell);
		   
		   if(orders.get(k).getScheduler() != null)
			   cell = new PdfPCell (new Paragraph ("派车人:"+orders.get(k).getScheduler().getName(),font));
		   else
			   cell = new PdfPCell (new Paragraph ("派车人:",font));
		   cell.setBorder(0);
		   cell.setColspan(2);
		   table.addCell (cell);
		   //单元格填充度
		   for(PdfPRow row:(ArrayList<PdfPRow>)table.getRows()){
			   for(PdfPCell cells:row.getCells()){
				   if( cells!=null)
					   cells.setPadding(4.0f);
			   }
		   }		   
		   doc.add (table);	   
	   }
	   doc.close ();
	   this.pdfStream = new ByteArrayInputStream(buffer.toByteArray());
	   buffer.close(); 
	   return SUCCESS;
	}

	public OrderStatement getModel() {
		return model;
	}
	
	public String getOrderStatementName() {
		return orderStatementName;
	}

	public void setOrderStatementName(String orderStatementName) {
		this.orderStatementName = orderStatementName;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public InputStream getPdfStream() {
		return pdfStream;
	}

	public void setPdfStream(InputStream pdfStream) {
		this.pdfStream = pdfStream;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public ByteArrayInputStream getImageStream() {
		return imageStream;
	}

	public void setImageStream(ByteArrayInputStream imageStream) {
		this.imageStream = imageStream;
	}

	public Long getOrderStatementId() {
		return orderStatementId;
	}

	public void setOrderStatementId(Long orderStatementId) {
		this.orderStatementId = orderStatementId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
