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
