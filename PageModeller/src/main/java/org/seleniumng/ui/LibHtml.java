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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class LibHtml {

    private static String addRowScriptTemplateN = Utils.getScriptResource(LibHtml.class,"addRows.js");

    public static void main(String[] args) {

    }

    private static String sq(String str) {
        return "'" + str + "'";
    }
 @Deprecated
    public static String getTableEntryFormXXX(String tableName, String whereColumn, String hasValue) {
        // TODO: Get list of columns for the view/table
        tableName.replaceAll(tableName, tableName.toLowerCase());
        List<String> fieldsList = LibDatabase.getTableFields(tableName);
        if (whereColumn != null)
            fieldsList.remove(whereColumn);

        Document html = Jsoup.parse("<html></html>");

        Element scriptElement = new Element("script").text(addRowScriptTemplateN);

        Element table = new Element("table").attr("id", tableName);
        Element headerRow = new Element("tr");

        // Add columns to the displayed table as header row
        for (String field : fieldsList) {
            headerRow.appendElement("th").text(field);
        }

        Element tbody = new Element("tbody");

        table.appendChild(tbody);
        tbody.appendChild(headerRow);
        // Form Submit elements:
        Element elTableName = new Element("input");
        elTableName.attr("type", "hidden");
        elTableName.attr("id", "tableName");
        elTableName.attr("name", "tableName");
        elTableName.attr("value", tableName);

        Element elPageName = new Element("input");
        elPageName.attr("type", "hidden");
        elPageName.attr("id", "pageName");
        elPageName.attr("name", "pageName");
        elPageName.attr("value", hasValue);

        Element addMore = new Element("input");
        addMore.attr("type", "button");
        addMore.attr("id", "addRow");
        addMore.attr("onclick", "add_row();");
        addMore.attr("value", "Add row");

        Element submit = new Element("input");
        submit.attr("type", "submit");
        submit.attr("id", "submit");
        submit.attr("value", "Go!");

        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/updateTable");
        form.appendChild(table);
        form.appendChild(addMore);
        form.appendChild(elTableName);
        if (whereColumn != null)
            form.appendChild(elPageName);
        form.appendChild(submit);
        html.body().before(scriptElement);
        html.body().appendChild(form);
        html.body().attr("onload", "add_row()");

        return Parser.unescapeEntities(html.toString(), false);
    }

    public static String getPageProvisioningForm(/*String pageName, String operation*/) {
        String targetTable = "PAGES";

        targetTable.replaceAll(targetTable, targetTable.toUpperCase());
        List<String> mainFieldsList = LibDatabase.getTableFields(targetTable);

        Element table = new Element("table").attr("id", targetTable);
        Element headerRow = new Element("tr").attr("id", "headerRow").attr("style", "visibility:visible;");

        String scriptBlock = addRowScriptTemplateN;

        Document html = Jsoup.parse("<html></html>");

        Element scriptElement = new Element("script").text(scriptBlock);
        Element tbody = new Element("tbody");
        Element thead = new Element("thead");
        
        table.appendChild(thead);
        thead.appendChild(headerRow);
        table.appendChild(tbody);

        // Form Submit elements:
        Element elTableName = new Element("input");
        elTableName.attr("type", "hidden");
        elTableName.attr("id", "tableName");
        elTableName.attr("name", "tableName");
        elTableName.attr("value", targetTable);

        Element addMore = new Element("input");
        addMore.attr("type", "button");
        addMore.attr("id", "addRow");
        addMore.attr("onclick", "addTableRows([],'PAGENAME','PARENTID' );");
        addMore.attr("value", "Add row");

        Element submit = new Element("input");
        submit.attr("type", "submit");
        submit.attr("id", "submit");
        submit.attr("onclick", "removeSelectCheckboxes();");
        submit.attr("value", "Go!");
        Element parentPageDiv = new Element("div").attr("id", "formMainDiv").attr("style", "visibility=inherit;");
        parentPageDiv.appendChild(table);
        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/insertPageGui");
        form.appendChild(parentPageDiv);
        form.appendChild(addMore);
        form.appendChild(elTableName);
//        form.appendChild(elPageName);
//        form.appendChild( elOperation);
        form.appendChild(submit);
        html.body().before(scriptElement);
        html.body().appendChild(form);
        html.body().attr("onload", "loadTable('PAGES','PAGENAME');");

        return Parser.unescapeEntities(html.toString(), false);

    }
@Deprecated
    private static String getFieldsArray(List<String> fieldList) {
        String replaceText = "";
        for (String field : fieldList) {
            replaceText = replaceText + sq(field) + ",";
        }
        replaceText = replaceText.replaceAll(",$", "");
        return replaceText;
    }
@Deprecated
    private static Element getTextArea(String columnName) {

        String idNameStr = "Row" + "'+rowCount+'" + "." + columnName;
        Element textArea = new Element("textarea");
        textArea.attr("name", idNameStr).attr("placeholder", columnName).attr("id", idNameStr).attr("style",
                "resize: none; width:100%;");
        return textArea;

    }

    public static String getWelcomeForm() {
        Document html = Jsoup.parse("<html></html>");
        html.body().appendElement("h2").text("Welcome to automation GUI map maintenance portal!");

        Element selectPage = getPageSelect();         
        Element enterNew = new Element("input").attr("name", "oper").attr("type", "radio").attr("value", "new").text("Add New Page");
        Element update = new Element("input").attr("name", "oper").attr("type", "radio").attr("value", "update").attr("checked", "checked").text("Update existing Page");
        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/fetchPage");
        form.appendChild(selectPage);
        form.appendChild(enterNew);
        form.appendChild(update);
        form.appendChild(new Element("input").attr("type", "submit").attr("value", "Fetch Entry Form"));
        html.body().appendChild(form);
        return Parser.unescapeEntities(html.toString(), false);
    }

    private static Element getPageSelect() {
    	Element selectElement = new Element("select").attr("name", "pageName");
        LinkedHashMap<String, String> availablePages = LibDatabase.getAvailablePages();
        return addOptionsToSelect(selectElement, availablePages);
    }

    private static Element addOptionsToSelect(Element selectElement, LinkedHashMap<String, String> map) {
        for (Entry<String, String> entry : map.entrySet()) {
            selectElement.appendChild(new Element("option").val(entry.getKey()).text(entry.getValue()));
        }
        return selectElement;
    }



	public static String getPageUpdateGUIForm(String pageName/*, String operxationc*/) {
        String mainPropertiesView = "PROPSVIEW";
       
        mainPropertiesView.replaceAll(mainPropertiesView, mainPropertiesView.toLowerCase());
        List<String> mainFieldsList = LibDatabase.getTableFields(mainPropertiesView);

        Element elTableName = new Element("input");
        elTableName.attr("type", "hidden");
        elTableName.attr("id", "tableName");
        elTableName.attr("name", "tableName");
        elTableName.attr("value", mainPropertiesView);
        
        Element table = new Element("table").attr("id", mainPropertiesView);
        Element headerRow = new Element("tr").attr("id", "headerRow").attr("style", "visibility:visible;");

        String scriptBlock = addRowScriptTemplateN;

        Document html = Jsoup.parse("<html></html>");

        Element scriptElement = new Element("script").text(scriptBlock);

        Element tbody = new Element("tbody");
        Element thead = new Element("thead");
        
        table.appendChild(thead);
        thead.appendChild(headerRow);
        table.appendChild(tbody);

        Element elPageName = new Element("input");
        elPageName.attr("type", "hidden");
        elPageName.attr("id", "pageName");
        elPageName.attr("name", "pageName");
        elPageName.attr("value", pageName);

        Element addMore = new Element("input");
        addMore.attr("type", "button");
        addMore.attr("id", "addRow");
        addMore.attr("onclick", "addTableRows([],'CONTROLNAME','STANDARDCLASS' );");
        addMore.attr("value", "Add row");
        
        Element submit = new Element("input");
        submit.attr("type", "submit");
        submit.attr("id", "submit");
        submit.attr("value", "Go!");
        Element parentPageDiv = new Element("div").attr("id", "formMainDiv").attr("style", "visibility=inherit;");
        parentPageDiv.appendChild(table);
        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/updatePageGui");
        form.appendChild(parentPageDiv);
        form.appendChild(elTableName);
        form.appendChild(elPageName);
//        form.appendChild( elOperation);
        form.appendChild(addMore);
        form.appendChild(submit);
        html.body().before(scriptElement);
        html.body().appendChild(form);
//        html.body().attr("onload", "add_HeaderRow('PROPSVIEW',)");
        html.body().attr("onload", "var pageName=document.getElementById('pageName').value;loadTable('PROPSVIEW','CONTROLNAME',pageName);");

        return Parser.unescapeEntities(html.toString(), false);

	}
}
