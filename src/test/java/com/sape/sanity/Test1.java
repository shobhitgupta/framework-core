package com.sape.sanity;

import org.testng.annotations.Test;

import com.sape.base.BaseTests;
import com.sape.common.Reporter;
import com.sape.pages.CommonPage;

public class Test1 extends BaseTests {
    @Test
    public void Method1() {
        //new CommonPage(driver).loadApplication(true);
    	driver.get("http://www.google.com");
    	  reporter.fail();
    }

    @Test
    public void Method2() {
        driver.get("https://www.timesofindia.com");
        reporter.pass();
    }
}
