package model;

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

        catch (FileNotFoundException e) {
            e.printStackTrace();
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
