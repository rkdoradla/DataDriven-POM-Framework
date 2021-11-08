package com.oce.nuxeo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.base.pageframework.BasePage;

public class NewContentTypePage extends BasePage{

	/*Core Details*/
	
	public By textContentType1 = By.xpath("//span[contains(text(),'CONTENT TYPE')");
	//public By owner_TextBox = By.xpath("//*[@id=\"brandBand_2\"]//label/input)[1]");
	//public By title_TextBox = By.xpath("//input[@id='input-114']");
	//public By contentPlan_TextBox = By.xpath("//*[@id=\"brandBand_2\"]//label/input)[3]");
	//public By country_TextBox = By.xpath("(//*[@id=\"brandBand_2\"]//label/input)[2]");
	//public By therapeuticArea_DropDown1 = By.xpath("(//input[contains(@id,'combobox-id-1')])[1]");
	//public By disease_DropDown1 = By.xpath("(//input[contains(@id,'combobox-id-1')])[2]");
	public By backDropElement = By.xpath("//*[@class = 'backdrop']");
	public By therapeuticSearchBox1 = By.xpath("//input[contains(@placeholder,'Start typing')])[1]");
	//public By condition_DropDown = By.id("combobox-id-1-99");
	//public By product_DropDown = By.id("combobox-id-1-104");
	//public By brand_DropDown = By.id("combobox-id-1-109");
	//public By category_DropDown = By.id("combobox-id-1-89");
	//public By contentType_DropDown = By.xpath("//*[@id=\"brandBand_2\"]//div[@class='backdrop']");
	//public By targetAudience_TextBox = By.id("combobox-id-1-57");
	//public By next_Button = By.xpath("//button[contains(text(),'Next')]");
	/*Plan Details*/
		
	//	public By agentType_DropDown = By.id("input-357");
	//	public By budgetType_TextBox  = By.id("input-360");
	//	public By actualCost_TextBox  = By.id("input-361");
	//	public By plannedStartDate_TextBox  = By.id("input-336");
	//	public By plannedEndDate_TextBox  = By.id("input-342");
	//	public By uploadFile_Button = By.xpath("//*[@id=\"file-selector-label-348\"]/span[1]");
	//	public By save_Button = By.xpath("//button[contains(text(),'Save')]");
	//	public By confirmMsg_Text = By.xpath("//*[contains(@class , 'toastContainer')]/div//*[contains(text(),'Success')]");
	
	public NewContentTypePage() {
		//	public LoginPage(String url) {
	    	//webdriver.get(url);
	    	PageFactory.initElements(webdriver, this);
	}
	
		
	@FindBy (xpath = "//span[contains(text(),'CONTENT TYPE')]")
	public WebElement textContentType;
	
	public By buttonClearOwner(String ownerName) {
		return By.xpath("(//button[contains(@data-label , '"+ownerName+"')])[1]");
	}
	
	@FindBy(xpath = "(//*[@id='brandBand_2']//label/input)[1]")
    public WebElement owner_TextBox;
	
	public By dropDownItemOwner(String owner) {
		return By.xpath("//li[contains(@data-label , '"+owner+"')]");
	}
	
	
	@FindBy(xpath = "(//input[contains(@name,'Title__c')])[1]")
    public WebElement title_TextBox;
	
	@FindBy(xpath = "(//*[@id='brandBand_2']//label/input)[3]")
    public WebElement contentPlan_TextBox;
	
	@FindBy(xpath = "(//*[@id='brandBand_2']//label/input)[2]")
    public WebElement country_TextBox;
	
	public By dropDownItemCountry(String countryName) {
		return By.xpath("//li[contains(@data-label , '"+countryName+"')]");
	}
	
	@FindBy(xpath = "(//input[contains(@id,'combobox-id-1')])[1]")
    public WebElement therapeuticArea_DropDown;
	
	@FindBy(xpath = "//div[@class='slds-combobox slds-dropdown-trigger slds-dropdown-trigger_click slds-is-open']/child::div[1]")
    public WebElement therapeuticArea_DropDownClick;
	
	
	@FindBy(xpath = "(//input[contains(@placeholder,'Start typing')])[1]")
	public WebElement therapeuticSearchBox;
	
	public By checkBoxTearapeuticItem(String itemName) {
		return By.xpath("//span[contains(text(), '"+itemName+"')]");
	}
	
