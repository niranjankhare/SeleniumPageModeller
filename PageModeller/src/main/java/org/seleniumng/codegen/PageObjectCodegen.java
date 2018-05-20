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
import java.util.Map;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import static org.seleniumng.utils.TAFConfig.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.seleniumng.ui.LibDatabase;

/**
 * @author niru
 * The main class that will generated the source code and metadata 
 * for the PageModeller.
 * This uses the 
 */
public class PageObjectCodegen {
	/**
	 * The code modeller to generate the source code for 
	 * Page Modeller
	 */
	private static JCodeModel codeModel = new JCodeModel();
	/**
	 * The code modeller to generate the metadata resources for 
	 * Page Modeller
	 * 
	 */
	private static JCodeModel resourceModel = new JCodeModel();
	
	/**
	 * Location under which the classes representing the 
	 * Page Objects will be generated
	 */
	private static String sourceDirPath =  tafConfig.getString("PageModeller.sourceDirPath");
	/**
	 * Location under which the Page Objects metadata will be 
	 * generated
	 */
	private static String resourceDirPath =  tafConfig.getString("PageModeller.resourceDirPath"); 
	
	/**
	 * Controls whether to overwrite existing user defined library templates
	 */
	private static Boolean reWriteUserDefinedLibs = tafConfig.getBoolean("PageModeller.reWriteUserDefinedLibs");

	/**
	 * The superclass for the PageRepository class generated for your
	 * application under test. This class defines constructors to auto-
	 * initialize your PageRepository 
	 */
	private static JClass objectRepositoryBaseClass = codeModel
			.directClass("org.seleniumng.utils.PageObjectRepository");
	/**
	 * The superclass for the Page Object class heirarchy.
	 * This class defines constructors to auto-initialize your Page objects
	 * using the Pages metadata.
	 */
	private static JClass pageClassBaseClass = codeModel.directClass("org.seleniumng.utils.PageObjectBaseClass");

	public static void main(String... args) {
		try {
			PageObjectCodegen.generateSource();

		} catch (JClassAlreadyExistsException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Uncaught error in generating page library!");
			ex.printStackTrace();
		}
		
	}
	
	

	/**
	 * Starting point for generating the Page Object Model. 
	 * Defines the Page Repository for your application based on the 
	 * metadata saved in the source database. 
	 * @throws JClassAlreadyExistsException
	 * @throws IOException
	 */
	public static void generateSource() throws JClassAlreadyExistsException, IOException {
		// Instantiate an instance of the JCodeModel class
		String myApplication = tafConfig.getString("application");
		String pagePackage = myApplication + ".webPages";
		String userLibrariesPackage = myApplication + ".PageLibraries";
		JDefinedClass repositoryToCreate = codeModel._class(userLibrariesPackage + ".PageRepository");
		repositoryToCreate._extends(objectRepositoryBaseClass);
	
		List<HashMap<String, String>> orderedList = LibDatabase.getPageHeirarchy();
		for (HashMap<String, String> webPages : orderedList) {
			for (String webPage : webPages.keySet()) {
				JClass pageClassToCreate = generatePageObject(pagePackage, userLibrariesPackage, webPage,
						webPages.get(webPage));
				repositoryToCreate.field(JMod.STATIC| JMod.PUBLIC, pageClassToCreate, "page" + webPage);

			}
		}
		codeModel.build(new File(sourceDirPath));
		resourceModel.build(new File(resourceDirPath));
	}

	/**
	 * @param pPackage the package to be used to generate the classes for Page objects
	 * Note that this should be a package will have only generatede code. Any manual edits
	 * in the package can be overwritten the next time code generation is done.
	 * @param uPackage the package to be used to generate the classes for 
	 * User to extend the generated Page objects where he can write his libraries  
	 * @param webPage Name for the Class representing the Page Object
	 * @param parent super class for the webPage class
	 * @return the class to include in the page repository for users
	 * this would be where the user is expected to implement his re-usable
	 * actions on this page. this could be the JDefinedClass or JClass 
	 * depending on whether the Library class exists or not and if user has
	 * specifically asked for re-writing the class 
	 * See: reWriteUserDefinedLibs
	 * @throws IOException in case of problems with writing the source code
	 */
	private static JClass generatePageObject(String pPackage, String uPackage, String webPage, String parent)
			throws IOException {

		JDefinedClass mainClass = null;
		JDefinedClass parentClass = null;
		JClass userImplClass = null;
		JPackage retResource = null;
		JPackage uLibResource = null;
		String classFQN = pPackage + "._Page" + webPage;
		String parentSN = "Page" + parent;
		String parentFQN = uPackage + "." + parentSN;
		String userImplClassSN = "Page" + webPage;
		String userImplClassFQN = uPackage + "." + userImplClassSN;
		try {
			if (codeModel._getClass(classFQN) == null) {
				mainClass = codeModel._class(classFQN);
				Boolean classExists = false;
				try {
					classExists = null != Class.forName(userImplClassFQN);
				}catch (Exception e){
					e.printStackTrace();
				}
				if (reWriteUserDefinedLibs || !classExists) {
					userImplClass = codeModel._class(userImplClassFQN);
					((JDefinedClass) userImplClass)._extends(mainClass);
				} else {
					userImplClass = codeModel.directClass(userImplClassFQN);
				}
			}
			if (parent != null) {
				parentClass = codeModel._getClass(parentFQN);
				if (parentClass == null && reWriteUserDefinedLibs) {
					parentClass: codeModel._class(parentFQN);
					mainClass._extends(parentClass);
				} else {
					JClass p = codeModel.directClass(parentFQN);
					mainClass._extends(p);
				}
			} else
				mainClass._extends(pageClassBaseClass);
			retResource = resourceModel._package(pPackage +"." +tafConfig.getString("language"));
			uLibResource = resourceModel._package(uPackage+"." +tafConfig.getString("language"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		LinkedHashMap<String, LinkedHashMap<String, String>> fields = LibDatabase.getPageGuiMapData(webPage);
		Map<String, ? extends Object> properties = LibDatabase.getPageGuiPropertyData(webPage);
		Config c = ConfigFactory.parseMap(properties);
		for (String control : fields.keySet()) {
			String stdClass = fields.get(control).get("standardClass");
			String classAbrv = LibDatabase.getClassAbrv(stdClass);
			String customClass = fields.get(control).get("customClass");
			customClass = (customClass == null || customClass.equals("") || customClass.equals("(No Maping)"))
					? stdClass : customClass;

			JClass jc = codeModel.ref("org.seleniumng.controls." + customClass);
			mainClass.field(JMod.PUBLIC, jc, classAbrv + control);

		}
		String rsrcPath = mainClass.name() + ".conf";
		String uLibRsrcPath = userImplClassSN+ ".conf";
		JTextFile rsrc = new JTextFile(rsrcPath);
		JTextFile uLibRrc = new JTextFile(uLibRsrcPath);
		String propertyMap = c.root().render(ConfigRenderOptions.concise().setFormatted(true).setJson(true));
		rsrc.setContents(propertyMap);
//		uLibRrc.setContents("\"somePropertyName\" = \""+ userImplClassSN+"\"");
		uLibRrc.setContents("");
		retResource.addResourceFile(rsrc);
		uLibResource.addResourceFile(uLibRrc);

		return userImplClass;
	}

}
