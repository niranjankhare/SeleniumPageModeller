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
package org.seleniumng.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static org.seleniumng.utils.TAFConfig.tafConfig;

public class PageObjectBaseClass {

	protected Config pageConf = null;

	public PageObjectBaseClass (){
		List <Class<?>> classHeirarchy = new ArrayList<Class<?>>();
		classHeirarchy.add(this.getClass());
		Class<?> c = this.getClass().getSuperclass();
		while  (!c.isAssignableFrom(org.seleniumng.utils.PageObjectBaseClass.class) ) {
			classHeirarchy.add(c);
			c = c.getSuperclass();
		}
		Config conf = loadPageConfig(classHeirarchy.get(classHeirarchy.size()-1));
		for (int i = classHeirarchy.size()-1; i>0; i--){
			conf = loadPageConfig(classHeirarchy.get(i-1)).withFallback(conf);
		}
		this.pageConf = conf;
		
		List<Field> fields = listAllFields();
		for (Field f:fields){
			Object o = getField (f);
			try {
				f.set(this, o);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected Object getField(Field field) {
		Class<?> ftype = field.getType();
		
		List <Class<?>> classHeirarchy = new ArrayList<Class<?>>();
		classHeirarchy.add(ftype);
		Class<?> c = ftype.getSuperclass();
		while (!c.isAssignableFrom(org.seleniumng.driver.GuiControl.class) ){	
			
			classHeirarchy.add(c);
			c = c.getSuperclass();
		}
		
		Config confControl = pageConf.getConfig(field.getName());
		Constructor<?> constructor = null;
//		for (int i = classHeirarchy.size()-1; i>0; i--){
//			confControl = loadPageConfig(classHeirarchy.get(i-1)).withFallback(confControl);
//		}
		
		try {
			constructor = ftype.getConstructor(new Class[]{Config.class});
			Object o = constructor.newInstance(confControl);
			return o;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	protected List<Field> listAllFields(/*Object obj*/) {
	    List<Field> fieldList = new ArrayList<Field>();
	    Class<?> tmpClass = this.getClass();
	    while (tmpClass != null) {
	    	List <Field> listControls = new ArrayList<Field>();
	    	for (Field field: tmpClass .getDeclaredFields()){
    			if ( org.seleniumng.driver.GuiControl.class.isAssignableFrom(field.getType())){
	    			listControls.add(field);	
	    		}
	    	}
	        fieldList.addAll(listControls);
	        tmpClass = tmpClass .getSuperclass();
	    }
	    return fieldList;
	}
		
	protected Config loadPageConfig (Class<?> class1){
		String langPath = tafConfig.getString("language");
		String langDefault = tafConfig.hasPath("language.fallback")?tafConfig.getString("language.fallback"): "en_us"; 
		String propFile = class1.getSimpleName()+ ".conf";
		URL u_lang = class1.getResource(langPath + "/"+propFile);
		URL u_langFallback = class1.getResource(langDefault + "/"+propFile);
		Config c_lang = null, c_fallback = null;
		try {
			c_lang = ConfigFactory.parseURL(u_lang);
		} catch (Exception e){
			System.out.println("Got Exception trying to load : "+ u_lang );
			System.exit(1);
		}
		try {
			c_fallback = ConfigFactory.parseURL(u_langFallback );
		} catch (Exception e){
			System.out.println("Got Exception trying to load : "+ u_langFallback );
		}
		Config cf = c_lang.withFallback(c_fallback);
		return cf;
	}
	
}
