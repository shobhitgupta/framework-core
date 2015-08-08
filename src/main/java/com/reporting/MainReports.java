package com.reporting;


import com.google.common.io.Files;
import com.reporting.exceptions.ReporterStepFailedException;
import com.reporting.logging.LogAs;
import com.reporting.utils.AuthorDetails;
import com.reporting.utils.Directory;
import com.reporting.utils.Platform;
import com.reporting.utils.Steps;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;

public class MainReports
{
  private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
  public static final int MAX_BAR_REPORTS = 10;
  public static final String MESSAGE = "ATU Reporter: Preparing Reports";
  public static String indexPageDescription = "Reports Description";
  public static String currentRunDescription = "Here you can give description about the current Run";
  private static String screenShotNumber;
  private static long lastExecutionTime;
  private static long currentExecutionTime;
  public static final String EMPTY = "";
  public static final String STEP_NUM = "STEP";
  public static final String PASSED_BUT_FAILED = "passedButFailed";
  
  public static void setWebDriver(WebDriver paramWebDriver)
  {
    webDriver.set(paramWebDriver);
    Platform.prepareDetails((WebDriver)webDriver.get());
  }
  
  public static WebDriver getWebDriver()
  {
    return (WebDriver)webDriver.get();
  }
  
  public static void setAuthorInfo(String paramString1, String paramString2, String paramString3)
  {
    AuthorDetails localAuthorDetails = new AuthorDetails();
    localAuthorDetails.setAuthorName(paramString1);
    localAuthorDetails.setCreationDate(paramString2);
    localAuthorDetails.setVersion(paramString3);
    Reporter.getCurrentTestResult().setAttribute("authorInfo", localAuthorDetails);
  }
  
  public static void setTestCaseReqCoverage(String paramString)
  {
    Reporter.getCurrentTestResult().setAttribute("reqCoverage", paramString);
  }
  
  private static void stepFailureHandler(ITestResult paramITestResult, Steps paramSteps, LogAs paramLogAs)
  {
    if (paramLogAs == LogAs.FAILED)
    {
      buildReportData(paramSteps);
      if (Directory.continueExecutionAfterStepFailed) {
        paramITestResult.setAttribute("passedButFailed", "passedButFailed");
      } else {
        throw new ReporterStepFailedException();
      }
      return;
    }
    buildReportData(paramSteps);
  }
  
  public static void add(String paramString, LogAs paramLogAs, CaptureScreen paramCaptureScreen)
  {
    if (paramCaptureScreen != null) {
      if (paramCaptureScreen.isCaptureBrowserPage()) {
        takeBrowserPageScreenShot();
      } else if (paramCaptureScreen.isCaptureDesktop()) {
        takeDesktopScreenshot();
      } else if (paramCaptureScreen.isCaptureWebElement()) {
        takeWebElementScreenShot(paramCaptureScreen.getElement());
      }
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString);
    localSteps.setInputValue("");
    localSteps.setExpectedValue("");
    localSteps.setActualValue("");
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(paramLogAs);
    stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
  }
  
  public static void add(String paramString1, String paramString2, LogAs paramLogAs, CaptureScreen paramCaptureScreen)
  {
    if (paramCaptureScreen != null) {
      if (paramCaptureScreen.isCaptureBrowserPage()) {
        takeBrowserPageScreenShot();
      } else if (paramCaptureScreen.isCaptureDesktop()) {
        takeDesktopScreenshot();
      } else if (paramCaptureScreen.isCaptureWebElement()) {
        takeWebElementScreenShot(paramCaptureScreen.getElement());
      }
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString1);
    localSteps.setInputValue(paramString2);
    localSteps.setExpectedValue("");
    localSteps.setActualValue("");
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(paramLogAs);
    stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
  }
  
  public static void add(String paramString1, String paramString2, String paramString3, LogAs paramLogAs, CaptureScreen paramCaptureScreen)
  {
    if (paramCaptureScreen != null) {
      if (paramCaptureScreen.isCaptureBrowserPage()) {
        takeBrowserPageScreenShot();
      } else if (paramCaptureScreen.isCaptureDesktop()) {
        takeDesktopScreenshot();
      } else if (paramCaptureScreen.isCaptureWebElement()) {
        takeWebElementScreenShot(paramCaptureScreen.getElement());
      }
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString1);
    localSteps.setInputValue("");
    localSteps.setExpectedValue(paramString2);
    localSteps.setActualValue(paramString3);
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(paramLogAs);
    stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
  }
  
  public static void add(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs, CaptureScreen paramCaptureScreen)
  {
    if (paramCaptureScreen != null) {
      if (paramCaptureScreen.isCaptureBrowserPage()) {
        takeBrowserPageScreenShot();
      } else if (paramCaptureScreen.isCaptureDesktop()) {
        takeDesktopScreenshot();
      } else if (paramCaptureScreen.isCaptureWebElement()) {
        takeWebElementScreenShot(paramCaptureScreen.getElement());
      }
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString1);
    localSteps.setInputValue(paramString2);
    localSteps.setExpectedValue(paramString3);
    localSteps.setActualValue(paramString4);
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(paramLogAs);
    stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
  }
  
  private static void buildReportData(Steps paramSteps)
  {
    screenShotNumber = null;
    int i = Reporter.getOutput().size() + 1;
    Reporter.getCurrentTestResult().setAttribute("STEP" + i, paramSteps);
    Reporter.log("STEP" + i);
  }
  
  private static String getExecutionTime()
  {
    currentExecutionTime = System.currentTimeMillis();
    long l = currentExecutionTime - lastExecutionTime;
    if (l > 1000L)
    {
      l /= 1000L;
      lastExecutionTime = currentExecutionTime;
      return l + " Sec";
    }
    lastExecutionTime = currentExecutionTime;
    return l + " Milli Sec";
  }
  
