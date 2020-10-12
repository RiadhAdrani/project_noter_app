package com.example.noter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.myViewHolder> {

    ArrayList<Note> mNoteList;

    public static class myViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mContentPreview;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.card_icon);
            mTitle = itemView.findViewById(R.id.card_title);
            mContentPreview = itemView.findViewById(R.id.card_preview);
        }
    }

    public NoteAdapter(ArrayList<Note> noteList){
        mNoteList = noteList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout,parent,false);
        myViewHolder mvh = new myViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Note currentNote = mNoteList.get(position);
        holder.mImageView.setImageResource(currentNote.nPicture);
        holder.mTitle.setText(currentNote.title);
        holder.mContentPreview.setText(currentNote.content);
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }


}
