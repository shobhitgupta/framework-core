package com.sape.pages;

import org.openqa.selenium.WebDriver;

import com.sape.common.Reporter;
import com.sape.common.Utilities;

public class PageClass1 extends BasePage {
	// private static final Logger LOG = Logger.getLogger(PageClass1.class);

	public PageClass1(WebDriver driver, Reporter reporter) {
		this.driver = driver;
		this.utils = new Utilities(driver, IMPLICIT_WAIT_IN_SECONDS);
	}
}
