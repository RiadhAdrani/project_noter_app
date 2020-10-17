package com.example.noter;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    public Date creation;
    public Date lastModified;
    public String content;
    public String title;
    public int nIcon;

    // creating a dummy note using Lorem ipsum
    public Note(String title, int nIcon){
        this.title = title;
        this.nIcon = nIcon;
        this.content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    }

    // creating a new note
    public Note(String title, String content){
        if (title.isEmpty()) this.title = "New note";
        else this.title = title;
        this.content = content;
    }

    // creating a new note
    public Note(String title, String content, int icon){
        if (title.isEmpty()) this.title = "New note";
        else this.title = title;
        this.content = content;
        this.nIcon = icon;
        // creation date
    }
}
