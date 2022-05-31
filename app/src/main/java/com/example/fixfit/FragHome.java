package com.example.fixfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragHome extends Fragment {
    ImageButton btn1, btn2, btn3, btn4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        btn1 = (ImageButton) view.findViewById(R.id.btn1);
        btn2 = (ImageButton) view.findViewById(R.id.btn2);
        btn3 = (ImageButton) view.findViewById(R.id.btn3);
        btn4 = (ImageButton) view.findViewById(R.id.btn4);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TechNeckActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RehabilitationActivity.class);
                startActivity(intent);
            }
        });

        //식단기록 액티비티 연결
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DietScheduleActivity.class);
                startActivity(intent);
            }
        });
        //운동기록 액티비티 연결
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HealthScheduleActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
