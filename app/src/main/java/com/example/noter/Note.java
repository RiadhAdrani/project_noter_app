package com.example.noter;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
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

    // category of the note
    public String category;

    // checkList
    public ArrayList<CheckListItem> checkList;

    // basic constructor;
    public Note(Context context){
        MyResources r = new MyResources(context);
        this.title = context.getString(R.string.titleDefault).trim();
        this.content = "";
        this.iconUID = r.GET_ICON_LIST().get(0).uid;
        this.creationDate = new MyDate().GET_CURRENT_DATE();
        this.lastModifiedDate = new MyDate().GET_CURRENT_DATE();
        this.category = r.GET_CATEGORY_BY_UID(MyResources.DEFAULT_CATEGORY.UID);
        this.checkList = new ArrayList<>();
    }

    Icon getIcon(Context context){
        // fetch and return the correspondent icon from MyResources

        MyResources r = new MyResources(context);
        return r.GET_ICON(this.iconUID);
    }

    // return the number of done items in the checklist
    public int getDoneCount(){
        int x = 0;
        for (CheckListItem item : checkList) {
            if (item.isDone) x++;
        }
        return x;
    }
}
