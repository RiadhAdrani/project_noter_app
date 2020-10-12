package com.example.noter;

import java.util.Date;

public class Note {

    public Date creation;
    public Date lastModified;
    public String content;
    public String title;
    public int nPicture;
    public String file;

    public Note(String title, int nPicture,String file){
        this.title = title;
        this.nPicture = nPicture;
        this.file = file;
    }
}
