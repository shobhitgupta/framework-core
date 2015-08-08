package com.reporting;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.google.common.io.Files;
import com.reporting.exceptions.ReporterStepFailedException;
import com.reporting.logging.LogAs;
import com.reporting.utils.AuthorDetails;
import com.reporting.utils.Directory;
import com.reporting.utils.Platform;
import com.reporting.utils.Steps;

public class ReportingUtilities {
	private static WebDriver staticWebDriver;
	public final int MAX_BAR_REPORTS = 10;
	public final String MESSAGE = "ATU Reporter: Preparing Reports";
	public static String indexPageDescription = "Reports Description";
	/**
	 * can be used to specify the details of current run
	 */
	public static String currentRunDescription = "";
	// private static String screenShotNumber;
	private static long lastExecutionTime;
	private static long currentExecutionTime;
	public final String EMPTY = "";
	public final String STEP_NUM = "STEP";
	public final String PASSED_BUT_FAILED = "passedButFailed";

	public static void setWebDriver(WebDriver paramWebDriver) {
		staticWebDriver = paramWebDriver;
		Platform.prepareDetails(staticWebDriver);
	}

	public static WebDriver getWebDriver() {
		return staticWebDriver;
	}

	public static void setAuthorInfo(String paramString1, String paramString2, String paramString3) {
		AuthorDetails localAuthorDetails = new AuthorDetails();
		localAuthorDetails.setAuthorName(paramString1);
		localAuthorDetails.setCreationDate(paramString2);
		localAuthorDetails.setVersion(paramString3);
		Reporter.getCurrentTestResult().setAttribute("authorInfo", localAuthorDetails);
	}

	public static void setTestCaseReqCoverage(String paramString) {
		Reporter.getCurrentTestResult().setAttribute("reqCoverage", paramString);
	}

	private static void stepFailureHandler(ITestResult paramITestResult, Steps paramSteps, LogAs paramLogAs) {
		if (paramLogAs == LogAs.FAILED) {
			buildReportData(paramSteps);
			if (Directory.continueExecutionAfterStepFailed) {
				paramITestResult.setAttribute("passedButFailed", "passedButFailed");
			} else {
				String stepData = paramSteps.getDescription() + " | " + paramSteps.getInputValue() + " | "
						+ paramSteps.getExpectedValue() + " | " + paramSteps.getActualValue();
				throw new ReporterStepFailedException(stepData);
			}
			return;
		}
		buildReportData(paramSteps);
	}

	public static void add(String paramString, LogAs paramLogAs, CaptureScreen paramCaptureScreen, WebDriver driver) {
		add(paramString, "", "", "", paramLogAs, null, driver);
	}

	public static void add(String paramString1, String paramString2, LogAs paramLogAs, CaptureScreen paramCaptureScreen,
			WebDriver driver) {
		add(paramString1, paramString2, "", "", paramLogAs, paramCaptureScreen, driver);
	}

	public static void add(String paramString1, String paramString2, String paramString3, LogAs paramLogAs,
			CaptureScreen paramCaptureScreen, WebDriver driver) {
		add(paramString1, "", paramString2, paramString3, paramLogAs, paramCaptureScreen, driver);
	}

	public static void add(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs,
			CaptureScreen paramCaptureScreen, WebDriver driver) {

		String screenShotNumberLocal = null;
		if (paramCaptureScreen != null) {
			if (paramCaptureScreen.isCaptureBrowserPage()) {
				screenShotNumberLocal = takeBrowserPageScreenShot(driver);
			} else if (paramCaptureScreen.isCaptureDesktop()) {
				screenShotNumberLocal = takeDesktopScreenshot();
			} else if (paramCaptureScreen.isCaptureWebElement()) {
				screenShotNumberLocal = takeWebElementScreenShot(driver, paramCaptureScreen.getElement());
			}
		}
		Steps localSteps = new Steps();
		localSteps.setDescription(paramString1);
		localSteps.setInputValue(paramString2);
		localSteps.setExpectedValue(paramString3);
		localSteps.setActualValue(paramString4);
		localSteps.setTime(getExecutionTime());
		localSteps.setLineNum(getLineNumDesc());
		localSteps.setScreenShot(screenShotNumberLocal);
		localSteps.setLogAs(paramLogAs);
		stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
	}

