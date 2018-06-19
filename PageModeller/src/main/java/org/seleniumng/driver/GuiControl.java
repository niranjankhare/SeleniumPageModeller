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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.typesafe.config.Config;

/**
 * Wrapper for Seleniums Remote Web element, to be able to provide methods
 * wrappers on top of what selenium webdriver provides
 * 
 * @author niru
 *
 */
public class GuiControl extends RemoteWebElement {
	public String friendlyName;
	public String locType;
	public String locValue;
	public String session;

	public GuiControl(Config config) {
		friendlyName = config.getString("CONTROLDESCRIPTION");
		locType = config.getString("LOCATORTYPE");
		locValue = config.getString("LOCATORVALUE");
		this.id = locValue;
		this.session = config.getString("session");

	}
	
	/**
	 * @return the WebElement wrapped by this instance
	 */
	public WebElement  i(){
		return findMe();
	}

	private WebElement findMe() {
		WebElement me = null;
		By byMethod = null;
		switch (this.locType) {
		case "ByClassName":
			byMethod = By.className(locValue);
			break;
		case "ByCssSelector":
			byMethod = By.cssSelector(locValue);
			break;
		case "ByLinkText":
			byMethod = By.linkText(locValue);
			break;
		case "ByName":
			byMethod = By.name(locValue);
			break;
		case "ByPartialLinkText":
			byMethod = By.partialLinkText(locValue);
			break;
		case "ByTagName":
			byMethod = By.tagName(locValue);
			break;
		case "ByXPath":
			byMethod = By.xpath(locValue);
			break;
		case "ById":
		default:
			byMethod = By.id(locValue);
		}
		me = DriverInventory.getDriver(session).findElement(byMethod);
		return me;
	}

	public void playAround(Integer... keysToSend) {
		// TODO Auto-generated method stub

	}

	public void setText(CharSequence... keysToSend) {
		clear();
		sendKeys(keysToSend);
	}

	public void typeAt(Integer positionReference, CharSequence... keysToSend) {
		// TODO Auto-generated method stub

	}

	/* All interface methods below? */
	@Override
	public void click() {
		findMe().click();

	}

	@Override
	public void submit() {
		findMe().submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		findMe().sendKeys(keysToSend);
	}

	@Override
	public void clear() {
		findMe().clear();

	}

	@Override
	public String getTagName() {
		return findMe().getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return findMe().getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		return findMe().isSelected();
	}

	@Override
	public boolean isEnabled() {
		return findMe().isEnabled();
	}

	@Override
	public String getText() {
		return findMe().getText();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return findMe().findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return findMe().findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		return findMe().isDisplayed();
	}

	@Override
	public Point getLocation() {
		return findMe().getLocation();
	}

	@Override
	public Dimension getSize() {
		return findMe().getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		return findMe().getCssValue(propertyName);
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return findMe().getScreenshotAs(target);
	}

	@Override
	public Rectangle getRect() {
		return findMe().getRect();
	}

}
