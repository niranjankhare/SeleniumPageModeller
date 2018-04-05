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

import java.util.LinkedHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverInventory {
	
	private static LinkedHashMap <Long, WebDriver> driverInventory = new LinkedHashMap <Long, WebDriver>(); 
	public void registerDriver (int threadInstance, WebDriver webDriver){
		// can this not pick up the driver instance associated with the thread?
	}
	public void registerDriver (int threadInstance, RemoteWebDriver remoteWebDriver){
		
	}
	
	public static WebDriver getDriver (String testName){
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
		Long threadId = Thread.currentThread().getId();
		WebDriver driver = driverInventory.get(threadId);
		if (driver == null) {
//			driver =new ChromeDriver(); 
			System.setProperty("webdriver.gecko.driver","D:/Users/niru/workspace/TestAutomationFramework/src/main/resources/geckodriver.exe");
			driver =new FirefoxDriver(); 
			driverInventory.put(threadId, driver);
			System.out.println("Returning new instance!!");
		} else {
			System.out.println("Returning Existing instance!!");
		}
		return driver;
	} 
}
