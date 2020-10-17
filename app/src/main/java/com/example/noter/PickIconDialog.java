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

    OnCreate onCreate;
    View dialog;
    Context mContext;

    public PickIconDialog(View dialog, Context mContext){
        this.dialog = dialog;
        this.mContext = mContext;
    }

    public PickIconDialog(Context mContext){
        this.mContext = mContext;
        dialog = new View(mContext);
    }

    public interface OnCreate{
        void buildRecyclerView();
    }

    public void setOnCreate(OnCreate onCreate){
        this.onCreate = onCreate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialog = inflater.inflate(R.layout.icon_pick_layout,null);
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
