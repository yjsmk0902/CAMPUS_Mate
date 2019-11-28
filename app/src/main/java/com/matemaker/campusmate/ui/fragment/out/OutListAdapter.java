package com.matemaker.campusmate.ui.fragment.out;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.OutActivityDTO;

import java.util.List;

public class OutListAdapter extends RecyclerView.Adapter<OutListAdapter.ViewHolder> {
    List<OutActivityDTO> outActivityList;
    Context context;


    public OutListAdapter(List<OutActivityDTO> outActivityList, Context context){
        this.outActivityList = outActivityList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_out_list,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(outActivityList.get(position).getProfile())
                .into(holder.profile);

        holder.title.setText(outActivityList.get(position).getTitle());
        holder.date.setText(outActivityList.get(position).getDate());
        holder.apply_date.setText(outActivityList.get(position).getApply_date());
    }

    @Override
    public int getItemCount() {

        return outActivityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView title;
        TextView date;
        TextView apply_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.profile);
            this.title= itemView.findViewById(R.id.title);
            this.date= itemView.findViewById(R.id.date);
            this.apply_date= itemView.findViewById(R.id.apply_date);
        }
    }
}