package com.iti.gov.mashawery.helpPackag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.model.Note;

import java.util.List;

public class FloatingNoteAdapter extends RecyclerView.Adapter<com.iti.gov.mashawery.helpPackag.FloatingNoteAdapter.FloatingNoteViewHolder>{
    Context context;
    private List<Note> notes ;
    public FloatingNoteAdapter(Context context, List<Note> notes) {
        this.context=context;
        this.notes= notes;
    }
    @NonNull
    @Override
    public  com.iti.gov.mashawery.helpPackag.FloatingNoteAdapter.FloatingNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v= inflater.inflate(R.layout.item_floating_note,parent,false);
        com.iti.gov.mashawery.helpPackag.FloatingNoteAdapter.FloatingNoteViewHolder viewHolder =  new com.iti.gov.mashawery.helpPackag.FloatingNoteAdapter.FloatingNoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.iti.gov.mashawery.helpPackag.FloatingNoteAdapter.FloatingNoteViewHolder holder, int position) {
        holder.addNote.setText(notes.get(position).getTitle());
    }
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (notes.size() != 0)
            return notes.size();
        else
            return 0;
    }

    public class FloatingNoteViewHolder extends RecyclerView.ViewHolder {
        public TextView addNote ;
        public FloatingNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            addNote = itemView.findViewById(R.id.txt_floating_note);

        }
    }

}
