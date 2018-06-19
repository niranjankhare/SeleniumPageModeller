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
package org.seleniumng.controls;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.seleniumng.driver.GuiControl;

import com.typesafe.config.Config;

/**
 * A class to represent the Table element
 * @author niru
 *
 */
public class Table extends GuiControl{

	public Table(Config config) {
		super(config);
		rowsLocator = config.getString("rowsLocator");
		headerRowLocator = config.getString("headerRowLocator");
	}
	
	private String rowsLocator;
	private String headerRowLocator;
	
	
	public List<WebElement> getRows(){
		return i().findElements(By.cssSelector("tr"));
	}
	
	public List<WebElement> getColumnNames(){
		return i().findElements(By.cssSelector("thead tr th"));
	}
	
}
