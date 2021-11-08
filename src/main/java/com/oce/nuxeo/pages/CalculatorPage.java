package com.oce.nuxeo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.base.pageframework.BasePage;


/**
 * @author Ramakrishna Doradla Venkatesh 
 */

public class CalculatorPage extends BasePage {
public CalculatorPage(String url) {
    	webdriver.get(url);
    	PageFactory.initElements(webdriver, this);
    }

    @FindBy(css = "[value='=']")
    public WebElement equalButton;
    
    @FindBy(css = "[value='+']")
    public WebElement plusButton;

    @FindBy(css = "[value='–']")
    public WebElement minusButton;

    @FindBy(css = "[value='×']")
    public WebElement multiplyButton;

    @FindBy(css = "[value='÷']")
    public WebElement divideButton;

    @FindBy(tagName = "textarea")
    public WebElement taperoll;
    
    @FindBy(xpath = "//*[text() = 'Got It']")
    public WebElement popUP;
    
//    @FindBy(xpath = "//*[contains(@id, 'fld') and @class = 'tapefld dtfld']")
//    public WebElement resultInput ;
    
    public By resultInput = By.xpath("//*[contains(@id, 'fld') and @class = 'tapefld dtfld']");
    
    public String getTitle() {
        return webdriver.getTitle();
    }
    
    public void handlePopUP() {
    	popUP.click();	
    }

    public void pressNumber(char number) {
        webdriver.findElement(By.cssSelector("[type=\"button\"][value = \"" + number + "\"]")).click();
    }

    public void pressPlus() {
    	plusButton.click();
    }

    public void pressMinus() {
        minusButton.click();
    }

    public void pressMultipy() {
        multiplyButton.click();
    }

    public void pressDivide() {
        divideButton.click();
    }

    public void pressEqual() {
        equalButton.click();
    }

    public String getResultValue() {
    	List<WebElement> results = webdriver.findElements(resultInput);
        return results.get(results.size()-1).getAttribute("value");
    }
}