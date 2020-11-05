package com.example.noter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.myViewHolder> {

    // CheckListAdapter is a custom made adapter to display
    // a list of CheckListItems in a recyclerView
    // check CheckListItem Java Class for more documentation

    // contains the check List
    ArrayList<CheckListItem> cList;

    // public constructor
    public CheckListAdapter(ArrayList<CheckListItem> list){
        this.cList = list;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        // static class defining the holder of the item to be displayed

        // creating variables
        CheckBox statusBox;
        TextView description;
        ImageView deleteItem;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing variables
            statusBox = itemView.findViewById(R.id.CLIC_checkBox);
            description = itemView.findViewById(R.id.CLIC_item_text);
            deleteItem = itemView.findViewById(R.id.CLIC_item_delete);

        }
    }

    @NonNull
    @Override
    public CheckListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflating the view with a custom layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_list_item_card,parent,false);

        return new CheckListAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.myViewHolder holder, int position) {
        // Assign information to the existing View elements
        // to be displayed

        CheckListItem currentItem = cList.get(position);

        // filling the holder with information from the current item

        holder.statusBox.setChecked(currentItem.isDone);

        holder.description.setText(currentItem.text.trim());
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }
}
