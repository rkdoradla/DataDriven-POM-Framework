package com.oce.nuxeo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.base.pageframework.BasePage;

public class HomePage extends BasePage {

//	public By viewProfile_Image = By.xpath("//span/*[@title= 'User']");
//	public By profileLabel_Link = By.xpath("//h1/a[@class = 'profile-link-label']");
//	public By contentTypes_DropDown = By.xpath("//a[@title = 'Content Types']/following::one-app-nav-bar-item-dropdown//*[@data-key = 'chevrondown']");
//	public By newContent_Link = By.xpath("//span[contains(text(),'New Content Type')]");
	
	public HomePage() {
		  	PageFactory.initElements(driver, this);
	}
	
	public By dropDownItem(String menuItem) {
		return By.xpath("//a[@title = '"+menuItem+"']");
	}
	///following::one-app-nav-bar-item-dropdown//*[@data-key = 'chevrondown']
	
    @FindBy(xpath = "//div[@data-aura-class = 'forceEntityIcon']")
    public WebElement viewProfile_Image;
    
    @FindBy(xpath = "//h1/a[@class = 'profile-link-label']")
    public WebElement profileLabel_Link;
    
    @FindBy(xpath = "//a[@title = 'Content Types']/following::one-app-nav-bar-item-dropdown//*[@data-key = 'chevrondown']")
    public WebElement contentTypes_DropDown;
    
    @FindBy(xpath = "//a[@title = 'Content Types']")
    public WebElement contentTypesLabel_Link;
    
    
    @FindBy(xpath = "//one-app-nav-bar-menu-item//span[contains(text(),'New Content Type')]")
    public WebElement newContent_Link;
    
    @FindBy(xpath = "//div[contains(text(),'New')]")
    public WebElement newContent_Link1;
    
    @FindBy(xpath = "//a[contains(text(),'Log Out')]")
    public WebElement logOutLink;
    

}
