package com.example.multi_nodepad;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NoteAsyncTask extends AsyncTask <MainActivity, Void, List<Notes>> {
    private static final String TAG = "NoteAsyncTask";
    public List<Notes> All_Notes = new ArrayList<>();
    public MainActivity mainActivity;
    public static boolean flag = false;

    public NoteAsyncTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(List<Notes> All_Notes) {

        Log.d(TAG, "onPostExecute: HI i am in POst Execute");
        super.onPostExecute(All_Notes);
        flag = false;
        mainActivity.getResult(All_Notes);

    }

    @Override
    protected List<Notes> doInBackground(MainActivity... mainActivities) {
        Log.d(TAG, "doInBackground: I am in Async Task");
        try {
            //mainActivity = mainActivities[0];
            InputStream is1 = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is1, mainActivity.getString(R.string.encoding)));
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
                //return async_notes;
            }
            reader.endArray();
        } catch (FileNotFoundException e) {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "loadFile: return " + All_Notes);
        return All_Notes;
    }
        /*
            Log.d(TAG, "doInBackground: I am with data" + All_Notes);
        } catch (FileNotFoundException e) {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        }
        //String result;
        //if(strings.length < 2){
        //  result = "There are less names";
        //}
        //result = "Hello " +strings[0]+ " " +strings[1];
        return All_Notes;*/
/*
    public List<Notes> loadFile(InputStream is1) {
        Log.d(TAG, "loadFile: I am in JSON load file ");
        List<Notes> async_notes = new ArrayList<>();
        try {

            JsonReader reader = new JsonReader(new InputStreamReader(is1, mainActivity.getString(R.string.encoding)));
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
                //return async_notes;
            }
            reader.endArray();
        } catch (FileNotFoundException e) {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "loadFile: return "+All_Notes.get(0));
        return All_Notes;
    }
    */
}
