package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

//TODO:add drawing toolbar: icon - during date - week view - settings (colored tags, notifications) - about
//TODO:add deadlines view (main view) - "add deadline" message or all nearest events with dates etc.
//TODO:add an opportunity to save tasks
//TODO:modify tasks
//TODO:delete tasks by sliding?
//TODO:check previous tasks
//TODO:complete add task activity: - name - datetime - colored tag - notification "yes-no".
//TODO:complete settings activity

////TODO:add an opportunity to add a new task by holding a week date?
////TODO:light theme?

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);// language configuration
        String lang = getResources().getConfiguration().locale.getDisplayLanguage(Locale.CHINESE);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton addTask = (FloatingActionButton)findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.tags:
                startActivity(new Intent(this, TagsActivity.class));
                return true;

            case R.id.notifications:
//                startActivity(new Intent(this, HistoryActivity.class));
                return true;

            case R.id.info:
//                startActivity(new Intent(this, HistoryActivity.class));
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}