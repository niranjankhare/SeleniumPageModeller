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

import com.typesafe.config.Config;

/**
 * Class to manage webdriver sessions.. for now based only on thread but would
 * need it to manage multiple webdriver sessions for a single thread
 * 
 * @author niru
 *
 */
public class DriverInventory {
	private static Config gridHub = TAFConfig.getConfig("seleniumGrid");
	private static LinkedHashMap<String, WebDriver> driverInventory = new LinkedHashMap<String, WebDriver>();

	public static String registerDriver(WebDriver webDriver) {
		String key = ((RemoteWebDriver) webDriver).getSessionId().toString();
		driverInventory.put(key, webDriver);
		return key;
	}
	//
	// public static String registerDriver(int threadInstance, RemoteWebDriver
	// remoteWebDriver) {
	// String key =
	// ((RemoteWebDriver)remoteWebDriver).getSessionId().toString();
	// driverInventory.put(key , remoteWebDriver);
	// return key;
	//
	// }

	public static String getNewDriver() {
		System.setProperty("webdriver.chrome.driver", TAFConfig.sysPropChromeDriverPath);
		System.setProperty("webdriver.gecko.driver", TAFConfig.sysPropMozGeckoDriverPath);
		return setupWebDriver(BrowserType.FIREFOX);
		
	}

	private static String getDriver() {
		// WebDriver driver = new ChromeDriver();
		return setupWebDriver(BrowserType.FIREFOX);// registerDriver(driver);
	}

