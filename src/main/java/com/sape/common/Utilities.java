package com.sape.common;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sape.enums.Browsers;
import com.sape.exceptions.AutomationFrameworkException;

public class Utilities {
	private WebDriver driver;
	private Reporter reporter;
	private static final Logger LOG = Logger.getLogger(Utilities.class);
	private long implicitWaitInSeconds;

	public Utilities(WebDriver driver, long implicitWaitInSeconds) {
		this.driver = driver;
		this.implicitWaitInSeconds = implicitWaitInSeconds;
	}

	public static String getTimestamp() {
		String ts = new Timestamp(new Date().getTime()).toString();
		ts = ts.replace("-", "").replace(" ", "_").replace(":", "").split("\\.")[0];
		return ts;
	}

	/**
	 * clicks on a webelement specified by By
	 * 
	 * @param by
	 * @param jsClick
	 *            if true, javascript click is performed, else
	 *            {@link #org.openqa.selenium.WebElement.click()
	 *            org.openqa.selenium.WebElement.click()} is used
	 * @author Abhishek Pandey @
	 */
	public boolean click(By by, boolean jsClick) {
		try {
			WebElement element = getElement(by);
			if (element == null) {
				throw new AutomationFrameworkException("unable to locate element for clicking. " + by.toString());
			}

			new WebDriverWait(driver, implicitWaitInSeconds).until(ExpectedConditions.elementToBeClickable(element));
			if (jsClick) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", element);
				LOG.info(getCallingFileNameAndLineNumber() + " clicked element " + by.toString());
			} else {
				driver.findElement(by).click();
			}

		} catch (Exception e) {
			throw new AutomationFrameworkException("failed to click on object", e);
		}

