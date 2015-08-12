package com.reporting.writers;

import java.io.PrintWriter;

import com.reporting.enums.ReportLabels;
import com.reporting.utils.Directory;

public class ConsolidatedReportsPageWriter extends ReportsPage {
	public static void menuLink(PrintWriter paramPrintWriter, int paramInt) {
		paramPrintWriter.println("\n<tr id=\"container\">\n<td id=\"menu\">\n<ul>\n");
		paramPrintWriter.println("<li class=\"menuStyle\"><a href=\"../index.html\" >Index</a></li>\n");
		if (paramInt == 1) {
			paramPrintWriter.println("\n<li class=\"menuStyle\"><a href=\"" + Directory.RUNName + paramInt + Directory.SEP
					+ "CurrentRun.html\" >" + "Run " + paramInt + "</a></li>\n");
		} else {
			for (int i = 1; i <= paramInt; i++) {
				if (i == paramInt) {
					paramPrintWriter.println("\n<li style=\"padding-top: 4px;padding-bottom: 4px;\"><a href=\""
							+ Directory.RUNName + i + Directory.SEP + "CurrentRun.html\" >" + "Run " + i + "</a></li>\n");
					break;
				}
				paramPrintWriter.println("\n<li class=\"menuStyle\"><a href=\"" + Directory.RUNName + i + Directory.SEP
						+ "CurrentRun.html\" >" + "Run " + i + "</a></li>\n");
			}
		}
		paramPrintWriter.println("\n</ul>\n</td>\n\n");
	}

	public static void header(PrintWriter paramPrintWriter) {
		paramPrintWriter.println("<!DOCTYPE html>\n\n<html>\n<head>\n<title>Execution Summary</title>\n\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../HTML_Design_Files/CSS/design.css\" />\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"../HTML_Design_Files/CSS/jquery.jqplot.css\" />\n"
				+ "<link rel=\"Stylesheet\" type=\"text/css\" href=\"../HTML_Design_Files/CSS/jquery-ui.min.css\"  />\n\n"
				+ "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jquery.min.js\"></script>\n"
				+ "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jquery.jqplot.min.js\"></script>\n"
				+ "<!--[if lt IE 9]>\n<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/excanvas.js\"></script>\n<![endif]-->\n\n\n"
				+ "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.barRenderer.min.js\"></script>\n"
				+ "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.categoryAxisRenderer.min.js\">"
				+ "</script>\n<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.pointLabels.min.js\">"
				+ "</script>\n\n<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.highlighter.min.js\">"
				+ "</script>\n\n<script type=\"text/javascript\" src=\"barChart.js\">"
				+ "</script>\n<script type=\"text/javascript\" src=\"lineChart.js\"></script>\n"
				+ "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jquery-ui.min.js\"></script>\n\n\n"
				+ "<script type=\"text/javascript\">"
				+ "$(document).ready(function() {$(\"#tabs\").tabs();\n$('#tabs').bind('tabsshow', " + "function(event, ui) "
				+ "{if (ui.index === 1 && plot1._drawCount === 0) {plot1.replot();}"
				+ "else if (ui.index === 2 && plot2._drawCount === 0) {plot2.replot();}});});\n"
				+ "</script>\n</head>\n<body>\n<table id=\"mainTable\">\n<tr id=\"header\" >\n<td id=\"logo\"><img src=\"../HTML_Design_Files/IMG/"
				+ ReportLabels.LOGO_LEFT.getLabel() + "\" alt=\"Logo\" height=\"70\" width=\"140\" /><br/>"
				+ ReportLabels.LOGO_LEFT_CAPTION.getLabel() + "</td>\n<td id=\"headertext\">\n"
				+ ReportLabels.HEADER_TEXT.getLabel() + " \n<div style=\"padding-right:20px;float:right\">"
				// + "<img src=\"../HTML_Design_Files/IMG/"
				// + ReportLabels.LOGO_RIGHT.getLabel()
				// + "\" height=\"70\" width=\"140\" />"
				+ "</i></div>" + "</td>\n</tr>\n" + "");
	}

	public static void content(PrintWriter paramPrintWriter) {
		paramPrintWriter.println("<td id=\"content\">\n" + "<p class=\"info\">"
				+ "The following Line chart demonstrates the number of Passed, Failed and Skipped Test Cases in last 10 Runs\n</p>\n"
				+ "<div id=\"tabs\" style=\"float: left;width: 99%;\">\n<ul>\n<li><a href=\"#tabs-1\">Line Chart</a></li>\n"
				+ "<li><a href=\"#tabs-2\">Bar Chart</a></li>\n</ul>\n<div id=\"tabs-1\" style=\"text-align: left;\">\n"
				+ "<div id=\"line\" style=\"height: 270px;  width: 85%; margin-top: 20px;color:black;\"></div>\n\n</div>\n"
				+ "<div id=\"tabs-2\" style=\"text-align: left;\">\n"
				+ "<div id=\"bar\" style=\"margin-top:20px; margin-left:20px; width:85%; height:300px;color:black;\"></div>\n"
				+ "</div>\n</div>\n</td>\n</tr>");

	}
}