	public By clickCheckBoxTearapeuticItem(String itemName) {
		return By.xpath("//span[contains(text(), '"+itemName+"')]");
	}
	
	@FindBy(xpath = "(//input[contains(@id,'combobox-id-1')])[2]")
    public WebElement disease_DropDown;
	
	@FindBy(xpath = "(//input[contains(@placeholder,'Start typing')])[2]")
	public WebElement diseaseSearchBox;
	
	public By checkBoxDiseaseItem(String itemName) {
		return By.xpath("//span[contains(text(), '"+itemName+"')]");
	}
	
	@FindBy(xpath = "(//input[contains(@id,'combobox-id-1')])[3]")
    public WebElement product_DropDown;
	
	@FindBy(xpath = "(//input[contains(@placeholder,'Start typing')])[3]")
	public WebElement productSearchBox;
	
	public By checkBoxProductItem(String itemName) {
		return By.xpath("//span[contains(text(), '"+itemName+"')]");
	}
	
	@FindBy(xpath = "(//input[contains(@id,'combobox-id-1')])[4]")
    public WebElement brand_DropDown;
	
	@FindBy(xpath = "(//input[contains(@placeholder,'Start typing')])[4]")
	public WebElement brandSearchBox;
	
	public By checkBoxBrandItem(String itemName) {
		return By.xpath("//span[contains(text(), '"+itemName+"')]");
	}
	
	@FindBy(xpath = "(//input[contains(@id,'combobox-id-1')])[5]")
    public WebElement category_DropDown;
	
	@FindBy(xpath = "(//input[contains(@placeholder,'Start typing')])[5]")
	public WebElement categorySearchBox;
	
	public By checkBoxCategoryItem(String itemName) {
		return By.xpath("//span[contains(text(), '"+itemName+"')]");
	}
	
	public By checkBoxContentType(String itemName) {
		return By.xpath("(//span[contains(text(), '"+itemName+"')])[2]");
	}

	@FindBy(xpath = "//label[contains(text(),'Target Audience')]/following-sibling::div//input")
    public WebElement targetAudience_TextBox;
		
	public By dropDownItemTargetAudience(String item) {
		return By.xpath("//li[contains(@data-label , '"+item+"')]");
	}
	
	
	//
	@FindBy(xpath = "//*[@id='brandBand_2']//div[@class='backdrop']9")
    public WebElement contentType_DropDown;
	
	
	@FindBy(xpath = "//button[contains(text(),'Next')]")
    public WebElement next_Button;
	
	@FindBy(xpath = "(//label[contains(text(),'Agency Type')]/following-sibling::div//input)[1]")
    public WebElement agentType_DropDown;
	
	public By dropDownItemAgentType(String item) {
		return By.xpath("(//*[contains(text(),'"+item+"')])[1]");
	}
	
	@FindBy(xpath = "(//input[@name = 'Budget_Cost__c'])[1]")
    public WebElement budgetCost_TextBox;
	
	@FindBy(xpath = "(//input[@name = 'Actual_Cost__c'])[1]")
    public WebElement actualCost_TextBox;
	
	@FindBy(xpath = "(//label[contains(text(),'Planned Start Date><')]/following-sibling::div//input)[1]")
    public WebElement plannedStartDate_TextBox;
	
	@FindBy(xpath = "(//label[contains(text(),'Planned End Date><')]/following-sibling::div//input)[1]")
    public WebElement plannedEndDate_TextBox;
	
	@FindBy(xpath = "(//*[@title = 'Select a date'])[2]")
    public WebElement plannedEndDateCalender_Btn;
	
	
	
	@FindBy(xpath = "//*[@id='file-selector-label-348']/span[1]")
    public WebElement uploadFile_Button;
	
	@FindBy(xpath = "(//button[contains(text(),'Save')])[1]")
    public WebElement save_Button;
	
	@FindBy(xpath = "//*[contains(@class , 'toastContainer')]/div//*[contains(text(),'Success')]")
    public WebElement confirmMsg_Text;
	
	@FindBy(xpath = "//span[contains(@class, 'slds-file-selector__button')]")
	public WebElement uploadBtn;
	
//	@FindBy(xpath = "//input[contains(@id , 'input-file')]")
//	public WebElement uploadBtn;
//	
//	@FindBy(xpath = "//*[text() = 'Upload Files']")
//	public WebElement uploadBtn;
	
	@FindBy(xpath = "//span[text() = 'Done']")
	public WebElement doneBtn;
}
