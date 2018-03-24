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

public class GuiControl extends RemoteWebElement {
	public String friendlyName ;
	public String locType;
	public String locValue;
	public GuiControl (Config config){
		friendlyName = config.getString("description");
		locType= config.getString("locatorType");
		locValue = config.getString("locatorValue");
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
		super.click();

	}

	@Override
	public void submit() {
		super.submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		super.sendKeys (keysToSend);
	}

	@Override
	public void clear() {
		super.clear();

	}

	@Override
	public String getTagName() {
		return super.getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return super.getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		return super.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	public String getText() {
		return super.getText();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return super.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return super.findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		return super.isDisplayed();	}

	@Override
	public Point getLocation() {
		return super.getLocation();
	}

	@Override
	public Dimension getSize() {
		return super.getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		return super.getCssValue(propertyName);
	}


	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return super.getScreenshotAs(target);
	}


	@Override
	public Rectangle getRect() {
		return super.getRect();
	}

}
