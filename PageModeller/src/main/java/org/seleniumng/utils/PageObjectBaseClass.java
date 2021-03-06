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
package org.seleniumng.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.seleniumng.driver.DriverInventory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static org.seleniumng.utils.TAFConfig.tafConfig;

public class PageObjectBaseClass {

	protected Config pageConf = null;
	protected String session = null;
	protected Integer pageDirtyTimeoutMils = tafConfig.getInt("PageModeller.pageDirtyTimeoutMils");
	protected Integer pageLoadTimeoutSecs = tafConfig.getInt("PageModeller.pageLoadTimeoutSecs");

	public PageObjectBaseClass() {
		List<Class<?>> classHeirarchy = new ArrayList<Class<?>>();
		classHeirarchy.add(this.getClass());
		Class<?> c = this.getClass().getSuperclass();
		while (!c.isAssignableFrom(org.seleniumng.utils.PageObjectBaseClass.class)) {
			classHeirarchy.add(c);
			c = c.getSuperclass();
		}
		Config conf = loadPageConfig(classHeirarchy.get(classHeirarchy.size() - 1));
		for (int i = classHeirarchy.size() - 1; i > 0; i--) {
			conf = loadPageConfig(classHeirarchy.get(i - 1)).withFallback(conf);
		}
		this.pageConf = conf;
//		List<Field> fields = listAllFields();
//		for (Field f:fields){
//			Object o = getField (f);
//			try {
//				f.set(this, o);
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public void initializeFields(String driver) {
		this.session = driver;
		List<Field> fields = listAllFields();
		for (Field f : fields) {
			Object o = getField(f);
			try {
				f.set(this, o);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	protected Object getField(Field field) {
		Class<?> ftype = field.getType();

		List<Class<?>> classHeirarchy = new ArrayList<Class<?>>();
		classHeirarchy.add(ftype);
		Class<?> c = ftype.getSuperclass();
		while (!c.isAssignableFrom(org.seleniumng.driver.GuiControl.class)) {

			classHeirarchy.add(c);
			c = c.getSuperclass();
		}

		Config confControl = pageConf.getConfig(field.getName());
		confControl = confControl.withFallback(ConfigFactory.parseString("session = " + this.session));
		Constructor<?> constructor = null;

		try {
			constructor = ftype.getConstructor(new Class[] { Config.class });
			Object o = constructor.newInstance(confControl);
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected List<Field> listAllFields(/* Object obj */) {
		List<Field> fieldList = new ArrayList<Field>();
		Class<?> tmpClass = this.getClass();
		while (tmpClass != null) {
			List<Field> listControls = new ArrayList<Field>();
			for (Field field : tmpClass.getDeclaredFields()) {
				if (org.seleniumng.driver.GuiControl.class.isAssignableFrom(field.getType())) {
					listControls.add(field);
				}
			}
			fieldList.addAll(listControls);
			tmpClass = tmpClass.getSuperclass();
		}
		return fieldList;
	}

	protected Config loadPageConfig(Class<?> class1) {
		String langPath = tafConfig.getString("language");
		String langDefault = tafConfig.hasPath("language.fallback") ? tafConfig.getString("language.fallback")
				: "en_us";
		String propFile = class1.getSimpleName() + ".conf";
//		URL u_lang = class1.getResource(langPath + "/"+propFile);
//		URL u_langFallback = class1.getResource(langDefault + "/"+propFile);
		Config c_lang = getPropConfig(class1, langPath + "/" + propFile),
				c_fallback = getPropConfig(class1, langDefault + "/" + propFile);

		Config cf = c_lang.withFallback(c_fallback);
		return cf;
	}

	private Config getPropConfig(Class<?> class1, String resourceFile) {

		Config retConfig = null;
		URL resourceAsURL = class1.getResource(resourceFile);
		try {
			retConfig = ConfigFactory.parseURL(resourceAsURL);
		} catch (Exception e) {
			System.out.println("Got Exception trying to load : " + resourceFile);
			retConfig = ConfigFactory.parseString("");
		}
		return retConfig;
	}

	public void waitForPageToLoad() {
		waitForPageToLoad(pageDirtyTimeoutMils, pageLoadTimeoutSecs);
	}

	public void waitForPageToLoad(int loadTimeout) {
		waitForPageToLoad(pageDirtyTimeoutMils, loadTimeout);
	}

	public void waitForPageToLoad(Integer dirtyTimeoutMills, Integer loadTimeoutSec) {
		long timer = System.currentTimeMillis();
		sleep(dirtyTimeoutMills); // some default wait for hte navigation to kick in
		JavascriptExecutor js = (JavascriptExecutor) DriverInventory.getDriver(session);

		long timeOut = System.currentTimeMillis() + loadTimeoutSec * 1000;
		Object o = null;
		while (System.currentTimeMillis() < timeOut) {
			o = js.executeScript("return document.readyState");
			if (o.toString().equalsIgnoreCase("complete")) {
				System.out.println("Exting loop after secs:" + (timer - System.currentTimeMillis()) / 1000);
				break;
			}
			sleep(10);
		}
	}

	public void sleep(int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
