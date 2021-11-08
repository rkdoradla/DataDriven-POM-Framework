package com.iqvia.contentCentral.smoketests;

import com.base.configuration.ConfigUtil;
import com.base.lib.Excel;
import com.base.pageframework.BaseTest;
import com.oce.nuxeo.steps.ClcSteps;
import com.oce.nuxeo.steps.Home;
import com.oce.nuxeo.steps.Login;
import com.oce.nuxeo.steps.NewContentType;
import com.relevantcodes.extentreports.LogStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Ramakrishna Doradla Venkatesh
 */

public class ContentCenterFlow1 extends BaseTest {
	Login objLogin;
	Home objHome;
	NewContentType objNewContentType;
	String url;

	@BeforeMethod()
	public void setUp() {
		url = ConfigUtil.config.get("env.base.url");
		objLogin = new Login();
		objHome = new Home();
		objNewContentType = new NewContentType();
		loggerManager.logger.info("Reading the excel test data sheet");
		// objLogin.openApplication(url);
	}

	@DataProvider
	public Object[][] getLoginData(Method method) {
		return Excel.getData(method, Test.class,
				ConfigUtil.config.get("env.testdata.location") + "\\regressionTestData\\ContentCentral_TestData.xlsx",
				"loginTestData");
	}

	@Test(enabled = true, priority = 1, dataProvider = "getLoginData", description = "Content central login verification; Coverage: loginVerification", testName = "loginTest")

	public void loginTest(LinkedHashMap<String, String> DataSet) throws IOException, InterruptedException {
		objLogin.openApplication(url);
		objLogin.loginToApplication(DataSet);
		objHome.verifyUserProfile(DataSet);
		//logMsgWithScreenShot("Profile verified successfully");
	}

	@DataProvider
	public Object[][] getNewContentData(Method method) {
		return Excel.getData(method, Test.class,
				ConfigUtil.config.get("env.testdata.location") + "\\regressionTestData\\ContentCentral_TestData.xlsx",
				"NewContent");
	}

	//@Test(enabled = true, priority = 2, dataProvider = "getNewContentData", description = "New content creation; Coverage: NewContentCreation", testName = "contentCreationTest")

	public void contentCreationTest(LinkedHashMap<String, String> DataSet) throws IOException, InterruptedException {
		objHome.navigateToCreateNewContentType(DataSet);
		objNewContentType.verifyNavigatingToNewContentPage();
		objNewContentType.fillCoreDetails(DataSet);
		objNewContentType.fillPlanDetails(DataSet);
		objNewContentType.verifyContentSubmission(DataSet);
		//objHome.logOut();
		System.out.println("end of test");
	}

	@AfterMethod
	public void teardown()
	{
		objHome.logOut();
	}
	
}