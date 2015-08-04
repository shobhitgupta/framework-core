package com.sape.common;

import org.openqa.selenium.WebDriver;

public final class WebDriverManager {
    private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<WebDriver>();

    private WebDriverManager() {

    }

    public static WebDriver getDriver() {
        return WEB_DRIVER.get();
    }

    public static void setWebDriver(WebDriver driver) {
        WEB_DRIVER.set(driver);
    }

}
