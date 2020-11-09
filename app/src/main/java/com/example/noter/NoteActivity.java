package com.example.noter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {
    // Activity used to display a Note object

    // Loaded Note
    private Note note;

    // The index of the note in the category
    private int noteIndex = -1;

    // The list of all the notes
    // private ArrayList<Note> mList = new ArrayList<>();

    // The number of icons to be displayed per grid
    // when the user is prompted to choose an icon
    final static int numberOfColumns = 5;

    // View of the editable Note.title
    private EditText titleText;

    // View of the editable Note.iconUID
    private ImageView noteIcon;

    // ArrayList containing Categories
    private ArrayList<Category> categoryList;

    // Adapter for the Category spinner
    private SpinnerAdapter categoryAdapter;

    // Importing my resources
    // For more documentation check
    // MyResources Java class file
    private MyResources MY_RESOURCES;

    // Spinner object containing categories
    private Spinner spinner;

    final static int NEW_POSITION = 2;


    androidx.appcompat.widget.Toolbar toolbar;

    // Tab view
    TabLayout tab;

    // fragments needed
    ContentFragment contentFragment;
    CheckListFragment checkListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // assign the layout of the activity
        // very important to assign the correct layout
        // because it will be used to fetch elements
        // could cause crashes if not set correctly
        setContentView(R.layout.note_activity_layout);

        // initializing the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // initializing an instance of MyResources
        // check MyResources Java Class for more documentation
        MY_RESOURCES = new MyResources(this);

        // creating an intent
        Intent i = getIntent();

        // initializing the note
        note = (int) i.getSerializableExtra(MyResources.LOAD_NOTE_KEY) != -1
                ? MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY).get((int) i.getSerializableExtra(MyResources.LOAD_NOTE_KEY))
                : new Note(this);

        noteIndex = MY_RESOURCES.GET_NOTE_INDEX(MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY),note);

        // Getting the View for the Note.title
        titleText = findViewById(R.id.note_title);

        // ----------------------------------------------------------------------------------------
        //                                  HANDLING TAB LAYOUT
        // ----------------------------------------------------------------------------------------

        // initializing the new tab bar
        tab = new TabLayout(this);
        tab = findViewById(R.id.note_activity_tab);

        // initializing viewPager
        final ViewPager viewPager = findViewById(R.id.note_activity_pager);

        // initializing FragmentAdapter
        final NoteFragmentPager fragmentAdapter = new NoteFragmentPager(getSupportFragmentManager(),2);

        // initializing Fragments
        contentFragment = ContentFragment.newInstance(note.content);

        // CheckListFragment implementation
        checkListFragment = CheckListFragment.newInstance(note.checkList,getApplicationContext());
        checkListFragment.setCheckListFragmentMethods(new CheckListFragment.CheckListFragmentMethods() {
            @Override
            public void onCheckListItemAdded() {
                note.checkList = checkListFragment.getCheckListItems();
            }
        });

        // overriding fragment selection by position/index
        fragmentAdapter.FragmentSelect(new NoteFragmentPager.FragmentSelect() {
            @Override
            public Fragment Selector(int position) {
                switch (position){

                    // TODO: add other fragment Activities
                    case 0: return contentFragment;
                    case 1: return checkListFragment;

                }
                return null;
            }

            @Override
            public CharSequence TitleSetter(int position) {
                switch (position){
                    // TODO: add other tabs title
                    // TODO: could add icons
                    case 0: return getString(R.string.content);
                    case 1: return getString(R.string.check_list);
                }
                return null;
            }
        });

        viewPager.setAdapter(fragmentAdapter);
        tab.setupWithViewPager(viewPager);

        // ----------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------

        // Cancel Button used to discard changes and return to MainActivity
        Button cancelButton = findViewById(R.id.cancel_button);
        
        // Cancel Button used to save changes and return to MainActivity
        Button saveButton = findViewById(R.id.save_button);

        // Getting the View for the Note.iconUID
        noteIcon = findViewById(R.id.note_icon);

        // Displaying Note.title
        titleText.setText(note.title);

        // Displaying Note.iconUID
        noteIcon.setImageResource(note.getIcon(getApplicationContext()).id);

        // overriding onClickListener
        noteIcon.setOnClickListener(new View.OnClickListener() {
            // Setting onClickListener for the icon
            // to display the icon list pick dialog

            @Override
            public void onClick(View view) {
                BuildIconDialog();
            }
        });

        // Setting onClickListener for the save button
        // Deprecated
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SaveNoteExit(noteIndex);
            }
        });

        // Setting onClickListener for the cancel button
        // Deprecated
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CancelExit();
            }
        });

        // useDummyCategoryList();

        categoryList = MY_RESOURCES.LOAD_CATEGORY_LOCALLY();

        // Referencing the spinner
        spinner = findViewById(R.id.note_category_spinner);

        // Filling the category adapter with DATA
        categoryAdapter = new CategorySpinner(this, categoryList);

        // Finishing up the spinner setup
        spinner.setAdapter(categoryAdapter);

        // Setting initial selected item
        spinner.setSelection(MY_RESOURCES.FIND_CATEGORY_BY_UID(categoryList, MY_RESOURCES.GET_CATEGORY(note.category)));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Defining behaviour for selected item
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("DEBUG","" + categoryList.get(i).name + " is Selected");

                if (categoryList.get(i).equals(MyResources.ADD_CATEGORY)){
                    // If Add Category is selected
                    // display a new dialog to add a new category

                    Log.d("DEBUG_NEW_CATEGORY","adding new category");

                    // creating a new Simple Input Dialog (custom class)
                    // customizable hint text
                    // For more documentation about how this works,
                    // check SimpleInputDialog Java Class file
                    final SimpleInputDialog dialog = new SimpleInputDialog(getApplicationContext(),getString(R.string.newCategory));

                    dialog.show(getSupportFragmentManager(),"confirm dialog box");
                    dialog.setButtons(new SimpleInputDialog.Buttons() {
                        @Override
                        public void onCancelClickListener() {
                            // Overriding the action of the cancel button

                            Log.d("DEBUG_NEW_CATEGORY","cancel new category creation");

                            // setting the selected category to the original note category
                            spinner.setSelection(MY_RESOURCES.FIND_CATEGORY_BY_UID(categoryList, MY_RESOURCES.GET_CATEGORY(note.category)));

                            // closing the dialog
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClickListener() {
                            // Overriding the action of the confirm button

                            Log.d("DEBUG_NEW_CATEGORY","creating a new category and saving changes");

                            // creating a temporary category object named by the inputField
                            final Category newCategory = new Category(dialog.inputField.getText().toString().trim());

                            if (MY_RESOURCES.FIND_CATEGORY_BY_NAME(categoryList,newCategory) == -1 && !newCategory.name.trim().toLowerCase().equals("all") ){

                                // if category input name is too short or empty
                                if (newCategory.name.trim().isEmpty() || newCategory.name.trim().length() < MyResources.CATEGORY_MINIMUM_NAME_LENGTH){

                                    // display alert message to the user
                                    Toast.makeText(getApplicationContext(),getString(R.string.category_short_alert),Toast.LENGTH_SHORT).show();

                                    // don't dismiss dialog

                                }

                                // category name is valid
                                else {

                                    // adding the temporary object to the category list
                                    AddCategoryAtPosition(newCategory,NEW_POSITION);

                                    // saving the category
                                    MY_RESOURCES.SAVE_CATEGORIES(categoryList,MyResources.CATEGORY_KEY);

                                    // reloading the category list
                                    categoryList = MY_RESOURCES.LOAD_CATEGORY_LOCALLY();

                                    // updating the spinner and selecting the newly added category
                                    UpdateCategorySpinner(MY_RESOURCES.FIND_CATEGORY_BY_UID(categoryList,newCategory));

                                    // closing the dialog
                                    dialog.dismiss();

                                }

                            } else {
                                // if the category exist,
                                // tell the user that a (copy) will be added
                                // to the category name
                                // or the user can cancel

                                Log.d("DEBUG_NEW_CATEGORY","Duplicate Detected");

                                if (newCategory.name.trim().toLowerCase().equals("all")) newCategory.name += " "+ getString(R.string.copy);

                                final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),
                                        "A category named : \""+newCategory.name+"\" already exists, it will be named \""+MY_RESOURCES.GET_SUITABLE_NAME(newCategory.name,categoryList)+"\". Continue?");
                                confirmDialog.show(getSupportFragmentManager(),"confirm dialog box");
                                confirmDialog.setButtons(new ConfirmDialog.Buttons() {
                                    @Override
                                    public void onCancelClickListener() {
                                        // Cancel the confirmation dialog box
                                        // and return to the creation dialog box

                                        Log.d("DEBUG_NEW_CATEGORY","Duplicate fix declined");

                                        // close the confirm dialog box
                                        confirmDialog.dismiss();
                                    }

                                    @Override
                                    public void onConfirmClickListener() {
                                        // confirm duplicate fix
                                        // and saving changes

                                        // adding "(copy)" to the temporary category name
                                        newCategory.name = MY_RESOURCES.GET_SUITABLE_NAME(newCategory.name.trim(),categoryList).trim();

                                        // adding the temporary object to the category list
                                        AddCategoryAtPosition(newCategory,NEW_POSITION);

                                        // saving the category
                                        MY_RESOURCES.SAVE_CATEGORIES(categoryList,MyResources.CATEGORY_KEY);

                                        // reloading the category list
                                        categoryList = MY_RESOURCES.LOAD_CATEGORY_LOCALLY();

                                        // updating the spinner and selecting the newly added category
                                        UpdateCategorySpinner(MY_RESOURCES.FIND_CATEGORY_BY_UID(categoryList,newCategory));

                                        // close the confirm dialog box
                                        confirmDialog.dismiss();

                                        // closing the dialog
                                        dialog.dismiss();

                                    }
                                });
                                // End of the confirmation dialog box Code
                            }
                        }
                    });
                    // End of the box dialog Code

                }
                else {
                    // Update user category with the newly selected one

                    note.category = categoryList.get(i).UID;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu for sorting purpose
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_note_activity,menu);

        MenuItem saveButton = menu.findItem(R.id.save_note);
        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                SaveNoteExit(noteIndex);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    void UpdateCategorySpinner(int index){
        // refresh the spinner after adding a new category

        // Filling the category adapter with DATA
        categoryAdapter = new CategorySpinner(this, categoryList);

        // Finishing up the spinner setup
        spinner.setAdapter(categoryAdapter);

        // Setting initial selected item
        spinner.setSelection(index);

        Log.d("DEBUG_NEW_CATEGORY",""+index);
    }


    void AddCategoryAtPosition(Category category,int position){
        if (position < 0 || position > categoryList.size() || categoryList.size() < 3){
            categoryList.add(category);
        }
        else {
            categoryList.add(position,category);
        }
    }

    void BuildIconDialog(){
        // display the icon list pick dialog

        final PickIconDialog iconDialog = new PickIconDialog(this);
        iconDialog.show(getSupportFragmentManager(),"icon Dialog");
        iconDialog.setOnCreate(new PickIconDialog.OnCreate() {
            @Override
            public void buildRecyclerView() {
                BuildIconRecyclerView(iconDialog.dialog,iconDialog,new ArrayList<>(MY_RESOURCES.GET_ICON_LIST()));
            }
        });
    }

    void BuildIconRecyclerView(View view, final PickIconDialog dialog, final ArrayList<Icon> iconList){
        // Build The RecyclerView

        RecyclerView mRecyclerView;

        // find the recycler view in the layout of the activity
        mRecyclerView = view.findViewById(R.id.icon_list_recyclerView);

        // Setting the Adapter of the RecyclerView
        IconAdapter mAdapter;

        // Setting the Layout of the RecyclerView
        RecyclerView.LayoutManager mLayoutManager;

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new GridLayoutManager(this,numberOfColumns);
        mAdapter = new IconAdapter(iconList);

        // Setting the functions when the user click on the Icon
        // See IconAdapter Class for more documentation
        mAdapter.setOnItemClickListener(new IconAdapter.OnItemClickListener() {

            @Override
            public void onItemClickListener(int position) {
                note.iconUID = iconList.get(position).uid;
                noteIcon.setImageResource(iconList.get(position).id);
                dialog.dismiss();
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    void CancelExit(){
        // Pass the user to MainActivity

        // Moving the last used category to the top of the list

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    ArrayList<Note> Save(int index){

        ArrayList<Note> mList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY);

        if (index == -1){

            // creating new note
            Note mNote = new Note(getApplicationContext());

            // title
            mNote.title = titleText.getText().toString().trim();

            // text
            mNote.content = contentFragment.getContent();

            // icon
            mNote.iconUID = note.iconUID;

            // category
            mNote.category = note.category;

            // checklist
            mNote.checkList = checkListFragment.getCheckListItems() != null ? checkListFragment.getCheckListItems() : new ArrayList<CheckListItem>();

            // adding the note
            mList.add(0,mNote);
        }
        else {

            // title
            mList.get(index).title = titleText.getText().toString().trim();

            // text
            mList.get(index).content = contentFragment.getContent().trim();

            // icon
            mList.get(index).iconUID = note.iconUID;

            // category
            mList.get(index).category = note.category;

            // checklist
            mList.get(index).checkList = checkListFragment.getCheckListItems() != null ? checkListFragment.getCheckListItems() : new ArrayList<CheckListItem>();
        }

        // Debugging
        Log.d("DEBUG_SAVING","Index of Note :"+noteIndex);
        Log.d("DEBUG_SAVING","Total Number of notes :"+mList.size());

        return mList;

    }

    void SaveNoteExit(final int position){
        // Local note saving

        // if the note name is empty or too short
        if (titleText.getText().toString().trim().isEmpty() || titleText.getText().toString().trim().length() < MyResources.NOTE_MINIMUM_NAME_LENGTH){

            // display alert message to the user
            Toast.makeText(getApplicationContext(),getString(R.string.note_name_short_alert),Toast.LENGTH_SHORT).show();
        }

        // name is valid
        // not too short nor empty
        else {

            ArrayList<Note> tempList = new ArrayList<>(MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_LIST_KEY));

            if (position != -1){
                tempList.remove(position);
            }

            // check if the note name exists already
            if (MY_RESOURCES.CHECK_IF_NOTE_TITLE_EXIST(tempList,titleText.getText().toString().trim())){
                final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),getString(R.string.duplicate_note_title_alert));
                confirmDialog.show(getSupportFragmentManager(),"duplicate note title");
                confirmDialog.setButtons(new ConfirmDialog.Buttons() {
                    @Override
                    public void onCancelClickListener() {
                        confirmDialog.dismiss();
                    }

                    @Override
                    public void onConfirmClickListener() {


                        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(Save(position) ,MyResources.NOTE_LIST_KEY);
                        CancelExit();

                    }
                });
            } else {

                Save(position);

                MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(Save(position),MyResources.NOTE_LIST_KEY);
                CancelExit();

            }

        }

    }

}