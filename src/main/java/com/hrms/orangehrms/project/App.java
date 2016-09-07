package com.hrms.orangehrms.project;

import java.io.File;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	File file = new File(System.getProperty("user.dir") + "//screenshots//");
    	File[] files = file.listFiles();
    	Random rand = new Random();
    	
    	//System.out.println("Total No. of files: "  + files.length);
    	System.out.println(files[rand.nextInt(files.length)]);
    	
    	/*
    	for(int i = 0; i < files.length; i++){
    		System.out.println(files[i]);
    	}
    	*/
    }
}
