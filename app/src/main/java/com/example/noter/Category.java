package com.example.noter;

import java.io.Serializable;

public class Category implements Serializable {
    // Class describing a Category Object in the application

    // Unique Identifier used to retrieve and save category
    public String UID;

    public Category (String uid){
        this.UID = uid;
    }

}
