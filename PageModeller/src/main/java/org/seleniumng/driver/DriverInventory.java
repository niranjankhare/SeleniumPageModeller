/*******************************************************************************
 * Copyright 2018 Niranjan Khare
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.seleniumng.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.seleniumng.utils.TAFConfig;

import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

/**
 * Class to manage webdriver sessions.. for now based only on thread but would
 * need it to manage multiple webdriver sessions for a single thread
 * 
 * @author niru
 *
 */
public class DriverInventory {
	private static Config gridHub = (TAFConfig.tafConfig.hasPath("seleniumGrid")) ? TAFConfig.getConfig("seleniumGrid")
			: null;
	private static Boolean useGrid = (gridHub == null) ? false
			: (gridHub.hasPath("enabled")) ? gridHub.getBoolean("enabled") : false;
	private static String browserName = (TAFConfig.tafConfig.hasPath("browser"))
			? TAFConfig.tafConfig.getString("browser") : "firefox";
	private static Config capabilities = (TAFConfig.tafConfig.hasPath("capabilities"))
			? TAFConfig.tafConfig.getConfig("capabilities") : null;
	private static LinkedHashMap<String, WebDriver> driverInventory = new LinkedHashMap<String, WebDriver>();

	/**
	 * @param webDriver 
	 * 				registers a new webdriver instance in the inventory HashMap
	 * @return
	 * 			the key for the webDriver entry
	 *  Note: to get a WebDriver instance, use getDriver for the Session 
	 */
	public static String registerDriver(WebDriver webDriver) {
		String key = ((RemoteWebDriver) webDriver).getSessionId().toString();
		driverInventory.put(key, webDriver);
		return key;
	}

	/**
	 * @return
	 *        the key for the webDriver entry
	 */ 
	public static String getNewDriver() {
		System.setProperty("webdriver.chrome.driver", TAFConfig.sysPropChromeDriverPath);
		System.setProperty("webdriver.gecko.driver", TAFConfig.sysPropMozGeckoDriverPath);
		return setupWebDriver(capabilities);
	}


	/**
	 * @param key the key for the WebDriver session to close 
	 */
	public static void closeSession(String key) {
		System.out.println("Closing Session:" + key);
		driverInventory.get(key).close();
		driverInventory.remove(key);
	}

	/**
	 * @param session the key representing the session
	 * @return the WebDriver instance identified by the session key
	 */
	public static WebDriver getDriver(String session) {
		return driverInventory.get(session);
	}
	
	/**
	 * Sets up the browser to be used for running automation
	 * 
	 * @param Config
	 *             The typesafe config object defining required capabilities 
   *	           required for the webDriver session
	 */
	private static String setupWebDriver (Config desiredCapabilities){
		MutableCapabilities capabilities = new MutableCapabilities();
		
		for (Entry<String, ConfigValue> e: desiredCapabilities.entrySet()){
			capabilities.setCapability(e.getKey(), e.getValue().unwrapped());
		}
		capabilities.setCapability("browserName", browserName);
		RemoteWebDriver remoteWebDriver = null;
		WebDriver driver = null;
		if (gridHub != null && useGrid) {
			String remoteDriverUrlString;
			if (!gridHub.getString("port").equals("")) {
				remoteDriverUrlString = "http://" + gridHub.getString("host") + ":" + gridHub.getString("port")
						+ "/wd/hub";
			} else {
				remoteDriverUrlString = "http://" + gridHub.getString("host") + "/wd/hub";
			}
			URL remoteDriverUrl = null;
			try {
				remoteDriverUrl = new URL(remoteDriverUrlString);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			remoteWebDriver = new RemoteWebDriver(remoteDriverUrl, capabilities);
			remoteWebDriver.setFileDetector(new LocalFileDetector());
			driver = (WebDriver)remoteWebDriver;
		} else {
			switch (browserName){
			case "firefox":
				FirefoxOptions o= new FirefoxOptions(capabilities);
				driver = new FirefoxDriver(o);
				break;
			case "chrome":
			case "googlechrome":
				ChromeOptions c = new ChromeOptions();
				for (Entry<String, ConfigValue> e: desiredCapabilities.entrySet()){
					c.setCapability(e.getKey(), e.getValue().unwrapped());
				}
				driver = new ChromeDriver(c);
				break;
			case "ie":
				break;
			case "safari":
				break;
			
			}
		}
		driver.manage().timeouts().implicitlyWait(TAFConfig.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
		Dimension d = new Dimension(1280, 1024);
		// Resize the current window to the given dimension
		driver.manage().window().setSize(d);
		return registerDriver(driver);
	}
}
