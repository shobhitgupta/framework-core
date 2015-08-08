package com.sape.common;

public final class Constants {
	public static final String FS = System.getProperty("file.separator");
	public static final String BASE_DIR = System.getProperty("user.dir") + FS;
	public static final String TEST_RESOURCES_DIR = "src" + FS + "test" + FS + "resources" + FS;
	public static final String APP_JSON_DIR = "appJson" + FS;
	public static final String MAIN_RESOURCES_DIR = "src" + FS + "main" + FS + "resources" + FS;
	public static final String TESTS_PACKAGE_NAME = "com.sape";
	public static final String AUT_INFO_FILE_NAME = "aut-info.properties";
	public static final String REPORTING_PROP_FILE_NAME = "reporting.properties";
	public static final String CONFIG_PROP_FILE_SUFFIX = "-config.properties";
	public static final String REPORTER_PROPS_FILE = "reporting.properties";
	public static final String REPORTER_PROPS_TEMPLATE_FILE = "reporting_template.properties";
	public static final String DATA_EXCEL_SUFFIX = "Data";
	public static final String TEST_FLOW_EXCEL_INCLUDE_INDICATOR = "Yes";
	public static final String THREAD_CLASS = "java.lang.Thread";
	public static final String COMMON_PAGE_FILE_NAME = "CommonPage.java";
	public static final String OPEN_FIN_RVM_EXE = "OpenFinRVM.exe";
	public static final String OPEN_FIN_EXE = "openfin.exe";
	public static final String OPEN_FIN_RVM_EXE_PATH_ON_REMOTE = "c:" + FS + "selenium-grid" + FS;
	public static final String APP_JSON_RELATIVE_PATH = ".." + FS + "frontend-ui-db" + FS + "src" + FS + "app.json";
	public static final String APP_JSON_FOLDER = "appJson" + FS;
	public static final String DATE_FORMAT = "dd-MMM-yyyy";
	public static final long IMPLICIT_WAIT_IN_SECONDS = 15;

	private Constants() {

	}

	public static final class Timeouts {
		public static final int VERY_HIGH = 120;
		public static final int HIGH = 60;
		public static final int MEDIUM = 30;
		public static final int LOW = 15;

		private Timeouts() {

		}
	}

}