  private static String getLineNumDesc()
  {
    String str = "" + java.lang.Thread.currentThread().getStackTrace()[3].getLineNumber();
    return str;
  }
  
  private static void takeDesktopScreenshot()
  {
    if (!Directory.takeScreenshot) {
      return;
    }
    ITestResult localITestResult = Reporter.getCurrentTestResult();
    String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
    screenShotNumber = Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
    File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
    try
    {
      Rectangle localRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage localBufferedImage = new Robot().createScreenCapture(localRectangle);
      ImageIO.write(localBufferedImage, "PNG", localFile);
    }
    catch (Exception localException)
    {
      screenShotNumber = null;
    }
  }
  
  private static void takeBrowserPageScreenShot()
  {
    if (!Directory.takeScreenshot) {
      return;
    }
    if (getWebDriver() == null)
    {
      screenShotNumber = null;
      return;
    }
    ITestResult localITestResult = Reporter.getCurrentTestResult();
    String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
    screenShotNumber = Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
    File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
    try
    {
      WebDriver localWebDriver;
      if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
        localWebDriver = new Augmenter().augment(getWebDriver());
      } else {
        localWebDriver = getWebDriver();
      }
      if ((localWebDriver instanceof TakesScreenshot))
      {
        byte[] arrayOfByte = (byte[])((TakesScreenshot)localWebDriver).getScreenshotAs(OutputType.BYTES);
        Files.write(arrayOfByte, localFile);
      }
      else
      {
        screenShotNumber = null;
      }
    }
    catch (Exception localException)
    {
      screenShotNumber = null;
    }
  }
  
  private static void takeWebElementScreenShot(WebElement paramWebElement)
  {
    if (!Directory.takeScreenshot) {
      return;
    }
    if (getWebDriver() == null)
    {
      screenShotNumber = null;
      return;
    }
    ITestResult localITestResult = Reporter.getCurrentTestResult();
    String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
    screenShotNumber = Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
    File localFile1 = new File(str + Directory.SEP + screenShotNumber + ".PNG");
    try
    {
      WebDriver localWebDriver;
      if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
        localWebDriver = new Augmenter().augment(getWebDriver());
      } else {
        localWebDriver = getWebDriver();
      }
      if ((localWebDriver instanceof TakesScreenshot))
      {
        File localFile2 = (File)((TakesScreenshot)webDriver.get()).getScreenshotAs(OutputType.FILE);
        BufferedImage localBufferedImage1 = ImageIO.read(localFile2);
        Point localPoint = paramWebElement.getLocation();
        int i = paramWebElement.getSize().getWidth();
        int j = paramWebElement.getSize().getHeight();
        BufferedImage localBufferedImage2 = localBufferedImage1.getSubimage(localPoint.getX(), localPoint.getY(), i, j);
        ImageIO.write(localBufferedImage2, "PNG", localFile1);
      }
      else
      {
        screenShotNumber = null;
      }
    }
    catch (Exception localException)
    {
      screenShotNumber = null;
    }
  }
  
  @Deprecated
  public static void add(String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      takeScreenShot();
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString);
    localSteps.setInputValue("");
    localSteps.setExpectedValue("");
    localSteps.setActualValue("");
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(LogAs.PASSED);
    buildReportData(localSteps);
  }
  
  @Deprecated
  public static void add(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean) {
      takeScreenShot();
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString1);
    localSteps.setInputValue(paramString2);
    localSteps.setExpectedValue("");
    localSteps.setActualValue("");
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(LogAs.PASSED);
    buildReportData(localSteps);
  }
  
  @Deprecated
  public static void add(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    if (paramBoolean) {
      takeScreenShot();
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString1);
    localSteps.setInputValue("");
    localSteps.setExpectedValue(paramString2);
    localSteps.setActualValue(paramString3);
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(LogAs.PASSED);
    buildReportData(localSteps);
  }
  
  @Deprecated
  public static void add(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    if (paramBoolean) {
      takeScreenShot();
    }
    Steps localSteps = new Steps();
    localSteps.setDescription(paramString1);
    localSteps.setInputValue(paramString2);
    localSteps.setExpectedValue(paramString3);
    localSteps.setActualValue(paramString4);
    localSteps.setTime(getExecutionTime());
    localSteps.setLineNum(getLineNumDesc());
    localSteps.setScreenShot(screenShotNumber);
    localSteps.setLogAs(LogAs.PASSED);
    buildReportData(localSteps);
  }
  
  private static void takeScreenShot()
  {
    if (!Directory.takeScreenshot) {
      return;
    }
    if (getWebDriver() == null)
    {
      screenShotNumber = null;
      return;
    }
    ITestResult localITestResult = Reporter.getCurrentTestResult();
    String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
    screenShotNumber = Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
    File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
    try
    {
      WebDriver localWebDriver;
      if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
        localWebDriver = new Augmenter().augment(getWebDriver());
      } else {
        localWebDriver = getWebDriver();
      }
      if ((localWebDriver instanceof TakesScreenshot))
      {
        byte[] arrayOfByte = (byte[])((TakesScreenshot)localWebDriver).getScreenshotAs(OutputType.BYTES);
        Files.write(arrayOfByte, localFile);
      }
      else
      {
        screenShotNumber = null;
      }
    }
    catch (Exception localException)
    {
      screenShotNumber = null;
    }
  }
  
  static
  {
    try
    {
      lastExecutionTime = Reporter.getCurrentTestResult().getStartMillis();
    }
    catch (Exception localException) {}
  }
}


/* Location:           D:\SeleniumFramework\lib\Framework.jar
 * Qualified Name:     atu.testng.reports.ATUReports
 * JD-Core Version:    0.7.0.1
 */