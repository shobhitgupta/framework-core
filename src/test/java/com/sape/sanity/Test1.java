package com.sape.sanity;

import org.testng.annotations.Test;

import com.sape.base.BaseTests;

public class Test1 extends BaseTests {
	@Test
	public void Method1() {
		driver.get("http://www.google.com");
		reporter.fail();
	}

	@Test
	public void Method2() {
		driver.get("http://www.seleniumhq.org");
		reporter.pass();
	}
}
