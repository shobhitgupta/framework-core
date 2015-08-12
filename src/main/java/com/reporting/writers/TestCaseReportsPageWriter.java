package com.reporting.writers;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestResult;
import org.testng.Reporter;

import com.reporting.enums.Colors;
import com.reporting.enums.ExceptionDetails;
import com.reporting.enums.ReportLabels;
import com.reporting.logging.LogAs;
import com.reporting.utils.Directory;
import com.reporting.utils.Platform;
import com.reporting.utils.Steps;
import com.reporting.utils.Utils;

public class TestCaseReportsPageWriter extends ReportsPage {
	public static void header(PrintWriter paramPrintWriter, ITestResult paramITestResult) {
		paramPrintWriter.println(
				"<!DOCTYPE html>\n\n<html>\n<head>\n<title>Pie Charts</title>\n\n<link rel=\"stylesheet\"type=\"text/css\"href=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/CSS/design.css\"/>\n<link rel=\"stylesheet\"type=\"text/css\"href=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/CSS/jquery.jqplot.css\"/>\n\n<script type=\"text/javascript\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/jquery.min.js\"></script>\n<script type=\"text/javascript\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/jquery.jqplot.min.js\"></script>\n<!--[if lt IE 9]>\n<script language=\"javascript\"type=\"text/javascript\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/excanvas.js\"></script>\n<![endif]-->\n\n<script language=\"javascript\"type=\"text/javascript\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/jqplot.pieRenderer.min.js\"></script>\n<script language=\"javascript\"type=\"text/javascript\">"
						+ "$(document).ready(function() {" + "$(\".exception\").hide();" + "$(\"#showmenu\").show();"
						+ "$('#showmenu').click(function() {" + "$('.exception').toggle(\"slide\"); });});"
						+ "</script></head>\n<body>\n<table id=\"mainTable\">\n<tr id=\"header\">\n<td id=\"logo\"><img src=\"../"
						+ getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/IMG/" + ReportLabels.LOGO_LEFT.getLabel()
						+ "\"alt=\"Logo\"height=\"80\"width=\"140\"/><br/>" + ReportLabels.LOGO_LEFT_CAPTION.getLabel()
						+ "</td>\n<td id=\"headertext\">\n" + ReportLabels.HEADER_TEXT.getLabel()
						+ "\n<div style=\"padding-right:20px;float:right\">"
						// + "<img src=\"../"
						// + getTestCaseHTMLPath(paramITestResult)
						// + "HTML_Design_Files/IMG/"
						// + ReportLabels.LOGO_RIGHT.getLabel()
						// + "\"height=\"70\"width=\"140\"/>"
						+ "</td>\n" + "</i></div></td>\n</tr>");
	}

	public static String getExecutionTime(ITestResult paramITestResult) {
		long duration = paramITestResult.getEndMillis() - paramITestResult.getStartMillis();
		if (duration > 1000) {
			duration /= 1000;
			if (duration > 60) {
				return duration / 60 + "m " + duration % 60 + "s";
			}
			return duration + "s";
		}
		return duration + "ms";
	}

	private static String getExceptionDetails(ITestResult paramITestResult) {
		try {
			paramITestResult.getThrowable().toString();
		} catch (Throwable localThrowable) {
			return "";
		}
		String str1 = paramITestResult.getThrowable().toString();
		String str2 = str1;
		if (str1.contains(":")) {
			str2 = str1.substring(0, str1.indexOf(":")).trim();
		} else {
			str1 = "";
		}
		try {
			str2 = getExceptionClassName(str2, str1);
			if (str2.equals("Assertion Error")) {
				if (str1.contains(">")) {
					str2 = str2 + str1.substring(str1.indexOf(":"), str1.lastIndexOf(">") + 1).replace(">", "\"");
					str2 = str2.replace("<", "\"");
				}
				if (paramITestResult.getThrowable().getMessage().trim().length() > 0) {
					str2 = paramITestResult.getThrowable().getMessage().trim();
				}
			} else if (str1.contains("{")) {
				str2 = str2 + str1.substring(str1.indexOf("{"), str1.lastIndexOf("}"));
				str2 = str2.replace("{\"method\":", "With ").replace(",\"selector\":", "= ");
			} else if ((str2.equals("Unable to connect Browser")) && (str1.contains("."))) {
				str2 = str2 + ": Browser is Closed";
			} else if (str2.equals("WebDriver Exception")) {
				str2 = paramITestResult.getThrowable().getMessage();
			}
		} catch (ClassNotFoundException localClassNotFoundException) {} catch (Exception localException) {}
		str2 = str2.replace(">", "\"");
		str2 = str2.replace("<", "\"");
		return str2;
	}

