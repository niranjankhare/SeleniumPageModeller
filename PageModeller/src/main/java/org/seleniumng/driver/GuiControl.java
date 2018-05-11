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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.typesafe.config.Config;
/**
 * Wrapper for Seleniums Remote Web element, to be able to provide
 * methods wrappers on top of what selenium webdriver provides
 * @author niru
 *
 */
public class GuiControl extends RemoteWebElement {
	public String friendlyName ;
	public String locType;
	public String locValue;
	public GuiControl (Config config){
		friendlyName = config.getString("CONTROLDESCRIPTION");
		locType= config.getString("LOCATORTYPE");
		locValue = config.getString("LOCATORVALUE");
		this.id = locValue;
		
	}
	public void playAround(Integer... keysToSend) {
		// TODO Auto-generated method stub

	}
	
	
	public void setText(CharSequence... keysToSend) {
		clear ();
		sendKeys(keysToSend);
	}
	
	
	public void typeAt(Integer positionReference, CharSequence... keysToSend ) {
		// TODO Auto-generated method stub

	}
	/*All interface methods below?*/
	@Override
	public void click() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		we.click();

	}

	@Override
	public void submit() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		we.submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		we.sendKeys (keysToSend);
	}

	@Override
	public void clear() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		we.clear();

	}

	@Override
	public String getTagName() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getTagName();
	}

	@Override
	public String getAttribute(String name) {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.isSelected();
	}

	@Override
	public boolean isEnabled() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.isEnabled();
	}

	@Override
	public String getText() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getText();
	}

	@Override
	public List<WebElement> findElements(By by) {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.isDisplayed();	}

	@Override
	public Point getLocation() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getLocation();
	}

	@Override
	public Dimension getSize() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getCssValue(propertyName);
	}


	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getScreenshotAs(target);
	}


	@Override
	public Rectangle getRect() {
		WebElement we = DriverInventory.getDriver().findElement(By.id(locValue));
		return we.getRect();
	}

}
