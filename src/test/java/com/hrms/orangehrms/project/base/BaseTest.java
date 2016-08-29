/*
 * FILENAME:		BaseTest.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	29-AUG-2016
 * MODIFIED DATE:	29-AUG-2016
 * DESCRIPTION:		This file will contain all the resuable functions
 * 					All test cases will extend this file
 * */
package com.hrms.orangehrms.project.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
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
			driver = new ChromeDriver();
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
		String screenshotPath=OrangeHRMSConstants.SCREENSHOTS_PATH + fileName;
		
		//taking screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		try {
			FileUtils.copyFile(srcFile, new File(screenshotPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//adding screenshot in the extent report
		test.log(LogStatus.INFO, "Taking Screenshot: " + test.addScreenCapture(screenshotPath));
	}
	
	
}
