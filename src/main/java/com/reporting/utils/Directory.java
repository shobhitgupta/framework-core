package com.reporting.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.regex.Matcher;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import com.reporting.ReportingUtilities;
import com.reporting.enums.RecordingFor;
import com.reporting.enums.ReportLabels;
import com.reporting.exceptions.ReporterException;
import com.reporting.writers.HTMLDesignFilesWriter;



public class Directory {
	private ReportingUtilities repUtils = new ReportingUtilities();
	//public static final String ATU_VERSION = "5.1.1";
	public static final String CURRENTDir = System.getProperty("user.dir");
	public static final String SEP = System.getProperty("file.separator");
	public static String reportsDirName = "reports";
	public static String REPORTSDir = CURRENTDir + SEP + reportsDirName;
	public static String RESULTSDir = REPORTSDir + SEP + "Results";
	public static String HTMLDESIGNDIRName = "HTML_Design_Files";
	public static String HTMLDESIGNDir = REPORTSDir + SEP + HTMLDESIGNDIRName;
	public static String CSSDIRName = "CSS";
	public static String CSSDir = HTMLDESIGNDir + SEP + CSSDIRName;
	public static String IMGDIRName = "IMG";
	public static String IMGDir = HTMLDESIGNDir + SEP + IMGDIRName;
	public static String JSDIRName = "JS";
	public static String JSDir = HTMLDESIGNDir + SEP + JSDIRName;
	public static String RUNName = "Run_";
	public static String RUNDir = RESULTSDir + SEP + RUNName;
	public static String SETTINGSFile = RESULTSDir + SEP + "Settings.properties";
	public static final char JS_SETTINGS_DELIM = ';';
	public static final String REPO_DELIM = "##@##@##";
	public static final char JS_FILE_DELIM = ',';
	public static final String MENU_LINK_NAME = "Run ";
	public static final String SCREENSHOT_TYPE = "PNG";
	public static final String SCREENSHOT_EXTENSION = ".PNG";
	// private static String logoRight = null;
	private static String logoLeft = null;
	public static String SCREENSHOT_DIRName = "img";
	public static boolean generateConfigReports = true;
	public static boolean generateExcelReports = false;
	public static boolean takeScreenshot = false;
	public static boolean continueExecutionAfterStepFailed = false;
	public static boolean recordExecutionAvailable = false;
	public static boolean recordSuiteExecution = false;
	public static boolean recordTestMethodExecution = false;
	public static final String MAIN_RECORD_FILE_NAME = "ATU_CompleteSuiteRecording";

	public void init() throws ReporterException {
		String propFileFullPath = getCustomProperties();
		if (propFileFullPath != null) {
			Properties localProperties = new Properties();
			try {
				localProperties.load(new FileReader(propFileFullPath));
				String reportsDirPath = localProperties.getProperty("reports.location").trim();
				String str2 = localProperties.getProperty("project.header.text").trim();
				// logoRight =
				// localProperties.getProperty("atu.header.logoRight").trim();
				logoLeft = localProperties.getProperty("reports.header.logoLeft").trim();
				String projectDescription = localProperties.getProperty("project.description").trim();
				String str4 = localProperties.getProperty("report.takescreenshot").trim();
				String str5 = localProperties.getProperty("report.configurationreports").trim();
				String str6 = localProperties.getProperty("atu.reports.excel").trim();
				String str7 = localProperties.getProperty("reports.continueExecutionAfterStepFailed").trim();
				String str8 = localProperties.getProperty("atu.reports.recordExecution").trim();
				try {
					if ((str2 != null) && (str2.length() > 0)) {
						ReportLabels.HEADER_TEXT.setLabel(str2);
					}
					if ((str4 != null) && (str4.length() > 0)) {
						try {
							takeScreenshot = Boolean.parseBoolean(str4);
						} catch (Exception localException1) {
						}
					}
					if ((str5 != null) && (str5.length() > 0)) {
						try {
							generateConfigReports = Boolean.parseBoolean(str5);
						} catch (Exception localException2) {
						}
					}
					if ((str6 != null) && (str6.length() > 0)) {
						try {
							generateExcelReports = Boolean.parseBoolean(str6);
						} catch (Exception localException3) {
						}
					}
					if ((str7 != null) && (str7.length() > 0)) {
						try {
							continueExecutionAfterStepFailed = Boolean.parseBoolean(str7);
						} catch (Exception localException4) {
						}
					}
					if ((str8 != null) && (str8.length() > 0)) {
						try {
							RecordingFor localRecordingFor = RecordingFor.valueOf(str8.toUpperCase());
							if (localRecordingFor == RecordingFor.SUITE) {
								recordSuiteExecution = true;
							} else if (localRecordingFor == RecordingFor.TESTMETHOD) {
								recordTestMethodExecution = true;
							}
						} catch (Throwable localThrowable) {
						}
					}
					if ((projectDescription != null) && (projectDescription.length() > 0)) {
						repUtils.indexPageDescription = projectDescription;
					}
					if ((reportsDirPath != null) && (reportsDirPath.length() > 0)) {
						REPORTSDir = reportsDirPath;
						reportsDirName = new File(REPORTSDir).getName();
						RESULTSDir = REPORTSDir + SEP + "Results";
						HTMLDESIGNDIRName = "HTML_Design_Files";
						HTMLDESIGNDir = REPORTSDir + SEP + HTMLDESIGNDIRName;
						CSSDIRName = "CSS";
						CSSDir = HTMLDESIGNDir + SEP + CSSDIRName;
						IMGDIRName = "IMG";
						IMGDir = HTMLDESIGNDir + SEP + IMGDIRName;
						JSDIRName = "JS";
						JSDir = HTMLDESIGNDir + SEP + JSDIRName;
						RUNName = "Run_";
						RUNDir = RESULTSDir + SEP + RUNName;
						SETTINGSFile = RESULTSDir + SEP + "Settings.properties";
					}
				} catch (Exception localException5) {
					throw new ReporterException(localException5.toString());
				}
			} catch (FileNotFoundException localFileNotFoundException) {
				throw new ReporterException("The Path for the Custom Properties file could not be found");
			} catch (IOException localIOException) {
				throw new ReporterException("Problem Occured while reading the ATU Reporter Config File");
			}
		}
	}

