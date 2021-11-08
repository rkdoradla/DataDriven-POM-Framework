package com.base.logging;

import org.apache.commons.io.FileUtils;
import org.testng.ITestNGMethod;

import com.base.configuration.ConfigUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Class has methods to create the logger instance and to print log headers. Methods to create
 * copy logs to remote share path.
 *
 *  @author  Ramakrishna Doradla Venkatesh
 * 	@version 
 * 	@since   
 */

public class LoggerManager 
{

    private String appName;
    private String runType;
    private String baseLocalLogFolderPath;
    private String baseRemoteLogFolderPath;
    private boolean createRemoteLog ;
    private boolean loginConsole;
    private String logLevel;
    private String logName;
    private String fullLocalLogFolderPath = "";
    private String fullRemoteLogFolderPath = "";
    public   Logger logger;
    
 	private Map<String, String> timeMap = new HashMap<String, String>();
	private Map<String, String> memoryMap = new HashMap<String, String>();
	private Map<String, String> eventToProcessMap = new HashMap<String, String>();
    


    /**
     * Logger manager.
     *
     * @param className ||description: Test Class name
     * ||allowedRange:
     * @throws Exception 
     */
    public LoggerManager(String className) throws Exception {
    	try {
    		appName = ConfigUtil.config.get("env.application");
        	runType = ConfigUtil.config.get("env.run.type");
            baseLocalLogFolderPath = ConfigUtil.config.get("env.base.log.folder");
            baseRemoteLogFolderPath = ConfigUtil.config.get("env.base.remote.log.folder");
            createRemoteLog = Boolean.parseBoolean(ConfigUtil.config.get("env.create.remote.log"));
            loginConsole = Boolean.parseBoolean(ConfigUtil.config.get("env.login.console"));
            logLevel = ConfigUtil.config.get("env.log.level");
            logName = ConfigUtil.config.get("env.log.name");
            
        	logger = generateLogger(className);
    	}catch(Exception e) {
    		throw e;
    	}
    }


    /**
     * Generate logger.
     *
     * @name generateLogger
     * @description Create the logger object with customized properties
     * @author Ramakrishna Doradla Venkatesh
     * @param className ||description: String Test class Name
     * ||allowedRange:
     * @return Logger	||description: Logger Object
     * @jiraId 
     */
    private Logger generateLogger(String className) throws Exception{
	    try {
	    	// Create Logger String filePattern, String fileThreshold
	        Logger log = Logger.getLogger(className);
	
	        //Remove all appenders
	        log.removeAllAppenders();
	
	        PatternLayout patternLayout = new PatternLayout("(%d{HH:mm:ss}) %m%n");
	
	        // Create Logging File Appender
	        RollingFileAppender fileApp = new RollingFileAppender();
	
	        fileApp.setFile(generateFullLocalLogDirectoryPath() + "/" + logName);
	        fileApp.setLayout(patternLayout);
	        fileApp.setThreshold(Level.toLevel(logLevel));
	        fileApp.setAppend(true);
	        fileApp.activateOptions();
	        log.addAppender(fileApp);
	
	        //log in console if required
	        ConsoleAppender consoleAppender;
	        if (loginConsole) {
	            consoleAppender = new ConsoleAppender();
	            consoleAppender.setWriter(new OutputStreamWriter(System.out));
	            consoleAppender.setLayout(patternLayout);
	            consoleAppender.setThreshold(Level.toLevel(logLevel));
	            consoleAppender.setName("Console appender");
	            consoleAppender.activateOptions();
	            log.addAppender(consoleAppender);
	        }
	        return log;
	    }catch(Exception e) {
	    	throw e;
	    }
    }
    
  


