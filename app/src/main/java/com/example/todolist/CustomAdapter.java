//A custom adapter that helps arranging the tasks into a numbered sequence

package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    ArrayList<String> arrayList;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String toDoThing = arrayList.get(position); //get the task from ArrayList
        int todo_order = position + 1; //calculate the order of the task

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item, null);

            TextView toDo = convertView.findViewById(R.id.toDoThing);
            toDo.setText(Integer.toString(todo_order) + ". " + toDoThing);
        }
        return convertView;
    }
}
