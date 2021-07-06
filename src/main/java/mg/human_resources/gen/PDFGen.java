/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.gen;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mg.starter.starter_boilerplate.bl.test.Example;
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
public final class PDFGen {

    private PDDocument document;
    private List<PDPage> pages;
    private PDPage firstPage;
    private String docName;

    public float MARGIN_DOC = 20;
    public float INTERLINE = 10;
    public float FONT_SIZE = 8;
    public float NEW_LINE = 15;
    
    public List<Example> examples = null;

    public PDFGen(String pdfName, List<Example> _examples) throws Exception {
        this.examples = _examples;
        setDocName(pdfName);
        config();
        PDPageContentStream firstContent = new PDPageContentStream(getDocument(), getFirstPage());

        drawContent(firstContent);
        drawShape(firstContent);
        sidebar();
        footer();

        firstContent.fill();
        firstContent.stroke();
        firstContent.close();
        
        

        save();

    }

    public void drawShape(PDPageContentStream content) throws Exception {
        //content = new PDPageContentStream(getDocument(), getFirstPage());
        float height = this.getFirstPage().getArtBox().getHeight();
        float width = this.getFirstPage().getArtBox().getWidth();
        content.moveTo(0, 0);
        content.curveTo1(width / 2, 10, width, 0);
    }

    public void save() throws IOException {
        getDocument().save(getDocName());
        getDocument().close();
    }

    public void config() {
        setDocument(new PDDocument());
        setPages(new ArrayList());
        setFirstPage(new PDPage(PDRectangle.A4));
        getDocument().addPage(getFirstPage());
    }

    public void drawRectangle(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3, Point2D.Float p4, PDPageContentStream content) throws Exception {
        content.setStrokingColor(Color.red);
        content.moveTo((float) p1.getX(), (float) p1.getY());
        content.lineTo((float) p2.getX(), (float) p2.getY());
        content.stroke();
        content.moveTo((float) p2.getX(), (float) p2.getY());
        content.lineTo((float) p3.getX(), (float) p3.getY());
        content.stroke();
        content.moveTo((float) p3.getX(), (float) p3.getY());
        content.lineTo((float) p4.getX(), (float) p4.getY());
        content.stroke();
        content.moveTo((float) p4.getX(), (float) p4.getY());
        content.lineTo((float) p1.getX(), (float) p1.getY());
        content.stroke();
    }

    public List<String> decoupStr(String str, double width) {
        List<String> result = new ArrayList();
        char[] arrStr = str.toCharArray();
        StringBuilder strBuild = new StringBuilder();
        float lengthOneLine = (float) Math.floor(width / FONT_SIZE);

        for (int iC = 0; iC < arrStr.length; iC++) {
            if ((iC % lengthOneLine) == 0) {
                result.add(strBuild.toString());
                strBuild = new StringBuilder();
            }
            if(strBuild.length() == 0 && arrStr[iC] == ' ') {
                continue;
            }
            strBuild.append(arrStr[iC]);
        }

        result.add(strBuild.toString());

        return result;
    }

    public void drawText(float x, float y, String text, float width,
            PDFont docFont,
            PDPageContentStream content) throws IOException {
        List<String> strs = decoupStr(text, width);

        content.beginText();
        content.newLineAtOffset(x, y);

        for (String str : strs) {
            content.showText(str);
            content.newLineAtOffset(0, -INTERLINE);
        }

        content.endText();
    }

    public void drawContent(PDPageContentStream content) throws IOException, Exception {

        PDFont docFont = PDType1Font.COURIER;
        content.setFont(docFont, FONT_SIZE);
        float width = this.getFirstPage().getArtBox().getWidth();

        float TEXTWIDTH = width - 4 * MARGIN_DOC;
        float heightDoc = getFirstPage().getMediaBox().getHeight();
        String description = "Description: Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "id venenatis ex, eu cursus quam. Nunc id erat id eros ultrices."
                + "condimentum. Sed rhoncus elit vitae mi facilisis, id molestie sapien "
                + "malesuada. Integer non nisi ac diam imperdiet accumsan at sit amet lectus. Nam vitae mauris sapien.";
        int numberLines = (int) Math.ceil(description.length() * FONT_SIZE / TEXTWIDTH);
        numberLines *= INTERLINE;
        drawText(MARGIN_DOC, heightDoc - MARGIN_DOC, "Titre: Système de gestion", TEXTWIDTH, docFont, content);
        drawText(MARGIN_DOC, heightDoc - MARGIN_DOC - NEW_LINE, description,
                TEXTWIDTH, docFont,
                content);
        // DRAW TABLE
        float PADDING_TABLE = 5;

        List<String> columns = new ArrayList();
        columns.add("Name");
        columns.add("Date Birth");
        columns.add("salary");
        /*List<Example> examples = new ArrayList();
        examples.add(new Example("zaka", 250000, null));
        examples.add(new Example("zina", 45000, null)); */
        numberLines++;

        content.beginText();
        float tableY = heightDoc - MARGIN_DOC - 3 * NEW_LINE - numberLines;
        content.newLineAtOffset(MARGIN_DOC + PADDING_TABLE, tableY);
        float colWidth = 150; // need to get the max string length and this equal to maxLen * FONT_SIZE

        for (String col : columns) {
            content.showText(col);
            content.newLineAtOffset(colWidth, 0);
        }
        float endTableX = -colWidth * columns.size();
        content.newLineAtOffset(endTableX, -INTERLINE);
        for (Example example : examples) {
            content.showText(example.getName());
            content.newLineAtOffset(colWidth, 0);
            content.showText("1980-02-01");
            content.newLineAtOffset(colWidth, 0);
            content.showText(String.valueOf(example.getSalary()));
            content.newLineAtOffset(colWidth, 0);
            content.newLineAtOffset(endTableX, -INTERLINE);
        }
        content.endText();

        float lineSize = NEW_LINE - PADDING_TABLE;
        float endYRect = tableY + lineSize - examples.size() * INTERLINE - INTERLINE - PADDING_TABLE;
        this.drawRectangle(
                new Point2D.Float(MARGIN_DOC, tableY + lineSize),
                new Point2D.Float(-1 * endTableX, tableY + lineSize),
                new Point2D.Float(-1 * endTableX, endYRect),
                new Point2D.Float(MARGIN_DOC, endYRect),
                content);
        // END DRAWING TABLE
    }

    public void sidebar() {

    }

    public void footer() {

    }

    public PDPage getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(PDPage firstPage) {
        this.firstPage = firstPage;
    }

    public PDDocument getDocument() {
        return document;
    }

    public void setDocument(PDDocument document) {
        this.document = document;
    }

    public List<PDPage> getPages() {
        return pages;
    }

    public void setPages(List<PDPage> pages) {
        this.pages = pages;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

}
