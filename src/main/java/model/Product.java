package model;

import java.io.*;
import java.util.HashMap;

public class Product {

    private static final String directoryName = "src/main/resources/data";
    private static final String productsFile = "products.txt";
    private static final String measuresFile = "measures.txt";

    private int position;
    private int code;
    private String title;
    private String measures;
    private int OKEI;
    private int cost;
    private HashMap <Integer, Double> remains;

    public static int counter = 1;

    private static HashMap <String, String> productCodes;
    private static HashMap <String, String> measuresCodes;

    public static void init() {
        String[] position;
        HashMap <String, String> productCodes = new HashMap<>();
        HashMap <String, String> measuresCodes = new HashMap<>();

        try {
            FileInputStream stream = new FileInputStream(new File
                    (directoryName + File.separator + productsFile));
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            String line;

            while((line = bufferedReader.readLine()) != null ) {
                position = line.split("#");
                productCodes.put(position[0], position[1]);
            }

            stream = new FileInputStream(new File(directoryName + File.separator + measuresFile));
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            while ((line = bufferedReader.readLine()) != null) {
                position = line.split("#");
                measuresCodes.put(position[0], position[1]);
            }

            Product.productCodes = productCodes;
            Product.measuresCodes = measuresCodes;

        }

        catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public Product(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public int getOKEI() {
        return OKEI;
    }

    public void setOKEI(int OKEI) {
        this.OKEI = OKEI;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public HashMap<Integer, Double> getRemains() {
        return remains;
    }

    public void setRemains(HashMap<Integer, Double> remains) {
        this.remains = remains;
    }
}
