package com.example.noter;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
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

    // Sort type
    Boolean isAscending = true;

    // Sort by
    int sortBy = 0;

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

        // Loading Categories from SharePreferences
        cList = MY_RESOURCES.GET_CATEGORY_LIST();

        RefreshCurrentCategory();

        BuildCategoryRecyclerView();

        // Loading notes from SharedPreferences
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);

        // Applying existing category filter
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // Setting the toolbar for the current activity
        // Could be customized via R.layout.main_activity_layout

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);

        // Fetching the Floating Action bar in the layout
        fab = findViewById(R.id.category_fab);

        // Overriding the function of the Floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MY_RESOURCES.LOAD_NOTE_IN_NOTE_ACTIVITY(nList,new Note(getApplicationContext()));
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
                        final Category newCategory = new Category(inputDialog.inputField.getText().toString().trim());

                        // if category does not exist
                        if (MY_RESOURCES.GET_CATEGORY_INDEX_BY_NAME(newCategory,cList) == -1){

                            // if category input name is too short or empty
                            if (newCategory.name.trim().isEmpty() || newCategory.name.trim().length() < MyResources.CATEGORY_MINIMUM_NAME_LENGTH){

                                // display alert message to the user
                                Toast.makeText(getApplicationContext(),getString(R.string.category_short_alert),Toast.LENGTH_SHORT).show();

                                // don't dismiss dialog
                            }

                            // category name is valid
                            else {
                                // add new category as it is
                                AddCategory(newCategory,NEW_CATEGORY_POSITION);

                                // cAdapter.notifyItemInserted(NEW_CATEGORY_POSITION);

                                // cAdapter.notifyItemInserted(NEW_CATEGORY_POSITION);
                                // cAdapter.notifyItemRangeChanged(NEW_CATEGORY_POSITION,cList.size()-1);

                                // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                                BuildCategoryRecyclerView();
                                // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

                                // dismiss dialog
                                inputDialog.dismiss();
                            }

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
                                    newCategory.name = MY_RESOURCES.GET_SUITABLE_NAME(newCategory.name.trim(),cList);

                                    // adding the suitable category name
                                    AddCategory(newCategory,NEW_CATEGORY_POSITION);

                                    // cAdapter.notifyItemInserted(NEW_CATEGORY_POSITION);

                                    // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                                    BuildCategoryRecyclerView();
                                    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

                                    // close and exit
                                    dialog.dismiss();

                                    // close input dialog
                                    inputDialog.dismiss();

                                }
                            });
                        }

                        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                        // BuildCategoryRecyclerView();
                        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                    }
                });

            }
        });

        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
        BuildNoteRecyclerView();

        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
        BuildCategoryRecyclerView();

        Log.d("DEBUG_NOTE_ACTIVITY","number of notes: "+nList.size());
        Log.d("DEBUG_NOTE_ACTIVITY","number of displayed notes: "+mList.size());

    }

    void RefreshCurrentCategory(){
        currentCategory = MY_RESOURCES.LOAD_SINGLE_CATEGORY_FROM_SHARED_PREFERENCES(MyResources.CURRENT_CATEGORY_KEY);

        if (currentCategory.UID.equals(MyResources.ALL_CATEGORY.UID)) currentCategory = MyResources.ALL_CATEGORY;
        else currentCategory = MY_RESOURCES.GET_CATEGORY(currentCategory.UID);
    }

    void RefreshSorting(){
        isAscending = MY_RESOURCES.LOAD_BOOLEAN_FROM_SHARED_PREFERENCES(MyResources.CURRENT_SORTING_TYPE_KEY);
        sortBy = MY_RESOURCES.LOAD_INTEGER_FROM_SHARED_PREFERENCES(MyResources.CURRENT_SORTING_KEY);
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

                // if category input name is too short or empty
                if (dialog.inputField.getText().toString().isEmpty() || dialog.inputField.getText().toString().length() < MyResources.CATEGORY_MINIMUM_NAME_LENGTH){

                    // display alert message to the user
                    Toast.makeText(getApplicationContext(),getString(R.string.category_short_alert),Toast.LENGTH_SHORT).show();

                    // don't dismiss dialog
                }

                // category name is valid
                else {
                        // if the name exists && the input field is different from the current name
                        if (MY_RESOURCES.CHECK_IF_NAME_EXIST(cList,dialog.inputField.getText().toString().trim())
                                && !cList.get(position).name.equals(dialog.inputField.getText().toString().trim()) ){

                            // creating a confirmation dialog
                            // check ConfirmDialog for more documentation
                            final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),
                                    "Category exists, do you want it to be renamed "+MY_RESOURCES.GET_SUITABLE_NAME(dialog.inputField.getText().toString().trim(),cList));

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
                                    cList.get(position).name = MY_RESOURCES.GET_SUITABLE_NAME(dialog.inputField.getText().toString().trim(),cList);

                                    // saving the category list to the shared preferences
                                    MY_RESOURCES.SAVE_CATEGORY_LIST(cList);

                                    // getting the shared preferences
                                    cList = MY_RESOURCES.GET_CATEGORY_LIST();

                                    // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                                    BuildCategoryRecyclerView();

                                    // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                                    BuildNoteRecyclerView();

                                    // exit the confirmation dialog
                                    confirmDialog.dismiss();

                                    // exit the dialog
                                    dialog.dismiss();
                                }
                            });
                        } else {

                            // if the name does not exists
                            cList.get(position).name = dialog.inputField.getText().toString().trim();

                            // saving category list to shared preferences
                            MY_RESOURCES.SAVE_CATEGORY_LIST(cList);

                            // loading the category list again
                            cList = MY_RESOURCES.GET_CATEGORY_LIST();

                            // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                            BuildCategoryRecyclerView();

                            // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                            BuildNoteRecyclerView();

                            // exit the dialog
                            dialog.dismiss();
                        }
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
                        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_LIST_KEY);

                        // getting notes from the shared preferences
                        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);

                        // filter category
                        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

                        // rebuilding the notes recycler view
                        // NOT OPTIMIZED ↓↓↓↓↓↓↓↓↓↓
                        BuildNoteRecyclerView();

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
        cAdapter = new CategoryAdapter(cList,this);

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
                BuildNoteRecyclerView();

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
        inflater.inflate(R.menu.toolbar_option_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onStart() {
        // Some Updates to be called when the activity Starts

        super.onStart();
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);
        RefreshCurrentCategory();
        RefreshSorting();
    }

    @Override
    protected void onPause() {
        // Some Automatic saving

        super.onPause();
        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_LIST_KEY);
        MY_RESOURCES.SAVE_SINGLE_CATEGORY_TO_SHARED_PREFERENCES(currentCategory,MyResources.CURRENT_CATEGORY_KEY);

        MY_RESOURCES.SAVE_BOOLEAN_TO_SHARED_PREFERENCES(isAscending,MyResources.CURRENT_SORTING_TYPE_KEY);
        MY_RESOURCES.SAVE_INTEGER_TO_SHARED_PREFERENCES(sortBy, MyResources.CURRENT_SORTING_KEY);
    }

    @Override
    protected void onStop() {
        // Some Automatic saving

        super.onStop();
        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_LIST_KEY);
        MY_RESOURCES.SAVE_SINGLE_CATEGORY_TO_SHARED_PREFERENCES(currentCategory,MyResources.CURRENT_CATEGORY_KEY);

        MY_RESOURCES.SAVE_BOOLEAN_TO_SHARED_PREFERENCES(isAscending,MyResources.CURRENT_SORTING_TYPE_KEY);
        MY_RESOURCES.SAVE_INTEGER_TO_SHARED_PREFERENCES(sortBy, MyResources.CURRENT_SORTING_KEY);
    }

    @Override
    protected void onRestart() {
        // Some Updates to be called when the activity Starts

        super.onRestart();
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);
        RefreshCurrentCategory();
        RefreshSorting();
        BuildNoteRecyclerView();
    }

    @Override
    protected void onResume() {
        // Some Updates to be called when the activity Starts

        super.onResume();
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);
        RefreshCurrentCategory();
        RefreshSorting();
        BuildNoteRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Overriding the functionality of each option
        // in the Option Menu from The Toolbar

        switch (item.getItemId()){
            case R.id.sort_type: isAscending = !isAscending;
                                 if (isAscending) item.setTitle(R.string.sort_ascending);
                                 else item.setTitle(R.string.sort_descending);
                                 SortBy(sortBy);
                                 return true;
            case R.id.sort_alpha: SortByAlpha(isAscending); return true;
            case R.id.sort_creation : SortByCreation(isAscending); return true;
            case R.id.sort_modified : SortByModified(isAscending); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    void SortBy(int sort){
        switch (sort){
            default:
            case 0: SortByAlpha(isAscending);break;
            case 1: SortByCreation(isAscending);break;
            case 2: SortByModified(isAscending);break;
        }
    }

    void SortByAlpha(boolean sortAscending){
        // Sorts a list of Note by Note.title property
        // Sorts Ascending

        Collections.sort(nList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.title.compareTo(n2.title);
            }
        });

        // if isAscending is false
        // reverse the list
        // for the descending order
        if (!isAscending) Collections.reverse(nList);

        // filter by the current filter
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // set sort by to sort by alpha
        sortBy = MyResources.SORT_ALPHA;

        // Update The RecyclerView
        BuildNoteRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_title),Toast.LENGTH_SHORT).show();
    }

    void SortByCreation(boolean sortAscending){
        // Sorts a list of Note by Note.creationDate property
        // Sorts Ascending Only

        Collections.sort(nList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.creationDate.compareTo(n2.creationDate);
            }
        });

        // if isAscending is false
        // reverse the list
        // for the descending order
        if (!isAscending) Collections.reverse(nList);

        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // set sort by to sort by creation date
        sortBy = MyResources.SORT_CREATION_DATE;

        // Update The RecyclerView
        BuildNoteRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_creation),Toast.LENGTH_SHORT).show();
    }

    void SortByModified(boolean sortAscending){
        // Sorts a list of Note by Note.lastModificationDate property
        // Sorts Ascending Only

        Collections.sort(nList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.lastModifiedDate.compareTo(n2.lastModifiedDate);
            }
        });

        // if isAscending is false
        // reverse the list
        // for the descending order
        if (!isAscending) Collections.reverse(nList);

        // filter by the current filter
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // set sort by to sort by modification date
        sortBy = MyResources.SORT_MODIFICATION_DATE;

        // Update The RecyclerView
        BuildNoteRecyclerView();

        // Alerting the user of the changes made
        Toast.makeText(this,getString(R.string.sorted_by_modification),Toast.LENGTH_SHORT).show();
    }

    void BuildNoteRecyclerView(){
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

                MY_RESOURCES.LOAD_NOTE_IN_NOTE_ACTIVITY(nList,mList.get(position));
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

                // Check my resource for more documentation
                DuplicateNote(position);

            }

            @Override
            public void onCopyContentClick(int position) {
                // Copy the content of the note

                MY_RESOURCES.COPY_NOTE_CONTENT(position,mList);
            }

            @Override
            public void onDeleteClick(int position) {
                // Delete the note

                DeleteNote(position);

                // Need to be more optimized
                BuildNoteRecyclerView();

                // mAdapter.notifyDataSetChanged();
                // mAdapter.notifyItemInserted(position);
            }

            @Override
            public void onLongClickListener(int position) {

            }
        });

        // Setting the Layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Setting the Adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    void DeleteNote(final int position){
        // Remove a note by its position

        // create a confirmation dialog
        final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),getString(R.string.delete_note_alert)
                + " \"" + mList.get(position).title + "\"");

        // display the confirmDialog
        confirmDialog.show(getSupportFragmentManager(),"confirm delete note");

        // overriding the button actions
        confirmDialog.setButtons(new ConfirmDialog.Buttons() {
            @Override
            public void onCancelClickListener() {
                confirmDialog.dismiss();
            }

            @Override
            public void onConfirmClickListener() {

                Log.d("DEBUG_DELETE",""+position);

                // delete the note
                nList.remove(MY_RESOURCES.GET_NOTE_INDEX(mList,mList.get(position)));

                // saving the new list
                MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_LIST_KEY);

                // updating the local list
                nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);

                // update
                mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

                BuildNoteRecyclerView();

                // dismiss
                confirmDialog.dismiss();
            }
        });

    }

    void DuplicateNote(int position){
        // Duplicate a note by inserting the same note
        // in the same position

        // creating the duplicate note
        Note tempNote = new Note(getApplicationContext());

        // overriding data
        tempNote.title = MY_RESOURCES.GET_SUITABLE_NOTE_NAME(mList.get(position).title,nList);
        tempNote.iconUID = mList.get(position).iconUID;
        tempNote.content = mList.get(position).content;
        tempNote.category = mList.get(position).category;

        // adding the note the list
        nList.add(0,tempNote);

        // saving changes to the shared preferences
        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(nList,MyResources.NOTE_LIST_KEY);

        // loading the updated list
        nList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);

        // filtering the list to be displayed
        mList = MY_RESOURCES.FILTER_NOTES_BY_CATEGORY(nList,currentCategory);

        // Need to be more optimized
        BuildNoteRecyclerView();

    }

}