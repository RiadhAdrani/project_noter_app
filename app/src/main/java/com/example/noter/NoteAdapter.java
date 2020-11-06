package com.example.noter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.myViewHolder> implements Filterable {

    // Note adapter is a custom created Adapter RecyclerView
    // Used to display notes dynamically

    // Reference to the list that will be fed to the adapter
    // to be displayed
    ArrayList<Note> mNoteList;

    ArrayList<Note> mNoteListFull;

    // Contains overridable functions
    // mainly onClickListeners
    // could be implemented in an Activity
    OnItemClickListener mListener;

    // Context of the Activity
    // in which the adapter is used.
    Context mContext;

    public NoteAdapter(ArrayList<Note> noteList,Context context){
        // NoteAdapter constructor

        mNoteList = noteList;
        mContext = context;
        mNoteListFull = new ArrayList<>(mNoteList);
    }



    public interface OnItemClickListener {
        // Interface containing (Hollow) overridable functions

        void onItemClick(int position);
        void onOptionClick(int position);
        void onDuplicateClick(int position);
        void onCopyContentClick(int position);
        void onDeleteClick(int position);
        void onLongClickListener(int position);
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
        public LinearLayout mCardInfo;
        public ImageView mCheckListIcon;
        public TextView mCheckListCount;

        public myViewHolder(@NonNull View itemView, final OnItemClickListener listener, final Context mContext, final NoteAdapter adapter) {
            super(itemView);

            // initializing views
            mImageView = itemView.findViewById(R.id.card_icon);
            mTitle = itemView.findViewById(R.id.card_title);
            mCategory = itemView.findViewById(R.id.card_category);
            mContentPreview = itemView.findViewById(R.id.card_preview);
            mOptions = itemView.findViewById(R.id.card_options);
            mCheckListIcon = itemView.findViewById(R.id.card_check_list_icon);
            mCheckListCount = itemView.findViewById(R.id.card_check_list_count);
            mCardInfo = itemView.findViewById(R.id.card_addition_preview);

            // create a popup menu
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    // Allow for multiple notes selection
                    listener.onLongClickListener(getAdapterPosition());

                    return true;
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

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate and load the layout of the element

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout,parent,false);
        return new myViewHolder(v, mListener,mContext,this);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // Assign information to the existing View elements
        // to be displayed

        MyResources myResources = new MyResources(mContext);

        Note currentNote = mNoteList.get(position);
        holder.mImageView.setImageResource(new MyResources(mContext).GET_ICON(currentNote.iconUID).id);
        holder.mTitle.setText(currentNote.title);

        // if for some reason the note does not have any category UID assigned to it,
        // set the UID to the DEFAULT_CATEGORY_UID
        if (currentNote.category == null){
            currentNote.category = MyResources.DEFAULT_CATEGORY.UID;
        }

        holder.mCategory.setText(myResources.GET_CATEGORY(currentNote.category).name);
        holder.mContentPreview.setText(currentNote.content);

        // if there is no additional information to show
        // set section visibility to GONE
        if (currentNote.checkList.isEmpty()){
            holder.mCardInfo.setVisibility(View.GONE);
        }
        // else there is information to display
        else {

            // checkList information
            // if empty hide, else display
            if (currentNote.checkList.isEmpty()){
                holder.mCheckListIcon.setVisibility(View.INVISIBLE);
                holder.mCheckListCount.setVisibility(View.INVISIBLE);
            }
            else {
                String checkListCountText = currentNote.getDoneCount()+"/"+currentNote.checkList.size();
                holder.mCheckListCount.setText(checkListCountText);
            }

        }



    }

    @Override
    public int getItemCount() {
        // Return the size of the List
        // needed for the RecyclerView

        return mNoteList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mNoteListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Note item : mNoteListFull) {
                    if (item.title.toLowerCase().contains(filterPattern)
                     || item.content.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults result = new FilterResults();
            result.values = filteredList;

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults result) {
            mNoteList.clear();
            mNoteList.addAll((List) result.values);
            notifyDataSetChanged();
        }
    };


}
