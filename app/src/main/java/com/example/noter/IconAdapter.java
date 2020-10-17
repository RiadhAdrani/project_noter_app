package com.example.noter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.myViewHolder> {

    ArrayList<Icon> mList;

    public IconAdapter(ArrayList<Icon> mList) {
        this.mList = mList;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        public ImageView mIcon;
        public TextView mName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.icon_frame);
            mName = itemView.findViewById(R.id.icon_name);
        }
    }


    @NonNull
    @Override
    public IconAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_layout,parent,false);
        // IconAdapter.myViewHolder mvh = new IconAdapter.myViewHolder(v);
        return new IconAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IconAdapter.myViewHolder holder, int position) {
        Icon currentIcon = mList.get(position);
        holder.mIcon.setImageResource(currentIcon.id);
        holder.mName.setText(currentIcon.name);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
