package com.matemaker.campusmate.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText("모임피드");

        initList();
        return root;
    }

    public void initList(){

        recyclerView = root.findViewById(R.id.home_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        //나중에 정보 따로 저장해서 아래에 넣기

        // specify an adapter (see also next example)

        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("moim");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MoimDTO> moimDTOs = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    MoimDTO moimDTO = postSnapshot.getValue(MoimDTO.class);
                    moimDTO.number = key;
                    moimDTOs.add(moimDTO);
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