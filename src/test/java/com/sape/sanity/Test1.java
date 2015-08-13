package com.sape.sanity;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sape.base.BaseTests;

public class Test1 extends BaseTests {
	@Test(dataProvider = "dp")
	public void googlingGoogle(int a) {
		// new Google(driver).search();
		// Utilities.sync(2);
		reporter.fail();
	}

	@Test
	public void yahooingGoogle() {
		// new Yahoo(driver).search();
		// Utilities.sync(2);
		reporter.pass();
	}

	@DataProvider
	Object[][] dp() {
		return new Object[][] { { 1 }, { 2 }, { 3 }, { 4 }, { 5 } };
	}
}
