package com.waelawada.genericreporter.views;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * This
 *
 * @author: Wael Awada
 */
public class LandscapePdfView extends AbstractPdfView {

    @Override
    protected Document newDocument() {
        return new Document(PageSize.A4.rotate());
    }

    @Override
    protected void buildPdfDocument(Map model, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        PdfBuilder.buildPdfDocument(model, document);
    }
}
