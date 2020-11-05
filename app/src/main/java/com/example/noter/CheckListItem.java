package com.example.noter;

import java.util.Date;

public class CheckListItem {

    public String text;
    Boolean isDone;
    Date dateCreated;
    Date dateDone;

    public CheckListItem(String text) {
        this.text = text;
        isDone = false;
        dateCreated = new MyDate().GET_CURRENT_DATE();
    }
}