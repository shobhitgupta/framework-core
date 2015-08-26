package com.sape.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sape.common.Reporter;
import com.sape.common.Utilities;

public class GoogleHomePage extends BasePage {
    // private static final Logger LOG = Logger.getLogger(PageClass1.class);
    private By btnSignUp = By.id("gb_70");

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        reporter = new Reporter(driver);
        utils = new Utilities(driver, 15);
        driver.get("http://www.google.com");
        new CommonPage(driver, reporter).waitForPageLoad(120);
    }

    public void search() {
        WebElement searchBox = utils.getElement(By.id("lst-ib"));
        reporter.info("Entering data into google searchbox");
        utils.sendKeys(searchBox, "Googling google");
        reporter.info("Data in textbox = " + searchBox.getAttribute("value"));
        utils.sendKeys(searchBox, Keys.ENTER);
    }

    public void clickSignUp() {
        utils.click(btnSignUp);
        
    }

}
