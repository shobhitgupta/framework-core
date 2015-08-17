package com.reporting.writers;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.reporting.ReportingUtilities;
import com.reporting.enums.ReportLabels;
import com.reporting.utils.Directory;
import com.reporting.utils.Platform;
import com.reporting.utils.Utils;

public class CurrentRunPageWriter extends ReportsPage {
    // private ReportingUtilities repUtils = new ReportingUtilities();

    public static void menuLink(PrintWriter paramPrintWriter, int paramInt) {
        paramPrintWriter.println("\n <tr id=\"container\">\n   <td id=\"menu\">\n       <ul> \n");
        // paramPrintWriter
        // .println("<li class=\"menuStyle\"><a href=\"../../index.html\"
        // >Index</a></li><li style=\"padding-top: 4px;\">"
        // + "<a href=\"../ConsolidatedPage.html\" >Consolidated
        // Page</a></li>\n");

        paramPrintWriter.println("<li class=\"menuStyle\" style=\"background-color: gray;margin-left: 0px; margin-right: 0px;\">"
                + "<a href=\"CurrentRun.html\" style=\"color: white;\">Summary</a></li>");
        paramPrintWriter.println("<li style=\"padding-top: 4px;\">" + "<a href=\"../ConsolidatedPage.html\" >Trends</a></li>\n");
        // if (paramInt == 1) {
        // paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"" +
        // Directory.RUN_PREFIX + paramInt + Directory.SEP
        // + "CurrentRun.html\" >" + "Run " + paramInt + "</a></li>\n");
        // } else {
        // for (int i = 1; i <= paramInt; i++) {
        // if (i == paramInt) {
        // paramPrintWriter.println("\n <li style=\"padding-top:
        // 4px;padding-bottom: 4px;\"><a href=\""
        // + Directory.RUN_PREFIX + i + Directory.SEP + "CurrentRun.html\" >" +
        // "Run " + i + "</a></li>\n");
        // break;
        // }
        // paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"" +
        // Directory.RUN_PREFIX + i + Directory.SEP
        // + "CurrentRun.html\" >" + "Run " + i + "</a></li>\n");
        // }
        // }
        paramPrintWriter.println("\n       </ul>\n   </td>\n\n");
    }

    public static String getExecutionTime(ITestResult paramITestResult) {
        long l = paramITestResult.getEndMillis() - paramITestResult.getStartMillis();
        if (l > 1000L) {
            l /= 1000L;
            return l + " Sec";
        }
        return l + " Milli Sec";
    }

    public static String getExecutionTime(long paramLong1, long paramLong2) {
        long l = paramLong2 - paramLong1;
        if (l > 1000L) {
            l /= 1000L;
            return l + " Sec";
        }
        return l + " Milli Sec";
    }

