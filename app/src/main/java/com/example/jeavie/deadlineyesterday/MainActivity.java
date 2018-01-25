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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//TODO: upd time in listview - runnable?
//TODO: complete tasks
//TODO: history activity
//TODO: clear history - snackbar: cancel
//TODO: week activity
//TODO: notifications settings
//TODO: about activity
//TODO: sort by order/deadlines
//TODO: vector images

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int INTENT_REQUEST_CODE = 1;
    public final static int INTENT_REQUEST_CODE_TWO = 2;
    public static int INTENT_RESULT_CODE = 1;
    public static int INTENT_RESULT_CODE_TWO = 2;
    public final static int INTENT_EMPTY_CODE = 0;
    public final static String INTENT_POSITION = "position";

    private ListView listView;
    DeadlineActivityAdapter deadlineActivityAdapter;
    int position;
    List<DeadlineActivity> list;
    String summary, getData, getTime;
    ArrayList <String> tagsArrList;

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

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        listView = findViewById(R.id.listDeadlines);

        TextView emptyText = findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

        list = new ArrayList<>();
        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list);
        listView.setAdapter(deadlineActivityAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("summary_data", list.get(position).getSummary());
                intent.putExtra("date_data", list.get(position).getDate());
                intent.putExtra("time_data", list.get(position).getTime());
                intent.putStringArrayListExtra("tags_data", list.get(position).getTagsArrList());
                intent.putExtra(INTENT_POSITION, position);
                startActivityForResult(intent, INTENT_REQUEST_CODE_TWO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_RESULT_CODE){
            if(resultCode == INTENT_RESULT_CODE) {
                summary = data.getStringExtra("summary");
                getData = data.getStringExtra("date");
                getTime = data.getStringExtra("time");
                tagsArrList = data.getStringArrayListExtra("tags");
                String tags = getTags(tagsArrList);
                String deadline = getDeadline(getData, getTime);
                list.add(new DeadlineActivity(summary, getData, getTime, deadline, tags, tagsArrList));
                deadlineActivityAdapter.notifyDataSetChanged();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if (requestCode == INTENT_RESULT_CODE_TWO){
            if (resultCode == INTENT_RESULT_CODE_TWO) {
                Toast.makeText(this, "Deadline upd", Toast.LENGTH_SHORT).show();
                summary = data.getStringExtra("changedSummary");
                getData = data.getStringExtra("changedDate");
                getTime = data.getStringExtra("changedTime");
                tagsArrList = data.getStringArrayListExtra("changedTags");
                String tags = getTags(tagsArrList);
                String deadline = getDeadline(getData, getTime);
                position = data.getIntExtra(INTENT_POSITION, -1);
                list.remove(position);
                list.add(position, new DeadlineActivity(summary, getData, getTime, deadline, tags, tagsArrList));
                deadlineActivityAdapter.notifyDataSetChanged();
            }
        }
    }

    public String getDeadline (String date, String time){
        String format = date + " " + time;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh : mm a");
        Date cal1 = new Date();
        Date cal2 = null;
        try {
            cal2 = df.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = cal2.getTime() - cal1.getTime();

        long diffMinutes = diff / (60 * 1000);

        long diffHours = diff / (60 * 60 * 1000);

        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays>1 && diffHours > 24){
            return String.valueOf(diffDays) + " days " + String.valueOf(diffHours - (diffDays*24)) + " hrs";
        } else if (diffDays==1 && diffHours > 24) {
            return String.valueOf(diffDays) + " day " + String.valueOf(diffHours - (diffDays*24)) + " hrs";
        }else if (diffHours>1){
            return String.valueOf(diffHours) + " hrs";
        }else if (diffHours==1){
            return String.valueOf(diffHours) + " hour";
        } else return String.valueOf(diffMinutes) + " min";
    }

    public String getTags(ArrayList<String> tags){
        String parsedTags = String.valueOf(tags).replace("[", "").replace("]", "");
        return parsedTags;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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