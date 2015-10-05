package com.sape.sanity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sape.base.BaseTests;
import com.sape.common.Utilities;
import com.sape.pages.GoogleHomePage;

public class Test1 extends BaseTests {
    @Test(dataProvider = "dp")
    public void googlingGoogle(String fName, String lName, String password, String bdayYear, String bdayMonth, String bdayDay) {
        new GoogleHomePage(driver).clickSignIn().clickSignUp().enterDetails(fName, lName, password, bdayYear, bdayMonth, bdayDay);
        Utilities.sync(2);
        reporter.pass();
    }

    @DataProvider
    Object[][] dp() {
        return new Object[][] { { "testing", "selenium", "xyzzypasswordissecret", "1992", "September", "16" },
                { "", "selenium", "xyzzypasswordissecret", "1992", "September", "16" },
                { "testing", "selenium", "xyzzypasswordissecret", "1992", "Invalid", "16" } };
    }
}
