package com.example.noter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.myViewHolder> {

    ArrayList<Note> mNoteList;
    OnItemClickListener mListener;
    Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onOptionClick(int position);
        void onDuplicateClick(int position);
        void onCopyContentClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mContentPreview;
        public ImageView mOptions;
        public PopupMenu mPopup;

        public myViewHolder(@NonNull View itemView, final OnItemClickListener listener, final Context mContext) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.card_icon);
            mTitle = itemView.findViewById(R.id.card_title);
            mContentPreview = itemView.findViewById(R.id.card_preview);
            mOptions = itemView.findViewById(R.id.card_options);

            mPopup = new PopupMenu(mContext,mOptions);
            mPopup.inflate(R.menu.card_option_menu);
            mPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int position = getAdapterPosition();
                    switch (menuItem.getItemId()){
                        case R.id.card_option_duplicate: listener.onDuplicateClick(position);break;
                        case R.id.card_option_copy_content: listener.onCopyContentClick(position);break;
                        case R.id.card_option_delete: listener.onDeleteClick(position);break;
                    }
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopup.show();
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onOptionClick(position);
                        }
                    }
                }
            });
        }

    }

    public NoteAdapter(ArrayList<Note> noteList,Context context){
        mNoteList = noteList;
        mContext = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout,parent,false);
        myViewHolder mvh = new myViewHolder(v, mListener,mContext);
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
