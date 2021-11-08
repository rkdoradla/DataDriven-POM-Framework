package com.oce.nuxeo.steps;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.base.configuration.ConfigUtil;
import com.base.lib.Excel;
import com.base.pageframework.BaseSteps;
import com.base.pageframework.BaseTest;
import com.oce.nuxeo.pages.NewContentTypePage;

public class NewContentType extends BaseSteps {

	NewContentTypePage objNewContentTypePage;

	public NewContentType() {
		objNewContentTypePage = new NewContentTypePage();
	}

	public void fillCoreDetails(LinkedHashMap<String, String> DataSet) throws InterruptedException {

		driver.findElement(objNewContentTypePage.buttonClearOwner(DataSet.get("Owner"))).click();
		objNewContentTypePage.owner_TextBox.sendKeys(DataSet.get("Owner"));
		Thread.sleep(500);
		driver.findElement(objNewContentTypePage.dropDownItemOwner(DataSet.get("Owner"))).click();
		objNewContentTypePage.title_TextBox.sendKeys(DataSet.get("Title"));
		objNewContentTypePage.country_TextBox.sendKeys(DataSet.get("Country"));
		Thread.sleep(1000);
		driver.findElement(objNewContentTypePage.dropDownItemCountry(DataSet.get("Country"))).click();

		objNewContentTypePage.therapeuticArea_DropDown.click();
		objNewContentTypePage.therapeuticSearchBox.sendKeys(DataSet.get("Therapeutic Area"));
		driver.findElement(objNewContentTypePage.checkBoxTearapeuticItem(DataSet.get("Therapeutic Area"))).click();
		// driver.findElement(objNewContentTypePage.disease_DropDown1).sendKeys(Keys.TAB);
		// objNewContentTypePage.category_DropDown.click();
		Thread.sleep(2000);
		/* driver.findElement(By.xpath("//*[@class = 'backdrop']")).click(); */
		driver.findElement(objNewContentTypePage.backDropElement).click();
		// waitForElementClickable(objNewContentTypePage.therapeuticArea_DropDown,
		// Integer.parseInt(ConfigUtil.config.get("env.base.timeout")));
		// javaScriptClick(objNewContentTypePage.therapeuticArea_DropDown);
		// objNewContentTypePage.therapeuticArea_DropDown.sendKeys(Keys.SPACE);
		// scrollTo(objNewContentTypePage.therapeuticArea_DropDown);

		/*
		 * boolean invisiable = wait.until(ExpectedConditions
		 * .invisibilityOfElementLocated(By.xpath("//div[@class='navbar-brand']")));
		 * 
		 * js.executeScript("$x(\"(//input[@value='Google Search'])[2]\").click()");
		 */
		// mouseClick(objNewContentTypePage.category_DropDown);
		// objNewContentTypePage.textContentType.click();
		// javaScriptClick(objNewContentTypePage.therapeuticArea_DropDown);
		// mouseClick(objNewContentTypePage.therapeuticArea_DropDown);
		// driver.findElement(By.xpath("//body")).click();
		// clickOutside();

		scrollTo(objNewContentTypePage.disease_DropDown);
		Thread.sleep(1000);
		objNewContentTypePage.disease_DropDown.click();
		objNewContentTypePage.diseaseSearchBox.sendKeys(DataSet.get("TA Disease / Condition"));
		Thread.sleep(1000);
		driver.findElement(objNewContentTypePage.checkBoxDiseaseItem(DataSet.get("TA Disease / Condition"))).click();
		Thread.sleep(2000);
		driver.findElement(objNewContentTypePage.backDropElement).click();

		Thread.sleep(2000);
		objNewContentTypePage.product_DropDown.click();
		// bug
		// objNewContentTypePage.productSearchBox.sendKeys(DataSet.get("Product"));
		
		String productData = DataSet.get("Product");
		String products[] = productData.split(",");
		for(int i=0;i<products.length;i++) {
			driver.findElement(objNewContentTypePage.checkBoxProductItem(products[i])).click();
			Thread.sleep(2000);
		}
		driver.findElement(objNewContentTypePage.backDropElement).click();

		Thread.sleep(2000);
//		objNewContentTypePage.brand_DropDown.click();
//		objNewContentTypePage.brandSearchBox.sendKeys(DataSet.get("Brand"));
//		driver.findElement(objNewContentTypePage.checkBoxBrandItem(DataSet.get("Brand"))).click();
//		Thread.sleep(2000);
//		//objNewContentTypePage.textContentType.click();
//		//javaScriptClick(objNewContentTypePage.brand_DropDown);
//		mouseClick(objNewContentTypePage.brand_DropDown);

		Thread.sleep(2000);
		objNewContentTypePage.category_DropDown.click();
		Thread.sleep(2000);
		// objNewContentTypePage.categorySearchBox.sendKeys(DataSet.get("Category"));
		driver.findElement(objNewContentTypePage.checkBoxCategoryItem(DataSet.get("Category"))).click();
		Thread.sleep(1000);

//		driver.findElement(objNewContentTypePage.checkBoxContentType(DataSet.get("Content Type"))).click();
//		Thread.sleep(1000);	

		objNewContentTypePage.targetAudience_TextBox.click();
		// objNewContentTypePage.targetAudience_TextBox.sendKeys(DataSet.get("Target
		// Audience"));
		driver.findElement(objNewContentTypePage.dropDownItemTargetAudience(DataSet.get("Target Audience"))).click();

		Thread.sleep(2000);
		objNewContentTypePage.next_Button.click();

		Thread.sleep(5000);
	}

