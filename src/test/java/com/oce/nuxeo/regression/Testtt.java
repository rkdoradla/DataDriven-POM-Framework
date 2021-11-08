package com.oce.nuxeo.regression;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Testtt {
	

	@Test
	public void testt() throws InterruptedException, IOException {
		
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver","C:\\Users\\u1102238\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();

		driver.get("http://3.129.73.164:8080/nuxeo/ui/");

		driver.manage().window().maximize();

	    driver.findElement(By.id("username")).sendKeys("rama");
	    driver.findElement(By.id("password")).sendKeys("Welcome1");

	    driver.findElement(By.name("Submit")).click();
	 //   driver.findElement(By.cssSelector("nuxeo-app")).click();

	  //.switchTo().frame("parent");
	    
	    List<WebElement> list = driver.findElements(By.tagName("iframe"));
	    
	    System.out.println(list.size());
		
		
		
	}

}
