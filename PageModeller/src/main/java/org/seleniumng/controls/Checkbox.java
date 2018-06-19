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

import org.seleniumng.driver.GuiControl;

import com.typesafe.config.Config;

/**
 * A class to represent the Checkbox element
 * @author niru
 *
 */
public class Checkbox extends GuiControl{

	public Checkbox(Config config) {
		super(config);
	}
	
	public Boolean isChecked (){
		String checked = i().getAttribute("checked");
		return Boolean.parseBoolean(checked);
	}
	
	public void setChecked(){
		if (!isChecked())
			i().click();
	}
	public void setUnChecked(){
		if (isChecked())
			i().click();
	}
}
