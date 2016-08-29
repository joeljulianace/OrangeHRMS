/*
 * FILENAME:		ExtentManager.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	29-AUG-2016
 * MODIFIED DATE:	29-AUG-2016
 * DESCRIPTION:		This file helps to create an extent report 
 * 					
 * **/
package com.hrms.orangehrms.project.util;

import java.io.File;
import java.util.Date;

import com.hrms.orangehrms.constants.OrangeHRMSConstants;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager{

	private static ExtentReports extent;
	
	public static ExtentReports getInstance(){
		
		if(extent == null){
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			extent = new ExtentReports(OrangeHRMSConstants.EXTENT_REPORTS_PATH + fileName, true, DisplayOrder.NEWEST_FIRST);
			
			extent.loadConfig(new File(OrangeHRMSConstants.EXTENT_REPORT_CONFIG_PATH));
			extent.addSystemInfo("Selenium Version", "2.53.0").addSystemInfo("Environment", "QA");
		}
		
		return extent;
	}
}