		return true;
	}

	/**
	 * equivalent to {@link #click(By, boolean) click(by, true)}
	 * 
	 * @param by
	 * @author Abhishek Pandey @
	 */
	public void click(By by) {
		if (Config.Execution.getBrowser() == Browsers.PHANTOMJS) {
			click(by, false);
		} else {
			click(by, true);
		}
	}

	/**
	 * equivalent to {@link #click(By, boolean) click(by, true)}
	 * 
	 * @param by
	 * @author Abhishek Pandey @
	 */
	public void click(By by, long maxWaitInSeconds) {
		if (Config.Execution.getBrowser() == Browsers.PHANTOMJS) {
			click(getElement(by, maxWaitInSeconds), false);
		} else {
			click(getElement(by, maxWaitInSeconds), true);
		}
	}

	/**
	 * clicks the specified WebElement
	 * 
	 * @param element
	 * @param jsClick
	 *            if true, javascript click is performed, else <i>
	 *            org.openqa.selenium.WebElement.click() </i>method is used
	 * @author Abhishek Pandey
	 */
	public void click(WebElement element, boolean jsClick) {
		try {
			if (!isClickable(element)) {
				LOG.warn("element not clickable");
				return;
			}
			if (jsClick) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", element);
			} else {
				element.click();
			}

			LOG.info(getCallingFileNameAndLineNumber() + " clicked element " + getXpath(element));
		} catch (StaleElementReferenceException e) {
			LOG.error(getCallingFileNameAndLineNumber() + " exception: ", e);
		} catch (WebDriverException e) {
			if (e.getMessage().contains("Element is not clickable at point")) {
				throw new AutomationFrameworkException("Element is not clickable at point specified", e);
			} else {
				LOG.error(getCallingFileNameAndLineNumber() + " exception: ", e);
			}
		} catch (Exception e) {
			LOG.error(getCallingFileNameAndLineNumber() + " exception: ", e);
		}
	}

	/**
	 * equivalent to {@link #click(WebElement, boolean) click(element, true)}
	 * 
	 * @param by
	 * @author Abhishek Pandey
	 */
	public void click(WebElement element) {
		if (Config.Execution.getBrowser() == Browsers.PHANTOMJS) {
			click(element, false);
		} else {
			click(element, true);
		}
	}

	/**
	 * checks if the string array passed to it is sorted or not.
	 * 
	 * @param String
	 *            []
	 * @author Sagar Wadhwa
	 */
	public static boolean isSorted(String[] data) {
		for (int i = 1; i < data.length - 1; i++) {
			if (data[i - 1].compareTo(data[i]) > 0) {
				LOG.info(data[i - 1] + " appears before " + data[i]);
				return false;
			}
		}
		return true;
	}

	/**
	 * checks if the string array passed to it is sorted or not.
	 * 
	 * @param String
	 *            []
	 * @author Sagar Wadhwa
	 */
	public boolean verifySorting(String[] columnData) {
		for (int i = 1; i < columnData.length; i++) {
			if (columnData[i - 1].compareTo(columnData[i]) > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checks if the String List passed to it is sorted or not.
	 * 
	 * @param list
	 *            The list to be checked
	 * 
	 * @param startIndex
	 *            The index from which the list is to be checked for sorting
	 * 
	 * @author Sagar Wadhwa
	 * @since (23-Mar-2015)
	 *
	 */
	public boolean verifyListSorting(List<String> list, int startIndex) {
		reporter.vrfy("Start index " + startIndex + " should not be less than 1", true, startIndex >= 1);
		reporter.vrfy("Start index " + startIndex + " should be less than max available index in list " + list.size(), true,
				startIndex < list.size());
		for (int i = startIndex; i < list.size(); i++) {
			if (list.get(i - 1).compareTo(list.get(i)) > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * equivalent to {@link #verifyListSorting(List, int)
	 * verifyListSorting(list, 0)}
	 * 
	 */
	public boolean verifyListSorting(List<String> list) {
		return verifyListSorting(list, 1);
	}

	public void sendKeys(By by, String str) {
		WebElement element = getElement(by);
		if (element == null) {
			LOG.error(getCallingFileNameAndLineNumber() + "unable to find element using " + by.toString());
			throw new AutomationFrameworkException("unable to find element using " + by.toString());
		}

		if (!isClickable(element)) {
			throw new AutomationFrameworkException("element not enabled: " + by.toString());
		}

		element.clear();
		element.sendKeys(str);
		String exp = str;
		String act = element.getAttribute("value");
		if ("password".equalsIgnoreCase(element.getAttribute("type"))) {
			if (exp.equalsIgnoreCase(act)) {
				LOG.info(getCallingFileNameAndLineNumber() + "password set");
			} else {
				LOG.error(getCallingFileNameAndLineNumber() + "password not set");
				throw new AutomationFrameworkException("failed to set password");
			}
		} else {
			if (exp.equalsIgnoreCase(act)) {
				LOG.info(getCallingFileNameAndLineNumber() + "value set: " + act);
			} else {
				LOG.error(getCallingFileNameAndLineNumber() + "value not set exp: " + exp + ", act: " + act);
				throw new AutomationFrameworkException("failed to set value");
			}
		}
	}

	public void sendKeysChord(WebElement element, String str) {
		if (element == null) {
			LOG.error(getCallingFileNameAndLineNumber() + "Element null");
			throw new AutomationFrameworkException("Element null ");
		}

		if (!isClickable(element)) {
			throw new AutomationFrameworkException("element not enabled ");
		}
		element.sendKeys(str);
	}

	public void sendKeys(WebElement element, String str) {
		if (element == null) {
			LOG.error(getCallingFileNameAndLineNumber() + "Element null");
			throw new AutomationFrameworkException("Element null ");
		}

		if (!isClickable(element)) {
			throw new AutomationFrameworkException("element not enabled ");
		}

		element.clear();
		element.sendKeys(str);
		String exp = str;
		String act = element.getAttribute("value");
		if ("password".equalsIgnoreCase(element.getAttribute("type"))) {
			if (exp.equalsIgnoreCase(act)) {
				LOG.info(getCallingFileNameAndLineNumber() + "password set");
			} else {
				LOG.error(getCallingFileNameAndLineNumber() + "password not set");
				throw new AutomationFrameworkException("failed to set password");
			}
		} else {
			if (exp.equalsIgnoreCase(act)) {
				LOG.info(getCallingFileNameAndLineNumber() + "value set: " + act);
			} else {
				LOG.error(getCallingFileNameAndLineNumber() + "value not set exp: " + exp + ", act: " + act);
				throw new AutomationFrameworkException(getCallingFileNameAndLineNumber() + "failed to set value");
			}
		}
	}

	public void sendKeys(By by, Keys key) {
		WebElement element = getElement(by);
		if (element == null) {
			LOG.error(getCallingFileNameAndLineNumber() + "unable to find element using " + by.toString());
			throw new AutomationFrameworkException("unable to find element using " + by.toString());
		}

		sendKeys(element, key);
	}

	public void sendKeys(WebElement element, Keys key) {
		if (!isClickable(element)) {
			throw new AutomationFrameworkException("element not enabled");
		}

		element.sendKeys(key);
	}

	/**
	 * looks for an element within 'parent' using the locator provided by 'by'.
	 * if parent is null, 'driver' passed to the constructor is used.
	 * 'maxWaitInSec' is the maximum time to look for element before returning
	 * null. if 'maxWaitInSec' is -1, the global implicit wait value defined in
	 * config.json is used
	 * 
	 * @author Sagar Wadhwa (04-Mar-2015)
	 * @param by
	 * @param parent
	 * @param maxWaitInSec
	 * @return
	 */
	public WebElement getElement(By by, WebElement parent, long maxWaitInSec) {
		return getElement(by, parent, maxWaitInSec, false);
	}

	public WebElement getElement(By by, WebElement parent, long maxWaitInSec, boolean silent) {
		try {
			List<WebElement> allElements = getElements(by, parent, maxWaitInSec);
			if (allElements.isEmpty()) {
				if (!silent) {
					LOG.warn("unable to locate element " + by.toString());
				}
				return null;
			} else {
				return allElements.get(0);
			}
		} catch (Exception e) {
			LOG.error(getCallingFileNameAndLineNumber() + "exception occured", e);
			return null;
		}
	}

	/**
	 * equivalent to {@link #getElement(By, WebElement, long) getElement(by,
	 * parent, -1)}
	 * 
	 * @author Sagar Wadhwa
	 * @since(04-Mar-2015)
	 */
	public WebElement getElement(By by, WebElement parent) {
		return getElement(by, parent, -1);
	}

	/**
	 * equivalent to {@link #getElement(By, WebElement) getElement(by,
	 * getElement(parent))}
	 * 
	 * @author Sagar Wadhwa
	 * @since(20-Mar-2015)
	 */
	public WebElement getElement(By by, By parent) {
		return getElement(by, getElement(parent));
	}

	/**
	 * equivalent to {@link #getElement(By, WebElement, long) getElement(by,
	 * null, -1)}
	 * 
	 * @author Abhishek Pandey (04-Mar-2015)
	 */
	public WebElement getElement(By by) {
		return getElement(by, null, -1);
	}

	/**
	 * equivalent to {@link #getElement(By, WebElement, long) getElement(by,
	 * null, maxWaitInSec)}
	 * 
	 * @author Abhishek Pandey
	 * @since (04-Mar-2015)
	 */
	public WebElement getElement(By by, long maxWaitInSec) {
		return getElement(by, null, maxWaitInSec, false);
	}

	public WebElement getElement(By by, long maxWaitInSec, boolean silent) {
		return getElement(by, null, maxWaitInSec, silent);
	}

	public void elementHighlight(WebElement element) {
		for (int i = 0; i < 2; i++) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) this.driver;
				String styleAttribute = "arguments[0].setAttribute('style', arguments[1]);";
				String styleAttributeValue = "color: red; border: 3px solid red;";
				js.executeScript(styleAttribute, element, styleAttributeValue);
				Thread.sleep(100);
				js.executeScript(styleAttribute, element, "");
				Thread.sleep(100);
				js.executeScript(styleAttribute, element, styleAttributeValue);
				Thread.sleep(100);
				js.executeScript(styleAttribute, element, "");
				Thread.sleep(100);
				js.executeScript(styleAttribute, element, styleAttributeValue);
				Thread.sleep(100);
				js.executeScript(styleAttribute, element, "");
			} catch (InterruptedException e) {
				LOG.debug(e);
			}
		}
	}

	public List<WebElement> getElements(By by) {
		return getElements(by, null, -1);
	}

	/**
	 * equivalent to {@link #getElements(By, WebElement) getElements(by,
	 * getElement(parent))}
	 * 
	 * @author Sagar Wadhwa
	 * @since(24-Mar-2015)
	 */
	public List<WebElement> getElements(By by, By parent) {
		return getElements(by, getElement(parent));
	}

	public List<WebElement> getElements(By by, WebElement parent) {
		return getElements(by, parent, -1);
	}

	public List<WebElement> getElements(By colAllSharingMemberNames, int maxWaitInSec) {
		return getElements(colAllSharingMemberNames, null, maxWaitInSec);
	}

	/**
	 * 
	 * @param by
	 * @param parent
	 *            if null, driver is used
	 * @param maxWaitInSec
	 *            if -1, implicitWait is used
	 * @return
	 * @author Abhishek Pandey
	 */
	public List<WebElement> getElements(By by, WebElement parent, long maxWaitInSec) {
		List<WebElement> elements = new ArrayList<WebElement>();
		try {
			long maxWaitInSecLocal = maxWaitInSec;
			if (maxWaitInSec == -1) {
				maxWaitInSecLocal = implicitWaitInSeconds;
			}
			driver.manage().timeouts().implicitlyWait(maxWaitInSecLocal, TimeUnit.SECONDS);
			if (parent == null) {
				return driver.findElements(by);
			} else {
				return parent.findElements(by);
			}
		} catch (Exception e) {
			LOG.info("exception occured", e);
		} finally {
			driver.manage().timeouts().implicitlyWait(this.implicitWaitInSeconds, TimeUnit.SECONDS);
		}

		return elements;
	}

	public WebElement getLinkByText(WebElement parent, String text, String filterXpath) {
		List<WebElement> allLinks = getElements(By.xpath(filterXpath), parent, -1);
		for (WebElement link : allLinks) {
			String searchText = link.getText().trim();
			if (searchText.equalsIgnoreCase(text)) {
				return link;
			}
		}
		return null;
	}

	/**
	 * Returns the double value rounded off to the places specified.
	 * 
	 * @param value
	 * @param places
	 *            if <0, throws IllegalArgumentException()
	 * 
	 * @return doubleValue (rounded off)
	 * @author Sagar Wadhwa
	 */
	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException("invalid value for places: " + places);
		}

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public WebElement getLinkByText(String text, String filterXpath) {
		return getLinkByText(null, text, filterXpath);
	}

	public WebElement getLinkByText(String text) {
		return getLinkByText(null, text, "//a");
	}

	public WebElement getLinkByText(WebElement parent, String text) {
		return getLinkByText(parent, text, "//a");
	}

	/**
	 * waits for seconds specified by <i>secondsToWait</i>
	 * 
	 * @author Abhishek Pandey
	 * @since 20-Jul-2015
	 * @param secondsToWait
	 */
	public static void sync(float secondsToWait) {
		try {
			Thread.sleep(Math.round(secondsToWait * 1000));
		} catch (InterruptedException e) {
			LOG.error("sync", e);
		}
	}

	/**
	 * generates a random number of specified length
	 * 
	 * @param prefix
	 *            sequence to be prefixed
	 * @param suffix
	 *            sequence to be suffixed
	 * @param length
	 *            length of the result
	 * @return a string of numbers
	 */
	public String getRandomNumber(String prefix, String suffix, int length) {
		String sRandomNumber = "";
		int iRandomLength = length - (prefix.length() + suffix.length());
		if (iRandomLength <= 0) {
			sRandomNumber = (prefix + suffix).substring(0, 10);
			return sRandomNumber;
		}

		while (sRandomNumber.length() < iRandomLength + 1) {
			sRandomNumber = sRandomNumber + String.valueOf(Math.random()).substring(2);
		}
		sRandomNumber = prefix + sRandomNumber.substring(1, iRandomLength + 1) + suffix;
		return sRandomNumber;
	}

	/**
	 * Returns the color of the element passed in hex format.
	 * 
	 * @param element
	 *            Element of which the hex code is to be returned
	 * 
	 * @return Color of the element in hex format.
	 */
	public String getHexColor(WebElement element) {
		String color = element.getCssValue("color");

		String[] rgb = color.substring(5).replaceAll("\\)", "").split(", ");
		int r = Integer.parseInt(rgb[0]);
		int g = Integer.parseInt(rgb[1]);
		int b = Integer.parseInt(rgb[2]);
		return "#" + Integer.toHexString(new Color(r, g, b).getRGB()).substring(2);
	}

	public boolean isClickable(By by, long maxWaitInSec) {
		long maxWaitInSecLocal = maxWaitInSec;
		if (maxWaitInSec == -1) {
			maxWaitInSecLocal = implicitWaitInSeconds;
		}
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement we = new WebDriverWait(driver, maxWaitInSecLocal).until(ExpectedConditions.elementToBeClickable(by));
			return we != null;
		} catch (TimeoutException e) {
			LOG.debug(e);
			return false;
		} catch (Exception e) {
			throw new AutomationFrameworkException(e);
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitWaitInSeconds, TimeUnit.SECONDS);
		}
	}

	public boolean isClickable(By by) {
		return isClickable(by, -1);
	}

	public boolean isClickable(WebElement element) {
		return isClickable(element, 0);
	}

	public boolean isClickable(WebElement element, long maxWaitInSec) {
		try {
			if (element == null) {
				LOG.error(getCallingFileNameAndLineNumber() + "element should not be null ");
				return false;
			}

			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement we = new WebDriverWait(driver, maxWaitInSec).until(ExpectedConditions.elementToBeClickable(element));
			return we != null;
		} catch (TimeoutException | StaleElementReferenceException e) {
			LOG.debug(e);
			return false;
		} catch (Exception e) {
			throw new AutomationFrameworkException(e);
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitWaitInSeconds, TimeUnit.SECONDS);
		}
	}

	public String getXpath(WebElement element) {
		return element.toString().split(" -> ")[1];
	}

	public boolean moveToElement(WebElement element) {
		try {
			new Actions(driver).moveToElement(element).build().perform();
		} catch (StaleElementReferenceException e) {
			throw new AutomationFrameworkException("element went stale", e);
		} catch (Exception e) {
			LOG.error(getCallingFileNameAndLineNumber() + "Exception occured: failed to move to element " + getXpath(element), e);
			return false;
		}
		return true;
	}

	public boolean moveByOffset(int xOffset, int yOffset) {
		try {
			new Actions(driver).moveByOffset(xOffset, yOffset).build().perform();
		} catch (Exception e) {
			LOG.error(getCallingFileNameAndLineNumber() + "Exception occured: failed to move by offset : " + xOffset + ","
					+ yOffset, e);
			return false;
		}
		return true;
	}

	public boolean moveToElement(By by) {
		WebElement element = getElement(by);
		if (element == null) {
			LOG.error(getCallingFileNameAndLineNumber() + "failed to find element " + by.toString());
			return false;
		}
		if (!moveToElement(element)) {
			return false;
		}
		return true;
	}

	public boolean isScrollBarPresent(WebElement element) {
		boolean isPresent = false;
		String script = "return document.documentElement.scrollHeight>document.documentElement.clientHeight;";

		if (element == null) {
			isPresent = (boolean) ((JavascriptExecutor) driver).executeScript(script);
		} else {
			isPresent = (boolean) ((JavascriptExecutor) element).executeScript(script);
		}

		return isPresent;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean webDriverWait(ExpectedCondition exp, long maxWaitInSec) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			new WebDriverWait(driver, maxWaitInSec).until(exp);
		} catch (TimeoutException e) {
			LOG.warn(e);
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(this.implicitWaitInSeconds, TimeUnit.SECONDS);
		}

		return true;
	}

	public String getMonthNumber(String monthName) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("MMMM").parse(monthName));
			return new SimpleDateFormat("MM").format(cal.getTime());
		} catch (ParseException e) {
			throw new AutomationFrameworkException("failed to get month number due to parsing error on month name: " + monthName,
					e);
		}
	}

	public String getMonthName(int monthNumber) {
		return new DateFormatSymbols().getMonths()[monthNumber - 1].toUpperCase();
	}

	public static void killTaskOnWindows(String taskName) {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + taskName);
		} catch (IOException e) {
			LOG.warn("failed to execute kill task: " + taskName, e);
		}
		LOG.info("task killed successfully: " + taskName);
	}

	public static Properties getProperties(String file) {
		Properties props = new Properties();
		try {
			props.load(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new AutomationFrameworkException("Unable to locate properties file: " + file, e);
		} catch (IOException e) {
			throw new AutomationFrameworkException("Unable to load properties file: " + file, e);
		}
		return props;
	}

	public static String getProperty(String file, String propertyName) {
		return getProperties(file).getProperty(propertyName);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadJsonToMap(String jsonFile) {
		Map<String, Object> map;

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();
		try {
			map = gson.fromJson(new FileReader(new File(jsonFile)), Map.class);
		} catch (JsonSyntaxException e) {
			throw new AutomationFrameworkException("invalid syntax in file: " + jsonFile, e);
		} catch (JsonIOException e) {
			throw new AutomationFrameworkException("json io exception for file: " + jsonFile, e);
		} catch (FileNotFoundException e) {
			throw new AutomationFrameworkException("file not found: " + jsonFile, e);
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, List<String>>> loadRegSummaryJsonToMap(String jsonFile) {
		Map<String, Map<String, List<String>>> map;

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();
		try {
			map = (Map<String, Map<String, List<String>>>) gson.fromJson(new FileReader(new File(jsonFile)), Map.class);
		} catch (JsonSyntaxException e) {
			throw new AutomationFrameworkException("invalid syntax in file: " + jsonFile, e);
		} catch (JsonIOException e) {
			throw new AutomationFrameworkException("json io exception for file: " + jsonFile, e);
		} catch (FileNotFoundException e) {
			throw new AutomationFrameworkException("file not found: " + jsonFile, e);
		}

		return map;
	}

	private String getCallingFileNameAndLineNumber() {
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		for (StackTraceElement ste : stes) {
			String thisClassName = this.getClass().getName();
			String stackClassName = ste.getClassName();
			if (!stackClassName.equals(thisClassName) && !Constants.THREAD_CLASS.equals(stackClassName)) {
				return "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
			}
		}
		return null;
	}

	public static Calendar getCalendar(String date, String format) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(DateUtils.parseDate(date, new String[] { format }));
		}
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public String getDateFromCalendar(Calendar calendar) {
		return getDateFromCalendar(calendar, Constants.DATE_FORMAT);
	}

	public String getDateFromCalendar(Calendar calendar, String format) {
		return new SimpleDateFormat(format).format(calendar.getTime()).toUpperCase();
	}

	/**
	 * equivalent to {@link #getTextFromList(List, boolean)
	 * getTextFromList(elements, false)}
	 * 
	 * @author Abhishek Pandey
	 * @param elements
	 * @return
	 */
	public static List<String> getTextFromList(List<WebElement> elements) {
		return getTextFromList(elements, false);
	}

	public static List<String> getTextFromList(List<WebElement> elements, boolean ignoreBlanks) {
		return getTextFromList(elements, ignoreBlanks, "", "");
	}

	public static List<String> getTextFromList(List<WebElement> elements, boolean ignoreBlanks, String regex, String replacement) {
		List<String> texts = new ArrayList<String>();
		for (WebElement element : elements) {
			String text = element.getText().trim().replaceAll(regex, replacement);
			if (ignoreBlanks && text.isEmpty()) {
				continue;
			}
			texts.add(text);
		}
		return texts;
	}

	public static WebElement getElementWithText(List<WebElement> elements, String expText) {
		for (WebElement element : elements) {
			if (expText.equals(element.getText().trim())) {
				return element;
			}
		}
		return null;
	}

	public void compareMaps(Map<String, List<String>> exp, Map<String, List<String>> act, String shortName) {
		int i = 0;
		for (String key : exp.keySet()) {
			// Reporter.vrfy("List of columns in the maps of '" + shortName +
			// "' should match for row : " + i++, exp.get(key),
			// act.get(key), null);
		}
	}

	public void compareMaps(Map<String, List<String>> exp, Map<String, List<String>> act) {
		for (String key : exp.keySet()) {
			// Reporter.vrfy("List of columns in the maps should match",
			// exp.get(key), act.get(key), null);
		}
	}

}
