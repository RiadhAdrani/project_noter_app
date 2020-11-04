package com.example.noter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    // Activity used to display a Note object

    // Loaded Note
    private Note note;

    // The index of the note in the category
    private int noteIndex = -1;

    // The list of note in the category
    private ArrayList<Note> mList = new ArrayList<>();

    // The list of icons
    private ArrayList<Icon> iconList = new ArrayList<>();

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

    // Tab view
    TabLayout tab;

    // create a new fragment for the content
    ContentFragment contentFragment;
    ContentFragment dummyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // assign the layout of the activity
        // very important to assign the correct layout
        // because it will be used to fetch elements
        // could cause crashes if not set correctly
        setContentView(R.layout.note_activity_layout);

        Intent i = getIntent();

        MY_RESOURCES = new MyResources(this);

        mList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_KEY);

        // Getting the necessary data from MainActivity
        // and saving them as local (Class) variable
        if ((int) i.getSerializableExtra(MyResources.LOAD_NOTE_KEY) != -1)
            note = mList.get((int) i.getSerializableExtra(MyResources.LOAD_NOTE_KEY));
        else
            note = new Note(this);

        // noteIndex = (int) i.getSerializableExtra("note_index");
        noteIndex = MY_RESOURCES.GET_NOTE_INDEX(mList,note);

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
        dummyFragment = ContentFragment.newInstance(note.title);

        // overriding fragment selection by position/index
        fragmentAdapter.FragmentSelect(new NoteFragmentPager.FragmentSelect() {
            @Override
            public Fragment Selector(int position) {
                switch (position){

                    // TODO: add other fragment Activities
                    case 0: return contentFragment;
                    case 1: return dummyFragment;

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

        noteIcon.setOnClickListener(new View.OnClickListener() {
            // Setting onClickListener for the icon
            // to display the icon list pick dialog

            @Override
            public void onClick(View view) {
                iconList = new MyResources(getApplicationContext()).GET_ICON_LIST();
                BuildIconDialog();
            }
        });

        // Setting onClickListener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNoteExit(noteIndex);
            }
        });

        // Setting onClickListener for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelExit();
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
    protected void onPause() {
        // Actions to be made when the activity is on pause

        super.onPause();

        // Liberate resources
        // finish();
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
                BuildIconRecyclerView(iconDialog.dialog,iconDialog);
            }
        });
    }

    void BuildIconRecyclerView(View view, final PickIconDialog dialog){
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



    void SaveNoteExit(final int position){
        // Local note saving

        // if the note name is empty
        if (titleText.getText().toString().trim().isEmpty() || titleText.getText().toString().trim().length() < MyResources.NOTE_MINIMUM_NAME_LENGTH){

            // display alert message to the user
            Toast.makeText(getApplicationContext(),getString(R.string.note_name_short_alert),Toast.LENGTH_SHORT).show();
        }

        // name is valid
        // not too short nor empty
        else {

            ArrayList<Note> tempList = new ArrayList<>(mList);

            if (position != -1){
                tempList.remove(position);
            }

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

                        if (position != -1){
                            // if it is an old note

                            mList.get(position).title = titleText.getText().toString().trim();

                            // TODO: change to fragment
                            //mList.get(position).content = contentText.getText().toString().trim();

                            // ???
                            mList.get(position).content = contentFragment.getContent();

                            mList.get(position).iconUID = note.iconUID;
                            mList.get(position).category = note.category;
                            mList.get(position).lastModifiedDate = new MyDate().GET_CURRENT_DATE();

                        }

                        else {
                            Note mNote = new Note(getApplicationContext());
                            mNote.title = titleText.getText().toString().trim();

                            // TODO: change to fragment
                            // mNote.content = contentText.getText().toString().trim();

                            // ???
                            mList.get(position).content = contentFragment.getContent();

                            mNote.iconUID = note.iconUID;
                            mNote.category = note.category;
                            mList.add(0,mNote);
                        }

                        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(mList,MyResources.NOTE_KEY);
                        CancelExit();

                    }
                });
            } else {

                if (position != -1){
                    // if it is an old note

                    mList.get(position).title = titleText.getText().toString().trim();

                    // TODO: change to fragment
                    // mList.get(position).content = contentText.getText().toString().trim();

                    // ???
                    mList.get(position).content = contentFragment.getContent();

                    mList.get(position).iconUID = note.iconUID;
                    mList.get(position).category = note.category;
                    mList.get(position).lastModifiedDate = new MyDate().GET_CURRENT_DATE();

                }

                else {
                    Note mNote = new Note(getApplicationContext());
                    mNote.title = titleText.getText().toString().trim();

                    // TODO: change to fragment
                    // mNote.content = contentText.getText().toString().trim();

                    // ???
                    mList.get(position).content = contentFragment.getContent();

                    mNote.iconUID = note.iconUID;
                    mNote.category = note.category;
                    mList.add(0,mNote);
                }

                MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(mList,MyResources.NOTE_KEY);
                CancelExit();

            }


        }

    }

    void CancelExit(){
        // Pass the user to MainActivity

        // Moving the last used category to the top of the list

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}