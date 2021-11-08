/*
 * 
 */
package com.base.pageframework;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.base.configuration.ConfigUtil;
import com.base.lib.Utils;
import com.base.logging.LoggerManager;

/**
 * 	Class implements WebDriver and insert Sync functionality to handle the element synchronization dynamically.
 *  
 * 	@author Ramakrishna Doradla Venkatesh 
 * 	@version 
 * 	@since 
 */

public class ExtendWebDriver implements WebDriver
{
	public WebDriver webdriver;
	public int timeout = Integer.parseInt(ConfigUtil.config.get("env.base.timeout"));
	protected static LoggerManager loggerManager;
	//
	
	/**
	 * Extend web driver.
	 *
	 * @param webdriver ||description: WebDriver object to be extended
	 * ||allowedRange:
	 */
	ExtendWebDriver(WebDriver webdriver)
	{
		this.webdriver = webdriver;
	}
	
	ExtendWebDriver() throws Exception
	{
		loggerManager = new LoggerManager(this.getClass().getSimpleName());
	}
	
	/**
	 * @name get
	 * @description Load a new web page in the current browser window 
	 * @author Ramakrishna Doradla Venkatesh 
	 * @param paramString ||description: Url of the webpage
	 *				||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void get(String paramString) {
		webdriver.get(paramString);
		
	}

	/**
	 * @name getCurrentUrl
	 * @description Get a string representing the current URL that the browser is looking at.
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return String	||description: Url String
	 * @jiraId 
	 */
	@Override
	public String getCurrentUrl() {
		return webdriver.getCurrentUrl();
	}

	/**
	 * @name getTitle
	 * @description The title of the current page.
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return String	||description: Current page title
	 * @jiraId 
	 */
	@Override
	public String getTitle() {
		return webdriver.getTitle();
	}


