package com.reporting.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import com.reporting.ReportingUtilities;
import com.reporting.exceptions.ReporterStepFailedException;
import com.reporting.utils.Directory;
import com.reporting.utils.Platform;
import com.reporting.utils.SettingsFile;
import com.reporting.writers.ConsolidatedReportsPageWriter;
import com.reporting.writers.CurrentRunPageWriter;
import com.reporting.writers.HTMLDesignFilesJSWriter;
import com.reporting.writers.IndexPageWriter;
import com.reporting.writers.TestCaseReportsPageWriter;

public class ReportsListener implements ITestListener, ISuiteListener {
	// private ReportingUtilities repUtils = new ReportingUtilities();
	int runCount = 0;
	ISuite iSuite;
	List<ITestResult> passedTests = new ArrayList<ITestResult>();
	List<ITestResult> failedTests = new ArrayList<ITestResult>();
	List<ITestResult> skippedTests = new ArrayList<ITestResult>();

	public void onFinish(ITestContext paramITestContext) {}

	public void onStart(ITestContext paramITestContext) {}

	public void onTestFailedButWithinSuccessPercentage(ITestResult paramITestResult) {}

	public void onTestFailure(ITestResult paramITestResult) {
		this.failedTests.add(paramITestResult);
	}

	public void onTestSkipped(ITestResult paramITestResult) {
		createReportDir(paramITestResult);
		this.skippedTests.add(paramITestResult);
	}

	public void onTestStart(ITestResult paramITestResult) {}

	public void onTestSuccess(ITestResult paramITestResult) {
		try {
			if (paramITestResult.getAttribute("passedButFailed").equals("passedButFailed")) {
				paramITestResult.setStatus(2);
				paramITestResult.setThrowable(new ReporterStepFailedException());
				this.failedTests.add(paramITestResult);
				return;
			}
		} catch (NullPointerException localNullPointerException) {}
		this.passedTests.add(paramITestResult);
	}

	public static void setPlatfromBrowserDetails(ITestResult paramITestResult) {
		Platform.prepareDetails(ReportingUtilities.getWebDriver());
		paramITestResult.setAttribute(Platform.BROWSER_NAME_PROP, Platform.BROWSER_NAME);
		paramITestResult.setAttribute(Platform.BROWSER_VERSION_PROP, Platform.BROWSER_VERSION);
	}

	public static void createReportDir(ITestResult paramITestResult) {
		String str = getReportDir(paramITestResult);
		Directory.mkDirs(str);
		Directory.mkDirs(str + Directory.SEP + Directory.SCREENSHOT_DIRName);
	}

	public static String getRelativePathFromSuiteLevel(ITestResult paramITestResult) {
		String str1 = paramITestResult.getTestContext().getSuite().getName();
		String str2 = paramITestResult.getTestContext().getCurrentXmlTest().getName();
		String str3 = paramITestResult.getTestClass().getName().replace(".", Directory.SEP);
		String str4 = paramITestResult.getMethod().getMethodName();
		// str4 = str4 + "_Iteration" +
		// (paramITestResult.getMethod().getCurrentInvocationCount() + 1);
		str4 = str4 + "_" + getTimeInMillis() + "";
		return str1 + Directory.SEP + str2 + Directory.SEP + str3 + Directory.SEP + str4;
	}

	private synchronized static long getTimeInMillis() {
		return System.currentTimeMillis();
	}

	public static String getReportDir(ITestResult paramITestResult) {
		String str1 = getRelativePathFromSuiteLevel(paramITestResult);
		paramITestResult.setAttribute("relativeReportDir", str1);
		String str2 = Directory.RUN_DIR + Directory.SEP + str1;
		paramITestResult.setAttribute("iteration", Integer.valueOf(paramITestResult.getMethod().getCurrentInvocationCount() + 1));
		paramITestResult.setAttribute("reportDir", str2);

		return str2;
	}