    /**
     * Prints the header.
     *
     * @name printHeader
     * @description  Adds header to the log file
     * @author Ramakrishna Doradla Venkatesh
     * @param properties ||description: HashMap<"HeaderProperty","Value">
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void printHeader(HashMap<String, String> properties) {

        try{
        	logger.info("**************************************** Script Run Details ***********************************");
        	logger.info("Application Name: " + appName);
            logger.info("Script Name: " + properties.get("scriptname"));
            logger.info("Test Environment: " + properties.get("environment"));
            logger.info("Test Cases Covered: " + properties.get("coverage"));
            logger.info("Executed By: " + System.getProperty("user.name"));
            logger.info("Execution Date: " + properties.get("execution.date"));
            logger.info("Machine Name: " + System.getenv("computername"));
            logger.info("Run Type: " + properties.get("runType"));
            logger.info("Release: " + properties.get("release"));
            logger.info("BuildNumber: " + properties.get("build"));
            logger.info("Browser: " + properties.get("browser") +"_" +properties.get("version"));
            logger.info("AutomationTool: " + properties.get("testtool"));
            logger.info("**************************************** End Run Details***************************************");
        } catch(Exception e) {
        	logger.info(e.getMessage());
        }
    }


    /**
     * Generate full local log directory path.
     *
     * @name generateFullLocalLogDirectoryPath
     * @description Generate the local log directory location from configuration files
     * @author Ramakrishna Doradla Venkatesh
     * @return String	||description: Full local log directory path
     * @jiraId 
     */
    private String generateFullLocalLogDirectoryPath() {
   	try{
    	  fullLocalLogFolderPath =  baseLocalLogFolderPath+"\\"+appName+"\\" + generateLogFolderName();
          File dir = new File(fullLocalLogFolderPath);
          dir.mkdir();
      }catch(Exception e){
    	  logger.info(e.getMessage());
      }
      
      return fullLocalLogFolderPath;
        
    }


    /**
     * Generate full remote log directory path.
     *
     * @name generateFullRemoteLogDirectoryPath
     * @description Generate the remote log directory location from configuration files
     * @author Ramakrishna Doradla Venkatesh
     * @return String	||description: Full remote log directory path
     * @jiraId 
     */
    private String generateFullRemoteLogDirectoryPath() {
    	try {
    	   fullRemoteLogFolderPath = baseRemoteLogFolderPath +"\\" +appName +"\\" +runType;
           File f = new File(fullLocalLogFolderPath);
           String logDir = f.getName();
           fullRemoteLogFolderPath = fullRemoteLogFolderPath +"\\" + logDir;
    	} catch(Exception e) {
    	   logger.info(e.getMessage());
    	}
       return fullRemoteLogFolderPath;  
    	
    }


    /**
     * Generate log folder name.
     *
     * @name generateLogFolderName
     * @description Generate the log directory name
     * @author Ramakrishna Doradla Venkatesh
     * @return String	||description: TimeStamped log folder string
     * @jiraId 
     */
    private synchronized String generateLogFolderName() {
    	String logFolder=null;
    	try {
    	// Append the date time to the directory
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
           Date dNow = new Date();
           logFolder = sdf.format(dNow).toString();
       }catch(Exception e) {
    	   logger.info(e.getMessage());
       }
       
       return logFolder;
    }


    /**
     * Finish log.
     *
     * @name finishLog
     * @description Copies logs to the remote folder if required and releases the logger object
     * @author Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @throws Exception 
     * @jiraId 
     */
    public void finishLog() {
        if (createRemoteLog && checkLocalLogFolder()){
        	try {
        		File dir = new File(generateFullRemoteLogDirectoryPath());
                dir.mkdir();
                FileUtils.copyDirectory(new File(fullLocalLogFolderPath), new File(fullRemoteLogFolderPath));
            } catch (Exception e) {
                logger.info("Attention: Can't copy logs to the remote folder, Local: " +fullLocalLogFolderPath +" Remote: "+fullRemoteLogFolderPath);
                logger.info(e.getMessage());
            }
        }

        logger = null;
        fullLocalLogFolderPath = "";
        fullRemoteLogFolderPath = "";
    }


    /**
     * Check local log folder.
     *
     * @name checkLocalLogFolder
     * @description Checks local log folder contains log file or not
     * @author Ramakrishna Doradla Venkatesh
     * @return boolean	||description: True|False, True if log file exists in the log directory
     * @jiraId 
     */
    private boolean checkLocalLogFolder(){
    	
	    try {
	    	File logDir = new File (fullLocalLogFolderPath);
	    	if(logDir.exists()){
		    	int logFileCount = new File(fullLocalLogFolderPath).listFiles().length;
		    	if (logFileCount == 0){
		    		return false;
		    	}else {
		    		return true;
		    	}
	    	}else {
	    		return false;
	    	}
	    } catch(Exception e) {
	    	logger.info(e.getMessage());
	    }	
	    return false;
    }
    

