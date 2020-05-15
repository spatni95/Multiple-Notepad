package com.example.multi_nodepad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesHolder> {
    private static final String TAG = "Adapter ";

    private List<Notes> All_Notes;
    private MainActivity mainAct;

    public NotesAdapter(List<Notes> All_Notes, MainActivity main) {
        this.All_Notes = All_Notes;
        mainAct = main;
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup notesHolder, int i) {
        View itemView = LayoutInflater.from(notesHolder.getContext())
                .inflate(R.layout.notes_view, notesHolder, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new NotesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder notesHolder, int i) {
        Notes noteItem = All_Notes.get(i);
        Log.d(TAG, "onBindViewHolder: " +noteItem.getTitle());
        String title1 = noteItem.getTitle();
        if(title1.length() > 80)
        {
            notesHolder.title_holder.setText(title1.substring(0,79) + "...");
        }
        else {
            notesHolder.title_holder.setText(title1);
        }
        notesHolder.date_holder.setText(noteItem.getDate());
        String description1 = noteItem.getDescription();
        //Length Recycle View Constraint
        if(description1.length() > 80)
            notesHolder.description_holder.setText(description1.substring(0, 79) + "...");
        else {
            notesHolder.description_holder.setText(description1);
        }
    }

    @Override
    public int getItemCount() {
        return All_Notes.size();
    }
}

