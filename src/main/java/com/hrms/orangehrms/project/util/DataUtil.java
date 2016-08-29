/*
 * FILENAME:		DataUtil.java
 * CREATED BY:		Joel Julian
 * CREATED DATE:	29-AUG-2016
 * MODIFIED DATE:	29-AUG-2016
 * DESCRIPTION:		This file contains all the utility functions required 
 * 					
 * **/
package com.hrms.orangehrms.project.util;

import java.util.Hashtable;

import com.hrms.orangehrms.constants.OrangeHRMSConstants;

public class DataUtil {

	/*
	public static void main(String args[]){

		Xls_Reader xls = new Xls_Reader(OrangeHRMSConstants.DATA_PATH + "OrangeHRMS.xlsx");
		String testCaseName = "AddEmployeeTest";
		//String testCaseName = "LoginTest";
		Object[][] testdata = new Object[2][1];
		testdata = getData(xls, testCaseName);
		System.out.println(testdata[1][0].toString());
	}
	*/
	
	
	//This functions helps to read test data from the xls file
	public static Object[][] getData(Xls_Reader xls, String testCaseName){
		
		int rowTestStartNum = 1;
		
		System.out.println("Test Case Name: " + testCaseName);
		
		//finding the test case name in the xls sheet
		//System.out.println(xls.getCellData(OrangeHRMSConstants.TEST_DATA_SHEET, 0, rowTestStartNum));
		while(!xls.getCellData(OrangeHRMSConstants.TEST_DATA_SHEET, 0, rowTestStartNum).equals(testCaseName.trim())){
			//System.out.println("==> Reading Test Name");
			rowTestStartNum++;
		}
		
		int rowTestDataStartNum = rowTestStartNum + 2;
		int rows = 0;
		
		while(!xls.getCellData(OrangeHRMSConstants.TEST_DATA_SHEET, 0, rowTestDataStartNum+rows).equals("")){
			//System.out.println("==> Counting rows");
			rows++;
		}
		
		System.out.println("Total No. of rows: " + rows);
		
		int colTestDataStartNum = rowTestStartNum + 1;
		int cols = 0;
		
		while(!xls.getCellData(OrangeHRMSConstants.TEST_DATA_SHEET, cols, colTestDataStartNum).equals("")){
			//System.out.println("==> Counting cols");
			cols++;
		}
		
		System.out.println("Total No. of cols: " + cols);
		
		Object data[][] = new Object[rows][1];
		Hashtable<String, String> table = null;
		
		for(int rNum = 0; rNum < rows; rNum++){
			table = new Hashtable<String, String>();
			for(int cNum = 0; cNum < cols; cNum++){
				String value = xls.getCellData(OrangeHRMSConstants.TEST_DATA_SHEET, cNum, (rowTestDataStartNum + rNum));
				String key = xls.getCellData(OrangeHRMSConstants.TEST_DATA_SHEET, cNum, colTestDataStartNum);
				//System.out.println("Key: " + key + " Value: " + value);
				table.put(key, value);
				//System.out.println(data[rNum][cNum]);
			}
			data[rNum][0] = table;
		}
		
		return data;
	}
	
	public static boolean isTestCaseRunnable(Xls_Reader xls, String testCaseName){
		
		int rowNum = 1;
		
		while(!xls.getCellData(OrangeHRMSConstants.TEST_CASES_SHEET, 0, rowNum).equals(testCaseName)){
			rowNum++;
		}
		
		if(xls.getCellData(OrangeHRMSConstants.TEST_CASES_SHEET, OrangeHRMSConstants.TEST_RUNMODE, rowNum).equals(OrangeHRMSConstants.TEST_RUNMODE_YES)){
			return true;
		}
		
		return false;
	}
}
