package data;

import javafx.util.Pair;
import model.Document;
import model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

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
                cell = sheet.getRow(5).getCell(73);
                cell.setCellValue(this.document.getOCPO());
            }

            if (this.document.getOrganization() != null) {
                cell = sheet.getRow(5).getCell(24);
                cell.setCellValue(this.document.getOrganization());
            }

            if (this.document.getUnit() != null) {
                cell = sheet.getRow(7).getCell(24);
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

            if (this.document.getCheckingPost() != null) {
                cell = sheet.getRow(68).getCell(34);
                cell.setCellValue(this.document.getCheckingPost());
            }

            if (this.document.getCheckingFace() != null) {
                cell = sheet.getRow(68).getCell(66);
                cell.setCellValue(this.document.getCheckingFace());
            }

            if (this.document.getProducts() != null) {

                int row = 24;

                for (Product product : document.getProducts()) {

                    int column = 0;

                    if (product.getPosition() != null) {

                        if (product.getPosition() > Document.maxFrontSideRows) {
                            column = 34;

                            for (Double sum : document.getTotal().getSideSum()) {

                                if (sum != null) {
                                    cell = sheet.getRow(row).getCell(column += 10);
                                    cell.setCellValue(sum);
                                }

                            }

                            row = 43;
                            column = 0;
                        }

                        cell = sheet.getRow(row).getCell(column += 3);
                        cell.setCellValue(product.getPosition());
                    }

                    if (product.getTitle() != null) {
                        cell = sheet.getRow(row).getCell(column += 12);
                        cell.setCellValue(product.getTitle());
                    }

                    if (product.getCode() != null) {
                        cell = sheet.getRow(row).getCell(column += 4);
                        cell.setCellValue(product.getCode());
                    }

                    if (product.getMeasures() != null) {
                        cell = sheet.getRow(row).getCell(column += 4);
                        cell.setCellValue(product.getMeasures());
                    }

                    if (product.getOKEI() != null) {
                        cell = sheet.getRow(row).getCell(column += 4);
                        cell.setCellValue(product.getOKEI());
                    }

                    if (product.getCost() != null) {
                        cell = sheet.getRow(row).getCell(column);
                        cell.setCellValue(product.getCost());
                    }

                    column = 30;
                    int headerRow = 19;
                    int headerColumn = 31;

                    for (Map.Entry <String, Pair <Integer, Double>> remain : product.getRemains().entrySet()) {

                        if (remain != null) {
                            String date = remain.getKey();
                            int count = remain.getValue().getKey();
                            double cost = remain.getValue().getValue();

                            cell = sheet.getRow(headerRow).getCell(headerColumn += 2);
                            cell.setCellValue(date.substring(0, 2));

                            cell = sheet.getRow(headerRow).getCell(headerColumn += 4);
                            cell.setCellValue(date.substring(3, 5));

                            cell = sheet.getRow(headerRow).getCell(headerColumn += 4);
                            cell.setCellValue(date.substring(6));

                            cell = sheet.getRow(row).getCell(column += 4);
                            cell.setCellValue(count);

                            cell = sheet.getRow(row).getCell(column += 6);
                            cell.setCellValue(cost);
                        }

                    }

                    row++;

                }

            }

            int row = 65;
            int column = 34;
            int index = 0;

            for (Double sum : document.getTotal().getSum()) {

                if (sum != null) {
                    cell = sheet.getRow(row).getCell(column);
                    cell.setCellValue(sum - document.getTotal().getOneSideSum(index++));

                    cell = sheet.getRow(row + 1).getCell(column += 10);
                    cell.setCellValue(sum);
                }

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
