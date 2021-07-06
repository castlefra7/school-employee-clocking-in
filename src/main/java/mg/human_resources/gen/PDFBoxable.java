/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.gen;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.utils.PDStreamUtils;
import be.quodlibet.boxable.utils.PageContentStreamOptimized;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import mg.human_resources.bl.Employee;
import mg.human_resources.rsc.PointingAttr;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author lacha
 */
public class PDFBoxable {

    private PageContentStreamOptimized cos;
    private PDPageContentStream oldCos;
    private PDPage page;
    private PDDocument mainDocument;
    private PDFont font = PDType1Font.HELVETICA;
    private float leftMargin = 50;
    private int marginBetweenYElements = 25;
    private float titleFontSize = 18;
    private int yPosition = 700;
    private float pFontSize = 12;

    public PDFBoxable() throws IOException {
        page = new PDPage(PDRectangle.A4);
        mainDocument = new PDDocument();
        oldCos = new PDPageContentStream(mainDocument, page);
        cos = new PageContentStreamOptimized(oldCos);
    }

    public void drawPageTitle() throws IOException {

        PDStreamUtils.write(cos, "Information employé", font, titleFontSize, leftMargin, yPosition,
                Color.BLACK);

        // drop Y position with default margin between vertical elements
        yPosition -= marginBetweenYElements;
    }

    public void drawEmployeeInf(Employee employee) throws Exception {
        float _leftMargin = (leftMargin * 3) + 5;
        PDStreamUtils.write(cos, "Date embauche:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getDate_begin_employment().toString(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;

        PDStreamUtils.write(cos, "Catégorie:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getCategory().getName(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;

        PDStreamUtils.write(cos, "Nom:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getLast_name(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;

        PDStreamUtils.write(cos, "Prénom:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getFirst_name(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;

        PDStreamUtils.write(cos, "Date de naissance:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getDate_birth().toString(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;
    }

    public void drawTablePointings(List<PointingAttr> pointings) throws IOException {
        //Dummy Table
        float margin = 50;
// starting y position is whole page height subtracted by top and bottom margin
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
// we want table across whole page width (subtracted by left and right margin ofcourse)
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

        boolean drawContent = true;
        float yStart = yStartNewPage;
        float bottomMargin = 70;
// y position is your coordinate of top left corner of the table
        float yPosition = 550;

        BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, mainDocument, page, true, drawContent);

        Row<PDPage> headerRow = table.createRow(30f);
        Cell<PDPage> cell = headerRow.createCell(100, "Header");
        table.addHeaderRow(headerRow);

        Row<PDPage> row = table.createRow(12);
        cell = row.createCell(30, "Data 1");
        cell = row.createCell(70, "Some value");

        table.draw();
    }

    public void save() throws IOException {
        cos.close();
        oldCos.close();
        mainDocument.addPage(page);
        mainDocument.save("testfile.pdf");
        mainDocument.close();
    }
}
