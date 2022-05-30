package com.example.fixfit.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.fixfit.R;

public class WorkOutDialog extends DialogFragment {

    private TextView time;

    private TextView pose1000;
    private TextView pose2000;

    private TextView pose2001;
    private TextView pose3000;

    private OnLoadedListener listener;

    public void setListener(OnLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_workout, null);

        time = dialog.findViewById(R.id.date);
        pose1000 = dialog.findViewById(R.id.pose1000);
        pose2000 = dialog.findViewById(R.id.pose2000);
        pose2001 = dialog.findViewById(R.id.pose2001);
        pose3000 = dialog.findViewById(R.id.pose3000);

        builder.setView(dialog);
        listener.OnLoadedListener();
        return builder.create();
    }



    public TextView getTime() {
        return time;
    }

    public TextView getPose1000() {
        return pose1000;
    }
    public TextView getPose2000() {
        return pose2000;
    }
    public TextView getPose2001() {
        return pose2001;
    }
    public TextView getPose3000() {
        return pose3000;
    }

    public interface OnLoadedListener {
        void OnLoadedListener();
    }
}
