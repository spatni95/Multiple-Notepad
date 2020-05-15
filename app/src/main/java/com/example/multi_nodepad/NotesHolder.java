package com.example.multi_nodepad;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NotesHolder extends RecyclerView.ViewHolder {

    public TextView title_holder;
    public TextView date_holder;
    public TextView description_holder;

    public NotesHolder (View view) {
        super(view);
        title_holder = itemView.findViewById(R.id.title);
        date_holder = itemView.findViewById(R.id.date);
        description_holder = itemView.findViewById(R.id.note_description);
    }

}
