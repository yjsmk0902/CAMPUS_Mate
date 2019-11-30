package com.matemaker.campusmate.ui.activity.moimboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.matemaker.campusmate.R;

import java.util.ArrayList;

public class MoimBoardManageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MoimBoardManagerListAdapter mAdapter;

    public String number ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_board_manage);
        number = getIntent().getStringExtra("number");
        if(!number.isEmpty())
            getWaitList();
    }

    public void getWaitList(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("moim")
                .child(number)
                .child("wait");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println(snapshot.getKey());
                    data.add(snapshot.getKey());
                }

                recyclerView = findViewById(R.id.board_manage_recycler);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                mAdapter = new MoimBoardManagerListAdapter(MoimBoardManageActivity.this,data);

                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(postListener);
    }
}
