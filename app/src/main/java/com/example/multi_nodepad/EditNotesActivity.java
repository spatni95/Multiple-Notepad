package com.example.multi_nodepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

public class EditNotesActivity extends AppCompatActivity {
    ActionBar actionBar;
    private static final String TAG = "EditNotesActivity";
    private EditText note_Text;
    private EditText note_title;
    private Notes notes;
    String title_note, text_note;
    private int position = -1;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2a03a6")));
        note_title = (EditText) findViewById(R.id.node_title);
        note_Text = (EditText) findViewById(R.id.note_text);
        Log.d(TAG, "onCreate: hi");
        note_Text.setMovementMethod(new ScrollingMovementMethod());
        Intent fetch_intent = getIntent();
        if (fetch_intent.hasExtra("click_note_title")) {
            title_note = fetch_intent.getStringExtra("click_note_title");
            text_note = fetch_intent.getStringExtra("click_note_text");
            position = fetch_intent.getIntExtra("click_Position", -1);
            Log.d(TAG, "onCreate: Position: "+position);
            flag = fetch_intent.getBooleanExtra("click_old_flag", false);
            Log.d(TAG, "onCreate: "+flag);
            note_title.setText(title_note);
            note_Text.setText(text_note);
        }
        //Log.d(TAG, "onCreate: outside if");
    }

    //Fetching the data
    public void FetchData() {
        Log.d(TAG, "FetchData: Edit Note");
        Intent d_intent = new Intent();

        Log.d(TAG, "FetchData: NOTE_TITLE =  " +note_title.getText().toString());

        d_intent.putExtra("edit_note_Title", note_title.getText().toString());


        d_intent.putExtra("edit_note_Text", note_Text.getText().toString());
        d_intent.putExtra("edit_Position", position);
        d_intent.getBooleanExtra("edit_Flag", flag);
        setResult(RESULT_OK, d_intent);
        finish();
    }


    //Calling Menu Layout
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                if (note_title.getText().toString().equals(title_note) && note_Text.getText().toString().equals(text_note) && flag == true) {
                    Log.d(TAG, "onOptionsItemSelected: Both New Edition: " +flag);
                    //Intent f_data = new Intent();
                    //setResult(RESULT_OK, f_data);
                    finish();
                } else if (note_title.getText().toString().isEmpty() && !note_Text.getText().toString().isEmpty()) {
                    Log.d(TAG, "onOptionsItemSelected: Only title is empty: " +flag);
                    Toast.makeText(this, "There is no Title, Cannot be saved", Toast.LENGTH_SHORT).show();
                    //Intent f_data = new Intent();
                   // setResult(RESULT_OK, f_data);
                    finish();
                } else if (note_title.getText().toString().isEmpty() && note_Text.getText().toString().isEmpty()) {
                    Log.d(TAG, "onOptionsItemSelected: both are empty: " +flag);
                    //FetchData();
                    Toast.makeText(this, "There is no Title, Cannot be saved", Toast.LENGTH_SHORT).show();
                    //Intent f_data = new Intent();
                    //setResult(RESULT_OK, f_data);
                    finish();
                }
                else if(!note_title.getText().toString().equals(title_note) || !note_Text.getText().toString().equals(text_note)) {
                    Log.d(TAG, "onOptionsItemSelected: if anyone is changed: " +flag);
                    FetchData();
                }
                else{
                    Log.d(TAG, "onOptionsItemSelected: New Data Save");
                    FetchData();
                }
                default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
        public void onBackPressed () {
            Log.d(TAG, "onBackPressed: flag: " +flag);
            Log.d(TAG, "onBackPressed: Positon: " +position);
            if (note_title.getText().toString().equals(title_note) && note_Text.getText().toString().equals(text_note)) {
                //FetchData();
                finish();
            } else if (note_title.getText().toString().isEmpty() && !note_Text.getText().toString().isEmpty()) {
                Toast.makeText(this, "There is no Title, Cannot be saved", Toast.LENGTH_SHORT).show();
                finish();
                //FetchData();
            } else if (note_title.getText().toString().isEmpty() && note_Text.getText().toString().isEmpty()) {
                Toast.makeText(this, "There is no Title, Cannot be saved", Toast.LENGTH_SHORT).show();
                finish();
                //FetchData();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your note is not saved!");
                builder.setMessage("Save note\"" + note_title.getText().toString() + "\"?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id1) {
                        FetchData();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id1) {
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

