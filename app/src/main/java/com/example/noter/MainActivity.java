package com.example.noter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
                createNewNote(-1);
            }
        });

        // useDummyElements();
        buildRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mList = loadNoteFromSharedPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNoteToSharedPreferences(mList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveNoteToSharedPreferences(mList);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mList = loadNoteFromSharedPreferences();
        buildRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList = loadNoteFromSharedPreferences();
        buildRecyclerView();
    }

    void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.category_RecyclerView);

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NoteAdapter(mList,this);

        mAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadNote(position);
            }

            @Override
            public void onOptionClick(int position) {
//                deleteNote(position);
//                saveNoteToSharedPreferences(mList);
            }

            @Override
            public void onDuplicateClick(int position) {
                duplicateNote(position);
            }

            @Override
            public void onCopyContentClick(int position) {
                copyNoteContent(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteNote(position);
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    void duplicateNote(int position){
        mList.add(position,mList.get(position));

        // Need to be more optimized
        buildRecyclerView();

        // mAdapter.notifyDataSetChanged();
        // mAdapter.notifyItemInserted(position);
    }

    void copyNoteContent(int position){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Note Content", mList.get(position).content);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(),getString(R.string.copy_content_toast),Toast.LENGTH_LONG).show();
    }

    void deleteNote(int position){
        mList.remove(position);

        // Need to be more optimized
        buildRecyclerView();

        //mAdapter.notifyDataSetChanged();
        //mAdapter.notifyItemRemoved(position);
    }

    void createNewNote(int position){
        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra("note", new Note(getString(R.string.new_note),""));
        i.putExtra("note_index", position);
        i.putExtra("note_list",mList);
        startActivity(i);
    }

    void addDummyNote(int position){
        if (mList.size() != 0) mList.add(position,new Note("new Note ("+ (int) (Math.random()*1000) +")",R.drawable.icon_big,"new_note"));
        else mList.add(new Note("new Note ("+ (int) (Math.random()*1000) +")",R.drawable.icon_big,"new_note"));

        // Need to be more optimized
        buildRecyclerView();
//        mAdapter.notifyDataSetChanged();
//        mAdapter.notifyItemInserted(position);
    }

    void loadNote(int position){
        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra("note", mList.get(position));
        i.putExtra("note_index", position);
        i.putExtra("note_list",mList);
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


    // --------------------------------------------------------------------------------------------------------------------//
    // -------------------------------------------- FUNCTIONS GRAVEYARD ---------------------------------------------------//
    // --------------------------------------------------------------------------------------------------------------------//

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

}
