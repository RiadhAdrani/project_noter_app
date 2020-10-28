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

    // static keys
    public static String NOTE_KEY = "NOTER_NOTE";
    public static String CATEGORY_KEY = "NOTER_CATEGORY";
    public static String LOAD_NOTE_KEY = "LOAD_NOTE";

    // Default category
    private static String ALL_CATEGORY_NAME = "All";
    private static String ALL_CATEGORY_UID = "com.example.noter.all.category";
    public static Category ALL_CATEGORY = new Category(ALL_CATEGORY_NAME, ALL_CATEGORY_UID);

    // Default category
    private static String DEFAULT_CATEGORY_NAME = "Default";
    private static String DEFAULT_CATEGORY_UID = "com.example.noter.default.category";
    public static Category DEFAULT_CATEGORY = new Category(DEFAULT_CATEGORY_NAME, DEFAULT_CATEGORY_UID);

    // Add new category
    private static String ADD_CATEGORY_NAME = "+ CATEGORY";
    private static String ADD_CATEGORY_UID = "com.example.noter.add.category";
    public static Category ADD_CATEGORY = new Category(ADD_CATEGORY_NAME, ADD_CATEGORY_UID);

    // Contains the list of available icons
    private ArrayList<Icon> ICON_LIST = new ArrayList<>();

    // ------------------------------------------------------------------------------------------ //
    //                                         METHODS                                            //
    // ------------------------------------------------------------------------------------------ //

    public MyResources(Context context){
        // Constructor

        this.context = context;

        // fill the icon list
        FILL_ICON_LIST();
    }

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

    public void COPY_NOTE_CONTENT(int position, ArrayList<Note> mList){
        // Copy the content of the Note to the Clipboard.

        // creating a new clipboard manager
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // store the content of the note in a temporary variable
        String mContent = mList.get(position).content;

        // getting the content
        ClipData clip = ClipData.newPlainText("Note Content",mContent );

        // assigning the data to the clipboard
        clipboard.setPrimaryClip(clip);

        // Alert the user of the success of the operation
        Toast.makeText(context,context.getString(R.string.copy_content_toast),Toast.LENGTH_LONG).show();
    }

    public void CREATE_NEW_NOTE(int position){
        // Create a new note
        // and pass the user to the NoteActivity

        Intent i = new Intent(context, NoteActivity.class);

        i.putExtra("note_index", position);

        // Start the activity
        context.startActivity(i);
    }

    public void SAVE_NOTE_TO_SHARED_PREFERENCES(ArrayList<Note> noteList,String key){
        // save the note list to the Shared Preference
        // Uses Gson with Json

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(noteList);

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        editor.putString(key,json);

        editor.apply();
    }

    public void LOAD_NOTE_IN_NOTE_ACTIVITY(int position,ArrayList<Note> noteList, Note note){
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

    public ArrayList<Note> LOAD_NOTE_FROM_SHARED_PREFERENCES(String key){
        // Load the note list from the Shared Preference
        // Uses Gson with Json

        // Temporary list
        ArrayList<Note> noteList ;

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString(key,null);

        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        noteList = gson.fromJson(json,type);

        if (noteList == null){
            return new ArrayList<>();
        } else
            return noteList;
    }

    public ArrayList<Icon> GET_ICON_LIST(){
        // return the icon list

        return ICON_LIST;
    }

    public Icon GET_ICON(String uid){
        // return an icon
        // with the property Icon.uid equals to uid

        for (Icon icon : ICON_LIST) {
            if (uid.equals(icon.uid)) return icon;
        }
        return ICON_LIST.get(0);
    }

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

    public int GET_CATEGORY_INDEX_BY_UID(Category mCategory, ArrayList<Category> categoryList){
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

    public int GET_CATEGORY_INDEX_BY_NAME(Category mCategory, ArrayList<Category> categoryList){
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

    public ArrayList<Note> FILTER_NOTES_BY_CATEGORY(ArrayList<Note> noteList, Category category ){
        // Filter a given note list
        // return a list containing notes having their category equal to
        // the passed category

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

    public boolean CHECK_IF_NAME_EXIST(ArrayList<Category> categoryList, String name){

        if (categoryList.isEmpty()) return false;

        for (Category category : categoryList) {
            if (category.name.equals(name)) return true;
        }

        return false;

    }

    public String GET_SUITABLE_NAME(String name, ArrayList<Category> list){
        // Check for duplicate name
        // and return the appropriate name

        if (this.CHECK_IF_NAME_EXIST(list,name)) {
            Log.d("DEBUG_CATEGORY_OPTIONS","Category named :"+name+" exists");
            return GET_SUITABLE_NAME(name +" "+context.getString(R.string.copy),list);
        }


        return name;

    }

    public void SAVE_NOTES_TO_SHARED_PREFERENCES(ArrayList<Note> list, String TAG){

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

    public ArrayList<Note> LOAD_NOTES_FROM_SHARED_PREFERENCES(String TAG){
        // Load the note list from the Shared Preference
        // Uses Gson with Json

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

    public Note GET_NOTE(ArrayList<Note> list,Note note){
        for (Note n : list) {
            if (note.equals(n)) return n;
        }

        return new Note(context);
    }

    public int GET_NOTE_INDEX(ArrayList<Note> list,Note note){

        if (list.isEmpty()) return -1;

        for (Note n : list) {
            if (n.title.equals(note.title) &&
                n.category.equals(note.category) &&
                n.creationDate.equals(note.creationDate) &&
                n.lastModifiedDate.equals(note.lastModifiedDate) &&
                n.content.equals(note.content) &&
                n.iconUID.equals(note.iconUID) ){

                Log.d("DEBUG_NOTE_ACTIVITY","Note found !");
                return list.indexOf(n);
            }
        }

        return -1;
    }

    public ArrayList<Note> QUICK_RANDOMIZED_SORTING_BY_MODIFICATION_DATE(ArrayList<Note> list){
        // Function that uses the randomized quick sort algorithm
        // to sort a list of notes by
        // the Note.modificationDate property
        // Uses Recursive call

        if (list.size() <= 1) return list;
        ArrayList<Note> high = new ArrayList<>();
        ArrayList<Note> low = new ArrayList<>();
        ArrayList<Note> equal = new ArrayList<>();
        Note e = list.get( (int) (Math.random() *list.size()) );

        for (Note x : list) {
            if (x.creationDate.getTime() < e.creationDate.getTime()) low.add(x);
            if (x.creationDate.getTime() > e.creationDate.getTime()) high.add(x);
            if (x.creationDate.getTime() == e.creationDate.getTime()) equal.add(x);
        }

        ArrayList<Note> temp = new ArrayList<>();
        temp.addAll(QUICK_RANDOMIZED_SORTING_BY_MODIFICATION_DATE(low));
        temp.addAll(equal);
        temp.addAll(QUICK_RANDOMIZED_SORTING_BY_MODIFICATION_DATE(high));

        return temp;
    }

    public ArrayList<Category> CREATE_DUMMY_CATEGORY_LIST(){
        ArrayList<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category("Default",true));
        categoryList.add(new Category("Studies",true));
        categoryList.add(new Category("Sports",true));
        categoryList.add(new Category("Habits",false));
        categoryList.add(new Category("MF",false));

        return categoryList;
    }

    public ArrayList<Icon> CREATE_DUMMY_ICON_LIST(int n){

        ArrayList<Icon> iconList = new ArrayList<>();

        for (int i = 0 ; i < n ; i++){
            iconList.add(this.GET_ICON_LIST().get( (int) (Math.random()*this.GET_ICON_LIST().size()) ));
        }

        return iconList;

    }

    public ArrayList<Note> QUICK_RANDOMIZED_SORTING_BY_ALPHA(ArrayList<Note> list){
        // Function that uses the randomized quick sort algorithm
        // to sort a list of notes by
        // the Note.title property
        // Uses Recursive call

        if (list.size() <= 1) return list;
        ArrayList<Note> high = new ArrayList<>();
        ArrayList<Note> low = new ArrayList<>();
        ArrayList<Note> equal = new ArrayList<>();
        Note e = list.get( (int) (Math.random() *list.size()) );
        for (Note x : list) {
            switch (STRING_COMPARISON(x.title,e.title)){
                case  0: equal.add(x)   ; break;
                case -1: low.add(x)     ; break;
                case  1: high.add(x)    ; break;
            }
        }

        ArrayList<Note> temp = new ArrayList<>();
        temp.addAll(QUICK_RANDOMIZED_SORTING_BY_ALPHA(low));
        temp.addAll(equal);
        temp.addAll(QUICK_RANDOMIZED_SORTING_BY_ALPHA(high));

        return temp;
    }

    public int STRING_COMPARISON(String a, String b){
        // Function used to compare two strings (a) and (b)
        // Custom made for the Randomized Quick Sorting Function
        // return -1 if (a) is alphabetically before (b)
        // return  0 if (a) is alphabetically after  (b)
        // return  1 if (a) is equal to              (b)

        int size;
        size = Math.min(a.length(), b.length());
        for (int i = 0; i < size; i++) {
            if (a.toLowerCase().charAt(i) < b.toLowerCase().charAt(i)) return -1; // A is before B
            if (a.toLowerCase().charAt(i) > b.toLowerCase().charAt(i)) return  1; // A is after B
        }
        if (a.length() < b.length()) return -1;
        else if (a.length() > b.length()) return 1;

        return 0;
    }

    public ArrayList<Note> QUICK_RANDOMIZED_SORTING_BY_CREATION_DATE(ArrayList<Note> list){
        // Function that uses the randomized quick sort algorithm
        // to sort a list of notes by
        // the Note.creationDate property
        // Uses Recursive call

        if (list.size() <= 1) return list;
        ArrayList<Note> high = new ArrayList<>();
        ArrayList<Note> low = new ArrayList<>();
        ArrayList<Note> equal = new ArrayList<>();
        Note e = list.get( (int) (Math.random() *list.size()) );

        for (Note x : list) {
            if (x.creationDate.getTime() < e.creationDate.getTime()) low.add(x);
            if (x.creationDate.getTime() > e.creationDate.getTime()) high.add(x);
            if (x.creationDate.getTime() == e.creationDate.getTime()) equal.add(x);
        }

        ArrayList<Note> temp = new ArrayList<>();
        temp.addAll(QUICK_RANDOMIZED_SORTING_BY_CREATION_DATE(low));
        temp.addAll(equal);
        temp.addAll(QUICK_RANDOMIZED_SORTING_BY_CREATION_DATE(high));

        return temp;
    }

}