	private static void buildReportData(Steps paramSteps) {
		// screenShotNumber = null;
		int i = Reporter.getOutput().size() + 1;
		Reporter.getCurrentTestResult().setAttribute("STEP" + i, paramSteps);
		Reporter.log("STEP" + i);
	}

	private static String getExecutionTime() {
		currentExecutionTime = System.currentTimeMillis();
		long l = currentExecutionTime - lastExecutionTime;
		if (l > 1000L) {
			l /= 1000L;
			lastExecutionTime = currentExecutionTime;
			return l + " Sec";
		}
		lastExecutionTime = currentExecutionTime;
		return l + " Milli Sec";
	}

	private static String getLineNumDesc() {
		String str = "" + java.lang.Thread.currentThread().getStackTrace()[3].getLineNumber();
		return str;
	}

	private static String takeDesktopScreenshot() {
		String screenShotNumberLocal = null;
		if (!Directory.takeScreenshot) {
			return screenShotNumberLocal;
		}
		ITestResult localITestResult = Reporter.getCurrentTestResult();
		String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
		// screenShotNumber =
		// Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
		screenShotNumberLocal = getTimeInMillis() + "";
		File localFile = new File(str + Directory.SEP + screenShotNumberLocal + ".PNG");
		try {
			Rectangle localRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage localBufferedImage = new Robot().createScreenCapture(localRectangle);
			ImageIO.write(localBufferedImage, "PNG", localFile);
		} catch (Exception localException) {
			screenShotNumberLocal = null;
		}
		return screenShotNumberLocal;
	}

	private static String takeBrowserPageScreenShot(WebDriver driver) {
		String screenShotNumberLocal = null;
		if (!Directory.takeScreenshot) {
			return screenShotNumberLocal;
		}
		if (driver == null) {
			return screenShotNumberLocal;
		}
		ITestResult localITestResult = Reporter.getCurrentTestResult();
		String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
		// screenShotNumber =
		// Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
		screenShotNumberLocal = getTimeInMillis() + "";
		File localFile = new File(str + Directory.SEP + screenShotNumberLocal + ".PNG");
		try {
			WebDriver localWebDriver;
			if (driver.getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
				localWebDriver = new Augmenter().augment(driver);
			} else {
				localWebDriver = driver;
			}
			if ((localWebDriver instanceof TakesScreenshot)) {
				byte[] arrayOfByte = (byte[]) ((TakesScreenshot) localWebDriver).getScreenshotAs(OutputType.BYTES);
				Files.write(arrayOfByte, localFile);
			}
		} catch (Exception localException) {
			screenShotNumberLocal = null;
		}
		return screenShotNumberLocal;
	}

	private synchronized static long getTimeInMillis() {
		return System.currentTimeMillis();
	}

	private static String takeWebElementScreenShot(WebDriver driver, WebElement paramWebElement) {
		String screenShotNumberLocal = null;
		if (!Directory.takeScreenshot) {
			return screenShotNumberLocal;
		}
		if (driver == null) {
			return screenShotNumberLocal;
		}
		ITestResult localITestResult = Reporter.getCurrentTestResult();
		String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
		// screenShotNumber =
		// Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
		screenShotNumberLocal = getTimeInMillis() + "";
		try {
			File localFile1 = new File(str + Directory.SEP + screenShotNumberLocal + ".PNG");
			WebDriver localWebDriver;
			if (driver.getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
				localWebDriver = new Augmenter().augment(driver);
			} else {
				localWebDriver = driver;
			}
			((JavascriptExecutor) (localWebDriver)).executeScript("arguments[0].focus();", paramWebElement);

			File localFile2 = (File) ((TakesScreenshot) localWebDriver).getScreenshotAs(OutputType.FILE);

			if ((localWebDriver instanceof TakesScreenshot)) {
				BufferedImage localBufferedImage1 = ImageIO.read(localFile2);
				Point localPoint = paramWebElement.getLocation();
				int i = paramWebElement.getSize().getWidth();
				int j = paramWebElement.getSize().getHeight();
				BufferedImage localBufferedImage2 = localBufferedImage1.getSubimage(localPoint.getX(), localPoint.getY(), i, j);
				ImageIO.write(localBufferedImage2, "PNG", localFile1);
			}
		} catch (RasterFormatException e) {
			takeBrowserPageScreenShot(driver);
		} catch (Exception localException) {
			return screenShotNumberLocal;
		}
		return screenShotNumberLocal;
	}

