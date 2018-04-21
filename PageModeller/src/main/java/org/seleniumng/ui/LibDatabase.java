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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map.Entry;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.SelectField;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import com.google.gson.Gson;

import db.jooq.generated.automationDb.*;
import db.jooq.generated.automationDb.tables.records.ExtendedpropsRecord;
import db.jooq.generated.automationDb.tables.records.GuimapRecord;
import db.jooq.generated.automationDb.tables.records.PropertiesRecord;

import static db.jooq.generated.automationDb.Automation.AUTOMATION;
import static db.jooq.generated.automationDb.tables.Guimap.*;
import static db.jooq.generated.automationDb.tables.Pages.*;
import static db.jooq.generated.automationDb.tables.Types.*;
import static db.jooq.generated.automationDb.tables.Propsview.*;
import static db.jooq.generated.automationDb.tables.Extendedpropsview.*;
import static db.jooq.generated.automationDb.tables.Properties.*;
import static db.jooq.generated.automationDb.tables.Extendedprops.*;

import static org.seleniumng.utils.TAFConfig.*;

public class LibDatabase {

	private static List<String> allTables = getTableList();

	public static void main(String[] args) {

		LinkedHashMap<String, String> parammap = new LinkedHashMap<String, String>();
		parammap.put("EXPROP1", "showControlId");
		parammap.put("EXPROP2", "options");
		System.out.println(new Gson().toJson(parammap));
		System.out.println();
	}

	public static List<String> getTableFields(String tableName) {

		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		Table<?> result = null;
		List<Table<?>> tables = Automation.AUTOMATION.getTables();

		for (Table<?> t : tables) {
			if (t.getName().toUpperCase().equals(tableName.toUpperCase())) {
				result = t;
				break;
			}
		}
		// TODO: validate and throw error if result is still null;
		org.jooq.Field<?>[] fieldList = result.fields();

		List<String> returnList = new ArrayList<String>();
		for (org.jooq.Field<?> tf : fieldList) {
			returnList.add(tf.getName());
		}

		return returnList;
	}

