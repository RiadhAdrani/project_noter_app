package com.example.noter;

import java.io.Serializable;

public class Category implements Serializable {
    // Class describing a Category Object in the application

    // Unique Identifier used to retrieve and save category
    public String UID;

    // Name to be displayed
    public String name;

    public Category (String name){
        // Dynamic Constructor
        this.name = name.trim();
        this.UID = new MyDate().GET_CURRENT_DATE().getTime() + name + new MyDate().GET_CURRENT_DATE().getTime();
    }

    public Category (String name ,String UID){
        // Custom Constructor
        this.name = name.trim();
        this.UID = UID;
    }

    public Category (String name, Boolean bool){
        // Dummy Constructor
        this.name = name.trim();
        this.UID = "kifqs8e5o2f6q9k7f8o5lv2q1el4vpf5qe3v6l5pfq";
    }

}