	private static String getExceptionClassName(String paramString1, String paramString2) throws ClassNotFoundException {
		String str = "";
		try {
			str = ExceptionDetails.valueOf(paramString1.trim().replace(".", "_")).getExceptionInfo();
		} catch (Exception localException) {
			str = paramString1;
		}
		return str;
	}

	public static String getReqCoverageInfo(ITestResult paramITestResult) {
		// try {
		// if (paramITestResult.getAttribute("reqCoverage") == null) {
		// return ReportLabels.TC_INFO_LABEL.getLabel();
		// }
		// return ReportLabels.TC_INFO_LABEL.getLabel() +
		// paramITestResult.getAttribute("reqCoverage").toString();
		// } catch (Exception localException) {}
		// return ReportLabels.TC_INFO_LABEL.getLabel();

		return paramITestResult.getAttribute("reqCoverage") == null ? ""
				: paramITestResult.getAttribute("reqCoverage").toString();
	}

	public static void content(PrintWriter paramPrintWriter, ITestResult paramITestResult, int paramInt) {
		paramPrintWriter.println("<td id=\"content\">\n");
		// paramPrintWriter.println(
		// "<div class=\"info\">\nThe following table lists down the Sequential
		// Steps during the Run <br/>\nTestCase Name: <b>"
		// + paramITestResult.getName() + ": Iteration " +
		// paramITestResult.getAttribute("iteration").toString()
		// + "</b><br/>" + "Time Taken for Executing: <b>" +
		// getExecutionTime(paramITestResult)
		// + "</b> <br/>\nCurrent Run Number: <b>Run " + paramInt +
		// "</b></br>\nMethod Type: <b>"
		// + CurrentRunPageWriter.getMethodType(paramITestResult) + "</b></br>"
		// + "</div>");
		paramPrintWriter.println("<div style=\"background-color: " + getColorBasedOnResult(paramITestResult)
				+ ";color: white;padding-top: 10px;padding-bottom: 10px;\">Status&nbsp;:&nbsp;" + getResult(paramITestResult)
				+ "</div>");

		paramPrintWriter.println(
				"<div class=\"chartStyle summary\"style=\"background-color: sienna;height:175px;width:47%;\">\n<b>Test Case Summary</b><br/><br/>\n<table>\n"
						+ "<tr>\n<td>Test Case Name</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>" + paramITestResult.getName()
						+ "</td>\n</tr>\n\n" + "<tr>\n<td>Duration</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
						+ getExecutionTime(paramITestResult)
						+ "</td>\n</tr>\n\n<tr>\n<td>Current Run</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>" + paramInt
						+ "</td>\n" + "</tr>\n\n<tr>\n<td>Iteration</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
						+ paramITestResult.getAttribute("iteration").toString() + "</td>\n</tr>" + "<tr>\n"
						+ "<td>Execution Date</td>\n" + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + Utils.getCurrentTime()
						+ "</td>\n" + "</tr>\n" + "</table>\n" + "</div>\n" + "");

		paramPrintWriter.println(
				"<div class=\"chartStyle summary\"style=\"background-color: sienna;height:175px;width:47%;margin-left: 20px;\">\n"
						+ "<b>Execution Platform Details</b><br/><br/>\n<table>\n<tr>\n<td>O.S</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
						+ Platform.OS + ", " + Platform.OS_ARCH + "Bit, v" + Platform.OS_VERSION
						+ "</td>\n</tr>\n<tr>\n<td>Java</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>" + Platform.JAVA_VERSION
						+ "</td>\n</tr>\n\n<tr>\n<td>Hostname</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
						+ Platform.getHostName()
						+ "</td>\n</tr>\n\n<tr>\n<td>Selenium</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
						+ Platform.DRIVER_VERSION + "</td>\n" + "<tr>\n" + "<td>Browser</td>\n"
						+ "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + getBrowserInfo(paramITestResult) + "</td>\n"
						+ "</tr>\n" + "</tr>\n" + "</table>\n" + "</div>\n" + "");

		// paramPrintWriter.println("<div class=\"chartStyle
		// summary\"style=\"background-color: "
		// + getColorBasedOnResult(paramITestResult) +
		// ";height:175px;width:30%;margin-left: 20px; \">\n"
		// + "<b>Summary</b><br/><br/>\n" + "<table>\n" + "<tr>\n" +
		// "<td>Status</td>\n"
		// + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" +
		// getResult(paramITestResult) + "</td>\n" + "</tr>\n" + "<tr>\n"
		// + "<td>Execution Date</td>\n" +
		// "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" +
		// Utils.getCurrentTime()
		// + "</td>\n" + "</tr>\n" + "\n" + "\n" + "<tr>\n" +
		// "<td>Browser</td>\n" + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
		// + "<td>" + getBrowserInfo(paramITestResult) + "</td>\n" + "</tr>\n" +
		// "</table>\n" + "</div>\n\n");

		String reqCovInfo = getReqCoverageInfo(paramITestResult);
		if (reqCovInfo.trim().isEmpty()) {
			reqCovInfo = "No information provided by user";
		}
		paramPrintWriter
				.println("<div class=\"info\"><b>" + ReportLabels.TC_INFO_LABEL.getLabel() + "</b>" + reqCovInfo + "</div>");

		// AuthorDetails localAuthorDetails = null;
		// try {
		// if (paramITestResult.getAttribute("authorInfo") == null) {
		// localAuthorDetails = new AuthorDetails();
		// } else {
		// localAuthorDetails = (AuthorDetails)
		// paramITestResult.getAttribute("authorInfo");
		// }
		// } catch (Exception localException1) {}
		// paramPrintWriter.println(
		// "<div class=\"chartStyle summary\"style=\"background-color:
		// #7F525D;margin-left: 20px; height: 200px;width: 30%; \">\n<b>Author
		// Info</b><br/><br/>\n<table>\n<tr>\n<td>Author
		// Name</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
		// + localAuthorDetails.getAuthorName()
		// + "</td>\n</tr>\n<tr>\n<td>Creation
		// Date</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
		// + localAuthorDetails.getCreationDate()
		// +
		// "</td>\n</tr>\n<tr>\n<td>Version</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>"
		// + localAuthorDetails.getVersion()
		// + "</td>\n</tr>\n<tr>\n<td>System
		// User</td>\n<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n<td>" + Platform.USER
		// + "</td>\n</tr>\n</table>\n</div>\n" + "");
		//
		Object localObject1;
		int i;
		Object localObject2;
		if (paramITestResult.getStatus() != 3) {
			localObject1 = Reporter.getOutput(paramITestResult);
			paramPrintWriter.println(
					"<div>\n<table class=\"chartStyle\"id=\"tableStyle\"style=\"height:50px; float: left\">\n<tr>\n<th>S.No</th>\n"
							+ "<th>Step Description</th>\n<th>Input Value</th>\n<th>Expected Value</th>\n<th>Actual Value</th>\n"
							+ "<th>Status</th>\n<th>Screen shot</th>\n</tr>\n\n");
			i = 1;
			if (Reporter.getOutput(paramITestResult).size() <= 0) {
				paramPrintWriter.print("<tr>");
				paramPrintWriter.print("<td colspan=\"8\"><b>No Steps Available</b></td>");
				paramPrintWriter.print("</tr>");
			}
			i = 1;
			Iterator localIterator = ((List) localObject1).iterator();
			while (localIterator.hasNext()) {
				localObject2 = (String) localIterator.next();
				Steps localSteps = (Steps) paramITestResult.getAttribute((String) localObject2);
				if (localSteps == null) {
					paramPrintWriter.print("<tr>");
					paramPrintWriter.println("<td>" + i + "</td>");
					paramPrintWriter.print("<td style=\"text-align:left\"colspan=\"8\">" + (String) localObject2 + "</td></tr>");
					i++;
				} else {
					paramPrintWriter.print("<tr>");
					paramPrintWriter.println("<td>" + i + "</td>");
					paramPrintWriter.println("<td>" + localSteps.getDescription() + "</td>");
					paramPrintWriter.println("<td>" + localSteps.getInputValue() + "</td>");
					paramPrintWriter.println("<td>" + localSteps.getExpectedValue() + "</td>");
					paramPrintWriter.println("<td>" + localSteps.getActualValue() + "</td>");
					// paramPrintWriter.println("<td>" + localSteps.getTime() +
					// "</td>");
					// paramPrintWriter.println("<td>" + localSteps.getLineNum()
					// + "</td>");
					paramPrintWriter.println("<td>" + getLogDescription(localSteps.getLogAs(), paramITestResult) + "</td>");
					try {
						long ssNum = Long.parseLong(localSteps.getScreenShot().trim());
						paramPrintWriter.println("<td><a href=\"img/" + ssNum + ".PNG\"><img alt=\"Screenshot\"src=\"img/" + ssNum
								+ ".PNG" + "\"/></a></td>");
					} catch (Exception e) {
						paramPrintWriter.println("<td></td>");
					}
					paramPrintWriter.print("</tr>");
					i++;
				}
			}
			paramPrintWriter.print("\n</table>\n");
		}
		if ((paramITestResult.getParameters().length > 0) || (paramITestResult.getStatus() == 3)
				|| (paramITestResult.getStatus() == 2)) {
			paramPrintWriter.println(
					"<div class=\"chartStyle summary\"style=\"color: red;width: 98%; height: inherit; padding-bottom: 10px;\">\n");
			if (paramITestResult.getParameters().length > 0) {
				paramPrintWriter.print("<b>Parameters: </b><br/>");
				for (Object loopVar1 : paramITestResult.getParameters()) {
					paramPrintWriter.print("Param: " + loopVar1.toString() + "<br/>");
				}
				paramPrintWriter.print("<br/><br/>");
			}
			if (paramITestResult.getStatus() == 3) {
				paramPrintWriter.println("<b>Reason for Skipping</b><br/><br/>\n");
				localObject1 = paramITestResult.getMethod().getGroupsDependedUpon();
				String[] arrayOfString = paramITestResult.getMethod().getMethodsDependedUpon();
				String str1;
				String str2;
				if (arrayOfString.length > 0) {
					str1 = "";
					for (String loopVar2 : arrayOfString) {
						str1 = str1 + loopVar2 + "<br/>";
					}
					paramPrintWriter.print("<b>Depends on Groups: </b><br/>" + str1);
				}
				if (arrayOfString.length > 0) {
					str1 = "";
					for (String loopVar3 : arrayOfString) {
						str1 = str1 + loopVar3 + "<br/>";
					}
					paramPrintWriter.print("<b>Depends on Methods: </b><br/>" + str1 + "<br/><br/>");
				}
				paramPrintWriter.print("</div>");
			}
			if (paramITestResult.getStatus() == 2) {
				paramPrintWriter.println("<b>Reason for Failure:&nbsp;&nbsp;</b>" + getExceptionDetails(paramITestResult)
						+ "<br/>\n<b id=\"showmenu\">Click Me to Show/Hide the Full Stack Trace</b>"
						+ "<div class=\"exception\">");
				try {
					paramITestResult.getThrowable().printStackTrace(paramPrintWriter);
				} catch (Exception localException2) {}
				paramPrintWriter.print("</div>");
			}
			paramPrintWriter.print("</div>");
		}
		paramPrintWriter.print("</div>\n\n</td>\n</tr>");
	}

