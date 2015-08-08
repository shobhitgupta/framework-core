package com.reporting.enums;

public enum ReportLabels {
	HEADER_TEXT("ATU Graphical Reports for Selenium -  TestNG"), PASS("Passed"), FAIL("Failed"),
	SKIP("Skipped"), X_AXIS("Run Number"), Y_AXIS("TC Number"), LINE_CHART_TOOLTIP("Test Cases"),
	LOGO_RIGHT(""), LOGO_RIGHT_CAPTION(
			"<i style=\"float:left;padding-left:20px;font-size:12px\"></i>"), LOGO_LEFT(""),
	LOGO_LEFT_CAPTION("<i style=\"float:left;padding-left:20px;font-size:12px\"></i>"),
	PIE_CHART_LABEL("Test Cases Percent Distribution"), TC_INFO_LABEL(
			"Requirement Coverage/Build Info/Cycle - Description");

	private String label;

	private ReportLabels(String paramString) {
		setLabel(paramString);
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String paramString) {
		this.label = paramString;
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.enums.ReportLabels
 * 
 * JD-Core Version: 0.7.0.1
 */