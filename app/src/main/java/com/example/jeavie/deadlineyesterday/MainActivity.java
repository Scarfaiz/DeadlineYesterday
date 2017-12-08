package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//TODO: main - "add deadline" message or "listview" events with dates etc.
//TODO: save, modify, delete tasks
//TODO: history activity, clear history - snackbar: cancel
//TODO: tags activity
//TODO: notifications activity
//TODO: about
//TODO: week activity
//TODO: sort by?

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int INTENT_REQUEST_CODE = 1;
    public final static int INTENT_RESULT_CODE = 1;
    public final static int INTENT_EMPTY_CODE = 0;
    String result;

    private ListView listView;
    DeadlineActivityAdapter deadlineActivityAdapter;
    List<DeadlineActivity> list;
    String summary, getData, getTime, tags;

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
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView)findViewById(R.id.listDeadlines);

        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

        list = new ArrayList<DeadlineActivity>();
        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list);
        listView.setAdapter(deadlineActivityAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_RESULT_CODE){
            if(resultCode == INTENT_RESULT_CODE) {
                Toast.makeText(this, "Deadline saved", Toast.LENGTH_SHORT).show();
                summary = data.getStringExtra("summary");
                list.add(new DeadlineActivity(summary, "", ""));
                deadlineActivityAdapter.notifyDataSetChanged();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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