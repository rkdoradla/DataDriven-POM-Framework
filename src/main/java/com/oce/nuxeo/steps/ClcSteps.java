package com.oce.nuxeo.steps;


import com.base.pageframework.BaseSteps;
import com.oce.nuxeo.pages.CalculatorPage;


/**
 * @author Ramakrishna Doradla Venkatesh
 */

public class ClcSteps extends BaseSteps {

    private CalculatorPage calculatorPage;

    public void openCalculator(String url) {
        logger.info("Opening " + url);
        calculatorPage = new CalculatorPage(url);
        
        //scrollToBottom();
        
    }
    
    public void handleUnwantedPopUP() {
    	logger.info("Closing the popup");
    	try {
        calculatorPage.handlePopUP();
    	}catch(Exception e) {}
    }
    
    public void performAddition(String summand1, String summand2) {
    	logger.info("Adding " + summand2 + " to " + summand1);
        
        for (char digit : summand1.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.plusButton.click();
        for (char digit : summand2.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.equalButton.click();
        
    }

    public void performSubtraction(String minuend, String subtrahend) {
    	logger.info("Subtracting " + subtrahend + " from " + minuend);
        for (char digit : minuend.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.minusButton.click();
        for (char digit : subtrahend.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.equalButton.click();
        calculatorPage.equalButton.click();
    }

    public void performMultiplication(String multiplicand, String multiplier) {
    	logger.info("Multiplying " + multiplicand + " and " + multiplier);
        for (char digit : multiplicand.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.multiplyButton.click();
        for (char digit : multiplier.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.equalButton.click();
    }

    public void performDivision(String dividend, String divisor) {
    	logger.info("Dividing " + dividend + " on " + divisor);
        for (char digit : dividend.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.divideButton.click();
        for (char digit : divisor.toCharArray()) {
        	calculatorPage.pressNumber(digit);
        }
        calculatorPage.equalButton.click();
    }

    public String getCalculationResult() {
    	logger.info("Fetching the result");
        return calculatorPage.getResultValue().trim();
    }

    public void additionVerify(String expected) {
    	 assertEquals(getCalculationResult(), expected,"Addition result is wrong");
    
    }
    public void subtractionVerify(String expected) {
     assertEquals(getCalculationResult(), expected,"Subtraction result is wrong");	
    	
    }
    public void multiplyVerify(String expected) {
    	assertEquals(getCalculationResult(), expected,"Multiplication result is wrong");
    }
    public void divisionVerify(String expected) {
    	assertEquals(getCalculationResult(), expected,"Division result is wrong");
    }
    
}
