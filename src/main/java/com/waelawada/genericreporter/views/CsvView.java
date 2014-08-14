package com.waelawada.genericreporter.views;

import au.com.bytecode.opencsv.CSVWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by waelawada on 8/13/14.
 */
public class CsvView extends org.springframework.web.servlet.view.AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedWriter writer = new BufferedWriter(response.getWriter());
        response.addHeader("Content-type", "text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"result.csv\"");

        CSVWriter csvWriter = new CSVWriter(writer);

        ArrayList<Map<String, String>> data = (ArrayList<Map<String, String>>) model.get("csvData");
        if (data == null) {
            throw new Exception("csvData doesn't exist in the model");
        }

        if(data.size()>0) {
            Map<String, String> firstResult = data.get(0);
            String[] headers = firstResult.keySet().toArray(new String[0]);
            if (request.getParameter("includeHeaders") != null) {
                csvWriter.writeNext(headers);
            }
            for (Map<String, String> resultRow : data) {
                String[] row = new String[headers.length];
                int index = 0;
                for (String column : headers) {
                    row[index++] = resultRow.get(column);
                }
                csvWriter.writeNext(row);

            }
        }
        else{
            csvWriter.writeNext(new String[]{"no data to show"});
        }
        csvWriter.close();
        writer.flush();
        writer.close();
    }

}
