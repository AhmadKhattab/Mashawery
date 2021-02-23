package com.iti.gov.mashawery.trip.create.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.NoteItemBinding;
import com.iti.gov.mashawery.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private Context context;
    private NoteListenerInterface noteListenerInterface;

    {
        noteList = new ArrayList<>();
    }


    public NotesAdapter() {
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public void setNoteListenerInterface(NoteListenerInterface noteListenerInterface) {
        this.noteListenerInterface = noteListenerInterface;

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.binding.tvNoteTitle.setText(note.getTitle());
        holder.binding.noteConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListenerInterface.onNoteClicked(note);
            }
        });
        holder.binding.ivRemoveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListenerInterface.onNoteDeleted(note);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (noteList == null)? 0 : noteList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        public NoteItemBinding binding;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NoteItemBinding.bind(itemView);
        }
    }
}
