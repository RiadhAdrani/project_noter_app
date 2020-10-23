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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.myViewHolder> {

    // Note adapter is a custom created Adapter RecyclerView
    // Used to display notes dynamically

    // Reference to the list that will be fed to the adapter
    // to be displayed
    ArrayList<Note> mNoteList;

    // Contains overridable functions
    // mainly onClickListeners
    // could be implemented in an Activity
    OnItemClickListener mListener;

    // Context of the Activity
    // in which the adapter is used.
    Context mContext;

    public interface OnItemClickListener {
        // Interface containing (Hollow) overridable functions

        void onItemClick(int position);
        void onOptionClick(int position);
        void onDuplicateClick(int position);
        void onCopyContentClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        // Function that allow the use of the interface

        mListener = listener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        // ViewHolder of every element
        // Actually prepare the template of the element

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mCategory;
        public TextView mContentPreview;
        public ImageView mOptions;
        public PopupMenu mPopup;

        public myViewHolder(@NonNull View itemView, final OnItemClickListener listener, final Context mContext) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.card_icon);
            mTitle = itemView.findViewById(R.id.card_title);
            mCategory = itemView.findViewById(R.id.card_category);
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
        // NoteAdapter constructor

        mNoteList = noteList;
        mContext = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate and load the layout of the element

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout,parent,false);
        myViewHolder mvh = new myViewHolder(v, mListener,mContext);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // Assign information to the existing View elements
        // to be displayed

        Note currentNote = mNoteList.get(position);
        holder.mImageView.setImageResource(new MyResources(mContext).GET_ICON(currentNote.iconUID).id);
        holder.mTitle.setText(currentNote.title);

        if (currentNote.category == null){
            currentNote.category = new Category("something");
        }

        holder.mCategory.setText(currentNote.category.UID);
        holder.mContentPreview.setText(currentNote.content);
    }

    @Override
    public int getItemCount() {
        // Return the size of the List
        // needed for the RecyclerView

        return mNoteList.size();
    }


}
