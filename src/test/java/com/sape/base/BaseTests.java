package com.sape.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import reporting.testng.reports.listeners.ConfigurationListener;
import reporting.testng.reports.listeners.MethodListener;
import reporting.testng.reports.listeners.ReportsListener;

import com.sape.common.Config;
import com.sape.common.Constants;
import com.sape.common.Reporter;
import com.sape.common.Utilities;
import com.sape.common.WebDriverSetup;
import com.sape.enums.Browsers;
import com.sape.exceptions.AutomationFrameworkException;

@Listeners({ ReportsListener.class, ConfigurationListener.class, MethodListener.class })
public abstract class BaseTests {
	static {
		// set path for reporting properties file
		System.setProperty("reporter.config", Constants.BASE_DIR + Constants.REPORTING_PROP_FILE_NAME);

	}

	protected WebDriver driver;
	protected Utilities utils;
	protected Reporter reporter;
	private static final Logger LOG = Logger.getLogger(BaseTests.class);

	@BeforeSuite
	public void suiteSetup(ITestContext ctx) {

	}

	@BeforeTest
	public void testSetup(ITestContext ctx) {
		Utilities.killTaskOnWindows(Config.Drivers.IE_DRIVER_EXE);
		Utilities.killTaskOnWindows(Config.Drivers.CHROME_DRIVER_EXE);
		Utilities.sync(5);
	}

	@BeforeClass
	public void classSetup(ITestContext ctx) {
		// Initialize web driver
		String browserName = ctx.getCurrentXmlTest().getParameter("browser");
		this.driver = new WebDriverSetup().getWebDriver(Browsers.fromString(browserName));
		if (this.driver == null) {
			throw new AutomationFrameworkException("unable to fetch browser for: " + Config.Execution.getBrowser());
		}

		// initialize reporter and utilities class
		reporter = new Reporter(driver);
		utils = new Utilities(this.driver, Config.General.IMPLICIT_WAIT_IN_SECONDS);
	}

	@AfterClass(alwaysRun = true)
	public void classTeardown() {
		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			LOG.warn(e);
		}
	}

	@AfterTest(alwaysRun = true)
	public void testTeardown() {
		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			LOG.warn(e);
		}
	}

	@AfterSuite(alwaysRun = true)
	public void suiteTeardown() {
		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			LOG.warn(e);
		}
	}
}