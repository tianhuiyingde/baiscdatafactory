package com.aixuexi.ss.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LOG {
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void log(String msg) {
		synchronized (LOG.class) {
			System.out.println("<" + FORMATTER.format(new Date()) + ">:" + msg);
		}
	}

	public static void log(Throwable e) {
		synchronized (LOG.class) {
			if (e != null) {
				e.printStackTrace();
				if (e.getCause() != null) {
					if (e.getCause().getMessage() != null) {
						System.out.println("<" + FORMATTER.format(new Date()) + ">:" + e.getCause().getMessage());
					}
				}
			}
		}
	}
}
