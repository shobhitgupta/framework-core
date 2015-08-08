package com.sape.sanity;

import org.testng.annotations.Test;

import com.sape.base.BaseTests;
import com.sape.common.Utilities;
import com.sape.pages.PageClass1;

public class Test1 extends BaseTests {
	PageClass1 pc;

	@Test
	public void Method1() {
		driver.get("http://www.google.com");
		pc = new PageClass1(driver);
		pc.method1();
		Utilities.sync(10);
		reporter.fail();
	}

	@Test
	public void Method2() {
		driver.get("http://www.seleniumhq.org");
		reporter.pass();
	}
}
