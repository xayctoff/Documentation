package model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Document {

    /*  Шапка документа */
    private String organization;
    private String unit;
    private int number;
    private String date;
    private int OCUD;
    private int OCPO;
    private Date dateFrom;
    private Date dateTo;

    /*  Поля таблицы  */
    private int position;
    private int productCode;
    private String name;
    private String measures;
    private int measuresCode;
    private double cost;
    private HashMap <Integer, Double> remains;


    private List <Integer> sum;

    /*  Подвал документа  */
    private String responsiblePost;
    private String checkingPost;
    private String responsibleFace;
    private String checkingFace;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public int getMeasuresCode() {
        return measuresCode;
    }

    public void setMeasuresCode(int measuresCode) {
        this.measuresCode = measuresCode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public HashMap <Integer, Double> getRemains() {
        return remains;
    }

    public void setRemains(HashMap <Integer, Double> remains) {
        this.remains = remains;
    }

    public List <Integer> getSum() {
        return sum;
    }

    public void setSum(List<Integer> sum) {
        this.sum = sum;
    }

    public String getResponsiblePost() {
        return responsiblePost;
    }

    public void setResponsiblePost(String responsiblePost) {
        this.responsiblePost = responsiblePost;
    }

    public String getCheckingPost() {
        return checkingPost;
    }

    public void setCheckingPost(String checkingPost) {
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
}
