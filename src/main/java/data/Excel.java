package data;

import model.Document;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Excel {

    private FileInputStream stream;
    {
        try {
            stream = new FileInputStream(this.getClass().getResource("/data/sample.xlsx").getPath());
        }

        catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private File output;
    private Document document;

    public Excel(Document document) {
        this.document = document;
    }

    public void write() {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            Sheet sheet = workbook.getSheetAt(0);
            Cell cell;

            if (this.document.getOCUD() != null) {
                cell = sheet.getRow(4).getCell(73);
                cell.setCellValue(this.document.getOCUD());
            }

            if (this.document.getOCPO() != null) {
                cell = sheet.getRow(4).getCell(73);
                cell.setCellValue(this.document.getOCPO());
            }

            if (this.document.getOrganization() != null) {
                cell = sheet.getRow(5).getCell(0);
                cell.setCellValue(this.document.getOrganization());
            }

            if (this.document.getUnit() != null) {
                cell = sheet.getRow(7).getCell(0);
                cell.setCellValue(this.document.getUnit());
            }

            if (this.document.getNumber() != null) {
                cell = sheet.getRow(13).getCell(40);
                cell.setCellValue(this.document.getNumber());
            }

            if (this.document.getDate() != null) {
                cell = sheet.getRow(13).getCell(49);
                cell.setCellValue(this.document.getDate());
            }

            if (this.document.getDateFrom() != null) {
                cell = sheet.getRow(13).getCell(58);
                cell.setCellValue(this.document.getDateFrom());
            }

            if (this.document.getDateFrom() != null) {
                cell = sheet.getRow(13).getCell(64);
                cell.setCellValue(this.document.getDateTo());
            }

            if (this.document.getResponsiblePost() != null) {
                cell = sheet.getRow(15).getCell(18);
                cell.setCellValue(this.document.getResponsiblePost());
            }

            if (this.document.getResponsibleFace() != null) {
                cell = sheet.getRow(15).getCell(33);
                cell.setCellValue(this.document.getResponsibleFace());
            }

            stream.close();

            FileOutputStream output = new FileOutputStream(this.output);
            workbook.write(output);
            output.close();

        }

        catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void setOutput(File output) {
        this.output = output;
    }
}
