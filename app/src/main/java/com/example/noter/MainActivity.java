package com.example.noter;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<Note> exampleCategory = new ArrayList<Note>();
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        exampleCategory.add(new Note("note one",R.drawable.icon_big,"one"));
        exampleCategory.add(new Note("note two",R.drawable.icon_big,"two"));
        exampleCategory.add(new Note("note three",R.drawable.icon_big,"three"));

        mRecyclerView = findViewById(R.id.category_RecyclerView);
        // mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NoteAdapter(exampleCategory);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