	public static void insertGuiMap(String pageName,
			LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap) {
		try {
			for (Entry<String, LinkedHashMap<String, String>> row : cleanParamMap.entrySet()) {
				LinkedHashMap<String, String> fieldMap = row.getValue();
				Set<String> keys = fieldMap.keySet();

				List<TableField<GuimapRecord, ?>> guimapFields = new ArrayList<TableField<GuimapRecord, ?>>();
				List<Object> guimapValues = new ArrayList<Object>();
				guimapFields.add(GUIMAP.PAGEID);

				Integer pageId = DbManager.getOpenContext().select(PAGES.PAGEID).from(PAGES)
						.where(PAGES.PAGENAME.eq(pageName)).execute();
				guimapValues.add(pageId);

				List<TableField<PropertiesRecord, ?>> propertiesFields = new ArrayList<TableField<PropertiesRecord, ?>>();
				List<Object> propertiesValues = new ArrayList<Object>();

				List<TableField<ExtendedpropsRecord, ?>> expropertiesFields = new ArrayList<TableField<ExtendedpropsRecord, ?>>();
				List<Object> expropertiesValues = new ArrayList<Object>();
				for (String key : keys) {
					if (GUIMAP.field(key) != null) {
						guimapFields.add((TableField<GuimapRecord, ?>) GUIMAP.field(key));
						guimapValues.add(fieldMap.get(key));
					} else if (PROPERTIES.field(key) != null) {
						propertiesFields.add((TableField<PropertiesRecord, ?>) PROPERTIES.field(key));
						propertiesValues.add((Object) fieldMap.get(key));
					} else if (EXTENDEDPROPS.field(key) != null) {
						expropertiesFields.add((TableField<ExtendedpropsRecord, ?>) EXTENDEDPROPS.field(key));
						expropertiesValues.add(fieldMap.get(key));
					}
				}

				InsertValuesStepN<?> insertSetStepGuiMap = DbManager.getOpenContext().insertInto(GUIMAP, guimapFields);
				insertSetStepGuiMap.values(guimapValues);
				Result<?> x = insertSetStepGuiMap.returning(GUIMAP.GUIMAPID).fetch();
				Integer guiMapId = x.getValue(0, GUIMAP.GUIMAPID);

				propertiesFields.add(PROPERTIES.GUIMAPID);
				propertiesValues.add(guiMapId);
				String locatorValue = row.getValue().get("LOCATORVALUE");
				propertiesFields.add(PROPERTIES.LOCATORTYPE);
				String locatorType = (locatorValue.startsWith("/")) ? "XPATH" : "ID";
				propertiesValues.add(locatorType);

				InsertValuesStepN<?> insertSetStepProperties = DbManager.getOpenContext().insertInto(PROPERTIES,
						propertiesFields);
				insertSetStepProperties.values(propertiesValues).execute();

				expropertiesFields.add(EXTENDEDPROPS.GUIMAPID);
				expropertiesValues.add(guiMapId);
				InsertValuesStepN<?> insertSetStepExtendedProps = DbManager.getOpenContext().insertInto(EXTENDEDPROPS,
						expropertiesFields);
				insertSetStepExtendedProps.values(expropertiesValues).execute();

			}

			System.out.println("done");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateGuiMap(String pageName,
			LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap) {
		try {
			for (Entry<String, LinkedHashMap<String, String>> row : cleanParamMap.entrySet()) {
				Boolean isInsert = false;
				LinkedHashMap<String, String> fieldMap = row.getValue();
				Set<String> keys = fieldMap.keySet();

				List<TableField<?, ?>> guimapFields = new ArrayList<TableField<?, ?>>();
				List<Object> guimapValues = new ArrayList<Object>();

				Integer pageId = DbManager.getOpenContext().select(PAGES.PAGEID).from(PAGES)
						.where(PAGES.PAGENAME.eq(pageName)).fetchOne(PAGES.PAGEID);

				List<TableField<?, ?>> propertiesFields = new ArrayList<TableField<?, ?>>();
				List<Object> propertiesValues = new ArrayList<Object>();

				List<TableField<?, ?>> expropertiesFields = new ArrayList<TableField<?, ?>>();
				List<Object> expropertiesValues = new ArrayList<Object>();

				Integer currentGuiMapId = null;
				for (String key : keys) {

					if (key.equalsIgnoreCase("GUIMAPID")) {
						isInsert = fieldMap.get(key).equalsIgnoreCase("");
						if (!isInsert)
							currentGuiMapId = Integer.parseInt(fieldMap.get(key));
						else
							;
					} else {
						if (GUIMAP.field(key) != null) {
							guimapFields.add((TableField<GuimapRecord, ?>) GUIMAP.field(key));
							guimapValues.add(fieldMap.get(key));
						} else if (PROPERTIES.field(key) != null) {
							propertiesFields.add((TableField<PropertiesRecord, ?>) PROPERTIES.field(key));
							propertiesValues.add((Object) fieldMap.get(key));
						} else if (EXTENDEDPROPS.field(key) != null) {
							expropertiesFields.add((TableField<ExtendedpropsRecord, ?>) EXTENDEDPROPS.field(key));
							expropertiesValues.add(fieldMap.get(key));
						}
					}
				}
				if (!isInsert) {
					if (guimapFields.size() > 0) {
						UpdateSetMoreStep<GuimapRecord> updateGuiMap = (UpdateSetMoreStep<GuimapRecord>) getTableUpdateStatement(
								GUIMAP, guimapFields, guimapValues);
						updateGuiMap.where(GUIMAP.GUIMAPID.eq(currentGuiMapId)).execute();
					}
					String locatorValue = row.getValue().get("LOCATORVALUE");
					propertiesFields.add(PROPERTIES.LOCATORTYPE);
					String locatorType = (locatorValue.startsWith("/")) ? "XPATH" : "ID";
					propertiesValues.add(locatorType);

					if (propertiesFields.size() > 0) {
						UpdateSetMoreStep<PropertiesRecord> updateProperties = (UpdateSetMoreStep<PropertiesRecord>) getTableUpdateStatement(
								PROPERTIES, propertiesFields, propertiesValues);
						updateProperties.where(PROPERTIES.GUIMAPID.eq(currentGuiMapId)).execute();
					}
					if (expropertiesFields.size() > 0) {

						UpdateSetMoreStep<ExtendedpropsRecord> updateExtendedProperties = (UpdateSetMoreStep<ExtendedpropsRecord>) getTableUpdateStatement(
								EXTENDEDPROPS, expropertiesFields, expropertiesValues);
						updateExtendedProperties.where(EXTENDEDPROPS.GUIMAPID.eq(currentGuiMapId)).execute();
					}
				} else {
					guimapFields.add(GUIMAP.PAGEID);
					guimapValues.add(pageId);
					InsertValuesStepN<?> insertSetStepGuiMap = DbManager.getOpenContext().insertInto(GUIMAP,
							guimapFields);
					insertSetStepGuiMap.values(guimapValues);
					Result<?> x = insertSetStepGuiMap.returning(GUIMAP.GUIMAPID).fetch();
					currentGuiMapId = x.getValue(0, GUIMAP.GUIMAPID);

					propertiesFields.add(PROPERTIES.GUIMAPID);
					propertiesValues.add(currentGuiMapId);
					String locatorValue = row.getValue().get("LOCATORVALUE");
					propertiesFields.add(PROPERTIES.LOCATORTYPE);
					String locatorType = (locatorValue.startsWith("/")) ? "XPATH" : "ID";
					propertiesValues.add(locatorType);

					InsertValuesStepN<?> insertSetStepProperties = DbManager.getOpenContext().insertInto(PROPERTIES,
							propertiesFields);
					insertSetStepProperties.values(propertiesValues).execute();

					expropertiesFields.add(EXTENDEDPROPS.GUIMAPID);
					expropertiesValues.add(currentGuiMapId);
					InsertValuesStepN<?> insertSetStepExtendedProps = DbManager.getOpenContext()
							.insertInto(EXTENDEDPROPS, expropertiesFields);
					insertSetStepExtendedProps.values(expropertiesValues).execute();
				}
			}

			System.out.println("done");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static List<String> getTableList() {
		List<String> tables = new ArrayList<String>();
		for (TableLike<?> t : Automation.AUTOMATION.getTables()) {
			tables.add(((Table) t).getName());
		}
		return tables;
	}

	public static LinkedHashMap getAvailablePages() {
		return getKeyValues(PAGES.PAGENAME, PAGES.PAGEDESCRIPTION, PAGES);
	}

	public static LinkedHashMap getAvailableTypes() {
		return getKeyValues(TYPES.ABRV, TYPES.CLASS, TYPES);
	}

	public static LinkedHashMap getStandardTypes() {
		// LinkedHashMap oldMap = getTypes("STANDARD");
		LinkedHashMap<String, String[]> list = new LinkedHashMap<String, String[]>();
		SelectConditionStep<Record3<String, String, String>> x = DbManager.getOpenContext()
				.select(TYPES.ABRV, TYPES.CLASS, TYPES.PROPERTYMAP).from(TYPES).where(TYPES.TYPE.eq("STANDARD"));
		for (Record rec : x.fetch()) {
			String[] nestedMap = new String[2];
			nestedMap[0] = rec.get(TYPES.CLASS);
			nestedMap[1] = rec.get(TYPES.PROPERTYMAP);
			list.put(rec.get(TYPES.CLASS), nestedMap);
		}
		return list;

	}

	public static LinkedHashMap getCustomTypes() {
		return getTypes("CUSTOM");
	}

	public static LinkedHashMap getTypes(String classType) {
		LinkedHashMap<String, String[]> list = new LinkedHashMap<String, String[]>();
		SelectConditionStep<Record3<String, String, String>> x = DbManager.getOpenContext()
				.select(TYPES.ABRV, TYPES.CLASS, TYPES.PROPERTYMAP).from(TYPES).where(TYPES.TYPE.eq(classType));
		for (Record rec : x.fetch()) {
			String[] nestedMap = new String[2];
			nestedMap[0] = rec.get(TYPES.CLASS);
			nestedMap[1] = rec.get(TYPES.PROPERTYMAP);
			list.put(rec.get(TYPES.CLASS), nestedMap);
		}
		return list;
	}

	private static LinkedHashMap getKeyValues(SelectField keyField, SelectField valueField, Table table) {
		LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
		SelectJoinStep<Record2<String, String>> x = DbManager.getOpenContext().select(keyField, valueField).from(table);
		for (Record rec : x.fetch()) {

			list.put(rec.get(PAGES.PAGENAME), rec.get(PAGES.PAGEDESCRIPTION));
		}
		return list;

	}

	public static Object getUIPropsView(String tableName, String pageName) {
		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		Table<?> table = AUTOMATION.getTable(tableName.toUpperCase());
		Collection<Condition> conditions = new ArrayList<Condition>();
		SelectConditionStep<?> x = DbManager.getOpenContext().selectFrom(table)
				.where(PROPSVIEW.PAGENAME.equal(pageName));
		List<Object> returnList = new ArrayList<Object>();
		Result<?> result = x.fetch();
		List<Object> fields = new ArrayList<Object>();
		for (Field<?> f : result.fields()) {
			String fName = f.getName();
			if (!fName.equalsIgnoreCase("MAPPEDCLASS"))
				fields.add(fName);
			else
				continue;
		}
		// fields.remove(fields.indexOf("MAPPEDCLASS"));
		returnList.add(fields);

		for (Record r : result) {
			List<Object> values1 = new ArrayList<Object>();
			for (Object f : fields) {
				values1.add(r.get((String) f));
			}
			returnList.add(values1);

		}
		return returnList;
	}

	public static Object getPageExtendedProperties(Integer gId) {
		// Table<?> table = AUTOMATION.getTable(tableName.toUpperCase());
		Collection<Condition> conditions = new ArrayList<Condition>();
		SelectConditionStep<?> x = DbManager.getOpenContext().selectFrom(EXTENDEDPROPSVIEW)
				.where(EXTENDEDPROPSVIEW.GUIMAPID.equal(gId));
		List<Object> returnList = new ArrayList<Object>();
		Result<?> result = x.fetch();
		List<Object> fields = new ArrayList<Object>();
		for (Field<?> f : result.fields()) {
			fields.add(f.getName());
		}
		returnList.add(fields);
		List<Object> values = new ArrayList<Object>();
		for (Record r : result) {
			for (Field<?> f : r.fields()) {
				values.add(r.get(f));
			}
			returnList.add(values);

		}

		return returnList;
	}

	public static Object getExtendedProptypes(String tableName) {
		LinkedHashMap<String, String[]> list = new LinkedHashMap<String, String[]>();
		SelectConditionStep<Record3<String, String, String>> x = DbManager.getOpenContext()
				.select(TYPES.ABRV, TYPES.CLASS, TYPES.PROPERTYMAP).from(TYPES).where(TYPES.HASEXTENDEDPROPS.isTrue());
		for (Record rec : x.fetch()) {
			String[] nestedMap = new String[2];
			nestedMap[0] = rec.get(TYPES.CLASS);
			nestedMap[1] = rec.get(TYPES.PROPERTYMAP);
			list.put(rec.get(TYPES.CLASS), nestedMap);
		}
		return list;
	}

	public static UpdateSetMoreStep<?> getTableUpdateStatement(TableImpl<?> table, List<TableField<?, ?>> tableFields,
			List<Object> values) {

		UpdateSetMoreStep<?> updateStatement = (UpdateSetMoreStep<?>) DbManager.getOpenContext().update(table);// .set(f,
		// DSL.cast(values.get(0), t));
		for (Integer i = 0; i < tableFields.size(); i++) {
			Field<?> f = table.field(tableFields.get(i));
			;
			DataType t = f.getDataType();
			updateStatement = updateStatement.set(f, DSL.cast(values.get(i), t));
		}
		return updateStatement;
	}

	public static LinkedHashMap<String, LinkedHashMap<String, String>> getPageGuiMapData(String webPage) {
		LinkedHashMap<String, LinkedHashMap<String, String>> pageData = new LinkedHashMap<String, LinkedHashMap<String, String>>();

		SelectConditionStep<Record3<String, String, String>> selectStatement = DbManager.getOpenContext()
				.select(PROPSVIEW.CONTROLNAME, PROPSVIEW.MAPPEDCLASS, PROPSVIEW.STANDARDCLASS).from(PROPSVIEW)
				.where(PROPSVIEW.PAGENAME.equal(webPage));

		for (Record r : selectStatement.fetch()) {
			LinkedHashMap<String, String> classmap = new LinkedHashMap<String, String>();
			classmap.put("standardClass", r.get(PROPSVIEW.STANDARDCLASS));
			classmap.put("customClass", r.get(PROPSVIEW.MAPPEDCLASS));
			pageData.put(r.get(PROPSVIEW.CONTROLNAME), classmap);
		}
		return pageData;
	}

	public static String getClassAbrv(String string) {
		Record1<String> r = DbManager.getOpenContext().select(TYPES.ABRV).from(TYPES).where(TYPES.CLASS.equal(string))
				.fetchOne();
		return r.get(TYPES.ABRV);
	}

	public static HashMap <String,String> getPageHeirarchy() {
		Select<?> selectStatement  = DbManager.getOpenContext().select(PAGES.PAGEID, PAGES.PARENTID).from(PAGES);
		HashMap<String,String> pageHeirarchy = new HashMap<String,String>();
		for (Record r : selectStatement.fetch()) {
			String pageName = DbManager.getOpenContext().select(PAGES.PAGENAME).from(PAGES).where(PAGES.PAGEID.eq(r.get(PAGES.PAGEID))).fetchOne().get(PAGES.PAGENAME);
			String parentName = DbManager.getOpenContext().select(PAGES.PAGENAME).from(PAGES).where(PAGES.PAGEID.eq(r.get(PAGES.PARENTID))).fetchOne().get(PAGES.PAGENAME);
			pageHeirarchy.put(pageName, parentName);
			
		}
		return pageHeirarchy;
	}

}
