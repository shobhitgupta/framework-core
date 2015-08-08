package com.reporting.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;




import com.reporting.exceptions.ReporterException;

public class SettingsFile {
	private static Properties properties;

	public static void initSettingsFile() throws ReporterException {
		create(Directory.SETTINGSFile);
		set("run", "0");
		set("passedList", "");
		set("failedList", "");
		set("skippedList", "");
	}

	public static void create(String paramString) throws ReporterException {
		File localFile = new File(paramString);
		try {
			if (!localFile.exists()) {
				localFile.createNewFile();
			} else {
				localFile.delete();
				localFile.createNewFile();
			}
		} catch (IOException localIOException) {
			throw new ReporterException("Unable To Create Required Files for Custom Reports. Path: " + paramString);
		}
	}

	public static void open() throws ReporterException {
		properties = new Properties();
		try {
			properties.load(new FileReader(Directory.SETTINGSFile));
		} catch (FileNotFoundException localFileNotFoundException) {
			throw new ReporterException("Settings File Not Available");
		} catch (IOException localIOException) {
			throw new ReporterException("Unable To Create Required Files for Custom Reports");
		}
	}

	public static void close() throws ReporterException {
		try {
			properties.store(new FileWriter(Directory.SETTINGSFile), "");
		} catch (FileNotFoundException localFileNotFoundException) {
			throw new ReporterException("Settings File Not Available");
		} catch (IOException localIOException) {
			throw new ReporterException("Unable To Create Required Files for Custom Reports");
		} finally {
			properties = null;
		}
	}

	public static void correctErrors() throws NumberFormatException, ReporterException {
		int i = Integer.parseInt(get("run"));
		int j = get("passedList").split(";").length;
		int k = get("failedList").split(";").length;
		int m = get("skippedList").split(";").length;
		if (isFirstParamBig(i, j, k, m)) {
			int n = i - j;
			String str1 = get("passedList");
			for (int i1 = 0; i1 < n; i1++) {
				str1 = str1 + 0 + ';';
			}
			set("passedList", str1);
			n = i - k;
			String str2 = get("failedList");
			for (int i2 = 0; i2 < n; i2++) {
				str2 = str2 + 0 + ';';
			}
			set("failedList", str2);
			n = i - m;
			String str3 = get("skippedList");
			for (int i3 = 0; i3 < n; i3++) {
				str3 = str3 + 0 + ';';
			}
			set("skippedList", str3);
			return;
		}
		if (isFirstParamBig(j, i, k, m)) {
			return;
		}
		if (isFirstParamBig(k, j, i, m)) {
			return;
		}
		if (isFirstParamBig(m, j, k, i)) {}
	}

	private static boolean isFirstParamBig(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		return (paramInt1 > paramInt2) && (paramInt1 > paramInt3) && (paramInt1 > paramInt4);
	}

	public static String get(String paramString) throws ReporterException {
		open();
		String str = properties.getProperty(paramString);
		close();
		return str;
	}

	public static void set(String paramString1, String paramString2) throws ReporterException {
		open();
		properties.setProperty(paramString1, paramString2);
		close();
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.utils.SettingsFile
 * 
 * JD-Core Version: 0.7.0.1
 */