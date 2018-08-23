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
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> locator = new HashMap<String, String>();
		locator.put("ById", "#id");
		HashMap<String, String> locator1 = new HashMap<String, String>();
		locator1.put("ByName", "name");
		list.add(locator);
		list.add(locator1);
		Gson gson = new Gson();
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
		String locatorOne = "{\"ByCssSelector\":\".btn.btn-primary\"}";
		String locatorTwo = "{\"ById\":\"j_password\"}";
		List<String> twoLocators = Arrays.asList(locatorOne, locatorTwo);
		String locatorComposite = "\"ByAll\":" + twoLocators;
		String nest = "{" + locatorComposite + "}";
		List<String> nestedComposite = Arrays.asList(locatorOne, nest);
		String locatorNested = "\"ByChained\":" + nestedComposite;
		String forCompositeConfig = "{" + locatorNested + "}";
		System.out.println(forCompositeConfig);
		String forStdConfig = locatorTwo;
		Config stubConfig = ConfigFactory.parseString("CONTROLDESCRIPTION: controlDescription");
		stubConfig = ConfigFactory.parseString("session: sessionValue").withFallback(stubConfig);
		Config compositeControlConfig = ConfigFactory.parseString("LOCATORS:" + forCompositeConfig);
		compositeControlConfig = compositeControlConfig
				.withFallback(stubConfig);
		Config stdControlConfig = ConfigFactory.parseString("LOCATORS:" + forStdConfig);
		stdControlConfig = stdControlConfig.withFallback(stubConfig );
//		System.out.println(forCompositeConfig);
		GuiControl control1 = new GuiControl(compositeControlConfig);
//		TestControl control2 = new TestControl(stdControlConfig);
		// Get different locators:
		String output = stdControlConfig.root().render();
		String name = "en_us/_PageLogin.conf";
//		  URL urlOut = _PageLogin.class.getResource(name);
		  URL urlin = null;
		  Config readFromFile = null;
//		  File f = new File(urlOut.getFile());
		try {
//			FileOutputStream os = new FileOutputStream(f);
//			os.write(output.getBytes());
			urlin =  _PageLogin.class.getResource(name);
			readFromFile =ConfigFactory.parseURL(urlin);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GuiControl controlread = new GuiControl(readFromFile);
	}

