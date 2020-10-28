package com.example.noter;

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

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity implements Serializable {

    // Activity Displaying the multiple notes of the selected category.

    MyResources MY_RESOURCES;

    static final int NEW_CATEGORY_POSITION = 2;

    // Raw list of notes
    ArrayList<Note> nList = new ArrayList<>();

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

    // current category filter
    Category currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // assign the layout of the activity
        // very important to assign the correct layout
        // because it will be used to fetch elements
        // could cause crashes if not set correctly
        setContentView(R.layout.main_activity_layout);

        MY_RESOURCES = new MyResources(this);

        // Import the current Category filter from sharedPreferences
        // If the category does not exist (any more, or has been deleted)
        // assign the current category to the default one.
        if (currentCategory == null){
            currentCategory = MyResources.ALL_CATEGORY;
        }

        // Loading Categories from SharePreferences
        cList = MY_RESOURCES.GET_CATEGORY_LIST();

        // Loading notes from SharedPreferences
        nList = loadNoteFromSharedPreferences();

        // Applying existing category filter
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // Setting the toolbar for the current activity
        // Could be customized via R.layout.main_activity_layout
        Toolbar toolbar = findViewById(R.id.category_toolbar);
        setActionBar(toolbar);

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

                // create a simple input dialog
                final SimpleInputDialog inputDialog = new SimpleInputDialog(getApplicationContext(),"Add new category");
                inputDialog.show(getSupportFragmentManager(),"add new category" );

                // overriding the interface methods
                inputDialog.setButtons(new SimpleInputDialog.Buttons() {
                    @Override
                    public void onCancelClickListener() {
                        inputDialog.dismiss();
                    }

                    @Override
                    public void onConfirmClickListener() {

                        // create a temporary category
                        final Category newCategory = new Category(inputDialog.inputField.getText().toString());

                        // if category does not exist
                        if (MY_RESOURCES.GET_CATEGORY_INDEX_BY_NAME(newCategory,cList) == -1){

                            // add new category as it is
                            AddCategory(newCategory,NEW_CATEGORY_POSITION);

                            // cAdapter.notifyItemInserted(NEW_CATEGORY_POSITION);

                            // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                            BuildCategoryRecyclerView();
                            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                        }
                        else {
                            // else
                            // a category with the same name exist
                            // trying to find a solution
                            // for the duplicate

                            // create a confirm dialog
                            final ConfirmDialog dialog = new ConfirmDialog(getApplicationContext(),
                                    "A category named : \""+newCategory.name+"\" already exists, it will be named \""+MY_RESOURCES.GET_SUITABLE_NAME(newCategory.name,cList)+"\". Continue?");
                            dialog.show(getSupportFragmentManager(),"add new category");

                            // overriding interface's methods
                            dialog.setButtons(new ConfirmDialog.Buttons() {
                                @Override
                                public void onCancelClickListener() {

                                    // close dialog and exit
                                    dialog.dismiss();
                                }

                                @Override
                                public void onConfirmClickListener() {

                                    // getting a suitable category name
                                    newCategory.name = MY_RESOURCES.GET_SUITABLE_NAME(newCategory.name,cList);

                                    // adding the suitable category name
                                    AddCategory(newCategory,NEW_CATEGORY_POSITION);

                                    // cAdapter.notifyItemInserted(NEW_CATEGORY_POSITION);

                                    // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                                    BuildCategoryRecyclerView();
                                    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

                                    // close and exit
                                    dialog.dismiss();
                                }
                            });
                        }

                        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                        // BuildCategoryRecyclerView();
                        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

                        // dismissing the simple input dialog
                        // for both cases
                        inputDialog.dismiss();
                    }
                });

            }
        });

        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
        buildRecyclerView();

        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
        BuildCategoryRecyclerView();

        Log.d("DEBUG_NOTE_ACTIVITY","number of notes: "+nList.size());
        Log.d("DEBUG_NOTE_ACTIVITY","number of displayed notes: "+mList.size());

    }

    void AddCategory(Category newCategory, int position){
        // Add newCategory to the list of categories
        // @ index position

        // Add a new category to the list
        cList.add(position,newCategory);

        // saving the category list
        // to the shared preferences
        MY_RESOURCES.SAVE_CATEGORY_LIST(cList);

        // getting back the category list
        // from shared preferences
        cList = MY_RESOURCES.GET_CATEGORY_LIST();
    }

    void DisplayCategoryOptionDialog(Category category, int position){

        // creating a dialog for the category options
        // Check CategoryOptionDialog Java class for more documentation
        final CategoryOptionDialog dialog = new CategoryOptionDialog(category,position);
        dialog.show(getSupportFragmentManager(),"category option dialog");

        // overriding the interface's methods
        dialog.setOnCategoryOptionClickListener(new CategoryOptionDialog.onCategoryOptionClickListener() {
            @Override
            public void onRenameButtonClick(final int position) {

                // if the name exists && the input field is different from the current name
                if (MY_RESOURCES.CHECK_IF_NAME_EXIST(cList,dialog.inputField.getText().toString())
                        && !cList.get(position).name.equals(dialog.inputField.getText().toString()) ){

                    // creating a confirmation dialog
                    // check ConfirmDialog for more documentation
                    final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),
                            "Category exists, do you want it to be renamed "+MY_RESOURCES.GET_SUITABLE_NAME(dialog.inputField.getText().toString(),cList));

                    confirmDialog.show(getSupportFragmentManager(),"confirm rename dialog");

                    // overriding the interface's methods
                    confirmDialog.setButtons(new ConfirmDialog.Buttons() {
                        @Override
                        public void onCancelClickListener() {

                            // close and exit
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void onConfirmClickListener() {

                            // assigning a suitable name for the category
                            cList.get(position).name = MY_RESOURCES.GET_SUITABLE_NAME(dialog.inputField.getText().toString(),cList);

                            // saving the category list to the shared preferences
                            MY_RESOURCES.SAVE_CATEGORY_LIST(cList);

                            // getting the shared preferences
                            cList = MY_RESOURCES.GET_CATEGORY_LIST();

                            // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                            BuildCategoryRecyclerView();

                            // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                            buildRecyclerView();

                            // exit the confirmation dialog
                            confirmDialog.dismiss();

                            // exit the dialog
                            dialog.dismiss();
                        }
                    });
                } else {

                    // if the name does not exists
                    cList.get(position).name = dialog.inputField.getText().toString();

                    // saving category list to shared preferences
                    MY_RESOURCES.SAVE_CATEGORY_LIST(cList);

                    // loading the category list again
                    cList = MY_RESOURCES.GET_CATEGORY_LIST();

                    // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                    BuildCategoryRecyclerView();

                    // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                    buildRecyclerView();

                    // exit the dialog
                    dialog.dismiss();
                }
            }

            @Override
            public void onDeleteButtonClick(final int position) {

                // create a confirmation dialog
                // for the deletion
                // for more documentation about ConfirmDialog,
                // check  ConfirmDialog Java class
                final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),"Are you sure you want to delete "+cList.get(position).name+" ?");
                confirmDialog.show(getSupportFragmentManager(),"confirm delete category");

                // overriding the interface methods
                confirmDialog.setButtons(new ConfirmDialog.Buttons() {
                    @Override
                    public void onCancelClickListener() {

                        // close and exit
                        confirmDialog.dismiss();
                    }

                    @Override
                    public void onConfirmClickListener() {

                        // replacing the deleted category with
                        // the Default category in each note having
                        // that category

                        if (currentCategory == cList.get(position)){
                            currentCategory = MyResources.DEFAULT_CATEGORY;
                        }

                        for (Note note : nList) {
                            if (note.category.equals(cList.get(position).UID))
                                note.category = MyResources.DEFAULT_CATEGORY.UID;
                        }

                        // deleting the category
                        cList.remove(position);

                        // saving the category list to the shared preferences
                        MY_RESOURCES.SAVE_CATEGORY_LIST(cList);

                        // reloadin the category list from shared preferences
                        cList = MY_RESOURCES.GET_CATEGORY_LIST();

                        // saving notes to shared preferences
                        saveNoteToSharedPreferences(nList);

                        // getting notes from the shared preferences
                        nList = loadNoteFromSharedPreferences();

                        // filter category
                        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

                        // rebuilding the notes recycler view
                        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                        buildRecyclerView();

                        // rebuilding the categories recycler view
                        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                        BuildCategoryRecyclerView();

                        // alerting the user that the category is deleted successfully
                        Toast.makeText(getApplicationContext(),"Category deleted, Notes updated",Toast.LENGTH_SHORT).show();

                        // closing the confirmation dialog box
                        confirmDialog.dismiss();

                        // closing the dialog
                        dialog.dismiss();

                    }
                });



            }

            @Override
            public void onCancelButtonClick(int position) {

                // closing the dialog
                dialog.dismiss();

            }
        });
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
                // Filter notes by the selected category

                // filtering the note list by category
                mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,cList.get(position));

                // updating the current category
                currentCategory = cList.get(position);

                // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                buildRecyclerView();

                // Toast.makeText(getApplicationContext(),"Category Clicked !",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickListener(int position) {

                // if the user click and hold on a category
                // apart from the default category and all category
                // the user will get the option dialog for the category
                if (cList.get(position) != MyResources.DEFAULT_CATEGORY && cList.get(position) != MyResources.ALL_CATEGORY )

                    // displaying the category options
                    DisplayCategoryOptionDialog(cList.get(position),position);
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
        nList = loadNoteFromSharedPreferences();
    }

    @Override
    protected void onPause() {
        // Some Automatic saving

        super.onPause();
        saveNoteToSharedPreferences(nList);
    }

    @Override
    protected void onStop() {
        // Some Automatic saving

        super.onStop();
        saveNoteToSharedPreferences(nList);

    }

    @Override
    protected void onRestart() {
        // Some Updates to be called when the activity Starts

        super.onRestart();
        nList = loadNoteFromSharedPreferences();
        buildRecyclerView();
    }

    @Override
    protected void onResume() {
        // Some Updates to be called when the activity Starts

        super.onResume();
        nList = loadNoteFromSharedPreferences();
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

        Collections.sort(nList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.title.compareTo(n2.title);
            }
        });

        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // Update The RecyclerView
        buildRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_title),Toast.LENGTH_SHORT).show();
    }

    void sortByCreation(){
        // Sorts a list of Note by Note.creationDate property
        // Sorts Ascending Only

        Collections.sort(nList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.creationDate.compareTo(n2.creationDate);
            }
        });

        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // Update The RecyclerView
        buildRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_creation),Toast.LENGTH_SHORT).show();
    }

    void sortByModified(){
        // Sorts a list of Note by Note.lastModificationDate property
        // Sorts Ascending Only

        Collections.sort(nList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.lastModifiedDate.compareTo(n2.lastModifiedDate);
            }
        });

        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

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

        // creating a temporary note
        Note tempNote = mList.get(position);

        // adding the note the list
        nList.add(0,tempNote);

        // saving changes to the shared preferences
        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_KEY);

        // loading the updated list
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_KEY);

        // filtering the list to be displayed
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // Need to be more optimized
        buildRecyclerView();

        // mAdapter.notifyDataSetChanged();
        // mAdapter.notifyItemInserted(position);
    }

    void copyNoteContent(int position){
        // Copy the content of the Note to the Clipboard.

        // creating a new clipboard manager
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // store the content of the note in a temporary variable
        String mContent = mList.get(position).content;

        // getting the content
        ClipData clip = ClipData.newPlainText("Note Content",mContent );

        // assigning the data to the clipboard
        clipboard.setPrimaryClip(clip);

        // Alert the user of the success of the operation
        Toast.makeText(getApplicationContext(),getString(R.string.copy_content_toast),Toast.LENGTH_LONG).show();
    }

    void deleteNote(int position){
        // Remove a note by its position

        // delete the note
        nList.remove(mList.get(position));

        // saving the new list
        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_KEY);

        // updating the local list
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_KEY);

        // update
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

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

        int j = MY_RESOURCES.GET_NOTE_INDEX(nList,mList.get(position));

        // -----------------------------------------------------------------------------------------
        // All lines of code are used to debug an issue caused by object.equals
        // it is now obvious that object.equals do not return a good value when used
        // on a custom class object.

        // display notes in nList
        int x = 0;
        for (Note note : nList) {
            Log.d("DEBUG_NOTE_ACTIVITY","Note ["+x+"]: "+note.title);
            x ++;
        }

        // Sperating the two lists
        Log.d("DEBUG_NOTE_ACTIVITY","--------------------------------");

        // display notes in mList
        int y = 0;
        for (Note note : mList) {
            Log.d("DEBUG_NOTE_ACTIVITY","Note ["+y+"]: "+note.title);
            y++;
        }

        Log.d("DEBUG_NOTE_ACTIVITY","Index of selected note: "+j);
        // -----------------------------------------------------------------------------------------


        Intent i = new Intent(this, NoteActivity.class);

        i.putExtra("note",  nList.get(j));
        i.putExtra("note_index", j );
        i.putExtra("note_list",nList);

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
        editor.putString(MyResources.NOTE_KEY,json);

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
        String json = sharedPreferences.getString(MyResources.NOTE_KEY,null);

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

