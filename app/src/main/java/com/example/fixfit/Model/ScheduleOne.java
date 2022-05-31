package com.example.fixfit.Model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.fixfit.R;

public class ScheduleOne extends LinearLayout {
    private TextView date;
    private TextView spec;

    private View view1;
    private View view2;
    private View view3;
    private View view4;

    public ScheduleOne(Context context) {
        super(context);
        initView();
    }

    public ScheduleOne(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScheduleOne(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ScheduleOne(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.schedule_one, this, false);

        addView(v);

        date = findViewById(R.id.schedule_one_date);
        spec = findViewById(R.id.schedule_one_date_spec);
        view1 = findViewById(R.id.schedule_one_1);
        view2 = findViewById(R.id.schedule_one_2);
        view3 = findViewById(R.id.schedule_one_3);
        view4 = findViewById(R.id.schedule_one_4);
    }

    public void turnOn(int index) {
        switch (index) {
            case 1:
                view1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_one_1));
                break;
            case 2:
                view2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_one_2));
                break;
            case 3:
                view3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_one_3));
                break;
            case 4:
                view4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.schedule_one_4));
                break;
        }
    }

    public void turnOff(int index) {
        switch (index) {
            case 1:
                view1.setBackground(null);
                break;
            case 2:
                view2.setBackground(null);
                break;
            case 3:
                view3.setBackground(null);
                break;
            case 4:
                view4.setBackground(null);
                break;
        }
    }

    public TextView getDate() {
        return date;
    }

    public TextView getSpec() {
        return spec;
    }

    public View getView1() {
        return view1;
    }

    public View getView2() {
        return view2;
    }

    public View getView3() {
        return view3;
    }

    public View getView4() {
        return view4;
    }
}
