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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import mg.human_resources.bl.Employee;
import mg.human_resources.bl.EmployeePaie;
import mg.human_resources.bl.EmployeeWeeklyHoursAndAmount;
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

    public int id_semaine = -1;

    public PDFBoxable() throws IOException {
        page = new PDPage(PDRectangle.A4);
        mainDocument = new PDDocument();
        oldCos = new PDPageContentStream(mainDocument, page);
        cos = new PageContentStreamOptimized(oldCos);
    }

    public void drawPageTitle() throws IOException {

        PDStreamUtils.write(cos, "Fiche de paie", font, titleFontSize, leftMargin, yPosition,
                Color.BLACK);

        // drop Y position with default margin between vertical elements
        yPosition -= (marginBetweenYElements * 2);
    }

    public void drawEmployeeInf(Employee employee) throws Exception {
        float _leftMargin = (leftMargin * 3) + 5;
        PDStreamUtils.write(cos, "Date embauche:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getDate_begin_employment().toString(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;

        PDStreamUtils.write(cos, "Catégorie:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, employee.getCategory().getName(), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;

        PDStreamUtils.write(cos, "Salaire base:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, String.valueOf(employee.getCategory().getStandard_salary()), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
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

        PDStreamUtils.write(cos, "Semaine:", font, pFontSize, leftMargin, yPosition, Color.BLACK);
        PDStreamUtils.write(cos, String.valueOf(id_semaine), font, pFontSize, _leftMargin, yPosition, Color.BLACK);
        yPosition -= marginBetweenYElements;
        
    }

    public void drawTablePaie(EmployeePaie empPaie) throws IOException {
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
        float yPosition = 450;

        BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, mainDocument, page, true, drawContent);

        Row<PDPage> headerRow = table.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(40, "Désignation");
        cell = headerRow.createCell(20, "Total Heures");
        cell = headerRow.createCell(20, "Total Horaire");
        cell = headerRow.createCell(20, "Montant");
        table.addHeaderRow(headerRow);
        

        List<EmployeeWeeklyHoursAndAmount> paies = empPaie.getPaie();
        String curr = " Ar";
        for (EmployeeWeeklyHoursAndAmount paie : paies) {
            Row<PDPage> row = table.createRow(12);
            cell = row.createCell(40, paie.getCode());
            cell = row.createCell(20, String.format("%.2f", paie.getHours()));
            cell = row.createCell(20, String.format("%.2f", paie.getHourlyRate()) + curr);
            cell = row.createCell(20, String.format("%.2f",paie.getTotalAmount()) + curr);
        }

        Row<PDPage> row = table.createRow(12);
        cell = row.createCell(40, "");
        cell = row.createCell(40, "Indemnité");
        cell = row.createCell(20, String.format("%.2f", empPaie.getAmounts()[0]));
        
        Row<PDPage> rowTot = table.createRow(12);
        cell = rowTot.createCell(40, "");
        cell = rowTot.createCell(40, "Total à payer");
        cell = rowTot.createCell(20, String.format("%.2f", empPaie.getAmounts()[1]));

        table.draw();
    }

    private String uploadDir;
    
    public void save() throws IOException {
        cos.close();
        oldCos.close();
        mainDocument.addPage(page);
        
        mainDocument.save(this.getUploadDir());
        mainDocument.close();
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    
    
}
