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
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JTextFile;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import static org.seleniumng.utils.TAFConfig.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author niru The main class that will generated the source code and metadata
 *         for the PageModeller. This uses the
 */
public class PageObjectCodegen {
	/**
	 * Location under which the classes representing the Page Objects will be
	 * generated
	 */
	private static String sourceDirPath = tafConfig.getString("PageModeller.sourceDirPath");
	/**
	 * Location under which the Page Objects metadata will be generated
	 */
	private static String resourceDirPath = tafConfig.getString("PageModeller.resourceDirPath");
	/**
	 * Parent package for the package/classes representing the Page Objects will be
	 * generated
	 */
	private static String targetPackage = tafConfig.getString("PageModeller.targetPackage");
	/**
	 * The code modeller to generate the source code for Page Modeller
	 */
	private static JCodeModel codeModel = new JCodeModel();
	/**
	 * The code modeller to generate the metadata resources for Page Modeller
	 * 
	 */
	private static JCodeModel resourceModel = new JCodeModel();

	private static String myApplication = tafConfig.getString("application");
	private static String pagePackage = targetPackage + "." + myApplication + ".webPages";
	private static String userLibrariesPackage = targetPackage + "." + myApplication + ".PageLibraries";

	private static List<String> languages = tafConfig.getStringList("languages");
	private static String defaultLanguage = languages.get(0);

	private static JPackage retResource = resourceModel._package(pagePackage + "." + defaultLanguage);
	private static JPackage uLibResource = resourceModel._package(userLibrariesPackage + "." + defaultLanguage);
	/**
	 * Controls whether to overwrite existing user defined library templates
	 */
	private static Boolean reWriteUserDefinedLibs = tafConfig.getBoolean("PageModeller.reWriteUserDefinedLibs");
	/**
	 * user defined controls take precedence over library ones
	 */
	private static String additionalControlsPackage = tafConfig.getString("PageModeller.additionalControlsPackage");;
	/**
	 * class path to find if class exists
	 */
	private static ClassPath classPath = getClassPath();
	/**
	 * The superclass for the PageRepository class generated for your application
	 * under test. This class defines constructors to auto- initialize your
	 * PageRepository
	 */
	private static JClass objectRepositoryBaseClass = codeModel.directClass("org.seleniumng.utils.SessionManager");
	/**
	 * The superclass for the Page Object class heirarchy. This class defines
	 * constructors to auto-initialize your Page objects using the Pages metadata.
	 */
	private static JClass pageClassBaseClass = codeModel.directClass("org.seleniumng.utils.PageObjectBaseClass");

	/**
	 * The package PageModeller references for Page control classes
	 */
	private static String pageModellerControlsPackage = "org.seleniumng.controls";

	private static ImmutableSet<ClassInfo> pageModellerControlsSet = classPath
			.getTopLevelClasses(pageModellerControlsPackage);
	private static ImmutableSet<ClassInfo> additionalControlsSet = classPath
			.getTopLevelClasses(additionalControlsPackage);
	private static ImmutableSet<ClassInfo> userLibrariesPackageSet = classPath.getTopLevelClasses(userLibrariesPackage);

