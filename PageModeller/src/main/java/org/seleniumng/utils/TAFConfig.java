package org.seleniumng.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TAFConfig {
	private static final Config resolver = ConfigFactory.parseResourcesAnySyntax("resolver");
	public static Config tafConfig = ConfigFactory.load().withFallback(resolver).resolve();
	
	public static String dbURL = tafConfig.getString("db.url");
	public static String dbUser = tafConfig.getString("db.username");
	public static String dbPass = tafConfig.getString("db.password");
}
