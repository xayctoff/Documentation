package model;

import java.util.ArrayList;

public class Total {

    private static final int sumCapacity = 5;

    private static ArrayList <Double> sum;
    private static ArrayList <Double> sideSum;

    static {
        sideSum = new ArrayList <>(sumCapacity) {
            {
                for (int i = 0; i < sumCapacity; i++) {
                    add(0.0);
                }
            }
        };

        sum = sideSum;
    }

    public ArrayList <Double> getSum() {
        return sum;
    }

    public double getOneSum(int index) {
        return sum.get(index);
    }

    public void setOneSum(double value, int index) {
        sum.set(index, value);
    }

    public void setSum(double value, Product product) {
        for (Double item : sum) {
            int index = sum.indexOf(item);
            sum.set(index, getOneSum(index) + value * product.getRemainsCount(index));
        }
    }

    public ArrayList <Double> getSideSum() {
        return sideSum;
    }

    public void setSideSum(double value, int index) {
        sideSum.set(index, value);
    }
}
