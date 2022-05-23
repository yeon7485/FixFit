package com.cookandroid.fixfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragSetup extends Fragment {
    public FragSetup(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setup, container, false);
        View dialogView = inflater.inflate(R.layout.dialog_profile, null);

        final EditText dlgName = (EditText) dialogView.findViewById(R.id.dlgName);
        final EditText dlgHeight = (EditText) dialogView.findViewById(R.id.dlgHeight);
        final EditText dlgBirth = (EditText) dialogView.findViewById(R.id.dlgBirth);

        final RadioGroup dialog_rg = (RadioGroup) dialogView.findViewById(R.id.dialog_rg);

        String [] data = {"프로필 설정", "프로필 이미지 설정", "글자 크기 설정"};


        ListView list = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                if(position == 0){
                    try{
                        builder.setTitle("프로필 설정");
                        builder.setView(dialogView);
                        builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = dlgName.getText().toString();
                                String height = dlgHeight.getText().toString();
                                String birth = dlgBirth.getText().toString();

                                int checkedId = dialog_rg.getCheckedRadioButtonId();
                                RadioButton rb = (RadioButton) dialogView.findViewById(checkedId);

                                String sex = rb.getText().toString();

                                MainActivity main = (MainActivity) getActivity();
                                main.getTextData(name, sex, height, birth);

                                Toast.makeText(getActivity(), "수정완료", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.show();
                    }
                    catch (Exception e){

                    }

                }

                else if(position == 1){
                    // 갤러리에서 사진 불러오는 액티비티 있지 않았나요??
                }

            }
        });
        return view;
    }
}
