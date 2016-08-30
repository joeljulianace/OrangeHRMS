/*
 * FILENAME:		Login.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	29-AUG-2016
 * MODIFIED DATE:	30-AUG-2016
 * DESCRIPTION:		This file contains all test cases related to the login functionality
 * 					for Orange HRMS
 * **/

package com.hrms.orangehrms.project.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hrms.orangehrms.constants.OrangeHRMSConstants;
import com.hrms.orangehrms.project.base.BaseTest;
import com.hrms.orangehrms.project.util.DataUtil;
import com.hrms.orangehrms.project.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class LoginTest extends BaseTest{

	Xls_Reader xls = null;
	String testCaseName = "LoginTest";
	
	@Test(dataProvider="getData")
	public void doLogin(Hashtable<String, String> data){
		test = report.startTest("Login Test");
		test.log(LogStatus.INFO, data.toString());
		
		if(!DataUtil.isTestCaseRunnable(xls, testCaseName)){
			test.log(LogStatus.SKIP, "Skipping as Test Case Runmode is No");
			throw new SkipException("Skipping as Test Case Runmode is No");
		}
		
		if(data.get("Runmode").equals(OrangeHRMSConstants.TEST_RUNMODE_NO)){
			test.log(LogStatus.SKIP, "Skipping as Test Data Runmode is No");
			throw new SkipException("Skipping as Test Data Runmode is No");
		}
		
		openBrowser(data.get("BrowserName"));
		navigate("appurl");
		type("loginpage_username_input_xpath", data.get("Username"));
		type("loginpage_password_input_xpath", data.get("Password"));
		click("loginpage_login_button_xpath");
		
		//checking if user is logged in and expected result
		if(isElementPresent("landingpage_welcomemsg_link_xpath") && data.get("Expected Result").equals(OrangeHRMSConstants.TEST_EXPECTED_RESULT_PASS)){
			test.log(LogStatus.INFO, "Login Successful");
			reportPass("Login Test Passed");
		//checking if the user is unable to login and expected result	
		}else if(isElementPresent("loginpage_errormsg_text_xpath") && data.get("Expected Result").equals(OrangeHRMSConstants.TEST_EXPECTED_RESULT_FAIL)){
			test.log(LogStatus.INFO, "Login UnSuccessful");
			reportPass("Login Test Passed");
		}else{
			test.log(LogStatus.INFO, "Login UnSuccessful");
			reportFailure("Login Test Failed");
		}
	}
	
	@BeforeTest
	public void startUp(){
		init();
	}
	
	@AfterTest
	public void tearDown(){
		
		if(report != null){
			report.endTest(test);
			report.flush();
		}
		
		if(driver != null){
			driver.quit();
		}
	}
	
	@AfterMethod
	public void close(){

	}
	
	@DataProvider
	public Object[][] getData(){
		xls = new Xls_Reader(OrangeHRMSConstants.DATA_PATH + "OrangeHRMS.xlsx");
		return DataUtil.getData(xls, testCaseName);
	}
}
