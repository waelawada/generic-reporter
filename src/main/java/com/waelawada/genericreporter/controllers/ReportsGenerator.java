package com.waelawada.genericreporter.controllers;

import com.google.gson.Gson;
import com.waelawada.genericreporter.utils.ProcedureUtils;
import com.waelawada.genericreporter.views.LandscapePdfView;
import com.waelawada.genericreporter.views.PortraitPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * This is the report controller that will generate html, pdf and json reports by database name and function name.
 *
 * @author: Wael Awada
 */
@Controller
@Qualifier("report1")
@RequestMapping("/report")
public class ReportsGenerator {

    @Autowired
    private DriverManagerDataSource dataSource;

    @Autowired
    private Gson gson;

    @Autowired
    private LandscapePdfView landscapePdfView;

    @Autowired
    private PortraitPdfView portraitPdfView;

    @Value("${company.logo}")
    private String logoUrl;

    @Value("${company.fontcolor}")
    private String fontColor;

    @Value("${company.color1}")
    private String color1;

    @Value("${company.color2}")
    private String color2;

    @Value("${company.headerColor}")
    private String headerColor;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * By providing a database name and a function name with a json extension, you will get a json document representing the result set returned from the database.
     *
     * @param dbName
     * @param functionName
     * @param request
     * @return result set as a JSON document.
     */
    @RequestMapping(value = "/{dbName}/{functionName}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    String getFunctionResult(@PathVariable String dbName, @PathVariable String functionName, HttpServletRequest request) {
        try{
            return gson.toJson(getResultSetAsArray(dbName, functionName, request.getParameterMap()));
        }
        catch (Exception e){
            return gson.toJson(e.getMessage());
        }
    }

    /**
     * By providing a databse name and a function name with an HTML extension, the result set will be added to the model and sent to the view for presentation
     *
     * @param dbName
     * @param functionName
     * @param request
     * @param model
     * @return String indication the view used to render the model generated
     */
    @RequestMapping("/{dbName}/{functionName}")
    public String getFunctionResultAsHtml(@PathVariable String dbName, @PathVariable String functionName, HttpServletRequest request, Model model) {
        model.addAttribute("color1", color1);
        model.addAttribute("color2", color2);
        model.addAttribute("headerColor", headerColor);
        model.addAttribute("logo", logoUrl);
        try {
            ArrayList<Map<String, String>> result = getResultSetAsArray(dbName, functionName, request.getParameterMap());
            if (result.size() > 0) {
                Map<String, String> firstResult = result.get(0);
                String[] headers = firstResult.keySet().toArray(new String[0]);
                model.addAttribute("tableHeaders", headers);
            }
            model.addAttribute("resultList", result);
            return "function/list";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "error/exception";
        }

    }

    /**
     * By providing a database name and a function name with a PDF extension, the result set will be added to a map and send to the PDF view.
     *
     * @param dbName
     * @param functionName
     * @param request
     * @return a ModelAndView Object containing the PDF view and the model map generated
     */
    @RequestMapping(value = "/{dbName}/{functionName}", produces = "application/pdf")
    public ModelAndView getFunctionResultAsPdf(@PathVariable String dbName, @PathVariable String functionName, HttpServletRequest request)  {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("color1", color1);
        model.put("color2", color2);
        model.put("headerColor", headerColor);
        model.put("logo", logoUrl);

        View selectedView = portraitPdfView;
        if(request.getParameter("landscape")!=null){
            selectedView = landscapePdfView;
        }

        try{

        ArrayList<Map<String, String>> result = getResultSetAsArray(dbName, functionName, request.getParameterMap());

        if (result.size() > 0) {
            Map<String, String> firstResult = result.get(0);
            String[] headers = firstResult.keySet().toArray(new String[0]);
            model.put("tableHeaders", headers);
        }
        model.put("resultList", result);

        return new ModelAndView(selectedView, model);
        }
        catch(Exception e){
            model.clear();
            model.put("error",e.getMessage());
            return  new ModelAndView(selectedView, model);
        }
    }

