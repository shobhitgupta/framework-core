package com.sape.sanity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sape.base.BaseTests;

public class Test2 extends BaseTests {
	@Test
	public void googlingGoogle() {
		// new Google(driver).search();
		// Utilities.sync(2);
		reporter.fail();
	}

	@Test(dataProvider = "dp")
	public void yahooingGoogle(int a) {
		// new Yahoo(driver).search();
		// Utilities.sync(2);
		reporter.pass();
	}

	@DataProvider
	Object[][] dp() {
		return new Object[][] { { 1 }, { 2 }, { 3 }, { 4 }, { 5 } };
	}
}
