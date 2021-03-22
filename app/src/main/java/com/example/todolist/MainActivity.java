/* Name: Anh Nguyen
   Date: March 21st, 2021
   Purpose: This is a To Do List application. This list is a numbered sequence. When user types a
   task into EditText widget and clicks "ADD", the task will be added into the list. New task is
   always added to the end of the list. If users clicks on a task on the to do list, the task will
   be displayed on EditText widget. User can either press "DELETE" to remove this task from the to
   do list or modify the task and press "UPDATE" to put the modified task back into the list. Also,
   there are "SAVE" and "CLOSE" buttons. "SAVE" button will write the list to storage while "CLOSE"
   save the list & close the application.
   Note: The 'list.txt' text file will be founded under data --> data --> com.example.todolist --> files

 */


package com.example.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnInitListener {
    private EditText toDo; //Allow user to type a task
    private ArrayList<String> list_string = new ArrayList<String>(); //store a list of tasks that user writes down
    private ListView listview;
    private String thingToFix; //temporary store the focused task (task currently chosen by user)
    private int place; //index of thingToFix in the ArrayList

    private final String file = "list.txt"; //text file that stores the to do list
    private OutputStreamWriter out; //output stream
    private TextToSpeech speaker;
    private static final String tag = "Widgets";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide title and icon in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);

        toDo = (EditText) findViewById(R.id.toDo);
        toDo.setHint("Enter text here...");

        listview = (ListView)findViewById(R.id.list);
        listview.setOnItemClickListener(this);   //connect listener

        //initialize Text to Speech engine (context, listener object)
        speaker = new TextToSpeech(this, this);

        //check if 'list.txt' exists. If it does, initialize the app with file contents
        try {
            InputStream in = openFileInput(file);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isr);
            String str = null;

            while ((str = reader.readLine()) != null) {
                list_string.add(str); //add to the list of tasks
            }
            CustomAdapter customAdapter = new CustomAdapter(this, list_string);
            listview.setAdapter(customAdapter);
            reader.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e3) {

        }
    }

    //speak methods will send text to be spoken
    public void speak(String output){
        	speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);  //for APIs before 21
        //speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    public void onInit(int status) {
        //status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            //set preferred language to US english.
            //if a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);

            //int result = speaker.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                //the TTS engine has been successfully initialized
                speak("Welcome to the To Do List app");
                Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            //initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }

    public void onDestroy(){
        //shut down TTS engine
        if(speaker != null){
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Add:
                String S_toDo = toDo.getText().toString();
                list_string.add(S_toDo); //add the task to ArrayList

                CustomAdapter customAdapter = new CustomAdapter(this, list_string);
                listview.setAdapter(customAdapter);
                toDo.setText(""); //clear EditText widget

                //if speaker is talking, stop it
                if(speaker.isSpeaking()){
                    Log.i(tag, "Speaker Speaking");
                    speaker.stop();
                    //else start speech
                } else {
                    Log.i(tag, "Speaker Not Already Speaking");
                    speak(S_toDo + " is added to the list");
                }

                return true;

            case R.id.Delete:
                String temp = list_string.get(place);
                list_string.remove(place); //remove the task at the specific index in ArrayList

                customAdapter = new CustomAdapter(this, list_string);
                listview.setAdapter(customAdapter);
                toDo.setText(""); //clear EditText widget

                //if speaker is talking, stop it
                if(speaker.isSpeaking()){
                    Log.i(tag, "Speaker Speaking");
                    speaker.stop();
                    //else start speech
                } else {
                    Log.i(tag, "Speaker Not Already Speaking");
                    speak(temp + " is deleted from the list");
                }

                return true;

            case R.id.Update:
                String S_toUpdate = toDo.getText().toString(); //get the modified task
                list_string.set(place, S_toUpdate); //update ArrayList with modified task

                customAdapter = new CustomAdapter(this, list_string);
                listview.setAdapter(customAdapter);
                toDo.setText(""); //clear EditText widget

                return true;

            case R.id.Save:
                save_list();
                return true;

            case R.id.Exit:
                save_list();
                try {
                    out.close();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    Log.e("MoreIO", e.getMessage());
                }
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        thingToFix = list_string.get(position); //get the task
        place = position; //store task's index in ArrayList
        toDo.setText(thingToFix); //display the focused task in EditText widget
    }

    //this function saves the current to do list in the 'list.txt' text file
    public void save_list() {
        try {
            try {
                out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
                for (int i = 0; i < list_string.size(); i++) {
                    out.write(list_string.get(i) + " \n"); //add each task to the file
                }
                Toast.makeText(this, "List is saved into 'list.txt' file", Toast.LENGTH_LONG)
                        .show();
            } catch (IOException e2) { }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            Log.e("MoreIO", e.getMessage());
        }
    }
}