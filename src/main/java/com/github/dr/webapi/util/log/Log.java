/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.github.dr.webapi.util.DateUtil.getLocalTimeFromU;

//Java
//Static

/**
 * Log Util
 * @version 1.0 
 * @date 2020年3月8日星期日 3:54  
 * 练手轮子? :P 
 */
public class Log {
	//默认 WARN
	private static int LOG_GRADE = 5;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private enum Logg {
		/*
		 * Log等级 默认为WARN
		 * 开发时为ALL
		 */
		OFF(8),FATAL(7),ERROR(6),WARN(5),INFO(4),DEBUG(3),TRACE(2),ALL(1);
		private int num;
		private Logg(int num) {
			this.num=num;
		}
		private int getLogg() {
			return num;
		}
	}


	public static void set(String log) {
		Log.LOG_GRADE=Logg.valueOf(log).getLogg();
	}

	/**
	 * Log：
	 * @param tag 标题 默认警告级
	 * @param e Exception
	 */
	public static void fatal(Exception e) {
		log(7,"FATAL",e);
	}
	public static void fatal(Object tag, Exception e) {
		log(7,tag,e);
	}

	public static void error(Exception e) {
		log(6,"ERROR",e);
	}
	public static void error(Object tag, Exception e) {
		log(6,tag,e);
	}
	public static void error(Object e) {
		logs(4,"ERROR",e);
	}
	public static void error(Object tag, Object e) {
		logs(4,tag,e);
	}

	public static void warn(Exception e) {
		log(5,"WARN",e);
	}
	public static void warn(Object tag, Exception e) {
		log(5,tag,e);
	}
	public static void warn(Object e) {
		logs(4,"WARN",e);
	}
	public static void warn(Object tag, Object e) {
		logs(4,tag,e);
	}

	public static void info(Exception e) {
		log(4,"INFO",e);
	}
	public static void info(Object tag, Exception e) {
		log(4,tag,e);
	}
	public static void info(Object e) {
		logs(4,"INFO",e);
	}
	public static void info(Object tag, Object e) {
		logs(4,tag,e);
	}

	public static void debug(Exception e) {
		log(3,"DEBUG",e);
	}
	public static void debug(Object tag, Exception e) {
		log(3,tag,e);
	}
	public static void debug(Object e) {
		logs(3,"DEBUG",e);
	}
	public static void debug(Object tag, Object e) {
		logs(3,tag,e);
	}

	public static void tarce(Exception e) {
		log(2,"TARCE",e);
	}
	public static void tarce(Object tag, Exception e) {
		log(2,tag,e);
	}

	/**
	 * WLog：
	 * @param i 警告级
	 * @param tag 标题 默认警告级
	 * @param e Exception
	 *i>=设置级 即写入文件
	 */
	private static void log(int i, Object tag, Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		logs(i,tag,stringWriter.getBuffer());
	}


	private static void logs(int i, Object tag, Object e) {
		if(LOG_GRADE>i) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		StringBuffer error = new StringBuffer(e.toString());
		String[] lines = error.toString().split(LINE_SEPARATOR);
		sb.append(getLocalTimeFromU(0,1))
			.append(" UTC")
			.append(LINE_SEPARATOR)
			.append(tag)
			.append(": ")
			.append(LINE_SEPARATOR);
		for (Object line : lines) {
			sb.append(line)
				.append(LINE_SEPARATOR);
		}
		System.out.println(sb);
	}
}