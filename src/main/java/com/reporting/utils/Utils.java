package com.reporting.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static String getCurrentTime() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd-MMM-yy h:mm:ss a");
		Date localDate = new Date();
		return localSimpleDateFormat.format(localDate);
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.utils.Utils
 * 
 * JD-Core Version: 0.7.0.1
 * 
 */