//	class TestControl {
//		public String friendlyName;
//		public String session;
//		private By byLocator;
//		Entry<String, Object> locators = null;
//
//		public TestControl(Config config) {
//			friendlyName = config.getString("CONTROLDESCRIPTION");
//			locators = createLocatorMap(config.getObject("LOCATORS"));
//			this.byLocator = getBys(locators);
//			this.session = config.getString("session");
//			// TODO Auto-generated constructor stub
//		}
//
//		private By getBys(Entry<String,Object> entry) {
//			List<By> bys = new ArrayList<By>();
//			By by = null;
////			for (Entry entry:locators.entrySet()) {
//			Object value = entry.getValue();
//			if (value instanceof String) {
//				System.out.println("leaf!!");
//				by = byLocator(entry);
//			} else {
//				System.out.println("Recurse");
//				ArrayList<Object> list = (ArrayList<Object>) value;
//				for (int i = 0; i < list.size(); i++) {
//					AbstractMap e =(AbstractMap)  list.get(i);
//				
//					System.out.println("leaf!!");					
////					 Object obj = e.getValue();
//					Entry y = (Entry<String, Object>) e.entrySet().iterator().next();
//					bys.add(getBys(y));
//				}
//				by = byLocator(entry.getKey(), bys.toArray(new By[bys.size()]));
//			}
//			return by;
//		}
//
//		private By byLocator(Entry<String, Object> entry) {
//			String value = (String) entry.getValue();
//			By byMethod = null;
//			switch (entry.getKey()) {
//			case "ByClassName":
//				byMethod = By.className(value);
//				break;
//			case "ByCssSelector":
//				byMethod = By.cssSelector(value);
//				break;
//			case "ByLinkText":
//				byMethod = By.linkText(value);
//				break;
//			case "ByName":
//				byMethod = By.name(value);
//				break;
//			case "ByPartialLinkText":
//				byMethod = By.partialLinkText(value);
//				break;
//			case "ByTagName":
//				byMethod = By.tagName(value);
//				break;
//			case "ByXPath":
//				byMethod = By.xpath(value);
//				break;
//			case "ByIdOrName":
//				byMethod = new ByIdOrName(value);
//				break;
////			case "ByChained":
////				List<By> locatorList = new ArrayList<By>();
////				for (Entry<String, String> locator : locators.entrySet()) {
////					locatorList.add(byLocator(locator));
////				}
////				byMethod = new ByChained(locatorList.toArray(new By[locatorList.size()]));
////				break;
////			case "ByAll":
////				byMethod = new ByAll(value);
////				break;
////			case "ByChild":
////				byMethod = NgBy.child(value);
////				break;
//			case "ById":
//			default:
//				byMethod = By.id(value);
//			}
//			return byMethod;
//		}
//
//		private By byLocator(String by, By... bys) {
//			By byMethod = null;
//			switch (by) {
//			case "ByChained":
////				List<By> locatorList = new ArrayList<By>();
////				for (Entry<String, String> locator : locators.entrySet()) {
////					locatorList.add(byLocator(locator));
////				}
//				byMethod = new ByChained(bys);
//				break;
//			case "ByAll":
//				byMethod = new ByAll(bys);
//				break;
//			case "ByChild":
//				byMethod = new ByChild(bys);
//				break;
//			}
//			return byMethod;
//		}
//
//		protected Entry<String, Object> createLocatorMap(ConfigObject object) {
////			Type listType = new TypeToken<List<LinkedHashMap<String, String>>>() {
////			}.getType();
//			String jSon = "";
//			try {
//				ConfigObject c = (ConfigObject) object;
//				jSon = c.render(ConfigRenderOptions.concise());
//				System.out.println("OK");
//			} catch (Exception e) {
//				e.printStackTrace();
//				;
//			}
//			Type typeMap = new TypeToken<HashMap<String, Object>>() {
//			}.getType();
//			HashMap<String, Object> bys = null;
//			HashMap<String, Object> obj = null;
//			try {
//				obj = (HashMap<String, Object>) new Gson().fromJson(jSon, typeMap);
//			} catch (Exception e) {
//				logger.error("Problem parsing json {}", jSon);
//				e.printStackTrace();
//			}
//			for (Entry<String, Object> entry : obj.entrySet()) {
//				System.out.println("key:" + entry.getKey());
//				System.out.println("ValueType:" + entry.getValue().getClass());
//			}
//
//			return obj.entrySet().iterator().next();
//		}
//
//	}

	@Test
	public void locatorsMap() {
		Gson gson = new Gson();
		ById byId = new By.ById("#myid");
		ByName byName = new By.ByName("the name");
		By byClassName = new By.ByClassName("someclass");
		By byCss = new By.ByCssSelector(".cssselector");
		By byLinkText = new By.ByLinkText("link text");
		By byPartialLinkText = new By.ByPartialLinkText("partialLinkText");
		By byTagName = new By.ByTagName("sometagName");
		ByChild byChild = new ByChild(byId, byName);
		ByChained byChained = new ByChained(byClassName, byCss);
		ByAll byAll = new ByAll(byLinkText, byPartialLinkText);

		ByChained compositeLocator = new ByChained(byChild, byTagName);

		By stdLocator = byId;
		String jsonComposite = gson.toJson(compositeLocator);
		String jsonStd = gson.toJson(stdLocator);
		System.out.println(jsonComposite);
		String configJson = jsonComposite.replaceAll("\\\"", "'");

		Config locComposite = ConfigFactory.parseString("LOCATORS = \"" + configJson + "\"");

		Config locStd = ConfigFactory.parseString("LOCATORS:" + jsonStd);
		String renderedCompositeConfig = locComposite.root().render(ConfigRenderOptions.concise().setFormatted(true));
		String renderedStdConfig = locStd.root().render(ConfigRenderOptions.concise().setFormatted(true));
		System.out.println(renderedCompositeConfig);
		System.out.println(renderedStdConfig);
		Config readCompositeConfig = ConfigFactory.parseString(renderedCompositeConfig);
		Config readStdConfig = ConfigFactory.parseString(renderedStdConfig);

//		Object automationLocatorObject = readCompositeConfig.getAnyRef("LOCATORS");
		String automationLocatorJson = readCompositeConfig.getString("LOCATORS");
		System.out.println(automationLocatorJson);
		ByChained by = gson.fromJson(automationLocatorJson, ByChained.class);

		System.out.println(by.getClass());

//		HashMap<?,?> myMap = (HashMap<?,?>)automationLocatorObject ;
//		
//		
//		for ( Entry<?, ?> entry  :myMap.entrySet()) {
//			System.out.println(entry.getKey());
//			System.out.println(entry.getValue().toString());
//		}

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