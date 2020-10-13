package com.example.noter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends Activity implements Serializable {

    ArrayList<Note> exampleCategory = new ArrayList<Note>();
    RecyclerView mRecyclerView;
    NoteAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });


        useDummyElements();
        buildRecyclerView();

    }

    void useDummyElements(){
        // Testing the RecyclerView
        // Dummy element ahead
        exampleCategory.add(new Note("note 1",R.drawable.icon_big,"one"));
        exampleCategory.add(new Note("note 2",R.drawable.icon_big,"two"));
        exampleCategory.add(new Note("note 3",R.drawable.icon_big,"three"));
        exampleCategory.add(new Note("note 4",R.drawable.icon_big,"four"));
        exampleCategory.add(new Note("note 5",R.drawable.icon_big,"five"));
        exampleCategory.add(new Note("note 6",R.drawable.icon_big,"six"));
        exampleCategory.add(new Note("note 7",R.drawable.icon_big,"seven"));
        exampleCategory.add(new Note("note 8",R.drawable.icon_big,"eight"));
        exampleCategory.add(new Note("note 9",R.drawable.icon_big,"nine"));
        exampleCategory.add(new Note("note 10",R.drawable.icon_big,"ten"));
        exampleCategory.add(new Note("note 11",R.drawable.icon_big,"eleven"));
        exampleCategory.add(new Note("note 12",R.drawable.icon_big,"twelve"));
    }

    void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.category_RecyclerView);

        // If the list contains an always fixed number of element,
        // set the boolean in the next line of code to TRUE
        // In the case of this note app, the size may vary by deleting/adding new notes
        // should be set to FALSE (if not using dummy data)
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NoteAdapter(exampleCategory);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadNote(position);
            }
        });
    }

    void loadNote(int position){
        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra("note", exampleCategory.get(position));
        startActivity(i);
    }

    void addNote(){
        exampleCategory.add(0,new Note("new Note ("+ (int) (Math.random()*10) +")",R.drawable.icon_big,"new_note"));
        mAdapter.notifyItemInserted(0);
    }
}
