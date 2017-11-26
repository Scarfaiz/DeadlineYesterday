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


//TODO:add drawing toolbar: icon - during date - week view - settings (colored tags, notifications) - about
//TODO:add deadlines view (main view) - "add deadline" message or all nearest events with dates etc.
//TODO:add an opportunity to save tasks
//TODO:modify tasks
//TODO:delete tasks by sliding?
//TODO:check previous tasks
//TODO:complete add task activity: - name - time - tag - notification.
//TODO:complete settings activity

////TODO:add an opportunity to add a new task by holding a week date?
////TODO:light theme?
public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    Locale locale;
    String lang;
    FloatingActionButton addTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// language configuration (show months in english)
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

            case R.id.toWeek:
                startActivity(new Intent(this, WeekActivity.class));
                return true;

            case R.id.history:
                startActivity(new Intent(this, HistoryActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
