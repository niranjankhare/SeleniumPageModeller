package org.seleniumng.controls;

import org.seleniumng.driver.GuiControl;

import com.typesafe.config.Config;

public class Select extends GuiControl{

	public Select(Config config) {
		super(config);
	}
	
    public void select(CharSequence... option) {
        System.out.println("Use selenium selectByVisibleText");
        /*
         * mySelectElement = driver.findElement(By.id("mySelect"));
Select dropdown= new Select(mySelectElement);
dropdown.selectByVisibleText("Italy");
         */
    }
    public void select(Integer index) {
        System.out.println("Use selenium selectByIndex");
        /*
         * mySelectElement = driver.findElement(By.id("mySelect"));
Select dropdown= new Select(mySelectElement);
dropdown.selectByIndex(2);
         */
    }

}
