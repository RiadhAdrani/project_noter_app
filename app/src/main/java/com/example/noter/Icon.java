package com.example.noter;

import java.io.Serializable;

public class Icon implements Serializable {
    int id;
    String name;

    public Icon(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
