package org.seleniumng.codegen;

import static db.jooq.generated.automationDb.tables.Pages.PAGES;
import static db.jooq.generated.automationDb.tables.Propsview.PROPSVIEW;
import static db.jooq.generated.automationDb.tables.Propwriterview.PROPWRITERVIEW;
import static db.jooq.generated.automationDb.tables.Types.TYPES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;

import org.jooq.Record17;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectWhereStep;
import org.seleniumng.ui.DbManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.jooq.generated.automationDb.tables.records.PagesRecord;

public class CodegenDatabase {
	private static DSLContext dslContext = DbManager.getOpenContext();
	private static SelectWhereStep<PagesRecord> pages = dslContext.selectFrom(PAGES);
	private static Result<PagesRecord> pagesResult = pages.fetch();
	private static Map<Integer, Result<PagesRecord>> pagesByParentId= pagesResult.intoGroups(PAGES.PARENTID);
	private static Map<Integer, Result<PagesRecord>> pagesByPageId= pagesResult.intoGroups(PAGES.PAGEID);
	
	private static SelectOnConditionStep<Record5<String,String, String, String, String>> mapData =  dslContext
			.select(PROPSVIEW.PAGENAME,PROPSVIEW.CONTROLNAME, PROPSVIEW.MAPPEDCLASS, PROPSVIEW.STANDARDCLASS,TYPES.ABRV).from(PROPSVIEW).innerJoin(TYPES).on(PROPSVIEW.STANDARDCLASS.eq(TYPES.CLASS));
	private static Result<Record5<String,String, String, String, String>> resultMapData = mapData.fetch();
	private static Map<String, Result<Record5<String,String, String, String, String>>> mapDataByPageName = resultMapData.intoGroups(PROPSVIEW.PAGENAME);

	private static SelectJoinStep<Record17<String, Integer, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String>> propertyData = dslContext
			.select(PROPWRITERVIEW.PAGENAME,PROPWRITERVIEW.GUIMAPID, PROPWRITERVIEW.ABRV, PROPWRITERVIEW.CONTROLNAME,
					PROPWRITERVIEW.CONTROLDESCRIPTION, PROPWRITERVIEW.LOCATORTYPE, PROPWRITERVIEW.LOCATORVALUE,
					PROPWRITERVIEW.MAPPEDCLASS, PROPWRITERVIEW.EXPROP1, PROPWRITERVIEW.EXPROP2,
					PROPWRITERVIEW.EXPROP3, PROPWRITERVIEW.EXPROP4, PROPWRITERVIEW.EXPROP5, PROPWRITERVIEW.EXPROP6,
					PROPWRITERVIEW.EXPROP7, PROPWRITERVIEW.EXPROP8, PROPWRITERVIEW.EXPROP9)
			.from(PROPWRITERVIEW);
	private static Result<Record17<String, Integer, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String>> resultPropertyData = propertyData.fetch(); 
	private static Map<String, Result<Record17<String, Integer, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String>>> propertyDataByPageName = resultPropertyData.intoGroups(PROPWRITERVIEW.PAGENAME);
	

	private static Result<Record2<String, String>> result = dslContext.select(TYPES.CLASS, TYPES.PROPERTYMAP)
			.from(TYPES).where(TYPES.HASEXTENDEDPROPS.isTrue()).fetch();
	private static Map<String, List<String>> typeMap = result.intoGroups(TYPES.CLASS, TYPES.PROPERTYMAP);
	
	public static List<HashMap<String, String>> getPageHeirarchy(Integer... pageId) {
		List<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>();
		List<Record> selectedRecords = null;
		if (pageId.length == 0)
			selectedRecords = getRecords (pagesByParentId , new Integer[] {-1});
		else 
			selectedRecords = getRecords (pagesByParentId , pageId);
		HashMap<String, String> pageHeirarchy = new HashMap<String, String>();
		for (Record r : selectedRecords) {
			Integer pId = r.get(PAGES.PAGEID);
			Integer prId = r.get(PAGES.PARENTID);
			String pageName = getRecords (pagesByPageId ,  new Integer[]{pId}).get(0).get(PAGES.PAGENAME);
			List<Record> parentRecord = getRecords (pagesByPageId, new Integer[]{prId});
			Record s =parentRecord.size()==0? null:(Record) parentRecord.get(0); 
			String parentName = (s == null) ? null : s.get(PAGES.PAGENAME);
			pageHeirarchy.put(pageName, parentName);

		}
		returnList.add(pageHeirarchy);
		List<Integer> a = new ArrayList<Integer>();
		for (Record r:selectedRecords){
			a.add(r.get(PAGES.PAGEID));
		} 
		if (a.size() > 0) {
			Integer list2[] = new Integer[a.size()];
			returnList.addAll(getPageHeirarchy((Integer[]) a.toArray(list2)));
		}
		return returnList;
	}
	
private static <K,R extends Record> List<Record> getRecords(Map<K, Result<R>> pagesMapByParentId, K[] pageId) {
		 List<Record> toReturn = new ArrayList<Record>();
		 for (int i=0; i <pageId.length; i++){
			 if (pagesMapByParentId.containsKey(pageId[i]))
				 toReturn.addAll(pagesMapByParentId.get(pageId[i]));
		 }
		return  toReturn;
	}

	public static LinkedHashMap<String, LinkedHashMap<String, String>> getPageGuiMapData(String webPage) {
		LinkedHashMap<String, LinkedHashMap<String, String>> pageData = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		for (Record r : getRecords(mapDataByPageName, new String[]{webPage})) {
			LinkedHashMap<String, String> classmap = new LinkedHashMap<String, String>();
			classmap.put("standardClass", r.get(PROPSVIEW.STANDARDCLASS));
			classmap.put("customClass", r.get(PROPSVIEW.MAPPEDCLASS));
			classmap.put("typeAbrv", r.get(TYPES.ABRV));
			pageData.put(r.get(PROPSVIEW.CONTROLNAME), classmap);
		}
		return pageData;
	}
	

	public static List<Object> getPageGuiMapData2(String webPage) 
	{
		Object obj1 = getPageGuiMapData(webPage);
		Object obj2 = getPageGuiPropertyData(webPage);
		return Arrays.asList(obj1,obj2);
	}
	public static Map<String, Object> getPageGuiPropertyData(String webPage) {
		Map<String, Object> pageData = new LinkedHashMap<String, Object>();
		for (Record r : getRecords(propertyDataByPageName, new String[]{webPage})) {
			HashMap<String, String> exmap = null;
			if (r.get(PROPWRITERVIEW.MAPPEDCLASS) != null
					&& !r.get(PROPWRITERVIEW.MAPPEDCLASS).equalsIgnoreCase("(No Maping)")) {
				String jSon = typeMap.get(r.get(PROPWRITERVIEW.MAPPEDCLASS)).get(0);
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
}
