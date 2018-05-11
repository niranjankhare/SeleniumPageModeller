package seleniumng;


import static myAut.PageLibraries.PageRepository.*;

import org.seleniumng.driver.DriverInventory;
import org.seleniumng.utils.TAFConfig;
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
	
	@Test (dataProvider="parallelDataProvider")
	@Parameters({"username","password"})
	public void sampleLogin(@Optional("nkh@ums.no")String username, @Optional("Admin1") String password) throws InterruptedException {
		new PageRepository();
		browse (TAFConfig.tafConfig.getString("aut.url"));
		pageLogin.Login(username,password);
	  Thread.sleep(10000);  // Let the user check stuff!
	  DriverInventory.closeSession();
	}
}
