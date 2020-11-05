package com.example.noter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class CheckListFragment extends Fragment{

    // TODO: change and add parameters
    private static final String CONTENT = "param1";

    private Context context;

    // Required empty public constructor
    public CheckListFragment(Context context){
        this.context = context;
    }


    public static CheckListFragment newInstance(String param1, Context context) {

        CheckListFragment fragment = new CheckListFragment(context);
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

        MyResources myResources = new MyResources(context);

        RecyclerView rv = getView().findViewById(R.id.checklist_recycler);

        CheckListAdapter checkListAdapter = new CheckListAdapter(myResources.GENERATE_DUMMY_CHECK_LIST_ITEMS(5));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(checkListAdapter);

        // TODO: display, add, remove and edit checkList
    }

}
