/*
 * FILENAME:		OrangeHRMSConstants.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	29-AUG-2016
 * MODIFIED DATE:	30-AUG-2016
 * DESCRIPTION:		This file contains all the constants used throughout the project 
 * 					
 * **/

package com.hrms.orangehrms.constants;

public class OrangeHRMSConstants {

	//paths
	public static final String OR_PROPERTIES_PATH= "//src//test//resources//OR.properties";
	public static final String EXTENT_REPORT_CONFIG_PATH= System.getProperty("user.dir") + "//src//test//resources//ReportsConfig.xml";
	public static final String CHROME_DRIVER_PATH= System.getProperty("user.dir") + "//chromedriver//chromedriver.exe";
	public static final String EXTENT_REPORTS_PATH= System.getProperty("user.dir") + "//extentreports//";
	public static final String SCREENSHOTS_PATH= System.getProperty("user.dir") + "//screenshots//";
	public static final String EXTENT_REPORTS_SCREENSHOTS_PATH= "../screenshots/";
	public static final String DATA_PATH= System.getProperty("user.dir") + "//data//";
	
	
	//browser names
	public static final String BROWSER_MOZILLA= "Mozilla";
	public static final String BROWSER_CHROME= "Chrome";
	
	//chrome properties
	public static final String CHROME_PROPERTY="webdriver.chrome.driver";
	
	//Ends with
	public static final String ENDS_WITH_ID = "_id";
	public static final String ENDS_WITH_NAME = "_name";
	public static final String ENDS_WITH_XPATH = "_xpath";
	
	//Excel file properties
	public static final String TEST_CASES_SHEET = "Test Cases";
	public static final String TEST_DATA_SHEET = "Test Data";
	public static final String TEST_CASE_NAME = "Test_Case_Name";
	public static final String TEST_RUNMODE = "Runmode";
	public static final String TEST_RUNMODE_YES = "Y";
	public static final String TEST_RUNMODE_NO = "N";
	public static final String TEST_EXPECTED_RESULT = "Expected Result";
	public static final String TEST_EXPECTED_RESULT_PASS = "Pass";
	public static final String TEST_EXPECTED_RESULT_FAIL = "Fail";
	public static final String JOBTITLE_JOB_SPECIFICATION_RANDOM = "Random";
	public static final String JOBTITLE_EDIT_JOB_SPECIFICATION_KEEP_CURRENT="Keep Current";
	public static final String JOBTITLE_EDIT_JOB_SPECIFICATION_DELETE_CURRENT="Delete Current";
	public static final String JOBTITLE_EDIT_JOB_SPECIFICATION_REPLACE_CURRENT="Replace Current";
}
