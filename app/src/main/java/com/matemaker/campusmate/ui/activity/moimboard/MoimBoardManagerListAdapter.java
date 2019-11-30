package com.matemaker.campusmate.ui.activity.moimboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.UserDTO;

import java.util.ArrayList;

public class MoimBoardManagerListAdapter extends RecyclerView.Adapter<MoimBoardManagerListAdapter.MyViewHolder>{
    ArrayList<String> mDataset;
    MoimBoardManageActivity mActivity;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView name;
        public Button ok,cancel;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            name = v.findViewById(R.id.text_item_boad_manage_name);
            ok = v.findViewById(R.id.button_item_boad_manage_ok);
            cancel = v.findViewById(R.id.button_item_boad_manage_cancel);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoimBoardManagerListAdapter(MoimBoardManageActivity activity, ArrayList<String> myDataset) {
        mActivity = activity;
        mDataset = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoimBoardManagerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_moim_board_manage_list, parent, false);

        MoimBoardManagerListAdapter.MyViewHolder vh = new MoimBoardManagerListAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MoimBoardManagerListAdapter.MyViewHolder holder, final int position) {
        FirebaseDatabase.getInstance().getReference("user").child(mDataset.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDTO user = dataSnapshot.getValue(UserDTO.class);
                setUIWithUser(holder,user,position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void setUIWithUser(final MoimBoardManagerListAdapter.MyViewHolder holder, UserDTO user, final int position){
        //TODO 승인 과정에서 쓸 유저의 정보 여기서 ui 설정 해주면 됨
        holder.name.setText(user.name);
        holder.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("moim")
                        .child(mActivity.number).child("member").child(mDataset.get(position)).setValue("일반회원");
                FirebaseDatabase.getInstance().getReference("moim")
                        .child(mActivity.number).child("wait").child(mDataset.get(position)).removeValue();
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("moim")
                        .child(mActivity.number).child("wait").child(mDataset.get(position)).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
