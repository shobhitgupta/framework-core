package com.sape.common;

import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sape.enums.Browsers;
import com.sape.exceptions.AutomationFrameworkException;

public class WebDriverSetup {
	private static final Logger LOG = Logger.getLogger(WebDriverSetup.class);
	private static final String LIB_DIR = Constants.BASE_DIR + Config.Paths.LIB_DIR + Constants.FS;
	WebDriver driver;
	private final static String INVALID_GRID_URL_TEXT = "invalid selenium grid url: ";

	// public Properties loadConfigFile() {
	// String propsFile = Constants.BASE_DIR + Constants.AUT_INFO_FILE_NAME;
	// Properties autProps = Utilities.getProperties(propsFile);
	//
	// // load environment details
	// Config.Execution.setEnvironment(autProps.getProperty("AUT_ENV"));
	// Config.Execution.setBrowser(autProps.getProperty("AUT_BROWSER"));
	// Config.Execution.setHubUrl(autProps.getProperty("AUT_HUB_URL"));
	// Config.Execution.setExecutionTarget(autProps.getProperty("AUT_EXECUTION_TARGET"));
	// Config.Execution.setAutSuite(autProps.getProperty("AUT_SUITE"));
	//
	// // load application credentials
	// Config.Execution.setBaseUser(autProps.getProperty("AUT_USER1"));
	// Config.Execution.setBaseUserPassword(autProps.getProperty("AUT_USER1_PASSWORD"));
	//
	// return autProps;
	// }

	public WebDriver initDriver() {
		// Properties autProps = loadConfigFile();
		//
		// if (Config.Execution.getEnvironment().length() == 0) {
		// throw new
		// AutomationFrameworkException("unable to fetch execution environment (AUT_ENV) from: "
		// + autProps);
		// } else {
		// LOG.info("execution environment is: " +
		// Config.Execution.getEnvironment());
		// }
		//
		// if (Config.Execution.getBrowser() == Browsers.INVALID) {
		// throw new
		// AutomationFrameworkException("unable to fetch target browser (AUT_BROWSER) from: "
		// + autProps);
		// } else {
		// LOG.info("targer browser is: " + Config.Execution.getBrowser());
		// }
		//
		// if (Config.Execution.getHubUrl().isEmpty()) {
		// throw new
		// AutomationFrameworkException("unable to fetch hub url for selenium grid (AUT_HUB_URL) from: "
		// + autProps);
		// } else {
		// LOG.info("hub url is: " + Config.Execution.getHubUrl());
		// }
		//
		// if (Config.Execution.getExecutionTarget() == ExecutionTarget.INVALID)
		// {
		// throw new AutomationFrameworkException(
		// "unable to fetch execution target for automation tests (AUT_EXECUTION_TARGET) from "
		// + autProps);
		// } else {
		// LOG.info("executing on: " + Config.Execution.getExecutionTarget());
		// }
		//
		// if (Config.Execution.getAutSuite() == Suites.INVALID) {
		// throw new
		// AutomationFrameworkException("unable to fetch execution target for automation tests (AUT_SUITE) from "
		// + autProps);
		// } else {
		// LOG.info("executing suite: " + Config.Execution.getAutSuite());
		// }

		return getWebDriver(Config.Execution.getBrowser());
	}

	public WebDriver getWebDriver(Browsers browserName) {
		int retryCntr = 0;
		while (retryCntr++ < 3) {
			try {
				switch (browserName) {
					case FIREFOX:
						driver = getFirefoxDriver();
						break;
					case INTERNET_EXPLORER:
						driver = getInternetExplorerDriver();
						break;
					case CHROME:
						driver = getChromeDriver();
						break;
					case PHANTOMJS:
						driver = getPhantomJsDriver();
						break;
					default:
						LOG.error("invalid browser: " + browserName.name());
						return null;
				}

				driver.manage().window().maximize();

				setWebDriverTimeouts();

				break;
			} catch (Exception e) {
				if (retryCntr == 3) {
					throw new AutomationFrameworkException("failed to fetch web driver", e);
				} else {
					LOG.warn(e);
				}
			}
		}

		return driver;
	}

	private void setWebDriverTimeouts() {
		if (driver != null) {
			driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT_IN_SECONDS, TimeUnit.SECONDS);
		} else {
			LOG.error("Driver is null. Cannot set driver timeouts.");
		}
	}

	private void maximizeHeadlessBrowserWindow(WebDriver driver) {
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		driver.manage().window().setSize(new Dimension(width, height));
	}

	private WebDriver getFirefoxDriver() {
		return new FirefoxDriver(new ProfilesIni().getProfile("default"));
	}

	private WebDriver getChromeDriver() {
		System.setProperty("webdriver.chrome.driver", LIB_DIR + "chromedriver.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions co = new ChromeOptions();
		co.addArguments(new String[] { "--disable-extensions" });
		capabilities.setCapability(ChromeOptions.CAPABILITY, co);
		return new ChromeDriver(capabilities);
	}

	private WebDriver getInternetExplorerDriver() {
		System.setProperty("webdriver.ie.driver", LIB_DIR + Config.Drivers.IE_DRIVER_EXE);
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		return new InternetExplorerDriver(capabilities);
	}

	private WebDriver getPhantomJsDriver() {
		System.setProperty("phantomjs.binary.path", LIB_DIR + Config.Drivers.PHANTOME_JS_EXE);
		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		capabilities.setCapability("takesScreenshot", true);
		WebDriver localDriver = new HtmlUnitDriver(capabilities);
		maximizeHeadlessBrowserWindow(localDriver);
		return localDriver;
	}
}
