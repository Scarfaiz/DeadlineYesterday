package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

//add an opportunity to add a new task by holding a calendar/week date
//add drawing toolbar: icon - during date - week view - month view - settings (colors, notifications) - about
//add week view (main view) - "no tasks" message or all nearest events with dates etc.
//add an opportunity to create and save tasks
//complete add task activity
//add settings activity
//light theme?
public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private Locale locale;
    private String lang;
    FloatingActionButton addTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lang = getResources().getConfiguration().locale.getDisplayLanguage(Locale.CHINESE);
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);

        addTask=(FloatingActionButton)findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });
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
