package com.sape.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sape.common.Reporter;
import com.sape.common.Utilities;

public class Yahoo extends BasePage {
    // private static final Logger LOG = Logger.getLogger(PageClass1.class);

    public Yahoo(WebDriver driver) {
        this.driver = driver;
        reporter = new Reporter(driver);
        utils = new Utilities(driver, 15);
        driver.get("http://www.yahoo.com");
        new CommonPage(driver, reporter).waitForPageLoad(120);
    }

    public void search() {
        WebElement searchBox = utils.getElement(By.id("UHSearchBox"));
        reporter.info("Entering data into yahoo search box");
        utils.sendKeys(searchBox, "Yahooing google");
        reporter.info("Data in textbox = " + searchBox.getAttribute("value"));
        utils.sendKeys(searchBox, Keys.ENTER);
    }

}