    public static void content(PrintWriter paramPrintWriter, ISuite paramISuite, List<ITestResult> paramList1,
            List<ITestResult> paramList2, List<ITestResult> paramList3, List<ITestResult> paramList4,
            List<ITestResult> paramList5, List<ITestResult> paramList6, int paramInt, long paramLong1, long paramLong2) {
        int i = paramList1.size() + paramList2.size() + paramList3.size();
        paramPrintWriter.println("<td id=\"content\">\n");
        // paramPrintWriter.println("<div class=\"info\">\nThe following pie
        // chart demonstrates the percentage of Passed, "
        // + "Failed and Skipped Test Cases<br/>\nTime Taken for Executing below
        // Test Cases: <b>"
        // + getExecutionTime(paramLong1, paramLong2) + "</b><br/>\n" + "Current
        // Run Number: <b>Run " + paramInt
        // + "</b>\n</div>\n");

        // summary
        paramPrintWriter.println("<div class=\"chartStyle summary\" style=\"width: 32%;background-color: sienna;\">\n"
                + "<b>Summary</b><br/><br/>\n" + "<table>\n" + "<tr>\n" + "<td>Execution Date</td>\n"
                + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + Utils.getCurrentTime() + "</td>\n" + "</tr>\n"
                + "<tr>\n<td>Duration</td>\n" + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>"
                + getExecutionTime(paramLong1, paramLong2) + "</td>\n" + "</tr>\n" + "<tr>\n<td>Run Number</td>\n"
                + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + paramInt + "</td>\n" + "</tr>\n"
                + "<tr>\n<td>Total Test Cases</td>\n" + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + i + "</td>\n"
                + "</tr>\n" + "<tr>\n" + "<td>Passed</td>\n" + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + paramList1.size()
                + "</td>\n" + "</tr>\n" + "\n" + "<tr>\n" + "<td>Failed</td>\n" + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>"
                + paramList2.size() + "</td>\n" + "</tr>\n" + "\n" + "<tr>\n" + "<td>Skipped</td>\n"
                + "<td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n" + "<td>" + paramList3.size() + "</td>\n" + "</tr>\n" + "</table> \n"
                + "</div>");

        // chart
        paramPrintWriter
                .println("<div class=\"chartStyle\" style=\"text-align: left;margin-left: 30px;float: left;width: 60%;\">\n"
                        + "<div id=\"chart\" style=\"height:300px;color:black;\"></div>\n" + "</div>\n" + "</div>\n" + "<div>\n");

        // run description
        String runDesc = ReportingUtilities.currentRunDescription;
        runDesc = runDesc.isEmpty() ? "No description provided by user" : runDesc;
        paramPrintWriter.println(
                "<div class=\"info\">" + "<b>Run Description&nbsp;&nbsp;:&nbsp;&nbsp;</b>" + runDesc + "</div>" + "<div>\n");

        // recording
        if (Directory.recordSuiteExecution) {
            paramPrintWriter.println(
                    "<p class=\"info\" id=\"showmenu\" style=\"padding-top: 10px;text-align: left;\"><b>Click Me to Show/Hide the Execution Video</b></p>"
                            + "<div id=\"video\" class=\"video\"><object classid=\"clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921\" "
                            + "codebase=\"http://downloads.videolan.org/pub/videolan/vlc/latest/win32/axvlc.cab\" width=\"400\" "
                            + "height=\"300\" id=\"vlc\" events=\"True\">  <param name=\"Src\" value=\"ATU_CompleteSuiteRecording.mov\"></param>"
                            + "<param name=\"ShowDisplay\" value=\"True\" ></param>    <param name=\"AutoLoop\" value=\"no\"></param>"
                            + "<param name=\"AutoPlay\" value=\"no\"></param>"
                            + "<embed type=\"application/x-google-vlc-plugin\" name=\"vlcfirefox\" autoplay=\"no\" "
                            + "loop=\"no\" width=\"99%\"height=\"100%\" target=\"ATU_CompleteSuiteRecording.mov\"></embed></object></div>");
        } else {
            paramPrintWriter.println("<p class=\"info\" id=\"showmenu\" style=\"padding-top: 10px;text-align: left;\">"
                    + "<b>Video recording of execution turned off by user</b></p>");
        }

        // filter
        paramPrintWriter.println("<div style=\"float:left;  color: #585858; font-size: 14px;\"><b>Showing:&nbsp;</b>"
                + "<select id=\"tcFilter\" class=\"filter\">"
                + "\n\t\t\t\t\t\t<option class=\"filterOption\" value=\"all\">All Tests</option>\n\t\t\t\t\t\t"
                + "<option class=\"filterOption\" value=\"pass\">Passed Tests</option>\n\t\t\t\t\t\t"
                + "<option class=\"filterOption\" value=\"fail\">Failed Tests</option>\n\t\t\t\t\t\t"
                + "<option class=\"filterOption\" value=\"skip\">Skipped Test</option>\n\t\t\t\t\t" + "</select>" + "</div>");

        // table
        paramPrintWriter.println("<table id=\"tableStyle\" class=\"chartStyle\" style=\"height:50px; float: left\">\n"
                + "<tr>\n<th>Test Set Name</th>\n<th>Test Class Name</th>\n  "
                + "<th>Test Case Name</th>\n<th>Browser</th>\n<th>Iteration</th>"
                + "<th>Time</th>\n<th style=\"width: 7%\">Status</th>\n" + "</tr>\n");
        writePassedData(paramPrintWriter, paramList1, paramInt);
        writeFailedData(paramPrintWriter, paramList2, paramInt);
        writeSkippedData(paramPrintWriter, paramList3, paramInt);
        writePassedData(paramPrintWriter, paramList4, paramInt);
        writeFailedData(paramPrintWriter, paramList5, paramInt);
        writeSkippedData(paramPrintWriter, paramList6, paramInt);
        paramPrintWriter.print("</table>\n       </div>\n   </td>\n </tr>");

        // filter script
        paramPrintWriter.println(
                "<script language=\"javascript\" type=\"text/javascript\" src=\"../../HTML_Design_Files/JS/tablefilter.js\">\n</script>\n <script>\n  var tableStyleFilters = {\n\t\tbtn: true,\n\t\tbtn_reset: true,\n\t\tcol_3: \"select\",\n\t\tcol_6: \"none\",\n\t\tbtn_text: \"GO\",\n\t\talternate_rows: true,\n\t\tbtn_reset_text: \"Clear All Table Filters\"}\n\t\tsetFilterGrid(\"tableStyle\",0,tableStyleFilters);\n</script>\n");

    }

