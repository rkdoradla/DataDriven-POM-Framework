package com.base.listeners;

import com.base.configuration.ConfigUtil;
import com.base.logging.LoggerManager;
import com.base.pageframework.BaseTest;
import com.base.pageframework.ExtendWebDriver;
//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
 
/**
 * Class has methods to listen the test methods, on any error or test failure it
 * captures the error and logs it to log file.
 *
 * @author Ramakrishna Doradla Venkatesh
 * @version
 * @since 
 */

public class LogfileMethodListener implements IInvokedMethodListener {
	
	
	public static String screenPathToLog;
	
	
	/**
	 * @name beforeInvocation
	 * @description Gets invoked before each TestNG annotated method gets executed
	 * @author Ramakrishna Doradla Venkatesh
	 * @param  method ||description: The method which is going to be executed
	 *				||allowedRange:
	 ** @param  testResult ||description: TestNG testResult object
	 *				||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		
	}

	

	/**
	 * @name afterInvocation
	 * @description Gets invoked after each TestNG annotated method gets executed, catches error from result object and logs the failure along with the screenshot.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param  method ||description: The method which was just executed
	 *				||allowedRange:
	 ** @param  testResult ||description: TestNG testResult object
	 *				||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	
		boolean isTestSkipped = false;

		boolean isResultValid = testResult.getThrowable() != null && testResult.getThrowable().getMessage() != null;

		if (isResultValid) {
			isTestSkipped = testResult.getThrowable().getMessage().contains("Test skipped");
		} else {
			isTestSkipped = true;
		}

		if (!testResult.isSuccess() && (!isTestSkipped)) {
			// WebDriver driver;
			ExtendWebDriver driver;
			LoggerManager logManager;
			try {
				Field fieldDriver = method.getTestMethod().getInstance().getClass().getSuperclass()
						.getDeclaredField("driver");
				fieldDriver.setAccessible(true);
				driver = (ExtendWebDriver) fieldDriver.get(method.getTestMethod().getInstance());
				fieldDriver.setAccessible(false);

				Field fieldLogManager = method.getTestMethod().getInstance().getClass().getSuperclass()
						.getDeclaredField("loggerManager");
				fieldLogManager.setAccessible(true);
				logManager = (LoggerManager) fieldLogManager.get(method.getTestMethod().getInstance());
				fieldLogManager.setAccessible(false);

				// log error
				String errorMessage = testResult.getThrowable().getMessage();
				String errorMethodName = "";
				int errorLine = -1;
				StackTraceElement errorMethod = null;
				StackTraceElement[] stackTraceElements = testResult.getThrowable().getStackTrace();
				if (method.isTestMethod()) {
					for (StackTraceElement element : stackTraceElements) {
						if (element.getMethodName().contains(method.getTestMethod().getMethodName())) {
							if (errorMethod != null) {
								errorLine = errorMethod.getLineNumber();
								errorMethodName = errorMethod.getMethodName();
								break;
							} else {
								errorLine = element.getLineNumber();
								errorMethodName = element.getMethodName();
							}
						}
						errorMethod = element;
					}
				} else {
					for (StackTraceElement element : stackTraceElements) {
						if (element.getClassName().contains("com.")) {
			 				errorLine = element.getLineNumber();
							errorMethodName = element.getMethodName();
							break;
						}
					}
				}

				String failureDescription = "FAIL: Function = " + errorMethodName + " at line " + errorLine
						+ "\nDescription: " + errorMessage + "\nStacktrace: ";
				for (StackTraceElement element : stackTraceElements) {
					failureDescription += (element + "\n");
				}
				// taking screenshot
				if (driver != null) {
					try {
						
						failureDescription += webdriverScreenshot(driver.webdriver, logManager);
						
						
					} catch (Exception ex) {
						failureDescription += desktopScreenshot(logManager);
					}
				} else {
					failureDescription += desktopScreenshot(logManager);
				}
				failureDescription += ("\nLOG ANALYSIS:" + "\nTester:" + "\nDisposition:" + "\nDefectId:"
						+ "\nComments:");
				logManager.logger.error(failureDescription);

			/*	// Closing the driver object
				if (driver != null) {
					System.out.println("Test Failed, Closing the browser instance");
					driver.quit();
					driver = null; 
				} */

			} catch (NoSuchFieldException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Webdriver screenshot.
	 * 
	 * @name webdriverScreenshot
	 * @description Takes a screenshot of the application under test and stores that in the current log folder
	 * @author Ramakrishna Doradla Venkatesh
	 * @param driver ||description: WebDriver Object
	 * ||allowedRange:
	 * @param logManager ||description: LogManger Object
	 * ||allowedRange:
	 * @return String	||description:  Path of the screenshot file
	 * @throws IOException 
	 * @jiraId 
	 */
	public static String webdriverScreenshot(WebDriver driver, LoggerManager logManager) {
		String logInfo = "";
		try {
			
			screenPathToLog = BaseTest.captureScreenShot(driver,logManager);
			logInfo = "Bitmap: " + screenPathToLog;
			
		} catch (Exception exception) {
			logInfo = "Bitmap: could not create the screenshot " + exception.getMessage();
		}
		return logInfo;
	}


	/**
	 * Desktop screenshot.
	 *
	 * @name desktopScreenshot
	 * @description Takes a screenshot of the desktop and stores that in the current log folder
	 * @author Ramakrishna Doradla Venkatesh
	 * @param logManager ||description: LogManger Object
	 * ||allowedRange:
	 * @return String	||description: Path of the screenshot file
	 * @throws Exception 
	 * @jiraId 
	 */
	public static String desktopScreenshot(LoggerManager logManager) {
		String logInfo = "";
		try {
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage capture;
			String logFolder = logManager.getFullLocalLogFolderPath();
			// generate the screenshot path
			String screenshotFile = "Test_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date()).toString()
					+ ".png";
			String screenPath = logFolder + "\\" + screenshotFile;
			String screenPathToLog = logManager.getFullRemoteLogFolderPath() + "\\" + screenshotFile;
			
		
			// create the screenshot to a file at some place
			capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "png", new File(screenPath));
			logInfo = "Bitmap: " + screenPathToLog;
		} catch (Exception exception) {
			logInfo = "Bitmap: could not create the screenshot " + exception.getMessage();
		}
		return logInfo;
	}

	/**
	 * Verify error.
	 *
	 * @name verifyError
	 * @description On error it logs the error and checks the property "env.verify.stop" and decides to continue or stop the test based on the property value
	 * @author Ramakrishna Doradla Venkatesh
	 * @param Er ||description: Error Object
	 * ||allowedRange:
	 * @return void	||description: 
	 * @jiraId 
	 */
	public static void verifyError(Error Er) {
		try {
			if (ConfigUtil.config.get("env.verify.stop").trim().toLowerCase().equals("false")) {

				ExtendWebDriver driver = BaseTest.getDriver();
				LoggerManager logManager = BaseTest.getLoggerManager();
				

				String errorMessage = Er.getMessage();
				String errorMethodName = Er.getStackTrace()[6].getMethodName();
				int errorLine = Er.getStackTrace()[6].getLineNumber();

				String failureDescription = "FAIL: Function = " + errorMethodName + " at line " + errorLine
						+ "\nDescription: " + errorMessage +"\n";

				// taking screenshot
				if (driver != null) {
					try {
						failureDescription += webdriverScreenshot(driver.webdriver, logManager);
					} catch (WebDriverException ex) {
						failureDescription += desktopScreenshot(logManager);
					}
				} else {
					failureDescription += desktopScreenshot(logManager);
				}
				failureDescription += ("\nLOG ANALYSIS:" + "\nTester:" + "\nDisposition:" + "\nDefectId:"
						+ "\nComments:");
				logManager.logger.error(failureDescription);
			}

			else {
				throw Er;
			}
		} catch (Error er) {
			throw er;
		} catch (Exception ex) {
			throw ex;
		}

	}

}
