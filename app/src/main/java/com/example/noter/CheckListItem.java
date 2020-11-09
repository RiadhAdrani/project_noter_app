package com.example.noter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class CheckListItem implements Serializable, Parcelable {

    // Object defining an item of a check list

    // description of the item
    public String text;

    // defines whether the item is done or not
    Boolean isDone;

    // creation date
    Date dateCreated;

    // finishing date
    Date dateDone;

    // public constructor
    public CheckListItem(String text) {
        this.text = text;
        isDone = false;
        dateCreated = new MyDate().GET_CURRENT_DATE();
    }

    // parcelable constructor
    // build the object by assembling primitive data types
    protected CheckListItem(Parcel in) {
        text = in.readString();

        byte tmpIsDone = in.readByte();
        isDone = tmpIsDone == 0 ? null : tmpIsDone == 1;

        dateCreated = new Date(in.readLong());

        dateDone = new Date(in.readLong());
    }

    // deconstruct the object to primitive data types
    // so it is easier to send it to fragments/Activities
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeByte((byte) (isDone == null ? 0 : isDone ? 1 : 2));
        dest.writeLong(dateCreated.getTime());

        try {
            dest.writeLong(dateDone.getTime());
        }catch (Exception e){
            Log.d("DEBUG_EXCEPTION","CheckListItem : Null Object Reference");
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckListItem> CREATOR = new Creator<CheckListItem>() {
        @Override
        public CheckListItem createFromParcel(Parcel in) {
            return new CheckListItem(in);
        }

        @Override
        public CheckListItem[] newArray(int size) {
            return new CheckListItem[size];
        }
    };
}