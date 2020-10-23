package com.example.noter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDate {
    // class used to handle Date in the application

    public MyDate(){
        // Constructor

    }

    Date GET_CURRENT_DATE(){
        // return the current date
        // as Date type

        return Calendar.getInstance().getTime();
    }

    String FORMAT_DATE_SHORT(Date date){
        // return the current date
        // formatted as (a short) String

        if (date == null){
            date = GET_CURRENT_DATE();
        }
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    String FORMAT_DATE_LONG(Date date){
        // return the current date
        // formatted as (a long) String

        if (date == null){
            date = GET_CURRENT_DATE();
        }
        return DateFormat.getDateInstance(DateFormat.FULL).format(date);
    }
}
