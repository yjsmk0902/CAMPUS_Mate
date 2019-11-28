package com.matemaker.campusmate.ui.activity.moimboard;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.BoardDTO;

import java.util.ArrayList;

class MoimBoardListAdapter extends RecyclerView.Adapter<MoimBoardListAdapter.MyViewHolder> {
    ArrayList<BoardDTO> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView title;
        public TextView contents;
        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.text_board_item_title);
            contents = v.findViewById(R.id.text_board_item_contents);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoimBoardListAdapter(ArrayList<BoardDTO> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoimBoardListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_moim_board_list, parent, false);

        MoimBoardListAdapter.MyViewHolder vh = new MoimBoardListAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MoimBoardListAdapter.MyViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),mDataset[finalPosition],Toast.LENGTH_SHORT).show();
            }
        });
        holder.title.setText(mDataset.get(position).title);
        holder.contents.setText(mDataset.get(position).contents);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}