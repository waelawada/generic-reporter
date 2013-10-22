package com.waelawada.genericreporter.views;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * This
 *
 * @author: Wael Awada
 */
public class PdfBuilder {

    public static Document buildPdfDocument(Map model, Document document) throws Exception{
        ArrayList<Map<String,String>> resultList = (ArrayList<Map<String,String>>) model.get("resultList");
        String[] tableHeaders = (String[]) model.get("tableHeaders");

        PdfPTable table = new PdfPTable(tableHeaders.length);
        table.setWidthPercentage(98);

        if(model.get("logo")!=null){
            PdfPCell imageCell = new PdfPCell(Image.getInstance(new URL((String) model.get("logo"))));
            imageCell.setBorderWidth(0);
            imageCell.setPadding(0.5f);
            table.addCell(imageCell);
            for(int i=1; i<tableHeaders.length;i++){
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorderWidth(0);
                table.addCell(emptyCell);
            }
        }

        for (int i = 0; i < tableHeaders.length; i++) {
            Phrase phrase = new Phrase(tableHeaders[i]);
            phrase.setFont(FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD));
            PdfPCell cell = new PdfPCell(phrase);
            cell.setBackgroundColor(Color.decode((String) model.get("headerColor")));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        int counter = 0;
        for (Map<String, String> result : resultList) {

            for (int i = 0; i < tableHeaders.length; i++) {
                Phrase phrase = new Phrase(result.get(tableHeaders[i]));
                phrase.setFont(FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL));
                PdfPCell cell = new PdfPCell(phrase);
                if((counter%2)==1){
                    cell.setBackgroundColor(Color.decode((String)model.get("color2")));
                }
                else{
                    cell.setBackgroundColor(Color.decode((String)model.get("color1")));
                }
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            counter++;
        }

        document.add(table);
        return document;
    }

}
