package com.kuo.kuoresume.data;

/**
 * Created by Kuo on 2016/4/24.
 */
public class ChartData {
    String name;
    int value;

    public ChartData(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
