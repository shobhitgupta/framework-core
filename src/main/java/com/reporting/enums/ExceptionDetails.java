package com.reporting.enums;

public enum ExceptionDetails
{
  org_openqa_selenium_ElementNotVisibleException("Element Not Visible"),  org_openqa_selenium_UnsupportedCommandException("Un Supported Command"),  org_openqa_selenium_remote_UnreachableBrowserException("Unable to connect Browser"),  org_openqa_selenium_UnhandledAlertException("An Alert Box caused problem"),  org_openqa_selenium_support_ui_UnexpectedTagNameException("Invalid TagName"),  org_openqa_selenium_firefox_UnableToCreateProfileException("Unable To Create profile"),  org_openqa_selenium_remote_ScreenshotException("Unable to take Screenshot"),  org_openqa_selenium_remote_SessionTerminatedException("Session Expired"),  org_openqa_selenium_StaleElementReferenceException("Element No More Available"),  org_openqa_selenium_TimeoutException("Time Out Error"),  org_openqa_selenium_interactions_MoveTargetOutOfBoundsException("Error due to Moving Out of window Size"),  org_openqa_selenium_XPathLookupException("InValid XPath"),  org_openqa_selenium_InvalidElementStateException("Invalid Element State"),  org_openqa_selenium_interactions_InvalidCoordinatesException("Invalid Co-ordinates"),  org_openqa_selenium_IllegalLocatorException("The Specified (By.) Locater is illegal"),  org_openqa_selenium_chrome_FatalChromeException("Fatal Chrome Error"),  org_openqa_selenium_remote_ErrorHandler_UnknownServerException("Server Error"),  java_lang_AssertionError("Assertion Error"),  org_openqa_selenium_NoSuchElementException("No such Element exists"),  org_openqa_selenium_NoAlertPresentException("Alert Not Present"),  org_openqa_selenium_NoSuchFrameException("Frame Does Not Exist"),  org_openqa_selenium_NoSuchWindowException("Unable to Identify Window"),  org_openqa_selenium_WebDriverException("WebDriver Exception");
  
  private String exceptionInfo;
  
  public String getExceptionInfo()
  {
    return this.exceptionInfo;
  }
  
  private ExceptionDetails(String paramString)
  {
    this.exceptionInfo = paramString;
  }
}


/* Location:           D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * Qualified Name:     atu.testng.reports.enums.ExceptionDetails
 * JD-Core Version:    0.7.0.1
 */