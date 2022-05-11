package com.example.fixfit.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.fixfit.R;

import java.util.Arrays;

public class InputNutrientDialog extends DialogFragment {

    private EditText input;

    private Button btnConfirm1;
    private Button btnConfirm2;
    private Button btnConfirm3;
    private Button btnCancel;

    private Spinner spinner;

    private InputNutrientDialog.OnConfirmClickListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Log.d("scheduleview", "asdf");

        View dialog = inflater.inflate(R.layout.dialog_register_nutrient, null);

        spinner = dialog.findViewById(R.id.dialog_register_type);
        spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.asList("아침", "점심", "저녁")));

        btnConfirm1 = dialog.findViewById(R.id.dialog_register_n1);
        btnConfirm2 = dialog.findViewById(R.id.dialog_register_n2);
        btnConfirm3 = dialog.findViewById(R.id.dialog_register_n3);
        btnCancel = dialog.findViewById(R.id.dialog_register_cancel);

        input = dialog.findViewById(R.id.dialog_register_input);

        btnCancel.setOnClickListener(view -> InputNutrientDialog.this.getDialog().cancel());

        btnConfirm1.setOnClickListener((View.OnClickListener) view -> {
            listener.onDateSet(spinner.getSelectedItemPosition(), 0, Integer.parseInt(input.getText().toString()));
            InputNutrientDialog.this.getDialog().cancel();
        });

        btnConfirm2.setOnClickListener((View.OnClickListener) view -> {
            listener.onDateSet(spinner.getSelectedItemPosition(), 1, Integer.parseInt(input.getText().toString()));
            InputNutrientDialog.this.getDialog().cancel();
        });

        btnConfirm3.setOnClickListener((View.OnClickListener) view -> {
            listener.onDateSet(spinner.getSelectedItemPosition(), 2, Integer.parseInt(input.getText().toString()));
            InputNutrientDialog.this.getDialog().cancel();
        });

        builder.setView(dialog);

        return builder.create();
    }


    public void setListener(InputNutrientDialog.OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public Button getBtnConfirm1() {
        return btnConfirm1;
    }

    public Button getBtnConfirm2() {
        return btnConfirm2;
    }

    public Button getBtnConfirm3() {
        return btnConfirm3;
    }

    public EditText getInput() {
        return input;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public interface OnConfirmClickListener {
        void onDateSet(int time2, int nutrient, int value);
    }
}
