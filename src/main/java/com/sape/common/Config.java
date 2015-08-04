package com.sape.common;

import java.util.Map;
import java.util.Properties;

import com.sape.enums.Browsers;
import com.sape.enums.ExecutionTarget;
import com.sape.enums.Suites;

@SuppressWarnings("unchecked")
public class Config {
    private static final Map<String, Object> MAP = Utilities.loadJsonToMap(Constants.BASE_DIR + "config.json");

    private Config() {

    }

    public static class Paths {
        private static final Map<String, String> PATHS = (Map<String, String>) MAP.get("paths");
        public static final String LOGS_DIR = "logs";
        public static final String LOGS_DIR_PATH = Constants.BASE_DIR + LOGS_DIR;
        public static final String REPORTS_DIR = "reports";
        public static final String REPORTS_DIR_PATH = Constants.BASE_DIR + REPORTS_DIR;
        public static final String REPORTS_PROPERTIES_FILE_PATH = Constants.BASE_DIR + "reporting.properties";
        public static final String LIB_DIR = PATHS.get("libDir");
        public static final String SUITE_DIR = PATHS.get("suiteDir");
        public static final String TESTNG_XML_NAME = PATHS.get("testngXmlName");
        public static final String SUITE_FILE_NAME = PATHS.get("suiteFileName");
        public static final String TEST_SET_DIR = PATHS.get("testSetDir");
        public static final String TEST_DATA_DIR = PATHS.get("testDataDir");

        private Paths() {

        }
    }

    public static class Drivers {
        private static final Map<String, String> DRIVERS = (Map<String, String>) MAP.get("drivers");
        public static final String CHROME_DRIVER_EXE = DRIVERS.get("chromeDriverExe");
        public static final String OPENFIN_DRIVER_EXE = DRIVERS.get("openFinDriverExe");
        public static final String IE_DRIVER_EXE = DRIVERS.get("ieDriverExe");
        public static final String PHANTOME_JS_EXE = DRIVERS.get("phantomJsExe");

        private Drivers() {

        }
    }

    public static class General {
        private static final Map<String, String> GENERAL = (Map<String, String>) MAP.get("general");
        public static final String SANITY_PACKAGE_NAME = GENERAL.get("sanityPackageName");
        public static final String REGRESSION_PACKAGE_NAME = GENERAL.get("regressionPackageName");
        public static final Long IMPLICIT_WAIT_IN_SECONDS = Long.parseLong(GENERAL.get("implicitWaitInSeconds"));
        public static final Long PAGE_LOAD_TIMEOUT_IN_SECONDS = Long.parseLong(GENERAL.get("pageLoadTimeoutInSeconds"));
        public static final Long CASSANDRA_READ_TIMEOUT_IN_SECONDS = Long.parseLong(GENERAL.get("cassandraReadTimeoutInSeconds"));
        public static final Long APP_LOAD_TIMEOUT_IN_SECONDS = Long.parseLong(GENERAL.get("appLoadTimeoutInSeconds"));
        public static final boolean MAXIMIZE_WINDOW = "true".equals(GENERAL.get("maximizeWindow")) ? true : false;

        private General() {

        }
    }

    public static class Execution {
        private static String environment;
        private static Browsers browser;
        private static ExecutionTarget executionTarget;
        private static String hubUrl;
        private static Suites autSuite;
        private static String baseUser;

        private static String baseUserPassword;
        private static Properties configProps;

        public static String getAppUrl() {
            loadEnvironmentConfig();
            return configProps.getProperty("url");
        }

        public static Properties loadEnvironmentConfig() {
            String propsFile = Constants.MAIN_RESOURCES_DIR + environment.toString() + Constants.CONFIG_PROP_FILE_SUFFIX;
            configProps = Utilities.getProperties(propsFile);
            return configProps;
        }

        private Execution() {

        }

        public static String getEnvironment() {
            return environment;
        }

        public static void setEnvironment(String environment) {
            Execution.environment = environment;
        }

        public static Browsers getBrowser() {
            return browser;
        }

        public static void setBrowser(String browser) {
            Execution.browser = Browsers.fromString(browser);
        }

        public static String getHubUrl() {
            return hubUrl;
        }

        public static void setHubUrl(String hubUrl) {
            Execution.hubUrl = hubUrl;
        }

        public static ExecutionTarget getExecutionTarget() {
            return executionTarget;
        }

        public static void setExecutionTarget(String executionTarget) {
            Execution.executionTarget = ExecutionTarget.fromString(executionTarget);
        }

        public static Suites getAutSuite() {
            return autSuite;
        }

        public static void setAutSuite(String autSuite) {
            Execution.autSuite = Suites.fromString(autSuite);
        }

        public static String getBaseUser() {
            return baseUser;
        }

        public static void setBaseUser(String baseUser) {
            Execution.baseUser = baseUser;
        }

        public static String getBaseUserPassword() {
            return baseUserPassword;
        }

        public static void setBaseUserPassword(String baseUserPassword) {
            Execution.baseUserPassword = baseUserPassword;
        }
    }
}
