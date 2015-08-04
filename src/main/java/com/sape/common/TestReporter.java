package com.sape.common;

import org.openqa.selenium.WebDriver;

import reporting.testng.reports.ReportingUtilities;
import reporting.testng.reports.logging.LogAs;
import reporting.testng.selenium.reports.CaptureScreen;
import reporting.testng.selenium.reports.CaptureScreen.ScreenshotOf;

public class TestReporter {
	
	  private WebDriver driver;
	  
	 public TestReporter(WebDriver driver){
		 this.driver=driver; 
	  }
	
	  public void fail() {
	        /*new Reporter().report("", "", "Should be successful", "Is successful", LogAs.PASSED, new CaptureScreen(
	                ScreenshotOf.BROWSER_PAGE), driver);*/
	        
	        ReportingUtilities.add("Test","Dummy", "New","Actual", LogAs.FAILED, new CaptureScreen(
	                ScreenshotOf.BROWSER_PAGE), driver);
	    }
	  
	  public void pass() {
	        /*new Reporter().report("", "", "Should be successful", "Is successful", LogAs.PASSED, new CaptureScreen(
	                ScreenshotOf.BROWSER_PAGE), driver);*/
	        
	        ReportingUtilities.add("Test","Dummy", "New","Actual", LogAs.PASSED, new CaptureScreen(
	                ScreenshotOf.BROWSER_PAGE), driver);
	    }

}
