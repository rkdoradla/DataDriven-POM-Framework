package com.oce.nuxeo.regression;

import com.base.configuration.ConfigUtil;
import com.base.lib.Excel;
import com.base.pageframework.BaseTest;
import com.oce.nuxeo.steps.ClcSteps;
import com.relevantcodes.extentreports.LogStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Ramakrishna Doradla Venkatesh
 */

public class ClcOperationsTest extends BaseTest {
    ClcSteps clc;
    String url; 

    @BeforeMethod() 
    public void setUp() {
        url = ConfigUtil.config.get("env.base.url");
        clc = new ClcSteps();
        loggerManager.logger.info("Reading the excel test data sheet");
     	clc.openCalculator(url);
 		clc.handleUnwantedPopUP();
    }

   @DataProvider
   public Object[][] getData(Method method){
	   List<Object[]>  DataList = null;
	   Object[][] dataArr = {} ;

	   String methodName = method.getAnnotation(Test.class).testName();
	  
	   List<Object[]>  DataListCases = Excel.excelRead(".\\testdata\\regressionTestData\\clcdata.xlsx", "TestRun");
	   for(Object[] data : DataListCases) {
		   @SuppressWarnings("unchecked")
		LinkedHashMap<String, String> DataSet = (LinkedHashMap<String,String>) data[0];
 		   if( DataSet.get("TestCaseName").equalsIgnoreCase(methodName) && DataSet.get("RunMode").equalsIgnoreCase("YES")) {
 			  DataList = Excel.excelRead(".\\testdata\\regressionTestData\\clcdata.xlsx", "clcdemo");
 			
	 			for(int i=0;i<DataList.size();i++) {
	 				LinkedHashMap<String, String> tempMap = (LinkedHashMap<String,String>) DataList.get(i)[0];
	 	 			String testCaseName = tempMap.get("TestCaseName");
	 	 			if(!testCaseName.equalsIgnoreCase(methodName)) {
	 	 				DataList.remove(i);
	 	 			}
	 			}

 			 dataArr = new Object[DataList.size()][];
 			 dataArr=DataList.toArray(dataArr);
 		   }
// 	       }else {
// 			   System.out.println("Skipping the test case as RunMode is NO or Test case" +DataSet.get("TestCaseName")+"is not found in the data sheet");
// 		   }
	   } 
	   return dataArr;
   }
  @Test(enabled = true, dataProvider = "getData",description="Check addition; Coverage: ArithmeticOperations1", testName="additionTest")
		  
    public void additionTest(LinkedHashMap<String, String> DataSet) throws IOException{
  	
 			//@SuppressWarnings("unchecked")
			//LinkedHashMap<String, String> DataSet = (LinkedHashMap<String,String>) Data[0];
 			String firstNum = DataSet.get("FirstNumber");
 			String secondNum = DataSet.get("SecondNumber");
 			String addResult = DataSet.get("Addition");
 			test.log(LogStatus.PASS,  "enter numbers");
 			//test.log(null, "enter numbers");
 			clc.performAddition(firstNum, secondNum);
 			test.log(LogStatus.PASS,  "verify result");
 			//test.log(null, "verify result");
 			clc.additionVerify(addResult);
 
     }

   @Test(enabled = true, dataProvider = "getData",description="Check Subsraction; Coverage: ArithmeticOperations1", testName="substractionTest")
    public void substractionTest(LinkedHashMap<String, String> DataSet) throws IOException{
    	
 	 			String firstNum = DataSet.get("FirstNumber");
 	 			String secondNum = DataSet.get("SecondNumber");
 	 			
 	 			String minResult = DataSet.get("Subtraction");
 	 		
 	 			clc.performSubtraction(firstNum, secondNum);
 	 			clc.subtractionVerify(minResult);
     } 
     
/*
   // @Test(enabled = true, description="Check multiplication; Coverage: ArithmeticOperations2")
	    public void multiplicationTest() throws IOException{
 	 
 	  for (Object[] Data : DataList) {
	 			@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> DataSet = (LinkedHashMap<String,String>) Data[0];
	 			String firstNum = DataSet.get("FirstNumber");
	 			String secondNum = DataSet.get("SecondNumber");
	 			
	 			String multiplyResult = DataSet.get("Multiply"); 
	 			clc.performMultiplication(firstNum, secondNum);
			 	clc.multiplyVerify(multiplyResult);
	 		
	     }
     }
     
  // @Test(enabled = true, description="Check division; Coverage: ArithmeticOperations2")
	    public void divisionTest() throws IOException{
 	 
 	 for (Object[] Data : DataList) {
	 			@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> DataSet = (LinkedHashMap<String,String>) Data[0];
	 			String firstNum = DataSet.get("FirstNumber");
	 			String secondNum = DataSet.get("SecondNumber");
	 			
	 			String divideResult = DataSet.get("Division");
	 			clc.performDivision(firstNum, secondNum);
			 	clc.divisionVerify(divideResult);  
			 		 		
	     }
      }
     
  */
}