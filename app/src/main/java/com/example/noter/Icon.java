package com.example.noter;

import java.io.Serializable;

public class Icon implements Serializable {
    int id;
    String name;
    String uid;

    public Icon(int id, String name, String uid) {
        this.id = id;
        this.name = name;
        this.uid = uid;
    }

}
