package com.calculator.dataentry.model;

import java.util.Calendar;

public class EveryDay {



    Calendar calendar;
    int icon,color;

    public EveryDay(Calendar calendar, int icon, int color) {
        this.calendar = calendar;
        this.icon = icon;
        this.color = color;
    }
}
