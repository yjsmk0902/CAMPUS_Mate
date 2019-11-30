package com.matemaker.campusmate.ui.fragment.home;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.MoimDTO;
import com.matemaker.campusmate.database.UserDTO;
import com.matemaker.campusmate.ui.activity.MainActivity;
import com.matemaker.campusmate.ui.activity.moimboard.MoimBoardActivity;

import java.util.ArrayList;


class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {
    ArrayList<MoimDTO> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView title;
        public Button join;
        public ImageView imageView;
        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.text_image_item);
            join = v.findViewById(R.id.button_moim_join);
            imageView = v.findViewById(R.id.image_list_item);
            view = v;
        }
    }

    public HomeListAdapter(ArrayList<MoimDTO> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public HomeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if(!mDataset.get(position).image.isEmpty()) {
            final StorageReference ref = FirebaseStorage.getInstance()
                    .getReferenceFromUrl("https://firebasestorage.googleapis.com" + mDataset.get(position).image);
            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(holder.view.getContext())
                            .load(task.getResult())
                            .into(holder.imageView);
                }
            });
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.uid == null){
                    Toast.makeText(v.getContext(), "아이디를 먼저 생성 해 주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(v.getContext(), MoimBoardActivity.class);
                intent.putExtra("title",mDataset.get(position).title);
                intent.putExtra("uid",mDataset.get(position).uid);
                intent.putExtra("subtitle",mDataset.get(position).subtitle);
                intent.putExtra("number",mDataset.get(position).number);
                v.getContext().startActivity(intent);
            }
        });

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.uid == null){
                    Toast.makeText(v.getContext(), "아이디를 먼저 생성 해 주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(MainActivity.uid.equals(mDataset.get(position).uid)){
                    //getWaitList(position);

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