	/**
	 * @name findElements
	 * @description Waits for WebPage & active JQuery to load and then find all elements within the current context using the given locator.
	 * 				The element gets synchronized against expected condition Clickable and then it is returned. 
	 * @author Ramakrishna Doradla Venkatesh  
	 * @param paramBy ||description: The By value locator of the element
	 *				||allowedRange:
	 * @return List	||description: List<WebElement>  
	 * @jiraId 
	 */
	@Override
	public List<WebElement> findElements(By paramBy) {
		try{
			BasePage.waitForPageToLoad(timeout);
//			if (timeout/100 > 2000)
//			{
//				Utils.waitForMSeconds(2000);
//			}
//			else
//			{
//				Utils.waitForMSeconds(timeout/100);
//			}
			
			JavascriptExecutor js = (JavascriptExecutor) webdriver;
			js.executeScript("var forceRedraw = function(element){\r\n" + 
					"  var disp = element.style.display;\r\n" + 
					"  element.style.display = 'none';\r\n" + 
					"  var trick = element.offsetHeight;\r\n" + 
					"  element.style.display = disp;\r\n" + 
					"};");
			
			Wait<WebDriver> wait = new FluentWait<WebDriver>(webdriver)
				       .withTimeout(timeout,TimeUnit.MILLISECONDS)
				       .pollingEvery(1, TimeUnit.SECONDS)
				       .ignoring(NoSuchElementException.class)
					   .ignoring(StaleElementReferenceException.class);

				 		wait.until(ExpectedConditions.and(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(paramBy)), new ExpectedCondition<Boolean>(){	
							@Override
							public Boolean apply(WebDriver driver) 
							{	
								return driver.findElement(paramBy).isEnabled();
							}	
				 		}
				 		));
			return webdriver.findElements(paramBy);
	
			}catch(Exception e){
						throw e;
			}
	}

	/**
	 * @name findElement
	 * @description Waits for WebPage & active JQuery to load and then find the first WebElement using the given locator.
	 * 				The element gets synchronized against expected condition Clickable and then it is returned. 
	 * @author Ramakrishna Doradla Venkatesh 
	 * @param paramBy ||description: The By value locator of the element
	 *				||allowedRange:
	 * @return WebElement	||description: The located WebElement
	 * @jiraId 
	 */
	@Override
	public WebElement findElement(By paramBy) 
	{
		
			try
			{
				BasePage.waitForPageToLoad(timeout);
//				if (timeout/100 > 2000)
//				{
//					Utils.waitForMSeconds(2000);
//				}
//				else
//				{
//					Utils.waitForMSeconds(timeout/100);
//				}
				
				JavascriptExecutor js = (JavascriptExecutor) webdriver;
				js.executeScript("var forceRedraw = function(element){\r\n" + 
						"  var disp = element.style.display;\r\n" + 
						"  element.style.display = 'none';\r\n" + 
						"  var trick = element.offsetHeight;\r\n" + 
						"  element.style.display = disp;\r\n" + 
						"};");
				
				Wait<WebDriver> wait = new FluentWait<WebDriver>(webdriver)
					       .withTimeout(timeout,TimeUnit.MILLISECONDS)
					       .pollingEvery(1, TimeUnit.SECONDS)
					       .ignoring(NoSuchElementException.class)
						   .ignoring(StaleElementReferenceException.class);

					 		wait.until(ExpectedConditions.and(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(paramBy)), new ExpectedCondition<Boolean>(){	
								@Override
								public Boolean apply(WebDriver driver) 
								{	
									return driver.findElement(paramBy).isEnabled();
								}	
					 		}
					 		));
				return webdriver.findElement(paramBy);
		
		}catch(Exception e){
					throw e;
		}	
	}

	/**
	 * @name getPageSource
	 * @description Get the source of the last loaded page
	 * @author Ramakrishna Doradla Venkatesh  
	 * @return String ||description: Source code of the page as String
	 * @jiraId 
	 */
	@Override
	public String getPageSource() {
		return webdriver.getPageSource();
	}

	/**
	 * @name close
	 * @description Close the current window, quitting the browser if it's the last window currently open.
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void close() {
		webdriver.close();
	}

	/**
	 * @name quit
	 * @description Quits this driver, closing every associated window.
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return void	||description:
	 * @jiraId 
	 */
	@Override
	public void quit() {
		webdriver.quit();
	}

	/**
	 * @name getWindowHandles
	 * @description Return a set of window handles which can be used to iterate over all open 
	 * 				windows of this WebDriver instance by passing them to switchTo().WebDriver.Options.window()
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return Set<String>	||description: Set<"Window Handle String">
	 * @jiraId 
	 */
	@Override
	public Set<String> getWindowHandles() {
		return webdriver.getWindowHandles();
	}

	/**
	 * @name getWindowHandle
	 * @description Return an opaque handle to this window that uniquely identifies it within this driver instance.
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return String	||description: Current window's handle as String
	 * @jiraId 
	 */
	@Override
	public String getWindowHandle() {
		return webdriver.getWindowHandle();
	}

	/**
	 * @name switchTo
	 * @description Switch commands to a different frame or window
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return TargetLocator	||description: A TargetLocator which can be used to select a frame or window
	 * @jiraId 
	 */
	@Override
	public TargetLocator switchTo() {
		return webdriver.switchTo();
	}

	/**
	 * @name navigate
	 * @description Allows the driver to access the browser's history and to navigate to a given URL
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return Navigation	||description: A WebDriver.Navigation that allows the selection of what to do next
	 * @jiraId 
	 */
	@Override
	public Navigation navigate() {
		return webdriver.navigate();
	}

	/**
	 * @name manage
	 * @description Gets the Option interface
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return Options	||description: An option interface
	 * @jiraId 
	 */
	@Override
	public Options manage() {
		return webdriver.manage();
	}
	
	/**
	 * Get web driver.
	 *
	 * @name getWebDriver
	 * @description Getter for original WebDriver object
	 * @author Ramakrishna Doradla Venkatesh 
	 * @return WebDriver	||description: WebDriver Object
	 * @jiraId 
	 */
	public WebDriver getWebDriver(){
		return webdriver;
	}

}
