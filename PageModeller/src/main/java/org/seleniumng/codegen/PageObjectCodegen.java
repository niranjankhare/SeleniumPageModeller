package org.seleniumng.codegen;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

import java.util.Arrays;

import org.seleniumng.controls.TextField;

public class PageObjectCodegen {

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
		String pagePackage = "heimdallSaaS.webPages";

		JCodeModel codeModel = new JCodeModel();
		JDefinedClass repositoryToCreate = codeModel._class(pagePackage + ".PageRepository");
		List<String> webPages = fetchPageList();
		for (String webPage : webPages) {
			JDefinedClass pageClassToCreate = codeModel._class(pagePackage + ".Page" + webPage);
			JClass jc = codeModel.ref(TextField.class);

			// Creating fields in the class

			JFieldVar field1 = pageClassToCreate.field(JMod.PRIVATE, jc, "foo", JExpr._null());

			repositoryToCreate.field(JMod.PUBLIC, pageClassToCreate, "pageLogin");

		}

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
		// TODO Auto-generated method stub
		return Arrays.asList("Login");
	}
}