package com.sape.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sape.exceptions.AutomationFrameworkException;

import reporting.testng.reports.ReportingUtilities;
import reporting.testng.reports.logging.LogAs;
import reporting.testng.selenium.reports.CaptureScreen;
import reporting.testng.selenium.reports.CaptureScreen.ScreenshotOf;




public class Reporter {
    private static final Logger LOG = Logger.getLogger(Reporter.class);
    private WebDriver driver;
    public Reporter(WebDriver driver) {
    	this.driver=driver;
    }
    
    public  void setDescription(String description) {
        reporting.testng.reports.ReportingUtilities.setTestCaseReqCoverage(description);
    }

    public  void pass(String desc, String data, String exp, String act) {
        report(desc, data, exp, act, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
    }

    public void pass(String data, String exp, String act) {
        pass("", data, exp, act);
    }

    public  void fail(String desc, String data, String exp, String act) {
        report(desc, data, exp, act, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
    }

    public  void fail(String data, String exp, String act) {
        report("", data, exp, act, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
    }

    public void pass(String desc, String data, String exp, String act, WebElement element) {
        report(desc, data, exp, act, LogAs.PASSED, new CaptureScreen(element));
    }

    public void fail(String desc, String data, String exp, String act, WebElement element) {
        report(desc, data, exp, act, LogAs.FAILED, new CaptureScreen(element));
    }

    public void fatal(String desc, String data, String exp, String act) {
        report(desc, data, exp, act, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
        throw new AutomationFrameworkException(desc + " | " + data + " | " + exp + " | " + act);
    }


    public void fatal(String data, String exp, String act) {
        fatal("", data, exp, act);
    }

   

    public void fatal(String data) {
        fatal("", data, "", "");
    }

    public void warning(String desc, String data, String exp, String act, WebDriver driver) {
        report(desc, data, exp, act, LogAs.WARNING, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
    }

    public void warning(String data, String exp, String act, WebDriver driver) {
        report("", data, exp, act, LogAs.WARNING, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
    }

    public void warning(String data) {
        report("", data, "", "", LogAs.WARNING, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
    }

    public void info(String desc, String data, String exp, String act) {
        report(desc, data, exp, act, LogAs.INFO, null);
    }

    public void info(String data) {
        info("", data, "", "");
    }

    public  void pass() {
        report("", "", "Should be successful", "Is successful", LogAs.PASSED, new CaptureScreen(
                ScreenshotOf.BROWSER_PAGE));
    }

    private void report(String desc, String data, String exp, String act, LogAs logAs, CaptureScreen captureScreen) {
        String stackTraceInfo = getCallingFileNameAndLineNumber() + " - " + getCallingMethodName();
        String descLocal = (desc == null || desc.isEmpty()) ? stackTraceInfo : desc;
        String dataLocal = data;

        ReportingUtilities.add(descLocal, dataLocal, exp, act, logAs, captureScreen, driver);

        if (logAs == LogAs.PASSED || logAs == LogAs.INFO) {
            LOG.info(descLocal + " | " + dataLocal + " | " + exp + " | " + act);
        } else if (logAs == LogAs.WARNING) {
            LOG.warn(descLocal + " | " + dataLocal + " | " + exp + " | " + act);
        } else {
            LOG.error(descLocal + " | " + dataLocal + " | " + exp + " | " + act);
        }
    }

    private String getCallingMethodName() {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : stes) {
            String thisClassName = this.getClass().getName();
            String stackClassName = ste.getClassName();
            if (!stackClassName.equals(thisClassName) && !Constants.THREAD_CLASS.equals(stackClassName)) {
                return ste.getMethodName();
            }
        }
        return null;
    }

    private String getCallingFileNameAndLineNumber() {
        try {
            StackTraceElement[] stes = Thread.currentThread().getStackTrace();
            String thisClassName = this.getClass().getName();
            for (int i = 0; i < stes.length; i++) {
                StackTraceElement ste = stes[i];
                String stackClassName = ste.getClassName();
                if (!stackClassName.equals(thisClassName) && !Constants.THREAD_CLASS.equals(stackClassName)) {
                    if (ste.getFileName().equals(Constants.COMMON_PAGE_FILE_NAME)) {
                        return "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")" + " - " + "("
                                + stes[i + 1].getFileName() + ":" + stes[i + 1].getLineNumber() + ")";
                    } else {
                        return "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
                    }
                }
            }
        } catch (Exception e) {
            LOG.info(e);
        }
        return null;
    }

    public boolean asrt(String desc, String data, String exp, String act, WebDriver driver) {
        if (exp == null || act == null) {
            return false;
        }
        if (exp.equals(act)) {
            info(desc, data, exp, act);
            return true;
        } else {
            fail(desc, data, exp, act);
            return false;
        }
    }

    public boolean vrfy(String desc, String data, String exp, String act) {
        if (exp == null || act == null) {
            throw new AutomationFrameworkException(desc + " | " + data + " | " + exp + " | " + act);
        }
        if (exp.equals(act)) {
            info(desc, data, exp, act);
            return true;
        } else {
            fatal(desc, data, exp, act);
            return false;
        }
    }

    public  boolean vrfy(String desc, String data, boolean exp, boolean act) {
        return vrfy(desc, data, "" + exp, "" + act);
    }

    public boolean vrfy(String data, String exp, String act) {
        if (exp == null || act == null) {
            throw new AutomationFrameworkException("" + " | " + data + " | " + exp + " | " + act);
        }
        if (exp.equals(act)) {
            info("", data, exp, act);
            return true;
        } else {
            fatal("", data, exp, act);
            return false;
        }
    }

    public boolean vrfy(String data, boolean exp, boolean act) {
        return vrfy("", data, "" + exp, "" + act);
    }

    public  boolean vrfy(String data, int exp, int act) {
        return vrfy("", data, "" + exp, "" + act);
    }

    public boolean vrfy(String data, float exp, float act) {
        return vrfy("", data, "" + exp, "" + act);
    }

    public boolean vrfy(String data, Double exp, Double act, WebDriver driver) {
        return vrfy("", data, "" + exp, "" + act);
    }

    public boolean vrfy(String data, List<String> exp, List<String> act, WebDriver driver) {
        return vrfy("", data + " " + exp + " vs " + act, "" + true, "" + CollectionUtils.disjunction(exp, act).isEmpty());

    }
}