	public static void mkDirs(String paramString) {
		File localFile = new File(paramString);
		if (!localFile.exists()) {
			localFile.mkdirs();
		}
	}

	public static boolean exists(String paramString) {
		File localFile = new File(paramString);
		return localFile.exists();
	}

	public void verifyRequiredFiles() throws ReporterException {
		init();
		mkDirs(REPORTSDir);
		if (!exists(RESULTSDir)) {
			mkDirs(RESULTSDir);
			SettingsFile.initSettingsFile();
		}
		if (!exists(HTMLDESIGNDir)) {
			mkDirs(HTMLDESIGNDir);
			mkDirs(CSSDir);
			mkDirs(JSDir);
			mkDirs(IMGDir);
			HTMLDesignFilesWriter.writeCSS();
			HTMLDesignFilesWriter.writeIMG();
			HTMLDesignFilesWriter.writeJS();
		}
		// if ((logoRight != null) && (logoRight.length() > 0)) {
		// String str = new File(logoRight).getName();
		// if (!new File(IMGDir + SEP + str).exists()) {
		// copyImage(logoRight);
		// }
		// ReportLabels.LOGO_LEFT.setLabel(str);
		// }

		if ((logoLeft != null) && (logoLeft.length() > 0)) {
			String str = new File(logoLeft).getName();
			if (!new File(IMGDir + SEP + str).exists()) {
				copyImage(logoLeft);
			}
			ReportLabels.LOGO_LEFT.setLabel(str);
		}
	}

	private static void copyImage(String paramString) throws ReporterException {
		File localFile = new File(paramString);
		if (!localFile.exists()) {
			return;
		}
		FileImageInputStream localFileImageInputStream = null;
		FileImageOutputStream localFileImageOutputStream = null;
		try {
			localFileImageInputStream = new FileImageInputStream(new File(paramString));
			localFileImageOutputStream = new FileImageOutputStream(new File(IMGDir + SEP + localFile.getName()));
			int i = 0;
			while ((i = localFileImageInputStream.read()) >= 0) {
				localFileImageOutputStream.write(i);
			}
			localFileImageOutputStream.close();
			return;
		} catch (Exception localException2) {
		} finally {
			try {
				localFileImageInputStream.close();
				localFileImageOutputStream.close();
				localFile = null;
			} catch (Exception localException4) {
				localFileImageInputStream = null;
				localFileImageOutputStream = null;
				localFile = null;
			}
		}
	}

	private static String getCustomProperties() throws ReporterException {
		if (System.getProperty("reporter.config") == null) {
			return null;
		}
		File propFile = new File(System.getProperty("reporter.config"));
		if (!propFile.exists() || propFile.isDirectory()) {
			return null;
		}

		try {
			File file = Files.createTempFile("reporter", ".properties").toFile();
			file.deleteOnExit();
			FileWriter fw = new FileWriter(file);
			String fileText = new String(Files.readAllBytes(propFile.toPath()));

			// replace variables
			fileText = fileText.replace("${file.separator}", Matcher.quoteReplacement(System.getProperty("file.separator")));
			fileText = fileText.replace("${user.dir}", Matcher.quoteReplacement(System.getProperty("user.dir")));

			fw.write(fileText);
			fw.close();
			return file.getAbsolutePath();
		} catch (IOException e) {
			throw new ReporterException("Failed to read custom config file at: " + propFile.getAbsolutePath());
		}
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.utils.Directory
 * 
 * JD-Core Version: 0.7.0.1
 */