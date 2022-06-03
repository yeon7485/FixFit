package com.example.fixfit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixfit.fragment.WorkOutDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HealthScheduleActivity extends AppCompatActivity {
    public String fname = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2;
    public EditText contextEditText;
    public FrameLayout schedule_layout;
    public String ViewIndex = "0";
    public static Context mContext;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("WorkOutSet");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_schedule);
        mContext = this;
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView2 = findViewById(R.id.textView2);
        contextEditText = findViewById(R.id.contextEditText);
        schedule_layout = findViewById(R.id.schedule_layout);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                schedule_layout.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d  운동일지", year, month + 1, dayOfMonth));
                contextEditText.setText("");
                checkDay(year, month, dayOfMonth);

                //dialog 띄우기
                WorkOutDialog dialog = new WorkOutDialog();

                dialog.show(getSupportFragmentManager(), "DialogWorkOut");

                dialog.setListener(() -> {
                    String date = year + "-" + (month + 1) + "-";
                    if(dayOfMonth < 10)
                        date += "0";
                    date += dayOfMonth;
                    dialog.getTime().setText(" - " + year + ". " + (month + 1) + "." + dayOfMonth);
                    db.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.getKey().equals("1000")) {
                                    dialog.getPose1000().setText(String.valueOf(ds.getValue()));
                                }
                                if (ds.getKey().equals("2000")) {
                                    dialog.getPose2000().setText(String.valueOf(ds.getValue()));
                                }
                                if (ds.getKey().equals("2001")) {
                                    dialog.getPose2001().setText(String.valueOf(ds.getValue()));
                                }
                                if (ds.getKey().equals("2002")) {
                                    dialog.getPose2002().setText(String.valueOf(ds.getValue()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                });

            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = contextEditText.getText().toString();
                if (!str.equals("")) {
                    saveDiary(fname);
                    textView2.setText(str);
                    schedule_layout.setVisibility(View.INVISIBLE);
                    save_Btn.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.VISIBLE);
                    del_Btn.setVisibility(View.VISIBLE);
                    contextEditText.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    ViewIndex = "0";

                } else {
                    ViewIndex = "1";
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요!!!", Toast.LENGTH_LONG).show();
                }
                Log.i("index", ViewIndex);
            }
        });
    }

    public void checkDay(int cYear, int cMonth, int cDay) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";//저장할 파일 이름설정
        FileInputStream fis = null;//FileStream fis 변수

        try {
            fis = openFileInput(fname);
            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);
            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);
            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);
            ViewIndex = "0";
            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule_layout.setVisibility(View.VISIBLE);
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                    ViewIndex = "1";
                }
            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule_layout.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(fname);
                }
            });
            if (textView2.getText().equals("")) {
                schedule_layout.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                ViewIndex = "1";

            }

        } catch (Exception e) {
            ViewIndex = "1";
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay) {
        FileOutputStream fos = null;
        String content = contextEditText.getText().toString();
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            fos.write((content).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //상황별 뒤로가기 버튼 이벤트
    @Override
    public void onBackPressed() {
        if (ViewIndex.equals("1")) {
            schedule_layout.setVisibility(View.INVISIBLE);
            ViewIndex = "0";
        } else {
            super.onBackPressed();
        }
    }
}
