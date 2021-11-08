package com.base.configuration;

import org.testng.ITestNGMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

/**
* Reads the Config properties file or gets the command line parameter arguments and creates a HashMap called 'config' made it available public.
* This config HashMap holds all environment properties.
*
* @author  Ramakrishna Doradla Venkatesh
* @version 
* @since   
*/

public class ConfigUtil {

    private static final String PATH_TO_CONFIGURATION_KEY = "config/config.properties";

    private static String browser = "IE";
    private static String application = "";
    private static String baseUrl = "";
    private static String runType = "Debug";
    private static String release = "";
    private static String build = "";
    private static String logName = "Log.txt";
    private static String baseLogFileFolder = ".\\Logs";
    private static String logLevel = "INFO";
    private static String logInConsole = "false";
    private static String timeout = "60000";
    private static String primaryColumnName = "";
    private static String verifyStop = "false";
    private static String user = "";
    private static String keyLocation = "";
    private static ITestNGMethod[] allTestMethods;
    private static String environment = "";
    
    
    public static HashMap<String,String> config;
    private static Properties configurationfromfile;

	


    /**
     * Config util.
     */
    private ConfigUtil() {
    }
    
    /**
     * Get new instance.
     *
     * @name getNewInstance
     * @description Getter for an instance of the ConfigUtil class
     * @author Ramakrishna Doradla Venkatesh
     * @return ConfigUtil	||description:
     * @jiraId 
     */
    public static ConfigUtil getNewInstance() {
    	ConfigUtil instance = new ConfigUtil();
        return instance;
    }

    /**
     * Static Initializer block 
     */
    static {
    	builddefaultConfiguration();
    	loadConfigurationfile();
    	updateConfig();
    	checkConfigData();
    }

