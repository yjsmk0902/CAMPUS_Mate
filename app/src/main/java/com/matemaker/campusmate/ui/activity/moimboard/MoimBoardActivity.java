package com.matemaker.campusmate.ui.activity.moimboard;

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

    private TextView textWrite, textInfo, textJoinManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_board);
        textWrite = findViewById(R.id.text_board_write);
        textInfo = findViewById(R.id.text_board_info);
        textJoinManage = findViewById(R.id.text_board_join_manage);

        //임시
        String str = "";
        str += "title: " + getIntent().getStringExtra("title") + "\n";
        str += "관리자: " + getIntent().getStringExtra("uid") + "\n";
        str += "subtitle: " + getIntent().getStringExtra("subtitle") + "\n";
        str += "number: " + getIntent().getStringExtra("number") + "\n";


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
        textInfo.setText(str);
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