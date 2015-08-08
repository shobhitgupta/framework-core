package com.reporting.writers;

import java.io.PrintWriter;

public class ReportsPage {
	public static void footer(PrintWriter paramPrintWriter) {
		paramPrintWriter
				.println("<tr id=\"footer\">\n                <td colspan=\"2\">\n     "
						+ "Best Viewed in &nbsp;\n <a href=\"http://www.mozilla.org/en-US/firefox/new/\">"
						+ "Firefox</a> &nbsp;\n<a href=\"http://www.apple.com/in/safari/\">Safari</a>&nbsp;\n        "
						+ "<a href=\"http://www.google.com/chrome/\">Chrome</a>&nbsp;\n                  "
						+ "  <a href=\"http://windows.microsoft.com/en-IN/internet-explorer/download-ie\">IE 9 & Above</a>"
						+ "&nbsp;\n&nbsp;&nbsp;\n"
						+ " Reports by: Automation Team\n</td>\n            </tr>\n        </table>\n    </body>\n</html>");
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.writers.ReportsPage
 * 
 * JD-Core Version: 0.7.0.1
 */