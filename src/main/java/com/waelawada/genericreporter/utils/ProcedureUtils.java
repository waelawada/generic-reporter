package com.waelawada.genericreporter.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by waelawada on 8/5/14.
 */
public class ProcedureUtils {

    /**
     * Returns the List of parameters for a certain function as a string
     *
     * @param dbName
     * @param functionName
     * @return string representing the parameters of the function
     * @throws java.sql.SQLException
     */
    private static String getFunctionParameterListAsString(String dbName, String functionName, DriverManagerDataSource dataSource) throws SQLException {
        String sql = "Select param_list from mysql.proc where name=? and db=?";
        String paramList = "";
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, functionName);
        preparedStatement.setString(2, dbName);
        ResultSet rs1 = preparedStatement.executeQuery();
        while (rs1.next()) {
            paramList = rs1.getString(1);
        }
        return paramList;
    }

    /**
     * Get the parameters of a function as a map that contains the name of the parameter and its SQL type.
     *
     * @param dbName
     * @param functionName
     * @return Map containing the parameter name and type
     * @throws SQLException
     * @throws java.io.IOException
     */
    public static Map<String, String> getFunctionParameterListAsMap(String dbName, String functionName, DriverManagerDataSource dataSource) throws Exception {

        Map<String, String> paramMap = new HashMap<String, String>();

        String paramList = getFunctionParameterListAsString(dbName, functionName, dataSource);
        StringTokenizer tokenizeParamList = new StringTokenizer(paramList, ",");
        while (tokenizeParamList.hasMoreTokens()) {
            String param = tokenizeParamList.nextToken();
            StringTokenizer tokenizeParamString = new StringTokenizer(param, " ");
            String direction = tokenizeParamString.nextToken();
            String paramName = tokenizeParamString.nextToken();
            String paramType = tokenizeParamString.nextToken();
            if (paramType.contains("(")) {
                paramType = paramType.substring(0, paramType.indexOf("("));
            }
            paramMap.put(paramName, paramType);
        }
        return paramMap;
    }

}
