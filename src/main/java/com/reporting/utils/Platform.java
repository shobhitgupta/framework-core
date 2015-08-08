package com.reporting.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.BuildInfo;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Platform
{
  private static BuildInfo driverInfo = new BuildInfo();
  public static final String DRIVER_VERSION = driverInfo.getReleaseLabel();
  public static final String DRIVER_REVISION = driverInfo.getBuildRevision();
  public static final String USER = System.getProperty("user.name");
  public static final String OS = System.getProperty("os.name");
  public static final String OS_ARCH = System.getProperty("os.arch");
  public static final String OS_VERSION = System.getProperty("os.version");
  public static final String JAVA_VERSION = System.getProperty("java.version");
  public static String BROWSER_NAME = "Unknown";
  public static String BROWSER_VERSION = "";
  public static String BROWSER_NAME_PROP = "BrowserName";
  public static String BROWSER_VERSION_PROP = "BrowserVersion";
  
  public static String getHostName()
  {
    try
    {
      return InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException localUnknownHostException) {}
    return "Unknown";
  }
  
  public static void prepareDetails(WebDriver paramWebDriver)
  {
    BROWSER_VERSION = "";
    BROWSER_NAME = "UnKnown";
    if (paramWebDriver == null)
    {
      BROWSER_VERSION = "";
      BROWSER_NAME = "UnKnown";
      return;
    }
    try
    {
      String str = (String)((JavascriptExecutor)paramWebDriver).executeScript("return navigator.userAgent;", new Object[0]);
      if (str.contains("MSIE"))
      {
        BROWSER_VERSION = str.substring(str.indexOf("MSIE") + 5, str.indexOf("Windows NT") - 2);
        BROWSER_NAME = "Internet Explorer";
      }
      else if (str.contains("Firefox/"))
      {
        BROWSER_VERSION = str.substring(str.indexOf("Firefox/") + 8);
        BROWSER_NAME = "Mozilla Firefox";
      }
      else if (str.contains("Chrome/"))
      {
        BROWSER_VERSION = str.substring(str.indexOf("Chrome/") + 7, str.lastIndexOf("Safari/"));
        BROWSER_NAME = "Google Chrome";
      }
      else if ((str.contains("AppleWebKit")) && (str.contains("Version/")))
      {
        BROWSER_VERSION = str.substring(str.indexOf("Version/") + 8, str.lastIndexOf("Safari/"));
        BROWSER_NAME = "Apple Safari";
      }
      else if (str.startsWith("Opera/"))
      {
        BROWSER_VERSION = str.substring(str.indexOf("Version/") + 8);
        BROWSER_NAME = "Opera";
      }
      else
      {
        return;
      }
      getCapabilitiesDetails(paramWebDriver);
    }
    catch (Exception localException1)
    {
      try
      {
        getCapabilitiesDetails(paramWebDriver);
      }
      catch (Exception localException2)
      {
        return;
      }
      return;
    }
    BROWSER_VERSION = "v" + BROWSER_VERSION;
  }
  
  private static void getCapabilitiesDetails(WebDriver paramWebDriver)
  {
    Capabilities localCapabilities = ((RemoteWebDriver)paramWebDriver).getCapabilities();
    BROWSER_NAME = localCapabilities.getBrowserName();
    BROWSER_VERSION = localCapabilities.getVersion();
  }
}


/* Location:           D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * Qualified Name:     atu.testng.reports.utils.Platform
 * JD-Core Version:    0.7.0.1
 */