	public void onFinish(ISuite paramISuite) {
		try {
			this.iSuite = paramISuite;
			String str1 = SettingsFile.get("passedList") + this.passedTests.size() + ';';
			String str2 = SettingsFile.get("failedList") + this.failedTests.size() + ';';
			String str3 = SettingsFile.get("skippedList") + this.skippedTests.size() + ';';
			SettingsFile.set("passedList", str1);
			SettingsFile.set("failedList", str2);
			SettingsFile.set("skippedList", str3);
			HTMLDesignFilesJSWriter.lineChartJS(str1, str2, str3, this.runCount);
			HTMLDesignFilesJSWriter.barChartJS(str1, str2, str3, this.runCount);
			HTMLDesignFilesJSWriter.pieChartJS(this.passedTests.size(), this.failedTests.size(), this.skippedTests.size(),
					this.runCount);
			// generateIndexPage();
			paramISuite.setAttribute("endExecution", Long.valueOf(System.currentTimeMillis()));
			long l = ((Long) paramISuite.getAttribute("startExecution")).longValue();
			generateConsolidatedPage();
			generateCurrentRunPage(l, System.currentTimeMillis());
			// FileUtils.copyFile(new File(Directory.RUN_DIR + Directory.SEP +
			// "CurrentRun.html"),
			// new File(Directory.REPORTSDir + Directory.SEP + "Summary.html"));
			startReportingForPassed(this.passedTests);
			startReportingForFailed(this.failedTests);
			startReportingForSkipped(this.skippedTests);

			if (Directory.generateConfigReports) {
				ConfigurationListener.startConfigurationMethodsReporting(this.runCount);
			}
			/*
			 * if (Directory.recordSuiteExecution) { try { this.recorder.stop();
			 * } catch (Throwable localThrowable) {} }
			 */
			createInternetShortcut(Directory.REPORTSDir + Directory.SEP + "SummaryReport.URL",
					Directory.RUN_DIR + Directory.SEP + "CurrentRun.html", "");
		} catch (Exception localException) {
			throw new IllegalStateException(localException);
		}
	}

	public void onStart(ISuite paramISuite) {
		try {
			paramISuite.setAttribute("startExecution", Long.valueOf(System.currentTimeMillis()));
			new Directory().verifyRequiredFiles();
			SettingsFile.correctErrors();
			int totalRuns = Integer.parseInt(SettingsFile.get("run").trim());

			int maxRuns = Directory.MAX_RUNS_IN_HISTORY;
			if (totalRuns >= maxRuns) {
				int toBeDeleted = totalRuns - maxRuns + 1;
				File[] fileNames = new File(Directory.RESULTSDir).listFiles();
				for (File file : fileNames) {
					if (file.isDirectory() && file.getName().startsWith(Directory.RUN_PREFIX)) {
						int runNum = Integer.parseInt(file.getName().split(Directory.RUN_PREFIX)[1]);
						if (runNum <= toBeDeleted) {
							FileUtils.deleteDirectory(file);
						} else {
							file.renameTo(new File(
									file.getParentFile() + Directory.SEP + Directory.RUN_PREFIX + (runNum - toBeDeleted)));
						}
					}
				}
				totalRuns = maxRuns - 1;
			}

			this.runCount = totalRuns + 1;

			SettingsFile.set("run", "" + this.runCount);
			Directory.RUN_DIR += this.runCount;
			Directory.mkDirs(Directory.RUN_DIR);
			/*
			 * if (Directory.recordSuiteExecution) { try { this.recorder = new
			 * ATUTestRecorder(Directory.RUN_DIR, "ExecutionVideo.mov", false);
			 * this.recorder.start(); } catch (Throwable localThrowable) {} }
			 */
			Directory.mkDirs(Directory.RUN_DIR + Directory.SEP + paramISuite.getName());
			Iterator<?> localIterator = paramISuite.getXmlSuite().getTests().iterator();
			while (localIterator.hasNext()) {
				XmlTest localXmlTest = (XmlTest) localIterator.next();
				Directory.mkDirs(
						Directory.RUN_DIR + Directory.SEP + paramISuite.getName() + Directory.SEP + localXmlTest.getName());
			}
		} catch (Exception localException) {
			throw new IllegalStateException(localException);
		}
	}