	public static String getLogDescription(LogAs paramLogAs, ITestResult paramITestResult) {
		switch (paramLogAs) {
			case PASSED:
				return "<img style=\"height:20px;width:20px;border:none\"alt=\"PASS\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logpass.png\"/>";
			case FAILED:
				return "<img style=\"height:18px;width:18px;border:none\"alt=\"FAIL\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logfail.png\"/>";
			case INFO:
				return "<img style=\"height:20px;width:20px;border:none\"alt=\"INFO\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/loginfo.png\"/>";
			case WARNING:
				return "<img style=\"height:20px;width:20px;border:none\"alt=\"WARNING\"src=\"../"
						+ getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logwarning.png\"/>";
		}
		return "img";
	}

	public static String getSkippedDescription(ITestResult paramITestResult) {
		String[] arrayOfString1 = paramITestResult.getMethod().getGroupsDependedUpon();
		String[] arrayOfString2 = paramITestResult.getMethod().getMethodsDependedUpon();
		String str1 = "";
		for (String str2 : arrayOfString1) {
			str1 = str1 + str2;
		}
		String str3 = "";
		for (String str4 : arrayOfString2) {
			str3 = str3 + str3;
		}
		return "";
	}

	private static String getBrowserInfo(ITestResult paramITestResult) {
		try {
			return paramITestResult.getAttribute(Platform.BROWSER_NAME_PROP).toString();
		} catch (Exception localException) {}
		return "";
	}

