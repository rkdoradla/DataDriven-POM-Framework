package com.base.pageframework;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import com.base.configuration.ConfigUtil;
import com.base.lib.Utils;
import com.base.logging.LoggerManager;

/**
 * Class has methods to handle basic page level actions & synchronization functions. 
 * The methods can be directly accessed in application steps class by extending BaseSteps class.
 * 
 *  @author Ramakrishna Doradla Venkatesh
 * 	@version 
 * 	@since    
 */

public class BasePage 
{

    protected static ExtendWebDriver driver;
    protected static WebDriver webdriver;
    protected static Logger logger;
    protected static LoggerManager loggerManager; 
    public static final int DEFAULT_TIMEOUT = Integer.parseInt(ConfigUtil.config.get("env.base.timeout"));

    
   /**
    * Base page.
    */
   public BasePage() 
   {
        BasePage.driver = BaseTest.getDriver();
        BasePage.webdriver = BaseTest.getPrimaryDriver();
        BasePage.logger = BaseTest.getLogger();
        BasePage.loggerManager = BaseTest.getLoggerManager();
   }

   public void javaScriptClick(WebElement ele) {
	   ((JavascriptExecutor) webdriver).executeScript("arguments[0].click();", ele);
   }

    /**
     * Scroll to bottom.
     *
     * @name scrollToBottom
     * @description Scroll to the bottom of page
     * @author  Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @jiraId 
     */
    public void scrollToBottom() 
    {
    	try
    	{
	        if (webdriver instanceof JavascriptExecutor)
	        {
	        	Utils.waitForMSeconds(1000);
	            ((JavascriptExecutor) webdriver)
	                    .executeScript("window.scrollTo(0, document.body.scrollHeight)");
	        }
    	} catch(Exception e)
    	{
    		if (e.getMessage().toLowerCase().contains("javascript error"))
	   		{	
				new SkipException("JavaScriptExecutor's exception thrown:");
				loggerManager.logger.info(e.getMessage());
	   		} else
			throw e;
    	}
    }


    /**
     * Scroll to top.
     *
     * @name scrollToTop
     * @description Scroll to the top of page 
     * @author  Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @jiraId 
     */
    public void scrollToTop() 
    {
    	try
    	{
        if (webdriver instanceof JavascriptExecutor)
        {
        	Utils.waitForMSeconds(1000);
            ((JavascriptExecutor) webdriver)
                    .executeScript("window.scrollTo(0,0);");
        }
    	}
    	catch(Exception e)
    	{
    		if (e.getMessage().toLowerCase().contains("javascript error"))
	   		{	
				new SkipException("JavaScriptExecutor's exception thrown:");
				loggerManager.logger.info(e.getMessage());
	   		} else
			throw e;
    	}
    }

 
    /**
     * Scroll to element.
     *
     * @name scrollToElement
     * @description Scroll up/down to the element on the page
     * @author  Ramakrishna Doradla Venkatesh
     * @param by ||description: Element's Locator
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public static void scrollToElement(By by) 
    {
    	try
    	{
	        WebElement webElement = driver.findElement(by);
	        
	        Actions actions = new Actions(webdriver);
	        actions.moveToElement(webElement);
	        actions.perform();
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }
    
    public static void mouseClick(WebElement ele)  
    {
    	try
    	{
	        Actions actions = new Actions(driver);
	        actions.moveToElement(ele).click().perform();
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }

    
    /**
     * Scroll to.
     *
     * @name scrollTo
     * @description Scroll down to the element on the page
     * @author  Ramakrishna Doradla Venkatesh
     * @param by ||description: Element's Locator
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public static void scrollTo(By by) 
    {
    	try{
    		Utils.waitForMSeconds(1000);
	    	WebElement webElement = driver.findElement(by);
	        ((JavascriptExecutor) webdriver).executeScript(
	                "arguments[0].scrollIntoView();", webElement);
    	} catch(Exception ex)
    	{
    		if (ex.getMessage().toLowerCase().contains("javascript error"))
	   		{	
				new SkipException("JavaScriptExecutor's exception thrown:");
				loggerManager.logger.info(ex.getMessage());
	   		} else
			throw ex;
    	}
    }

    public static void scrollTo(WebElement ele) 
    {
    	try{
    		Utils.waitForMSeconds(1000);
	        ((JavascriptExecutor) webdriver).executeScript(
	                "arguments[0].scrollIntoView();", ele);
    	} catch(Exception ex)
    	{
    		if (ex.getMessage().toLowerCase().contains("javascript error"))
	   		{	
				new SkipException("JavaScriptExecutor's exception thrown:");
				loggerManager.logger.info(ex.getMessage());
	   		} else
			throw ex;
    	}
    }


    /**
     * Wait for ajax.
     *
     * @name waitForAjax
     * @description Waits for any active jQuery to get executed completely
     * @author  Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @jiraId 
     */
    public static void waitForAjax()
    {
    		try{
    			waitForPageToLoad(DEFAULT_TIMEOUT);
    		} catch(Exception e) {
    			throw e;
    		}
		
    }
    

