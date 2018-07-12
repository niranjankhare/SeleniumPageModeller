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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record16;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.SelectField;
import org.jooq.SelectJoinStep;
import org.jooq.SelectWhereStep;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.jooq.generated.automationDb.*;
import db.jooq.generated.automationDb.tables.records.ExtendedpropsRecord;
import db.jooq.generated.automationDb.tables.records.GuimapRecord;
import db.jooq.generated.automationDb.tables.records.PagesRecord;
import db.jooq.generated.automationDb.tables.records.PropertiesRecord;

import static db.jooq.generated.automationDb.Automation.AUTOMATION;
import static db.jooq.generated.automationDb.tables.Guimap.*;
import static db.jooq.generated.automationDb.tables.Pages.*;
import static db.jooq.generated.automationDb.tables.Types.*;
import static db.jooq.generated.automationDb.tables.Propsview.*;
import static db.jooq.generated.automationDb.tables.Extendedpropsview.*;
import static db.jooq.generated.automationDb.tables.Properties.*;
import static db.jooq.generated.automationDb.tables.Extendedprops.*;
import static db.jooq.generated.automationDb.tables.Propwriterview.*;

public class LibDatabase {

	private static List<String> allTables = getTableList();

	public static void main(String[] args) {

		String x = Utils.getScriptResource(LibDatabase.class, "DATABASE.sql");
		System.out.println(x);
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

	public static void insertUpdatePages(LinkedHashMap<String, LinkedHashMap<String, String>> postParamMap) {

		try {
			List<Integer> pagesToDelete= new ArrayList<Integer>();
			for (Entry<String, LinkedHashMap<String, String>> row : postParamMap.entrySet()) { // for
																								// 1
				Boolean isInsert = false;
				LinkedHashMap<String, String> fieldMap = row.getValue();
				Set<String> keys = fieldMap.keySet();

				List<TableField<?, ?>> pagesFields = new ArrayList<TableField<?, ?>>();
				List<Object> pagesValues = new ArrayList<Object>();
			
				Integer pageId = null;
				for (String key : keys) {
					if (key.equalsIgnoreCase("delete")){
						pagesToDelete.add(Integer.parseInt(fieldMap.get(key)));
						continue;
					}
					if (key.equalsIgnoreCase("PAGEID")) {
						isInsert = fieldMap.get(key).equalsIgnoreCase("");
						if (!isInsert)
							pageId = Integer.parseInt(fieldMap.get(key));
						else
							continue;
					} else {
						pagesFields.add((TableField<PagesRecord, ?>) PAGES.field(key));
						if (key.equalsIgnoreCase(PAGES.PARENTID.getName())){
							if (fieldMap.get(key).equals("")|| fieldMap.get(key).equals("_blank")){
								pagesValues.add(null);
								// over to next value
								continue;
							}  
						} 
						pagesValues.add(fieldMap.get(key));
					}
				}
				if (!isInsert) {
					if (pagesFields.size() > 0) {
						UpdateSetMoreStep<PagesRecord> updateGuiMap = (UpdateSetMoreStep<PagesRecord>) getTableUpdateStatement(
								PAGES, pagesFields, pagesValues);
						updateGuiMap.where(PAGES.PAGEID.eq(pageId)).execute();
					}
				} else {
					InsertValuesStepN<?> insertSetStepGuiMap = DbManager.getOpenContext().insertInto(PAGES,
							pagesFields);
					insertSetStepGuiMap.values(pagesValues);
					Result<?> x = insertSetStepGuiMap.returning(PAGES.PAGEID).fetch();
					pageId = x.getValue(0, PAGES.PAGEID);
				}

			}
			DbManager.getOpenContext().delete(PAGES).where(PAGES.PAGEID.in(pagesToDelete)).execute();
		} catch (Exception e){
			System.out.println("Some problem updating database:");
			e.printStackTrace();
		}

		System.out.println("done");

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
							if (key.equals("MAPPEDCLASS")) {
								propertiesValues.add((Object) "(No Maping)");
							} else
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
	
	public static LinkedHashMap getAvailablePageIds() {
		LinkedHashMap<String, String> pageData = getKeyValues(PAGES.PAGEID, PAGES.PAGENAME, PAGES);
		LinkedHashMap <String, Object> toReturn = new LinkedHashMap<String,Object>();
		for (String key : pageData.keySet()){
			toReturn.put(key, Arrays.asList(pageData.get(key), null));
		}
		return toReturn;
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

	private static LinkedHashMap getTypes(String classType) {
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
			list.put(rec.get(keyField.getName()).toString(), rec.get(valueField.getName()).toString());
		}
		return list;
	}

	public static Object getUIPropsView(String tableName, String pageName) {
		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		Table<?> table = AUTOMATION.getTable(tableName.toUpperCase());
		Collection<Condition> conditions = new ArrayList<Condition>();
		SelectConditionStep<?> x = DbManager.getOpenContext().selectFrom(table).where(PROPSVIEW.PAGENAME.eq(pageName));
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

		SelectConditionStep<Record4<String, String, String,String>> selectStatement = DbManager.getOpenContext()
				.select(PROPSVIEW.CONTROLNAME, PROPSVIEW.MAPPEDCLASS, PROPSVIEW.STANDARDCLASS,TYPES.ABRV).from(PROPSVIEW).innerJoin(TYPES).on(PROPSVIEW.STANDARDCLASS.eq(TYPES.CLASS))
				.where(PROPSVIEW.PAGENAME.equal(webPage));

		for (Record r : selectStatement.fetch()) {
			LinkedHashMap<String, String> classmap = new LinkedHashMap<String, String>();
			classmap.put("standardClass", r.get(PROPSVIEW.STANDARDCLASS));
			classmap.put("customClass", r.get(PROPSVIEW.MAPPEDCLASS));
			classmap.put("typeAbrv", r.get(TYPES.ABRV));
			pageData.put(r.get(PROPSVIEW.CONTROLNAME), classmap);
		}
		return pageData;
	}
	public static List<Object> getPageGuiMapData2(String webPage) {
		Object obj1 = getPageGuiMapData(webPage);
		Object obj2 = getPageGuiPropertyData(webPage);
		return Arrays.asList(obj1,obj2);
	}
	public static Map<String, Object> getPageGuiPropertyData(String webPage) {
		Map<String, Object> pageData = new LinkedHashMap<String, Object>();

		SelectConditionStep<Record16<Integer, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String>> selectProperties = DbManager
				.getOpenContext()
				.select(PROPWRITERVIEW.GUIMAPID, PROPWRITERVIEW.ABRV, PROPWRITERVIEW.CONTROLNAME,
						PROPWRITERVIEW.CONTROLDESCRIPTION, PROPWRITERVIEW.LOCATORTYPE, PROPWRITERVIEW.LOCATORVALUE,
						PROPWRITERVIEW.MAPPEDCLASS, PROPWRITERVIEW.EXPROP1, PROPWRITERVIEW.EXPROP2,
						PROPWRITERVIEW.EXPROP3, PROPWRITERVIEW.EXPROP4, PROPWRITERVIEW.EXPROP5, PROPWRITERVIEW.EXPROP6,
						PROPWRITERVIEW.EXPROP7, PROPWRITERVIEW.EXPROP8, PROPWRITERVIEW.EXPROP9)
				.from(PROPWRITERVIEW).where(PROPWRITERVIEW.PAGENAME.equal(webPage));

		Result<Record2<String, String>> result = DbManager.getOpenContext().select(TYPES.CLASS, TYPES.PROPERTYMAP)
				.from(TYPES).where(TYPES.HASEXTENDEDPROPS.isTrue()).fetch();
		Map<String, List<String>> xPropertyMap = result.intoGroups(TYPES.CLASS, TYPES.PROPERTYMAP);
		for (Record r : selectProperties.fetch()) {
			HashMap<String, String> exmap = null;
			if (r.get(PROPWRITERVIEW.MAPPEDCLASS) != null
					&& !r.get(PROPWRITERVIEW.MAPPEDCLASS).equalsIgnoreCase("(No Maping)")) {
				String jSon = xPropertyMap.get(r.get(PROPWRITERVIEW.MAPPEDCLASS)).get(0);
				exmap = new Gson().fromJson(jSon, new TypeToken<HashMap<String, String>>() {
				}.getType());
			}
			LinkedHashMap<String, Object> propertyMap = new LinkedHashMap<String, Object>();

			propertyMap.put("CONTROLDESCRIPTION", r.get(PROPSVIEW.CONTROLDESCRIPTION));
			propertyMap.put("LOCATORTYPE", r.get(PROPSVIEW.LOCATORTYPE));
			propertyMap.put("LOCATORVALUE", r.get(PROPSVIEW.LOCATORVALUE));
			if (exmap != null) {
				for (String xf : exmap.keySet()) {
					propertyMap.put(exmap.get(xf), r.get(xf));
				}
			}
			propertyMap.put("LOCATORVALUE", r.get(PROPSVIEW.LOCATORVALUE));
			Object o = (Object) propertyMap;
			pageData.put(r.get(PROPWRITERVIEW.ABRV) + r.get(PROPWRITERVIEW.CONTROLNAME).toString(), o);

		}
		return pageData;
	}

	public static String getClassAbrv(String string) {
		Record1<String> r = DbManager.getOpenContext().select(TYPES.ABRV).from(TYPES).where(TYPES.CLASS.equal(string))
				.fetchOne();
		return r.get(TYPES.ABRV);
	}

	public static List<HashMap<String, String>> getPageHeirarchy(Integer... pageId) {
		List<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>();
		Select<?> selectStatement = null;
		if (pageId.length == 0)
			selectStatement = DbManager.getOpenContext().select(PAGES.PAGEID, PAGES.PARENTID).from(PAGES)
					.where(PAGES.PARENTID.isNull());
		else
			selectStatement = DbManager.getOpenContext().select(PAGES.PAGEID, PAGES.PARENTID).from(PAGES)
					.where(PAGES.PARENTID.in(pageId));
		HashMap<String, String> pageHeirarchy = new HashMap<String, String>();
		for (Record r : selectStatement.fetch()) {
			Integer pId = r.get(PAGES.PAGEID);
			Integer prId = r.get(PAGES.PARENTID);
			String pageName = DbManager.getOpenContext().select(PAGES.PAGENAME).from(PAGES).where(PAGES.PAGEID.eq(pId))
					.fetchOne().get(PAGES.PAGENAME);
			Record1<String> s = DbManager.getOpenContext().select(PAGES.PAGENAME).from(PAGES)
					.where(PAGES.PAGEID.eq(prId)).fetchOne();
			String parentName = (s == null) ? null : s.get(PAGES.PAGENAME);
			pageHeirarchy.put(pageName, parentName);

		}
		returnList.add(pageHeirarchy);
		List<Integer> a = selectStatement.fetch().getValues(PAGES.PAGEID, Integer.class);
		if (a.size() > 0) {
			Integer list2[] = new Integer[a.size()];
			returnList.addAll(getPageHeirarchy((Integer[]) a.toArray(list2)));
		}
		return returnList;

	}

	public static Object getTableData(String tableName) {
		SelectWhereStep<?> pages = DbManager.getOpenContext().selectFrom(AUTOMATION.getTable(tableName.toUpperCase()));
		List<Object> returnList = new ArrayList<Object>();
		Result<?> result = pages.fetch();
		List<Object> fields = new ArrayList<Object>();
		for (Field<?> f : result.fields()) {
			fields.add(f.getName());
		}
		returnList.add(fields);
		for (Record r : result) {
			List<Object> values = new ArrayList<Object>();
			for (Object f : fields) {
				values.add(r.get((String) f));
			}
			returnList.add(values);
		}
		return returnList;
	}

}
