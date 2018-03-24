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

@SuppressWarnings("serial")
public class HTMLServerMain extends HttpServlet {

    public HTMLServerMain() {

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

//        String tableName = getParameter(req, "tableName");
        String responseStr = "";
        switch (sPath) {
            case "/favicon.ico":
                break;
//            case "/test":
//                LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap = processRequestInput(
//                        req.getParameterMap());
//                LibDatabase.updateTable(tableName, cleanParamMap);
//                responseStr = req.getParameterMap().toString();
//
//                break;
            case "/freeform":
                responseStr = LibHtml.getTableEntryForm("entryform", null, null);
                break;
            case "":
            case "/":
            default:
                responseStr = LibHtml.getWelcomeForm();

        }
        writeResponse(resp, responseStr,"text/html;charset=UTF-8", null);
    }

    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sPath = req.getPathInfo().toLowerCase();

        String pageName = getParameter(req, "pageName");
        String operation = getParameter(req, "oper");
        String responseStr = "";
        LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap = processRequestInput(
                req.getParameterMap());
        switch (sPath.toLowerCase()) {
            case "/insertpagegui":
//                LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap = processRequestInput(
//                        req.getParameterMap());
                LibDatabase.insertGuiMap(pageName, cleanParamMap);
                responseStr = req.getParameterMap().toString();

                break;
            case "/updatepagegui":
//                LinkedHashMap<String, LinkedHashMap<String, String>> cleanParamMap = processRequestInput(
//                        req.getParameterMap());
                LibDatabase.updateGuiMap(pageName, cleanParamMap);
                responseStr = req.getParameterMap().toString();

                break;
            case "/fetchpage":
                if (operation.equalsIgnoreCase("new"))
                	responseStr = LibHtml.getPageAddGUIForm(pageName, operation);
                else 
                	responseStr = LibHtml.getPageUpdateGUIForm(pageName, operation);
                break;
            case "/fetchcustomclasses":
                break;
            case "/":
                break;
                
            default:
                responseStr = "<html><h2>Not implemented</h2></BR><p>Quit kidding, there's nothing here!!</p></html>";
        }
        writeResponse(resp, responseStr,"text/html;charset=UTF-8", null);
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