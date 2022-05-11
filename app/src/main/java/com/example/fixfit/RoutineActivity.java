package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.fixfit.fragment.InputRoutineDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RoutineActivity extends AppCompatActivity {

    private ListView list;

    private List<String> routines = new ArrayList<>();

    private RoutineAdapter adapter;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        preferences = getSharedPreferences("routines", MODE_PRIVATE);

        list = findViewById(R.id.routine_list);
        adapter = new RoutineAdapter();

        list.setAdapter(adapter);

        loadRoutines();
    }

    public void loadRoutines() {
        int size = preferences.getInt("size", 0);

        routines.clear();

        for(int i = 0; i < size; i++) {
            routines.add(preferences.getString("routine" + i, ""));
        }

        adapter.notifyDataSetChanged();
    }


    public void saveRoutines() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("size", routines.size());

        for(int i = 0; i < routines.size(); i++) {
            editor.putString("routine" + i, routines.get(i));
        }

        editor.apply();
    }

    class RoutineAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return routines.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return routines.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(position < routines.size())
                return routines.get(position).hashCode();
            else
                return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position < routines.size()) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.frag_routine, parent, false);
                CheckBox checkBox = convertView.findViewById(R.id.frag_routine_checkbox);

                Calendar calendar = Calendar.getInstance();

                String routineName = routines.get(position);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                String key = year + "-" + month + "-" + day + "-" + routineName;

                checkBox.setText(routineName);

                if(preferences.contains(key)) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = preferences.edit();
                        if(isChecked) {
                            editor.putBoolean(key, true);
                        } else {
                            editor.remove(key);
                        }

                        editor.apply();
                    }
                });
            } else {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.frag_routine_plus, parent, false);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputRoutineDialog dialog = new InputRoutineDialog();
                        dialog.setListener(text -> {
                            routines.add(text);

                            adapter.notifyDataSetChanged();
                            saveRoutines();
                        });

                        dialog.show(getSupportFragmentManager(), "routinedialog");
                    }
                });
            }
            return convertView;
        }
    }

}