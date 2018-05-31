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

import javax.naming.ConfigurationException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigException.Missing;
public class TAFConfig {
	private static final Config resolver = ConfigFactory.parseResourcesAnySyntax("resolver");
	public static final long DEFAULT_IMPLICIT_WAIT = 0;
	public static Config tafConfig = ConfigFactory.load().withFallback(resolver).resolve();
	
	public static String dbURL = getValue("db.url");
	public static String dbUser = getValue("db.username");
	public static String dbPass = getValue("db.password");
	
	public static String sysPropChromeDriverPath = getValue("webdriver.chrome.driver");
	public static String sysPropMozGeckoDriverPath = getValue("webdriver.gecko.driver");
	
	public static Config getConfig (String atPath){
		if (tafConfig.hasPath(atPath))
			return tafConfig.getConfig(atPath);
		else 
			throw new Missing(atPath);
	}
	
	@SuppressWarnings("unchecked")
	private static  <T> T getValue (String atPath){
		if (tafConfig.hasPath(atPath))
			return (T) tafConfig.getAnyRef(atPath);
		else
			throw new Missing (atPath);
	}
}
