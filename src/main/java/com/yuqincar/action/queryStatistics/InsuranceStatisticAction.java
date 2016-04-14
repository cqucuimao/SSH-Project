package com.yuqincar.action.queryStatistics;

import java.util.Calendar;
import java.util.Date;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class InsuranceStatisticAction extends BaseAction {
	
	@Autowired
	private CarInsuranceService carInsuranceService;
	private static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat MM = new SimpleDateFormat("MM");
	private Date payDate;
	private Date date1;
	private Date date2;
	private String dateString;
	private boolean canShowImage;
	private String clicks;
	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	private String plateNumber;
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getInsureCompany() {
		return insureCompany;
	}

	public void setInsureCompany(String insureCompany) {
		this.insureCompany = insureCompany;
	}

	public String getInsureType() {
		return insureType;
	}

	public void setInsureType(String insureType) {
		this.insureType = insureType;
	}

	private String insureCompany;
	private String insureType;

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	

	public String getDateString() {
		return DateUtils.getYMDString(payDate);
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public boolean isCanShowImage() {
		return canShowImage;
	}

	public void setCanShowImage(boolean canShowImage) {
		this.canShowImage = canShowImage;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	/** 列表（今日统计） */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(payDate!=null && !"".equals(payDate))
			helper.addWhereCondition("? <= ci.payDate and ci.payDate <= ?",DateUtils.getMinDate(payDate),DateUtils.getMaxDate(payDate));
				
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 本周统计*/
	public String weekList() throws Exception{
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(payDate!=null && !"".equals(payDate))
			helper.addWhereCondition("? <= ci.payDate and ci.payDate <= ?",DateUtils.getWeek(payDate).getLowDate(),DateUtils.getWeek(payDate).getHighDate());
		
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "weekList";
	}
	
	/** 本月统计*/
	public String monthList() throws Exception{
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(payDate!=null && !"".equals(payDate))
			helper.addWhereCondition("? <= ci.payDate and ci.payDate <= ?", DateUtils.getFirstDateOfMonth(payDate),DateUtils.getEndDateOfMonth(payDate));
		
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		if(clicks!=null && clicks.equals("clicked"))
			canShowImage=true;
		return "monthList";
	}
	
	/** 查询*/
	public String query() throws Exception{
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(ci.payDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(ci.payDate))>=0", date1 ,date2);
		
		if(plateNumber!=null && !"".equals(plateNumber))
			helper.addWhereCondition("ci.car.plateNumber = ?", plateNumber );
		
		if(insureCompany!=null && !"".equals(insureCompany))
			helper.addWhereCondition("ci.insureCompany like ?", "%"+insureCompany+"%");
		
		if(insureType!=null && !"".equals(insureType))
			helper.addWhereCondition("ci.insureType like ?", "%"+insureType+"%");
		
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "query";
	}
	
	/** 显示环比直方图*/
	public void MoMHistogram() throws Exception{

			// 设置主题
			StandardChartTheme standardChartTheme = new StandardChartTheme("name");
			standardChartTheme.setLargeFont(new Font("楷体",Font.BOLD, 12));//可以改变轴向的字体
			standardChartTheme.setRegularFont(new Font("宋体",Font.BOLD, 12));//可以改变图例的字体
			standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD, 12));//可以改变图标的标题字体
			ChartFactory.setChartTheme(standardChartTheme);//Theme.getTheme()
			// 构造数据
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();//柱状图数据
			DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();//折线图数据
			
			payDate=DateUtils.getYMD(dateString);
			int x = Integer.parseInt(YYYY.format(payDate));
			int y = Integer.parseInt(MM.format(payDate));
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(payDate);
			for(int i=0;i<12;i++){
				calendar.set(Calendar.MONTH, i);
				calendar.set(Calendar.DAY_OF_MONTH,1);
				Date begin=calendar.getTime();
				Date end=DateUtils.getEndDateOfMonth(begin);
				BigDecimal money=carInsuranceService.statisticCarInsurance(DateUtils.getMinDate(begin),DateUtils.getMaxDate(end));
				dataset.addValue(money, "保险金额", x + "年" + (i+1) + "月");
	            lineDataset.addValue(money, "保险金额", x + "年" + (i+1) + "月");
			}
			
			JFreeChart chart = ChartFactory.createBarChart("保险金额(元)",
			     "",// 目录轴的显示标签
			     "", // 数值轴的显示标签
			     dataset,// 数据集
			     PlotOrientation.VERTICAL,// 图表方向：水平、垂直
			     true,// 是否显示图例(对于简单的柱状图必须是false)
			     true,//是否生成工具
			     false);// 是否生成URL链接
			//字体清晰
		    //chart.setTextAntiAlias(false);
			chart.getTitle().setFont(new Font("隶书", Font.BOLD, 26));//设置title标题
			chart.setBackgroundPaint(new Color(238, 238, 255));//设置背景色
			chart.getLegend().setItemFont(new Font("隶书", Font.BOLD, 18));// 底部
			CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();//图本身
			categoryplot.setDataset(1, lineDataset);//放折线图数据
			   LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
			   lineandshaperenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			   //设置曲线是否显示数据点
			   lineandshaperenderer.setBaseShapesVisible(true);
			   //设置曲线显示各数据点的值
			   lineandshaperenderer.setBaseItemLabelsVisible(true);
			   lineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
			   //显示折点数据
			   lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			   lineandshaperenderer.setBaseItemLabelFont(new Font("Dialog", 1, 16));
			   lineandshaperenderer.setBaseItemLabelsVisible(true);
			   
			   categoryplot.setRenderer(1, lineandshaperenderer);
			ValueAxis valueAxis = categoryplot.getRangeAxis();
			   valueAxis.setLabelFont(new Font("黑体", Font.ITALIC, 18)); // 设置数据字体(纵轴)
			   CategoryAxis categoryaxis = categoryplot.getDomainAxis();
			   categoryaxis.setLabelFont(new Font("黑体", Font.ITALIC, 18)); // 设置时字体（横轴）
			   categoryaxis.setLowerMargin(0.05); // 柱状图和纵轴紧靠
			   
			   categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
			   categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);//折线在柱面前面显示

			   response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 900, 600);  
	}
	
	/** 显示同比直方图*/
	public void YoYHistogram() throws Exception{
		
			// 设置主题
			StandardChartTheme standardChartTheme = new StandardChartTheme("name");
			standardChartTheme.setLargeFont(new Font("楷体",Font.BOLD, 12));//可以改变轴向的字体
			standardChartTheme.setRegularFont(new Font("宋体",Font.BOLD, 12));//可以改变图例的字体
			standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD, 12));//可以改变图标的标题字体
			ChartFactory.setChartTheme(standardChartTheme);//Theme.getTheme()
			// 构造数据
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();//柱状图数据
			DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();//折线图数据
			
			System.out.println("dateString="+dateString);
			payDate=DateUtils.getYMD(dateString);
			Date today = new Date();
			int x = Integer.parseInt(YYYY.format(payDate));
			int y = Integer.parseInt(MM.format(payDate));
			int z = Integer.parseInt(YYYY.format(today));
			
			if(x<=z){
				Calendar calendar=Calendar.getInstance();
				calendar.set(Calendar.MONTH, y-1);
				calendar.set(Calendar.YEAR, z);
				calendar.set(Calendar.DAY_OF_MONTH,1);
				Date begin=calendar.getTime();
				Date end=DateUtils.getEndDateOfMonth(begin);
				BigDecimal money = carInsuranceService.statisticCarInsurance(DateUtils.getMinDate(begin),DateUtils.getMaxDate(end));
				dataset.addValue(money, "保险金额", z + "年" + y + "月");
	            lineDataset.addValue(money, "保险金额", z + "年" + y + "月");
			}else{
				for(int i=z;i<=x;i++){
					Calendar calendar=Calendar.getInstance();
					//calendar.setTime(today);
					calendar.set(Calendar.YEAR, i);
					calendar.set(Calendar.MONTH, y-1);
					calendar.set(Calendar.DAY_OF_MONTH,1);
					Date begin=calendar.getTime();
					Date end=DateUtils.getEndDateOfMonth(begin);
					//System.out.println("begin="+DateUtils.getYMDString(begin));
					//System.out.println("end="+DateUtils.getYMDString(end));
					BigDecimal money=carInsuranceService.statisticCarInsurance(DateUtils.getMinDate(begin),DateUtils.getMaxDate(end));
					dataset.addValue(money, "保险金额", i + "年" + y + "月");
		            lineDataset.addValue(money, "保险金额", i + "年" + y + "月");
				}
			}
			
			JFreeChart chart = ChartFactory.createBarChart("保险金额(元)",
			     "",// 目录轴的显示标签
			     "", // 数值轴的显示标签
			     dataset,// 数据集
			     PlotOrientation.VERTICAL,// 图表方向：水平、垂直
			     true,// 是否显示图例(对于简单的柱状图必须是false)
			     true,//是否生成工具
			     false);// 是否生成URL链接
			//字体清晰
		    //chart.setTextAntiAlias(false);
			chart.getTitle().setFont(new Font("隶书", Font.BOLD, 26));//设置title标题
			chart.setBackgroundPaint(new Color(238, 238, 255));//设置背景色
			chart.getLegend().setItemFont(new Font("隶书", Font.BOLD, 18));// 底部
			CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();//图本身
			
			// 改变柱子的默认颜色
			org.jfree.chart.renderer.category.BarRenderer renderer;
			renderer = new org.jfree.chart.renderer.category.BarRenderer();
			renderer.setSeriesPaint(0, new Color(80, 180, 180));
			
			categoryplot.setDataset(1, lineDataset);//放折线图数据
			   LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
			   lineandshaperenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			   //设置曲线是否显示数据点
			   lineandshaperenderer.setBaseShapesVisible(true);
			   //lineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));//设置折线加粗
			   lineandshaperenderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-5D, -5D, 10D, 10D));//设置折线拐点
			   //设置曲线显示各数据点的值
			   lineandshaperenderer.setBaseItemLabelsVisible(true);
			   lineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
			   //显示折点数据
			   lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			   lineandshaperenderer.setBaseItemLabelFont(new Font("Dialog", 1, 16));
			   lineandshaperenderer.setBaseItemLabelsVisible(true);
			   
			   categoryplot.setRenderer(1, lineandshaperenderer);
			   categoryplot.setRenderer(renderer);  //使用我们设置的柱子颜色
			ValueAxis valueAxis = categoryplot.getRangeAxis();
			   valueAxis.setLabelFont(new Font("黑体", Font.ITALIC, 18)); // 设置数据字体(纵轴)
			   CategoryAxis categoryaxis = categoryplot.getDomainAxis();
			   categoryaxis.setLabelFont(new Font("黑体", Font.ITALIC, 18)); // 设置时字体（横轴）
			   categoryaxis.setLowerMargin(0.05); // 柱状图和纵轴紧靠
			   categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
			   categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);//折线在柱面前面显示

			   response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 900, 600);  
	}

}
