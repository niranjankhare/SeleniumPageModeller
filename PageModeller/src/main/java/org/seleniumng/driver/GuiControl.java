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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.seleniumng.utils.ByChild;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typesafe.config.Config;

/**
 * Wrapper for Seleniums Remote Web element, to be able to provide methods
 * wrappers on top of what selenium webdriver provides
 * 
 * @author niru
 *
 */
public class GuiControl extends RemoteWebElement {
	private final static Logger logger = LogManager.getLogger(GuiControl.class);
	public String friendlyName;
	public String session;
	private List<LinkedHashMap<String, String>> locators;
	private List<By> byLocators;

	public GuiControl(Config config) {
		friendlyName = config.getString("CONTROLDESCRIPTION");
		locators = createLocatorMap(config.getString("LOCATORS"));
		this.byLocators = getBys();
		this.session = config.getString("session");
	}

	private List<By> getBys() {
		List<By> bys = new ArrayList<By>();
		for (int i = 0; i < locators.size(); i++) {
			LinkedHashMap<String, String> locator = locators.get(i);
			for (Entry<String, String> e : locator.entrySet()) {
				bys.add(byLocator(e));
			}
		}
		return bys;
	}

	private List<LinkedHashMap<String, String>> createLocatorMap(Object object) {
		Type listType = new TypeToken<List<LinkedHashMap<String, String>>>() {
		}.getType();
		String jSon = (String) object;
		List<LinkedHashMap<String, String>> bys = null;
		try {
			bys = new Gson().fromJson(jSon, listType);
		} catch (Exception e) {
			logger.error("Problem parsing json {}", jSon);
			e.printStackTrace();
		}
		return bys;
	}

	/**
	 * @return the WebElement wrapped by this instance
	 */
	public WebElement me() {
		WebElement me = null;
		if (byLocators.size() == 1) {
			me = DriverInventory.getDriver(session).findElement(byLocators.get(0));
		}
		return me;
	}

	private By byLocator(Entry<String, String> entry) {
		String value = entry.getValue();
		By byMethod = null;
		switch (entry.getKey()) {
		case "ByClassName":
			byMethod = By.className(value);
			break;
		case "ByCssSelector":
			byMethod = By.cssSelector(value);
			break;
		case "ByLinkText":
			byMethod = By.linkText(value);
			break;
		case "ByName":
			byMethod = By.name(value);
			break;
		case "ByPartialLinkText":
			byMethod = By.partialLinkText(value);
			break;
		case "ByTagName":
			byMethod = By.tagName(value);
			break;
		case "ByXPath":
			byMethod = By.xpath(value);
			break;
		case "ByIdOrName":
			byMethod = new ByIdOrName(value);
			break;
//		case "ByChained":
//			List<By> locatorList = new ArrayList<By>();
//			for (Entry<String, String> locator : locators.entrySet()) {
//				locatorList.add(byLocator(locator));
//			}
//			byMethod = new ByChained(locatorList.toArray(new By[locatorList.size()]));
//			break;
//		case "ByAll":
//			byMethod = new ByAll(value);
//			break;
//		case "ByChild":
//			byMethod = NgBy.child(value);
//			break;
		case "ById":
		default:
			byMethod = By.id(value);
		}
		return byMethod;
	}

	public void playAround(Integer... keysToSend) {
		// TODO Auto-generated method stub

	}

	public void setText(CharSequence... keysToSend) {
		logger.info("Setting text to {}", friendlyName);	
		clear();
		sendKeys(keysToSend);
	}

	public void typeAt(Integer positionReference, CharSequence... keysToSend) {
		logger.info("Typing at postion to {}", friendlyName);	

	}

	/* All interface methods below? */
	@Override
	public void click() {
		logger.info("Clicking {}", friendlyName);	
		me().click();

	}

	@Override
	public void submit() {
		logger.info("Submitting {}", friendlyName);	
		me().submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		logger.debug("Sending keys to {}", friendlyName);	
		me().sendKeys(keysToSend);
	}

	@Override
	public void clear() {
		logger.info("Clearing {}", friendlyName);	
		me().clear();

	}

	@Override
	public String getTagName() {
		return me().getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return me().getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		return me().isSelected();
	}

	@Override
	public boolean isEnabled() {
		return me().isEnabled();
	}

	@Override
	public String getText() {
		return me().getText();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return me().findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return me().findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		return me().isDisplayed();
	}

	@Override
	public Point getLocation() {
		return me().getLocation();
	}

	@Override
	public Dimension getSize() {
		return me().getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		return me().getCssValue(propertyName);
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return me().getScreenshotAs(target);
	}

	@Override
	public Rectangle getRect() {
		return me().getRect();
	}

}
