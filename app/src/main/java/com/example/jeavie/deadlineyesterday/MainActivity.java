package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//add an opportunity to add a new task by holding a calendar/week date
//add drawing toolbar: icon - during date - week view - month view - settings (colors, notifications) - about
//add week view (main view) - "no tasks" message or all nearest events with dates etc.
//add an opportunity to create and save tasks
//complete add task activity
//add settings activity
//light theme?
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.toMonth:
                startActivity(new Intent(this, MonthActivity.class));
                return true;

            case R.id.addTask:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