	/**
	 * Sets up the browser to be used for running automation
	 * 
	 * @param browser
	 *            : See {@link BrowserType} This method is most probably not
	 *            goign to be used, as we will need some additional param to
	 *            specify profile related to localized language etc. See
	 *            setWebDriver (Browser, profile)
	 */
	private static String setupWebDriver(String browser) {
		DesiredCapabilities desiredCapabilities = null;
		MutableCapabilities options = null;
		RemoteWebDriver remoteWebDriver = null;
		Class<? extends RemoteWebDriver> clazz = null;
		switch (browser) {
		case BrowserType.FIREFOX:
			FirefoxOptions ffOptions = new FirefoxOptions();
			// ffOptions.setCapability(capabilityName, value);
			// FirefoxProfile profile = new FirefoxProfile();
			// profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
			// "text/csv");
			// desiredCapabilities = DesiredCapabilities.firefox();
			// desiredCapabilities.setCapability(FirefoxDriver.PROFILE,
			// profile);
			options = ffOptions;
			clazz= FirefoxDriver.class;
//			clazz.newInstance();
			if (gridHub == null){
				remoteWebDriver = new FirefoxDriver();
			}
			
			break;
		case BrowserType.GOOGLECHROME:
			options = new ChromeOptions();
			options.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			clazz = ChromeDriver.class;
			break;
		// case IE:
//			https://docs.microsoft.com/en-us/microsoft-edge/webdriver
		// desiredCapabilities = DesiredCapabilities.internetExplorer();
		// break;
		} // switch case brwosertype
				
		
		if (gridHub !=null){
		String remoteDriverUrlString;
		if (!gridHub.getString("port").equals("")) {
			remoteDriverUrlString = "http://" + gridHub.getString("host") + ":" + gridHub.getString("port") + "/wd/hub";
		} else {
			remoteDriverUrlString = "http://" + gridHub.getString("host") + "/wd/hub";
		}
		URL remoteDriverUrl = null;
		try {
			remoteDriverUrl = new URL(remoteDriverUrlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		remoteWebDriver = new RemoteWebDriver(remoteDriverUrl, options);
		remoteWebDriver.setFileDetector(new LocalFileDetector());
		} 
		else {
			try {
				remoteWebDriver = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		remoteWebDriver.manage().timeouts().implicitlyWait(TAFConfig.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
		Dimension d = new Dimension(1280, 1024);
		// Resize the current window to the given dimension

		// if (!SuiteConfiguration.fullscreen) {
		remoteWebDriver.manage().window().setSize(d);
		// }else {
		// webDriver.manage().window().maximize();
		// }
		
		return registerDriver (remoteWebDriver);

	}

	private static String setWebDriver(String browser, MutableCapabilities options) {
		RemoteWebDriver remoteWebDriver = null;
		
		if (gridHub !=null){
		String remoteDriverUrlString;
		if (!gridHub.getString("port").equals("")) {
			remoteDriverUrlString = "http://" + gridHub.getString("host") + ":" + gridHub.getString("port") + "/wd/hub";
		} else {
			remoteDriverUrlString = "http://" + gridHub.getString("host") + "/wd/hub";
		}
		URL remoteDriverUrl = null;
		try {
			remoteDriverUrl = new URL(remoteDriverUrlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		remoteWebDriver = new RemoteWebDriver(remoteDriverUrl, options);
		remoteWebDriver.setFileDetector(new LocalFileDetector());
} else {
			remoteWebDriver = new FirefoxDriver(options);
		}
		
		remoteWebDriver.manage().timeouts().implicitlyWait(TAFConfig.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
		Dimension d = new Dimension(1280, 1024);
		// Resize the current window to the given dimension

		// if (!SuiteConfiguration.fullscreen) {
		remoteWebDriver.manage().window().setSize(d);
		// }else {
		// webDriver.manage().window().maximize();
		// }
		
		return registerDriver (remoteWebDriver);

	}

	/**
	 * Sets up the Webdriver for running automation using the supplied browser
	 * type and Profile
	 * 
	 * @param browser
	 *            : See {@link BrowserType}
	 * @param ffProfilePath
	 */
	// private static WebDriver setWebDriver(String browser, DesiredCapabilities
	// desiredCapabilities) {
	//
	// WebDriver webDriver = null;
	// // if (SuiteConfiguration.useGrid){
	// String remoteDriverUrlString;
	// if (!gridHub.getString("port").equals("")) {
	// remoteDriverUrlString = "http://" + gridHub.getString("host") + ":" +
	// gridHub.getString("port") + "/wd/hub";
	// } else {
	// remoteDriverUrlString = "http://" + gridHub.getString("host") +
	// "/wd/hub";
	// }
	// URL remoteDriverUrl = null;
	// try {
	// remoteDriverUrl = new URL(remoteDriverUrlString);
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// }
	// webDriver = new RemoteWebDriver(remoteDriverUrl, desiredCapabilities);
	// ((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
	// // }
	// // else{
	// //
	//
	// /***
	// * To Remove
	// */
	// //
	// // switch (browser.toString()){
	// // case BrowserType.FIREFOX:
	// // webDriver = new FirefoxDriver(desiredCapabilities);
	// // break;
	// //// case IE:
	// //// System.setProperty("webdriver.ie.driver",
	// // SuiteConfiguration.sysPropIEDriverPath);
	// //// webDriver = new InternetExplorerDriver(desiredCapabilities);
	// //// break;
	// // case BrowserType.GOOGLECHROME:
	// // System.setProperty("webdriver.chrome.driver",
	// // TAFConfig.sysPropChromeDriverPath);
	// // webDriver = new ChromeDriver(desiredCapabilities);
	// // break;
	// // }
	// //
	// /***
	// * To Remove ^^^^^^
	// */
	//
	// //
	// // }
	// webDriver.manage().timeouts().implicitlyWait(TAFConfig.DEFAULT_IMPLICIT_WAIT,
	// TimeUnit.SECONDS);
	// Dimension d = new Dimension(1280, 1024);
	// // Resize the current window to the given dimension
	//
	// // if (!SuiteConfiguration.fullscreen) {
	// webDriver.manage().window().setSize(d);
	// // }else {
	// // webDriver.manage().window().maximize();
	// // }
	// driverInventory.put(Thread.currentThread().getId(), webDriver);
	// return webDriver;
	// }

	public static void closeSession(String key) {
		System.out.println("Closing Session:" + key);
		driverInventory.get(key).close();
		driverInventory.remove(key);
	}

	public static WebDriver getDriver(String session) {
		return driverInventory.get(session);
	}
}