    /**
     * Generated the call statement based on the function name, database name and the parameters
     *
     * @param dbName
     * @param functionName
     * @param parameters
     * @return a string representing the call statement to be executed.
     */
    private String getQueryString(String dbName, String functionName, Map<String, String> parameters) {
        StringBuilder sql = new StringBuilder("{call ");
        sql.append(dbName);
        sql.append(".");
        sql.append(functionName);
        sql.append("(");
        StringBuilder params = new StringBuilder("");
        for (Map.Entry entry : parameters.entrySet()) {
            params.append("?,");
        }
        if (params.length() > 1) params.deleteCharAt(params.length() - 1);
        sql.append(params);
        sql.append(")};");
        return sql.toString();
    }

    /**
     * Returns the result set as an array of Maps containing the key and value for every row.
     * @param dbName
     * @param functionName
     * @param parameters
     * @return
     */
    private ArrayList getResultSetAsArray(String dbName, String functionName, Map<String, String[]> parameters) throws Exception {
        ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Map<String, String> params = ProcedureUtils.getFunctionParameterListAsMap(dbName, functionName, dataSource);
        String sqlString = getQueryString(dbName, functionName, params);

        ResultSet functionResult = executeQueryString(sqlString, params, parameters);
        ResultSetMetaData metaData = functionResult.getMetaData();

        while (functionResult.next()) {
            Map<String, String> result = new HashMap<String, String>();
            for (int columnNumber = 1; columnNumber <= metaData.getColumnCount(); columnNumber++) {
                if (metaData.getColumnType(columnNumber) == Types.VARCHAR) {
                    result.put(metaData.getColumnName(columnNumber), functionResult.getString(columnNumber));
                } else if (metaData.getColumnType(columnNumber) == Types.INTEGER || metaData.getColumnType(columnNumber) == Types.BIGINT) {
                    result.put(metaData.getColumnName(columnNumber), String.valueOf(functionResult.getInt(columnNumber)));
                } else if (metaData.getColumnType(columnNumber) == Types.DATE) {
                    result.put(metaData.getColumnName(columnNumber), String.valueOf(functionResult.getDate(columnNumber)));
                } else if (metaData.getColumnType(columnNumber) == Types.DOUBLE) {
                    result.put(metaData.getColumnName(columnNumber), String.valueOf(functionResult.getDouble(columnNumber)));
                } else if (metaData.getColumnType(columnNumber) == Types.DECIMAL) {
                    result.put(metaData.getColumnName(columnNumber), String.valueOf(functionResult.getBigDecimal(columnNumber)));
                }
            }
            resultList.add(result);
        }

        return resultList;
    }

    /**
     * Executes the query string generated with the parameters provided by the user in the HTTP request.
     * @param sqlString
     * @param sqlParams
     * @param httpParams
     * @return
     * @throws Exception
     */
    private ResultSet executeQueryString(String sqlString, Map<String, String> sqlParams, Map<String, String[]> httpParams) throws Exception {
        CallableStatement callableStatement = dataSource.getConnection().prepareCall(sqlString);
        for (Map.Entry entry : sqlParams.entrySet()) {
            String paramName = (String) entry.getKey();
            String paramType = (String) entry.getValue();
            if (paramType.equalsIgnoreCase("VARCHAR")) {
                if (httpParams.get(paramName) != null) {
                    callableStatement.setString(paramName, httpParams.get(paramName)[0]);
                } else {
                    throw new Exception("Missing Param");
                }
            } else if (paramType.equalsIgnoreCase("BIGINT") || paramType.equalsIgnoreCase("INT")) {
                if (httpParams.get(paramName) != null) {
                    callableStatement.setLong(paramName, Long.valueOf(httpParams.get(paramName)[0]));
                } else {
                    throw new Exception("Missing Param");
                }
            } else if (paramType.equalsIgnoreCase("CHAR")) {
                if (httpParams.get(paramName) != null) {
                    callableStatement.setString(paramName, httpParams.get(paramName)[0]);
                } else {
                    throw new Exception("Missing Param");
                }
            } else if (paramType.equalsIgnoreCase("DATE")) {
                if (httpParams.get(paramName) != null) {
                    Date parameterDate = dateFormat.parse(httpParams.get(paramName)[0]);
                    callableStatement.setDate(paramName, new java.sql.Date(parameterDate.getTime()));
                } else {
                    throw new Exception("Missing Param");
                }
            }
        }
        return callableStatement.executeQuery();
    }


}
