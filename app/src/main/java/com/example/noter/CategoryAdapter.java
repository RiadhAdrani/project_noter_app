package com.example.noter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.myViewHolder> {

    // CategoryAdapter is a custom created Adapter RecyclerView
    // Used to display categories dynamically

    // Reference to the list that will be fed to the adapter
    // to be displayed
    ArrayList<Category> mList;

    // Contains overridable functions
    // mainly onClickListeners
    // could be implemented in an Activity
    OnCategoryClickListener listener;

    Context context;

    public CategoryAdapter(ArrayList<Category> mList,Context context){
        // Basic constructor

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate and load the layout of the element

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout,parent,false);

        // Return the inflated view to be displayed
        return new CategoryAdapter.myViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.myViewHolder holder, final int position) {
        // Assign information to the existing View elements
        // to be displayed

        // get the current category element
        Category currentCategory = mList.get(position);

        // assign text to the TextView
        holder.mText.setText(currentCategory.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null){
                    listener.onClickListener(position);
                }

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (listener != null){
                    listener.onLongClickListener(position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder of every element
        // Actually prepare the template of the element

        // TextView to display the category name
        public TextView mText;

        public myViewHolder(@NonNull final View itemView, final OnCategoryClickListener listener) {
            super(itemView);

            mText = itemView.findViewById(R.id.category_name_textView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (listener != null){
//                        listener.onClickListener(getAdapterPosition());
//                    }
//
//                }
//            });
//
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    if (listener != null){
//                        listener.onLongClickListener(getAdapterPosition());
//                    }
//                    return true;
//                }
//            });

        }
    }

    interface OnCategoryClickListener{
        // Interface containing (Hollow) overridable functions

        void onClickListener(int position);
        void onLongClickListener(int position);
    }

    void setOnCategoryClickListener(OnCategoryClickListener listener){
        this.listener = listener;
    }
}
