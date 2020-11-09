package com.example.noter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

    // static keys for SharedPreferences
    // and transferring data from an Activity to another.
    public static String NOTE_LIST_KEY = "NOTER_NOTE";
    public static String CATEGORY_KEY = "NOTER_CATEGORY";
    public static String LOAD_NOTE_KEY = "LOAD_NOTE";
    public static String CURRENT_CATEGORY_KEY = "CURRENT_CATEGORY";
    public static String CURRENT_SORTING_KEY = "CURRENT_SORTING";
    public static String CURRENT_SORTING_TYPE_KEY = "CURRENT_SORTING_TYPE";

    // Default category
    private static final String ALL_CATEGORY_NAME = "All";
    private static final String ALL_CATEGORY_UID = "com.example.noter.all.category";
    public static Category ALL_CATEGORY = new Category(ALL_CATEGORY_NAME, ALL_CATEGORY_UID);

    // Default category
    private static final String DEFAULT_CATEGORY_NAME = "Default";
    private static final String DEFAULT_CATEGORY_UID = "com.example.noter.default.category";
    public static Category DEFAULT_CATEGORY = new Category(DEFAULT_CATEGORY_NAME, DEFAULT_CATEGORY_UID);

    // Add new category
    private static final String ADD_CATEGORY_NAME = "+ CATEGORY";
    private static final String ADD_CATEGORY_UID = "com.example.noter.add.category";
    public static Category ADD_CATEGORY = new Category(ADD_CATEGORY_NAME, ADD_CATEGORY_UID);

    // static int
    public static final int CATEGORY_MINIMUM_NAME_LENGTH = 3;
    public static final int NOTE_MINIMUM_NAME_LENGTH = 2;

    // Sorting types
    public static int SORT_ALPHA = 0;
    public static int SORT_CREATION_DATE = 1;
    public static int SORT_MODIFICATION_DATE = 2;

    // Contains the list of available icons
    private ArrayList<Icon> ICON_LIST = new ArrayList<>();

    // ------------------------------------------------------------------------------------------ //
    //                                         METHODS                                            //
    // ------------------------------------------------------------------------------------------ //

    // Basic constructor for MyResource
    public MyResources(Context context){
        // Constructor

        this.context = context;

        // fill the icon list
        FILL_ICON_LIST();
    }

    // ------------------------------------------------------------------------------------------//

    // Load an integer from the Shared Preference
    // Uses Gson with Json
    public Integer LOAD_INTEGER_FROM_SHARED_PREFERENCES(String TAG){

        Integer integer;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(TAG,null);

        Type type = new TypeToken<Integer>() {}.getType();
        integer = gson.fromJson(json,type);

        if (integer == null) return 0;

        return integer;
    }

    // ------------------------------------------------------------------------------------------//

    // Save an integer to sharedPreferences with the key TAG
    public void SAVE_INTEGER_TO_SHARED_PREFERENCES(Integer integer, String TAG){

        // save the categories to the Shared Preference
        // Uses Gson with Json

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(integer);

        // TAG
        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        editor.putString(TAG,json);

        editor.apply();
    }

    // ------------------------------------------------------------------------------------------//

    // Load a boolean from the Shared Preference
    // Uses Gson with Json
    public Boolean LOAD_BOOLEAN_FROM_SHARED_PREFERENCES(String TAG){

        Boolean bool;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(TAG,null);

        Type type = new TypeToken<Boolean>() {}.getType();
        bool = gson.fromJson(json,type);

        if (bool == null) return true;

        return bool;
    }

    // ------------------------------------------------------------------------------------------//

    // Save a boolean to sharedPreferences with the key TAG
    public void SAVE_BOOLEAN_TO_SHARED_PREFERENCES(Boolean bool, String TAG){

        // save the categories to the Shared Preference
        // Uses Gson with Json

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bool);

        // TAG
        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        editor.putString(TAG,json);

        editor.apply();
    }

    // ------------------------------------------------------------------------------------------//

    // Load the a single category from the Shared Preference
    // Uses Gson with Json
    public Category LOAD_SINGLE_CATEGORY_FROM_SHARED_PREFERENCES(String TAG){

        // Temporary list
        Category category ;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(TAG,null);

        Type type = new TypeToken<Category>() {}.getType();
        category = gson.fromJson(json,type);

        if (category == null){
            return ALL_CATEGORY;
        } else
            return category;
    }

    // ------------------------------------------------------------------------------------------//

    // Save a single category to sharedPreferences with the key TAG
    public void SAVE_SINGLE_CATEGORY_TO_SHARED_PREFERENCES(Category category, String TAG){

        // save the categories to the Shared Preference
        // Uses Gson with Json

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(category);

        // TAG
        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        editor.putString(TAG,json);

        editor.apply();
    }

    // ------------------------------------------------------------------------------------------//

    // checking if a note with title "name" exist in "list"
    public boolean CHECK_IF_NOTE_TITLE_EXIST(ArrayList<Note> list,String title){
        for (Note note : list) {
            if (title.equals(note.title)) return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------------------------//

    // checking if "name" exist in "list"
    // and trying to solve the duplicate names by adding
    // a string (copy)
    public String GET_SUITABLE_NOTE_NAME(String name, ArrayList<Note> list){
        // Check for duplicate name
        // and return the appropriate name

        name = name.trim();

        if (this.CHECK_IF_NOTE_TITLE_EXIST(list,name)) {

            return GET_SUITABLE_NOTE_NAME(name +" "+context.getString(R.string.copy),list);
        }

        return name.trim();

    }

    // ------------------------------------------------------------------------------------------//

    // return the index of a "mCategory" in "categoryList" by its name
    public int FIND_CATEGORY_BY_NAME(ArrayList<Category> categoryList,Category mCategory){
        // Return the index of the note category
        // found in categoryList

        for (Category c : categoryList) {

            // if the two names matches
            // return the index of the current element

            if (c.name.trim().equals(mCategory.name.trim())){

                Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

                return categoryList.indexOf(c);
            }
        }

        Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

        return -1;
    }

    // ------------------------------------------------------------------------------------------//

    // Save "categoryList" to SharedPreferences after removing DEFAULT_CATEGORY and ADD_CATEGORY
    public void SAVE_CATEGORIES(ArrayList<Category> categoryList,String key){
        // Prepare the category list to be saved
        // Saving only the custom made categories

        // Removing the Default category
        categoryList.remove(FIND_CATEGORY_BY_UID(categoryList,DEFAULT_CATEGORY));

        // Removing the "Add category" category
        categoryList.remove(FIND_CATEGORY_BY_UID(categoryList,ADD_CATEGORY));

        // Save category list from SharedPreferences
        SAVE_CATEGORIES_TO_SHARED_PREFERENCES(categoryList,key);
    }

    // ------------------------------------------------------------------------------------------//

    // return the index of a "mCategory" in "categoryList" by its UID
    public int FIND_CATEGORY_BY_UID(ArrayList<Category> categoryList,Category mCategory){
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

    // ------------------------------------------------------------------------------------------//

    // Return "categoryList" from SharedPreferences after adding DEFAULT_CATEGORY and ADD_CATEGORY
    public ArrayList<Category> LOAD_CATEGORY_LOCALLY(){
        // Prepare the category list to be used in the activity

        // Load category list from SharedPreferences
        ArrayList<Category> categoryList = LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(MyResources.CATEGORY_KEY);

        // Add the Default Category
        categoryList.add(0,MyResources.DEFAULT_CATEGORY);

        // Add the "ADD CATEGORY"
        categoryList.add(0,MyResources.ADD_CATEGORY);

        return categoryList;
    }

    // ------------------------------------------------------------------------------------------//

    // Copy the content of the note given by it's "position" in "mList"
    // to the clipboard
    public void COPY_NOTE_CONTENT(int position, ArrayList<Note> mList){
        // Copy the content of the Note to the Clipboard.

        // creating a new clipboard manager
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // store the content of the note in a temporary variable
        String mContent = mList.get(position).content.trim();

        // getting the content
        ClipData clip = ClipData.newPlainText("Note Content",mContent );

        // assigning the data to the clipboard
        clipboard.setPrimaryClip(clip);

        // Alert the user of the success of the operation
        Toast.makeText(context,context.getString(R.string.copy_content_toast),Toast.LENGTH_LONG).show();
    }

    // ------------------------------------------------------------------------------------------//

    // Load NoteActivity with the note "note"
    public void LOAD_NOTE_IN_NOTE_ACTIVITY(ArrayList<Note> noteList, Note note){
        // Load a note in the NoteActivity

        // getting the index of the list
        int j = GET_NOTE_INDEX(noteList,note);

        // creating an intent
        Intent i = new Intent(context, NoteActivity.class);

        // adding data to the intent
        i.putExtra(LOAD_NOTE_KEY, j );

        // Start the activity
        context.startActivity(i);
    }


    // ------------------------------------------------------------------------------------------//

    // return the list of the icons
    public ArrayList<Icon> GET_ICON_LIST(){
        // return the icon list

        return ICON_LIST;
    }

    // ------------------------------------------------------------------------------------------//

    // return an icon by its uid
    public Icon GET_ICON(String uid){
        // return an icon
        // with the property Icon.uid equals to uid

        for (Icon icon : ICON_LIST) {
            if (uid.equals(icon.uid)) return icon;
        }
        return ICON_LIST.get(0);
    }

    // ------------------------------------------------------------------------------------------//

    // Save category list to sharedPreferences with the key TAG
    public void SAVE_CATEGORIES_TO_SHARED_PREFERENCES(ArrayList<Category> list, String TAG){

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

    // ------------------------------------------------------------------------------------------//

    // Return category list from sharedPreferences using the key TAG
    public ArrayList<Category> LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(String TAG){
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

    // ------------------------------------------------------------------------------------------//

    // Return the UID of a Category from SharedPreferences by its UID, passed as parameter
    public String GET_CATEGORY_BY_UID(String uid){
        for (Category category : LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(CATEGORY_KEY)) {
            if (category.UID.equals(uid)){
                Log.d("DEBUG_FIND_CATEGORY","Category "+ category.name + "found !");
                return category.UID;
            }
        }

        Log.d("DEBUG_FIND_CATEGORY","Could not find category's ID, replaced by DEFAULT CATEGORY");
        return DEFAULT_CATEGORY.UID;
    }

    // ------------------------------------------------------------------------------------------//

    // Return a category from SharedPreferences by its UID, passed as parameter
    public Category GET_CATEGORY(String uid){
        for (Category category : LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(CATEGORY_KEY)) {
            if (category.UID.equals(uid)){
                Log.d("DEBUG_FIND_CATEGORY","Category "+ category.name + "found !");
                return category;
            }
        }

        Log.d("DEBUG_FIND_CATEGORY","Could not find category's ID, replaced by DEFAULT CATEGORY");
        return DEFAULT_CATEGORY;
    }

    // ------------------------------------------------------------------------------------------//

    // Return "categoryList" from SharedPreferences after adding DEFAULT_CATEGORY and ALL_CATEGORY
    public ArrayList<Category> GET_CATEGORY_LIST(){
        // Prepare the category list to be used in the activity

        ArrayList<Category> categoryList;

        // Load category list from SharedPreferences
        categoryList = LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(MyResources.CATEGORY_KEY);

        // Add the Default Category
        categoryList.add(0,MyResources.DEFAULT_CATEGORY);

        // Add the Default Category
        categoryList.add(0,MyResources.ALL_CATEGORY);

        return categoryList;
    }

    // ------------------------------------------------------------------------------------------//

    // Save "categoryList" to SharedPreferences after removing DEFAULT_CATEGORY and ALL_CATEGORY
    public void SAVE_CATEGORY_LIST(ArrayList<Category> categoryList){
        // Prepare the category list to be saved
        // Saving only the custom made categories

        // Removing the Default category
        categoryList.remove(GET_CATEGORY_INDEX_BY_UID(DEFAULT_CATEGORY,categoryList));

        // Removing the all category
        categoryList.remove(GET_CATEGORY_INDEX_BY_UID(ALL_CATEGORY,categoryList));

        // Save category list from SharedPreferences
        SAVE_CATEGORIES_TO_SHARED_PREFERENCES(categoryList,MyResources.CATEGORY_KEY);
    }

    // ------------------------------------------------------------------------------------------//

    // Return the index of the note category
    // found in categoryList by checking
    // just the UID
    public int GET_CATEGORY_INDEX_BY_UID(Category mCategory, ArrayList<Category> categoryList){

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

    // ------------------------------------------------------------------------------------------//

    // Return the index of the note category
    // found in categoryList by checking
    // just the name
    public int GET_CATEGORY_INDEX_BY_NAME(Category mCategory, ArrayList<Category> categoryList){


        for (Category c : categoryList) {

            // if the two names matches
            // return the index of the current element

            if (c.name.trim().equals(mCategory.name.trim())){

                Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

                return categoryList.indexOf(c);
            }
        }

        Log.d("DEBUG_NEW_CATEGORY","Category "+ mCategory.name +" found !");

        return -1;
    }

    // ------------------------------------------------------------------------------------------//

    // Filter a given note list
    // return a list containing notes with category equal to "category"
    // the passed category
    public ArrayList<Note> FILTER_NOTES_BY_CATEGORY(ArrayList<Note> noteList, Category category ){

        if (noteList == null || noteList.isEmpty()){
            // if the noteList is empty
            // return an empty array
            // avoiding null object exception
            return new ArrayList<>();
        }

        if (category == ALL_CATEGORY){
            // display all notes

            return noteList;
        }

        ArrayList<Note> tempList = new ArrayList<>();

        for (Note note : noteList) {
            // filter note by category

            if (note.category.equals(category.UID)) tempList.add(note);
        }

        return tempList;
    }

    // ------------------------------------------------------------------------------------------//

    // Check if "name" exist in "categoryList"
    // return true if it exist
    // return false if not
    // return false if the list is empty
    public boolean CHECK_IF_NAME_EXIST(ArrayList<Category> categoryList, String name){

        name = name.trim();

        if (categoryList.isEmpty()) return false;

        for (Category category : categoryList) {
            if (category.name.trim().equals(name)) return true;
        }

        return false;

    }

    // ------------------------------------------------------------------------------------------//

    // checking if "name" exist in "list"
    // and trying to solve the duplicate names by adding
    // a string (copy)
    public String GET_SUITABLE_NAME(String name, ArrayList<Category> list){
        // Check for duplicate name
        // and return the appropriate name

        name = name.trim();

        if (this.CHECK_IF_NAME_EXIST(list,name)) {
            Log.d("DEBUG_CATEGORY_OPTIONS","Category named :"+name+" exists");
            return GET_SUITABLE_NAME(name +" "+context.getString(R.string.copy),list);
        }


        return name.trim();

    }

    // ------------------------------------------------------------------------------------------//

    // save the categories to the Shared Preference
    // Uses Gson with Json
    public void SAVE_NOTES_TO_SHARED_PREFERENCES(ArrayList<Note> list, String TAG){

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

    // ------------------------------------------------------------------------------------------//

    // Load the note list from the Shared Preference
    // Uses Gson with Json
    public ArrayList<Note> LOAD_NOTES_FROM_SHARED_PREFERENCES(String TAG){

        // Temporary list
        ArrayList<Note> list ;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(TAG,null);

        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        list = gson.fromJson(json,type);

        if (list == null){
            return new ArrayList<>();
        } else
            return list;
    }

    // ------------------------------------------------------------------------------------------//

    // return the index of "note" in the ArrayList "list"
    // return -1 if "note" does not exist in "list"
    // Replaces (Class) Note.equals()
    public int GET_NOTE_INDEX(ArrayList<Note> list,Note note){

        // return -1 if list is empty
        if (list.isEmpty()) return -1;

        // comparing the elements one by one
        // to assure complete resemblance
        for (Note n : list) {
            if (n.title.trim().equals(note.title.trim()) &&
                n.category.equals(note.category) &&
                n.creationDate.equals(note.creationDate) &&
                n.lastModifiedDate.equals(note.lastModifiedDate) &&
                n.content.trim().equals(note.content.trim()) &&
                n.iconUID.equals(note.iconUID) ){

                Log.d("DEBUG_NOTE_ACTIVITY","Note found !");
                // returning the found element
                return list.indexOf(n);
            }
        }

        // if not found
        // return -1
        return -1;
    }


    // ------------------------------------------------------------------------------------------//

    // ------------------------------------------------------------------------------------------//

    // FUNCTION 00
    // fill ICON_LIST with all the icons
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
        list.add(new Icon(R.drawable.ic_newnote_0,context.getString(R.string.ic_noter),"ic_default_0"));

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

        // NOTER
        list.add(new Icon(R.drawable.ic_noter_app_0,context.getString(R.string.ic_noter),"ic_noter_0"));

        // ---------------------------------------------------------------------------------------- //
        // ---------------------------------------------------------------------------------------- //

        ICON_LIST = list;
    }

}
