package com.matemaker.campusmate.ui.fragment.manage;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;


import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.MoimDTO;
import com.matemaker.campusmate.ui.activity.moimboard.MoimBoardActivity;

import java.util.ArrayList;

class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.MyViewHolder> {
    ArrayList<MoimDTO> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView title;
        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title_manage);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ManageListAdapter(ArrayList<MoimDTO> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ManageListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_list, parent, false);

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
                Intent intent = new Intent(v.getContext(), MoimBoardActivity.class);
                intent.putExtra("title",mDataset.get(position).title);
                intent.putExtra("uid",mDataset.get(position).uid);
                intent.putExtra("subtitle",mDataset.get(position).subtitle);
                intent.putExtra("number",mDataset.get(position).number);
                v.getContext().startActivity(intent);
            }
        });
        holder.title.setText(mDataset.get(position).title);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}