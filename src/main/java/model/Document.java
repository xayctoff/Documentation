package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Document {

    private static final String directoryName = "src/main/resources/data";
    private static final String headerFile = "header.txt";
    private static final String organizationsFile = "organizations.txt";
    private static final String unitsFile = "units.txt";
    private static final String postsFile = "posts.txt";

    public static final int maxRows = 32;
    public static final int maxFrontSideRows = 10;

    /*  Шапка документа */
    private ArrayList <String> organization;
    private ArrayList <String> unit;
    private int number;
    private String date;
    private int OCUD;
    private int OCPO;
    private Date dateFrom;
    private Date dateTo;

    /*  Подвал документа  */
    private ArrayList <String> responsiblePost;
    private ArrayList <String> checkingPost;
    private String responsibleFace;
    private String checkingFace;

    private ArrayList <Product> products;
    private Total total;

    public void add(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }

        this.products.add(product);
    }

    private ArrayList <String> readArrayList(BufferedReader bufferedReader) throws IOException {
        ArrayList <String> arrayList = new ArrayList <>();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            arrayList.add(line);
        }

        return arrayList;
    }

    public void getHeaderFromFile() {
        try {
            FileInputStream stream = new FileInputStream(new File
                    (directoryName + File.separator + headerFile));
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            this.setNumber(Integer.parseInt(bufferedReader.readLine()));
            this.setOCUD(Integer.parseInt(bufferedReader.readLine()));
            this.setOCPO(Integer.parseInt(bufferedReader.readLine()));

            stream = new FileInputStream(new File(directoryName + File.separator + organizationsFile));
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            this.setOrganization(readArrayList(bufferedReader));

            stream = new FileInputStream(new File(directoryName + File.separator + unitsFile));
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            this.setUnit(readArrayList(bufferedReader));
        }

        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void getFooterFromFile() {
        try {
            FileInputStream stream = new FileInputStream(new File
                    (directoryName + File.separator + postsFile));
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            this.setResponsiblePost(readArrayList(bufferedReader));
            this.setCheckingPost(readArrayList(bufferedReader));
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList <String> getOrganization() {
        return organization;
    }

    public void setOrganization(ArrayList <String> organization) {
        this.organization = organization;
    }

    public ArrayList <String> getUnit() {
        return unit;
    }

    public void setUnit(ArrayList <String> unit) {
        this.unit = unit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOCUD() {
        return OCUD;
    }

    public void setOCUD(int OCUD) {
        this.OCUD = OCUD;
    }

    public int getOCPO() {
        return OCPO;
    }

    public void setOCPO(int OCPO) {
        this.OCPO = OCPO;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public ArrayList <String> getResponsiblePost() {
        return responsiblePost;
    }

    public void setResponsiblePost(ArrayList <String> responsiblePost) {
        this.responsiblePost = responsiblePost;
    }

    public ArrayList <String> getCheckingPost() {
        return checkingPost;
    }

    public void setCheckingPost(ArrayList <String> checkingPost) {
        this.checkingPost = checkingPost;
    }

    public String getResponsibleFace() {
        return responsibleFace;
    }

    public void setResponsibleFace(String responsibleFace) {
        this.responsibleFace = responsibleFace;
    }

    public String getCheckingFace() {
        return checkingFace;
    }

    public void setCheckingFace(String checkingFace) {
        this.checkingFace = checkingFace;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
