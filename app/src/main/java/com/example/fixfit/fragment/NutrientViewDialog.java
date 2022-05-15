package com.example.fixfit.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fixfit.R;

import java.util.Arrays;
import java.util.Calendar;

public class NutrientViewDialog extends DialogFragment {

    private TextView time;

    private TextView kg;
    private TextView kcalTotal;

    private TextView nutrient1;
    private TextView nutrient2;
    private TextView nutrient3;

    private OnLoadedListener listener;

    public void setListener(OnLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_view_nutrient, null);

        time = dialog.findViewById(R.id.dialog_view_nutrient_date);
        kg = dialog.findViewById(R.id.dialog_view_nutrient_kg);
        kcalTotal = dialog.findViewById(R.id.dialog_view_nutrient_kcal);
        nutrient1 = dialog.findViewById(R.id.dialog_view_nutrient_n1);
        nutrient2 = dialog.findViewById(R.id.dialog_view_nutrient_n2);
        nutrient3 = dialog.findViewById(R.id.dialog_view_nutrient_n3);

        builder.setView(dialog);

        listener.OnLoadedListener();

        return builder.create();
    }

    public TextView getTime() {
        return time;
    }

    public TextView getKg() {
        return kg;
    }

    public TextView getKcalTotal() {
        return kcalTotal;
    }

    public TextView getNutrient1() {
        return nutrient1;
    }

    public TextView getNutrient2() {
        return nutrient2;
    }

    public TextView getNutrient3() {
        return nutrient3;
    }

    public interface OnLoadedListener {
        void OnLoadedListener();
    }
}