    /**
     * Get full local log folder path.
     *
     * @name getFullLocalLogFolderPath
     * @description  Get the local log directory location from the configuration file
     * @author Ramakrishna Doradla Venkatesh
     * @return String	||description: Local Log directory Path
     * @jiraId 
     */
    public String getFullLocalLogFolderPath() {
        return fullLocalLogFolderPath;
    }


    /**
     * Get full remote log folder path.
     *
     * @name getFullRemoteLogFolderPath
     * @description Get the remote log directory location.
     * @author Ramakrishna Doradla Venkatesh
     * @return String	||description: Remote Log directory path
     * @jiraId 
     */
    public String getFullRemoteLogFolderPath() {
        if (fullRemoteLogFolderPath.equals("")) {
            return  generateFullRemoteLogDirectoryPath();
        }
        return fullRemoteLogFolderPath;
    }

  
    /**
     * Get test coverage.
     *
     * @name getTestCoverage
     * @description Get tests coverage from test annotation
     * @author Ramakrishna Doradla Venkatesh
     * @param testClassName ||description: Test Class Name
     * ||allowedRange:
     * @return String	||description: tests coverage String
     * @jiraId 
     */
    public String getTestCoverage(String testClassName, String testMethod) {
        String currentDescription;
        String currentClass;
        String testCoverage = "";

     //   List<String> coverageList = new ArrayList<>();

        Pattern pattern;
        Matcher matcher;

        try {
            for (ITestNGMethod method : ConfigUtil.getAllTestMethods()) {
                currentDescription = method.getDescription();
                currentClass = method.getRealClass().getName();
                if (currentClass.equals(testClassName)) {
                    if(method.getMethodName().equals(testMethod)) {
                    	if (null ==currentDescription) {
                            currentDescription = "";
                        }
                        pattern = Pattern.compile("Coverage:(.*)");
                        matcher = pattern.matcher(currentDescription);
                        if (matcher.find()) {
                           // coverageList.add(matcher.group(1).trim());
                        	testCoverage = matcher.group(1).trim();
                        }
                    }
                }
            }
         /*   Collections.sort(coverageList);
            for (String coverageItem : coverageList) {
                testCoverage += (coverageItem + "; ");
            } */
        }
        catch (Exception ex){
            logger.info("Test coverage throws exception:\n"+ex.getMessage());
        }
        return testCoverage;
    }
    
    /**
	 * Start transaction timer.
	 *
	 * @name startTransactionTimer
	 * @description Takes a snapshot of the current time and associates an alias to it.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param eventName ||description: Any event name you choose.
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	public void startTransactionTimer(String eventName) 
	{
		try {
			logger.info("Start transaction timer: " + eventName);
			timeMap.put(eventName, Long.toString(System.currentTimeMillis()));
		}catch(Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Stop transaction timer.
	 *
	 * @name stopTransactionTimer
	 * @description Takes a snapshot of the current time and associates an alias to it.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param eventName ||description: The event name you chose earlier.
	 * ||allowedRange: Any event name you already created when calling startTransactionTimer(eventName).
	 * @return void	||description:
	 * @jiraId 
	 */
	public void stopTransactionTimer(String eventName) {
		try {
			logger.warn("Stop transaction timer: " + eventName);
			if(!timeMap.containsKey(eventName)) {
				logger.warn("Event not found by event name \"" + eventName + "\"; make sure you first called "
						+ "startTransactionTimer(" + eventName + ").");
				return;
			}
			
			Long time = Long.parseLong(timeMap.get(eventName));
			timeMap.remove(eventName);
			String delta = getTimeDiff(System.currentTimeMillis() - time);

			logger.info("@@TransactionTime: "+ eventName + "[" + delta + "]");

		}catch(Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Gets the time diff.
	 *
	 * @name getTimeDiff
	 * @description Pass a UNIX epoch long value for time and returns a nicely formatted time as string.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param diff ||description: Time as long.
	 * ||allowedRange:
	 * @return String	||description: Time nicely formatted.
	 * @jiraId 
	 */
	public String getTimeDiff(long diff) {
		String totalTime = null;
		try{
			
			long diffMilliSeconds = diff % 1000;
			long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			if(diffMinutes > 0) {
				totalTime = String.format("%dm:%ds:%dms", diffMinutes, diffSeconds, diffMilliSeconds); 	 
			} else {
				totalTime = String.format("%ds:%dms", diffSeconds, diffMilliSeconds);
			}
			
		} catch(Exception e) {
			logger.info(e.getMessage());
		}
		return totalTime;
	}

	/**
	 * Start memory watch.
	 *
	 * @name startMemoryWatch
	 * @description Takes a snapshot of the memory usage of the provided process and associates an alias to it.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param eventName ||description: Any event name you choose.  Describes the application/event you are taking a memory snapshot of.
	 * ||allowedRange:
	 * @param processName ||description: The process name you are taking a memory snapshot of.  Should end with '.exe'.
	 * ||allowedRange: Any currently running process ending with .exe
	 * @return void	||description:
	 * @throws Exception 
	 * @jiraId 
	 */
	public void startMemoryWatch(String eventName, String processName) {
		
		int procMemory = 0;
		try {
			String line;
			logger.info("Start memory watch: " + eventName + "::" + processName);
			Process process = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe /fo csv /nh");

			BufferedReader input =
					new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = input.readLine()) != null)  {
				if (line.contains(processName)){
					List<String> items = Arrays.asList(line.split(",\""));
					String lineMemory =  items.get(4).replace("\"","").replace(",","").replace(" K", "");
					procMemory += Integer.parseInt(lineMemory);
				}                              
			}
			input.close();

		} catch (Exception err) {
			logger.info(err.getMessage());
		}

		eventToProcessMap.put(eventName, processName);
		memoryMap.put(processName, Integer.toString(procMemory));
	}

