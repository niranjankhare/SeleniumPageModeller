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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

	/**
	 * Saves a screenshot of the session
	 */
	public void saveScreenshot() {
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE), destFile;

		try {
			destFile = File.createTempFile(TAFConfig.tafConfig.getString("browser"), "-ScreenShot.png");
			FileChannel src = new FileInputStream(scrFile).getChannel();
			FileChannel dest = new FileOutputStream(destFile).getChannel();
			dest.transferFrom(src, 0, src.size());
			System.out.println("Screenshot saved as:" + destFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
