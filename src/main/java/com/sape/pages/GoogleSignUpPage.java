package com.sape.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sape.common.Reporter;
import com.sape.common.Utilities;

public class GoogleSignUpPage extends BasePage {
    // private static final Logger LOG = Logger.getLogger(PageClass1.class);
    private By edtFirstName = By.id("FirstName");
    private By edtLastName = By.id("LastName");
    private By edtEmailAddress = By.id("GmailAddress");
    private By edtPassword = By.id("Passwd");
    private By edtConfirmPassword = By.id("PasswdAgain");
    private By lblFnameRequired = By.id("errormsg_0_FirstName");
    private By eleBirthdayMonthContainer = By.xpath("//span[@id='BirthMonth']//div[contains(@class,'goog-menu ')]");
    private By eleBirthdayMonthSelector = By.xpath("//span[@id='BirthMonth']//div[@title='Birthday']");
    private By lblPasswordError = By.id("errormsg_0_PasswdAgain");
    private By edtBirthDay = By.id("BirthDay");
    private By edtBirthYear = By.id("BirthYear");

    public GoogleSignUpPage(WebDriver driver) {
        this.driver = driver;
        reporter = new Reporter(driver);
        utils = new Utilities(driver, 15);
        new CommonPage(driver, reporter).waitForPageLoad(120);
        if (!driver.getCurrentUrl().contains("SignUp")) {
            reporter.warning("Cannot open sign up page. Current url :" + driver.getCurrentUrl());
            driver.get("accounts.google.com/SignUp");
            new CommonPage(driver, reporter).waitForPageLoad(120);
        }
    }

    public void enterDetails(String fName, String lName, String password, String bdayYear, String bdayMonth, String bdayDay) {
        utils.click(edtFirstName, false);
        utils.click(edtLastName, false);
        reporter.vrfy("First name required error message should show up", "You can't leave this empty.",
                utils.getElement(lblFnameRequired).getText());
        utils.sendKeys(edtFirstName, fName);
        utils.sendKeys(edtLastName, lName);
        reporter.vrfy("First name required error message should disappear", true, !utils.isClickable(lblFnameRequired, 2));
        utils.sendKeys(edtEmailAddress, fName + lName + "xyzzy");
        utils.sendKeys(edtPassword, password);
        utils.sendKeys(edtConfirmPassword, password.substring(1));
        utils.click(edtPassword, false);
        reporter.vrfy("Passwords don't match error should be present", true, utils.isClickable(lblPasswordError));
        utils.sendKeys(edtConfirmPassword, password);
        reporter.vrfy("Passwords don't match error should disappear", true, !utils.isClickable(lblPasswordError, 2));
        utils.click(eleBirthdayMonthSelector, false);
        List<WebElement> months = utils.getElements(By.xpath("./div"), eleBirthdayMonthContainer);
        boolean monthFound = false;
        for (WebElement month : months) {
            if (bdayMonth.equalsIgnoreCase(month.getText().trim())) {
                utils.click(month, false);
                monthFound = true;
                break;
            }
        }
        reporter.vrfy("Month should be found and selected", true, monthFound);
        utils.sendKeys(edtBirthDay, bdayDay);
        utils.sendKeys(edtBirthYear, bdayYear);

    }

}