    private static void writePassedData(PrintWriter paramPrintWriter, List<ITestResult> paramList, int paramInt) {
        String str = "pass";
        Iterator<ITestResult> localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            if (!localITestResult.getMethod().isTest()) {
                str = "config";
            }
            paramPrintWriter.print("<tr class=\"all " + str + "\">\n" + "<td><a href=\""
                    + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getTestSetName(localITestResult) + "</a></td>\n"
                    + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getClassName(localITestResult)
                    + "</a></td>\n" + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">"
                    + getTestCaseName(localITestResult) + "</a></td>\n" + "<td><a href=\""
                    + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getBrowserName(localITestResult) + "</a></td>\n"
                    + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getIteration(localITestResult)
                    + "</a></td>\n" + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">"
                    + getExecutionTime(localITestResult) + "</a></td>\n"
                    + "<td><img  style=\"border: none; width: 25px\" src=\"../../HTML_Design_Files/IMG/pass.png\"></td>\n"
                    + "</tr>\n");
        }
    }

    private static void writeFailedData(PrintWriter paramPrintWriter, List<ITestResult> paramList, int paramInt) {
        String str = "fail";
        Iterator<ITestResult> localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            if (!localITestResult.getMethod().isTest()) {
                str = "config";
            }
            paramPrintWriter.print("<tr class=\"all " + str + "\">\n" + "<td><a href=\""
                    + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getTestSetName(localITestResult) + "</a></td>\n"
                    + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getClassName(localITestResult)
                    + "</a></td>\n" + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">"
                    + getTestCaseName(localITestResult) + "</a></td>\n" + "<td><a href=\""
                    + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getBrowserName(localITestResult) + "</a></td>\n"
                    + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getIteration(localITestResult)
                    + "</a></td>\n" + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">"
                    + getExecutionTime(localITestResult) + "</a></td>\n"
                    + "<td><img  style=\"border: none;width: 25px\" src=\"../../HTML_Design_Files/IMG/fail.png\"></td>\n"
                    + "</tr>\n");
        }
    }

    private static void writeSkippedData(PrintWriter paramPrintWriter, List<ITestResult> paramList, int paramInt) {
        String str = "skip";
        Iterator<ITestResult> localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            if (!localITestResult.getMethod().isTest()) {
                str = "config";
            }
            paramPrintWriter.print("<tr class=\"all " + str + "\">\n" + "<td><a href=\""
                    + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getTestSetName(localITestResult) + "</a></td>\n"
                    + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getClassName(localITestResult)
                    + "</a></td>\n" + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">"
                    + getTestCaseName(localITestResult) + "</a></td>\n" + "<td><a href=\""
                    + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getBrowserName(localITestResult) + "</a></td>\n"
                    + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getIteration(localITestResult)
                    + "</a></td>\n" + "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">"
                    + getExecutionTime(localITestResult) + "</a></td>\n"
                    + "<td><img  style=\" border: none;width: 25px\" src=\"../../HTML_Design_Files/IMG/skip.png\"></td>\n"
                    + "</tr>\n");
        }
    }

    public static String getTestCaseHTMLPath(ITestResult paramITestResult, int paramInt) {
        String str1 = paramITestResult.getAttribute("reportDir").toString();
        int i = (Directory.RUN_PREFIX + paramInt).length();
        String str2 = str1.substring(str1.indexOf(Directory.RUN_PREFIX + paramInt) + (i + 1));
        return str2 + Directory.SEP + getTestCaseName(paramITestResult) + ".html";
    }

    public static String getPackageName(ITestResult paramITestResult) {
        return paramITestResult.getTestClass().getRealClass().getPackage().getName();
    }

    public static String getTestSetName(ITestResult paramITestResult) {
        return paramITestResult.getTestContext().getCurrentXmlTest().getName();
    }

    public static String getClassName(ITestResult paramITestResult) {
        return paramITestResult.getTestClass().getRealClass().getSimpleName();
    }

    public static String getIteration(ITestResult paramITestResult) {
        return paramITestResult.getAttribute("iteration").toString();
    }

    public static String getTestCaseName(ITestResult paramITestResult) {
        return paramITestResult.getName();
    }

    public static String getReportDir(ITestResult paramITestResult) {
        String str1 = paramITestResult.getTestContext().getSuite().getName();
        String str2 = paramITestResult.getTestContext().getCurrentXmlTest().getName();
        String str3 = paramITestResult.getTestClass().getName().replace(".", Directory.SEP);
        String str4 = paramITestResult.getMethod().getMethodName();
        str4 = str4 + "_Iteration" + paramITestResult.getMethod().getCurrentInvocationCount();
        String str5 = str1 + Directory.SEP + str2 + Directory.SEP + str3 + Directory.SEP + str4;
        return str5;
    }

    public static String getMethodType(ITestResult paramITestResult) {
        ITestNGMethod localITestNGMethod = paramITestResult.getMethod();
        if (localITestNGMethod.isAfterClassConfiguration()) {
            return "After Class";
        }
        if (localITestNGMethod.isAfterGroupsConfiguration()) {
            return "After Groups";
        }
        if (localITestNGMethod.isAfterMethodConfiguration()) {
            return "After Method";
        }
        if (localITestNGMethod.isAfterSuiteConfiguration()) {
            return "After Suite";
        }
        if (localITestNGMethod.isAfterTestConfiguration()) {
            return "After Test";
        }
        if (localITestNGMethod.isBeforeClassConfiguration()) {
            return "Before Class";
        }
        if (localITestNGMethod.isBeforeGroupsConfiguration()) {
            return "Before Groups";
        }
        if (localITestNGMethod.isBeforeMethodConfiguration()) {
            return "Before Method";
        }
        if (localITestNGMethod.isBeforeSuiteConfiguration()) {
            return "Before Suite";
        }
        if (localITestNGMethod.isBeforeTestConfiguration()) {
            return "Before Test";
        }
        if (localITestNGMethod.isTest()) {
            return "Test Method";
        }
        return "Unknown";
    }

    public static void header(PrintWriter paramPrintWriter) {
        paramPrintWriter.println(
                "<!DOCTYPE html>\n\n<html>\n    <head>\n        <title>Current Run Reports</title>\n\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"../../HTML_Design_Files/CSS/design.css\" />\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"../../HTML_Design_Files/CSS/jquery.jqplot.css\" />\n\n        <script type=\"text/javascript\" src=\"../../HTML_Design_Files/JS/jquery.min.js\"></script>\n        <script type=\"text/javascript\" src=\"../../HTML_Design_Files/JS/jquery.jqplot.min.js\"></script>\n        <!--[if lt IE 9]>\n        <script language=\"javascript\" type=\"text/javascript\" src=\"../../HTML_Design_Files/JS/excanvas.js\"></script>\n        <![endif]-->\n\n        <script language=\"javascript\" type=\"text/javascript\" src=\"../../HTML_Design_Files/JS/jqplot.pieRenderer.min.js\"></script>\n        <script type=\"text/javascript\" src=\"pieChart.js\"></script>\n");
        paramPrintWriter.print(
                "<script language=\"javascript\" type=\"text/javascript\">$(document).ready(function() { $(\".video\").hide();$(\"#showmenu\").show(); $('#showmenu').click(function(){ $('.video').toggle(\"slide\"); }); });</script><style>#showmenu{text-align:center; padding-top:350px;color: #585858; font-size: 14px;}#video{height: 550px;    margin-top: 5px;    width: 97%;    border-style: solid;    border-width: 1px;    border-color: #21ABCD;    /* Shadow for boxes */    -moz-box-shadow: 0 0 10px #CCCCCC;    -ms-box-shadow: 0 0 10px #CCCCCC;    -webkit-box-shadow: 0 0 10px #CCCCCC;    box-shadow: 0 0 10px #CCCCCC;    zoom: 1;    filter: progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=0),        progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=90),        progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=180),        progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=270);    background-color: white;}</style>");
        paramPrintWriter.println("<script language=\"javascript\" type=\"text/javascript\">\n$(document).ready(function() {\r\n	"
                + "$('#tcFilter').on('change', function() {\r\n		"
                + "if ($(this).val() == 'pass') {\r\n\t\t\t$('.pass').show();\r\n\t\t\t$('.fail').hide();\r\n\t\t\t$('.skip').hide();\r\n\t\t\t}\r\n\t\t"
                + "if ($(this).val() == 'fail') {\r\n\t\t\t$('.pass').hide();\r\n\t\t\t$('.fail').show();\r\n\t\t\t$('.skip').hide();\r\n\t\t\t}\r\n\r\n\t\t"
                + "if ($(this).val() == 'skip') {\r\n\t\t\t$('.pass').hide();\r\n\t\t\t$('.fail').hide();\r\n\t\t\t$('.skip').show();\r\n\t\t\t}\r\n\t\t\r\n\t\t"
                + "if ($(this).val() == 'all') {\r\n\t\t\t$('.pass').show();\r\n\t\t\t$('.fail').show();\r\n\t\t\t$('.skip').show();\r\n\t\t\t}\r\n\t});\r\n});\t"
                + "   \n</script>");
        paramPrintWriter.println(
                "</head>\n	<body>\n		<table id=\"mainTable\">\n <tr id=\"header\" >\n   <td id=\"logo\"><img src=\"../../HTML_Design_Files/IMG/"
                        + ReportLabels.LOGO_LEFT.getLabel() + "\" alt=\"Logo\" height=\"70\" width=\"140\" /> " + "<br/>"
                        + ReportLabels.LOGO_LEFT_CAPTION.getLabel() + "" + "</td>\n" + "<td id=\"headertext\">\n" + ""
                        + ReportLabels.HEADER_TEXT.getLabel() + "<div style=\"padding-right:20px;float:right\">"
                        // + "<img src=\"../../HTML_Design_Files/IMG/"
                        // + ReportLabels.LOGO_RIGHT.getLabel()
                        // + "\" height=\"70\" width=\"140\" />"
                        + "</i></div>" + "</td>\n\n</tr>");
    }

    private static String getBrowserName(ITestResult paramITestResult) {
        try {
            return paramITestResult.getAttribute(Platform.BROWSER_NAME_PROP).toString();
        } catch (Exception localException) {}
        return "";
    }
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.writers.CurrentRunPageWriter
 * 
 * JD-Core Version: 0.7.0.1
 */