//    ArrayList<Note> QuickSortModification(ArrayList<Note> list){
//        // Function that uses the randomized quick sort algorithm
//        // to sort a list of notes by
//        // the Note.modificationDate property
//        // Uses Recursive call
//
//        if (list.size() <= 1) return list;
//        ArrayList<Note> high = new ArrayList<>();
//        ArrayList<Note> low = new ArrayList<>();
//        ArrayList<Note> equal = new ArrayList<>();
//        Note e = list.get( (int) (Math.random() *list.size()) );
//
//        for (Note x : list) {
//            if (x.creationDate.getTime() < e.creationDate.getTime()) low.add(x);
//            if (x.creationDate.getTime() > e.creationDate.getTime()) high.add(x);
//            if (x.creationDate.getTime() == e.creationDate.getTime()) equal.add(x);
//        }
//
//        ArrayList<Note> temp = new ArrayList<>();
//        temp.addAll(QuickSortModification(low));
//        temp.addAll(equal);
//        temp.addAll(QuickSortModification(high));
//
//        return temp;
//    }
//
//    ArrayList<Note> QuickSortAlpha(ArrayList<Note> list){
//        // Function that uses the randomized quick sort algorithm
//        // to sort a list of notes by
//        // the Note.title property
//        // Uses Recursive call
//
//        if (list.size() <= 1) return list;
//        ArrayList<Note> high = new ArrayList<>();
//        ArrayList<Note> low = new ArrayList<>();
//        ArrayList<Note> equal = new ArrayList<>();
//        Note e = list.get( (int) (Math.random() *list.size()) );
//        for (Note x : list) {
//            switch (StringComparison(x.title,e.title)){
//                case  0: equal.add(x)   ; break;
//                case -1: low.add(x)     ; break;
//                case  1: high.add(x)    ; break;
//            }
//        }
//
//        ArrayList<Note> temp = new ArrayList<>();
//        temp.addAll(QuickSortAlpha(low));
//        temp.addAll(equal);
//        temp.addAll(QuickSortAlpha(high));
//
//        return temp;
//    }
//
//    int StringComparison(String a, String b){
//        // Function used to compare two strings (a) and (b)
//        // Custom made for the Randomized Quick Sorting Function
//        // return -1 if (a) is alphabetically before (b)
//        // return  0 if (a) is alphabetically after  (b)
//        // return  1 if (a) is equal to              (b)
//
//        int size;
//        size = Math.min(a.length(), b.length());
//        for (int i = 0; i < size; i++) {
//            if (a.toLowerCase().charAt(i) < b.toLowerCase().charAt(i)) return -1; // A is before B
//            if (a.toLowerCase().charAt(i) > b.toLowerCase().charAt(i)) return  1; // A is after B
//        }
//        if (a.length() < b.length()) return -1;
//        else if (a.length() > b.length()) return 1;
//
//        return 0;
//    }
//
//    ArrayList<Note> QuickSortCreation(ArrayList<Note> list){
//        // Function that uses the randomized quick sort algorithm
//        // to sort a list of notes by
//        // the Note.creationDate property
//        // Uses Recursive call
//
//        if (list.size() <= 1) return list;
//        ArrayList<Note> high = new ArrayList<>();
//        ArrayList<Note> low = new ArrayList<>();
//        ArrayList<Note> equal = new ArrayList<>();
//        Note e = list.get( (int) (Math.random() *list.size()) );
//
//        for (Note x : list) {
//            if (x.creationDate.getTime() < e.creationDate.getTime()) low.add(x);
//            if (x.creationDate.getTime() > e.creationDate.getTime()) high.add(x);
//            if (x.creationDate.getTime() == e.creationDate.getTime()) equal.add(x);
//        }
//
//        ArrayList<Note> temp = new ArrayList<>();
//        temp.addAll(QuickSortCreation(low));
//        temp.addAll(equal);
//        temp.addAll(QuickSortCreation(high));
//
//        return temp;
//    }
//
//
//    private ArrayList<Category> useDummyCategoryList(){
//        ArrayList<Category> categoryList;
//
//        categoryList = new ArrayList<>();
//        categoryList.add(new Category("Default",true));
//        categoryList.add(new Category("Studies",true));
//        categoryList.add(new Category("Sports",true));
//        categoryList.add(new Category("Unity",false));
//        categoryList.add(new Category("Habits",false));
//        categoryList.add(new Category("MF",false));
//        categoryList.add(new Category("Freelance",false));
//        categoryList.add(new Category("Android Studio",false));
//
//        return categoryList;
//    }
//
//    void useDummyElements(){
//        // Testing the RecyclerView
//        // Dummy element ahead
////        mList.add(new Note("note 1",R.drawable.icon_big,"one"));
////        mList.add(new Note("note 2",R.drawable.icon_big,"two"));
////        mList.add(new Note("note 3",R.drawable.icon_big,"three"));
////        mList.add(new Note("note 4",R.drawable.icon_big,"four"));
////        mList.add(new Note("note 5",R.drawable.icon_big,"five"));
////        mList.add(new Note("note 6",R.drawable.icon_big,"six"));
////        mList.add(new Note("note 7",R.drawable.icon_big,"seven"));
////        mList.add(new Note("note 8",R.drawable.icon_big,"eight"));
////        mList.add(new Note("note 9",R.drawable.icon_big,"nine"));
////        mList.add(new Note("note 10",R.drawable.icon_big,"ten"));
////        mList.add(new Note("note 11",R.drawable.icon_big,"eleven"));
////        mList.add(new Note("note 12",R.drawable.icon_big,"twelve"));
//    }
//
//    void addDummyNote(int position){
////        if (mList.size() != 0) mList.add(position,new Note("new Note ("+ (int) (Math.random()*1000) +")","new_note"));
////        else mList.add(new Note("new Note ("+ (int) (Math.random()*1000) +")","new_note"));
////        Need to be more optimized
////        buildRecyclerView();
////        mAdapter.notifyDataSetChanged();
////        mAdapter.notifyItemInserted(position);
//    }
}
