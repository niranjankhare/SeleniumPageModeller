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
package org.seleniumng.codegen;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.jar.JarException;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import static org.seleniumng.utils.TAFConfig.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.seleniumng.controls.*;
import org.seleniumng.ui.LibDatabase;

public class PageObjectCodegen {
	private static JCodeModel codeModel = new JCodeModel();
	public static void main(String... args) {

		try {
			PageObjectCodegen.generateSource();
		} catch (JClassAlreadyExistsException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void generateSource() throws JClassAlreadyExistsException, IOException {
		// Instantiate an instance of the JCodeModel class
		String myApplication = tafConfig.getString("application");
		String pagePackage = myApplication + ".webPages";
		
		JDefinedClass repositoryToCreate = codeModel._class(pagePackage + ".PageRepository");
		List<String> webPages = fetchPageList();
		for (String webPage : webPages) {
			JDefinedClass pageClassToCreate = func(pagePackage, webPage);
			// JClass jc = codeModel.ref(TextField.class);
			//
			// // Creating fields in the class
			//
			// JFieldVar field1 = pageClassToCreate.field(JMod.PRIVATE, jc,
			// "foo", JExpr._null());

			repositoryToCreate.field(JMod.PUBLIC, pageClassToCreate, "page" + webPage);

		}
		// again

		// JDefinedClass will let you create a class in a specified package.

		/*
		 * //The codeModel instance will have a list of Java primitives which
		 * can be //used to create a primitive field in the new class JFieldVar
		 * field2 = classToBeCreated.field(JMod.PRIVATE, codeModel.DOUBLE,
		 * "bar",JExpr.direct("new TextField()")); // field2.assign( ); //Create
		 * getter and setter methods for the fields JMethod field1GetterMethod =
		 * classToBeCreated.method(JMod.PUBLIC, field1.type(), "getFoo"); //code
		 * to create a return statement with the field1
		 * field1GetterMethod.body()._return(field1); JMethod field1SetterMethod
		 * = classToBeCreated.method(JMod.PUBLIC, codeModel.VOID, "setFoo");
		 * //code to create an input parameter to the setter method which will
		 * take a variable of type field1
		 * field1SetterMethod.param(field1.type(), "inputFoo"); //code to create
		 * an assignment statement to assign the input argument to the field1
		 * field1SetterMethod.body().assign(JExpr._this().ref ("foo"), JExpr.ref
		 * ("inputFoo"));
		 * 
		 * JMethod field2GetterMethod = classToBeCreated.method(JMod.PUBLIC,
		 * field2.type(), "getBar"); field2GetterMethod.body()._return(field2);
		 * JMethod field2SetterMethod = classToBeCreated.method(JMod.PUBLIC,
		 * codeModel.VOID, "setBar"); field2SetterMethod.param(field2.type(),
		 * "inputBar"); field2SetterMethod.body().assign(JExpr._this().ref
		 * ("bar"), JExpr.ref ("inputBar"));
		 * 
		 * //creating an enum class within our main class JDefinedClass
		 * enumClass = classToBeCreated._enum(JMod.PUBLIC, "REPORT_COLUMNS");
		 * //This code creates field within the enum class JFieldVar columnField
		 * = enumClass.field(JMod.PRIVATE|JMod.FINAL, String.class, "column");
		 * JFieldVar filterableField = enumClass.field(JMod.PRIVATE|JMod.FINAL,
		 * codeModel.BOOLEAN, "filterable");
		 * 
		 * //Define the enum constructor JMethod enumConstructor =
		 * enumClass.constructor(JMod.PRIVATE);
		 * enumConstructor.param(String.class, "column");
		 * enumConstructor.param(codeModel.BOOLEAN, "filterable");
		 * enumConstructor.body().assign(JExpr._this().ref ("column"),
		 * JExpr.ref("column")); enumConstructor.body().assign(JExpr._this().ref
		 * ("filterable"), JExpr.ref("filterable"));
		 * 
		 * JMethod getterColumnMethod = enumClass.method(JMod.PUBLIC,
		 * String.class, "getColumn");
		 * getterColumnMethod.body()._return(columnField); JMethod
		 * getterFilterMethod = enumClass.method(JMod.PUBLIC, codeModel.BOOLEAN,
		 * "isFilterable"); getterFilterMethod.body()._return(filterableField);
		 * 
		 * JEnumConstant enumConst = enumClass.enumConstant("FOO_BAR");
		 * enumConst.arg(JExpr.lit("fooBar")); enumConst.arg(JExpr.lit(true));
		 */
		// This will generate the code in the specified file path.
		codeModel.build(new File("src/main/java"));
	}

	private static List<String> fetchPageList() {
		Set<String> set = LibDatabase.getAvailablePages().keySet();
		List<String> pages = new ArrayList<String>();
		for (String page : set) {
			pages.add(page);
		}
		return pages;
	}

	private static JDefinedClass func(String pPackage, String webPage) throws IOException {
		
		JDefinedClass retClass = null;
		try {
			retClass = codeModel._class(pPackage + ".Page" + webPage);
		} catch (Exception e) {

		}
		LinkedHashMap<String, LinkedHashMap<String, String>> data = LibDatabase.getPageData(webPage);

		for (String control : data.keySet()) {
			String abrv = LibDatabase.getClassAbrv(data.get(control).get("abrv"));
			String classz = data.get(control).get("class");
			JClass jc = codeModel.ref("org.seleniumng.controls."+classz);
			retClass.field(JMod.PRIVATE, jc, abrv+control, JExpr._null());
		}
		codeModel.build(new File("src/main/java"));
		return retClass;
	}
}