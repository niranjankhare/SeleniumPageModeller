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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.seleniumng.driver.DriverInventory;

public class SessionManager {

	public String session;

	public void get(String url) {
		DriverInventory.getDriver(session).get(url);
	}

	public WebDriver getDriver() {
		return DriverInventory.getDriver(session);
	}

	public void waitForPageToLoad(int i) {
		long timer = System.currentTimeMillis();
		sleep(3800); // some default wait for hte navigation to kick in
		JavascriptExecutor js = (JavascriptExecutor) getDriver();

		long timeOut = System.currentTimeMillis() + i * 1000;
		Object o = null;
		while (System.currentTimeMillis() < timeOut) {
			
			o = js.executeScript("return document.readyState");
//			System.out.println(o.toString().equalsIgnoreCase("complete"));
			if (o.toString().equalsIgnoreCase("complete")) {
				System.out.println("exting loop after mills:" + (timer - System.currentTimeMillis()));
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

	public SessionManager() {
		Class<?> cls = this.getClass();
		session = DriverInventory.getNewDriver();

		Field[] fields = cls.getDeclaredFields();

		for (Field f : fields) {
			Class<?> ftype = f.getType();
			Constructor<?> constructor = null;
			try {
				constructor = ftype.getConstructor();
				Object o = constructor.newInstance();
				Class[] paramWebDriver = new Class[1];
				paramWebDriver[0] = String.class;
				Method method = ftype.getMethod("initializeFields", paramWebDriver);
				method.invoke(o, session);
				f.set(this, o);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
