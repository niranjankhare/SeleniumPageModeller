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
package org.seleniumng.ui;

import static org.seleniumng.utils.TAFConfig.dbPass;
import static org.seleniumng.utils.TAFConfig.dbURL;
import static org.seleniumng.utils.TAFConfig.dbUser;

import java.sql.Connection;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DbManager {
	private static String userName = dbUser;
	private static String password = dbPass;
	private static String url = dbURL;
	private static DataSource conn = initDbConnection();

	private static DataSource initDbConnection() {
		PoolProperties p = new PoolProperties();
		p.setUrl(url);
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername(userName);
		p.setPassword(password);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(10);
		p.setInitialSize(3);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);
		return datasource;
	}

	/**
	 * @return teh DSL Context for JOOQ query exection
	 */
	public static DSLContext getOpenContext() {
		return DSL.using(conn, SQLDialect.MYSQL, new Settings().withRenderSchema(false) );
	}

}
