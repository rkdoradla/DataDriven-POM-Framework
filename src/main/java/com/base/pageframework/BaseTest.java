/*
 * 
 */
package com.base.pageframework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.base.configuration.ConfigUtil;
import com.base.listeners.LogfileMethodListener;
import com.base.listeners.LogfileTestListener;
import com.base.logging.LoggerManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/** 
 * 	BaseTest: Script's super class, which takes care of instantiation of
 * 	WebDriver, Logger.
 * 
 * 	@author Ramakrishna Doradla Venkatesh
 * 	@version 
 * 	@since    
 */ 
 
@Listeners({ LogfileMethodListener.class, LogfileTestListener.class })
public class BaseTest {
	
	
	private static ExtendWebDriver driver;
	protected static LoggerManager loggerManager; 
	private static String testTool = "Selenium_Java";
	
	
	public static ExtentReports extent;
	public static ExtentTest test;
	static String testName;
	static String testMethodName;
	static String logFolder;
	static String extentReportBaseFolder;
	/*
	 * 
	 */
	@BeforeSuite
	public void extentSetup() {
		
		String localBasePath  =  ConfigUtil.config.get("env.extent.logLocation.base") + "\\"+ 
				                 ConfigUtil.config.get("env.run.type")+"\\"+
								 ConfigUtil.config.get("env.application");								 ;
		
		String logFolder=null;
    	try {
    	// Append the date time to the directory
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
           Date dNow = new Date();
           logFolder = sdf.format(dNow).toString();
       }catch(Exception e) {
    	   System.out.println(e.getMessage());
       }
    	extentReportBaseFolder = localBasePath+"\\"+logFolder;
		extent = new ExtentReports(extentReportBaseFolder+"\\ExtenReport.html", true);
		extent.loadConfig(new File(ConfigUtil.config.get("env.extent.configXML")));	
	    extent.addSystemInfo("Application Name", ConfigUtil.config.get("env.application"));
	    extent.addSystemInfo("Test Environment", ConfigUtil.config.get("env.environment"));
	    extent.addSystemInfo("Run Type", ConfigUtil.config.get("env.run.type"));
	    extent.addSystemInfo("Release", ConfigUtil.config.get("env.release"));
	    extent.addSystemInfo("BuildNumber", ConfigUtil.config.get("env.build"));
	    extent.addSystemInfo("Browser", ConfigUtil.config.get("env.browser"));
	    extent.addSystemInfo("AutomationTool", testTool);
		 		
	}
	
	
	
	@AfterSuite
	public void extenEnd() {
		File file = new File(logFolder);
		String absolutePath = file.getAbsolutePath();
		//System.out.println("log path is : "+absolutePath); 
		extent.setTestRunnerOutput("<a href='"+absolutePath+"/Log.txt'>LogFile</a>");
		extent.flush();
		extent.close();
	}
	
	
	/**
	 * Sets the up browser.
	 *
	 * @name setUpBrowser
	 * @description Creates new WebDriver instance based on the configuration and extends that driver with custom Synchronization functions,
	 * 				then setups the browser window.
	 * @author Ramakrishna Doradla Venkatesh   
	 * @return void	||description:
	 * @jiraId 
	 */
	@BeforeClass(alwaysRun = true)
	public void createBrowser() {
		// To remove Log File Warning in Selenium
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		driver = new ExtendWebDriver(instantiateDriver());
		setBrowserWindow(driver);
	}
	
	/**
	 * Sets the up logger.
	 *
	 * @name setUpLogger
	 * @description Instantiates and returns a new Logger object and prints the log header, uses the following Config properties
	 * 				"env.run.type" - runType | "env.release" - release | "env.build" - build.
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return void	||description:
	 * @jiraId 
	 */
	@BeforeClass(alwaysRun = true)
	public void createLogger() {
		try{
			loggerManager = new LoggerManager(this.getClass().getSimpleName());
		}catch(Exception e){
			System.out.println("Failed to create logger instance");
			e.printStackTrace();
			if (driver != null) {
				driver.quit();
			}
			System.exit(0);
		}
	}
	
