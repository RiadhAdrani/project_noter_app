package com.example.noter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CheckListFragment extends Fragment{

    // TODO: change and add parameters
    private static final String PARAM1 = "param1";

    private final Context context;

    private ArrayList<CheckListItem> list;

    private CheckListFragmentMethods methods;

    // Required empty public constructor
    public CheckListFragment(Context context){
        this.context = context;
    }


    public static CheckListFragment newInstance(ArrayList<CheckListItem> param1, Context context) {

        CheckListFragment fragment = new CheckListFragment(context);
        Bundle args = new Bundle();

        // get the input parameter
        args.putParcelableArrayList(PARAM1, param1);
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

        // check if there is no arguments
        if (getArguments() != null) {

            // get the argument
            list = getArguments().getParcelableArrayList(PARAM1);

            // check if the passed parameter is null
            if (list == null) {
                list = new ArrayList<>();
                Log.d("DEBUG_CL_FRAGMENT","Passed list is null !!!");}
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // MyResources myResources = new MyResources(context);

        RecyclerView rv = getView().findViewById(R.id.fragment_checklist_recycler);

        final CheckListAdapter checkListAdapter = new CheckListAdapter(list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(checkListAdapter);

        // TODO: display, add, remove and edit checkList

        // initializing views
        final EditText addText = getView().findViewById(R.id.fragment_checklist_add_text);
        ImageButton addButton = getView().findViewById(R.id.fragment_checklist_add_button);
        ImageButton sortTab = getView().findViewById(R.id.fragment_checklist_sort);

        // overriding with specific action

        // add a new checkListItem to the list
        // and refresh the recyclerView
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create a new CheckListItem fom the input text
                CheckListItem newItem = new CheckListItem(addText.getText().toString().trim());

                // insert the new item into the list
                list.add(0,newItem);

                if (methods != null) methods.onCheckListItemAdded();

                // refresh the recyclerView
                checkListAdapter.notifyItemInserted(0);
            }
        });

    }

    // public function accessed from outside the class
    // from noteActivity
    public ArrayList<CheckListItem> getCheckListItems(){
        return list;
    }

    // sort list & refresh the recyclerView
    private void RefreshRecyclerView(){

    }

    interface CheckListFragmentMethods{
        void onCheckListItemAdded();
    }

    public void setCheckListFragmentMethods(CheckListFragmentMethods methods){
        this.methods = methods;
    }

}