    /**
     * Clear input.
     *
     * @name clearInput
     * @description clear input using BACKSPACE
     * @author  Ramakrishna Doradla Venkatesh
     * @param element ||description: WebElement to be cleared
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void clearInput(WebElement element) {
        try {
        	String text = element.getAttribute("value");
            for (int i = 0; i < text.length(); i++) {
                element.sendKeys(Keys.BACK_SPACE);
            }
        }catch(Exception e) {
        	throw e;
        }
    	
    }

    /**
     * Checks if is element present.
     *
     * @name isElementPresent
     * @description Checks if an element isPresent
     * @author  Ramakrishna Doradla Venkatesh
     * @param elementLocator ||description: ElementLocator locator of the element to check
     * ||allowedRange:
     * @return boolean	||description: True|False, True - If element is found
     * @jiraId 
     */
    public boolean isElementPresent(By elementLocator) 
    {
    	try
    	{
	        webdriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	        List<WebElement> list = webdriver.findElements(elementLocator);
	        webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	        if (list.isEmpty()) 
	        {
	            return false;
	        } 
	        else 
	        {
	            return list.get(0).isDisplayed();
	        }
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    	
    	
    }

    
    /**
     * Checks if is element present.
     *
     * @name isElementPresent
     * @description Checks if element isPresent with timeout
     * @author  Ramakrishna Doradla Venkatesh
     * @param elementLocator ||description: Locator of an element to check
     * ||allowedRange:
     * @param timeoutInMSeconds ||description: Wait timeout in MilliSeconds
     * ||allowedRange:
     * @return boolean	||description: True|False, True if element found
     * @jiraId 
     */
    public boolean isElementPresent(By elementLocator, int timeoutInMSeconds) 
    {
    	try
    	{
	        webdriver.manage().timeouts().implicitlyWait(timeoutInMSeconds, TimeUnit.MILLISECONDS);
	        List<WebElement> list = webdriver.findElements(elementLocator);
	        webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	        if (list.isEmpty()) {
	            return false;
	        } else {
	            return list.get(0).isDisplayed();
	        }
    	}
    	 catch (TimeoutException e) 
        {
 			throw new NoSuchElementException(
 					"The element by locator '" + elementLocator + "' is not present on the page.");
 		}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }


    
    /**
     * Wait for element visible.
     *
     * @name waitForElementVisible
     * @description Waits for an element to be visible
     * @author  Ramakrishna Doradla Venkatesh
     * @param elementLocator ||description: Locator of an element to check
     * ||allowedRange:
     * @param timeoutInMSeconds ||description: Wait timeout in Milliseconds
     * ||allowedRange:
     * @return void	||description:
     * @throws 
     * @jiraId 
     */
    public void waitForElementVisible(By elementLocator, int timeoutInMSeconds) {
       try
       {
    	   Utils.waitForMSeconds(1000);
    	   Wait<WebDriver> wait = new FluentWait<WebDriver>(webdriver)
			       .withTimeout(DEFAULT_TIMEOUT,TimeUnit.MILLISECONDS)
			       .pollingEvery(1, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
    	   			
    	   wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(elementLocator)));
    	   
		} 
       catch (TimeoutException e) 
       {
			throw new NoSuchElementException(
					"The element by locator '" + elementLocator + "' is not visible on the page.");
		}
       catch(Exception ex)
   		{
    	   throw ex;
   		}

    }

