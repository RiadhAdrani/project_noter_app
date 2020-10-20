package com.example.noter;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    public Date creationDate;
    public Date lastModifiedDate;
    public String content;
    public String title;
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
        MyResources r = new MyResources(context);
        return r.GET_ICON(this.iconUID);
    }
}
