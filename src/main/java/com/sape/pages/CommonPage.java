package com.sape.pages;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sape.common.Config;
import com.sape.common.Reporter;
import com.sape.common.Utilities;

public class CommonPage extends BasePage {

    private static final Logger LOG = Logger.getLogger(CommonPage.class);
    private static final long IMPLICIT_WAIT_IN_SECONDS = Config.General.IMPLICIT_WAIT_IN_SECONDS;

    public CommonPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new Utilities(this.driver, IMPLICIT_WAIT_IN_SECONDS);
    }

    /**
     * Waits on the attribute of the specified element
     * 
     * @param element
     * @param attr
     * @param finalValue
     * @param maxWaitForSeconds
     */
    public void waitForAttributeChanged(WebElement element, String attr, String finalValue, int maxWaitForSeconds) {
        WebDriverWait wait = new WebDriverWait(this.driver, maxWaitForSeconds);

        wait.until(new ExpectedCondition<Boolean>() {
            private WebElement element;
            private String attr;
            private String finalValue;

            private ExpectedCondition<Boolean> init(WebElement element, String attr, String finalValue) {
                this.element = element;
                this.attr = attr;
                this.finalValue = finalValue;
                return this;
            }

            public Boolean apply(WebDriver driver) {
                String enabled = element.getAttribute(this.attr);
                if (enabled.equals(this.finalValue))
                    return true;
                else
                    return false;
            }
        }.init(element, attr, finalValue));
    }

    /**
     * launches specified url
     *
     * @param url
     * @param deleteCookies
     *            if true, cookies are deleted before launching the url
     * @author Abhishek Pandey
     */
    private void launchUrl(String url, boolean deleteCookies) {
        if (deleteCookies) {
            driver.manage().deleteAllCookies();
            LOG.info("cookies deleted");
        }
        driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
        driver.get(url);
        LOG.info("url launched: " + url);
    }

    /**
     * picks the url based on target environment (using {@link #getAppUrl()})
     * and navigates to it
     * 
     * @author Abhishek Pandey (22-Jan-2015)
     */
    public void loadApplication(boolean deleteCookies) {
        if (deleteCookies) {
            launchUrl(Config.Execution.getAppUrl(), true);
        } else {
            launchUrl(Config.Execution.getAppUrl(), false);
        }
    }

    /**
     * Returns the handle of the window which contains the URL passed as
     * parameter.
     * 
     * @param url
     *            : URL of the window for which the handle needs to be returned
     * @return Window handle with url containing the parameter <tt>url</tt>
     *
     * @author Sagar Wadhwa
     * @since 1-Apr-2015
     */
    public String getWindowHandleFromURL(String url) {
        String currentHandle = this.driver.getWindowHandle();
        Set<String> handles = this.driver.getWindowHandles();
        String handleRequired = null;
        for (String handle : handles) {
            this.driver.switchTo().window(handle);
            if (this.driver.getCurrentUrl().contains(url)) {
                handleRequired = handle;
                break;
            }
        }
        this.driver.switchTo().window(currentHandle);
        return handleRequired;
    }

    /**
     * target a window by title
     * 
     * @param webDriver
     *            instance of WebDriver
     * @param windowTitle
     *            title to match
     * @return true if successful
     */
    public boolean switchWindow(String windowTitle, long maxWaitInSeconds) {
        boolean found = false;
        long start = System.currentTimeMillis();
        while (!found) {
            try {
                for (String name : driver.getWindowHandles()) {
                    driver.switchTo().window(name);
                    if (driver.getTitle().contains(windowTitle)) {
                        LOG.info("switched to window: " + windowTitle);
                        found = true;
                        break;
                    }
                }
            } catch (Exception e) {
                continue;
            } finally {
                Utilities.sync(1);
                long elapsedMillis = System.currentTimeMillis() - start;
                if (elapsedMillis > maxWaitInSeconds * 1000) {
                    LOG.warn("timed out (" + maxWaitInSeconds + "s) while switching to window: " + windowTitle);
                    break;
                } else {
                    LOG.info("waiting to switch to window: " + windowTitle + "(" + elapsedMillis / 1000.0 + " of "
                            + maxWaitInSeconds + ")");
                }
            }
        }

        if (!found) {
            LOG.warn("window to switch not found: " + windowTitle);
        }

        return found;
    }

    /**
     * Switches to window with the handle passed as parameter and closes the
     * original window from which it is switched.
     * 
     * @param handle
     *            : handle of the window to which the switch needs to be done
     * 
     * @author Sagar Wadhwa
     * @since 1-Apr-2015
     */
    public void closeCurrentWindowAndSwitch(String handle) {
        Reporter.info("Closing window with handle : '" + this.driver.getWindowHandle()
                + "' and switching to window with handle : " + handle, this.driver);
        this.driver.close();
        this.driver.switchTo().window(handle);
        Reporter.vrfy("Handle of the current window should change to handle passed", handle, this.driver.getWindowHandle(),
                this.driver);
    }

}
