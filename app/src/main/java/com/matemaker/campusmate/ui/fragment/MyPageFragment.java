package com.matemaker.campusmate.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.matemaker.campusmate.R;


public class MyPageFragment extends Fragment {

    public View root;
    private TextView age;
    private TextView name;
    private TextView gender;
    private TextView stu_num;
    private TextView subject;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_my_page, container, false);
        final TextView textView = root.findViewById(R.id.text_mypage);

        age = root.findViewById(R.id.textView_showAge);
        name = root.findViewById(R.id.textView_showName);
        gender = root.findViewById(R.id.textView_showGender);
        stu_num = root.findViewById(R.id.textView_showStudentID);
        subject = root.findViewById(R.id.textView_showDept);

        return root;
    }
}