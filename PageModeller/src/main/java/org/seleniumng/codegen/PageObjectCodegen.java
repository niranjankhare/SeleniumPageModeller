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

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import static org.seleniumng.utils.TAFConfig.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.seleniumng.controls.*;
import org.seleniumng.ui.LibDatabase;

public class PageObjectCodegen {
	private static JCodeModel codeModel = new JCodeModel();
	private static JCodeModel resourceModel = new JCodeModel();
	private static String sourceDirPath = "src/main/java";
	private static String resourceDirPath = "src/main/resources";

	static Boolean overriteLib = false;

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
		String userLibrariesPackage = myApplication + ".PageLibraries";

		JDefinedClass repositoryToCreate = codeModel._class(userLibrariesPackage + ".PageRepository");
		// HashMap<String, String> webPages = fetchPageList();
		List<HashMap<String, String>> orderedList = LibDatabase.getPageHeirarchy();
		for (HashMap<String, String> webPages : orderedList) {
			for (String webPage : webPages.keySet()) {
				JClass pageClassToCreate = generatePageObject(pagePackage, userLibrariesPackage, webPage,
						webPages.get(webPage));
				repositoryToCreate.field(JMod.PUBLIC, pageClassToCreate, "page" + webPage);

			}
		}
		codeModel.build(new File(sourceDirPath));
		resourceModel.build(new File(resourceDirPath));
	}

	// private static HashMap<String, String> fetchPageList() {
	// List<HashMap<String, String>> set = LibDatabase.getPageHeirarchy();
	// return (HashMap<String, String>) set;
	// }

	private static JClass generatePageObject(String pPackage, String uPackage, String webPage, String parent)
			throws IOException {

		JDefinedClass mainClass = null;
		JDefinedClass parentClass = null;
		JClass userImplClass = null;
		JPackage retResource = null;
		String classFQN = pPackage + "._Page" + webPage;
		String parentSN = "Page"+parent;
		String parentFQN = uPackage + "."+ parentSN;
		String userImplClassSN = "Page" + webPage;
		String userImplClassFQN = uPackage +"."+ userImplClassSN;
		try {
			if (codeModel._getClass(classFQN) == null) {
				mainClass = codeModel._class(classFQN);
				if (overriteLib) {
					userImplClass = codeModel._class(userImplClassFQN);
					((JDefinedClass)userImplClass)._extends(mainClass);
				} else {
					userImplClass =codeModel.directClass (userImplClassFQN);
				}
			}
			if (parent != null) {
				parentClass = codeModel._getClass(parentFQN);
				if (parentClass == null && overriteLib) {
					parentClass: codeModel._class(parentFQN);
					mainClass._extends(parentClass);
				} else {
					JClass p = codeModel.directClass(parentFQN);
					mainClass._extends(p);
				}
			}
			retResource = resourceModel._package(uPackage + ".en");

		} catch (Exception e) {
			e.printStackTrace();
		}
		LinkedHashMap<String, LinkedHashMap<String, String>> fields = LibDatabase.getPageGuiMapData(webPage);
		LinkedHashMap<String, LinkedHashMap<String, String>> properties = LibDatabase.getPageGuiPropertyData(webPage);
		for (String control : fields.keySet()) {
			String stdClass = fields.get(control).get("standardClass");
			String classAbrv = LibDatabase.getClassAbrv(stdClass);
			String customClass = fields.get(control).get("customClass");
			customClass = (customClass == null || customClass.equals("") || customClass.equals("(No Maping)"))
					? stdClass : customClass;

			JClass jc = codeModel.ref("org.seleniumng.controls." + customClass);
			mainClass.field(JMod.PROTECTED, jc, classAbrv + control, JExpr._null());
			
		}
		String rsrcPath = userImplClassSN + ".conf";
		JTextFile rsrc = new JTextFile(rsrcPath);
		
//		rsrc.setContents(propertyMap );
		retResource.addResourceFile(rsrc);

		return userImplClass;
	}

	// again

	// JDefinedClass will let you create a class in a specified package.

	/*
	 * //The codeModel instance will have a list of Java primitives which can be
	 * //used to create a primitive field in the new class JFieldVar field2 =
	 * classToBeCreated.field(JMod.PRIVATE, codeModel.DOUBLE,
	 * "bar",JExpr.direct("new InputText()")); // field2.assign( ); //Create
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