	/**
	 * Stop memory watch.
	 *
	 * @name stopMemoryWatch
	 * @description Prints the delta in memory usage for a chosen application process between startMemoryWatch and stopMemoryWatch calls.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param eventName ||description: The event name you chose to represent your process.
	 * ||allowedRange: Any event name you already created when calling startMemoryWatch(eventName).
	 * @return void	||description:
	 * @throws Exception 
	 * @jiraId 
	 */
	public void stopMemoryWatch(String eventName) {
		int procMemory = 0;
		String processName=null;
		try {
			String line;
			logger.info("Stop memory watch: " + eventName);
			if(!eventToProcessMap.containsKey(eventName)) {
				logger.warn("Event not found by event name \"" + eventName + "\"; make sure you first called "
						+ "startMemoryWatch(" + eventName + ").");
				return;
			}

			processName = eventToProcessMap.get(eventName);
			eventToProcessMap.remove(eventName);

			Process process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /fo csv /nh");

			BufferedReader input =
					new BufferedReader(new InputStreamReader(process.getInputStream()));

			while((line = input.readLine()) != null)  {
				if (line.contains(processName)) {
					List<String> items = Arrays.asList(line.split(",\""));
					String lineMemory =  items.get(4).replace("\"","").replace(",","").replace(" K", "");
					procMemory += Integer.parseInt(lineMemory);
				}                              
			}
			input.close();

		} catch (Exception err) {
			logger.info(err.getMessage());
		}

		String delta = Integer.toString(Integer.parseInt(memoryMap.get(processName)) - procMemory);
		memoryMap.remove(eventName);

		logger.info("@@MemoryDelta: " + eventName + "[" + delta + "KB]");
	}

	/**
	 * Gets the memory.
	 *
	 * @name getMemory
	 * @description This method logs the memory usage for the given process.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param processName ||description: full process name, appended with .exe
	 * ||allowedRange: any running process ending with .exe
	 * @return void	||description:
	 * @throws Exception 
	 * @jiraId 
	 */
	public void getMemory(String processName) {

		
		int procMemory = 0;
		try {
			String line;
			logger.info("Start getMemory: " + processName);
			Process process = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe /fo csv /nh");

			BufferedReader input =
					new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = input.readLine()) != null)  {
				if (line.contains(processName)){
					List<String> items = Arrays.asList(line.split(",\""));
					String lineMemory =  items.get(4).replace("\"","").replace(",","").replace(" K", "");
					procMemory += Integer.parseInt(lineMemory);
				}                              
			}
			input.close();

		} catch (Exception err) {
			logger.info(err.getMessage());
		}

		logger.info("@@Memory: " + processName + "[" + procMemory + "KB]");

	}

      
}