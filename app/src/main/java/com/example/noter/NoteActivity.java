package com.example.noter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText titleText;
    private EditText contentText;
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


        titleText.setText(note.title);
        contentText.setText(note.content);

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
    }

    void saveNoteListToSharedPreferences(ArrayList<Note> noteList){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(noteList);
        editor.putString("task list",json);
        editor.apply();
    }

    void saveNote(int position){
        if (position != -1){
            mList.get(position).title = titleText.getText().toString();
            mList.get(position).content = contentText.getText().toString();
        } else {
            mList.add(0,new Note(titleText.getText().toString(),contentText.getText().toString(),R.drawable.icon_big));
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
}