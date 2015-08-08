package com.reporting.writers;

import java.io.PrintWriter;

import com.reporting.enums.ReportLabels;

public class IndexPageWriter extends ReportsPage {
	public static void header(PrintWriter paramPrintWriter) {
		paramPrintWriter
				.println("<!DOCTYPE html>\n\n<html>\n<head>\n<title>Execution Summary</title>\n\n "
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"HTML_Design_Files/CSS/design.css\" />\n\n "
						+ "</head>\n<body>\n<table id=\"mainTable\">\n<tr id=\"header\" >\n"
						+ "<td id=\"logo\"><img src=\"HTML_Design_Files/IMG/"
						+ ReportLabels.LOGO_LEFT.getLabel()
						+ "\" alt=\"Logo\" height=\"70\" width=\"140\" /> <br/>"
						+ ReportLabels.LOGO_LEFT_CAPTION.getLabel()
						+ "</td>\n<td id=\"headertext\">\n"
						+ ReportLabels.HEADER_TEXT.getLabel()
						+ "\n<div style=\"padding-right:20px;float:right\">"
						// + "<img src=\"HTML_Design_Files/IMG/"
						// + ReportLabels.LOGO_RIGHT.getLabel()
						// + "\" height=\"70\" width=\"140\" /> "
						+ "</i></div></td>\n</tr>\n\n<tr id=\"container\">\n<td id=\"menu\">\n\n<ul>\n<li class=\"menuStyle\"><a href=\"Results/ConsolidatedPage.html\""
						+ ">Consolidated Reports</a></li>\n</ul>\n</td>");
	}

	public static void content(PrintWriter paramPrintWriter, String paramString) {
		paramPrintWriter.println("<td id=\"content\">\n");
		paramPrintWriter.print(paramString);
		paramPrintWriter.println("                </td>\n            </tr>");
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.writers.IndexPageWriter
 * 
 * JD-Core Version: 0.7.0.1
 */