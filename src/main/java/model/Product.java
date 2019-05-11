package model;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Product {

    private static final String directoryName = "src/main/resources/data";
    private static final String productsFile = "products.txt";
    private static final String measuresFile = "measures.txt";
    private static final int remainsCapacity = 5;

    private int position;
    private String code;
    private String title;
    private String measures;
    private String OKEI;
    private double cost;

    public static int counter = 1;

    private static ArrayList <Pair <Integer, Double>> remains;
    private static HashMap <String, String> productCodes;
    private static HashMap <String, String> measuresCodes;

    static {
        remains = new ArrayList <>(remainsCapacity) {
            {
                for (int i = 0; i < remainsCapacity; i++) {
                    add(new Pair<>(0, 0.0));
                }
            }
        };
    }

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

    public static HashMap <String, String> getProductCodes() {
        return productCodes;
    }

    public static HashMap <String, String> getMeasuresCodes() {
        return measuresCodes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public String getOKEI() {
        return OKEI;
    }

    public void setOKEI(String OKEI) {
        this.OKEI = OKEI;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ArrayList <Pair <Integer, Double>> getRemains() {
        return remains;
    }

    public void setRemains(int index, int count) {
        remains.set(index, new Pair <> (count, getCost() * count));
    }

    public int getRemainsCount(int index) {
        return remains.get(index).getKey();
    }

    public void setRemainsCount(int count, int index) {
        remains.set(index, new Pair<> (count, getRemainsSum(index)));
    }

    public double getRemainsSum(int index) {
        return remains.get(index).getValue();
    }

    public void setRemainsSum(double sum, int index) {
        remains.set(index, new Pair<> (getRemainsCount(index), sum));
    }

    public void setRemainsAllSum(double sum) {
        int index = 0;

        for (Pair <Integer, Double> pair : remains) {
            int count = pair.getKey();
            remains.set(index, new Pair <>(count, sum * count));
            index++;
        }
    }

}
