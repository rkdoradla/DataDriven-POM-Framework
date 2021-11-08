package com.oce.nuxeo.regression;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class MycontactformFileUpload {

	@Test
	public void testt() throws InterruptedException, IOException {
		
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver","C:\\Users\\u1102238\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();

		driver.get("https://www.mycontactform.com/samples.php");
		driver.manage().window().maximize();
		
		WebElement ele = driver.findElement(By.id("attach4589"));
		new Actions(driver).moveToElement(ele).click().perform();
		
		Thread.sleep(5000);
		
		//Process p = Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\AutoIT\\executable\\MycontactForm_FileUpload.exe");
		Process p = Runtime.getRuntime().exec("C:\\Users\\u1102238\\Downloads\\MycontactForm_FileUpload.exe");
			
		p.waitFor();
		if (p.exitValue()==1) {
		    System.out.println("YAY!");
		}
		else {
		    System.out.println("boo");
		}

		Thread.sleep(10000);
		
	}
}
