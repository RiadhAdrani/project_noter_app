package com.example.noter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private String FILE_NAME = "file_name";
    private int noteIndex = -1;
    private ArrayList<Note> mList = new ArrayList<>();

    private ArrayList<Icon> iconList = new ArrayList<>();
    final static int numberOfColumns = 5;

    private EditText titleText;
    private EditText contentText;
    private ImageView noteIcon;
    private Button cancelButton;
    private Button saveButton;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_layout);

        Intent i = getIntent();
        note = (Note) i.getSerializableExtra("note");
        noteIndex = (int) i.getSerializableExtra("note_index");
        mList = (ArrayList<Note>) i.getSerializableExtra("note_list");

        titleText = findViewById(R.id.note_title);
        contentText = findViewById(R.id.note_text);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        noteIcon = findViewById(R.id.note_icon);

        titleText.setText(note.title);
        contentText.setText(note.content);

        if (noteIndex != -1 ) {
            noteIcon.setImageResource(note.getIcon(getApplicationContext()).id);
        }
        else noteIcon.setImageResource(R.drawable.icon_small);

        noteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconList = new MyResources(getApplicationContext()).GET_ICON_LIST();
                // createDummyIconList();
                buildIconDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(noteIndex);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        // Toast.makeText(this,new MyDate().FORMAT_DATE_LONG(note.lastModifiedDate),Toast.LENGTH_SHORT).show();
        Log.d("DEBUG_TIME",""+note.title+ "(Last Modified): " +note.lastModifiedDate.getTime());
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    void buildIconDialog(){
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
        RecyclerView mRecyclerView;
        IconAdapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.icon_list_recyclerView);

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new GridLayoutManager(this,numberOfColumns);
        mAdapter = new IconAdapter(iconList);
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
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(noteList);
        editor.putString("notes",json);
        editor.apply();
    }

    void saveNote(int position){
        if (position != -1){
            mList.get(position).title = titleText.getText().toString();
            mList.get(position).content = contentText.getText().toString();
            mList.get(position).iconUID = note.iconUID;
            mList.get(position).lastModifiedDate = new MyDate().GET_CURRENT_DATE();
        } else {
            Note mNote = new Note(getApplicationContext());
            mNote.title = titleText.getText().toString();
            mNote.content = contentText.getText().toString();
            mNote.iconUID = note.iconUID;
            mList.add(0,mNote);
        }

        saveNoteListToSharedPreferences(mList);
        cancel();
    }

    void cancel(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
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