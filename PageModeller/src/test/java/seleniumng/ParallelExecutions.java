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
package seleniumng;


import java.io.UnsupportedEncodingException;
import org.seleniumng.driver.DriverInventory;
import org.testng.annotations.Test;

public class ParallelExecutions {
	
	
	@Test
	public void testCaseOne() throws UnsupportedEncodingException {
		//Printing Id of the thread on using which test method got executed
		DriverInventory.getDriver("");
	

	    System.out.println("Done");

	}

	@Test
	public void testCaseTwo() {
		////Printing Id of the thread on using which test method got executed
		DriverInventory.getDriver("");
	}
	
	@Test
	public void testCaseThree() {
		////Printing Id of the thread on using which test method got executed
		DriverInventory.getDriver("");
	}
	
	@Test
	public void testCaseFour() {
		////Printing Id of the thread on using which test method got executed
		DriverInventory.getDriver("");
	}

}
