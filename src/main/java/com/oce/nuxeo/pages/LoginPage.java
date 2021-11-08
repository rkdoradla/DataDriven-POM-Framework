package com.oce.nuxeo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.base.pageframework.BasePage;

public class LoginPage extends BasePage {
	
//	public By userName_TextBox = By.id("username");
//	public By passWord_TextBox = By.id("password");
//	public By login_Button = By.id("Login");
//	public By errorMsg_Text = By.id("error");
	
	
	public LoginPage() {
	//	public LoginPage(String url) {
    	//webdriver.get(url);
    	PageFactory.initElements(webdriver, this);
    }

    @FindBy(id = "username")
    public WebElement userName_TextBox;
    
    @FindBy(id = "password")
    public WebElement passWord_TextBox;
    
    @FindBy(id = "Login")
    public WebElement login_Button;
    
    @FindBy(id = "error")
    public WebElement errorMsg_Text;
    

}
