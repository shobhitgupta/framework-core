package com.sape.sanity;

import org.testng.annotations.Test;

import com.sape.base.BaseTests;
import com.sape.common.Reporter;
import com.sape.pages.CommonPage;

public class Test2 extends BaseTests {
    @Test
    public void Method3() {
        //new CommonPage(driver).loadApplication(true);
    	driver.get("http://www.yahoo.com");
        reporter.pass();
    }

    @Test
    public void Method4() {
        driver.get("http://www.tutorialspoint.com");
        reporter.fail();
    }
}
