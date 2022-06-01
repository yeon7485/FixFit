package com.example.fixfit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fixfit.Model.ScheduleOne;
import com.example.fixfit.fragment.InputNutrientDialog;
import com.example.fixfit.fragment.NutrientViewDialog;
import com.example.fixfit.fragment.YearMonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietScheduleActivity extends AppCompatActivity {

    private List<ScheduleOne> schedules = new ArrayList<>();

    private TextView timeView;
    private TextView monthView;
    private TextView monthSpecView;
    private TextView change;

    private int year;
    private int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);

        timeView = findViewById(R.id.frag_schedule_time);
        monthView = findViewById(R.id.frag_schedule_month);
        monthSpecView = findViewById(R.id.frag_schedule_month_spec);
        change = findViewById(R.id.change);

        initSchedule();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        initData();

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearMonthPickerDialog pd = new YearMonthPickerDialog(year, month);
                pd.setListener((datePicker, year, month, day) -> {
                    DietScheduleActivity.this.year = year;
                    DietScheduleActivity.this.month = month - 1; // 아마..

                    initData();
                });
                pd.show(getSupportFragmentManager(), "YearMonthPicker");
            }
        });
    }


    private static String[] monthText = new String[] {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
    };

    private static String[] weekText = new String[] {
            "", "일", "월", "화", "수", "목", "금", "토"
    };

    public void initData() {
        timeView.setText(year + "년 " + (month + 1) + "월");
        monthView.setText(String.valueOf((month + 1)));
        monthSpecView.setText(monthText[month]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);

        // 일요일이 0
        int start = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        Log.d("scheduleview", "start: " + start);

        // past
        for(int i = start - 1; i >= 0; i--) {
            calendar.add(Calendar.DATE, -1);
            schedules.get(i).getDate().setText(String.valueOf(calendar.get(Calendar.DATE)));
            schedules.get(i).getDate().setTextColor(Color.GRAY);

            Log.d("scheduleview", calendar.get(Calendar.DATE) + ", " + i);
        }

        Calendar indexFirst = Calendar.getInstance();
        indexFirst.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        indexFirst.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        indexFirst.set(Calendar.DATE, calendar.get(Calendar.DATE));

        // current
        calendar.set(Calendar.DATE, 1);

        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.d("scheduleview", "max: " + max);

        for(int i = start; i < start + max; i++) {
            schedules.get(i).getDate().setText(String.valueOf(calendar.get(Calendar.DATE)));

            switch(i%7 + 1) {
                case Calendar.SUNDAY:
                    schedules.get(i).getDate().setTextColor(Color.RED);
                    break;
                case Calendar.SATURDAY:
                    schedules.get(i).getDate().setTextColor(Color.BLUE);
                    break;
                default:
                    schedules.get(i).getDate().setTextColor(ContextCompat.getColor(this, R.color.text_color));
                    break;
            }

            Log.d("scheduleview", calendar.get(Calendar.DATE) + ", " + i + ", " + calendar.get(Calendar.DAY_OF_WEEK));
            calendar.add(Calendar.DATE, 1);
        }

        // future
        for(int i = start + max; i < schedules.size(); i++) {
            schedules.get(i).getDate().setText(String.valueOf(calendar.get(Calendar.DATE)));
            schedules.get(i).getDate().setTextColor(Color.GRAY);

            Log.d("scheduleview", calendar.get(Calendar.DATE) + ", " + i);
            calendar.add(Calendar.DATE, 1);
        }

        SharedPreferences scheduleSaves = getSharedPreferences("schedules", MODE_PRIVATE);

        // 저장 여부 체크
        for(int i = 0; i < schedules.size(); i++) {
            int currentMonth = indexFirst.get(Calendar.MONTH) + 1;
            int currentDate = indexFirst.get(Calendar.DATE);
            int week = indexFirst.get(Calendar.DAY_OF_WEEK);
            String time = indexFirst.get(Calendar.YEAR) + "-" + currentMonth + "-" + currentDate;

            Log.d("scheduleview", time);

            ScheduleOne schedule = schedules.get(i);
            //포맷: time-숫자-숫자2
            //숫자: 0 = 아침 1 = 점심 2 = 저녁 3 = 몸무게
            //숫자2: 0=탄 1=단 2=지

            int totalKcal0 = (int)(4.1 * (getNutrientCompact(scheduleSaves, time, 0, 0) +
                    getNutrientCompact(scheduleSaves, time, 1, 0) +
                    getNutrientCompact(scheduleSaves, time, 2, 0)));

            int totalKcal1 = 9 * (getNutrientCompact(scheduleSaves, time, 0, 1) +
                    getNutrientCompact(scheduleSaves, time, 1, 1) +
                    getNutrientCompact(scheduleSaves, time, 2, 1));

            int totalKcal2 = 4 * (getNutrientCompact(scheduleSaves, time, 0, 2) +
                    getNutrientCompact(scheduleSaves, time, 1, 2) +
                    getNutrientCompact(scheduleSaves, time, 2, 2));

            int totalKcal = totalKcal0 + totalKcal1 + totalKcal2;

            int weight = getWeight(scheduleSaves, time);

            if(totalKcal0 > 0) schedule.turnOn(1); else schedule.turnOff(1);
            if(totalKcal1 > 0) schedule.turnOn(2); else schedule.turnOff(2);
            if(totalKcal2 > 0) schedule.turnOn(3); else schedule.turnOff(3);
            if(totalKcal > 0) schedule.turnOn(4); else schedule.turnOff(4);

            schedule.setOnClickListener(view -> {
                NutrientViewDialog dialog = new NutrientViewDialog();

                dialog.show(getSupportFragmentManager(), "NutrientViewDialog");

                dialog.setListener(() -> {
                    dialog.getTime().setText(" - " + currentMonth + ". " + currentDate + ". (" + weekText[week] + ")");
                    dialog.getKg().setText(String.valueOf(weight));
                    dialog.getKcalTotal().setText(String.valueOf(totalKcal));
                    dialog.getNutrient1().setText(String.valueOf(totalKcal0) + "kcal");
                    dialog.getNutrient2().setText(String.valueOf(totalKcal1) + "kcal");
                    dialog.getNutrient3().setText(String.valueOf(totalKcal2) + "kcal");
                });
            });

            schedule.setOnLongClickListener(view -> {
                InputNutrientDialog dialog = new InputNutrientDialog();
                dialog.setListener((int time2, int nutrient, int value) -> {
                    setNutrient(scheduleSaves, time, time2, nutrient, value);
                });
                dialog.show(getSupportFragmentManager(), "YearMonthPicker");
                return true;
            });

            indexFirst.add(Calendar.DATE, 1);
        }
    }

    public void setNutrient(SharedPreferences scheduleSaves, String time, int time2, int nutrientType, int value) {
        Log.d("schedulesview", "set neutrient: " + time + ", timing: " + time2 + ", nutrientType: " + nutrientType + ", value: " + value);
        SharedPreferences.Editor editor = scheduleSaves.edit();

        editor.putInt(createKey(time, time2, nutrientType), value);

        editor.apply();

        // 업데이트
        initData();
    }

    public int getWeight(SharedPreferences scheduleSaves, String time) {
        return getNutrient(scheduleSaves, createKey(time, 3, 0));
    }

    public int getNutrientCompact(SharedPreferences scheduleSaves, String time, int time2, int nutrientType) {
        return getNutrient(scheduleSaves, createKey(time, time2, nutrientType));
    }

    public int getNutrient(SharedPreferences scheduleSaves, String key) {
        if(scheduleSaves.contains(key)) {
            return scheduleSaves.getInt(key, 0);
        }
        return 0;
    }

    public String createKey(String time, int time2, int nutrientType) {
        return time + "-" + time2 + "-" + nutrientType;
    }

//    public int calculateKcal(int carbohydrate, int fat, int protein) {
//        return (int) (4.1 * carbohydrate + 9 * fat + 4 * protein);
//    }

    public void initSchedule() {
        initSchedulesLine((LinearLayout) findViewById(R.id.frag_schedule_1));
        initSchedulesLine((LinearLayout) findViewById(R.id.frag_schedule_2));
        initSchedulesLine((LinearLayout) findViewById(R.id.frag_schedule_3));
        initSchedulesLine((LinearLayout) findViewById(R.id.frag_schedule_4));
        initSchedulesLine((LinearLayout) findViewById(R.id.frag_schedule_5));
        initSchedulesLine((LinearLayout) findViewById(R.id.frag_schedule_6));


    }

    public void initSchedulesLine(LinearLayout layout) {
        for(int i = 0; i < layout.getChildCount(); i++) {
            ScheduleOne scheduleOne = (ScheduleOne) layout.getChildAt(i);
            schedules.add(scheduleOne);
        }
    }
}