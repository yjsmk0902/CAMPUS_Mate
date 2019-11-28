package com.matemaker.campusmate.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.MoimDTO;
import com.matemaker.campusmate.database.UserDTO;
import com.matemaker.campusmate.ui.activity.MainActivity;

import java.util.ArrayList;


class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {
    ArrayList<MoimDTO> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView title;
        public Button join;
        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.text_image_item);
            join = v.findViewById(R.id.button_moim_join);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeListAdapter(ArrayList<MoimDTO> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int finalPosition = position;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(v.getContext(), MoimBoardActivity.class);
                intent.putExtra("title",mDataset.get(position).title);
                intent.putExtra("uid",mDataset.get(position).uid);
                intent.putExtra("subtitle",mDataset.get(position).subtitle);
                intent.putExtra("number",mDataset.get(position).number);
                v.getContext().startActivity(intent);*/
            }
        });

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.uid.equals(mDataset.get(position).uid)){

                    getWaitList(position);

                    Toast.makeText(v.getContext(), "this is Manager", Toast.LENGTH_SHORT).show();
                    return;
                }
                applyMoim(position);
            }
        });

        holder.title.setText(mDataset.get(position).title);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void applyMoim(final int position){
        String uid = MainActivity.uid;
        if(uid == null){
            //login error
        }

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(uid);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getKey();
                FirebaseDatabase.getInstance()
                        .getReference("moim")
                        .child(mDataset.get(position).number)
                        .child("wait")
                        .child(user)
                        .setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userRef.addValueEventListener(postListener);
    }

    public void getWaitList(final int position){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("moim").child(mDataset.get(position).number).child("wait");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey() == null) return;
                    DatabaseReference userRef = ref.child("user").child(data.getKey());
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserDTO user = dataSnapshot.getValue(UserDTO.class);
                            System.out.println(user.name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    userRef.addValueEventListener(postListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(postListener);
    }
}