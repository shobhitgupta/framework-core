package com.sape.common;

import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
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
import org.openqa.selenium.remote.RemoteWebDriver;

import com.sape.enums.Browsers;
import com.sape.enums.ExecutionTarget;
import com.sape.enums.Suites;
import com.sape.exceptions.AutomationFrameworkException;

public class WebDriverSetup {
    private static final Logger LOG = Logger.getLogger(WebDriverSetup.class);
    private static final String LIB_DIR = Constants.BASE_DIR + Config.Paths.LIB_DIR + Constants.FS;
    WebDriver driver;
    private Long implicitWaitInSeconds;
    private final static String INVALID_GRID_URL_TEXT = "invalid selenium grid url: ";

    public WebDriverSetup(long implicitWaitInSeconds) {
        this.implicitWaitInSeconds = implicitWaitInSeconds;
    }

    public Properties loadConfigFile() {
        String propsFile = Constants.BASE_DIR + Constants.AUT_INFO_FILE_NAME;
        Properties autProps = Utilities.getProperties(propsFile);

        // load environment details
        Config.Execution.setEnvironment(autProps.getProperty("AUT_ENV"));
        Config.Execution.setBrowser(autProps.getProperty("AUT_BROWSER"));
        Config.Execution.setHubUrl(autProps.getProperty("AUT_HUB_URL"));
        Config.Execution.setExecutionTarget(autProps.getProperty("AUT_EXECUTION_TARGET"));
        Config.Execution.setAutSuite(autProps.getProperty("AUT_SUITE"));

        // load application credentials
        Config.Execution.setBaseUser(autProps.getProperty("AUT_USER1"));
        Config.Execution.setBaseUserPassword(autProps.getProperty("AUT_USER1_PASSWORD"));

        return autProps;
    }

    public WebDriver initDriver() {
        Properties autProps = loadConfigFile();

        if (Config.Execution.getEnvironment().length() == 0) {
            throw new AutomationFrameworkException("unable to fetch execution environment (AUT_ENV) from: " + autProps);
        } else {
            LOG.info("execution environment is: " + Config.Execution.getEnvironment());
        }

        if (Config.Execution.getBrowser() == Browsers.INVALID) {
            throw new AutomationFrameworkException("unable to fetch target browser (AUT_BROWSER) from: " + autProps);
        } else {
            LOG.info("targer browser is: " + Config.Execution.getBrowser());
        }

        if (Config.Execution.getHubUrl().isEmpty()) {
            throw new AutomationFrameworkException("unable to fetch hub url for selenium grid (AUT_HUB_URL) from: " + autProps);
        } else {
            LOG.info("hub url is: " + Config.Execution.getHubUrl());
        }

        if (Config.Execution.getExecutionTarget() == ExecutionTarget.INVALID) {
            throw new AutomationFrameworkException(
                    "unable to fetch execution target for automation tests (AUT_EXECUTION_TARGET) from " + autProps);
        } else {
            LOG.info("executing on: " + Config.Execution.getExecutionTarget());
        }

        if (Config.Execution.getAutSuite() == Suites.INVALID) {
            throw new AutomationFrameworkException("unable to fetch execution target for automation tests (AUT_SUITE) from "
                    + autProps);
        } else {
            LOG.info("executing suite: " + Config.Execution.getAutSuite());
        }

        return createDriverInstance(Config.Execution.getBrowser());
    }

    private WebDriver createDriverInstance(Browsers browserName) {
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

                Utilities.sync(10);

                if (Config.General.MAXIMIZE_WINDOW) {
                    maximizeBrowserWindow();
                }

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

    private void maximizeBrowserWindow() {
        if (!Config.Execution.getBrowser().equals(Browsers.OPENFIN)) {
            long start = System.currentTimeMillis();
            do {
                try {
                    driver.manage().window().maximize();
                    break;
                } catch (Exception e) {
                    LOG.warn("waiting for browser window to load before maximizing", e);
                }
            } while ((System.currentTimeMillis() - start) / 1000 < Constants.Timeouts.HIGH);
        }
    }

    private void setWebDriverTimeouts() {
        if (driver != null) {
            driver.manage().timeouts().pageLoadTimeout(Config.General.PAGE_LOAD_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(this.implicitWaitInSeconds, TimeUnit.SECONDS);

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
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        if (Config.Execution.getExecutionTarget() == ExecutionTarget.LOCAL) {
            return new FirefoxDriver(new ProfilesIni().getProfile("default"));
        } else {
            try {
                return new RemoteWebDriver(new URL(Config.Execution.getHubUrl()), capabilities);
            } catch (MalformedURLException e) {
                throw new AutomationFrameworkException(INVALID_GRID_URL_TEXT + Config.Execution.getHubUrl(), e);
            }
        }
    }

    private WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", LIB_DIR + Config.Drivers.CHROME_DRIVER_EXE);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions co = new ChromeOptions();
        co.addArguments(new String[] { "--disable-extensions", "--no-proxy-server" });
        capabilities.setCapability(ChromeOptions.CAPABILITY, co);
        if (Config.Execution.getExecutionTarget() == ExecutionTarget.LOCAL) {
            return new ChromeDriver(capabilities);
        } else {
            try {
                return new RemoteWebDriver(new URL(Config.Execution.getHubUrl()), capabilities);
            } catch (MalformedURLException e) {
                throw new AutomationFrameworkException(INVALID_GRID_URL_TEXT + Config.Execution.getHubUrl(), e);
            }
        }
    }

    private WebDriver getInternetExplorerDriver() {
        System.setProperty("webdriver.ie.driver", LIB_DIR + Config.Drivers.IE_DRIVER_EXE);
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        if (Config.Execution.getExecutionTarget() == ExecutionTarget.LOCAL) {
            return new InternetExplorerDriver(capabilities);
        } else {
            try {
                return new RemoteWebDriver(new URL(Config.Execution.getHubUrl()), capabilities);
            } catch (MalformedURLException e) {
                throw new AutomationFrameworkException(INVALID_GRID_URL_TEXT + Config.Execution.getHubUrl(), e);
            }
        }
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
