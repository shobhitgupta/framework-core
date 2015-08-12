package com.reporting.writers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.reporting.enums.Colors;
import com.reporting.enums.ReportLabels;
import com.reporting.utils.Directory;

public class HTMLDesignFilesJSWriter {
	private static String reduceData(String paramString, int paramInt) {
		int i = 0;
		for (int j = 0; j < paramString.length(); j++) {
			if (paramString.charAt(j) == ',') {
				i++;
				if (i == paramInt) {
					paramString = paramString.substring(j + 1, paramString.length());
				}
			}
		}
		return paramString;
	}

	public static void pieChartJS(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		try {
			PrintWriter localPrintWriter = new PrintWriter(
					Directory.RESULTSDir + Directory.SEP + Directory.RUNName + paramInt4 + Directory.SEP + "pieChart.js");
			localPrintWriter.println("$(document).ready(function() {\n    var data = [['" + ReportLabels.PASS.getLabel() + "', "
					+ paramInt1 + "], ['" + ReportLabels.FAIL.getLabel() + "', " + paramInt2 + "], ['"
					+ ReportLabels.SKIP.getLabel() + "', " + paramInt3 + "]];\n" + "jQuery.jqplot('chart', [data],\n"
					+ "{seriesColors: [\"" + Colors.PASS.getColor() + "\", \"" + Colors.FAIL.getColor() + "\", \""
					+ Colors.SKIP.getColor() + "\"],\n" + "seriesDefaults: {\n" + "// Make this a pie chart.\n"
					+ "renderer: jQuery.jqplot.PieRenderer,\n" + "rendererOptions: {\n" + "padding: 15,\n" + "sliceMargin: 1,\n"
					+ "// Put data labels on the pie slices.\n" + "// By default, labels show the percentage of the slice.\n"
					+ "showDataLabels: true\n" + "}\n" + "},\n" + "grid: {borderColor: '#cccccc', background: '#ffffff',\n"
					+ "borderWidth: 0, // pixel width of border around grid.\n" + "shadow: false // draw a shadow for grid.\n"
					+ "},\n" + "legend: {show: true, location: 'e'}\n" + "}\n" + ");\n" + "});");
			localPrintWriter.close();
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		}
	}

	public static void barChartJS(String paramString1, String paramString2, String paramString3, int paramInt) {
		paramString1 = paramString1.substring(0, paramString1.lastIndexOf(';')).replace(';', ',').trim();
		paramString2 = paramString2.substring(0, paramString2.lastIndexOf(';')).replace(';', ',').trim();
		paramString3 = paramString3.substring(0, paramString3.lastIndexOf(';')).replace(';', ',').trim();
		int i = 0;
		if (paramInt > 10) {
			i = paramInt - 10;
			paramString1 = reduceData(paramString1, i);
			paramString2 = reduceData(paramString2, i);
			paramString3 = reduceData(paramString3, i);
		}
		try {
			PrintWriter localPrintWriter = new PrintWriter(Directory.RESULTSDir + Directory.SEP + "barChart.js");
			localPrintWriter.println("            $(document).ready(function(){\n                var s1 = [" + paramString1
					+ "];\n" + "var s2 = [" + paramString2 + "];\n" + "var s3 = [" + paramString3 + "];\n");
			localPrintWriter.print("var ticks = [");
			for (int j = i + 1; j <= paramInt; j++) {
				localPrintWriter.print(j);
				if (j != paramInt) {
					localPrintWriter.print(",");
				}
			}
			localPrintWriter.print("];");
			localPrintWriter.println(
					"    $.jqplot('bar', [s1, s2, s3], {\n        animate: true,axesDefaults:{min:0,tickInterval: 1},        seriesColors: [\""
							+ Colors.PASS.getColor() + "\", \"" + Colors.FAIL.getColor() + "\", \"" + Colors.SKIP.getColor()
							+ "\"],\n" + "stackSeries: false,\n" + "seriesDefaults: {\n" + "renderer: $.jqplot.BarRenderer,\n"
							+ "pointLabels: {show: true}\n"
							+ ", rendererOptions: {barWidth: 12, barMargin: 25, fillToZero: true}\n" + "}\n" + ",\n"
							+ "grid: {borderColor: '#ffffff', background: '#ffffff',\n"
							+ "borderWidth: 0.5, // pixel width of border around grid.\n"
							+ "shadow: false // draw a shadow for grid.\n" + "}\n" + ",\n" + "legend: {\n" + "show: true,\n"
							+ "location: 'e',\n" + "placement: 'outside',\n" + "labels: ['" + ReportLabels.PASS.getLabel()
							+ "', '" + ReportLabels.FAIL.getLabel() + "', '" + ReportLabels.SKIP.getLabel() + "']\n" + "},\n"
							+ "axes: {\n" + "xaxis: {\n" + "renderer: $.jqplot.CategoryAxisRenderer,\n" + "ticks: ticks,\n"
							+ "label: \"" + ReportLabels.X_AXIS.getLabel() + "\"\n" + "}\n" + ",\n" + "yaxis: {\n" + "label: \""
							+ ReportLabels.Y_AXIS.getLabel() + "\",\n" + "tickOptions: {\n" + "formatString: \"%dtc\"\n" + "}\n"
							+ "}\n" + "}\n" + "});\n" + "});");
			localPrintWriter.close();
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		}
	}

