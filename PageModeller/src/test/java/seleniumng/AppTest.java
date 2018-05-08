package seleniumng;


import static myAut.PageLibraries.PageRepository.*;

import org.seleniumng.driver.DriverInventory;
import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import myAut.PageLibraries.PageRepository;

public class AppTest extends TestTemplate {

	public static void main(String[] args) {
		new PageRepository();
		browse ("https://example.com/login");
		pageLogin.Login("myUsername", "myPassword");

	}
	
	public static void browse (String url){
		DriverInventory.getDriver().get(url);
	}
	
	@Test (dataProvider="parallelDataProvider", dataProviderClass= TestDataProvider.class)
	@Parameters({"username","password"})
	public void sampleLogin(@Optional("nkh@ums.no")String username, @Optional("Admin1") String password) throws InterruptedException {
		new PageRepository();
		browse ("https://dev.ums2.no");
		pageLogin.Login(username,password);
	  Thread.sleep(10000);  // Let the user actually see something!
	}
}
