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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TAFConfig {
	private static final Config resolver = ConfigFactory.parseResourcesAnySyntax("resolver");
	public static final long DEFAULT_IMPLICIT_WAIT = 0;
	public static Config tafConfig = ConfigFactory.load().withFallback(resolver).resolve();
	
	public static String dbURL = tafConfig.getString("db.url");
	public static String dbUser = tafConfig.getString("db.username");
	public static String dbPass = tafConfig.getString("db.password");
	
	public static String sysPropChromeDriverPath = tafConfig.getString("webdriver.chrome.driver");
	public static String sysPropMozGeckoDriverPath = tafConfig.getString("webdriver.gecko.driver");
	
	public static Config getConfig (String atPath){
		if (tafConfig.hasPath(atPath))
			return tafConfig.getConfig(atPath);
		else return null;
				
	}
}
