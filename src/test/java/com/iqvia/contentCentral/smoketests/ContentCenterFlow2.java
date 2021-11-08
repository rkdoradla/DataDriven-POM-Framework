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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Ramakrishna Doradla Venkatesh
 */

public class ContentCenterFlow2 extends BaseTest {
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
        //objLogin.openApplication(url);
 	}

   @DataProvider
   public Object[][] getLoginData(Method method){
	  return Excel.getData(method, Test.class,ConfigUtil.config.get("env.testdata.location")+"\\regressionTestData\\ContentCentral_TestData.xlsx", "loginTestData");
   }
   @Test(enabled = true,priority =1, dataProvider = "getLoginData",description="Content central login verification; Coverage: loginVerification", testName="loginTest1")
		  
    public void loginTest(LinkedHashMap<String, String> DataSet) throws IOException, InterruptedException{
	        objLogin.openApplication(url);
	   		objLogin.loginToApplication(DataSet);
	  		objHome.verifyUserProfile(DataSet); 
     }

  
}