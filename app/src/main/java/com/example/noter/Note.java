package com.example.noter;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    // Class describing a Note Object in the application

    // Creation date of the note
    public Date creationDate;

    // The date of the last modification
    // made to the note
    public Date lastModifiedDate;

    // The content of the note
    public String content;

    // The title of the note
    public String title;

    // Unique ID used to load the correspondent icon from MyResources
    public String iconUID;

    // basic constructor;
    public Note(Context context){
        this.title = context.getString(R.string.titleDefault);
        this.content = "";
        this.iconUID = new MyResources(context).GET_ICON_LIST().get(0).uid;
        this.creationDate = new MyDate().GET_CURRENT_DATE();
        this.lastModifiedDate = new MyDate().GET_CURRENT_DATE();
    }

    Icon getIcon(Context context){
        // fetch and return the correspondent icon from MyResources

        MyResources r = new MyResources(context);
        return r.GET_ICON(this.iconUID);
    }
}
