package com.example.noter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Transition;

import java.util.ArrayList;
import java.util.Objects;

public class CategorySpinner extends ArrayAdapter<Category> {

    public onClickListener listener;

    public CategorySpinner(Context context, ArrayList<Category> list){
        super(context,0,list);
    }


    public interface onClickListener{
        void setOnDeleteClickListener(int position);
    }


    public void setOnDeleteClickListener(onClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent,listener);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent,listener);    }

    public View initView(final int position, View convertView, ViewGroup parent, final onClickListener listener){

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_spinner_card,
                    parent,
                    false
            );
        }

        TextView categoryNameView = convertView.findViewById(R.id.category_spinner_text);

        Category currentItem = getItem(position);

        if (currentItem != null){
            categoryNameView.setText(currentItem.name);
        }

        Log.d("SPINNER_DEBUG","Initializing spinner element : "+position);
        return convertView;
    }
}