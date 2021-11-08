package com.base.lib;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

import com.base.logging.LoggerManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	public static LoggerManager loggerManager;
	
	/**
	 * Get random integer.
	 *
	 * @name getRandomInteger
	 * @description Get a random integer between the set interval
	 * @author Ramakrishna Doradla Venkatesh
	 * @param min ||description: Minimum range
	 * ||allowedRange:
	 * @param max ||description: Maximum range
	 * ||allowedRange:
	 * @return int	||description: Integer between min and max, inclusive
	 * @jiraId 
	 */
	public static int getRandomInteger(int min, int max) {
		Integer num = null;
		try {
			Random rand = new Random();
			num = (Integer)rand.nextInt(max - min + 1) + min;
			
		}catch(IllegalArgumentException e) {
			loggerManager.logger.info(e.getMessage());
		}
		return num.intValue();
	}


	/**
	 * Generate random string.
	 *
	 * @name generateRandomString
	 * @description Creates a random string of set length
	 * @author Ramakrishna Doradla Venkatesh
	 * @param length ||description: Random String length
	 * ||allowedRange:
	 * @return String	||description: A random string of set length
	 * @jiraId 
	 */
	public static String generateRandomString(int length) {
		String str = null;
		
		try {
			str= RandomStringUtils.random(length);
		}catch(Exception e) {
			loggerManager.logger.info(e.getMessage());
		}
		return str;
	}

	/**
	 * Wait for M seconds.
	 *
	 * @name waitForMSeconds
	 * @description Waits the thread for set milliseconds
	 * @author Ramakrishna Doradla Venkatesh
	 * @param timeoutInMilliSeconds ||description: Wait timeout in milliseconds
	 * ||allowedRange:
	 * @return void	||description:
	 * @throws InterruptedException 
	 * @jiraId 
	 */
	public static void waitForMSeconds(int timeoutInMilliSeconds) {
		try {
			Thread.sleep(timeoutInMilliSeconds);
		} catch (InterruptedException e) {
			loggerManager.logger.info(e.getMessage());
		}
	}

	/**
	 * Wait for seconds.
	 *
	 * @name waitForSeconds
	 * @description Waits the thread for set seconds
	 * @author Ramakrishna Doradla Venkatesh
	 * @param timeoutInSeconds ||description: Wait timeout in seconds
	 * ||allowedRange:
	 * @return void	||description:
	 * @throws InterruptedException 
	 * @jiraId 
	 */
	public static void waitForSeconds(int timeoutInSeconds) {
		try {
			Thread.sleep(timeoutInSeconds * 1000);
		} catch (InterruptedException e) {
			loggerManager.logger.info(e.getMessage());
		}
	}

	/**
	 * Load file into string.
	 *
	 * @name loadFileIntoString
	 * @description Reads a given file and returns the content as String
	 * @author Ramakrishna Doradla Venkatesh
	 * @param fileName ||description: path to file to load & read
	 * ||allowedRange:
	 * @return String	||description: String content in the file
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	public static String loadFileIntoString(String fileName) {
		String str=null;
		try{
			str = new String(Files.readAllBytes(Paths.get(fileName)));
		}catch(Exception e) {
			loggerManager.logger.info(e.getMessage());
		}
		return str;
	}
	

	/**
	 * Read json node.
	 *
	 * @name getJsonNode
	 * @description Converts the given Json string into JsonNode 
	 * @author Ramakrishna Doradla Venkatesh
	 * @param json ||description: Json string
	 * ||allowedRange:
	 * @return JsonNode	||description: Converted JsonNode
	 * @throws Exception 	||type: exception
	 * @jiraId 
	 */
	public static JsonNode getJsonNode(String json) {
		JsonNode rootArray = null;
		try{
			ObjectMapper mapper = new ObjectMapper();
			rootArray = mapper.readTree(json);
		}catch(Exception e) {
			loggerManager.logger.info(e.getMessage());
		}
		return rootArray;
	}

	
	/**
	 * Generate file copy.
	 *
	 * @name generateFileCopy
	 * @description Creates copy of existing file with new generated name.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param pathToInitialFile ||description:Path of the file to be copied
	 * ||allowedRange:
	 * @return File	||description: New copied file
	 * @throws Exception 
	 * @jiraId 
	 */
	public static File generateFileCopy(String pathToInitialFile) {
		File targetFile = null;
		try {
			File initialFile = new File(pathToInitialFile);
			String ext = pathToInitialFile.substring(pathToInitialFile.lastIndexOf("."));
			String generatedName = RandomStringUtils.randomAlphanumeric(8);
			targetFile = new File("resources/" + generatedName + ext);
			Files.copy(initialFile.toPath(), targetFile.toPath());
		} catch (Exception e) {
			loggerManager.logger.info(e.getMessage());
		}
		return targetFile;
	}
	
	/**
	 * Get format date.
	 *
	 * @name getFormatDate
	 * @description Converts a given date's format to set required format
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sStrDate ||description: Date String
	 * ||allowedRange:
	 * @param sCurFormat ||description: String Current Date format
	 * ||allowedRange:
	 * @param sReqFormat ||description: String Required Date format
	 * ||allowedRange:
	 * @return String	||description: Formatted data as String
	 * @throws ParseException 
	 * @jiraId 
	 */
	public static String getFormatDate(String sStrDate, String sCurFormat, String sReqFormat){

		String sDate = null;
		
		try {
			
			SimpleDateFormat formatter = new SimpleDateFormat(sCurFormat);
	    	Date sParseDate = formatter.parse(sStrDate);
	    	sDate = new SimpleDateFormat(sReqFormat).format(sParseDate);
			
		} catch (Exception e) {
			loggerManager.logger.info(e.getMessage());
		}
		
		return sDate;
 }
	
	/**
	 * Check cur date.
	 *
	 * @name checkCurDate
	 * @description Return the given date's as Future/Present/Past 
	 * @author Ramakrishna Doradla Venkatesh
	 * @param sStrDate ||description: String Date
	 * ||allowedRange:
	 * @param sformat ||description: String Date format
	 * ||allowedRange:
	 * @return String	||description: Future|Past|Present based on the given date
	 * @throws ParseException 
	 * @jiraId 
	 */
	public static String checkCurDate(String sStrDate, String sformat)
	{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(sformat);
    		Date date =  new Date();
        	Date inputDate = sdf.parse(sStrDate);
        	Date sysDate = sdf.parse(sdf.format(date));

        	if(inputDate.compareTo(sysDate)>0)
        		return "Future";
        	else if(inputDate.compareTo(sysDate)<0)
        		return "Past";
        	else if(inputDate.compareTo(sysDate)==0)
        		return "Present";
        	else
        		return "Invalid Input";
		}catch(Exception e){
			loggerManager.logger.info(e.getMessage());
    	}
		return "Invalid Input";
	}

	
	/**
	 * Formats date as given in config and adds or subtracts the specified to the current date.
	 *
	 * @name addDaysToDate
	 * @description Formats date as given in config and adds specified number of days to current date
	 * @author Ramakrishna Doradla Venkatesh
	 * @param days
	 *            ||description: Number of days to be added to current date ||allowedRange:
	 * @param dateFormat
	 *            ||description: ||allowedRange:
	 * @return String ||description: Updated date
	 * @jiraId
	 */
	public String addDaysToDate(String days, String dateFormat) {
		String str=null;
		try{
			SimpleDateFormat format= new SimpleDateFormat(dateFormat);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(days.trim()));
			str =  format.format(calendar.getTime());
		}catch(Exception e) {
			loggerManager.logger.info(e.getMessage());
		}
		return str;
		
	}
}