	public void fillPlanDetails(LinkedHashMap<String, String> DataSet) throws InterruptedException {
		objNewContentTypePage.agentType_DropDown.click();
		driver.findElement(objNewContentTypePage.dropDownItemAgentType(DataSet.get("Agency Type"))).click();

		objNewContentTypePage.budgetCost_TextBox.sendKeys(DataSet.get("Budget Cost"));
		objNewContentTypePage.actualCost_TextBox.sendKeys(DataSet.get("Actual Cost"));
		objNewContentTypePage.plannedStartDate_TextBox.sendKeys(DataSet.get("Planned Start Date"));
		objNewContentTypePage.plannedEndDate_TextBox.sendKeys(DataSet.get("Planned End Date"));
		Thread.sleep(5000);
		objNewContentTypePage.plannedEndDate_TextBox.sendKeys(Keys.TAB);
		objNewContentTypePage.plannedEndDateCalender_Btn.sendKeys(Keys.TAB);
		uploadFile();
		Thread.sleep(2000);
		waitForElementClickable(objNewContentTypePage.doneBtn,
				Integer.parseInt(ConfigUtil.config.get("env.base.timeout")));
		objNewContentTypePage.doneBtn.click();
		Thread.sleep(2000);
		objNewContentTypePage.save_Button.click();
	}

	public void uploadFile() {
		try {
			//Thread.sleep(200);
			// objNewContentTypePage.uploadBtn.click();
			// scrollTo(objNewContentTypePage.uploadBtn);
//			mouseClick(objNewContentTypePage.uploadBtn);
//			javaScriptClick(objNewContentTypePage.uploadBtn);
			Thread.sleep(2000);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);             
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(3000);
			String filePath = "C:\\Users\\u1102238\\eclipse-workspace\\oce-demo\\AutoIT\\uploadFiles\\test.txt";
			//String filePath = "C:\\Users\\u1102238\\eclipse-workspace\\oce-demo\\AutoIT\\uploadFiles\\Framework Proposed New.pdf";
			StringSelection stringSelection = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(500);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void verifyContentSubmission(LinkedHashMap<String, String> DataSet) throws IOException {
		waitForElementVisible(objNewContentTypePage.confirmMsg_Text, DEFAULT_TIMEOUT);
		String confirmMsgText = objNewContentTypePage.confirmMsg_Text.getText();
		System.out.println(confirmMsgText);
		assertTrue(confirmMsgText.contains("Success"), DataSet.get("ConfirmMsgErrorText"));
		BaseTest.logMsgWithScreenShot("content created successfully");
		Excel.writeData(ConfigUtil.config.get("env.testdata.location") + "\\regressionTestData\\ContentCentral_TestData.xlsx",
				"NewContent", confirmMsgText);
	}

	public void verifyNavigatingToNewContentPage() {
		waitForElementClickable(objNewContentTypePage.textContentType,
				Integer.parseInt(ConfigUtil.config.get("env.base.timeout")));
		assertTrue(objNewContentTypePage.textContentType.getText().contains("CONTENT"), "Header Validation Failed");
	}

}
