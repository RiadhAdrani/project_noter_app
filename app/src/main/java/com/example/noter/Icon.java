package com.example.noter;

import java.io.Serializable;

public class Icon implements Serializable {
    // Class describing an Icon Object in the application

    // Icon id found in /res/drawable/
    // R.drawable.ic_example
    int id;

    // Icon name
    // Names could be declared in /res/values/strings
    String name;

    // The unique identifier of the icon
    // should not match any other uid of
    // any other icon
    String uid;

    // Constructor
    public Icon(int id, String name, String uid) {
        this.id = id;
        this.name = name;
        this.uid = uid;
    }

}
