package com.example.noter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyResources implements Serializable {
    // Class used to handle static resources
    // such as drawable ...
    // Works as the database of the Application

    // Very important
    Context context;

    // ------------------------------------------------------------------------------------------ //
    //                                         STATICS                                            //
    // ------------------------------------------------------------------------------------------ //

    // static keys
    public static String NOTE_KEY = "NOTER_NOTE";
    public static String CATEGORY_KEY = "NOTER_CATEGORY";

    // Default category
    private static String DEFAULT_CATEGORY_NAME = "DEFAULT";
    private static String DEFAULT_CATEGORY_UID = "com.example.noter.default.category";
    public static Category DEFAULT_CATEGORY = new Category(DEFAULT_CATEGORY_NAME, DEFAULT_CATEGORY_UID);

    // Add new category
    private static String ADD_CATEGORY_NAME = "+ CATEGORY";
    private static String ADD_CATEGORY_UID = "com.example.noter.add.category";
    public static Category ADD_CATEGORY = new Category(ADD_CATEGORY_NAME, ADD_CATEGORY_UID);

    // Contains the list of available icons
    private ArrayList<Icon> ICON_LIST = new ArrayList<>();

    public MyResources(Context context){
        // Constructor

        this.context = context;

        // fill the icon list
        FILL_ICON_LIST();
    }


    // ------------------------------------------------------------------------------------------ //
    //                                         METHODS                                            //
    // ------------------------------------------------------------------------------------------ //

    private void FILL_ICON_LIST(){
        // Construct the icon list

        // temp Icon list
        ArrayList<Icon> list = new ArrayList<>();

        // ICON CATEGORIES
        // ADD YOURS AT THE CUSTOM PART
        // DO NOT MODIFY ANY LINE IN THE STATIC PART

        // ---------------------------------------------------------------------------------------- //
        //                                          STATIC                                          //
        // ---------------------------------------------------------------------------------------- //

        // NOTER
        list.add(new Icon(R.drawable.icon_small,context.getString(R.string.ic_noter),"ic_default"));

        // WORKPLACE
        list.add(new Icon(R.drawable.ic_workplace_0,context.getString(R.string.ic_workplace),"ic_workplace_0"));
        list.add(new Icon(R.drawable.ic_workplace_1,context.getString(R.string.ic_workplace),"ic_workplace_1"));

        // OPTIONS
        list.add(new Icon(R.drawable.ic_options_0,context.getString(R.string.ic_options_0),"ic_options_0"));

        // SCREWDRIVER
        list.add(new Icon(R.drawable.ic_screwdriver_0,context.getString(R.string.ic_screwdriver),"ic_screwdriver_0"));

        // COMPUTER
        list.add(new Icon(R.drawable.ic_computer_0,context.getString(R.string.ic_computer),"ic_computer_0"));

        // CENTRAL UNIT
        list.add(new Icon(R.drawable.ic_cu_0,context.getString(R.string.ic_cu),"ic_centralUnit_0"));

        // MONITOR
        list.add(new Icon(R.drawable.ic_monitor_0,context.getString(R.string.ic_monitor),"ic_monitor_0"));

        // TELEPHONE
        list.add(new Icon(R.drawable.ic_telephone_0,context.getString(R.string.ic_telephone),"ic_telephone_0"));
        list.add(new Icon(R.drawable.ic_telephone_1,context.getString(R.string.ic_telephone),"ic_telephone_1"));

        // SMART PHONES
        list.add(new Icon(R.drawable.ic_smartphone_0,context.getString(R.string.ic_smartphone),"ic_smartPhone_0"));
        list.add(new Icon(R.drawable.ic_smartphone_1,context.getString(R.string.ic_smartphone),"ic_smartPhone_1"));

        // FOLDER
        list.add(new Icon(R.drawable.ic_folder_0,context.getString(R.string.ic_folder),"ic_folder_0"));
        list.add(new Icon(R.drawable.ic_folder_1,context.getString(R.string.ic_folder),"ic_folder_1"));

        // FOLDER
        list.add(new Icon(R.drawable.ic_file_0,context.getString(R.string.ic_file),"ic_file_0"));
        list.add(new Icon(R.drawable.ic_file_1,context.getString(R.string.ic_file),"ic_file_1"));

        // LOCKER
        list.add(new Icon(R.drawable.ic_locker_0,context.getString(R.string.ic_locker),"ic_locker_0"));
        list.add(new Icon(R.drawable.ic_locker_1,context.getString(R.string.ic_locker),"ic_locker_1"));

        // KEY
        list.add(new Icon(R.drawable.ic_key_0,context.getString(R.string.ic_key),"ic_key_0"));
        list.add(new Icon(R.drawable.ic_key_1,context.getString(R.string.ic_key),"ic_key_1"));

        // COFFEE
        list.add(new Icon(R.drawable.ic_coffee_0,context.getString(R.string.ic_coffee),"ic_coffee_0"));
        list.add(new Icon(R.drawable.ic_coffee_1,context.getString(R.string.ic_coffee),"ic_coffee_1"));

        // ---------------------------------------------------------------------------------------- //
        //                                          CUSTOM                                          //
        // ---------------------------------------------------------------------------------------- //

        // DARK SOULS
        list.add(new Icon(R.drawable.ic_bonfire_0,context.getString(R.string.ic_bonfire),"ic_bonfire_0"));

        // ---------------------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------------------- //

        ICON_LIST = list;
    }

    ArrayList<Icon> GET_ICON_LIST(){
        // return the icon list

        return ICON_LIST;
    }

    Icon GET_ICON(String uid){
        // return an icon
        // with the property Icon.uid equals to uid

        for (Icon icon : ICON_LIST) {
            if (uid.equals(icon.uid)) return icon;
        }
        return ICON_LIST.get(0);
    }

    void SAVE_CATEGORIES_TO_SHARED_PREFERENCES(ArrayList<Category> list, String TAG){

        // save the categories to the Shared Preference
        // Uses Gson with Json

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);

        // TAG
        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        editor.putString(TAG,json);

        editor.apply();
    }

    ArrayList<Category> LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(String TAG){
        // Load the note list from the Shared Preference
        // Uses Gson with Json

        // Temporary list
        ArrayList<Category> list ;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(TAG,null);

        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        list = gson.fromJson(json,type);

        if (list == null){
            return new ArrayList<>();
        } else
            return list;
    }

    String GET_CATEGORY_BY_UID(String uid){
        for (Category category : LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(CATEGORY_KEY)) {
            if (category.UID.equals(uid)){
                Log.d("DEBUG_FIND_CATEGORY","Category "+ category.name + "found !");
                return category.UID;
            }
        }

        Log.d("DEBUG_FIND_CATEGORY","Could not find category's ID, replaced by DEFAULT CATEGORY");
        return DEFAULT_CATEGORY.UID;
    }

    Category GET_CATEGORY(String uid){
        for (Category category : LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(CATEGORY_KEY)) {
            if (category.UID.equals(uid)){
                Log.d("DEBUG_FIND_CATEGORY","Category "+ category.name + "found !");
                return category;
            }
        }

        Log.d("DEBUG_FIND_CATEGORY","Could not find category's ID, replaced by DEFAULT CATEGORY");
        return DEFAULT_CATEGORY;
    }

    ArrayList<Category> GET_CATEGORY_LIST(){
        // Prepare the category list to be used in the activity

        ArrayList<Category> categoryList;

        // Load category list from SharedPreferences
        categoryList = LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(MyResources.CATEGORY_KEY);

        // Add the Default Category
        categoryList.add(0,MyResources.DEFAULT_CATEGORY);

        return categoryList;
    }

    void SAVE_CATEGORY_LIST(ArrayList<Category> categoryList){
        // Prepare the category list to be saved
        // Saving only the custom made categories

        // Removing the Default category
        categoryList.remove(GET_CATEGORY_INDEX_BY_UID(DEFAULT_CATEGORY,categoryList));

        // Save category list from SharedPreferences
        SAVE_CATEGORIES_TO_SHARED_PREFERENCES(categoryList,MyResources.CATEGORY_KEY);
    }

    private int GET_CATEGORY_INDEX_BY_UID(Category mCategory, ArrayList<Category> categoryList){
        // Return the index of the note category
        // found in categoryList

        for (Category c : categoryList) {
            if (c.UID.equals(mCategory.UID)){

                // if the two UID matches
                // return the index of the current element
                Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

                return categoryList.indexOf(c);
            }
        }

        Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" NOT found !");

        return -1;
    }

    private int GET_CATEGORY_INDEX_BY_NAME(Category mCategory, ArrayList<Category> categoryList){
        // Return the index of the note category
        // found in categoryList

        for (Category c : categoryList) {

            // if the two names matches
            // return the index of the current element

            if (c.name.equals(mCategory.name)){

                Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

                return categoryList.indexOf(c);
            }
        }

        Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

        return -1;
    }


}
