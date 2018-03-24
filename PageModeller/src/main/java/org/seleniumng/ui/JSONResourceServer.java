package org.seleniumng.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class JSONResourceServer extends HttpServlet {

    public JSONResourceServer() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processPost(req, resp);
    }

    private String getParameter(HttpServletRequest httpReq, String parameter) {
        String value = "Expected parameter:" + parameter
                + " was not found, please check the url for correct parameters";
        try {
            value = httpReq.getParameter(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sPath = req.getPathInfo().toLowerCase();
        String tableName = "";
        String pageName = "";
        
        String responseStr = "";
        switch (sPath) {
            case "/libdatabase/gettablefields":
            	tableName = getParameter(req, "tableName");
                Object sFields = LibDatabase.getTableFields(tableName);
                responseStr = new Gson().toJson(sFields);
                break;
            case "/libdatabase/getcustomtypes":
                Object cTypes = LibDatabase.getCustomTypes();
                responseStr = new Gson().toJson(cTypes);
                break;
            case "/libdatabase/getstandardypes":
                Object sTypes = LibDatabase.getStandardTypes();
                responseStr = new Gson().toJson(sTypes);
                break;
            case "/libdatabase/gettabledata":
            	tableName = getParameter(req, "tableName");
            	pageName = getParameter(req, "pageName");
                Object oTableData = LibDatabase.getTableData(tableName, pageName);
                responseStr = new Gson().toJson(oTableData);
                break;
            case "/libdatabase/pageguiextendedprops":
            	tableName = getParameter(req, "tableName");
            	pageName = getParameter(req, "pageName");
            	Object oMoreProps = LibDatabase.getPageExtendedProperties(tableName, pageName);
                responseStr = new Gson().toJson(oMoreProps);
                System.out.println(responseStr);
            	break;
            case "/libdatabase/getextendedproptypes":
            	tableName = getParameter(req, "tableName");
            	pageName = getParameter(req, "pageName");
            	Object oExtendedPropTypes = LibDatabase.getExtendedProptypes(tableName, pageName);
                responseStr = new Gson().toJson(oExtendedPropTypes);
                System.out.println(responseStr);
            	break;
            case "/favicon.ico":
                break;
            case "/test":
                LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap = processRequestInput(
                        req.getParameterMap());
//                LibDatabase.updateTable(tableName, cleanParamMap);
                responseStr = req.getParameterMap().toString();

                break;
            case "/freeform":
                responseStr = LibHtml.getTableEntryForm("entryform", null, null);
                break;
            case "":
            case "/":
            default:
                responseStr = LibHtml.getWelcomeForm();

        }
        writeResponse(resp, responseStr,"Content-type: application/json; charset=utf-8", null);
    }

    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sPath = req.getPathInfo().toLowerCase();

        String pageName = getParameter(req, "pageName");
        String responseStr = "";
        switch (sPath.toLowerCase()) {
            case "/updatetable":
                LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap = processRequestInput(
                        req.getParameterMap());
//                LibDatabase.updateTable(pageName, cleanParamMap);
                responseStr = req.getParameterMap().toString();

                break;
            case "/fetchpage":
//                responseStr = LibHtml.getPageAddGUIForm(pageName);
                break;
            case "/fetchcustomclasses":
                break;
            case "/":
                break;
                
            default:
//                responseStr = LibHtml.getPageAddGUIForm(pageName);
        }
        writeResponse(resp, responseStr,"Content-type: application/json; charset=utf-8", null);
    }

    protected void processBoth(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private LinkedHashMap<String, LinkedHashMap<String, String>> processRequestInput(
            Map<String, String[]> parameterMap) {
        LinkedHashMap<String, LinkedHashMap<String, String>> toReturn = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        for (Entry<String, String[]> e : parameterMap.entrySet()) {
            String[] keys = e.getKey().split("\\.");
            if (keys.length == 1)
                continue;
            String rowKey = keys[0];
            String columnKey = keys[1];
            if (toReturn.containsKey(rowKey)) {
                LinkedHashMap<String, String> oldValue = toReturn.get(rowKey);
                oldValue.put(columnKey, e.getValue()[0]);
                toReturn.put(rowKey, oldValue);
            } else {
                LinkedHashMap<String, String> columnValues = new LinkedHashMap<String, String>();
                columnValues.put(columnKey, e.getValue()[0]);
                toReturn.put(rowKey, columnValues);
            }
            System.out.println(e.getKey() + ":" + e.getValue());
        }

        return toReturn;
    }

    protected void writeResponse(HttpServletResponse response, String respStr, String contentType, Exception e) throws IOException {
        if (e != null) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            printWriter.flush();
            respStr = writer.toString();

        }
        // TODO: write jsoup document
        response.setContentType(contentType);
        response.getWriter().println(respStr);
        response.getWriter().close();
    }

}