package com.example.noter;

import android.content.Context;

import java.util.ArrayList;

public class MyResources {
    Context context;

    public MyResources(Context context){
        this.context = context;
    }

    private void fillIconList(ArrayList<Icon> list){
        // NOTER
        list.add(new Icon(R.drawable.icon_small,context.getString(R.string.ic_noter)));

        // WORKPLACE
        list.add(new Icon(R.drawable.ic_workplace_0,context.getString(R.string.ic_workplace_0)));
        list.add(new Icon(R.drawable.ic_workplace_1,context.getString(R.string.ic_workplace_1)));

        // OPTIONS
        list.add(new Icon(R.drawable.ic_options_0,context.getString(R.string.ic_options_0)));

        // SCREWDRIVER
        list.add(new Icon(R.drawable.ic_screwdriver_0,context.getString(R.string.ic_screwdriver_0)));

    }

    ArrayList<Icon> GET_ICON_LIST(){
        ArrayList<Icon> list = new ArrayList<Icon>();
        fillIconList(list);
        return list;
    }

}