    /**
     * Builddefault configuration.
     *
     * @name builddefaultConfiguration
     * @description Updates HashMap<String,String> config with the Default values
     * @author Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @jiraId 
     */
    private static void builddefaultConfiguration(){
    	try{config = new HashMap<String,String>();
	    	config.put("env.browser",browser);
	    	config.put("env.application",application);
	    	config.put("env.base.url",baseUrl);
	    	config.put("env.run.type",runType);
	    	config.put("env.release",release);
	    	config.put("env.build",build);
	    	config.put("env.log.name",logName);
	    	config.put("env.base.log.folder",baseLogFileFolder);
	    	config.put("env.log.level",logLevel);
	    	config.put("env.login.console",logInConsole);
	       	config.put("env.base.timeout",timeout);
	    	config.put("env.data.primarycolumn",primaryColumnName);
	    	config.put("env.verify.stop",verifyStop);
	    	config.put("env.user",user);
	    	config.put("env.key.location",keyLocation);
	    	config.put("env.environment",environment);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Load configurationfile.
     *
     * @name loadConfigurationfile
     * @description Reads the config.properties file into a Properties variable
     * @author Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @jiraId 
     */
    private static void loadConfigurationfile() {
    	configurationfromfile = new Properties();
        FileInputStream input;
        try {
            input = new FileInputStream(PATH_TO_CONFIGURATION_KEY);
            configurationfromfile.load(input);
            input.close();
        } catch (FileNotFoundException e) {
        	System.out.println("Config file not found at "+PATH_TO_CONFIGURATION_KEY +" , continuing with default settings.");
        	System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Update config.
     *
     * @name updateConfig
     * @description Updates HashMap<String,String> config with the Config properties values
     * @author Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @jiraId 
     */
    private static void updateConfig() {
    	try {
    	 	Properties sysProps = System.getProperties();
            Enumeration<?> eSysProps = sysProps.propertyNames();
        	Enumeration<?> eConfigfileProps = configurationfromfile.propertyNames();
        	
        	//Updating with config file properties
        	while(eConfigfileProps.hasMoreElements()){
        		String key = (String) eConfigfileProps.nextElement();
        		if(configurationfromfile.getProperty(key)!= null && !configurationfromfile.getProperty(key).trim().isEmpty()){
        			if(config.containsKey(key.toLowerCase().trim())) 
        				config.put(key.toLowerCase().trim(),configurationfromfile.getProperty(key));
        			else
        				config.put(key.trim(),configurationfromfile.getProperty(key));
        		}
        	}
        	
        	//Updating with command line execution properties
        	while(eSysProps.hasMoreElements()){
        		String key = (String) eSysProps.nextElement();
        		if(key.trim().startsWith("env.") && sysProps.getProperty(key)!= null && !sysProps.getProperty(key).trim().isEmpty()){
        			if(config.containsKey(key.toLowerCase().trim()))
        				config.put(key.toLowerCase().trim(),sysProps.getProperty(key));
        			else
        				config.put(key.trim(),sysProps.getProperty(key));
        		}
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    		System.exit(0);
    	}
    }
    
    /**
     * Check config data.
     *
     * @name checkConfigData
     * @description Validates mandatory configuration properties
     * @author Ramakrishna Doradla Venkatesh
     * @return void	||description:
     * @throws  
     * @jiraId 
     */
    private static void checkConfigData() {
    	
    	try{
    		for (Map.Entry<String, String> entry : config.entrySet()) {
        	    String key = entry.getKey();
        	    String value = entry.getValue().trim();
        	    
        	    switch(key.toLowerCase().trim()){
    	    	    case ("env.browser"):
    	    	    	checkBrowser(value);
    	    	    	break;
    	    	    case ("env.application"):
    	    	    	if(value.isEmpty() || value.length()<3){
    	    	    		throw new InvalidParameterException("Application name is either Empty or Short");
    	    	    	} else {
    	    	    		config.put(key,value.trim());
    	    	    	}
    	    			break;
    	    	    case ("env.base.url"):
    	    	    	if(value.isEmpty()){
    	    	    		throw new InvalidParameterException("Base url can't be empty");
    	    	    	} else {
    	    	    		config.put(key,value.trim());
    	    	    	}
    	    			break;
    	    	    case ("env.release"):
    	    	    	if(value.isEmpty()){
    	    	    		throw new InvalidParameterException("Release value can't be empty");
    	    	    	} else {
    	    	    		config.put(key,value.trim());
    	    	    	}
    	    			break;
    	    	    case ("env.build"):
    	    	    	if(value.isEmpty()){
    	    	    		throw new InvalidParameterException("Build value can't be empty");
    	    	    	} else {
    	    	    		config.put(key,value.trim());
    	    	    	}
    	    			break;
    	    	    case ("env.run.type"):
    	    	    	if(value.toLowerCase().equals("debug")){
    	    	      		config.put("env.base.log.folder",config.get("env.base.log.folder") +"\\Debug");
    	      	    	} else if(value.toLowerCase().equals("dryrun") || value.toLowerCase().equals("dry run")){
    	    	    		config.put("env.base.log.folder",config.get("env.base.log.folder") +"\\DryRun");
    	    	    	} else{
    	      	    		config.put("env.base.log.folder",config.get("env.base.log.folder") +"\\Automation");
    	    	    	}
    	    			break;

    	    	    default: 
    	    		    		config.put(key,value.trim());
     	    	    	break;
    	            }
        	}
    	}
    	catch(InvalidParameterException e){
    		e.printStackTrace();
    		System.exit(0);
    	}
    }
    
	/**
	 * Check browser.
	 *
	 * @name checkBrowser
	 * @description Checks validity of the browser property in the config.properties file
	 * @author Ramakrishna Doradla Venkatesh
	 * @param Value ||description: Browser value from Config file
	 * ||allowedRange: IE/Chrome/Firefox/Safari
	 * @return void	||description:
	 * @jiraId 
	 */
    private static void checkBrowser(String Value) {
		try{
			switch (Value.toLowerCase().trim()) {
	            case ("ie"):
	                config.put("env.browser","IE");
	                break;
	            case ("chrome"):
	            	config.put("env.browser","Chrome");
	            	break;
	            case ("firefox"):
	            	config.put("env.browser","Firefox");
	                break;
	            case ("safari"):
	            	config.put("env.browser","Safari");
	                break;
	            default: {
	                throw new InvalidParameterException("Invalid browser property was set or current browser is not supported, "+browser);
	            }
			}
		}
		catch(InvalidParameterException e){
			e.printStackTrace();
			System.exit(0);
        }
    }
    
    /**
     * Sets the all test methods.
     *
     * @name setAllTestMethods
     * @description Setter to set allTestMethods
     * @author Ramakrishna Doradla Venkatesh
     * @param methods ||description: Array of ITestNGMethod
     * ||allowedRange:
     * @return void	||description:
     * @jiraId 
     */
    public static void setAllTestMethods(ITestNGMethod[] methods) {
        allTestMethods = methods;
    }

    /**
     * Get all test methods.
     *
     * @name getAllTestMethods
     * @description Getter to get allTestMethods 
     * @author Ramakrishna Doradla Venkatesh
     * @return ITestNGMethod[]	||description: Array of ITestNGMethod
     * @jiraId 
     */
    public static ITestNGMethod[] getAllTestMethods() {
        return allTestMethods;
    } 
  
}