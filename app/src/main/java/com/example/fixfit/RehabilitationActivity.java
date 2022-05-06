package com.example.fixfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.fixfit.item.Workout;

import java.util.ArrayList;

public class RehabilitationActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView lower_back_recyclerview;
    private RecyclerView knee_recyclerview;
    private RecyclerView.LayoutManager layoutManager;


    ArrayList<Workout> lowerBackList = new ArrayList<>();
    ArrayList<Workout> kneeList = new ArrayList<>();

    int[] lowerBack_img = {R.drawable.lower_back_1, R.drawable.lower_back_2, R.drawable.lower_back_3};
    String[] lowerBack_txt = {"등 들어올리기", "지면 누르기", "상체 일으키기"};

    int[] knee_img = {R.drawable.knee_1};
    String[] knee_txt = {"앉아서 다리펴기"};

    WorkoutAdapter lower_back_adapter;
    WorkoutAdapter knee_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehabilitation);

        lower_back_recyclerview = (RecyclerView) findViewById(R.id.lower_back_recyclerview);
        lower_back_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lower_back_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 가로

        knee_recyclerview = (RecyclerView) findViewById(R.id.knee_recyclerview);
        knee_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        knee_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 가로


        for (int i = 0; i < lowerBack_img.length; i++) {
            lowerBackList.add(new Workout(lowerBack_img[i], lowerBack_txt[i]));
        }
        lower_back_adapter = new WorkoutAdapter(lowerBackList);
        lower_back_recyclerview.setAdapter(lower_back_adapter);

        for (int i = 0; i < knee_img.length; i++) {
            kneeList.add(new Workout(knee_img[i], knee_txt[i]));
        }
        knee_adapter = new WorkoutAdapter(kneeList);
        knee_recyclerview.setAdapter(knee_adapter);
    }


    @Override
    public void onClick(View view) {

    }
}