package com.oce.nuxeo.steps;

import java.util.LinkedHashMap;

import com.base.pageframework.BaseSteps;
import com.oce.nuxeo.pages.LoginPage;

public class Login extends BaseSteps {

	LoginPage objLoginPage;
	
	public Login() {
		objLoginPage = new LoginPage();
	}
	
	public void openApplication(String url) {
		driver.get(url);
	}
	
	public void loginToApplication(LinkedHashMap<String, String> DataSet) throws InterruptedException {
		objLoginPage.userName_TextBox.sendKeys(DataSet.get("UserName"));
		objLoginPage.passWord_TextBox.sendKeys(DataSet.get("PassWord"));
		objLoginPage.login_Button.click();
		Thread.sleep(2000);
	}
	
	public void verifyLoginError() {
		
	}
	
	
}