	public void highlight(WebDriver driver, WebElement element) {
		((JavascriptExecutor) (driver)).executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
				"color: yellow; border: 2px solid yellow;");
	}

	public void lowlight(WebDriver driver, WebElement element) {
		((JavascriptExecutor) (driver)).executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
	}

	// @Deprecated
	// public void add(String paramString, boolean paramBoolean) {
	// if (paramBoolean) {
	// takeScreenShot();
	// }
	// Steps localSteps = new Steps();
	// localSteps.setDescription(paramString);
	// localSteps.setInputValue("");
	// localSteps.setExpectedValue("");
	// localSteps.setActualValue("");
	// localSteps.setTime(getExecutionTime());
	// localSteps.setLineNum(getLineNumDesc());
	// localSteps.setScreenShot(screenShotNumber);
	// localSteps.setLogAs(LogAs.PASSED);
	// buildReportData(localSteps);
	// }
	//
	// @Deprecated
	// public void add(String paramString1, String paramString2, boolean
	// paramBoolean) {
	// if (paramBoolean) {
	// takeScreenShot();
	// }
	// Steps localSteps = new Steps();
	// localSteps.setDescription(paramString1);
	// localSteps.setInputValue(paramString2);
	// localSteps.setExpectedValue("");
	// localSteps.setActualValue("");
	// localSteps.setTime(getExecutionTime());
	// localSteps.setLineNum(getLineNumDesc());
	// localSteps.setScreenShot(screenShotNumber);
	// localSteps.setLogAs(LogAs.PASSED);
	// buildReportData(localSteps);
	// }
	//
	// @Deprecated
	// public void add(String paramString1, String paramString2, String
	// paramString3, boolean paramBoolean) {
	// if (paramBoolean) {
	// takeScreenShot();
	// }
	// Steps localSteps = new Steps();
	// localSteps.setDescription(paramString1);
	// localSteps.setInputValue("");
	// localSteps.setExpectedValue(paramString2);
	// localSteps.setActualValue(paramString3);
	// localSteps.setTime(getExecutionTime());
	// localSteps.setLineNum(getLineNumDesc());
	// localSteps.setScreenShot(screenShotNumber);
	// localSteps.setLogAs(LogAs.PASSED);
	// buildReportData(localSteps);
	// }
	//
	// @Deprecated
	// public void add(String paramString1, String paramString2, String
	// paramString3, String paramString4, boolean paramBoolean) {
	// if (paramBoolean) {
	// takeScreenShot();
	// }
	// Steps localSteps = new Steps();
	// localSteps.setDescription(paramString1);
	// localSteps.setInputValue(paramString2);
	// localSteps.setExpectedValue(paramString3);
	// localSteps.setActualValue(paramString4);
	// localSteps.setTime(getExecutionTime());
	// localSteps.setLineNum(getLineNumDesc());
	// localSteps.setScreenShot(screenShotNumber);
	// localSteps.setLogAs(LogAs.PASSED);
	// buildReportData(localSteps);
	// }
	//
	// @Deprecated
	// private void takeScreenShot() {
	// if (!Directory.takeScreenshot) {
	// return;
	// }
	// if (getWebDriver() == null) {
	// screenShotNumber = null;
	// return;
	// }
	// ITestResult localITestResult = Reporter.getCurrentTestResult();
	// String str = localITestResult.getAttribute("reportDir").toString() +
	// Directory.SEP + Directory.IMGDIRName;
	// screenShotNumber =
	// Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1 + "";
	// File localFile = new File(str + Directory.SEP + screenShotNumber +
	// ".PNG");
	// try {
	// WebDriver localWebDriver;
	// if
	// (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver"))
	// {
	// localWebDriver = new Augmenter().augment(getWebDriver());
	// } else {
	// localWebDriver = getWebDriver();
	// }
	// if ((localWebDriver instanceof TakesScreenshot)) {
	// byte[] arrayOfByte = (byte[]) ((TakesScreenshot)
	// localWebDriver).getScreenshotAs(OutputType.BYTES);
	// Files.write(arrayOfByte, localFile);
	// } else {
	// screenShotNumber = null;
	// }
	// } catch (Exception localException) {
	// screenShotNumber = null;
	// }
	// }
	//
	// {
	// try {
	// lastExecutionTime = Reporter.getCurrentTestResult().getStartMillis();
	// } catch (Exception localException) {}
	// }
}
