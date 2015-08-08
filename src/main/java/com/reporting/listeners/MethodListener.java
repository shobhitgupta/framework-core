package com.reporting.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class MethodListener implements IInvokedMethodListener {
    public void afterInvocation(IInvokedMethod paramIInvokedMethod, ITestResult paramITestResult) {}

    public void beforeInvocation(IInvokedMethod paramIInvokedMethod, ITestResult paramITestResult) {
        if ((!paramIInvokedMethod.isConfigurationMethod()) || (paramIInvokedMethod.isTestMethod())) {
            ReportsListener.createReportDir(paramITestResult);
            ReportsListener.setPlatfromBrowserDetails(paramITestResult);
        }
    }
}