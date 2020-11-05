package com.example.noter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CheckListFragment extends Fragment {

    // TODO: change and add parameters
    private static final String CONTENT = "param1";

    // Required empty public constructor
    public CheckListFragment(){

    }

    public static CheckListFragment newInstance(String param1) {

        CheckListFragment fragment = new CheckListFragment();
        Bundle args = new Bundle();

        // get the input parameter
        args.putString(CONTENT, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: display, add, remove and edit checkList
    }
}
