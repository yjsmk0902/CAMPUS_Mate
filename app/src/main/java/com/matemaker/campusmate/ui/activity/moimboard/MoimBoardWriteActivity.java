package com.matemaker.campusmate.ui.activity.moimboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.matemaker.campusmate.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.matemaker.campusmate.database.BoardDTO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matemaker.campusmate.ui.activity.MainActivity;

public class MoimBoardWriteActivity extends AppCompatActivity {

    EditText title, contents;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_board_write);
        title = findViewById(R.id.edit_board_title);
        contents = findViewById(R.id.edit_board_contents);
        save = findViewById(R.id.btn_board_write);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardDTO boardDTO = new BoardDTO(
                        MainActivity.uid,
                        getIntent().getStringExtra("number"),
                        System.currentTimeMillis(),
                        title.getText().toString(),
                        contents.getText().toString()
                );

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("moim/"+getIntent().getStringExtra("number")+"/board");
                ref.push().setValue(boardDTO.toMap());
                finish();
            }
        });

    }
}
