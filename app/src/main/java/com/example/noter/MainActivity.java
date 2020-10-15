package com.example.noter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity implements Serializable {

    ArrayList<Note> mList = new ArrayList<Note>();
    RecyclerView mRecyclerView;
    NoteAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        mList = loadNoteFromSharedPreferences();
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoteToSharedPreferences(mList);
                addNote();
            }
        });


        // useDummyElements();
        buildRecyclerView();

    }

    void useDummyElements(){
        // Testing the RecyclerView
        // Dummy element ahead
        mList.add(new Note("note 1",R.drawable.icon_big,"one"));
        mList.add(new Note("note 2",R.drawable.icon_big,"two"));
        mList.add(new Note("note 3",R.drawable.icon_big,"three"));
        mList.add(new Note("note 4",R.drawable.icon_big,"four"));
        mList.add(new Note("note 5",R.drawable.icon_big,"five"));
        mList.add(new Note("note 6",R.drawable.icon_big,"six"));
        mList.add(new Note("note 7",R.drawable.icon_big,"seven"));
        mList.add(new Note("note 8",R.drawable.icon_big,"eight"));
        mList.add(new Note("note 9",R.drawable.icon_big,"nine"));
        mList.add(new Note("note 10",R.drawable.icon_big,"ten"));
        mList.add(new Note("note 11",R.drawable.icon_big,"eleven"));
        mList.add(new Note("note 12",R.drawable.icon_big,"twelve"));
    }

    void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.category_RecyclerView);

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NoteAdapter(mList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadNote(position);
            }

            @Override
            public void onOptionClick(int position) {
                deleteNote(position);
                saveNoteToSharedPreferences(mList);
            }
        });
    }

    void loadNote(int position){
        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra("note", mList.get(position));
        startActivity(i);
    }

    void saveNoteToSharedPreferences(ArrayList<Note> noteList){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(noteList);
        editor.putString("task list",json);
        editor.apply();
    }

    ArrayList<Note> loadNoteFromSharedPreferences(){
        ArrayList<Note> noteList = new ArrayList<Note>();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list",null);
        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        noteList = gson.fromJson(json,type);

        if (noteList == null){
            return new ArrayList<Note>();
        } else
            return noteList;
    }

    void addNote(){
        mList.add(0,new Note("new Note ("+ (int) (Math.random()*10) +")",R.drawable.icon_big,"new_note"));
        mAdapter.notifyItemInserted(0);
    }

    void deleteNote(int position){
        mList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}
