package com.example.noter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    // listener for the item clicks
    OnItemClick listener;

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
    public void onBindViewHolder(@NonNull final CheckListAdapter.myViewHolder holder, final int position) {
        // Assign information to the existing View elements
        // to be displayed

        final CheckListItem currentItem = cList.get(position);

        // filling the holder with information from the current item

        // setting the status of the checkBox
        holder.statusBox.setChecked(currentItem.isDone);

        holder.statusBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // if (listener != null) listener.onItemChecked(position);

                currentItem.isDone = holder.statusBox.isChecked();

                if (currentItem.isDone) {
                    holder.description.setPaintFlags(holder.description.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    holder.description.setPaintFlags( holder.description.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }

            }
        });

        // setting the text of the current check list item
        holder.description.setText(currentItem.text.trim());

        // if item is checked, paint a strike through line, else clear paint style
        if (currentItem.isDone) holder.description.setPaintFlags(holder.description.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.description.setPaintFlags( holder.description.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        // overriding the action of the delete button
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // checking if the listener is not null/empty
                // and executing the overridden method
                if (listener != null) listener.onDeleteClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public interface OnItemClick{
        void onDeleteClick(int position);
        void onItemChecked(int position);
    }

    public void setOnItemClick(OnItemClick listener){
        this.listener = listener;
    }
}
