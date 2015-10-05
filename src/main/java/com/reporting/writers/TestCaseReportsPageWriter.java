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
                        + "</script></head>\n<body>\n<table id=\"mainTable\">\n<TR id=\"header\">\n<TD id=\"logo\"><img src=\"../"
                        + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/IMG/" + ReportLabels.LOGO_LEFT.getLabel()
                        + "\"alt=\"Logo\"height=\"80\"width=\"140\"/><br/>" + ReportLabels.LOGO_LEFT_CAPTION.getLabel()
                        + "</TD>\n<TD id=\"headertext\">\n" + ReportLabels.HEADER_TEXT.getLabel()
                        + "\n<div style=\"padding-right:20px;float:right\">"
                        // + "<img src=\"../"
                        // + getTestCaseHTMLPath(paramITestResult)
                        // + "HTML_Design_Files/IMG/"
                        // + ReportLabels.LOGO_RIGHT.getLabel()
                        // + "\"height=\"70\"width=\"140\"/>"
                        + "</TD>\n" + "</i></div></TD>\n</TR>>");
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
        paramPrintWriter.println("<TD id=\"content\">\n");
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
                        + "<TR>\n<TD>Test Case Name</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>" + paramITestResult.getName()
                        + "</TD>\n</TR>>\n\n" + "<TR>\n<TD>Duration</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
                        + getExecutionTime(paramITestResult)
                        + "</TD>\n</TR>>\n\n<TR>\n<TD>Current Run</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>" + paramInt
                        + "</TD>\n" + "</TR>>\n\n<TR>\n<TD>Iteration</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
                        + paramITestResult.getAttribute("iteration").toString() + "</TD>\n</TR>>" + "<TR>\n"
                        + "<TD>Execution Date</TD>\n" + "<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n" + "<TD>" + Utils.getCurrentTime()
                        + "</TD>\n" + "</TR>>\n" + "</table>\n" + "</div>\n" + "");

        paramPrintWriter.println(
                "<div class=\"chartStyle summary\"style=\"background-color: sienna;height:175px;width:47%;margin-left: 20px;\">\n"
                        + "<b>Execution Platform Details</b><br/><br/>\n<table>\n<TR>\n<TD>O.S</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
                        + Platform.OS + ", " + Platform.OS_ARCH + "Bit, v" + Platform.OS_VERSION
                        + "</TD>\n</TR>>\n<TR>\n<TD>Java</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>" + Platform.JAVA_VERSION
                        + "</TD>\n</TR>>\n\n<TR>\n<TD>Hostname</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
                        + Platform.getHostName()
                        + "</TD>\n</TR>>\n\n<TR>\n<TD>Selenium</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
                        + Platform.DRIVER_VERSION + "</TD>\n" + "<TR>\n" + "<TD>Browser</TD>\n"
                        + "<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n" + "<TD>" + getBrowserInfo(paramITestResult) + "</TD>\n"
                        + "</TR>>\n" + "</TR>>\n" + "</table>\n" + "</div>\n" + "");

        // paramPrintWriter.println("<div class=\"chartStyle
        // summary\"style=\"background-color: "
        // + getColorBasedOnResult(paramITestResult) +
        // ";height:175px;width:30%;margin-left: 20px; \">\n"
        // + "<b>Summary</b><br/><br/>\n" + "<table>\n" + "<TR>\n" +
        // "<TD>Status</TD>\n"
        // + "<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n" + "<TD>" +
        // getResult(paramITestResult) + "</TD>\n" + "</TR>>\n" + "<TR>\n"
        // + "<TD>Execution Date</TD>\n" +
        // "<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n" + "<TD>" +
        // Utils.getCurrentTime()
        // + "</TD>\n" + "</TR>>\n" + "\n" + "\n" + "<TR>\n" +
        // "<TD>Browser</TD>\n" + "<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n"
        // + "<TD>" + getBrowserInfo(paramITestResult) + "</TD>\n" + "</TR>>\n"
        // +
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
        // Info</b><br/><br/>\n<table>\n<TR>\n<TD>Author
        // Name</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
        // + localAuthorDetails.getAuthorName()
        // + "</TD>\n</TR>>\n<TR>\n<TD>Creation
        // Date</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
        // + localAuthorDetails.getCreationDate()
        // +
        // "</TD>\n</TR>>\n<TR>\n<TD>Version</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>"
        // + localAuthorDetails.getVersion()
        // + "</TD>\n</TR>>\n<TR>\n<TD>System
        // User</TD>\n<TD>&nbsp;&nbsp;:&nbsp;&nbsp;</TD>\n<TD>" + Platform.USER
        // + "</TD>\n</TR>>\n</table>\n</div>\n" + "");
        //
        Object localObject1;
        int i;
        Object localObject2;
        if (paramITestResult.getStatus() != 3) {
            localObject1 = Reporter.getOutput(paramITestResult);
            paramPrintWriter.println(
                    "<div>\n<table class=\"chartStyle\"id=\"tableStyle\"style=\"height:50px; float: left\">\n<TR>\n<TH>S.No</TH>\n"
                            + "<TH>Step Description</TH>\n<TH>Input Value</TH>\n<TH>Expected Value</TH>\n<TH>Actual Value</TH>\n"
                            + "<TH>Status</TH>\n<TH>Screen shot</TH>\n</TR>>\n\n");
            i = 1;
            if (Reporter.getOutput(paramITestResult).size() <= 0) {
                paramPrintWriter.print("<TR>");
                paramPrintWriter.print("<TD colspan=\"8\"><b>No Steps Available</b></TD>");
                paramPrintWriter.print("</TR>>");
            }
            i = 1;
            Iterator localIterator = ((List) localObject1).iterator();
            while (localIterator.hasNext()) {
                localObject2 = (String) localIterator.next();
                Steps localSteps = (Steps) paramITestResult.getAttribute((String) localObject2);
                if (localSteps == null) {
                    paramPrintWriter.print("<TR>");
                    paramPrintWriter.println("<TD>" + i + "</TD>");
                    paramPrintWriter.print("<TD style=\"text-align:left\"colspan=\"8\">" + (String) localObject2 + "</TD></TR>>");
                    i++;
                } else {
                    paramPrintWriter.print("<TR>");
                    paramPrintWriter.println("<TD>" + i + "</TD>");
                    paramPrintWriter.println("<TD>" + localSteps.getDescription() + "</TD>");
                    paramPrintWriter.println("<TD>" + localSteps.getInputValue() + "</TD>");
                    paramPrintWriter.println("<TD>" + localSteps.getExpectedValue() + "</TD>");
                    paramPrintWriter.println("<TD>" + localSteps.getActualValue() + "</TD>");
                    // paramPrintWriter.println("<TD>" + localSteps.getTime() +
                    // "</TD>");
                    // paramPrintWriter.println("<TD>" + localSteps.getLineNum()
                    // + "</TD>");
                    paramPrintWriter.println("<TD>" + getLogDescription(localSteps.getLogAs(), paramITestResult) + "</TD>");
                    try {
                        long ssNum = Long.parseLong(localSteps.getScreenShot().trim());
                        paramPrintWriter.println("<TD><a href=\"img/" + ssNum + ".PNG\"><img alt=\"Screenshot\"src=\"img/" + ssNum
                                + ".PNG" + "\"/></a></TD>");
                    } catch (Exception e) {
                        paramPrintWriter.println("<TD></TD>");
                    }
                    paramPrintWriter.print("</TR>>");
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
        paramPrintWriter.print("</div>\n\n</TD>\n</TR>>");
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

    public static void menuLink(PrintWriter paramPrintWriter, ITestResult paramITestResult, int runNum) {
        String relPath = getTestCaseHTMLPath(paramITestResult);
        paramPrintWriter.println("\n<TR id=\"container\">\n<TD id=\"menu\">\n<ul>\n");
        paramPrintWriter
                .println("<li class=\"menuStyle\"><a href=\"" + relPath + "CurrentRun.html" + "\">Back To Summary</a></li>\n");
        // paramPrintWriter.println("<li class=\"menuStyle\"><a href=\"../" +
        // getTestCaseHTMLPath(paramITestResult)
        // + "index.html\">Index</a></li>" + "<li style=\"padding-top: 4px;\"><a
        // href=\""
        // + getTestCaseHTMLPath(paramITestResult) +
        // "ConsolidatedPage.html\">Consolidated Page</a></li>\n");
        // if (runNum == 1) {
        // paramPrintWriter.println("\n<li class=\"menuStyle\"><a href=\"" +
        // Directory.RUN_PREFIX + runNum + Directory.SEP
        // + "CurrentRun.html\">" + "Run " + runNum + "</a></li>\n");
        // } else {
        // for (int i = 1; i <= runNum; i++) {
        // if (i == runNum) {
        // paramPrintWriter.println("\n<li style=\"padding-top:
        // 4px;padding-bottom: 4px;\"><a href=\""
        // + Directory.RUN_PREFIX + i + Directory.SEP + "CurrentRun.html\">" +
        // "Run " + i + "</a></li>\n");
        // break;
        // }
        // paramPrintWriter.println("\n<li class=\"menuStyle\"><a href=\"" +
        // Directory.RUN_PREFIX + i + Directory.SEP
        // + "CurrentRun.html\">" + "Run " + i + "</a></li>\n");
        // }
        // }
        paramPrintWriter.println("\n</ul>\n</TD>\n\n");
    }
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.writers.TestCaseReportsPageWriter
 * 
 * JD-Core Version: 0.7.0.1
 */