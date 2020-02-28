package com.iti.mobile.triporganizer.details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Note;

import java.util.HashSet;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesHolder> {

    private onRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View v,int position);
    }

    private Context context;
    private List<Note> notesList;

    private static final String TAG = "NoteAdapter";

    public NoteAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    public void setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener=onRecyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.note_item, parent, false);
        NotesHolder notesHolder = new NotesHolder(row);
        return notesHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        holder.noteName.setText(notesList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView noteName;
        ImageView deleteNoteImageView;

        public NotesHolder(View itemView) {
            super(itemView);
            noteName=itemView.findViewById(R.id.noteName);
            deleteNoteImageView=itemView.findViewById(R.id.deleteNoteImgView);
            deleteNoteImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onRecyclerViewItemClickListener!=null){
                onRecyclerViewItemClickListener.onItemClickListener(v,getAdapterPosition());
            }
        }
    }
}
