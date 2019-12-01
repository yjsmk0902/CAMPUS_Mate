package com.matemaker.campusmate.ui.activity.moimboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.BoardDTO;
import com.matemaker.campusmate.ui.activity.MainActivity;

public class MoimBoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MoimBoardListAdapter mAdapter;

    private TextView textWrite, textTitle, textSubtitle, textJoinManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_board);
        textWrite = findViewById(R.id.text_board_write);
        textTitle = findViewById(R.id.text_board_title);
        textSubtitle = findViewById(R.id.text_board_subtitle);
        textJoinManage = findViewById(R.id.text_board_join_manage);

        if(MainActivity.uid.equals(getIntent().getStringExtra("uid"))){
            textJoinManage.setVisibility(View.VISIBLE);
            textJoinManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MoimBoardActivity.this, MoimBoardManageActivity.class);
                    intent.putExtra("number",getIntent().getStringExtra("number"));
                    startActivity(intent);
                }
            });
        }
        textTitle.setText(getIntent().getStringExtra("title"));
        textSubtitle.setText(getIntent().getStringExtra("subtitle"));
        checkMember();

        textWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoimBoardActivity.this, MoimBoardWriteActivity.class);
                intent.putExtra("number",getIntent().getStringExtra("number"));
                startActivity(intent);
            }
        });
        initList();
    }

    public void initList(){
        recyclerView = findViewById(R.id.board_recycler);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //나중에 정보 따로 저장해서 아래에 넣기

        // specify an adapter (see also next example)
        getData();
    }

    public void checkMember(){
        FirebaseDatabase.getInstance().getReference()
                .child("moim").child(getIntent().getStringExtra("number"))
                .child("member").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isMember = false;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(MainActivity.uid)){
                        isMember = true;
                    }
                }
                if(isMember){
                    textWrite.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("moim/"+getIntent().getStringExtra("number")+"/board");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<BoardDTO> boardDTOs = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    BoardDTO boardDTO = postSnapshot.getValue(BoardDTO.class);
                    boardDTOs.add(boardDTO);
                }
                mAdapter = new MoimBoardListAdapter(boardDTOs);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(postListener);
    }

}