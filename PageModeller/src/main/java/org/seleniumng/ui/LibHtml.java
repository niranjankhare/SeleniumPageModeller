package org.seleniumng.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class LibHtml {

//    private static String addRowScriptTemplate  = "function add_fields() {var rowCount = document.getElementById('__TABLENAME__').getElementsByTagName(\"tbody\")[0].rows.length;document.getElementById(\"__TABLENAME__\").insertRow(-1).innerHTML = '__ROWHTML__';} ";
/*
 <input type='radio' name=choice value=new onclick="document.getElementById('newPage').disabled=false;document.getElementById('selectPage').disabled=true">Enter new:</inpur><input type='radio' name=choice value=new onclick="document.getElementById('selectPage').disabled=false;document.getElementById('newPage').disabled=true">Update existing:</input>
<form action="">
  <input id=newPage type="text" name="newPage" value="NewPage" style=display:inherit; disabled=true></input><br>
  <select id=selectPage name="selectPage" value="female" disabled=true><option value=a>value1</option>value2<option value=b></option></select> page<br>
  <input type="radio" name="gender" value="other"> Other
</form> 
 * */
    private static String addRowScriptTemplateN = getScriptResource("addRows.js");                                                                                                                                                                                      // LibHtml.class.getResourceAsStream("");

    public static void main(String[] args) {

    }

    private static String sq(String str) {
        // TODO Auto-generated method stub
        return "'" + str + "'";
    }

    private static String getScriptResource(String resouceName) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        InputStream inputStream = LibHtml.class.getResourceAsStream(resouceName);
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            return result.toString("UTF-8");
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    // REQUIRED
    public static String getTableEntryForm(String tableName, String whereColumn, String hasValue) {
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
        // tbody.appendChild(dataRow);

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

    public static String getPageAddGUIForm(String pageName, String operation) {
        String mainPropertiesView = "propsview";

        mainPropertiesView.replaceAll(mainPropertiesView, mainPropertiesView.toLowerCase());
        List<String> mainFieldsList = LibDatabase.getTableFields(mainPropertiesView);

        Element table = new Element("table").attr("id", mainPropertiesView);
        Element headerRow = new Element("tr").attr("id", "headerRow").attr("style", "visibility:visible;");

        // Add columns to the displayed table as header row
        for (String field : mainFieldsList) {
            headerRow.appendElement("th").text(field);
        }

        headerRow.appendElement("th").text("More properties");

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
        elTableName.attr("value", mainPropertiesView);

        Element elPageName = new Element("input");
        elPageName.attr("type", "hidden");
        elPageName.attr("id", "pageName");
        elPageName.attr("name", "pageName");
        elPageName.attr("value", pageName);

        Element elOperation = new Element("input");
        elOperation.attr("type", "hidden");
        elOperation.attr("id", "oper");
        elOperation.attr("name", "oper");
        elOperation.attr("value", operation);

        Element addMore = new Element("input");
        addMore.attr("type", "button");
        addMore.attr("id", "addRow");
        addMore.attr("onclick", "add_NewRow();");
        addMore.attr("value", "Add row");

        Element submit = new Element("input");
        submit.attr("type", "submit");
        submit.attr("id", "submit");
        submit.attr("value", "Go!");
        Element parentPageDiv = new Element("div").attr("id", "formMainDiv").attr("style", "visibility=inherit;");
        parentPageDiv.appendChild(table);
        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/insertPageGui");
        form.appendChild(parentPageDiv);
        form.appendChild(addMore);
        form.appendChild(elTableName);
        form.appendChild(elPageName);
        form.appendChild( elOperation);
        form.appendChild(submit);
        html.body().before(scriptElement);
        html.body().appendChild(form);
        html.body().attr("onload", "add_NewRow()");

        return Parser.unescapeEntities(html.toString(), false);

    }

    private static String getFieldsArray(List<String> fieldList) {
        String replaceText = "";
        for (String field : fieldList) {
            replaceText = replaceText + sq(field) + ",";
        }
        replaceText = replaceText.replaceAll(",$", "");
        return replaceText;
    }

//    private static void WriteFile(String fileName, String content) {
//        File f = new File(fileName);
//
//        System.out.println(f.getName());
//        FileOutputStream fo = null;
//        if (!f.exists()) {
//            try {
//                f.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        try {
//
//            fo = new FileOutputStream(f);
//            String b = content;
//            fo.write(b.getBytes());
//            fo.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

    public static Element getTextArea(String columnName) {

        String idNameStr = "Row" + "'+rowCount+'" + "." + columnName;
        Element textArea = new Element("textarea");
        textArea.attr("name", idNameStr).attr("placeholder", columnName).attr("id", idNameStr).attr("style",
                "resize: none; width:100%;");
        return textArea;

    }

    public static String getWelcomeForm() {
        Document html = Jsoup.parse("<html></html>");
        html.body().appendElement("h2").text("Welcome to automation GUI map maintenance portal!");

        Element selectPage = new Element("select").attr("name", "pageName");
        selectPage = addAvailablePages(selectPage);
        Element enterNew = new Element("input").attr("name", "oper").attr("type", "radio").attr("value", "new").text("Add New GUI");
        Element update = new Element("input").attr("name", "oper").attr("type", "radio").attr("value", "update").attr("checked", "checked").text("Update existing GUI");
        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/fetchPage");
        form.appendChild(selectPage);
        form.appendChild(enterNew);
        form.appendChild(update);
        form.appendChild(new Element("input").attr("type", "submit").attr("value", "Fetch Entry Form"));
        html.body().appendChild(form);
        return Parser.unescapeEntities(html.toString(), false);
    }

    private static Element addAvailablePages(Element selectElement) {
        LinkedHashMap<String, String> availablePages = LibDatabase.getAvailablePages();
        return addOptionsToSelect(selectElement, availablePages);
    }

    private static Element addOptionsToSelect(Element selectElement, LinkedHashMap<String, String> map) {
        for (Entry<String, String> entry : map.entrySet()) {
            selectElement.appendChild(new Element("option").val(entry.getKey()).text(entry.getValue()));
        }
        return selectElement;
    }



	public static String getPageUpdateGUIForm(String pageName, String operation) {
        String mainPropertiesView = "propsview";
       
        mainPropertiesView.replaceAll(mainPropertiesView, mainPropertiesView.toLowerCase());
        List<String> mainFieldsList = LibDatabase.getTableFields(mainPropertiesView);

        Element elTableName = new Element("input");
        elTableName.attr("type", "hidden");
        elTableName.attr("id", "tableName");
        elTableName.attr("name", "tableName");
        elTableName.attr("value", mainPropertiesView);
        
        Element table = new Element("table").attr("id", mainPropertiesView);
        Element headerRow = new Element("tr").attr("id", "headerRow").attr("style", "visibility:visible;");

        // Add columns to the displayed table as header row
//        for (String field : mainFieldsList) {
//            headerRow.appendElement("th").text(field);
//        }
//
//        headerRow.appendElement("th").text("More properties");

        String scriptBlock = addRowScriptTemplateN;

        Document html = Jsoup.parse("<html></html>");

        Element scriptElement = new Element("script").text(scriptBlock);

        Element tbody = new Element("tbody");
        Element thead = new Element("thead");
        
        table.appendChild(thead);
        thead.appendChild(headerRow);
        table.appendChild(tbody);
//        tbody.appendChild(headerRow);
        
       

        Element elPageName = new Element("input");
        elPageName.attr("type", "hidden");
        elPageName.attr("id", "pageName");
        elPageName.attr("name", "pageName");
        elPageName.attr("value", pageName);

        Element elOperation = new Element("input");
        elOperation.attr("type", "hidden");
        elOperation.attr("id", "oper");
        elOperation.attr("name", "oper");
        elOperation.attr("value", operation);

//        Element addMore = new Element("input");
//        addMore.attr("type", "button");
//        addMore.attr("id", "addRow");
//        addMore.attr("onclick", "add_NewRow();");
//        addMore.attr("value", "Add row");

        Element submit = new Element("input");
        submit.attr("type", "submit");
        submit.attr("id", "submit");
        submit.attr("value", "Go!");
        Element parentPageDiv = new Element("div").attr("id", "formMainDiv").attr("style", "visibility=inherit;");
        parentPageDiv.appendChild(table);
        Element form = new Element("form").attr("id", "guimap").attr("method", "post").attr("action", "/updatePageGui");
        form.appendChild(parentPageDiv);
//        form.appendChild(addMore);
        form.appendChild(elTableName);
        form.appendChild(elPageName);
        form.appendChild( elOperation);
        form.appendChild(submit);
        html.body().before(scriptElement);
        html.body().appendChild(form);
        html.body().attr("onload", "add_UpdateRow()");

        return Parser.unescapeEntities(html.toString(), false);


	}
}