	public static void main(String... args) {
		DateTime start = DateTime.now();
		try {
			PageObjectCodegen.generateSource();

		} catch (JClassAlreadyExistsException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Uncaught error in generating page library!");
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateTime done = DateTime.now();
		System.out.println("Done in mills: ");
		System.out.println(done.getMillis() - start.getMillis());
	}

	/**
	 * Starting point for generating the Page Object Model. Defines the Page
	 * Repository for your application based on the metadata saved in the source
	 * database.
	 * 
	 * @throws JClassAlreadyExistsException thrown by codeModel
	 * @throws IOException                  thrown by codeModel
	 * @throws ClassNotFoundException       if the controlClass for page fields does
	 *                                      not exist
	 */
	public static void generateSource() throws JClassAlreadyExistsException, IOException, ClassNotFoundException {
		JDefinedClass repositoryToCreate = codeModel._class(userLibrariesPackage + "." + myApplication + "Session");
		repositoryToCreate._extends(objectRepositoryBaseClass);

		List<HashMap<String, String>> orderedList = CodegenDatabase.getPageHeirarchy();

		for (HashMap<String, String> webPages : orderedList) {
			for (String webPage : webPages.keySet()) {
				JClass pageClassToCreate = generatePageObject(webPage, webPages.get(webPage));
				repositoryToCreate.field(JMod.PUBLIC, pageClassToCreate, "page" + webPage);

			}
		}
		codeModel.build(new File(sourceDirPath));
		resourceModel.build(new File(resourceDirPath));
		codeModel = new JCodeModel();
		resourceModel = new JCodeModel();
	}

	/**
	 * @param webPage Name for the Class representing the Page Object
	 * @param parent  super class for the webPage class
	 * @return the class to include in the page repository for users this would be
	 *         where the user is expected to implement his re-usable actions on this
	 *         page. this could be the JDefinedClass or JClass depending on whether
	 *         the Library class exists or not and if user has specifically asked
	 *         for re-writing the class See: reWriteUserDefinedLibs
	 * @throws IOException, ClassNotFoundException in case of problems with writing
	 *                      the source code
	 */
	private static JClass generatePageObject(String webPage, String parent) throws IOException, ClassNotFoundException {

		JDefinedClass pageClass = null;
		JDefinedClass parentClass = null;
		JClass userImplClass = null;
		String pageSN = "_Page" + webPage;
		String pageFQN = pagePackage + "." + pageSN;
		String parentSN = "Page" + parent;
		String parentFQN = userLibrariesPackage + "." + parentSN;
		String userImplClassSN = "Page" + webPage;
		String userImplClassFQN = userLibrariesPackage + "." + userImplClassSN;
		try {
			if (codeModel._getClass(pageFQN) == null) {
				pageClass = codeModel._class(pageFQN);
				Boolean classExists = getClassExists(userImplClassSN, userLibrariesPackageSet);
				if (reWriteUserDefinedLibs || !classExists) {
					userImplClass = codeModel._class(userImplClassFQN);
					((JDefinedClass) userImplClass)._extends(pageClass);
				} else {
					userImplClass = codeModel.directClass(userImplClassFQN);
				}
			}
			if (parent != null) {
				parentClass = codeModel._getClass(parentFQN);
				if (parentClass == null && reWriteUserDefinedLibs) {
					parentClass = codeModel._class(parentFQN);
					pageClass._extends(parentClass);
				} else {
					JClass p = codeModel.directClass(parentFQN);
					pageClass._extends(p);
				}
			} else
				pageClass._extends(pageClassBaseClass);

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Object> fieldProperties = CodegenDatabase.getPageGuiMapData2(webPage);
		LinkedHashMap<String, LinkedHashMap<String, String>> fields = (LinkedHashMap<String, LinkedHashMap<String, String>>) fieldProperties
				.get(0);
		Map<String, ? extends Object> properties = (Map<String, ? extends Object>) fieldProperties.get(1);
		Config c = ConfigFactory.empty();
		for (Entry<String, ? extends Object> entry : properties.entrySet()) {
			Config e = (Config) entry.getValue();
			c = e.atKey(entry.getKey()).withFallback(c);
		}
		for (String control : fields.keySet()) {
			String stdClass = fields.get(control).get("standardClass");
			String classAbrv = fields.get(control).get("typeAbrv");
			String customClass = fields.get(control).get("customClass");
			String controlClass = (customClass == null || customClass.equals("") || customClass.equals("(No Maping)"))
					? stdClass
					: customClass;

			String controlClassPackage = getControlClassPackage(controlClass);
			if (controlClassPackage == null)
				throw new ClassNotFoundException("You do not have a class:" + controlClass);
			JClass jc = codeModel.ref(controlClassPackage + "." + controlClass);
			pageClass.field(JMod.PUBLIC, jc, classAbrv + control);

		}
		String rsrcFQN = pageClass.name() + ".conf";
		String uLibRsrcFQN = userImplClassSN + ".conf";
		JTextFile rsrc = new JTextFile(rsrcFQN);
		JTextFile uLibRrc = new JTextFile(uLibRsrcFQN);
		String propertyMap = c.root().render(ConfigRenderOptions.concise().setFormatted(true).setJson(true));
		System.out.println(propertyMap);
		rsrc.setContents(propertyMap);
		uLibRrc.setContents("");
		retResource.addResourceFile(rsrc);
		uLibResource.addResourceFile(uLibRrc);

		return userImplClass;
	}

	private static Boolean getClassExists(String clazz, ImmutableSet<ClassInfo> classInfoSet) {

		Boolean exists = false;
		for (ClassInfo e : classInfoSet) {
			exists = e.getSimpleName().equals(clazz);
			if (exists)
				break;
		}
		return exists;
	}

	private static String getControlClassPackage(String controlClass) {
		String clsPackage = null;
		if (getClassExists(controlClass, additionalControlsSet)) {
			clsPackage = additionalControlsPackage;
			return clsPackage;
		}
		if (getClassExists(controlClass, pageModellerControlsSet))
			clsPackage = pageModellerControlsPackage;
		return clsPackage;
	}

	private static ClassPath getClassPath() {
		ClassPath toReturn = null;
		try {
			toReturn = ClassPath.from(Thread.currentThread().getContextClassLoader());
		} catch (Exception e) {

		}
		return toReturn;
	}
}
