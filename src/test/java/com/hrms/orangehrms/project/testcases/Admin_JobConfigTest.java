/*
 * FILENAME:		Admin_JobConfig.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	30-AUG-2016
 * MODIFIED DATE:	07-SEP-2016
 * DESCRIPTION:		This file contains all job configuration related test cases
 *                  whose actions would be executed via an admin 
 * 					
 * **/
package com.hrms.orangehrms.project.testcases;

import java.util.Hashtable;
import org.openqa.selenium.Keys;
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
		test = report.startTest("Add Job Title Test");
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
		//entering job title
		type("jobtitles_jobtitle_input_xpath", data.get("Job Title"));
		//entering job description
		type("jobtitles_jobdescription_textarea_xpath", data.get("Job Description"));
		//checking if Random is mentioned in the job specification text
		if(data.get("Job Specification").equals(OrangeHRMSConstants.JOBTITLE_JOB_SPECIFICATION_RANDOM)){
			//call the function to attach a random file
			String fileName = selectRandomFile(OrangeHRMSConstants.SCREENSHOTS_PATH);
			
			//Checking if a file name is returned
			if(fileName != null){
				//Attaching the file
				type("jobtitles_jobspecification_attachment_xpath", fileName);
			}else{
				//Attaching nothing
				test.log(LogStatus.INFO, "No Files Found In: " + OrangeHRMSConstants.SCREENSHOTS_PATH);
				fileName = "";
				type("jobtitles_jobspecification_attachment_xpath", fileName);
			}
			
		}else{
			//attaching the file
			type("jobtitles_jobspecification_attachment_xpath", data.get("Job Specification"));
		}
		
		//entering note
		type("jobtitles_note_textarea_xpath", data.get("Note"));
		//clicking save button
		click("jobtitles_save_button_xpath");
		
		//Checking if the job title is already present or the file is not found
		//If not present proceed with filling the other details
		if(!(isElementPresent("addjobtitle_jobtitle_errormsg_text_xpath") 
			|| isElementPresent("jobtitles_jobspecification_attachment_filenotfound_xpath"))){
			
			//Checking if the job title has been added
			if(getJobTitleRowNum(data.get("Job Title")) != -1){
				reportPass("Job Title Added Successfully: " + data.get("Job Title"));
			}else{
				reportFailure("Job Title Could Not Be Added Successfully: " + data.get("Job Title"));
			}	
		}else if(isElementPresent("addjobtitle_jobtitle_errormsg_text_xpath")){
			
			//Checking if the job title field is left blank
			if(!(getText("addjobtitle_jobtitle_errormsg_text_xpath").equals(OR.get("jobtitle_required_errormsg")))){
				//If title is already present error message is displayed, checking for duplicate flag
				if(data.get("Duplicate").equals("Y")){
					//If error message occurs passing the test
					reportPass(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
				}else{
					//failing the test as error message occurred
					softAssert.assertTrue(false, OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
					reportFailure(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
					//reportFailure(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("Job Title"));
				}
			}else{
				//Reporting error incase the job title field is left blank
				reportFailure("Job Title Is Mandatory");
			}
		}else{
			//reporting the failure as file is not found
			reportFailure("File Not Found: " + data.get("Job Specification"));
			//refreshing the page
			refreshPage();
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
			reportFailure("Unable to find job title: " + data.get("Job Title"));
		}
	}
	
	@Test(dataProvider="getEditData")
	public void editJobTitleTest(Hashtable<String, String> data){
		
		test = report.startTest("Edit Job Title Test");
		test.log(LogStatus.INFO, data.toString());
		
		//Checking the test case runmode
		if(!DataUtil.isTestCaseRunnable(xls, "EditJobTitleTest")){
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
		
		//checking if the old job title is present
		if(getJobTitleRowNum(data.get("Old Job Title")) != -1){
			//Clicking on the job title
			clickJobTitle(data.get("Old Job Title"));
			//clicking the edit button
			click("editjobtitle_edit_button_xpath");
			//Clearing the text in the job title field
			type("editjobtitle_jobtitle_input_xpath", Keys.chord(Keys.SHIFT,Keys.HOME,Keys.DELETE));
			//Typing the job title
			type("editjobtitle_jobtitle_input_xpath", data.get("New Job Title"));
			//Checking if the job specification is not blank
			if(!(data.get("Job Specification").equals(""))){
				//Checking if the job specification contain any of the below mentioned values
				if((data.get("Job Specification").equals(OrangeHRMSConstants.JOBTITLE_EDIT_JOB_SPECIFICATION_KEEP_CURRENT) 
					|| data.get("Job Specification").equals(OrangeHRMSConstants.JOBTITLE_EDIT_JOB_SPECIFICATION_DELETE_CURRENT) 
					|| data.get("Job Specification").equals(OrangeHRMSConstants.JOBTITLE_EDIT_JOB_SPECIFICATION_REPLACE_CURRENT))){
					//selecting the job specification
					selectJobSpecification(data.get("Job Specification"));
					
					//Checking if the job specification is to replace the file
					if(data.get("Job Specification").equals(OrangeHRMSConstants.JOBTITLE_EDIT_JOB_SPECIFICATION_REPLACE_CURRENT)){
						//Checking if random file has to be selected
						if(data.get("Attachment").equals(OrangeHRMSConstants.JOBTITLE_JOB_SPECIFICATION_RANDOM)){
							//Getting the random file name
							String fileName = selectRandomFile(OrangeHRMSConstants.SCREENSHOTS_PATH);
							
							//Checking if a file name is returned
							if(fileName != null){
								//Attaching the file
								type("editjobtitle_jobtitle_jobspecification_attachment_xpath", fileName);
							}else{
								//Attaching nothing
								test.log(LogStatus.INFO, "No Files Found In: " + OrangeHRMSConstants.SCREENSHOTS_PATH);
								fileName = "";
								type("editjobtitle_jobtitle_jobspecification_attachment_xpath", fileName);
							}
						}else{
							type("editjobtitle_jobtitle_jobspecification_attachment_xpath", data.get("Attachment"));
						}
					}
				}
			}else if(!(data.get("Attachment").equals(""))){
				//Checking if the attachment is provided in the xls file
				//Checking if the attachment needed is random
				if(data.get("Attachment").equals(OrangeHRMSConstants.JOBTITLE_JOB_SPECIFICATION_RANDOM)){
					//Getting the random file name
					String fileName = selectRandomFile(OrangeHRMSConstants.SCREENSHOTS_PATH);
					
					//Checking if a file name is returned
					if(fileName != null){
						//Attaching the file
						type("editjobtitle_jobtitle_jobspecification_attachment_xpath", fileName);
					}else{
						//Attaching nothing
						test.log(LogStatus.INFO, "No Files Found In: " + OrangeHRMSConstants.SCREENSHOTS_PATH);
						fileName = "";
						type("editjobtitle_jobtitle_jobspecification_attachment_xpath", fileName);
					}
				}else{
					//attaching the file
					type("editjobtitle_jobtitle_jobspecification_attachment_xpath", data.get("Attachment"));
				}
			}
			
			
			//Checking if the job title field is left blank
			if(!getText("addjobtitle_jobtitle_errormsg_text_xpath").equals(OR.get("jobtitle_required_errormsg"))){
				//checking if new job title entered is same as an existing job title			
				if(!(isElementPresent("addjobtitle_jobtitle_errormsg_text_xpath") && getText("addjobtitle_jobtitle_errormsg_text_xpath").equals(OR.get("addjobtitle_errormsg")))){
					//clicking the save button
					click("editjobtitle_save_button_xpath");
					//checking if the attachment was found
					if(!isElementPresent("jobtitles_jobspecification_attachment_filenotfound_xpath")){
						//checking if job title is successfully changed
						if(getJobTitleRowNum(data.get("New Job Title")) != -1){
							reportPass("Edit Job Title Test Passed");
						}else{
							reportFailure(data.get("New Job Title") + " could not be saved");
						}
					}else{
						//reporting failure if the file was not found
						reportFailure("File Not Found: " + data.get("Attachment"));
						//refreshing the page
						refreshPage();
					}
				}else if(isElementPresent("addjobtitle_jobtitle_errormsg_text_xpath")){
					//checking if the job title is entered as duplicate for testing
					if(data.get("Duplicate").equals("Y")){
						reportPass(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("New Job Title"));
						reportPass("Edit Job Title Test Passed");
					}else{
						softAssert.assertTrue(false, OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("New Job Title"));
						reportFailure(OR.getProperty("addjobtitle_errormsg") + ", error message displayed for job title: "  + data.get("New Job Title"));
					}
				}
			}else{
				//reporting error is job title field is left blank
				reportFailure("Job Title Field is Mandatory");
			}
		}else{
			//reporting failure in case the old job title does not exist in the list
			reportFailure("Unable to find job title: " + data.get("Old Job Title"));
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
	
	@DataProvider
	public Object[][] getEditData(){
		xls = new Xls_Reader(OrangeHRMSConstants.DATA_PATH + "OrangeHRMS.xlsx");
		return DataUtil.getData(xls, "EditTitleTest");
	}
}
