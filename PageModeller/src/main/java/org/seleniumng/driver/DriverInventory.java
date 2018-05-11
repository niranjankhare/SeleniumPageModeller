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

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.seleniumng.utils.TAFConfig;

import com.typesafe.config.Config;

public class DriverInventory {
	private static Config gridHub = TAFConfig.getConfig("seleniumGrid");
	private static LinkedHashMap<Long, WebDriver> driverInventory = new LinkedHashMap<Long, WebDriver>();

	public void registerDriver(int threadInstance, WebDriver webDriver) {
		// can this not pick up the driver instance associated with the thread?
	}

	public void registerDriver(int threadInstance, RemoteWebDriver remoteWebDriver) {

	}

	public static WebDriver getDriver() {
		System.setProperty("webdriver.chrome.driver", TAFConfig.sysPropChromeDriverPath);
		System.setProperty("webdriver.gecko.driver", TAFConfig.sysPropMozGeckoDriverPath);
		for (Long k : driverInventory.keySet()) {
			System.out.println("Session:"+ k);
		}
		WebDriver driver = driverInventory.get(Thread.currentThread().getId());
		if (driver == null) {
			if (gridHub == null)
				return getDriver(Thread.currentThread().getId());
			else {
				return setWebDriver(BrowserType.GOOGLECHROME);
			}
		} else
			return driver;

	}

	private static WebDriver getDriver(Long threadId) {
		WebDriver driver = driverInventory.get(threadId);
		if (driver == null) {
			// driver =new ChromeDriver();

			driver = new ChromeDriver();
			driverInventory.put(threadId, driver);
			System.out.println("Returning new instance!!");
		} else {
			System.out.println("Returning Existing instance!!");
		}
		return driver;
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
	private static WebDriver setWebDriver(String browser) {
		DesiredCapabilities desiredCapabilities = null;
		ChromeOptions options = null;
		switch (browser) {
		case BrowserType.FIREFOX:
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			desiredCapabilities = DesiredCapabilities.firefox();
			desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);

			break;
		case BrowserType.GOOGLECHROME:
			// desiredCapabilities = DesiredCapabilities.chrome();
			options = new ChromeOptions();
			// options.addArguments("--test-type");
			options.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			// desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,
			// options);

			break;
		// case IE:
		// desiredCapabilities = DesiredCapabilities.internetExplorer();
		// break;
		}
		// return setWebDriver (browser, desiredCapabilities);
		return setWebDriver(browser, options);

	}

	private static WebDriver setWebDriver(String browser, ChromeOptions options) {
		WebDriver webDriver = null;
		// if (SuiteConfiguration.useGrid){
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
		webDriver = new RemoteWebDriver(remoteDriverUrl, options);
		((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
		// }
		// else{
		//

		/***
		 * To Remove
		 */
		//
		// switch (browser.toString()){
		// case BrowserType.FIREFOX:
		// webDriver = new FirefoxDriver(desiredCapabilities);
		// break;
		//// case IE:
		//// System.setProperty("webdriver.ie.driver",
		// SuiteConfiguration.sysPropIEDriverPath);
		//// webDriver = new InternetExplorerDriver(desiredCapabilities);
		//// break;
		// case BrowserType.GOOGLECHROME:
		// System.setProperty("webdriver.chrome.driver",
		// TAFConfig.sysPropChromeDriverPath);
		// webDriver = new ChromeDriver(desiredCapabilities);
		// break;
		// }
		//
		/***
		 * To Remove ^^^^^^
		 */

		//
		// }
		webDriver.manage().timeouts().implicitlyWait(TAFConfig.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
		Dimension d = new Dimension(1280, 1024);
		// Resize the current window to the given dimension

		// if (!SuiteConfiguration.fullscreen) {
		webDriver.manage().window().setSize(d);
		// }else {
		// webDriver.manage().window().maximize();
		// }
		driverInventory.put(Thread.currentThread().getId(), webDriver);
		return webDriver;

	}

	/**
	 * Sets up the Webdriver for running automation using the supplied browser
	 * type and Profile
	 * 
	 * @param browser
	 *            : See {@link BrowserType}
	 * @param ffProfilePath
	 */
	private static WebDriver setWebDriver(String browser, DesiredCapabilities desiredCapabilities) {

		WebDriver webDriver = null;
		// if (SuiteConfiguration.useGrid){
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
		webDriver = new RemoteWebDriver(remoteDriverUrl, desiredCapabilities);
		((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
		// }
		// else{
		//

		/***
		 * To Remove
		 */
		//
		// switch (browser.toString()){
		// case BrowserType.FIREFOX:
		// webDriver = new FirefoxDriver(desiredCapabilities);
		// break;
		//// case IE:
		//// System.setProperty("webdriver.ie.driver",
		// SuiteConfiguration.sysPropIEDriverPath);
		//// webDriver = new InternetExplorerDriver(desiredCapabilities);
		//// break;
		// case BrowserType.GOOGLECHROME:
		// System.setProperty("webdriver.chrome.driver",
		// TAFConfig.sysPropChromeDriverPath);
		// webDriver = new ChromeDriver(desiredCapabilities);
		// break;
		// }
		//
		/***
		 * To Remove ^^^^^^
		 */

		//
		// }
		webDriver.manage().timeouts().implicitlyWait(TAFConfig.DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
		Dimension d = new Dimension(1280, 1024);
		// Resize the current window to the given dimension

		// if (!SuiteConfiguration.fullscreen) {
		webDriver.manage().window().setSize(d);
		// }else {
		// webDriver.manage().window().maximize();
		// }
		driverInventory.put(Thread.currentThread().getId(), webDriver);
		return webDriver;
	}

	public static void closeSession() {
		System.out.println("Closing Session:"+ Thread.currentThread().getId());
		getDriver().close();
	}
}
