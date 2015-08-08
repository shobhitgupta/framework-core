package com.sape.sanity;

import org.testng.annotations.Test;

import com.sape.base.BaseTests;
import com.sape.common.Utilities;

public class Test2 extends BaseTests {
	@Test
	public void Method3() {
		driver.get("http://www.testng.org");
		Utilities.sync(2);
		reporter.pass();
	}

	@Test
	public void Method4() {
		driver.get("http://www.tutorialspoint.com");
		Utilities.sync(2);
		reporter.fail();
	}
}
