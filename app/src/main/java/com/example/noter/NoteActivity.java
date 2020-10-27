package com.example.noter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    // Activity used to display a Note object

    // [UNUSED]
    // The name of the .txt file
    private String FILE_NAME = "file_name";

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

    // View of the editable Note.content
    private EditText contentText;

    // View of the editable Note.iconUID
    private ImageView noteIcon;

    // Cancel Button used to discard changes and return to MainActivity
    private Button cancelButton;

    // Cancel Button used to save changes and return to MainActivity
    private Button saveButton;

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

        // Getting the necessary data from MainActivity
        // and saving them as local (Class) variable
        note = (Note) i.getSerializableExtra("note");

        mList = MY_RESOURCES.LOAD_NOTES_FROM_SHARED_PREFERENCES(MyResources.NOTE_KEY);

        // noteIndex = (int) i.getSerializableExtra("note_index");
        noteIndex = mList.indexOf(note);

        // Getting the View for the Note.title
        titleText = findViewById(R.id.note_title);

        // Getting the View for the Note.content
        contentText = findViewById(R.id.note_text);

        // Getting the Button for cancelButton
        cancelButton = findViewById(R.id.cancel_button);

        // Getting the Button for saveButton
        saveButton = findViewById(R.id.save_button);

        // Getting the View for the Note.iconUID
        noteIcon = findViewById(R.id.note_icon);

        // Displaying Note.title
        titleText.setText(note.title);

        // Displaying Note.content
        contentText.setText(note.content);

        // [USELESS]
        // if the note is newly made,
        // assign the default icon to the note
        // if (noteIndex == -1) noteIcon.setImageResource(R.drawable.icon_small);

        // Displaying Note.iconUID
        noteIcon.setImageResource(note.getIcon(getApplicationContext()).id);

        noteIcon.setOnClickListener(new View.OnClickListener() {
            // Setting onClickListener for the icon
            // to display the icon list pick dialog

            @Override
            public void onClick(View view) {
                iconList = new MyResources(getApplicationContext()).GET_ICON_LIST();
                buildIconDialog();
            }
        });

        // Setting onClickListener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(noteIndex);
            }
        });

        // Setting onClickListener for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        // useDummyCategoryList();

        loadCategoryLocally();

        // Referencing the spinner
        spinner = findViewById(R.id.note_category_spinner);

        // Filling the category adapter with DATA
        categoryAdapter = new CategorySpinner(this, categoryList);

        // Finishing up the spinner setup
        spinner.setAdapter(categoryAdapter);

        // Setting initial selected item
        spinner.setSelection(findCategoryByUID( MY_RESOURCES.GET_CATEGORY(note.category)));


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
                            spinner.setSelection(findCategoryByUID( MY_RESOURCES.GET_CATEGORY(note.category)));

                            // closing the dialog
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirmClickListener() {
                            // Overriding the action of the confirm button

                            Log.d("DEBUG_NEW_CATEGORY","creating a new category and saving changes");

                            // creating a temporary category object named by the inputField
                            final Category newCategory = new Category(dialog.inputField.getText().toString());

                            if (findCategoryByName(newCategory) == -1){

                                // adding the temporary object to the category list
                                addCategoryAtPosition(newCategory,NEW_POSITION);

                                // saving the category
                                saveCategory();

                                // reloading the category list
                                loadCategoryLocally();

                                // updating the spinner and selecting the newly added category
                                updateCategorySpinner(findCategoryByUID(newCategory));

                                // closing the dialog
                                dialog.dismiss();

                            } else {
                                // if the category exist,
                                // tell the user that a (copy) will be added
                                // to the category name
                                // or the user can cancel

                                Log.d("DEBUG_NEW_CATEGORY","Duplicate Detected");

                                final ConfirmDialog confirmDialog = new ConfirmDialog(getApplicationContext(),"Category exist, \"(copy)\" will added at the end.");
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
                                        newCategory.name += " (copy)";

                                        // adding the temporary object to the category list
                                        addCategoryAtPosition(newCategory,NEW_POSITION);

                                        // saving the category
                                        saveCategory();

                                        // reloading the category list
                                        loadCategoryLocally();

                                        // updating the spinner and selecting the newly added category
                                        updateCategorySpinner(findCategoryByUID(newCategory));

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


    void updateCategorySpinner(int index){
        // refresh the spinner after adding a new category

        // Filling the category adapter with DATA
        categoryAdapter = new CategorySpinner(this, categoryList);

        // Finishing up the spinner setup
        spinner.setAdapter(categoryAdapter);

        // Setting initial selected item
        spinner.setSelection(index);

        Log.d("DEBUG_NEW_CATEGORY",""+index);
    }

    void loadCategoryLocally(){
        // Prepare the category list to be used in the activity

        // Load category list from SharedPreferences
        categoryList = MY_RESOURCES.LOAD_CATEGORIES_FROM_SHARED_PREFERENCES(MyResources.CATEGORY_KEY);

        // Add the Default Category
        categoryList.add(0,MyResources.DEFAULT_CATEGORY);

        // Add the "ADD CATEGORY"
        categoryList.add(0,MyResources.ADD_CATEGORY);
    }

    void saveCategory(){
        // Prepare the category list to be saved
        // Saving only the custom made categories

        // Removing the Default category
        categoryList.remove(findCategoryByUID(MyResources.DEFAULT_CATEGORY));

        // Removing the "Add category" category
        categoryList.remove(findCategoryByUID(MyResources.ADD_CATEGORY));

        // Save category list from SharedPreferences
        MY_RESOURCES.SAVE_CATEGORIES_TO_SHARED_PREFERENCES(categoryList,MyResources.CATEGORY_KEY);
    }

    void addCategoryAtPosition(Category category,int position){
        if (position < 0 || position > categoryList.size() || categoryList.size() < 3){
            categoryList.add(category);
        }
        else {
            categoryList.add(position,category);
        }
    }

    private int findCategoryByUID(Category mCategory){
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

    private int findCategoryByName(Category mCategory){
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

    void buildIconDialog(){
        // display the icon list pick dialog

        final PickIconDialog iconDialog = new PickIconDialog(this);
        iconDialog.show(getSupportFragmentManager(),"icon Dialog");
        iconDialog.setOnCreate(new PickIconDialog.OnCreate() {
            @Override
            public void buildRecyclerView() {
                buildIconRecyclerView(iconDialog.dialog,iconDialog);
            }
        });
    }

    void buildIconRecyclerView(View view, final PickIconDialog dialog){
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

    void saveNoteListToSharedPreferences(ArrayList<Note> noteList){
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

    void saveNote(int position){
        // Local note saving

        if (position != -1){
            // if it is an old note

            mList.get(position).title = titleText.getText().toString();
            mList.get(position).content = contentText.getText().toString();
            mList.get(position).iconUID = note.iconUID;
            mList.get(position).category = note.category;
            mList.get(position).lastModifiedDate = new MyDate().GET_CURRENT_DATE();

        }

        else {
            Note mNote = new Note(getApplicationContext());
            mNote.title = titleText.getText().toString();
            mNote.content = contentText.getText().toString();
            mNote.iconUID = note.iconUID;
            mNote.category = note.category;
            mList.add(0,mNote);
        }

        MY_RESOURCES.SAVE_NOTES_TO_SHARED_PREFERENCES(mList,MyResources.NOTE_KEY);
        cancel();
    }

    void cancel(){
        // Pass the user to MainActivity

        // Moving the last used category to the top of the list

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }










    // ------------------------------------------------------------------------------------------ //
    //                                  FUNCTIONS GRAVEYARD                                       //
    // ------------------------------------------------------------------------------------------ //

    private void useDummyCategoryList(){
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Default",true));
        categoryList.add(new Category("Studies",true));
        categoryList.add(new Category("Sports",true));
        categoryList.add(new Category("Habits",false));
        categoryList.add(new Category("MF",false));
    }

    public void saveNoteAsTXT (){
        String text = contentText.getText().toString();
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this,"Saved to "+getFilesDir()+"/"+FILE_NAME,Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String retrieveTextFromTXT(String fileName){
        String mText = "";

        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }

            contentText.setText(sb.toString());
            mText = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mText;
    }

    public void createDummyIconList(int n){
        MyResources resources = new MyResources(getApplicationContext());
        for (int i = 0 ; i < n ; i++){
            iconList.add(resources.GET_ICON_LIST().get( (int) (Math.random()*resources.GET_ICON_LIST().size()) ));
        }

    }
}