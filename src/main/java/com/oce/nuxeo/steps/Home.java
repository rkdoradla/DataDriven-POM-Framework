package com.oce.nuxeo.steps;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.base.configuration.ConfigUtil;
import com.base.pageframework.BaseSteps;
import com.base.pageframework.BaseTest;
import com.oce.nuxeo.pages.HomePage;
import com.oce.nuxeo.pages.LoginPage;

public class Home extends BaseSteps {

	HomePage objHomePage;
	
	public Home() {
		objHomePage = new HomePage();
	}
	
	public void verifyUserProfile(LinkedHashMap<String, String> DataSet) throws InterruptedException, IOException {
	   waitForElementClickable(objHomePage.viewProfile_Image, Integer.parseInt(ConfigUtil.config.get("env.base.timeout")));
	   objHomePage.viewProfile_Image.click();
	   Thread.sleep(3000);
	   waitForElementClickable(objHomePage.profileLabel_Link, DEFAULT_TIMEOUT);
	   String actualText = objHomePage.profileLabel_Link.getText();
	  // String actualText = "ZxZx";
	   assertTrue(actualText.contains(DataSet.get("ProfileName")), DataSet.get("ProfileErrorMsg"));
	   BaseTest.logMsgWithScreenShot("Profile verified successfully");
	   objHomePage.viewProfile_Image.click();

	}
	
	public void navigateToCreateNewContentType(LinkedHashMap<String, String> DataSet) throws InterruptedException {
		 javaScriptClick(objHomePage.contentTypesLabel_Link);
		 Thread.sleep(1000);
		 javaScriptClick(objHomePage.newContent_Link1);
		}
	
	
	public void logOut() {
	   waitForElementClickable(objHomePage.viewProfile_Image, Integer.parseInt(ConfigUtil.config.get("env.base.timeout")));
	   objHomePage.viewProfile_Image.click();
	    waitForElementClickable(objHomePage.logOutLink, Integer.parseInt(ConfigUtil.config.get("env.base.timeout")));
		objHomePage.logOutLink.click();
	}
	
	
	
}
