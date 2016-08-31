/*
 * FILENAME:		Admin_JobConfig.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	30-AUG-2016
 * MODIFIED DATE:	30-AUG-2016
 * DESCRIPTION:		This file contains all job configuration related test cases
 *                  whose actions would be executed via an admin 
 * 					
 * **/
package com.hrms.orangehrms.project.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hrms.orangehrms.constants.OrangeHRMSConstants;
import com.hrms.orangehrms.project.base.BaseTest;
import com.hrms.orangehrms.project.util.DataUtil;
import com.hrms.orangehrms.project.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class Admin_JobConfigTest extends BaseTest{

	Xls_Reader xls = null;
	
	@Test(dataProvider="getData")
	public void addJobTitles(Hashtable<String, String> data){
		test = report.startTest("Add Job Titles Test");
		test.log(LogStatus.INFO, data.toString());
		
		if(!DataUtil.isTestCaseRunnable(xls, "JobTitleTest")){
			test.log(LogStatus.SKIP, "Skipping as Test Case Runmode is No");
			throw new SkipException("Skipping as Test Case Runmode is No");
		}
		
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
		type("jobtitles_jobdescription_textarea_xpath", data.get("Job Description"));
		type("jobtitles_jobspecification_attachment_xpath", data.get("Job Specification"));
		type("jobtitles_note_textarea_xpath", data.get("Note"));
		click("jobtitles_save_button_xpath");
		
		test.log(LogStatus.PASS, "Add Job Title Test Passed");
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
}
