package com.sape.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SuiteExecutor {
    private static final Logger LOG = Logger.getLogger(SuiteExecutor.class);
    private static final String CONTROLLER_SHEET_NAME = "Controller";

    private SuiteExecutor() {

    }

    public static void main(String[] args) {
        try {
            List<XmlSuite> suites = getTestngXml();

            TestNG tng = new TestNG();
            tng.setXmlSuites(suites);
            tng.setPreserveOrder(true);
            tng.run();

        } catch (Exception e) {
            LOG.error("main", e);
        }
    }

    public static List<XmlSuite> getTestngXml() {
        ExcelUtils masterExcel;
        ExcelUtils suiteExcel;
        List<XmlTest> xmlTests = null;
        boolean parallel = false;
        List<XmlSuite> suites = new ArrayList<XmlSuite>();

        try {
            // initialize variables
            String baseDir = Constants.BASE_DIR;
            String suiteDir = Config.Paths.SUITE_DIR;
            String suiteFileName = Config.Paths.SUITE_FILE_NAME;

            // read suite file
            masterExcel = new ExcelUtils(baseDir + Constants.FS + suiteDir + Constants.FS + suiteFileName);
            masterExcel.setSheet("Executor");
            int rowCount = masterExcel.getRowCount();

            // set testng suite name
            XmlSuite suite = new XmlSuite();
            suite.setName("Test Suite");

            // add listeners
            suite.addListener("atu.testng.reports.listeners.ATUReportsListener");
            suite.addListener("atu.testng.reports.listeners.ConfigurationListener");
            suite.addListener("atu.testng.reports.listeners.MethodListener");

            suites.add(suite);
            int i = 4;

            // get parallel execution info
            String parallelFlag = masterExcel.getCellData(2, 1);
            if ("YES".equals(parallelFlag)) {
                parallel = true;
            }

            // capture suite and browser data
            List<String> suiteName = new ArrayList<String>();
            List<String> browserName = new ArrayList<String>();
            while (i < rowCount) {
                if ("".equals(masterExcel.getCellData(i, 0))) {
                    break;
                }
                if ("YES".equals(masterExcel.getCellData(i, 1))) {
                    suiteName.add(masterExcel.getCellData(i, 0));
                    browserName.add(masterExcel.getCellData(i, 2));
                }
                i++;
            }
            xmlTests = new ArrayList<XmlTest>();
            for (int j = 0; j < suiteName.size(); j++) {
                xmlTests.add(new XmlTest(suite));
                xmlTests.get(j).setName(suiteName.get(j));
                xmlTests.get(j).setName(suiteName.get(j));
                xmlTests.get(j).addParameter("Browser", browserName.get(j));
                String suitename = suiteName.get(j);

                suiteExcel = new ExcelUtils(Constants.BASE_DIR + suiteDir + Constants.FS
                        + Config.Paths.TEST_SET_DIR + Constants.FS + suitename + ".xlsx");
                suiteExcel.setSheet(CONTROLLER_SHEET_NAME);
                int rowTest = 1;
                List<XmlClass> xmlClass = new ArrayList<XmlClass>();
                int z = 0;
                while (rowTest < suiteExcel.getRowCount()) {
                    suiteExcel.setSheet(CONTROLLER_SHEET_NAME);
                    if ("".equals(suiteExcel.getCellData(rowTest, 0))) {
                        break;
                    }
                    if ("YES".equals(suiteExcel.getCellData(rowTest, 1))) {
                        xmlClass.add(new XmlClass(Config.General.REGRESSION_PACKAGE_NAME + "."
                                + suiteExcel.getCellData(rowTest, 0)));
                        suiteExcel.setSheet(suiteExcel.getCellData(rowTest, 0));
                        List<XmlInclude> methods = new ArrayList<XmlInclude>();
                        int a = suiteExcel.getRowCount();
                        int x = 1;
                        while (x < a) {

                            if ("YES".equals(suiteExcel.getCellData(x, 2))) {
                                methods.add(new XmlInclude(suiteExcel.getCellData(x, 0)));
                            }
                            x++;
                        }
                        xmlClass.get(z).setIncludedMethods(methods);
                        z++;
                    }
                    xmlTests.get(j).setClasses(xmlClass);
                    suiteExcel.setSheet(CONTROLLER_SHEET_NAME);
                    rowTest++;
                }
            }

            if (parallel) {
                suite.setParallel("tests");
                suite.setThreadCount(xmlTests.size());
            }

        } catch (Exception e) {
            LOG.error("main", e);
        }

        return suites;

    }
}