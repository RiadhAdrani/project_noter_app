package com.example.noter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CategoryOptionDialog extends AppCompatDialogFragment {

    View dialog;

    Category category;

    int position;

    EditText inputField;

    onCategoryOptionClickListener listener;

    CategoryOptionDialog(Category category, int position){
        this.category = category;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Function called to build the dialog box

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialog = inflater.inflate(R.layout.category_option_dialog_layout,null);
        builder.setView(dialog);

        TextView categoryName = dialog.findViewById(R.id.category_option_name);
        inputField = dialog.findViewById(R.id.category_rename_option);
        Button renameButton = dialog.findViewById(R.id.category_rename_button);
        Button deleteButton = dialog.findViewById(R.id.category_delete_button);
        Button cancelButton = dialog.findViewById(R.id.category_cancel_button);

        categoryName.setText(category.name);
        inputField.setText(category.name);

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRenameButtonClick(position);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteButtonClick(position);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancelButtonClick(position);
            }
        });



        return builder.create();
    }

    interface onCategoryOptionClickListener{
        void onRenameButtonClick(int position);
        void onDeleteButtonClick(int position);
        void onCancelButtonClick(int position);
    }

    void setOnCategoryOptionClickListener(onCategoryOptionClickListener listener){
        this.listener = listener;
    }
}