	public static void lineChartJS(String paramString1, String paramString2, String paramString3, int paramInt) {
		paramString1 = paramString1.substring(0, paramString1.lastIndexOf(';')).replace(';', ',').trim();
		paramString2 = paramString2.substring(0, paramString2.lastIndexOf(';')).replace(';', ',').trim();
		paramString3 = paramString3.substring(0, paramString3.lastIndexOf(';')).replace(';', ',').trim();

		int max = getMax(getMax(paramString1) + "," + getMax(paramString1) + "," + getMax(paramString1));
		int yAxisTickInterval = (int) Math.max(1, Math.pow(10, (String.valueOf(max).length() - 1)));

		try {
			PrintWriter localPrintWriter = new PrintWriter(Directory.RESULTSDir + Directory.SEP + "lineChart.js");
			localPrintWriter.println("            $(document).ready(function(){\n                var line1 = [" + paramString1
					+ "];\n" + "var line2 = [" + paramString2 + "];\n" + "var line3 = [" + paramString3 + "];\n");
			localPrintWriter.print("var ticks = [");
			int i = 1;
			if (paramInt == 1) {
				i = 0;
			}
			for (int j = i; j <= paramInt; j++) {
				localPrintWriter.print(j);
				if (j != paramInt) {
					localPrintWriter.print(",");
				}
			}
			localPrintWriter.print("];");
			localPrintWriter
					.print("$.jqplot('line', [line1, line2, line3], {\n        animate: true,\naxesDefaults:{min:0},        seriesDefaults: {\n            rendererOptions: {\n                smooth: true\n            }\n        },\n        series: [{lineWidth: 1.5, label: '"
							+ ReportLabels.PASS.getLabel() + "'},\n" + "{lineWidth: 1.5, label: '" + ReportLabels.FAIL.getLabel()
							+ "'},\n" + "{lineWidth: 1.5, label: '" + ReportLabels.SKIP.getLabel() + "'}],\n" + "axes: {\n"
							+ "xaxis: {\n" + "label: \"" + ReportLabels.X_AXIS.getLabel() + "\",\n" + "ticks: ticks,\n"
							+ "tickOptions: {\n");
			if (paramInt <= 10) {
				localPrintWriter.print("                    formatString: \"%'d Run\"\n");
			} else {
				localPrintWriter.print("                    formatString: \"%'d \"\n");
			}

			localPrintWriter
					.print("                },\n                pad: 1.2,\n                rendererOptions: {\n                    tickInset: 0.3,\n                    minorTicks: 1\n                }\n            },\n            yaxis: {\n                label: \""
							+ ReportLabels.Y_AXIS.getLabel() + "\"\n" + ",tickInterval: " + yAxisTickInterval + "\n" + "}\n"
							+ "},\n" + "highlighter: {\n" + "show: true,\n" + "sizeAdjust: 10,\n" + "tooltipLocation: 'n',\n"
							+ "tooltipAxes: 'y',\n" + "tooltipFormatString: '%d :&nbsp;<b><i><span style=\"color:black;\">"
							+ ReportLabels.LINE_CHART_TOOLTIP.getLabel() + "</span></i></b>',\n" + "useAxesFormatters: false\n"
							+ "},\n" + "cursor: {\n" + "show: true\n" + "},\n"
							+ "grid: {background: '#ffffff', drawGridLines: true, gridLineColor: '#cccccc', borderColor: '#cccccc',\n"
							+ "borderWidth: 0.5, shadow: false},\n"
							+ "legend: {show: true, placement: 'outside', location: 'e'},\n" + "seriesColors: [\""
							+ Colors.PASS.getColor() + "\", \"" + Colors.FAIL.getColor() + "\", \"" + Colors.SKIP.getColor()
							+ "\"]\n" + "});\n" + "});\n" + "");
			localPrintWriter.close();
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		}
	}

	private static int getMax(String paramString1) {
		int max = 0;
		for (String a : paramString1.split(",")) {
			int num = Integer.parseInt(a);
			max = num > max ? num : max;
		}
		return max;
	}
}
