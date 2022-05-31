package com.example.fixfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixfit.Adapter.WorkoutAdapter;
import com.example.fixfit.Model.WorkoutModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RehabilitationActivity extends AppCompatActivity {

    private RecyclerView neck_recyclerview, waist_recyclerview, knee_recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("WorkOutSet");
    private String date;

    ArrayList<WorkoutModel> neckList = new ArrayList<>();
    ArrayList<WorkoutModel> waistList = new ArrayList<>();
    ArrayList<WorkoutModel> kneeList = new ArrayList<>();
    ArrayList<Integer> neckCodeList = new ArrayList<>();
    ArrayList<Integer> waistCodeList = new ArrayList<>();
    ArrayList<Integer> kneeCodeList = new ArrayList<>();

    int[] neck_img = {R.drawable.neck_1, R.drawable.neck_2};
    String[] neck_txt = {"목 당기기", "목 스트레칭"};

    int[] waist_img = {R.drawable.waist_1, R.drawable.waist_2, R.drawable.waist_3};
    String[] waist_txt = {"등 들어올리기", "윗몸 일으키기", "다리 뒤로 차기"};

    int[] knee_img = {R.drawable.knee_1};
    String[] knee_txt = {"앉아서 다리펴기"};

    WorkoutAdapter neck_adapter;
    WorkoutAdapter waist_adapter;
    WorkoutAdapter knee_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehabilitation);

        neck_recyclerview = (RecyclerView) findViewById(R.id.neck_recyclerview);
        neck_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        neck_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 가로

        waist_recyclerview = (RecyclerView) findViewById(R.id.waist_recyclerview);
        waist_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        waist_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 가로

        knee_recyclerview = (RecyclerView) findViewById(R.id.knee_recyclerview);
        knee_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        knee_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 가로


        for (int i = 0; i < neck_img.length; i++) {
            neckList.add(new WorkoutModel(neck_img[i], neck_txt[i]));
            neckCodeList.add(1000 + i);
        }
        neck_adapter = new WorkoutAdapter(neckList, neckCodeList);
        neck_recyclerview.setAdapter(neck_adapter);

        for (int i = 0; i < waist_img.length; i++) {
            waistList.add(new WorkoutModel(waist_img[i], waist_txt[i]));
            waistCodeList.add(2000 + i);
        }
        waist_adapter = new WorkoutAdapter(waistList, waistCodeList);
        waist_recyclerview.setAdapter(waist_adapter);

        for (int i = 0; i < knee_img.length; i++) {
            kneeList.add(new WorkoutModel(knee_img[i], knee_txt[i]));
            kneeCodeList.add(3000 + i);
        }
        knee_adapter = new WorkoutAdapter(kneeList, kneeCodeList);
        knee_recyclerview.setAdapter(knee_adapter);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("M", Locale.KOREA);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        date = yearFormat.format(currentTime) + "-" + monthFormat.format(currentTime) + "-" + dayFormat.format(currentTime);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int check = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(date)) {
                        check = 1;
                    }
                }
                if (check == 0) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("1000", "0");
                    map.put("2000", "0");
                    map.put("2001", "0");
                    map.put("2002", "0");
                    map.put("3000", "0");
                    db.child(date).setValue(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}