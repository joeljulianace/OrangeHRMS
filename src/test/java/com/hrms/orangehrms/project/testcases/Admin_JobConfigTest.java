/*
 * FILENAME:		Admin_JobConfig.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	30-AUG-2016
 * MODIFIED DATE:	02-SEP-2016
 * DESCRIPTION:		This file contains all job configuration related test cases
 *                  whose actions would be executed via an admin 
 * 					
 * **/
package com.hrms.orangehrms.project.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.hrms.orangehrms.constants.OrangeHRMSConstants;
import com.hrms.orangehrms.project.base.BaseTest;
import com.hrms.orangehrms.project.util.DataUtil;
import com.hrms.orangehrms.project.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class Admin_JobConfigTest extends BaseTest{

	Xls_Reader xls = null;
	SoftAssert softAssert = null;
	
	@Test(dataProvider="getData")
	public void addJobTitles(Hashtable<String, String> data){
		test = report.startTest("Add Job Titles Test");
		test.log(LogStatus.INFO, data.toString());
		
		//Checking the test case runmode
		if(!DataUtil.isTestCaseRunnable(xls, "JobTitleTest")){
			test.log(LogStatus.SKIP, "Skipping as Test Case Runmode is No");
			throw new SkipException("Skipping as Test Case Runmode is No");
		}
		
		//Checking the test data runmode
		if(data.get("Runmode").equals(OrangeHRMSConstants.TEST_RUNMODE_NO)){
			test.log(LogStatus.SKIP, "Skipping as Test Data Runmode is No");
			throw new SkipException("Skipping as Test Data Runmode is No");
		}
		
		//checking if user is already logged in
		if(!isLoggedIn){
			openBrowser(data.get("BrowserName"));
			navigate("appurl");
			doLogin(ENV.getProperty("username"), ENV.getProperty("password"));
		}

		click("landingpage_admin_tab_link_xpath");
		click("admin_jobmenu_link_xpath");
		click("admin_jobmenu_jobtitles_link_xpath");
		click("jobtitles_add_button_xpath");
		type("jobtitles_jobtitle_input_xpath", data.get("Job Title"));
		
		//Checking if the job title is already present
		//If not present proceed with filling the other details
		if(!isElementPresent("addjobtitle_jobtitle_errormsg_text_xpath")){
			type("jobtitles_jobdescription_textarea_xpath", data.get("Job Description"));
			type("jobtitles_jobspecification_attachment_xpath", data.get("Job Specification"));
			type("jobtitles_note_textarea_xpath", data.get("Note"));
			click("jobtitles_save_button_xpath");
			
			//Checking if the job title has been added
			if(getJobTitleRowNum(data.get("Job Title")) != -1){
				reportPass("Job Title Added Successfully: " + data.get("Job Title"));
			}else{
				reportFailure("Job Title Could Not Be Added Successfully: " + data.get("Job Title"));
			}	
		}else if(isElementPresent("addjobtitle_jobtitle_errormsg_text_xpath")){
			//If title is already present error message is displayed, checking for duplicate flag	
			if(data.get("Duplicate").equals("Y")){
				//If error message occurs passing the test
				reportPass(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
			}else{
				//failing the test as error message occurred
				softAssert.assertTrue(false, OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
				test.log(LogStatus.FAIL, OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
				//reportFailure(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
			}
		}
	}
	
	@Test(dataProvider="getDeleteData")
	public void deleteJobTitleTest(Hashtable<String, String> data){
		
		test = report.startTest("Delete Job Title Test");
		test.log(LogStatus.INFO, data.toString());
		
		//Checking the test case runmode
		if(!DataUtil.isTestCaseRunnable(xls, "DeleteJobTitleTest")){
			test.log(LogStatus.SKIP, "Skipping as Test Case Runmode is No");
			throw new SkipException("Skipping as Test Case Runmode is No");
		}
		
		//Checking the test data runmode
		if(data.get("Runmode").equals(OrangeHRMSConstants.TEST_RUNMODE_NO)){
			test.log(LogStatus.SKIP, "Skipping as Test Data Runmode is No");
			throw new SkipException("Skipping as Test Data Runmode is No");
		}
		
		//checking if user is already logged in
		if(!isLoggedIn){
			openBrowser(data.get("BrowserName"));
			navigate("appurl");
			doLogin(ENV.getProperty("username"), ENV.getProperty("password"));
		}
		
		click("landingpage_admin_tab_link_xpath");
		click("admin_jobmenu_link_xpath");
		click("admin_jobmenu_jobtitles_link_xpath");
		
		if(getJobTitleRowNum(data.get("Job Title")) != -1){
			//Selecting the job that needs to be deleted
			selectJobTitle(data.get("Job Title"));
			//Clicking the delete button
			click("jobtitles_delete_button_xpath");
			//Clicking the confirmation delete button
			click("jobtitles_dialog_delete_button_xpath");
			
			//checking if the job title has gotten deleted from the job title table
			if(getJobTitleRowNum(data.get("Job Title")) == -1 && (data.get("Expected Result").equals(OrangeHRMSConstants.TEST_EXPECTED_RESULT_PASS))){
				reportPass("Delete Job Title Test Passed");
			}
		}else if(getJobTitleRowNum(data.get("Job Title")) == -1 && (data.get("Expected Result").equals(OrangeHRMSConstants.TEST_EXPECTED_RESULT_FAIL))){
			//reporting pass if the job title was not present and the expected result was the same
			reportPass("Delete Job Title Test Passed");
		}else{
			//for any other combination the result was be a failure
			softAssert.assertTrue(false, "Unable to find job title: " + data.get("Job Title"));
			test.log(LogStatus.FAIL, "Unable to find job title: " + data.get("Job Title"));
		}
	}
	
	@BeforeMethod
	public void create(){
		softAssert = new SoftAssert();
	}
	
	@AfterMethod
	public void destroy(){
		
		try{
			softAssert.assertAll();
		}catch(Error e){
			test.log(LogStatus.FAIL, e.getMessage());
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
	
	@DataProvider
	public Object[][] getData(){
		xls = new Xls_Reader(OrangeHRMSConstants.DATA_PATH + "OrangeHRMS.xlsx");
		return DataUtil.getData(xls, "JobTitleTest");
	}
	
	@DataProvider
	public Object[][] getDeleteData(){
		xls = new Xls_Reader(OrangeHRMSConstants.DATA_PATH + "OrangeHRMS.xlsx");
		return DataUtil.getData(xls, "DeleteTitleTest");
	}
}
