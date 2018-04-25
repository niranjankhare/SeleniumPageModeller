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

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;

import java.util.Arrays;

import static org.seleniumng.utils.TAFConfig.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.seleniumng.controls.*;
import org.seleniumng.ui.LibDatabase;

public class PageObjectCodegen {
	private static JCodeModel codeModel = new JCodeModel();
	private static JCodeModel resourceModel = new JCodeModel();
	private static String sourceDirPath = "src/main/java";
	private static String resourceDirPath = "src/main/resources";

	public static void main(String... args) {
		// List<HashMap<String, String>> h = LibDatabase.getPageHeirarchy();
		try {
			PageObjectCodegen.generateSource();

		} catch (JClassAlreadyExistsException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// System.out.println(b.toString());
		System.out.println("Done");
	}

	public static void generateSource() throws JClassAlreadyExistsException, IOException {
		// Instantiate an instance of the JCodeModel class
		String myApplication = tafConfig.getString("application");
		String pagePackage = myApplication + ".webPages";

		JDefinedClass repositoryToCreate = codeModel._class(pagePackage + ".PageRepository");
		// HashMap<String, String> webPages = fetchPageList();
		List<HashMap<String, String>> orderedList = LibDatabase.getPageHeirarchy();
		for (HashMap<String, String> webPages : orderedList) {
			for (String webPage : webPages.keySet()) {
				JDefinedClass pageClassToCreate = generatePageObject(pagePackage, webPage, webPages.get(webPage));
				repositoryToCreate.field(JMod.PUBLIC, pageClassToCreate, "page" + webPage);
			}
		}
		codeModel.build(new File(sourceDirPath));
	}

	// private static HashMap<String, String> fetchPageList() {
	// List<HashMap<String, String>> set = LibDatabase.getPageHeirarchy();
	// return (HashMap<String, String>) set;
	// }

	private static JDefinedClass generatePageObject(String pPackage, String webPage, String parent) throws IOException {

		JDefinedClass retClass = null;
		JDefinedClass parentClass = null;
		JPackage retResource = null;
		try {
			String classFQN = pPackage + ".Page" + webPage;
			String parentFQN = pPackage + ".Page" + parent;
			if (codeModel._getClass(classFQN)== null)
				retClass = codeModel._class(pPackage + ".Page" + webPage);
			if (parent !=null && codeModel._getClass(parentFQN)==null){
				parentClass = codeModel._class(parentFQN);
			retClass._extends(parentClass);
			}
			retResource = resourceModel._package(pPackage + ".en");

		} catch (Exception e) {
			e.printStackTrace();
		}
		LinkedHashMap<String, LinkedHashMap<String, String>> data = LibDatabase.getPageGuiMapData(webPage);

		for (String control : data.keySet()) {
			String stdClass = data.get(control).get("standardClass");
			String classAbrv = LibDatabase.getClassAbrv(stdClass);
			String customClass = data.get(control).get("customClass");
			customClass = (customClass==null|| customClass.equals("")) ? stdClass : customClass;

			JClass jc = codeModel.ref("org.seleniumng.controls." + customClass);
			retClass.field(JMod.PRIVATE, jc, classAbrv + control, JExpr._null());

		}
		String rsrcPath = "Page" + webPage + ".conf";
		JTextFile rsrc = new JTextFile(rsrcPath);
		rsrc.setContents("resourceContent");
		retResource.addResourceFile(rsrc);
		resourceModel.build(new File(resourceDirPath));
		return retClass;
	}

	// again

	// JDefinedClass will let you create a class in a specified package.

	/*
	 * //The codeModel instance will have a list of Java primitives which can be
	 * //used to create a primitive field in the new class JFieldVar field2 =
	 * classToBeCreated.field(JMod.PRIVATE, codeModel.DOUBLE,
	 * "bar",JExpr.direct("new TextField()")); // field2.assign( ); //Create
	 * getter and setter methods for the fields JMethod field1GetterMethod =
	 * classToBeCreated.method(JMod.PUBLIC, field1.type(), "getFoo"); //code to
	 * create a return statement with the field1
	 * field1GetterMethod.body()._return(field1); JMethod field1SetterMethod =
	 * classToBeCreated.method(JMod.PUBLIC, codeModel.VOID, "setFoo"); //code to
	 * create an input parameter to the setter method which will take a variable
	 * of type field1 field1SetterMethod.param(field1.type(), "inputFoo");
	 * //code to create an assignment statement to assign the input argument to
	 * the field1 field1SetterMethod.body().assign(JExpr._this().ref ("foo"),
	 * JExpr.ref ("inputFoo"));
	 * 
	 * JMethod field2GetterMethod = classToBeCreated.method(JMod.PUBLIC,
	 * field2.type(), "getBar"); field2GetterMethod.body()._return(field2);
	 * JMethod field2SetterMethod = classToBeCreated.method(JMod.PUBLIC,
	 * codeModel.VOID, "setBar"); field2SetterMethod.param(field2.type(),
	 * "inputBar"); field2SetterMethod.body().assign(JExpr._this().ref ("bar"),
	 * JExpr.ref ("inputBar"));
	 * 
	 * //creating an enum class within our main class JDefinedClass enumClass =
	 * classToBeCreated._enum(JMod.PUBLIC, "REPORT_COLUMNS"); //This code
	 * creates field within the enum class JFieldVar columnField =
	 * enumClass.field(JMod.PRIVATE|JMod.FINAL, String.class, "column");
	 * JFieldVar filterableField = enumClass.field(JMod.PRIVATE|JMod.FINAL,
	 * codeModel.BOOLEAN, "filterable");
	 * 
	 * //Define the enum constructor JMethod enumConstructor =
	 * enumClass.constructor(JMod.PRIVATE); enumConstructor.param(String.class,
	 * "column"); enumConstructor.param(codeModel.BOOLEAN, "filterable");
	 * enumConstructor.body().assign(JExpr._this().ref ("column"),
	 * JExpr.ref("column")); enumConstructor.body().assign(JExpr._this().ref
	 * ("filterable"), JExpr.ref("filterable"));
	 * 
	 * JMethod getterColumnMethod = enumClass.method(JMod.PUBLIC, String.class,
	 * "getColumn"); getterColumnMethod.body()._return(columnField); JMethod
	 * getterFilterMethod = enumClass.method(JMod.PUBLIC, codeModel.BOOLEAN,
	 * "isFilterable"); getterFilterMethod.body()._return(filterableField);
	 * 
	 * JEnumConstant enumConst = enumClass.enumConstant("FOO_BAR");
	 * enumConst.arg(JExpr.lit("fooBar")); enumConst.arg(JExpr.lit(true));
	 */

}
