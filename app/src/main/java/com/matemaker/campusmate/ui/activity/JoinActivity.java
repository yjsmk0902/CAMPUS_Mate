package com.matemaker.campusmate.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.UserDTO;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "JoinActivity";

    private FirebaseAuth mAuth;
    private Button buttonCreate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mAuth = FirebaseAuth.getInstance();

        buttonCreate = findViewById(R.id.button_join_create);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser(){
        RadioGroup GG = findViewById(R.id.edit_join_gender_btn);

        final String email = ((EditText)findViewById(R.id.edit_join_email)).getText().toString();
        final String password = ((EditText)findViewById(R.id.edit_join_pw)).getText().toString();
        final String name = ((EditText)findViewById(R.id.edit_join_name)).getText().toString();
        final String age = ((EditText)findViewById(R.id.edit_join_age)).getText().toString();
        final String stu_num = ((EditText)findViewById(R.id.edit_join_stu_num)).getText().toString();
        final String subject = ((EditText)findViewById(R.id.edit_join_subject)).getText().toString();
        final String gender = ((RadioButton)findViewById(GG.getCheckedRadioButtonId())).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            getSharedPreferences("user",0).edit()
                                    .putString("id",email)
                                    .putString("pw",password)
                                    .apply();

                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("user").child(user.getUid());
                            UserDTO userDTO = new UserDTO(user.getUid(),user.getEmail(), name, age,
                                    stu_num, gender, subject);
                            myRef.setValue(userDTO.toMap());

                            Intent intent = new Intent(JoinActivity.this,MainActivity.class);
                            //intent로는 최소한의 정보만 넘기자 ex) 제일 중요한 키가 되는 uid만 넘겨도 상관없음.
                            //어차피 각 activity에서 firebase에 통신하기 때문에 굳이 모든 자료를 들고 갈 필요는 없음.
                            intent.putExtra("uid", user.getUid());
                            intent.putExtra("email", user.getEmail());

                            startActivity(intent);

                            Toast.makeText(JoinActivity.this,"회원가입완료",Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });

    }


}