package seleniumng;




import org.seleniumng.driver.DriverInventory;
import org.seleniumng.utils.TAFConfig;
import static org.seleniumng.utils.TAFConfig.tafConfig;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import ExampleApp.PageLibraries.ExampleAppSession;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;



public class AppTest extends TestTemplate {

	public static void main(String[] args) {
		ExampleAppSession s1 = new ExampleAppSession();
		String n =tafConfig.getString("aut.url");
		s1.get(n );
		s1.pageLogin.login("myUsername", "myPassword");

	}
	
	public static void browse (String url){
		DriverInventory.getDriver("").get(url);
	}
	
	@Test (dataProvider="serialDataProvider", invocationCount =1)
	@Parameters({"username","password"})
	public void sampleLogin(@Optional("user@aut.in")String username, @Optional("password") String password) throws InterruptedException {
		ExampleAppSession s1 = new ExampleAppSession();
		String n =tafConfig.getString("aut.url");
		s1.get(n );
		s1.pageLogin.login("myUsername", "password!23");
		s1.pageDashboard.lnkSend.click();
		
	}
	
	@Test
	public void mapJson () {
	LinkedHashMap<Integer, Map<String,String>>	 map = new LinkedHashMap<Integer,Map<String,String>>(); 
	Map<String,String> locator = new HashMap<String,String>();
	locator.put("ById", "#id");
	map.put(1,locator );
	Gson gson = new Gson();
	
	String mapStr = gson.toJson(map);
	String locatorStr = gson.toJson(locator);
	System.out.println(locatorStr);
	Config loc = ConfigFactory.parseString(mapStr);
	System.out.println(loc.root().render());
	String s = (String)loc.getAnyRef("1");
	System.out.println(s);
	}
	@Test
	public void listJson () {
		 Type listType = new TypeToken<List <HashMap<String,String>>>() {
		}.getType();
	List <HashMap<String,String>>  list = new ArrayList<HashMap<String,String>>();
	HashMap<String,String> locator = new HashMap<String,String>();
	locator.put("ById", "#id");
	HashMap<String,String> locator1 = new HashMap<String,String>();
	locator1.put("ByName", "name");
	list.add(locator);
	list.add(locator1 );
	Gson gson = new Gson();
	String json = gson.toJson(list);
	System.out.println(json);
	List <HashMap<String,String>> returned = gson.fromJson(json, listType); 
	
	Config loc = ConfigFactory.parseString ("LOCATORS:"+json);
	String renderedConfig = loc.root().render(ConfigRenderOptions.concise().setFormatted(true));
	
	Config readConfig = ConfigFactory.parseString(renderedConfig);
	
	List s = readConfig.getAnyRefList("LOCATORS");
	System.out.println(s.get(0));
	}
}

//{
//    "iTxtUsername" : {
//        "CONTROLDESCRIPTION" : "Login User name field",
//        "LOCATORS" : [{"ById": ,
//        "LOCATORVALUE" : "j_username"
//    }
//}


//{
//    "iTxtUsername" : {
//        "CONTROLDESCRIPTION" : "Login User name field",
//        "LOCATORTYPE" : "ById",
//        "LOCATORVALUE" : "j_username"
//    }
//}