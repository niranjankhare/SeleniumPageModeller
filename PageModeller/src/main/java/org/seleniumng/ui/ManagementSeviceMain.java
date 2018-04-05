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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ManagementSeviceMain {

	private static final Logger logger = LogManager.getLogger(ManagementSeviceMain.class);
	
    public static void main(String[] args) throws Exception
    {
        // Create a basic jetty server object that will listen on port 8080.  Note that if you set this to port 0
        // then a randomly available port will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
    	logger.info("NewLog!!");

    	Config config  = ConfigFactory.parseString("enabled = true \nport=7070");
    	ServletHandler handler = new ServletHandler();
    	if (true/*manFridayConfig.getBoolean("enabled")*/){
   	        
	    	Server server = new Server(config.getInt("port"));
	    	handler = new ServletHandler();
	    	if (config.getBoolean("enabled")){
	    		handler.addServletWithMapping (HTMLServerMain.class, "/*" );
	    		handler.addServletWithMapping (JSONResourceServer.class, "/fetch/*" );
	    	}
	    	server.setHandler(handler);
	   	 	server.start();
	   	 	server.join();
    	}
    		
    }

}
