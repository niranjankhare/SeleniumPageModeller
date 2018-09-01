package seleniumng;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.seleniumng.driver.DriverInventory;
import org.seleniumng.driver.GuiControl;
import org.seleniumng.utils.ByChild;

import static org.seleniumng.utils.TAFConfig.tafConfig;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigMergeable;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;

import ExampleApp.PageLibraries.ExampleAppSession;
import ExampleApp.webPages._PageLogin;

import java.util.AbstractMap;

import java.util.Arrays;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import ExampleApp.PageLibraries.ExampleAppSession;
import java.io.FileOutputStream;
import java.io.IOException;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class AppTest extends TestTemplate {

	public static void main(String[] args) {
		ExampleAppSession s1 = new ExampleAppSession();
		String n = tafConfig.getString("aut.url");
		s1.get(n);
		s1.pageLogin.login("myUsername", "myPassword");

	}
	
	@Test
	public static void locators() {
		ExampleAppSession s1 = new ExampleAppSession();
		String n = tafConfig.getString("aut.url");
		s1.get(n);
		s1.pageLogin.login("myUsername", "myPassword");

	}
	public static void browse(String url) {
		DriverInventory.getDriver("").get(url);
	}

	@Test(dataProvider = "serialDataProvider", invocationCount = 1)
	@Parameters({ "username", "password" })
	public void sampleLogin(@Optional("user@aut.in") String username, @Optional("password") String password)
			throws InterruptedException {
		ExampleAppSession s1 = new ExampleAppSession();
		String n = tafConfig.getString("aut.url");
		s1.get(n);
		s1.pageLogin.login("myUsername", "password!23");
		s1.pageDash.waitForPageToLoad();

	}

	@Test
	public void mapJson() {
		LinkedHashMap<Integer, Map<String, String>> map = new LinkedHashMap<Integer, Map<String, String>>();
		Map<String, String> locator = new HashMap<String, String>();
		locator.put("ById", "#id");
		map.put(1, locator);
		Gson gson = new Gson();

		String mapStr = gson.toJson(map);
		String locatorStr = gson.toJson(locator);
		System.out.println(locatorStr);
		Config loc = ConfigFactory.parseString(mapStr);
		System.out.println(loc.root().render());
		String s = (String) loc.getAnyRef("1");
		System.out.println(s);
	}

	@Test
	public void listJson() {
		Type listType = new TypeToken<List<HashMap<String, String>>>() {
		}.getType();
		Gson gson = new Gson();
		List<String> list = new ArrayList<String>();
		HashMap<String, String> locator = new HashMap<String, String>();
		locator.put("ById", "#id");
		HashMap<String, String> locator1 = new HashMap<String, String>();
		locator1.put("ByName", "name");
		list.add(gson.toJson(locator));
		list.add(gson.toJson(locator1));
		
		String value = gson.toJson(list);
		Config c = ConfigFactory.parseString("value:"+value);
		System.out.println(c.root().render());
		
		
		String json = gson.toJson(list);
		System.out.println(json);
		List<HashMap<String, String>> returned = gson.fromJson(json, listType);

		Config loc = ConfigFactory.parseString("LOCATORS:" + json);
		String renderedConfig = loc.root().render(ConfigRenderOptions.concise().setFormatted(true));

		Config readConfig = ConfigFactory.parseString(renderedConfig);

		List s = readConfig.getAnyRefList("LOCATORS");
		System.out.println(s.get(0));
	}

	@Test(dataProvider = "serialDataProvider", invocationCount = 1)
	@Parameters({ "username", "password" })
	public void locatorsTest(@Optional("user@aut.in") String username, @Optional("password") String password)
			throws InterruptedException {
		String locatorOne = "ByCssSelector=.btn.btn-primary";
		String locatorTwo = "ById=j_password";
		
		Config l1 = ConfigFactory.parseString(locatorOne);
		Config l2 = ConfigFactory.parseString(locatorTwo);
		List<Config> l1l2 = Arrays.asList(l1,l2);
		Config g1 = ConfigFactory.empty(), g2 = ConfigFactory.empty();
		g1 = g1.withFallback(l1.atKey("LOCATORS"));
		
//		g2 = g2.withFallback(other)
		
		System.out.println(g1.root().render(ConfigRenderOptions.concise()));

	}


	@Test
	public void locatorsMap() {
		Config c = ConfigFactory.empty();
		ConfigValue v1 = ConfigValueFactory.fromAnyRef("#username"),v2= ConfigValueFactory.fromAnyRef("#password"),
				v3 =  ConfigValueFactory.fromAnyRef(".tbody.tr"), v4 =  ConfigValueFactory.fromAnyRef("image");
		Config l1 = ConfigFactory.empty().withValue("ById",v1 );
		Config l2 = ConfigFactory.empty().withValue("ById",v2 );
		
		Config l3 = ConfigFactory.empty().withValue("ByClass",v3 );
		Config l4 = ConfigFactory.empty().withValue("ByTag",v4 );
		
		ConfigValue l3l4 = ConfigValueFactory.fromIterable(Arrays.asList(l3.root(), l4.root()));
		
		Config composite = ConfigFactory.empty().withValue("ByAll", l3l4);
		// add username
		c = l1.atKey("Locators").atKey("iTxtUsername").withFallback(c);
		// add password
		c = l2.atKey("Locators").atKey("iTxtPassword").withFallback(c);
		
		// add the composite one
		c = composite.atKey("Locators").atKey("btnLogin").withFallback(c);
		
		System.out.println(c.root().render(ConfigRenderOptions.concise().setJson(true).setComments(false).setFormatted(true)));



	}
}

