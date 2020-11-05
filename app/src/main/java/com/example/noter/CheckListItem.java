package com.example.noter;

import java.util.Date;

public class CheckListItem {

    // Object defining an item of a check list

    // description of the item
    public String text;

    // defines whether the item is done or not
    Boolean isDone;

    // creation date
    Date dateCreated;

    // finishing date
    Date dateDone;

    // public constructor
    public CheckListItem(String text) {
        this.text = text;
        isDone = false;
        dateCreated = new MyDate().GET_CURRENT_DATE();
    }
}