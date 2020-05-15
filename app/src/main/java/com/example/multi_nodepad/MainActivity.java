package com.example.multi_nodepad;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {
    ActionBar actionBar;
    private static final String TAG = "Main Activity";

    private static final int Edit_Request_Code = 1;
    String title_note, text_note;
    int position;
    Boolean flag;
    
    private final List<Notes> All_Notes = new ArrayList<>();
    private RecyclerView notesRecycler;
    private NotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2a03a6")));

        new NoteAsyncTask(this).execute(this);
        Log.d(TAG, "onCreate: Async Task is Done");
        //loadFile();
        notesRecycler = findViewById(R.id.main_recycleview);
        notesAdapter = new NotesAdapter(All_Notes, this);
        notesRecycler.setAdapter(notesAdapter);
        notesRecycler.setLayoutManager(new LinearLayoutManager(this));
        setTitle("Multi Notes (" + All_Notes.size() + ")");
    }

    public void getResult(List<Notes> list){
        All_Notes.addAll(list);
        setTitle("Multi Notes (" + All_Notes.size() + ")");
        notesAdapter.notifyDataSetChanged();

        //notesRecycler = findViewById(R.id.main_recycleview);
        //notesAdapter = new NotesAdapter(All_Notes, this);
        //notesRecycler.setAdapter(notesAdapter);
        //notesRecycler.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "getResult: " );

    }

    //Calling Menu Layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    //This Method tell Application what to do, when particular menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_notes:
                Log.d(TAG, "onOptionsItemSelected: Add_notes");
                Intent new_intent = new Intent(MainActivity.this, EditNotesActivity.class);
                startActivityForResult(new_intent, Edit_Request_Code);
                return true;
            case R.id.app_info:
                Log.d(TAG, "onOptionsItemSelected: App_Info");
                Intent info_intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(info_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        setTitle("Multi Notes (" + All_Notes.size() + ")");
        notesAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            saveNotes();
        }catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

/*
    private void loadFile() {
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                Notes note = new Notes();
                while (reader.hasNext()) {
                    String titleString1 = reader.nextName();
                    switch (titleString1) {
                        case "note_title":
                            note.setTitle(reader.nextString());
                            break;
                        case "date":
                            note.setDate(reader.nextString());
                            break;
                        case "note_Text":
                            note.setDescription(reader.nextString());
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                All_Notes.add(note);
            }
            reader.endArray();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    private void saveNotes() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos,getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();
            for (int i = 0; i < All_Notes.size(); i++) {
                writer.beginObject();
                writer.name("note_title").value(All_Notes.get(i).getTitle());
                //Log.d(TAG, "saveNotes: "+writer);
                writer.name("date").value(All_Notes.get(i).getDate());
                writer.name("note_Text").value(All_Notes.get(i).getDescription());
                writer.endObject();
            }
            Log.d(TAG, "saveNotes: " +writer);
            writer.endArray();
            writer.close();
            Log.d(TAG, "saveNotes: SavedNotes");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    //By CLicking on Particular notes, This Method will Redirect you to Edit Activity with data
    @Override
    public void onClick(View view1) {
        int position = notesRecycler.getChildLayoutPosition(view1);
        Notes note1 = All_Notes.get(position);
        Log.d(TAG, "onClick: hi1");
        Intent intent = new Intent(MainActivity.this, EditNotesActivity.class);
        intent.putExtra("click_note_title", note1.getTitle());
        intent.putExtra("click_note_text", note1.getDescription());
        intent.putExtra("click_Position", position);
        intent.putExtra("click_old_flag", true);
        startActivityForResult(intent, Edit_Request_Code);
        Log.d(TAG, "onClick: hi2");
    }

    @Override
    public boolean onLongClick(View view1) {
        final int position = notesRecycler.getChildLayoutPosition(view1);
        Notes note1 = All_Notes.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note '"  + All_Notes.get(position).getTitle().toString() + "'?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id1) {
                All_Notes.remove(position);
                setTitle("Multi Notes (" + All_Notes.size() + ")");
                notesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id1) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == Edit_Request_Code) {
            //Log.d(TAG, "onActivityResult: if1");
            if (resultCode == RESULT_OK) {
                //Log.d(TAG, "onActivityResult: if2");
                title_note = data.getStringExtra("edit_note_Title");
                text_note = data.getStringExtra("edit_note_Text");
                position = data.getIntExtra("edit_Position", -1);
                flag = data.getBooleanExtra("edit_Flag", false);
                Log.d(TAG, "onActivityResult: " + title_note);
                //Log.d(TAG, "onActivityResult: " + text_note);
                //Log.d(TAG, "onActivityResult: Position: " + position);
                //Log.d(TAG, "onActivityResult: Position: " + position);
                SimpleDateFormat date_format = new SimpleDateFormat("EEE MMM d, hh:mm a");
                String date_format1 = date_format.format(new Date());
                Log.d(TAG, "onActivityResult: Text: " +text_note);
                Log.d(TAG, "onActivityResult: Position:"+position);
                Log.d(TAG, "onActivityResult: Flag:"+flag);
                if(position == -1 && flag == false && !title_note.isEmpty()){
                        Log.d(TAG, "onActivityResult: Saurabh");

                        All_Notes.add(0, new Notes(title_note, date_format1, text_note));

                        Log.d(TAG, "onActivityResult: New data" +All_Notes.get(0));

                        notesAdapter.notifyDataSetChanged();
                        setTitle("Multi Notes (" + All_Notes.size() + ")");
                    }
                    else if(position !=-1 && flag != true) {
                        if (title_note.isEmpty()) {
                            All_Notes.remove(position);
                            notesAdapter.notifyDataSetChanged();
                            setTitle("Multi Notes (" + All_Notes.size() + ")");
                        }
                        else {
                            Log.d(TAG, "onActivityResult: Already changing");
                        All_Notes.remove(position);
                        All_Notes.add(0, new Notes(title_note, date_format1, text_note));
                        notesAdapter.notifyDataSetChanged();
                            setTitle("Multi Notes (" + All_Notes.size() + ")");
                        }
                    }
            }
                else{
                    Log.d(TAG, "onActivityResult: hi data result");
                    }
            }
            else {
                Log.d(TAG, "onActivityResult:  hi data1 result");
            }
        }
}
