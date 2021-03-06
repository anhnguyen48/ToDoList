/* Name: Anh Nguyen
   Date: March 6th, 2021
   Purpose: This is a To Do List application. This list is a numbered sequence. When user types a
   task into EditText widget and clicks "ADD", the task will be added into the list. New task is
   always added to the end of the list. If users clicks on a task on the to do list, the task will
   be displayed on EditText widget. User can either press "DELETE" to remove this task from the to
   do list or modify the task and press "UPDATE" to put the modified task back into the list. Also,
   there are "SAVE" and "CLOSE" buttons. "SAVE" button will write the list to storage while "CLOSE"
   save the list & close the application.
   Note: Save function is not a part of this homework assignment, hence it won't work for this app.
 */


package com.example.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private EditText toDo; //Allow user to type a task
    private ArrayList<String> list_string = new ArrayList<String>(); //store a list of tasks that user writes down
    private ListView listview;
    private String thingToFix; //temporary store the focused task (task currently chosen by user)
    private int place; //index of thingToFix in the ArrayList

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

                return true;

            case R.id.Delete:
                list_string.remove(place); //remove the task at the specific index in ArrayList

                customAdapter = new CustomAdapter(this, list_string);
                listview.setAdapter(customAdapter);
                toDo.setText(""); //clear EditText widget

                return true;

            case R.id.Update:
                String S_toUpdate = toDo.getText().toString(); //get the modified task
                list_string.set(place, S_toUpdate); //update ArrayList with modified task

                customAdapter = new CustomAdapter(this, list_string);
                listview.setAdapter(customAdapter);
                toDo.setText(""); //clear EditText widget

                return true;

            case R.id.Save:
                return true;

            case R.id.Exit:
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
}