package seleniumng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;

import org.testng.annotations.Parameters;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public interface TestDataProvider {
	public static final Logger logger = LoggerFactory.getLogger(TestDataProvider.class);

	@DataProvider
	public default Object[][] defaultDataProvider(Method methodContext) {
		return serialDataProvider(methodContext);
	}

	@DataProvider
	public default Object[][] serialDataProvider(Method testMethod) {
		return dataProvider(testMethod);
	}

	@DataProvider(parallel = true)
	public default Object[][] parallelDataProvider(Method testMethod) {
		return dataProvider(testMethod);
	}

	public default Object[][] dataProvider(Method methodContext) {
		Class<?> testClass = methodContext.getDeclaringClass();

		final URL dataFile = getDataResource(methodContext.getName());
		Boolean isDataDriven = (dataFile == null) ? false : true;

		if (isDataDriven) {
			try {
				Field runCheck = testClass.getField("runDataDriven");
				isDataDriven = (Boolean) runCheck.get(testClass);
				logger.info("But runCheck Says:" + isDataDriven);
			} catch (Exception e) {

			}
		}

		int numRecords = 0; // default
		Parameters p = methodContext.getAnnotation(org.testng.annotations.Parameters.class);
		String[] parmNames = p.value();
		Parameter[] pValues = methodContext.getParameters();

		List<Map<String, String>> dataRows = new ArrayList<Map<String, String>>();
		if (isDataDriven) {// run when Data driven
			dataRows = readTestDataFile(dataFile, "|");
			numRecords = dataRows.size();
			logger.info("Returning parameters dataset\nmethod:{},\nfile:{}",
					testClass.getName() + "." + methodContext.getName(), dataFile.getPath());

		} else {
			numRecords = 1;
			Map<String, String> optionalRecord = new LinkedHashMap<String, String>();
			String csvHeaders = "";
			for (int i = 0; i < parmNames.length; i++) {
				System.out.println(parmNames[i] + "=" + ((Optional) pValues[i].getDeclaredAnnotations()[0]).value());
				optionalRecord.put(parmNames[i], ((Optional) pValues[i].getDeclaredAnnotations()[0]).value());
				csvHeaders = parmNames[i] + "|" + csvHeaders;
			}
			logger.info("Returning pre-defined Optional parameters as dataset for method:{}" + testClass.getName() + "."
					+ methodContext.getName());
			dataRows.add(optionalRecord);
		}
		Object[][] returnObject = new Object[numRecords][parmNames.length];
		Class<?> cla[] = methodContext.getParameterTypes();
		for (int r = 0; r < dataRows.size(); r++) {
			for (int i = 0; i < parmNames.length; i++) {
				String parmValue = dataRows.get(r).get(parmNames[i]);
				Object pParam = getParamAs(parmValue, pValues[i].getParameterizedType());
				returnObject[r][i] = pParam;
			}
		}
		return returnObject;
	}

	static Object getParamAs(String parmValue, Type type) {
		Object retVal = null;
		Type rawType = null;

		rawType = (type instanceof ParameterizedType) ? ((ParameterizedType) type).getRawType() : type;

		switch (rawType.getTypeName()) {
		case "java.lang.String":
			retVal = (parmValue.equalsIgnoreCase("null")) ? null : String.valueOf(parmValue);
			break;
		case "java.lang.Integer":
			retVal = (parmValue.equalsIgnoreCase("null")) ? null : Integer.valueOf(parmValue);
			break;
		case "java.lang.Boolean":
			retVal = (parmValue.equalsIgnoreCase("null")) ? null : Boolean.valueOf(parmValue);
			break;
		case "java.lang.Double":
			retVal = (parmValue.equalsIgnoreCase("null")) ? null : Double.valueOf(parmValue);
			break;
		case "java.lang.Float":
			retVal = (parmValue.equalsIgnoreCase("null")) ? null : Float.valueOf(parmValue);
			break;
		case "java.util.List":
			if (parmValue.equalsIgnoreCase("null"))
				retVal = null;
			else {
				Type itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
				List<Object> lst = new ArrayList<Object>();
				for (String item : parmValue.split(",")) {
					lst.add(getParamAs(item, itemType));
				}
				retVal = lst;
			}
			break;
		default:
			retVal = (parmValue.equalsIgnoreCase("null")) ? null : String.valueOf(parmValue);
		}
		return retVal;
	}

	static List<Map<String, String>> readTestDataFile(final URL content, final String separator) {

		List<String> csvFileIM = null, csvFile = null;

		try {
			csvFileIM = Resources.asCharSource(content, Charsets.UTF_8).readLines();
			csvFile = new ArrayList<String>();
			for (int i = 0; i < csvFileIM.size(); i++) {
				if (!csvFileIM.get(i).startsWith("//") && !csvFileIM.get(i).startsWith("--")
						&& !csvFileIM.get(i).startsWith("##")) {
					csvFile.add(csvFileIM.get(i));
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		String[] headers = csvFile.get(0).split("\\" + separator);

		List<Map<String, String>> recordSet = new ArrayList<Map<String, String>>();

		for (int i = 1; i < csvFile.size(); i++) {
			Map<String, String> record = new HashMap<String, String>();
			String[] values = csvFile.get(i).split("\\" + separator);
			// add record only if it has the right number of parameteres
			if (headers.length != values.length) 
				continue;
			for (int j = 0; j < headers.length; j++) {
				String value = "";
				if (j < values.length) {
					value = values[j];
				}
				record.put(headers[j].trim(), value);

			}
			recordSet.add(record);
		}

		return recordSet;
	}

	URL getDataResource(String resourceName);

}
