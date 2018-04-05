package org.seleniumng.ui;

import static org.seleniumng.utils.TAFConfig.dbPass;
import static org.seleniumng.utils.TAFConfig.dbURL;
import static org.seleniumng.utils.TAFConfig.dbUser;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DbManager {
	private static String userName = dbUser;
	private static String password = dbPass;
	private static String url = dbURL;
	private static URL database = null;
	private static DataSource conn = initDbConnection();
	
	
	public static void main(String[] args) throws Exception {
        PoolProperties p = new PoolProperties();
//        p.setUrl("jdbc:mysql://192.168.3.141:3306/mysql");
//        p.setDriverClassName("com.mysql.jdbc.Driver");
//        p.setUsername("root");
//        p.setPassword("password");
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
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);

        Connection con = null;
        try {
          con = datasource.getConnection();
          Statement st = con.createStatement();
          ResultSet rs = st.executeQuery("select * from user");
          int cnt = 1;
          while (rs.next()) {
              System.out.println((cnt++)+". Host:" +rs.getString("Host")+
                " User:"+rs.getString("User")+" Password:"+rs.getString("Password"));
          }
          rs.close();
          st.close();
        } finally {
          if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
    }
	
	
	private static DataSource initDbConnection() {
	       PoolProperties p = new PoolProperties();
//	        p.setUrl("jdbc:mysql://192.168.3.141:3306/mysql");
//	        p.setDriverClassName("com.mysql.jdbc.Driver");
//	        p.setUsername("root");
//	        p.setPassword("password");
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
	        p.setJdbcInterceptors(
	          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
	          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
	        DataSource datasource = new DataSource();
	        datasource.setPoolProperties(p);
	        return datasource;
	}


	private static Connection initDbConnectionX() {
//		try {
//			conn = DriverManager.getConnection(url, userName, password);
//			return conn;
//		} catch (Exception e) {
//			System.out.println("Unable to connect to database, Exiting!!:");
//			e.printStackTrace();
//			System.exit(-1);
//			;
//			return null;
//		}
 return null;
	}
	public static DSLContext getOpenContext() {
//		try {
//			if (conn.isClosed()) {
//				conn = initDbConnection();
//			} else {
//				try {
//					DSLContext d = DSL.using(conn);
//					d.fetch("SELECT VERSION();");
//				} catch (Exception e) {
//					conn = initDbConnection();
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return DSL.using(conn,SQLDialect.MYSQL);
	}

}