	@BeforeMethod(alwaysRun = true)
	public void printHeader(Method method) {

		test = extent.startTest((this.getClass().getSimpleName() + "::" + method.getName()));
		test.assignAuthor(ConfigUtil.config.get("env.user"));
		test.assignCategory(loggerManager.getTestCoverage(this.getClass().getName(),method.getName()));
		//test.assignCategory(loggerManager.getTestCoverage(this.getClass().getSimpleName(), method.getName()));
		//String temp = test.getDescription();
		testName= test.getTest().getName();
		testMethodName = testName.substring(testName.indexOf(':')+2);
		test.log(LogStatus.PASS, testMethodName +" started"); 
		//**
		
		try{
			//Getting browser details from WebDriver object
		    Capabilities cap = ((RemoteWebDriver) driver.webdriver).getCapabilities();
		    String browserName = cap.getBrowserName();
		    String browserVer = cap.getVersion().toString();
		    
			HashMap<String, String> scriptProperties = new HashMap<String, String>();
			scriptProperties.put("scriptname", this.getClass().getSimpleName());
			scriptProperties.put("environment", ConfigUtil.config.get("env.environment"));
			scriptProperties.put("coverage", loggerManager.getTestCoverage(this.getClass().getName(),method.getName()));
			scriptProperties.put("execution.date",
			(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date()).toString());
			scriptProperties.put("runType", ConfigUtil.config.get("env.run.type"));
			scriptProperties.put("release", ConfigUtil.config.get("env.release"));
			scriptProperties.put("build", ConfigUtil.config.get("env.build"));
			scriptProperties.put("browser", browserName);
			scriptProperties.put("version", browserVer);
			scriptProperties.put("testtool", testTool);
			
			loggerManager.printHeader(scriptProperties);
		} catch (Exception ex){
			loggerManager.logger.info("Failed to print the Log Header");
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Check web driver.
	 *
	 * @name checkWebDriver
	 * @description Checks the validity of the current WebDriver Object
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return void	||description:
	 * @jiraId 
	 */
	@BeforeMethod(alwaysRun = true)
	public void checkWebDriver() {
		try {
			driver.getWindowHandles();
			driver.getTitle();
        }
        catch (WebDriverException exception){
            System.out.println("Restarting browser");
    		driver = new ExtendWebDriver(instantiateDriver());
    		setBrowserWindow(driver.webdriver);
        }
	} 
	  
	/*
	 *
	 */ 
	@AfterMethod(alwaysRun = true)
	public void closeExtentTest(ITestResult result) throws IOException {
		
		if(result.getStatus()==ITestResult.FAILURE)
		{
		
		String screenShotPath =  LogfileMethodListener.screenPathToLog;
		File file = new File(screenShotPath);
		String absolutePath = file.getAbsolutePath();
		String image= test.addScreenCapture(absolutePath);
		test.log(LogStatus.FAIL,  testMethodName + " is failed" + image );
		
		} else {
			test.log(LogStatus.PASS, test.getDescription() + " is passed");
		}
	    
		extent.endTest(test);
		extent.flush();
	}
	
	public static void logMsgWithScreenShot(String logMsg) throws IOException {
	
		String screenShotPath = captureScreenShot(driver.webdriver,loggerManager);
		File file = new File(screenShotPath);
		String absolutePath = file.getAbsolutePath();
		String image= test.addScreenCapture(absolutePath);
		test.log(LogStatus.PASS, test.getDescription() + logMsg + image);
	}
	
	/**
	 * Drop logger.
	 *
	 * @name dropLogger
	 * @description Kills the active logger Object
	 * @author Ramakrishna Doradla Venkatesh   
	 * @return void	||description:
	 * @jiraId 
	 */
	@AfterClass(alwaysRun = true)
	public void zipLogger() {
		try {
			loggerManager.finishLog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drop driver.
	 *
	 * @name dropDriver
	 * @description Kills the active ExtendedWebDriver object
	 * @author Ramakrishna Doradla Venkatesh   
	 * @return void	||description:
	 * @jiraId 
	 */
	@AfterClass(alwaysRun = true)
	public void zipDriver() {
		if (driver != null) {
	//		driver.quit();
			driver.close();
		}
	}
	
	/**
	 * Get driver.
	 *
	 * @name getDriver
	 * @description Getter for ExtendedWebDriver object
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return ExtendWebDriver	||description: Current ExtendedWebDriver instance
	 * @jiraId 
	 */
	public static ExtendWebDriver getDriver() {
        return driver;
    }
	
	/**
	 * Get logger.
	 *
	 * @name getLogger
	 * @description Getter for current logger object
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return Logger	||description: Current logger instance
	 * @jiraId 
	 */
	public static Logger getLogger() {
		return loggerManager.logger;
	}
	
	/**
	 * Get logger manager.
	 *
	 * @name getLoggerManager
	 * @description Getter for LoggerManager object
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return LoggerManager	||description: Current LoggerManager instance
	 * @jiraId 
	 */
	public static LoggerManager getLoggerManager() {
		return loggerManager;
	}
	
	/**
	 * Get primary driver.
	 *
	 * @name getPrimaryDriver
	 * @description Getter for original WebDriver object
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return WebDriver	||description: Current WebDriver instance
	 * @jiraId 
	 */
	public static WebDriver getPrimaryDriver(){
		return driver.getWebDriver();
	}
	
	/**
	 * Instantiate driver.
	 *
	 * @name instantiateDriver
	 * @description Instantiates and returns the driver based on the property set for "env.browser"
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return WebDriver	||description: A new instance of WebDriver
	 * @jiraId 
	 */
	private static WebDriver instantiateDriver() {
		WebDriver webdriver = null;
		/*
		 * HashMap<String, Integer> contentSettings = new HashMap<String, Integer>();
		 * HashMap<String, Object> profile = new HashMap<String, Object>();
		 * HashMap<String, Object> prefs = new HashMap<String, Object>();
		 
		 * contentSettings.put("notifications", 2); contentSettings.put("geolocation",2); 
		 * contentSettings.put("media_stream", 1);
		 * profile.put("managed_default_content_settings", contentSettings);
		 * prefs.put("profile", profile);
		 */
		
		//options.setExperimentalOption("prefs", prefs);
				
		try{    
			String browser = ConfigUtil.config.get("env.browser");
			switch (browser) {
				case "Firefox":
					System.setProperty("webdriver.gecko.driver",".\\resources\\drivers\\geckodriver.exe");
					webdriver = new FirefoxDriver();
					break;
				case "Chrome":

					System.setProperty("webdriver.chrome.driver", ".\\resources\\drivers\\chromedriver.exe");
					//WebDriverManager.chromedriver().setup();
					ChromeOptions options = new ChromeOptions();
				    options.addArguments("--disable-notifications");
					webdriver = new ChromeDriver(options);
     				 break;
				case "IE":
					System.setProperty("webdriver.ie.driver", ".\\resources\\drivers\\IEDriverServer.exe");
					webdriver  = new InternetExplorerDriver();
					break;
				case "Safari":
					webdriver = new SafariDriver();
					break;
				default:
                    throw new IllegalStateException("Browser is not supported");
				}
		}catch(Exception e){
			System.out.println("Fatal Error: Failed to create WebDriver instance, quitting test.");
			System.out.println("ERROR MESSAGE: "+e.getMessage());
			System.exit(0);
		}
			return webdriver;	
		}

	/**
	 * Sets the browser window.
	 *
	 * @name setBrowserWindow
	 * @description Maximizes the webdriver's browser window 
	 * @author Ramakrishna Doradla Venkatesh  
	 * @param webdriver ||description: Webdriver's instance
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	private static void setBrowserWindow(WebDriver webdriver) {
		if (webdriver != null) {
			//Maximize the browser window
			webdriver.manage().window().maximize();
			webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			webdriver.manage().timeouts().pageLoadTimeout(120,TimeUnit.SECONDS);
		}
 	}
	
	/*
	public static String captureScreenShot(WebDriver driver,LoggerManager logManager) throws IOException {
		
		
			logFolder = logManager.getFullLocalLogFolderPath();
			logFolder = logFolder.replace("Logs", "extentReports");
			// take the screenshot at the end of every test
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			// generate the screenshot file name
			String screenshotFile = "Test_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date()).toString()
					+ ".png";
			String screenPath = logFolder + "\\" + screenshotFile;
			String screenPathToLog = logManager.getFullLocalLogFolderPath() + "\\" + screenshotFile;
			
			// save the screenshot to a file at some place
			FileUtils.copyFile(scrFile, new File(screenPath));
			
			return screenPathToLog;
		
	}*/
	
	public static String captureScreenShot(WebDriver driver,LoggerManager logManager) throws IOException {
		
		logFolder = logManager.getFullLocalLogFolderPath();
	
		// take the screenshot at the end of every test
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// generate the screenshot file name
		String screenshotFile = "Test_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date()).toString()
				+ ".png";
		String screenPath = extentReportBaseFolder + "\\" + screenshotFile;
		String screenPathToLog = extentReportBaseFolder + "\\" + screenshotFile;
		
		// save the screenshot to a file at some place
		FileUtils.copyFile(scrFile, new File(screenPath));
		
		return screenPathToLog;	
	}

	public static void logMsgs(String logMsg) {
		loggerManager.logger.info(logMsg);
		//test.lo
	}
}