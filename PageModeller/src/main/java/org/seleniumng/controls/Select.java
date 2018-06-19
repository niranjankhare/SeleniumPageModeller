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
 *  A class to represent the Select element
 *  
 * @author niru
 *
 */
public class Select extends GuiControl{

	public Select(Config config) {
		super(config);
	}
	
    /**
     * @param option
     *               the text representing the option to select
     */
    public void select(String option) {
        System.out.println("Using selenium selectByVisibleText");
        
        org.openqa.selenium.support.ui.Select s = new org.openqa.selenium.support.ui.Select (i());
        s.selectByVisibleText(option);
    }
    /**
     * @param index
     *             the index of the option to select
     */      
    public void select(Integer index) {
        System.out.println("Using selenium selectByIndex");
        org.openqa.selenium.support.ui.Select s = new org.openqa.selenium.support.ui.Select (i());
        s.selectByIndex(index);
    }

}
