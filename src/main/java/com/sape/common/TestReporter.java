package com.sape.common;

import org.openqa.selenium.WebDriver;

import com.reporting.CaptureScreen;
import com.reporting.ReportingUtilities;
import com.reporting.CaptureScreen.ScreenshotOf;
import com.reporting.logging.LogAs;

public class TestReporter {

	private WebDriver driver;

	public TestReporter(WebDriver driver) {
		this.driver = driver;
	}

	public void fail() {
		/*
		 * new Reporter().report("", "", "Should be successful",
		 * "Is successful", LogAs.PASSED, new CaptureScreen(
		 * ScreenshotOf.BROWSER_PAGE), driver);
		 */

		ReportingUtilities.add("Test", "Dummy", "New", "Actual", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE),
				driver);
	}

	public void pass() {
		/*
		 * new Reporter().report("", "", "Should be successful",
		 * "Is successful", LogAs.PASSED, new CaptureScreen(
		 * ScreenshotOf.BROWSER_PAGE), driver);
		 */

		ReportingUtilities.add("Test", "Dummy", "New", "Actual", LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE),
				driver);
	}

}
