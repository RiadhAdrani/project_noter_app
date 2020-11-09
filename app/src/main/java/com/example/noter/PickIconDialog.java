package com.example.noter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PickIconDialog extends AppCompatDialogFragment{

    // Class describing the Dialog showing when
    // the user need to pick an icon

    // interface instance
    OnCreate onCreate;

    // View of the dialog
    View dialog;

    // Context of the Activity
    // in which the adapter is used.
    Context mContext;

    public PickIconDialog(Context mContext){
        // Constructor 2

        this.mContext = mContext;
        dialog = new View(mContext);
    }

    public interface OnCreate{
        // Interface containing (Hollow) overridable functions

        void buildRecyclerView();
    }

    public void setOnCreate(OnCreate onCreate){
        // Allow the use of the interface

        this.onCreate = onCreate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Function called to build the dialog box

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // LayoutInflater inflater = getActivity().getLayoutInflater();
        dialog = getActivity().getLayoutInflater().inflate(R.layout.icon_pick_layout,null);

        builder.setView(dialog);

        onCreate.buildRecyclerView();

        Button cancelButton = dialog.findViewById(R.id.icon_dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }
}
