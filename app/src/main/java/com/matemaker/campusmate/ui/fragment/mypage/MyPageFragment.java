package com.matemaker.campusmate.ui.fragment.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.UserDTO;
import com.matemaker.campusmate.ui.activity.MainActivity;


public class MyPageFragment extends Fragment {

    public View root;
    private TextView age;
    private TextView name;
    private TextView gender;
    private TextView stuNum;
    private TextView subject;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_my_page, container, false);

        age = root.findViewById(R.id.textView_showAge);
        name = root.findViewById(R.id.textView_showName);
        gender = root.findViewById(R.id.textView_showGender);
        stuNum = root.findViewById(R.id.textView_showStudentID);
        subject = root.findViewById(R.id.textView_showDept);

        getMyPage();
        return root;
    }

    void getMyPage(){
        if(MainActivity.uid == null){
            return;
        }
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(MainActivity.uid);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDTO user = dataSnapshot.getValue(UserDTO.class);
                age.setText(user.age);
                name.setText(user.name);
                gender.setText(user.gender);
                stuNum.setText(user.stu_num);
                subject.setText(user.subject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userRef.addValueEventListener(postListener);
    }
}