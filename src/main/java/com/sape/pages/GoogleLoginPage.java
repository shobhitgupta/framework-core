package com.sape.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sape.common.Reporter;
import com.sape.common.Utilities;

public class GoogleLoginPage extends BasePage {
    // private static final Logger LOG = Logger.getLogger(PageClass1.class);
    private By lnkSignUp = By.xpath("//span[@id='link-signup']/a");

    public GoogleLoginPage(WebDriver driver) {
        this.driver = driver;
        reporter = new Reporter(driver);
        utils = new Utilities(driver, 15);
        new CommonPage(driver, reporter).waitForPageLoad(120);
        if (!driver.getCurrentUrl().contains("Login")) {
            reporter.warning("Cannot open login page. Current url :" + driver.getCurrentUrl());
            driver.get("accounts.google.com/ServiceLogin");
            new CommonPage(driver, reporter).waitForPageLoad(120);
        }
    }

    public GoogleSignUpPage clickSignUp() {
        utils.click(lnkSignUp);
        return new GoogleSignUpPage(driver);
    }
}
