package com.example.fixfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class FragSetup extends Fragment {
    public FragSetup() {
    }

    int REQUEST_IMAGE_CODE = 1001;
    String themeColor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setup, container, false);
        View dialogView = inflater.inflate(R.layout.dialog_profile, null);
        View dialogTheme = inflater.inflate(R.layout.dialog_theme, null);

        final EditText dlgName = (EditText) dialogView.findViewById(R.id.dlgName);
        final EditText dlgHeight = (EditText) dialogView.findViewById(R.id.dlgHeight);
        final EditText dlgBirth = (EditText) dialogView.findViewById(R.id.dlgBirth);

        final RadioGroup dialog_rg = (RadioGroup) dialogView.findViewById(R.id.dialog_rg);
        final RadioGroup theme_rg = (RadioGroup) dialogTheme.findViewById(R.id.RGroup);

        String[] data = {"프로필 설정", "프로필 이미지 설정", "테마 설정"};


        ListView list = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_list, data);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                if (position == 0) {
                    try {
                        if (dialogView.getParent() != null)
                            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
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
                    } catch (Exception e) {

                    }

                } else if (position == 1) {
                    Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(in, REQUEST_IMAGE_CODE);
                } else {
                    if (dialogTheme.getParent() != null)
                        ((ViewGroup) dialogTheme.getParent()).removeView(dialogTheme);
                    builder.setView(dialogTheme);
                    RadioButton light = (RadioButton) dialogTheme.findViewById(R.id.RB_Light);
                    RadioButton dark = (RadioButton) dialogTheme.findViewById(R.id.RB_Dark);
                    Log.v("rb", light.getText().toString());
                    if(ThemeUtil.modLoad(getContext()).equals("light")){
                        light.setChecked(true);
                    } else{
                        dark.setChecked(true);
                    }

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int checkedId = theme_rg.getCheckedRadioButtonId();
                            switch (checkedId) {
                                case R.id.RB_Light:
                                    themeColor = ThemeUtil.LIGHT_MODE;
                                    ThemeUtil.applyTheme(themeColor);
                                    ThemeUtil.modSave(getContext(), themeColor);
                                    break;
                                case R.id.RB_Dark:
                                    themeColor = ThemeUtil.DARK_MODE;
                                    ThemeUtil.applyTheme(themeColor);
                                    ThemeUtil.modSave(getContext(), themeColor);
                                    break;
                            }

                            Toast.makeText(getActivity(), "수정완료", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("닫기", null);
                    builder.show();
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            Uri image = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                ((MainActivity) getActivity()).ImgProfile.setImageBitmap(bitmap);
                ((MainActivity) getActivity()).setBitmap(((MainActivity) getActivity()).image, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
