package com.example.noter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatDialogFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity implements Serializable {

    // Activity Displaying the multiple notes of the selected category.

    MyResources MY_RESOURCES;

    // List of Notes
    ArrayList<Note> mList = new ArrayList<>();

    // RecyclerView used to load notes
    RecyclerView mRecyclerView;

    // Custom adapter
    // See NoteAdapter (Class) for more documentation
    NoteAdapter mAdapter;

    // LayoutManager which decides how notes are displayed
    RecyclerView.LayoutManager mLayoutManager;

    // Floating Action Button used to create notes
    FloatingActionButton fab;

    // List of Categories
    ArrayList<Category> cList = new ArrayList<>();

    // RecyclerView used to display categories
    RecyclerView cRecyclerView;

    // Custom Adapter
    // See CategoryAdapter (Class) for more documentation
    CategoryAdapter cAdapter;

    // LayoutManager which decides how notes are displayed
    RecyclerView.LayoutManager cLayoutManager;

    // Add Category Button
    ImageButton addCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // assign the layout of the activity
        // very important to assign the correct layout
        // because it will be used to fetch elements
        // could cause crashes if not set correctly
        setContentView(R.layout.main_activity_layout);

        MY_RESOURCES = new MyResources(this);

        // Setting the toolbar for the current activity
        // Could be customized via R.layout.main_activity_layout
        Toolbar toolbar = findViewById(R.id.category_toolbar);
        setActionBar(toolbar);

        // Loading notes from SharedPreferences
        mList = loadNoteFromSharedPreferences();

        // Loading Categories from SharePreferences
        cList = MY_RESOURCES.GET_CATEGORY_LIST();

        // Fetching the Floating Action bar in the layout
        fab = findViewById(R.id.category_fab);

        // Overriding the function of the Floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewNote(-1);
            }
        });

        // Referencing the button from the Activity layout
        addCategoryButton = findViewById(R.id.add_category_button);

        // Assigning the action of the button
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating an Simple Input Alert (custom class)
                // customizable hint text
                // For more documentation about how this works,
                // check SimpleInputDialog Java Class file

                SimpleInputDialog inputDialog = new SimpleInputDialog(getApplicationContext(),"tag");
            }
        });

        buildRecyclerView();
        BuildCategoryRecyclerView();

    }


    void BuildCategoryRecyclerView(){
        // Build The RecyclerView

        // find the recycler view in the layout of the activity
        cRecyclerView = findViewById(R.id.category_recyclerView);

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        cRecyclerView.setHasFixedSize(false);

        // Setting the Layout of the RecyclerView
        cLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        // Setting the Adapter of the RecyclerView
        cAdapter = new CategoryAdapter(cList);

        Log.d("DEBUG_CATEGORY_RV","Number of Elements : "+cAdapter.mList.size());

        cAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onClickListener(int position) {
                Toast.makeText(getApplicationContext(),"Category Clicked !",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickListener(int position) {
                Toast.makeText(getApplicationContext(),"Category Clicked & Hold !",Toast.LENGTH_SHORT).show();
            }
        });

        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Overriding the function of the Option menu

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting_option_menu,menu);
        return true;
    }

    @Override
    protected void onStart() {
        // Some Updates to be called when the activity Starts

        super.onStart();
        mList = loadNoteFromSharedPreferences();
    }

    @Override
    protected void onPause() {
        // Some Automatic saving

        super.onPause();
        saveNoteToSharedPreferences(mList);
    }

    @Override
    protected void onStop() {
        // Some Automatic saving

        super.onStop();
        saveNoteToSharedPreferences(mList);

    }

    @Override
    protected void onRestart() {
        // Some Updates to be called when the activity Starts

        super.onRestart();
        mList = loadNoteFromSharedPreferences();
        buildRecyclerView();
    }

    @Override
    protected void onResume() {
        // Some Updates to be called when the activity Starts

        super.onResume();
        mList = loadNoteFromSharedPreferences();
        buildRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Overriding the functionality of each option
        // in the Option Menu from The Toolbar

        switch (item.getItemId()){
            case R.id.sort_alpha: sortByAlpha(); return true;
            case R.id.sort_creation : sortByCreation(); return true;
            case R.id.sort_modified : sortByModified(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    void sortByAlpha(){
        // Sorts a list of Note by Note.title property
        // Sorts Ascending Only

        Collections.sort(mList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.title.compareTo(n2.title);
            }
        });

        // Save the list to the Shared Preferences
        saveNoteToSharedPreferences(mList);

        // Update The RecyclerView
        buildRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_title),Toast.LENGTH_SHORT).show();
    }

    void sortByCreation(){
        // Sorts a list of Note by Note.creationDate property
        // Sorts Ascending Only

        Collections.sort(mList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.creationDate.compareTo(n2.creationDate);
            }
        });

        // Save the list to the Shared Preferences
        saveNoteToSharedPreferences(mList);

        // Update The RecyclerView
        buildRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_creation),Toast.LENGTH_SHORT).show();
    }

    void sortByModified(){
        // Sorts a list of Note by Note.lastModificationDate property
        // Sorts Ascending Only

        Collections.sort(mList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.lastModifiedDate.compareTo(n2.lastModifiedDate);
            }
        });

        // Save the list to the Shared Preferences
        saveNoteToSharedPreferences(mList);

        // Update The RecyclerView
        buildRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_modification),Toast.LENGTH_SHORT).show();
    }

    void buildRecyclerView(){
        // Build The RecyclerView

        // find the recycler view in the layout of the activity
        mRecyclerView = findViewById(R.id.note_RecyclerView);

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        mRecyclerView.setHasFixedSize(false);

        // Setting the Layout of the RecyclerView
        mLayoutManager = new LinearLayoutManager(this);

        // Setting the Adapter of the RecyclerView
        mAdapter = new NoteAdapter(mList,this);

        // Setting the functions of the drop down menu of each element
        // See NoteAdapter Class for more documentation
        mAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                // display the drop down menu

                loadNote(position);
            }

            @Override
            public void onOptionClick(int position) {
                // Do nothing
                // This function could be deleted

                // deleteNote(position);
                // saveNoteToSharedPreferences(mList);
            }

            @Override

            public void onDuplicateClick(int position) {
                // Duplicate the note

                duplicateNote(position);
            }

            @Override
            public void onCopyContentClick(int position) {
                // Copy the content of the note

                copyNoteContent(position);
            }

            @Override
            public void onDeleteClick(int position) {
                // Delete the note

                deleteNote(position);
            }
        });

        // Setting the Layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the Adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    void duplicateNote(int position){
        // Duplicate a note by inserting the same note
        // in the same position

        mList.add(position,mList.get(position));

        // Need to be more optimized
        buildRecyclerView();

        // mAdapter.notifyDataSetChanged();
        // mAdapter.notifyItemInserted(position);
    }

    void copyNoteContent(int position){
        // Copy the content of the Note to the Clipboard.

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Note Content", mList.get(position).content);
        clipboard.setPrimaryClip(clip);

        // Alert the user of the success of the operation
        Toast.makeText(getApplicationContext(),getString(R.string.copy_content_toast),Toast.LENGTH_LONG).show();
    }

    void deleteNote(int position){
        // Remove a note by its position

        mList.remove(position);

        // Need to be more optimized
        buildRecyclerView();

        //mAdapter.notifyDataSetChanged();
        //mAdapter.notifyItemRemoved(position);
    }

    void createNewNote(int position){
        // Create a new note
        // and pass the user to the NoteActivity

        Intent i = new Intent(this, NoteActivity.class);


        i.putExtra("note", new Note(getApplicationContext()));
        i.putExtra("note_index", position);
        i.putExtra("note_list",mList);

        // Start the activity
        startActivity(i);
    }

    void loadNote(int position){
        // Load a note in the NoteActivity

        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra("note", mList.get(position));
        i.putExtra("note_index", position);
        i.putExtra("note_list",mList);

        // Start the activity
        startActivity(i);
    }

    void saveNoteToSharedPreferences(ArrayList<Note> noteList){
        // save the note list to the Shared Preference
        // Uses Gson with Json

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(noteList);

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        editor.putString("notes",json);

        editor.apply();
    }

    ArrayList<Note> loadNoteFromSharedPreferences(){
        // Load the note list from the Shared Preference
        // Uses Gson with Json

        // Temporary list
        ArrayList<Note> noteList ;

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();

        // The key of the Data
        // works as an ID
        // Should be unique, otherwise data will be overridden
        String json = sharedPreferences.getString("notes",null);

        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        noteList = gson.fromJson(json,type);

        if (noteList == null){
            return new ArrayList<>();
        } else
            return noteList;
    }














    // ------------------------------------------------------------------------------------------ //
    //                                  FUNCTIONS GRAVEYARD                                       //
    // ------------------------------------------------------------------------------------------ //

    ArrayList<Note> QuickSortModification(ArrayList<Note> list){
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
        temp.addAll(QuickSortModification(low));
        temp.addAll(equal);
        temp.addAll(QuickSortModification(high));

        return temp;
    }

    ArrayList<Note> QuickSortAlpha(ArrayList<Note> list){
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
            switch (StringComparison(x.title,e.title)){
                case  0: equal.add(x)   ; break;
                case -1: low.add(x)     ; break;
                case  1: high.add(x)    ; break;
            }
        }

        ArrayList<Note> temp = new ArrayList<>();
        temp.addAll(QuickSortAlpha(low));
        temp.addAll(equal);
        temp.addAll(QuickSortAlpha(high));

        return temp;
    }

    int StringComparison(String a, String b){
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

    ArrayList<Note> QuickSortCreation(ArrayList<Note> list){
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
        temp.addAll(QuickSortCreation(low));
        temp.addAll(equal);
        temp.addAll(QuickSortCreation(high));

        return temp;
    }


    private ArrayList<Category> useDummyCategoryList(){
        ArrayList<Category> categoryList;

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Default",true));
        categoryList.add(new Category("Studies",true));
        categoryList.add(new Category("Sports",true));
        categoryList.add(new Category("Unity",false));
        categoryList.add(new Category("Habits",false));
        categoryList.add(new Category("MF",false));
        categoryList.add(new Category("Freelance",false));
        categoryList.add(new Category("Android Studio",false));

        return categoryList;
    }

    void useDummyElements(){
        // Testing the RecyclerView
        // Dummy element ahead
//        mList.add(new Note("note 1",R.drawable.icon_big,"one"));
//        mList.add(new Note("note 2",R.drawable.icon_big,"two"));
//        mList.add(new Note("note 3",R.drawable.icon_big,"three"));
//        mList.add(new Note("note 4",R.drawable.icon_big,"four"));
//        mList.add(new Note("note 5",R.drawable.icon_big,"five"));
//        mList.add(new Note("note 6",R.drawable.icon_big,"six"));
//        mList.add(new Note("note 7",R.drawable.icon_big,"seven"));
//        mList.add(new Note("note 8",R.drawable.icon_big,"eight"));
//        mList.add(new Note("note 9",R.drawable.icon_big,"nine"));
//        mList.add(new Note("note 10",R.drawable.icon_big,"ten"));
//        mList.add(new Note("note 11",R.drawable.icon_big,"eleven"));
//        mList.add(new Note("note 12",R.drawable.icon_big,"twelve"));
    }

    void addDummyNote(int position){
//        if (mList.size() != 0) mList.add(position,new Note("new Note ("+ (int) (Math.random()*1000) +")","new_note"));
//        else mList.add(new Note("new Note ("+ (int) (Math.random()*1000) +")","new_note"));
//        Need to be more optimized
//        buildRecyclerView();
//        mAdapter.notifyDataSetChanged();
//        mAdapter.notifyItemInserted(position);
    }
}
