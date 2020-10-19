package com.example.noter;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class MyResources implements Serializable {
    Context context;
    private ArrayList<Icon> ICON_LIST = new ArrayList<>();

    public MyResources(Context context){
        this.context = context;
        fillIconList();
    }

    private void fillIconList(){
        ArrayList<Icon> list = new ArrayList<>();
        // NOTER
        list.add(new Icon(R.drawable.icon_small,context.getString(R.string.ic_noter),"ic_default"));

        // WORKPLACE
        list.add(new Icon(R.drawable.ic_workplace_0,context.getString(R.string.ic_workplace_0),"ic_workplace_0"));
        list.add(new Icon(R.drawable.ic_workplace_1,context.getString(R.string.ic_workplace_1),"ic_workplace_1"));

        // OPTIONS
        list.add(new Icon(R.drawable.ic_options_0,context.getString(R.string.ic_options_0),"ic_options_0"));

        // SCREWDRIVER
        list.add(new Icon(R.drawable.ic_screwdriver_0,context.getString(R.string.ic_screwdriver_0),"ic_screwdriver_0"));

        // COMPUTER
        list.add(new Icon(R.drawable.ic_computer_0,context.getString(R.string.ic_computer_0),"ic_computer_0"));

        // CENTRAL UNIT
        list.add(new Icon(R.drawable.ic_cu_0,context.getString(R.string.ic_cu_0),"ic_centralUnit_0"));

        // MONITOR
        list.add(new Icon(R.drawable.ic_monitor_0,context.getString(R.string.ic_monitor_0),"ic_monitor_0"));

        // TELEPHONE
        list.add(new Icon(R.drawable.ic_telephone_0,context.getString(R.string.ic_telephone_0),"ic_telephone_0"));
        list.add(new Icon(R.drawable.ic_telephone_1,context.getString(R.string.ic_telephone_1),"ic_telephone_1"));

        // DARK SOULS
        list.add(new Icon(R.drawable.ic_bonfire_0,context.getString(R.string.ic_bonfire_0),"ic_bonfire_0"));

        ICON_LIST = list;
    }

    ArrayList<Icon> GET_ICON_LIST(){
        return ICON_LIST;
    }

    Icon GET_ICON(String uid){
        for (Icon icon : ICON_LIST) {
            if (uid.equals(icon.uid)) return icon;
        }
        return ICON_LIST.get(0);
    }


}
