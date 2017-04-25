package com.yuqincar.action.queryStatistics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.mysql.fabric.xmlrpc.base.Data;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.MonitorGroupService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
public class RealtimeChartAction extends BaseAction{
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private MonitorGroupService monitorGroupService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private LBSDao lbsDao;
	
	//导出PDF流
	private InputStream pdfStream;
	//PDF文件名
	private String fileName;
	private Date date;
	
	
	public String exportUI(){
		return "exportUI";
	}
	
	public String export() throws Exception{
		//设置pdf文件名
		String tempName = "监控报表.pdf";
		fileName = new String(tempName.getBytes("GB2312"), "ISO8859-1");
		//手动画表格
	    Document doc = new Document (PageSize.A4);
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    PdfWriter.getInstance (doc, buffer);
	    doc.setMargins(10, 10, 40, 20);
	    doc.open (); 
	    
	    //标题字体
	    BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font topFont = new Font(baseFont, 20, Font.BOLD);
	    Font titleFont = new Font(baseFont, 20, Font.BOLD);
	    DecimalFormat df = new DecimalFormat("0.0");//浮点数显示格式
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//时间格式
        //内容字体
	    Font font = new Font(baseFont, 10, Font.NORMAL);
	    //首页
	    doc.newPage();
	    Paragraph t = new Paragraph("渝勤汽车运营车辆监控报表（"+DateUtils.getChineseYMDString(date)+"）",topFont);
	    t.setAlignment(1);
	    doc.add(t);
	    Paragraph p = new Paragraph("\n",titleFont);
	    doc.add(p);
   	    PdfPTable table = new PdfPTable (5);
	    table.setTotalWidth(660f);
	    table.setWidths(new float[]{110f,100f,100f,350f,100f});
	    //get all tracks
	    ArrayList<RealtimeChartVO> rcvo = new ArrayList<RealtimeChartVO>();
		try {
			rcvo = getAllTrails();
			if(rcvo == null || rcvo.size() <=0){
				addFieldError("null1", "该时间段内没有轨迹！");
				return exportUI();
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("null2", "请求出错，请重试！");
			return exportUI();
		}
		PdfPCell cell = new PdfPCell();
	    for(int i=0;i<rcvo.size();i++){
	    	
	    	//plateNumber of car
	    	cell = new PdfPCell (new Paragraph ("车辆:",font));
		    table.addCell (cell);	
		    cell = new PdfPCell(new Paragraph(rcvo.get(i).getPlateNumber(),font));
		    cell.setColspan (4);
		    table.addCell(cell);
		    //all miles
		    cell = new PdfPCell (new Paragraph ("总里程:",font));
		    table.addCell (cell);	
		    String totalMiles = df.format(rcvo.get(i).getTotalMile());
		    cell = new PdfPCell(new Paragraph(totalMiles+" 公里",font));
		    cell.setColspan (4);
		    table.addCell(cell);
		    //list of tracks
		    cell = new PdfPCell(new Paragraph("轨迹序号",font));
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("开始时间",font));
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("结束时间",font));
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("轨迹摘要",font));
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("里程(公里)",font));
		    table.addCell(cell);
		    for(int j=0;j<rcvo.get(i).getPaths().size();j++){
			    cell = new PdfPCell(new Paragraph((j+1)+"",font));
			    table.addCell(cell);
			    String fromDate = sdf.format(rcvo.get(i).getPaths().get(j).getBeginDate());
			    cell = new PdfPCell(new Paragraph(fromDate,font));
			    table.addCell(cell);
			    String toDate = sdf.format(rcvo.get(i).getPaths().get(j).getEndDate());
			    cell = new PdfPCell(new Paragraph(toDate,font));
			    table.addCell(cell);
			    cell = new PdfPCell(new Paragraph(rcvo.get(i).getPaths().get(j).getPathAbstract(),font));
			    table.addCell(cell);
			    cell = new PdfPCell(new Paragraph(rcvo.get(i).getPaths().get(j).getMiles()+"",font));
			    table.addCell(cell);
		    }
		    
		    
	    }
	    doc.add(table);
	    //打印结束，关闭
	    doc.close ();
	    this.pdfStream = new ByteArrayInputStream(buffer.toByteArray());
	    buffer.close(); 
	    
		return SUCCESS;
			   
	}


	
	//get trails
	public ArrayList<RealtimeChartVO> getAllTrails(){
		ArrayList<RealtimeChartVO> rcvo = new ArrayList<RealtimeChartVO>();
		//sort by plateNumber
		List<Car> cars = monitorGroupService.sortCarByPlateNumber(monitorGroupService.getCarFromStandingGarage());
		long begin = date.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		long end = calendar.getTime().getTime();
		String url = "http://api.capcare.com.cn:1045/api/get.part.do?begin="+begin+"&end="+end+"&token="+Configuration.getCapcareToken()+
				 "&user_id="+Configuration.getCapcareUserId()+"&app_name="+Configuration.getCapcareAppName()+"&device_sn=";
		for(Car car : cars){
			String sn = car.getDevice().getSN();
			String json = HttpMethod.get(url+sn);
			JSONObject jsonObject = JSONObject.fromObject(json);
			int ret = Integer.valueOf(jsonObject.getInt("ret"));
			//filter the tracks
			if(ret == 1){
				JSONArray tracks = jsonObject.getJSONArray("track");
				if(tracks.size()>0){
					RealtimeChartVO vo = new RealtimeChartVO();
					ArrayList<Path> paths = new ArrayList<Path>();
					//from end to begin
					for(int i=tracks.size()-1;i>=0;i--){
						long time = tracks.getJSONObject(i).getJSONArray("states").getJSONObject(0).getLong("receive")
											-tracks.getJSONObject(i).getJSONArray("states").getJSONObject(1).getLong("receive");
						if(tracks.getJSONObject(i).getDouble("distance")/time > 2.0/3600000){
								Path path = new Path();
								long pathBegin = tracks.getJSONObject(i).getJSONArray("states").getJSONObject(1).getLong("receive");
								long pathEnd = tracks.getJSONObject(i).getJSONArray("states").getJSONObject(0).getLong("receive");
								double miles = tracks.getJSONObject(i).getDouble("distance");
								path.setBeginDate(new Date(pathBegin));
								path.setEndDate(new Date(pathEnd));
								path.setMiles(miles);
								path.setPathAbstract(orderService.getOrderTrackAbstract(sn, new Date(pathBegin), new Date(pathEnd)));
								paths.add(path);
						}
					}
					if(paths.size()>0){
						vo.setPlateNumber(car.getPlateNumber());
						vo.setPaths(paths);
						vo.setTotalMile(lbsDao.getStepMileByEnhancedTrack(car, date, calendar.getTime()));
						rcvo.add(vo);
					}
				}
			}
		}
		return rcvo;
	}
	
	class Path{
		Date beginDate;
		Date endDate;
		String pathAbstract;
		double miles;
		public Date getBeginDate() {
			return beginDate;
		}
		public void setBeginDate(Date beginDate) {
			this.beginDate = beginDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public String getPathAbstract() {
			return pathAbstract;
		}
		public void setPathAbstract(String pathAbstract) {
			this.pathAbstract = pathAbstract;
		}
		public double getMiles() {
			return miles;
		}
		public void setMiles(double miles) {
			this.miles = miles;
		}
	}
	
	class RealtimeChartVO{
		String plateNumber;
		float totalMile;
		ArrayList<Path> paths;
		public String getPlateNumber() {
			return plateNumber;
		}
		public void setPlateNumber(String plateNumber) {
			this.plateNumber = plateNumber;
		}
		public float getTotalMile() {
			return totalMile;
		}
		public void setTotalMile(float totalMile) {
			this.totalMile = totalMile;
		}
		public ArrayList<Path> getPaths() {
			return paths;
		}
		public void setPaths(ArrayList<Path> paths) {
			this.paths = paths;
		}
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
