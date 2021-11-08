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

public class ClcOperationsTest2 extends BaseTest {
    ClcSteps clc;
    String url; 

    @BeforeMethod() 
    public void setUp() {
        url = ConfigUtil.config.get("env.base.url");
        clc = new ClcSteps();
        loggerManager.logger.info("Reading the excel test data sheet");
     	clc.openCalculator(url);
 		//clc.handleUnwantedPopUP();
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
  	
	  	System.out.println("test completed");
     }

   
}