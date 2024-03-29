package com.matemaker.campusmate.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.MoimDTO;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private View root; // HomeActivity 객체 정보

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeListAdapter mAdapter;

    private Button club;
    private Button study;
    private Button sports;
    private Button hobby;
    private Button light;
    private Button all;
    private Button buttonSearch;
    private String category = null;
    private EditText editSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        //textView.setText("모임피드");
        club = root.findViewById(R.id.club);
        study = root.findViewById(R.id.study);
        sports = root.findViewById(R.id.sports);
        hobby = root.findViewById(R.id.hobby);
        light = root.findViewById(R.id.light);
        all = root.findViewById(R.id.all);
        initList();
        return root;
    }

    public void initList(){

        recyclerView = root.findViewById(R.id.home_recycler);
        buttonSearch = root.findViewById(R.id.button_home_search);
        editSearch = root.findViewById(R.id.edit_home_search);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        //나중에 정보 따로 저장해서 아래에 넣기

        // specify an adapter (see also next example)

        getData();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(editSearch.getText().toString());
            }
        });
        club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "동아리";
                getData();
            }
        });
        hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "취미";
                getData();
            }
        });
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "번개";
                getData();
            }
        });
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "스터디";
                getData();
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "운동";
                getData();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = null;
                getData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void getData(final String str){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("moim");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MoimDTO> moimDTOs = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    MoimDTO moimDTO = postSnapshot.getValue(MoimDTO.class);
                    moimDTO.number = key;
                    if(moimDTO.title.contains(str)){
                        moimDTOs.add(moimDTO);
                    }
                }
                mAdapter = new HomeListAdapter(moimDTOs);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(postListener);
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("moim");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MoimDTO> moimDTOs = new ArrayList<>();
                if(category == null){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        String key = postSnapshot.getKey();
                        MoimDTO moimDTO = postSnapshot.getValue(MoimDTO.class);
                        moimDTO.number = key;
                        moimDTOs.add(moimDTO);
                    }
                }else{
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        if(category.equals(postSnapshot.getValue(MoimDTO.class).category)){
                            String key = postSnapshot.getKey();
                            MoimDTO moimDTO = postSnapshot.getValue(MoimDTO.class);
                            moimDTO.number = key;
                            moimDTOs.add(moimDTO);
                        }
                    }
                }
                mAdapter = new HomeListAdapter(moimDTOs);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(postListener);
    }
}