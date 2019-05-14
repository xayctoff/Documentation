package model;

import java.util.ArrayList;
import java.time.LocalDate;

public class Document {

    private static final String headerFile = "/data/header.txt";
    private static final String organizationsFile = "/data/organizations.txt";
    private static final String unitsFile = "/data/units.txt";
    private static final String postsFile = "/data/posts.txt";

    public static final int maxRows = 32;
    public static final int maxFrontSideRows = 10;

    /*  Шапка документа */
    private ArrayList <String> organization;
    private ArrayList <String> unit;
    private String  number;
    private String date;
    private String OCUD;
    private String OCPO;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    /*  Подвал документа  */
    private ArrayList <String> responsiblePost;
    private ArrayList <String> checkingPost;
    private String responsibleFace;
    private String checkingFace;

    private ArrayList <Product> products;
    private Total total;

    public Document() {
        this.total = new Total();
    }

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
            FileInputStream stream = new FileInputStream(this.getClass().getResource(headerFile).getPath());
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            this.setNumber(bufferedReader.readLine());
            this.setOCUD(bufferedReader.readLine());
            this.setOCPO(bufferedReader.readLine());

            stream = new FileInputStream(this.getClass().getResource(organizationsFile).getPath());
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            this.setOrganization(readArrayList(bufferedReader));

            stream = new FileInputStream(this.getClass().getResource(unitsFile).getPath());
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            this.setUnit(readArrayList(bufferedReader));

            stream.close();
        }

        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void getFooterFromFile() {
        try {
            FileInputStream stream = new FileInputStream(this.getClass().getResource(postsFile).getPath());
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            this.setResponsiblePost(readArrayList(bufferedReader));
            this.setCheckingPost(responsiblePost);

            stream.close();
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOCUD() {
        return OCUD;
    }

    public void setOCUD(String OCUD) {
        this.OCUD = OCUD;
    }

    public String getOCPO() {
        return OCPO;
    }

    public void setOCPO(String OCPO) {
        this.OCPO = OCPO;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
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

    public ArrayList <Product> getProducts() {
        return products;
    }
}
