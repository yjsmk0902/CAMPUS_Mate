package com.matemaker.campusmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.UserDTO;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static String uid = null;
    public static String name = null;
    public static String email = null;

    private AppBarConfiguration mAppBarConfiguration;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*툴바(테마) 설정*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uid = getIntent().getStringExtra("uid");
        email = getIntent().getStringExtra("email");

        /*uid가 null일 경우도 고려해야 함. onDataChange 함수의 인자 값은 null이 아니라고 단정지어져 있음. -> @NonNull 시그널*/
        if(uid != null && email != null){
            FirebaseDatabase.getInstance().getReference().child("user")
                    .child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                        Log.d("MainActivity", userDTO.name);

                        //로그인 후 UI 작업은 아래 함수에서 진행하도록 하자.
                        updateUI(userDTO);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    finish();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration =new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_open,R.id.nav_manage,
                R.id.nav_out,R.id.nav_mypage)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
    }

    private void updateUI(UserDTO user){
        //TODO 로그인 후 처리
        View hView = navigationView.getHeaderView(0);
        Button buttonLogin = hView.findViewById(R.id.login_btn);
        TextView textResultLogin = hView.findViewById(R.id.login_text);
        buttonLogin.setVisibility(View.GONE);
        textResultLogin.setVisibility(View.VISIBLE);
        textResultLogin.setText(email);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void login_Activity(View v){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
