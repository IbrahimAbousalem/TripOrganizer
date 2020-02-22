package com.iti.mobile.triporganizer.details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;

import java.util.HashSet;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesHolder> {
    private Context context;
    private List<String> notesList;
    private HashSet<String> selectedNotesList;
    private static final String TAG = "NoteAdapter";

    public NoteAdapter(Context context, List<String> notesList) {
        this.context = context;
        this.notesList = notesList;
        selectedNotesList = new HashSet<>();
    }

    public HashSet<String> getSelectedNotesList() {
        return selectedNotesList;
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
        holder.checkBox.setText(notesList.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedNotesList.add(notesList.get(position));
                } else {
                    selectedNotesList.remove(notesList.get(position));
                }
                Log.i(TAG, "..............................SelectedNoteListSize....................... " + getSelectedNotesList().size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public NotesHolder(View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.checkBox_Notes);
        }
    }
}