	public void generateIndexPage() {
		PrintWriter localPrintWriter = null;
		try {
			localPrintWriter = new PrintWriter(Directory.REPORTSDir + Directory.SEP + "index.html");
			IndexPageWriter.header(localPrintWriter);
			IndexPageWriter.content(localPrintWriter, ReportingUtilities.indexPageDescription);
			IndexPageWriter.footer(localPrintWriter);
			return;
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			try {
				localPrintWriter.close();
			} catch (Exception localException3) {
				localPrintWriter = null;
			}
		}
	}

	public void generateCurrentRunPage(long paramLong1, long paramLong2) {
		PrintWriter localPrintWriter = null;
		try {
			localPrintWriter = new PrintWriter(Directory.RUN_DIR + Directory.SEP + "CurrentRun.html");
			CurrentRunPageWriter.header(localPrintWriter);
			CurrentRunPageWriter.menuLink(localPrintWriter, 0);
			CurrentRunPageWriter.content(localPrintWriter, this.iSuite, this.passedTests, this.failedTests, this.skippedTests,
					ConfigurationListener.passedConfigurations, ConfigurationListener.failedConfigurations,
					ConfigurationListener.skippedConfigurations, this.runCount, paramLong1, paramLong2);
			CurrentRunPageWriter.footer(localPrintWriter);
			return;
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			try {
				localPrintWriter.close();
			} catch (Exception localException3) {
				localPrintWriter = null;
			}
		}
	}

	public void generateConsolidatedPage() {
		PrintWriter localPrintWriter = null;
		try {
			localPrintWriter = new PrintWriter(Directory.RESULTSDir + Directory.SEP + "ConsolidatedPage.html");
			ConsolidatedReportsPageWriter.header(localPrintWriter);
			ConsolidatedReportsPageWriter.menuLink(localPrintWriter, this.runCount);
			ConsolidatedReportsPageWriter.content(localPrintWriter);
			ConsolidatedReportsPageWriter.footer(localPrintWriter);
			return;
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			try {
				localPrintWriter.close();
			} catch (Exception localException3) {
				localPrintWriter = null;
			}
		}
	}

	public void startReportingForPassed(List<ITestResult> paramList) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		// for (;;) {
		while (localIterator.hasNext()) {
			ITestResult localITestResult = (ITestResult) localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
				TestCaseReportsPageWriter.footer(localPrintWriter);
				try {
					localPrintWriter.close();
				} catch (Exception localException1) {
					localPrintWriter = null;
				}
			} catch (FileNotFoundException localFileNotFoundException) {
				localFileNotFoundException.printStackTrace();
			} finally {
				try {
					localPrintWriter.close();
				} catch (Exception localException3) {
					localPrintWriter = null;
				}
			}
		}
		// }
	}

	public void startReportingForFailed(List<ITestResult> paramList) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		// for (;;) {
		while (localIterator.hasNext()) {
			ITestResult localITestResult = (ITestResult) localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
				TestCaseReportsPageWriter.footer(localPrintWriter);
				try {
					localPrintWriter.close();
				} catch (Exception localException1) {
					localPrintWriter = null;
				}
			} catch (FileNotFoundException localFileNotFoundException) {} finally {
				try {
					localPrintWriter.close();
				} catch (Exception localException3) {
					localPrintWriter = null;
				}
			}
		}
		// }
	}

	public void startReportingForSkipped(List<ITestResult> paramList) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		// for (;;) {
		while (localIterator.hasNext()) {
			ITestResult localITestResult = (ITestResult) localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
				TestCaseReportsPageWriter.footer(localPrintWriter);
				try {
					localPrintWriter.close();
				} catch (Exception localException1) {
					localPrintWriter = null;
				}
			} catch (FileNotFoundException localFileNotFoundException) {
				localFileNotFoundException.printStackTrace();
			} finally {
				try {
					localPrintWriter.close();
				} catch (Exception localException3) {
					localPrintWriter = null;
				}
			}
		}
	}

	public static void createInternetShortcut(String shortcutFile, String target, String icon) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter(shortcutFile));

		br.write("[InternetShortcut]\r\nURL=" + "file:///" + target.replaceAll("\\\\", "/") + "\r\n");
		if (!icon.equals("")) {
			br.write("IconFile=" + icon + "\n");
		}
		br.close();
	}
}
