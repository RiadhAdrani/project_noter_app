package com.example.noter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDate {

    public MyDate(){

    }

    Date GET_CURRENT_DATE(){
        return Calendar.getInstance().getTime();
    }

    String FORMAT_DATE_SHORT(Date date){
        if (date == null){
            date = GET_CURRENT_DATE();
        }
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    String FORMAT_DATE_LONG(Date date){
        if (date == null){
            date = GET_CURRENT_DATE();
        }
        return DateFormat.getDateInstance(DateFormat.FULL).format(date);
    }
}
