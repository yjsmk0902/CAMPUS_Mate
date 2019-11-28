package com.matemaker.campusmate.ui.fragment.out;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.matemaker.campusmate.database.OutActivityDTO;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class OutFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<OutActivityDTO> outActivityList;
    FirebaseDatabase firebaseDatabase;
    RecyclerView.Adapter adaptor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_out, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        outActivityList = new ArrayList<>();

        adaptor = new OutListAdapter(outActivityList, getActivity());

        recyclerView.setAdapter(adaptor);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("OutActivity");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                Log.d(TAG, "Value is:" + value);
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    String value2 = dataSnapshot2.getValue().toString();
                    Log.d(TAG, "Value is:" + value2);
                    OutActivityDTO out = dataSnapshot2.getValue(OutActivityDTO.class);

                    outActivityList.add(out);
                    adaptor.notifyItemInserted(outActivityList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "FAILED", databaseError.toException());
            }
        });
        return v;
    }
}