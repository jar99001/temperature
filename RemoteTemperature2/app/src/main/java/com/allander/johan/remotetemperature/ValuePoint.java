package com.allander.johan.remotetemperature;

import java.sql.Date;
import java.sql.Time;

class ValuePoint {
    public Date datum;
    public Time tid;
    public double temperatur;
    public static int counter = 0;

    public ValuePoint(Date d, Time t, double v) {
            datum = d;
            tid = t;
            temperatur = v;
            counter++;
    }

    public String print() {
//        return "hej";
        return datum.toString() + " -> " +  tid.toString() + " Value: " + temperatur + "\n";
    }

    public int getCount() {
        return counter;
    }
}
