package com.example.noter;

import java.util.ArrayList;
import java.util.Date;

public class Category {
    // Class describing a Category Object in the application

    // The list of the notes within this category
    public ArrayList<Note> list;

    // Unique Identifier used to retrieve and save category
    public String UID;

    // Contains the icon unique ID (identifier)
    // This will be used to load the icon from MyResources
    public String iconUID;

    // Creation date of the category
    // (useful to sort the list of categories)
    public Date creationDate;

    // The date of the last modification made by the user
    // (useful to sort the list of categories)
    public Date lastModifiedDate;

    public Category(){
        // Basic constructor;

        this.creationDate = new MyDate().GET_CURRENT_DATE();
        this.lastModifiedDate = new MyDate().GET_CURRENT_DATE();
    }

}
