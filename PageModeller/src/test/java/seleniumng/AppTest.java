package seleniumng;




import org.seleniumng.driver.DriverInventory;
import org.seleniumng.utils.TAFConfig;
import static org.seleniumng.utils.TAFConfig.tafConfig;
import org.testng.annotations.Test;

import ExampleApp.PageLibraries.ExampleAppSession;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;



public class AppTest extends TestTemplate {

	public static void main(String[] args) {
		ExampleAppSession s1 = new ExampleAppSession();
		String n =tafConfig.getString("aut.url");
		s1.get(n );
		s1.pageLogin.login("nkh@ums.no", "Admin1");
		ExampleAppSession s2 = new ExampleAppSession();
//		String n =tafConfig.getString("aut.url");
		s2.get(n );
		s2.pageLogin.login("manfriday@test.ums", "Admin1");
//		pageLogin.Login("myUsername", "myPassword");

	}
	
	public static void browse (String url){
		DriverInventory.getDriver("").get(url);
	}
	
	@Test (dataProvider="parallelDataProvider")
	@Parameters({"username","password"})
	public void sampleLogin(@Optional("nkh@ums.no")String username, @Optional("Admin1") String password) throws InterruptedException {
//		new PageRepository();
//		browse (TAFConfig.tafConfig.getString("aut.url"));
//		pageLogin.Login(username,password);
	  Thread.sleep(10000);  // Let the user check stuff!
	  DriverInventory.closeSession("");
	}
}
