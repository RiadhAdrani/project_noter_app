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
    OnItemClickListener listener;

    public IconAdapter(ArrayList<Icon> mList) {
        this.mList = mList;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        public ImageView mIcon;
        public TextView mName;

        public myViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.icon_frame);
            mName = itemView.findViewById(R.id.icon_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClickListener(position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public IconAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_layout,parent,false);
        // IconAdapter.myViewHolder mvh = new IconAdapter.myViewHolder(v);
        return new IconAdapter.myViewHolder(v,listener);
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
