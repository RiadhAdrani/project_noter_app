package com.example.noter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

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

    ArrayList<Note> mList = new ArrayList<>();
    RecyclerView mRecyclerView;
    NoteAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setActionBar(toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting_option_menu,menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_alpha: sortByAlpha(); return true;
            case R.id.sort_creation : sortByCreation(); return true;
            case R.id.sort_modified : sortByModified(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    void sortByAlpha(){
        Collections.sort(mList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.title.compareTo(n2.title);
            }
        });
        saveNoteToSharedPreferences(mList);
        buildRecyclerView();
        Toast.makeText(this,getString(R.string.sorted_by_title),Toast.LENGTH_SHORT).show();
    }

    void sortByCreation(){
        //saveNoteToSharedPreferences(mList = QuickSortCreation(mList));

        Collections.sort(mList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.creationDate.compareTo(n2.creationDate);
            }
        });

        saveNoteToSharedPreferences(mList);
        buildRecyclerView();
        Toast.makeText(this,getString(R.string.sorted_by_creation),Toast.LENGTH_SHORT).show();
    }

    void sortByModified(){
        Collections.sort(mList, new Comparator<Note>() {
            public int compare(Note n1, Note n2) {
                return n1.lastModifiedDate.compareTo(n2.lastModifiedDate);
            }
        });

        saveNoteToSharedPreferences(mList);
        buildRecyclerView();
        Toast.makeText(this,getString(R.string.sorted_by_modification),Toast.LENGTH_SHORT).show();
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
        i.putExtra("note", new Note(getApplicationContext()));
        i.putExtra("note_index", position);
        i.putExtra("note_list",mList);
        startActivity(i);
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
        editor.putString("notes",json);
        editor.apply();
    }

    ArrayList<Note> loadNoteFromSharedPreferences(){
        ArrayList<Note> noteList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes",null);
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

    ArrayList<Note> QuickSortModification(ArrayList<Note> list){
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
