package com.example.fixfit.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.fixfit.R;

public class InputRoutineDialog extends DialogFragment {

    private EditText input;
    private Button btnConfirm;
    private Button btnCancel;
    private InputRoutineDialog.OnConfirmClickListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Log.d("scheduleview", "asdf");

        View dialog = inflater.inflate(R.layout.dialog_register_routine, null);

        btnConfirm = dialog.findViewById(R.id.dialog_register_routine_confirm);
        btnCancel = dialog.findViewById(R.id.dialog_register_routine_cancel);

        input = dialog.findViewById(R.id.dialog_register_routine_input);

        btnCancel.setOnClickListener(view -> InputRoutineDialog.this.getDialog().cancel());

        btnConfirm.setOnClickListener(view -> {
            listener.onItemClicked(input.getText().toString());
            InputRoutineDialog.this.getDialog().cancel();
        });


        builder.setView(dialog);

        return builder.create();
    }

    public void setListener(InputRoutineDialog.OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public Button getBtnConfirm() {
        return btnConfirm;
    }

    public EditText getInput() {
        return input;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public interface OnConfirmClickListener {
        void onItemClicked(String text);
    }
}
