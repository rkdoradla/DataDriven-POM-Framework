package com.base.pageframework;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.testng.Assert;

import com.base.listeners.LogfileMethodListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;


/**
 * Class has methods to perform common functionality which could be easily in the test. 
 * All application's steps class should extend this class. 
 * 
 *  @author  Ramakrishna Doradla Venkatesh
 * 	@version 
 * 	@since  
 */

public class BaseSteps extends BasePage {
	
	public static final int DEFAULT_TIMEOUT = 30;
		
	/**
	 * Assert equals.
	 *
	 * @name assertEquals
	 * @description Asserts two strings values for equality, if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
	 * @author Ramakrishna Doradla Venkatesh
	 * @param actual ||description: The actual value to be compared
	 * ||allowedRange:
	 * @param expected ||description: The Expected value
	 * ||allowedRange:
	 * @param message ||description: On fail logging message 
	 * ||allowedRange:
	 * @return void	||description:
	 * @jiraId  
	 */
 
	public void  assertEquals(String actual, String expected, String message){
	
		try {
            Assert.assertEquals(actual, expected, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
         
         }
       catch (Error er){
        	LogfileMethodListener.verifyError(er);
        	throw er;
           	}
	    }

    /**
     * Assert equals.
     *
     * @name assertEquals
     * @description Asserts two int values for equality, if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param actual ||description: The actual value to be compared
     * ||allowedRange:
     * @param expected ||description: The Expected value
     * ||allowedRange:
     * @param message ||description: On fail logging message 
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertEquals(int actual, int expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        } catch (Error er) {
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }

    /**
     * Assert equals.
     *
     * @name assertEquals
     * @description Asserts two Objects for equality, if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param actual ||description: The actual value to be compared
     * ||allowedRange:
     * @param expected ||description: The Expected value
     * ||allowedRange:
     * @param message ||description: On fail logging message 
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        } catch (Error er) {
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }

    /**
     * Assert equals.
     *
     * @name assertEquals
     * @description Asserts two JsonNode values for equality, if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param actual ||description: The actual value to be compared
     * ||allowedRange:
     * @param expected ||description: The Expected value
     * ||allowedRange:
     * @param message ||description: On fail logging message 
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertEquals(JsonNode actual, JsonNode expected, String message) {
        try {
            Assert.assertTrue(actual.equals(expected), message);
            logger.debug("\"" + actual + "\" equals to \"" + expected + "\".");
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        } catch (Error er) {
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }

    /**
     * Assert not equals.
     *
     * @name assertNotEquals
     * @description Asserts two Objects for not equality, if value not matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param actual ||description: The actual value to be compared
     * ||allowedRange:
     * @param expected ||description: The Expected value
     * ||allowedRange:
     * @param message ||description: On fail logging message 
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertNotEquals(Object actual, Object expected, String message){
        try {
            Assert.assertNotEquals(actual, expected, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        }
        catch (Error er){
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }

    /**
     * Assert true.
     *
     * @name assertTrue
     * @description Asserts boolean value for "True", if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param actual ||description: The actual value
     * ||allowedRange:
     * @param message ||description: On fail logging message 
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertTrue(boolean actual, String message){
        try {
            Assert.assertTrue(actual, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        }
        catch (Error er){
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }

    /**
     * Assert not null.
     *
     * @name assertNotNull
     * @description Asserts an Object for not null, if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param objectNotNull ||description: The actual value
     * ||allowedRange:
     * @param message ||description: On fail logging message 
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertNotNull(Object objectNotNull, String message){
        try {
            Assert.assertNotNull(objectNotNull, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        }
        catch (Error er){
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }

    /**
     * Assert null.
     *
     * @name assertNull
     * @description Asserts an Object for null, if value matches logs Pass details 
	 * 				else LogfileMethodListener.verifyError(er) is called.
     * @author Ramakrishna Doradla Venkatesh 
     * @param objectNull ||description: The actual value
     * ||allowedRange:
     * @param message ||description: On fail logging message
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public void assertNull(Object objectNull, String message){
        try {
            Assert.assertNull(objectNull, message);
            logger.info("PASS: Function = " + Thread.currentThread().getStackTrace()[2].getMethodName());
        }
        catch (Error er){
        	LogfileMethodListener.verifyError(er);
        	throw er;
        }
    }
	
	
	
}