	private static String getBrowserVersion(ITestResult paramITestResult) {
		try {
			return paramITestResult.getAttribute(Platform.BROWSER_VERSION_PROP).toString();
		} catch (Exception localException) {}
		return "";
	}

	private static String getColorBasedOnResult(ITestResult paramITestResult) {
		if (paramITestResult.getStatus() == 1) {
			return Colors.PASS.getColor();
		}
		if (paramITestResult.getStatus() == 2) {
			return Colors.FAIL.getColor();
		}
		if (paramITestResult.getStatus() == 3) {
			return Colors.SKIP.getColor();
		}
		return Colors.PASS.getColor();
	}

	private static String getResult(ITestResult paramITestResult) {
		if (paramITestResult.getStatus() == 1) {
			try {
				if (paramITestResult.getAttribute("passedButFailed").equals("passedButFailed")) {
					return "Failed";
				}
				return "Passed";
			} catch (Exception localException) {
				return "Passed";
			}
		}
		if (paramITestResult.getStatus() == 2) {
			return "Failed";
		}
		if (paramITestResult.getStatus() == 3) {
			return "Skipped";
		}
		return "Unknown";
	}

	public static String getTestCaseHTMLPath(ITestResult paramITestResult) {
		String str = paramITestResult.getAttribute("reportDir").toString();
		str = str.substring(str.indexOf(Directory.RESULTSDir) + (Directory.RESULTSDir.length() + 1));
		String[] arrayOfString = str.replace(Directory.SEP, "##@##@##").split("##@##@##");
		str = "";
		for (int i = 0; i < arrayOfString.length; i++) {
			str = str + "../";
		}
		return str;
	}

