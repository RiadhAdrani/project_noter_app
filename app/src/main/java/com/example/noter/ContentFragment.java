package com.example.noter;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

public class ContentFragment extends Fragment {

    private static final String CONTENT = "param1";

    public String fContent;

    EditText contentView;

    // Required empty public constructor
    public ContentFragment() {

    }

    // handling parameters
    public static ContentFragment newInstance(String param1) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();

        // get the input parameter
        args.putString(CONTENT, param1);
        fragment.setArguments(args);

        return fragment;
    }

    // processing data
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fContent = getArguments().getString(CONTENT);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentView = getView().findViewById(R.id.f_note_text);
        contentView.setText(fContent);

        // add a scroller for the EditText view
        contentView.setScroller(new Scroller(getContext()));
        contentView.setVerticalScrollBarEnabled(true);
        contentView.setMovementMethod(new ScrollingMovementMethod());

        final TextView contentCounter = getView().findViewById(R.id.f_count_text);
        contentCounter.setText(
                (getResources().getInteger(R.integer.note_content_max_length) - contentView.length()) +
                "/" +
                getResources().getInteger(R.integer.note_content_max_length));

        // watcher that will count the number of remaining character for the
        // current note
        contentView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                contentCounter.setText(
                        (getResources().getInteger(R.integer.note_content_max_length) - s.toString().length()) +
                        "/" +
                        getResources().getInteger(R.integer.note_content_max_length));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_content, container, false);
    }

    public String getContent(){
        return contentView.getText().toString().trim();
    }

}