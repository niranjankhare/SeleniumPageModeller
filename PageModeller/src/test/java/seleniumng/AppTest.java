package seleniumng;


import static myAut.PageLibraries.PageRepository.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.seleniumng.driver.DriverInventory;
import org.testng.annotations.Test;

import myAut.PageLibraries.PageRepository;

public class AppTest {

	public static void main(String[] args) {
		new PageRepository();
		browse ("https://dev.ums2.no");
		
//		pageDashboard.userRole.select ("WHere are you");
		pageLogin.Login("NKH", "somepass");
//		System.out.println(pageLogin.menuConfig.friendlyName);
//		System.out.println(pageLogin.menuConfig.locValue);
//		System.out.println(pageLogin.selLanguage.friendlyName);
//		System.out.println(pageLogin.footerStuff.friendlyName);
//		System.out.println(pageDashboard.footerStuff.friendlyName);
//		System.out.println(pageLogin.controlAtTheBase.friendlyName);

	}
	
	public static void browse (String url){
		DriverInventory.getDriver().get(url);
//		pageLogin.getWebDriver().get(url);
	}
	@Test
	public void testGoogleSearch() throws InterruptedException {
	  // Optional, if not specified, WebDriver will search your path for chromedriver.
	  System.setProperty("webdriver.chrome.driver", "D:\\Users\\niru\\Sandbox\\chromedriver.exe");

	  WebDriver driver = new ChromeDriver();
	  driver.get("http://www.google.com/xhtml");
	  Thread.sleep(5000);  // Let the user actually see something!
	  WebElement searchBox = driver.findElement(By.name("q"));
	  searchBox.sendKeys("ChromeDriver");
	  searchBox.submit();
	  Thread.sleep(5000);  // Let the user actually see something!
	  driver.quit();
	}
}
