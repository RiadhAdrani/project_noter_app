package com.example.noter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CategorySpinner extends ArrayAdapter<Category> {

    public CategorySpinner(Context context, ArrayList<Category> list){
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);    }

    private View initView(int position, View convertView, ViewGroup parent){
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

        Log.d("DEBUG","Initializing spinner element : "+position);
        return convertView;
    }
}