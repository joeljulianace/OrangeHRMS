/*
 * FILENAME:		BaseTest.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	29-AUG-2016
 * MODIFIED DATE:	01-SEP-2016
 * DESCRIPTION:		This file will contain all the resuable functions
 * 					All test cases will extend this file
 * */
package com.hrms.orangehrms.project.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import com.hrms.orangehrms.constants.OrangeHRMSConstants;
import com.hrms.orangehrms.project.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest{
	
	public WebDriver driver = null;
	public Properties OR = null;
	public Properties ENV = null;
	public ExtentTest test = null;
	public ExtentReports report = ExtentManager.getInstance();
	private WebDriver chrome_driver = null;
	public boolean isLoggedIn = false;
	
	//All initialization activities will be done in this function
	public void init(){
		if(OR == null){
			OR = new Properties();
			try{
				FileInputStream fin = new FileInputStream(System.getProperty("user.dir") + OrangeHRMSConstants.OR_PROPERTIES_PATH);
				OR.load(fin);
				String env = OR.getProperty("env");
				ENV = new Properties();
				fin = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//" + env+ ".properties");
				ENV.load(fin);
			}catch(Exception e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Opens the brower window based on the browser name passed
	public void openBrowser(String browserName){	
		test.log(LogStatus.INFO, "Opening Browser: " + browserName);
		if(browserName.equals(OrangeHRMSConstants.BROWSER_MOZILLA)){
			driver = new FirefoxDriver();
		}else if(browserName.equals(OrangeHRMSConstants.BROWSER_CHROME)){
			System.setProperty(OrangeHRMSConstants.CHROME_PROPERTY, OrangeHRMSConstants.CHROME_DRIVER_PATH);
			
			if(chrome_driver == null){
				chrome_driver = new ChromeDriver();
			}
			
			driver = chrome_driver;
		}
		test.log(LogStatus.INFO, "Browser Opened Successfully");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
	//navigates to the url
	public void navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigating to URL: " + ENV.getProperty(urlKey));
		driver.get(ENV.getProperty(urlKey));
	}
	
	//This function will return the element from the page
	public WebElement getElement(String locatorKey){
		WebElement element = null;
		
		try{
			if(locatorKey.endsWith(OrangeHRMSConstants.ENDS_WITH_ID)){
				element = driver.findElement(By.id(OR.getProperty(locatorKey)));
			}else if(locatorKey.endsWith(OrangeHRMSConstants.ENDS_WITH_NAME)){
				element = driver.findElement(By.name(OR.getProperty(locatorKey)));
			}else if(locatorKey.endsWith(OrangeHRMSConstants.ENDS_WITH_XPATH)){
				element = driver.findElement(By.xpath(OR.getProperty(locatorKey)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return element;
	}
	
	//This will type into a text field
	public void type(String locatorKey, String text){
		test.log(LogStatus.INFO, "Entering text in: "+ locatorKey);
		getElement(locatorKey).sendKeys(text);
	}
	
	//This will click an element on the page
	public void click(String locatorKey){
		test.log(LogStatus.INFO, "Clicking on: " + locatorKey);
		getElement(locatorKey).click();
	}
	
	//This will return text within a given element
	public String getText(String locatorKey){
		test.log(LogStatus.INFO, "Getting text from: " + locatorKey);
		return getElement(locatorKey).getText();
	}
	
	//This function checks if an element is present on the page
	public boolean isElementPresent(String locatorKey){
		test.log(LogStatus.INFO, "Checking presence of element: " + locatorKey);
		
		List<WebElement> element = null;
		
		try{
			if(locatorKey.endsWith(OrangeHRMSConstants.ENDS_WITH_ID)){
				element = driver.findElements(By.id(OR.getProperty(locatorKey)));
			}else if(locatorKey.endsWith(OrangeHRMSConstants.ENDS_WITH_NAME)){
				element = driver.findElements(By.name(OR.getProperty(locatorKey)));
			}else if(locatorKey.endsWith(OrangeHRMSConstants.ENDS_WITH_XPATH)){
				element = driver.findElements(By.xpath(OR.getProperty(locatorKey)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(element.size() > 0){
			test.log(LogStatus.INFO, "Presence of element: " + locatorKey);
			return true;
		}
		
		test.log(LogStatus.INFO, "Element Not Present: " + locatorKey);
		return false;
	}
	
	/********************************VALIDATIONS****************************************/
	//This function is used to report a pass
	public void reportPass(String message){
		test.log(LogStatus.PASS, message);
	}
	//This function is used to report a failure	
	public void reportFailure(String message){
		test.log(LogStatus.FAIL, message);
		takeScreenshot();
		Assert.fail(message);
	}
	
	//This function helps to take a screenshot and attach it in the extent report
	public void takeScreenshot(){
		Date d = new Date();
		//creating the filename
		String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
		//String screenshotPath=OrangeHRMSConstants.SCREENSHOTS_PATH + fileName;
		
		//taking screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		try {
			FileUtils.copyFile(srcFile, new File(OrangeHRMSConstants.SCREENSHOTS_PATH + fileName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//adding screenshot in the extent report by using relative path
		test.log(LogStatus.INFO, "Taking Screenshot: " + test.addScreenCapture(OrangeHRMSConstants.EXTENT_REPORTS_SCREENSHOTS_PATH + fileName));
	}
	
	/******************************Application Specific Functions*****************************/
	
	//This function will serve as a default login function for other tests
	public void doLogin(String username, String password){
		
		type("loginpage_username_input_xpath", username);
		type("loginpage_password_input_xpath", password);
		click("loginpage_login_button_xpath");
		
		//checking if user is logged in and expected result
		if(isElementPresent("landingpage_welcomemsg_link_xpath")){
			test.log(LogStatus.INFO, "Login Successful");
			isLoggedIn = true;
			//reportPass("Login Test Passed");
		//checking if the user is unable to login and expected result	
		}else if(isElementPresent("loginpage_errormsg_text_xpath")){
			test.log(LogStatus.INFO, "Login UnSuccessful");
			reportFailure("Login Test Failed");
		}else{
			test.log(LogStatus.INFO, "Login UnSuccessful");
			reportFailure("Login Test Failed");
		}
	}
	
	//This functions will return on which row the given job title is present
	public int getJobTitleRowNum(String jobTitle){
		
		test.log(LogStatus.INFO, "Searching Job Title: " + jobTitle);
		List<WebElement> allJobTitles = driver.findElements(By.xpath(OR.getProperty("jobtitles_jobtitle_table_xpath")));
		
		for(int i = 0; i < allJobTitles.size(); i++){
			if(allJobTitles.get(i).getText().trim().equals(jobTitle.trim())){
				test.log(LogStatus.INFO, "Job Title Found: " + jobTitle);
				return (i+1);
			}
		}
		//will return -1 if the job title is not found in the job titles table
		test.log(LogStatus.INFO, "Job Title Not Found: " + jobTitle);
		return -1;
	}
}
