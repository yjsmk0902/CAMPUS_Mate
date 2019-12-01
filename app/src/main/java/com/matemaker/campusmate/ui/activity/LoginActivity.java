package com.matemaker.campusmate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matemaker.campusmate.R;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button login, nologin;
    private EditText id,pw;
    @Override
    //예외처리 만드세요
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        id = findViewById(R.id.edit_login_id);
        pw = findViewById(R.id.edit_login_pw);
        nologin = findViewById(R.id.btn_login_nosign);

        nologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        String savedID = getSharedPreferences("user", Context.MODE_PRIVATE).getString("id",null);
        String savedPW = getSharedPreferences("user", Context.MODE_PRIVATE).getString("pw",null);

        id.setText(savedID);
        pw.setText(savedPW);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(id.getText().toString(),pw.getText().toString());
            }
        });

    }

    public void createUser(View v){
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
        startActivity(intent);
    }
    public void loginUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    getSharedPreferences("user",Context.MODE_PRIVATE).edit()
                            .putString("id",id.getText().toString())
                            .putString("pw",pw.getText().toString())
                            .apply();

                    FirebaseUser user = mAuth.getCurrentUser();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("uid",user.getUid());
                    intent.putExtra("email",user.getEmail());

                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}