    public void waitForElementVisible(WebElement elementLocator, int timeoutInMSeconds) {
        try
        {
     	   Utils.waitForMSeconds(1000);
     	   Wait<WebDriver> wait = new FluentWait<WebDriver>(webdriver)
 			       .withTimeout(DEFAULT_TIMEOUT,TimeUnit.MILLISECONDS)
 			       .pollingEvery(1, TimeUnit.SECONDS)
 			       .ignoring(NoSuchElementException.class);
     	   			
     	   wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(elementLocator)));
     	   
 		} 
        catch (TimeoutException e) 
        {
 			throw new NoSuchElementException(
 					"The element by locator '" + elementLocator + "' is not visible on the page.");
 		}
        catch(Exception ex)
    		{
     	   throw ex;
    		}

     }

    /**
     * Wait for element clickable.
     *
     * @name waitForElementClickable
     * @description Waits for element to be clickable
     * @author  Ramakrishna Doradla Venkatesh
     * @param elementLocator ||description: Locator of an element to check
     * ||allowedRange:
     * @param timeoutInMSeconds ||description: Wait Timeout in Milliseconds
     * ||allowedRange:
     * @return void	||description:
     * @throws
     * @jiraId 
     */
    public void waitForElementClickable(By elementLocator, int timeoutInMSeconds) 
    {
        try
        {
     	   Utils.waitForMSeconds(1000);
     	   Wait<WebDriver> wait = new FluentWait<WebDriver>(webdriver)
 			       .withTimeout(DEFAULT_TIMEOUT,TimeUnit.MILLISECONDS)
 			       .pollingEvery(1, TimeUnit.SECONDS)
 			       .ignoring(NoSuchElementException.class);
     	   			
     	   wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(elementLocator)));	   
 		} 
        catch (TimeoutException e) 
        {
 			throw new NoSuchElementException(
 					"The element by locator '" + elementLocator + "' is not available on the page.");
 		}catch(Exception ex) {
 			throw ex;
 		}
    	
    }

    /**
     * Wait for element clickable.
     *
     * @name waitForElementClickable
     * @description Waits for element to be clickable
     * @author  Ramakrishna Doradla Venkatesh
     * @param elementLocator ||description: Locator of an element to check
     * ||allowedRange:
     * @param timeoutInMSeconds ||description: Wait Timeout in Milliseconds
     * ||allowedRange:
     * @return void	||description:
     * @throws
     * @jiraId 
     */
    public void waitForElementClickable( WebElement elementLocator, int timeoutInMSeconds) 
    {
        try
        {
     	   Utils.waitForMSeconds(1000);
     	   Wait<WebDriver> wait = new FluentWait<WebDriver>(webdriver)
 			       .withTimeout(DEFAULT_TIMEOUT,TimeUnit.MILLISECONDS)
 			       .pollingEvery(1, TimeUnit.SECONDS)
 			       .ignoring(NoSuchElementException.class);
     	   			
     	   wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(elementLocator)));	   
 		} 
        catch (TimeoutException e) 
        {
 			throw new NoSuchElementException(
 					"The element by locator '" + elementLocator + "' is not available on the page.");
 		}catch(Exception ex) {
 			throw ex;
 		}
    	
    }


    /**
     * Wait until tab is opened.
     *
     * @name waitUntilTabIsOpened
     * @description Waits for a tab to be opened
     * @author   Ramakrishna Doradla Venkatesh
     * @param timeoutinMSeconds ||description: Wait timeout in Milliseconds
     * ||allowedRange:
     * @param tabCountToOpen ||description: Number of opened tabs after the tab is opened
     * ||allowedRange:
     * @return void	||description:
     * @throws
     * @jiraId 
     */
    public void waitUntilTabIsOpened(int timeoutinMSeconds, int tabCountToOpen)
    {
    	try
    	{
    		Utils.waitForMSeconds(1000);
      	        	   
    		(new WebDriverWait(webdriver, timeoutinMSeconds)).until(ExpectedConditions.numberOfWindowsToBe(tabCountToOpen));
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }


    /**
     * Switch to the tab by index.
     *
     * @name switchToTheTabByIndex
     * @description  switch to a tab by given index
     * @author  Ramakrishna Doradla Venkatesh
     * @param tabIndex ||description: Tab index to switch
     * ||allowedRange:
     * @return void	||description:
     * @throws
     * @jiraId 
     */
    
    public void switchToTheTabByIndex(int tabIndex) 
    {
        try {
        	waitUntilTabIsOpened(DEFAULT_TIMEOUT, tabIndex);

            ArrayList<String> availableWindows = new ArrayList<String>(webdriver.getWindowHandles());
            if (!availableWindows.isEmpty()) {
                webdriver.switchTo().window(availableWindows.get(tabIndex - 1));
            }

        }catch(Exception e) {
        	throw e;
        }
    }
   

    /**
     * Select drop down by index.
     *
     * @name selectDropDownByIndex
     * @description selects drop down by index
     * @author  Ramakrishna Doradla Venkatesh
     * @param element ||description: Drop down WebElement
     * ||allowedRange:
     * @param index ||description: Index of element to be selected
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void selectDropDownByIndex(WebElement element, int index) {
       try {
    	   Select dropdown = new Select(element);
           dropdown.selectByIndex(index);
       } catch(Exception ex) {
    	   throw ex;
       }
    }


    /**
     * Select drop down by value.
     *
     * @name selectDropDownByValue
     * @description Selects drop down element by value
     * @author  
     * @param element ||description: Drop down WebElement
     * ||allowedRange:
     * @param value ||description: Value of element to be selected
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void selectDropDownByValue(WebElement element, String value) {
        try {
        	Select dropdown = new Select(element);
            dropdown.selectByValue(value);
        } catch(Exception ex) {
        	throw ex;
        }
    }
   

	/**
	 * Wait for element present.
	 *
	 * @name waitForElementPresent
	 * @description Waits for an element to be present
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param elementLocator ||description:
	 * ||allowedRange:
	 * @param timeoutInMSeconds ||description: Wait timeout in Milliseconds
	 * ||allowedRange:
	 * @return void	||description:
	 * @throws
	 * @jiraId 
	 */
	public static void waitForElementPresent(By elementLocator, int timeoutInMSeconds) 
	{
			boolean isTimeToStop = false;
			boolean isPresent = false;
			long endTime = System.currentTimeMillis() + timeoutInMSeconds;
	
			while (!isPresent && !isTimeToStop) 
			{
				isPresent = isElementPresentOnPage(elementLocator);
				if (System.currentTimeMillis() >= endTime) {
					isTimeToStop = true;
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					loggerManager.logger.info(e.getMessage());
				}
			}
	}
	
		
	/**
	 * Checks if is element present on page.
	 *
	 * @name isElementPresentOnPage
	 * @description Checks if is element present on page
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param element ||description: By locator of the element to be checked
	 * ||allowedRange:
	 * @return boolean	||description: True|False, True - If element is displayed
	 * @jiraId 
	 */
	public static boolean isElementPresentOnPage(By element)
	{
		try
		{
			WebElement webelement = webdriver.findElement(element);
			if(webelement.isDisplayed()||webelement.isEnabled())
				return true;
			else
				return false;
		}
		catch (Exception ex) 
		{
			logger.info(ex.getMessage());
			return false;
		}
	}



	/**
	 * Wait for ajax.
	 *
	 * @name waitForAjax
	 * @description Waits for all Ajax requests to get finished
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param timeoutInMilliseconds ||description: Wait timeout in milliseconds
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	public void waitForAjax(int timeoutInMilliseconds)
	{
		try{
			waitForPageToLoad(timeoutInMilliseconds);
		} catch(Exception e) {
			throw e;
		}
	}

	
	/**
	 * Refresh page.
	 *
	 * @name refreshPage
	 * @description Refresh the WebPage 
	 * @author  Ramakrishna Doradla Venkatesh
	 * @return void	||description:
	 * @jiraId 
	 */
	public void refreshPage() 
	{
		try
		{
			WebDriver.Navigation navigation = webdriver.navigate();
			navigation.refresh();
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	
	/**
	 * Get center point of UI element.
	 *
	 * @name getCenterPointOfUIElement
	 * @description Returns center of the given WebElement
	 * @author  
	 * @param element ||description: WebElement to be processed
	 * ||allowedRange:
	 * @return Point	||description: Center Point of the element
	 * @jiraId 
	 */
	public static Point getCenterPointOfUIElement(WebElement element) {
		Point pt= null;
		try {
			Point topLeftPoint = element.getLocation();
			//System.out.println(topLeftPoint.getX());
			//System.out.println(topLeftPoint.getY());
			Dimension elemSize = element.getSize();
			//System.out.println(elemSize.getWidth());
			//System.out.println(elemSize.getHeight());
			//System.out.println(topLeftPoint.getX() + (elemSize.getWidth()) / 2 +"   " +(topLeftPoint.getY() + (elemSize.getHeight()) / 2));
			pt= new Point(topLeftPoint.getX() + (elemSize.getWidth()) / 2,
					topLeftPoint.getY() + (elemSize.getHeight()) / 2);
		}catch(Exception e) {
			throw e;
		}
		return pt;
	}


	
	/**
	 * Switch to opened tab.
	 *
	 * @name switchToOpenedTab
	 * @description Switches to newly opened tab
	 * @author   Ramakrishna Doradla Venkatesh
	 * @return void	||description:
	 * @throws Exception 
	 * @throws
	 * @jiraId 
	 */
	public static void switchToOpenedTab() throws Exception {
		try 
		{
			Thread.sleep(5000);
			
			String oldTab = webdriver.getWindowHandle();
			ArrayList<String> newTab = new ArrayList<String>(webdriver.getWindowHandles());
			newTab.remove(oldTab);
			webdriver.switchTo().window(newTab.get(newTab.size() - 1));
			webdriver.manage().window().maximize();
		} 
		catch (Exception e)
		{
			throw e;
		}
		
	}
	
	
	
	/**
	 * Accept alert.
	 *
	 * @name acceptAlert
	 * @description Accepts an alert Message
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param sBtnText ||description: Button string value to be selected
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	public static void acceptAlert(String sBtnText){
		try{
    		WebDriverWait wait = new WebDriverWait(webdriver, 2);
    		wait.until(ExpectedConditions.alertIsPresent());
    		Alert alert = webdriver.switchTo().alert();
    		switch(sBtnText){
    		
		    		case "Accept" :   alert.accept();  
					break;
		    		
		    		case "Dismiss" :  alert.dismiss(); 
					break;
		    		
		    		default : alert.accept();
    		}
    	} catch(Exception  ex){
    		throw ex;
    	}
	}
	
	
	/**
	 * Wait for page to load.
	 *
	 * @name waitForPageToLoad
	 * @description Waits for the page to load
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param DEFAULT_WAIT_TIME ||description: Wait timeout in seconds
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	public static void waitForPageToLoad(int DEFAULT_WAIT_TIME)
	{
		long entryTime = System.currentTimeMillis();
		//Utils.waitForMSeconds(3000);
		try{
			new FluentWait<WebDriver>(webdriver)
	        .withTimeout(DEFAULT_WAIT_TIME,TimeUnit.MILLISECONDS)
	        .pollingEvery(1, TimeUnit.SECONDS)
	        .until(new ExpectedCondition<Boolean>(){	
				@Override
				public Boolean apply(WebDriver driver) 
	            {
	            	try
	            	{
	                   	JavascriptExecutor js = (JavascriptExecutor) driver;
												
	                   	if (js.executeScript("return document.readyState").equals("complete")) 
	                   	{
	                   		if ((boolean) js.executeScript("return window.jQuery==undefined"))
	                   		{
	                   			return true;	
		    				} 
	                   		else if ((boolean) js.executeScript("return jQuery.active==0"))
	                   		{
	                   			return true;	
		    				}
	                   		else {
	                   			Utils.waitForMSeconds(1000);
		    					return false;	
	                   		}
	    				}
	            	}
	            	catch(Exception ex)
	            	{
	            			Utils.waitForMSeconds(3000);
	            	} 
	            	return false;
	            }
			});
		
		} catch(Exception e) {
			
			//System.out.println("Exception occured in waitForPageLoad, retrying again!"+e.getLocalizedMessage());
    		Utils.waitForMSeconds(3000);
    		if(DEFAULT_WAIT_TIME>3000) {
    			waitForPageToLoad(DEFAULT_WAIT_TIME-(int)(System.currentTimeMillis()-entryTime));
    		}
		}
	}
	

	/**
	 * Select child window element.
	 *
	 * @name selectChildWindowElement
	 * @description Switches to the child window and perform operations
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param element ||description: WebElement to be clicked
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId 
	 */
	public static void selectChildWindowElement(WebElement element)
	{
		try{
			for(String windowHandle  : webdriver.getWindowHandles())
			{
			webdriver.switchTo().window(windowHandle);
			}
			element.click();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	

	/**
	 * Switch to child window.
	 *
	 * @name switchToChildWindow
	 * @description Switches to the last child window
	 * @author  Ramakrishna Doradla Venkatesh
	 * @return void	||description:
	 * @jiraId 
	 */
	public static void switchToChildWindow()
	{
		try{
			for(String winHandle : webdriver.getWindowHandles())
			webdriver.switchTo().window(winHandle);
		}catch(Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * Opens the web page.
	 *
	 * @name openWebPage
	 * @description Clears the browser cookies and then opens the given URL
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param sURL
	 *            ||description: URL of the Application ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void openWebPage(String sURL) {
		try {
			if(!sURL.isEmpty()) {
				logger.info("Opening the URL : " + sURL);
				driver.manage().deleteAllCookies();
				driver.get(sURL);
			} else
				throw(new IllegalArgumentException("String sURL is empty"));
		}catch(Exception e) {
			throw e;
		}
	}


	/**
	 * Press esc.
	 *
	 * @name pressEsc
	 * @description A method for Pressing Escape Keyboard Key.
	 * @author PeelaDha
	 * @return void ||description:
	 * @jiraId
	 */
	public void pressEsc() {
		try {
		((JavascriptExecutor) webdriver).executeScript("window.focus();");
		(new Thread() {
			public void run() {
				try {
					waitForPageToLoad(10);
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}).start();
		}catch(Exception e) {
			if (e.getMessage().toLowerCase().contains("javascript error"))
	   		{
				new SkipException("JavaScriptExecutor's exception thrown by pressEsc() in BasePage");
				logger.info(e.getMessage());
	   		} else
	   			throw e;
		}
	}
	
	/**
	 * Select Drop Down By Visible Text.
	 *
	 * @name selectDropDownByVisibleText
	 * @description This method is used to select dropdown by visible text
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param element
	 *            ||description: WebElement ||allowedRange:
	 * @param value
	 *            ||description: Select option ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void selectDropDownByVisibleText(WebElement element, String value) {
		try{
			if(element==null)
				throw(new IllegalArgumentException("WebElement is null"));
			else if(value.isEmpty())
				throw(new IllegalArgumentException("String value to select is empty"));
			else{
				Select dropdown = new Select(element);
				dropdown.selectByVisibleText(value);
			} 
		}catch(Exception e) {
			throw e;
		}
	}
	
	
	
	/**
	 * Checks for attribute.
	 *
	 * @name hasAttribute
	 * @description Check for the attribute value is not null
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param element ||description: WebElement
	 * ||allowedRange:
	 * @param attrib ||description: Attribute value to look up
	 * ||allowedRange:
	 * @return boolean	||description:
	 * @jiraId 
	 */
	public boolean hasAttribute(WebElement element, String attrib) {
		boolean flag = false;
		try {
			if(element==null)
				throw(new IllegalArgumentException("WebElement is null"));
			else if(attrib.isEmpty())
				throw(new IllegalArgumentException("Attribute to look is empty"));
			else {
				flag = (element.getAttribute(attrib) != null);
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	
	/**
	 * Click visiblebutton.
	 *
	 * @name clickVisibleButton
	 * @description Clicks on the button which is visible
	 * @author  Ramakrishna Doradla Venkatesh
	 * @param list
	 *            ||description:Passes list of all elements ||allowedRange:
	 * @return void ||description:
	 * @jiraId
	 */
	public void clickVisibleButton(List<WebElement> list) {
	try {
		int count = 0;
		if(list.isEmpty())
			throw(new IllegalArgumentException("WebElement list is empty"));
		for (WebElement element : list) {
			try {
				if (element.isDisplayed())
					{
					((JavascriptExecutor) webdriver).executeScript("arguments[0].click();", element);
					}
				count++;
				} catch (Exception e) {
					if (e.getMessage().toLowerCase().contains("javascript error"))
			   		{	
						new SkipException("JavaScriptExecutor's exception thrown by clickVisibileButton(List<WebElement> list) in BasePage");
						loggerManager.logger.info(e.getMessage());
			   		} else
					logger.info("WebElement at index: "+count+" is not visible");
		    	}
			}
		}catch(Exception e) {
			throw e;
		}
	}
	public void clickOutside() {
        Actions action = new Actions(driver);
        action.moveByOffset(0, 0).click().build().perform();
    }
}