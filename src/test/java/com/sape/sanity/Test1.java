package com.sape.sanity;

import org.testng.annotations.Test;

import com.sape.base.BaseTests;
import com.sape.common.Utilities;
import com.sape.pages.Google;
import com.sape.pages.Yahoo;

public class Test1 extends BaseTests {
	@Test
	public void googlingGoogle() {
		new Google(driver).search();
		Utilities.sync(2);
		reporter.fail();
	}

	@Test
	public void yahooingGoogle() {
		new Yahoo(driver).search();
		Utilities.sync(2);
		reporter.pass();
	}
}