	public static void menuLink(PrintWriter paramPrintWriter, ITestResult paramITestResult, int paramInt) {
		getTestCaseHTMLPath(paramITestResult);
		paramPrintWriter.println("\n<tr id=\"container\">\n<td id=\"menu\">\n<ul>\n");
		paramPrintWriter.println("<li class=\"menuStyle\"><a href=\"../" + getTestCaseHTMLPath(paramITestResult)
				+ "index.html\">Index</a></li>" + "<li style=\"padding-top: 4px;\"><a href=\""
				+ getTestCaseHTMLPath(paramITestResult) + "ConsolidatedPage.html\">Consolidated Page</a></li>\n");
		if (paramInt == 1) {
			paramPrintWriter.println("\n<li class=\"menuStyle\"><a href=\"" + Directory.RUNName + paramInt + Directory.SEP
					+ "CurrentRun.html\">" + "Run " + paramInt + "</a></li>\n");
		} else {
			for (int i = 1; i <= paramInt; i++) {
				if (i == paramInt) {
					paramPrintWriter.println("\n<li style=\"padding-top: 4px;padding-bottom: 4px;\"><a href=\""
							+ Directory.RUNName + i + Directory.SEP + "CurrentRun.html\">" + "Run " + i + "</a></li>\n");
					break;
				}
				paramPrintWriter.println("\n<li class=\"menuStyle\"><a href=\"" + Directory.RUNName + i + Directory.SEP
						+ "CurrentRun.html\">" + "Run " + i + "</a></li>\n");
			}
		}
		paramPrintWriter.println("\n</ul>\n</td>\n\n");
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.writers.TestCaseReportsPageWriter
 * 
 * JD-Core Version: 0.7.0.1
 */