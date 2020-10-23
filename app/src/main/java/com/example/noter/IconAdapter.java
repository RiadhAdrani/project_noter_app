package com.example.noter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.myViewHolder> {

    // Icon adapter is a custom created Adapter RecyclerView
    // Used to load icons dynamically

    // Reference to the list that will be fed to the adapter
    // to be displayed
    ArrayList<Icon> mList;

    // Contains overridable functions
    // mainly onClickListeners
    // could be implemented in an Activity
    OnItemClickListener listener;

    public IconAdapter(ArrayList<Icon> mList) {
        // Constructor

        this.mList = mList;
    }

    public interface OnItemClickListener{
        // Interface containing (Hollow) overridable functions

        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        // Function that allow the use of the interface

        this.listener = listener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        // ViewHolder of every element
        // Actually prepare the template of the element

        public ImageView mIcon;
        // public TextView mName;

        public myViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.icon_frame);
            // mName = itemView.findViewById(R.id.icon_name);

            // setting an onClickListener for the icon
            // using the interface method
            // which will be overridden later
            // in an Activity Class
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClickListener(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public IconAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate and load the layout of the element

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_layout,parent,false);
        return new IconAdapter.myViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull IconAdapter.myViewHolder holder, int position) {
        // Assign information to the existing View elements
        // to be displayed

        Icon currentIcon = mList.get(position);
        holder.mIcon.setImageResource(currentIcon.id);
        // holder.mName.setText(currentIcon.name);
    }

    @Override
    public int getItemCount() {
        // Return the size of the List
        // needed for the RecyclerView

        return mList.size();
    }
}
