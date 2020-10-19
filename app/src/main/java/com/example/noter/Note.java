package com.example.noter;

import android.app.Application;
import android.content.Context;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    public Date creation;
    public Date lastModified;
    public String content;
    public String title;
    public String iconUID;

    // basic constructor;
    public Note(Context context){
        this.title = context.getString(R.string.titleDefault);
        this.content = "";
        this.iconUID = new MyResources(context).GET_ICON_LIST().get(0).uid;
    }

    Icon getIcon(Context context){
        MyResources r = new MyResources(context);
        return r.GET_ICON(this.iconUID);
    }
}
