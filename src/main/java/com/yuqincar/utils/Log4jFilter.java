package com.yuqincar.utils;

import org.apache.log4j.spi.LoggingEvent;

public final class Log4jFilter extends org.apache.log4j.spi.Filter {
	@Override
	public int decide(LoggingEvent event) {
		//大于等于WARN的日志不允许输出
		if(event.getMessage().toString().contains("ConnectTimeoutException")) {
			return DENY;
		} else {
			return ACCEPT;
		}
	}
}