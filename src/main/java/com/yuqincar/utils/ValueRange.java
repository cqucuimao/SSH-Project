package com.yuqincar.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一个表示数值范围的工具类
 * 
 * 该类为unmutable类，数值只在构造时传入，null表示不限制。
 */
public class ValueRange {

	private static final BigDecimal MAX_BIGDECIMAL = new BigDecimal(
			"100000000000");
	private static final BigDecimal MIN_BIGDECIMAL = new BigDecimal(
			"-100000000000");
	private static final Date MIN_DATE = str2Date("1800-01-01");
	private static final Date MAX_DATE = str2Date("3000-01-01");
	private static final String MIN_STRING = "";
	private static final byte bytes[] = { (byte) 255, (byte) 255, (byte) 255,
			(byte) 255 };
	private static final String MAX_STRING = new String(bytes);

	protected static Date str2Date(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(dateStr);
		} catch (ParseException pe) {
			throw new RuntimeException("不可能发生的事情", pe);
		}
	}

	private Object lowValue = null;
	private Object highValue = null;

	/**
	 * 构造一个整数范围
	 * 
	 * @param low
	 *            an <code>Integer</code> 下限，null 表示不限制
	 * @param high
	 *            an <code>Integer</code> 上限, null 表示不限制
	 */
	public ValueRange(Integer low, Integer high) {
		setLowValue(low == null ? Integer.MIN_VALUE : low);
		setHighValue(high == null ? Integer.MAX_VALUE : high);
	}

	/**
	 * 构造一个长整数范围
	 * 
	 * @param low
	 *            a <code>Long</code> 下限, null表示不限制
	 * @param high
	 *            a <code>Long</code> 上限, null表示不限制
	 */
	public ValueRange(Long low, Long high) {
		setLowValue(low == null ? Long.MIN_VALUE : low);
		setHighValue(high == null ? Long.MAX_VALUE : high);
	}

	/**
	 * 构造一个BigDecimal范围
	 * 
	 * @param low
	 *            a <code>BigDecimal</code> 下限，null表示不限制
	 * @param high
	 *            a <code>BigDecimal</code> 上限，null表示不限制
	 */
	public ValueRange(BigDecimal low, BigDecimal high) {
		setLowValue(low == null ? MIN_BIGDECIMAL : low);
		setHighValue(high == null ? MAX_BIGDECIMAL : high);
	}

	/**
	 * 构造一个Double数据类型的范围
	 * 
	 * @param low
	 *            a <code>Double</code> 下限，null表示不限制
	 * @param high
	 *            a <code>Double</code> 上限，null表示不限制
	 */
	public ValueRange(Double low, Double high) {
		setLowValue(low == null ? Double.MIN_VALUE : low);
		setHighValue(high == null ? Double.MAX_VALUE : high);
	}

	public ValueRange(Float low, Float high) {
		setLowValue(low == null ? Float.MIN_VALUE : low);
		setHighValue(high == null ? Float.MAX_VALUE : high);
	}

	/**
	 * 构造一个日期范围
	 * 
	 * @param low
	 *            a <code>Date</code> 下限，null表示不限制
	 * @param high
	 *            a <code>Date</code>上限，null表示不限制
	 */
	public ValueRange(Date low, Date high) {
		setLowValue(low == null ? MIN_DATE : low);
		setHighValue(high == null ? MAX_DATE : high);
	}

	/**
	 * 构造一个字符串范围
	 * 
	 * @param low
	 *            a <code>String</code> 下限，null表示不限制
	 * @param high
	 *            a <code>String</code> 上限，null表示不限制
	 */
	public ValueRange(String low, String high) {
		setLowValue(low == null ? MIN_STRING : low);
		setHighValue(high == null ? MAX_STRING : high);
	}

	/**
	 * 内部使用，保持该对象的unmutable
	 * 
	 * @param lowValue
	 *            an <code>Object</code> value
	 */
	protected void setLowValue(Object lowValue) {
		this.lowValue = lowValue;
	}

	/**
	 * 内部使用，保持该对象的unmutable
	 * 
	 * @param highValue
	 *            an <code>Object</code> value
	 */
	protected void setHighValue(Object highValue) {
		this.highValue = highValue;
	}

	/**
	 * 读取下限值，不会出现null值，lowValue == null时，被对应的数据类型的MIN_VALUE替换。
	 * 
	 * @return an <code>Object</code> 内部数值对象
	 */
	public Object getLowValue() {
		return lowValue;
	}

	/**
	 * 读取上限值，不会出现null值，highValue == null时，被对应的数据类型的MAX_VALUE替换
	 * 
	 * @return an <code>Object</code> value
	 */
	public Object getHighValue() {
		return highValue;
	}
}
