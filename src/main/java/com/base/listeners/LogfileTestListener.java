/*
 * 
 */
package com.base.listeners;

import com.base.configuration.ConfigUtil;
import com.base.logging.LoggerManager;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Class has methods to be invoked before each test. Methods to update test
 * methods to ConfigUtil and log iteration details in the log, if dataproviders
 * functionality is used to parameterize test.
 *
 * @author Ramakrishna Doradla Venkatesh
 * @version 
 * @since
 */

public class LogfileTestListener extends TestListenerAdapter {


	/**
	 * @name onStart
	 * @description Get invoked after the test class is instantiated and reads configuration properties.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param testContext ||description: Test context which contains all the information for a given test run
	 *				||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void onStart(ITestContext testContext) {
		try {
			super.onStart(testContext);
			ITestNGMethod[] methods = testContext.getAllTestMethods();
			ConfigUtil.setAllTestMethods(methods);
		}catch(Exception e) {
			throw e;
		}
	}

	/**
	 * @name onTestStart
	 * @description Gets invoked each time before a test will be invoked, logs the iteration details "@@Iteration: value",
	 * 				value is from primary column set as Config property "env.data.primarycolumn". 
	 * @author Ramakrishna Doradla Venkatesh
	 * @param tr ||description: The result object of a test
	 *				||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void onTestStart(ITestResult tr) {
		try {
			Field fieldLogManager = tr.getMethod().getInstance().getClass().getSuperclass()
					.getDeclaredField("loggerManager");
			fieldLogManager.setAccessible(true);
			LoggerManager logManager = (LoggerManager) fieldLogManager.get(tr.getMethod().getInstance());
			fieldLogManager.setAccessible(false);
			Object[] iterationDataObj = tr.getParameters();
			try {
				if (iterationDataObj.length > 0) {
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, String> iterationData = (LinkedHashMap<String, String>) iterationDataObj[0];
					logManager.logger.info("@@Iteration:" + iterationData.get(ConfigUtil.config.get("env.data.primarycolumn")));
				}
			} catch (ClassCastException ex) {
				logManager.logger.info("Cannot get iteration data from the data set, data received is of type "
						+ iterationDataObj.getClass().getName());

			}
		} catch (NoSuchFieldException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}
}
