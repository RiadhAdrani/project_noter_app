package com.example.noter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SimpleInputDialog extends AppCompatDialogFragment {

    // View of the dialog
    View dialog;

    // Context of the Activity
    // in which the adapter is used.
    Context mContext;

    // a Buttons interface instance
    Buttons setButtons;

    // View for the EditText
    EditText inputField;

    // Hint for the inputField
    String hint;

    public SimpleInputDialog(Context context, String hint){
        this.mContext = context;
        this.hint = hint;
    }

    interface Buttons{
        // Interface for the buttons
        void onCancelClickListener();
        void onConfirmClickListener();
    }

    public void setButtons(SimpleInputDialog.Buttons buttons){
        // Interface constructor like

        this.setButtons = buttons;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Function called to build the dialog box

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialog = inflater.inflate(R.layout.input_string_dialog_layout,null);
        builder.setView(dialog);

        inputField = dialog.findViewById(R.id.input_text);
        inputField.setHint(hint);

        Button cancelButton = dialog.findViewById(R.id.cancel_action_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtons.onCancelClickListener();
            }
        });

        Button confirmButton = dialog.findViewById(R.id.confirm_action_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtons.onConfirmClickListener();
            }
        });

        return builder.create();
    }
}
