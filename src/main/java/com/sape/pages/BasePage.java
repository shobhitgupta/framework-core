package com.sape.pages;

import org.openqa.selenium.WebDriver;

import com.sape.common.Config;
import com.sape.common.Utilities;

public abstract class BasePage {
	protected WebDriver driver;
	protected Utilities utils;
	protected static final long IMPLICIT_WAIT_IN_SECONDS = Config.General.IMPLICIT_WAIT_IN_SECONDS;

	public BasePage() {
		utils = new Utilities(driver, 10);
	}
}
