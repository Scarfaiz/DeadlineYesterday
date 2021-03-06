package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.jeavie.deadlineyesterday.activities.AddDeadlineActivity;
import com.example.jeavie.deadlineyesterday.data.Codes;
import com.example.jeavie.deadlineyesterday.activities.LabelsActivity;
import com.example.jeavie.deadlineyesterday.fragments.HistoryFragment;
import com.example.jeavie.deadlineyesterday.fragments.HomeFragment;
import com.example.jeavie.deadlineyesterday.fragments.RecyclerViewFragment;

import java.util.Locale;

//TODO: vector images +
//TODO: empty btn
//TODO: recyclerview style +
//TODO: add and edit in one +
//TODO: addDeadline style, btn? +
//TODO: upd deadlines in recyclerView +
//TODO: swipe function +

//TODO: SQLite database +-

//TODO: stop updating deadlines
//TODO: pass data to history

//TODO: history - delete full data btn, clear history - snackbar: cancel

//TODO: home activity:
//TODO: today, tomorrow, report cardviews
//TODO: today, tomorrow, report views?

//TODO: custom labels?
//TODO: clean bin
//TODO: tags to labels?

//TODO: colors

//TODO: notifications activity
//TODO: about activity

//TODO: vector icon

//TODO: sort by deadlines/add


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;

    DrawerLayout drawerLayout;
    FrameLayout frameLayout;

    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle toggle;

    private HomeFragment homeFragment;
    private RecyclerViewFragment recyclerViewFragment;
    private HistoryFragment historyFragment;

    public FloatingActionButton addDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        setPreferences();
        setToolbar();
        setDrawerLayout();
        setNavigationView();
        setBottomNavigationView();
        setFab();
    }

    private void setPreferences (){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);// language configuration
        String lang = getResources().getConfiguration().locale.getDisplayLanguage(Locale.CHINESE);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    private void setDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNavigationView(){
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setBottomNavigationView(){
        homeFragment = new HomeFragment();
        recyclerViewFragment = new RecyclerViewFragment();
        historyFragment = new HistoryFragment();

        frameLayout = findViewById(R.id.main_frame);
        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        addDeadline.show();
                        setFragment(homeFragment);
                        break;
                    case R.id.nav_deadlines:
                        addDeadline.show();
                        setFragment(recyclerViewFragment);
                        break;
                    case R.id.nav_history:
                        addDeadline.hide();
                        setFragment(historyFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void setFab(){
        addDeadline = findViewById(R.id.addTask);
        addDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddDeadlineActivity.class);
                //one more intent? try - catch
                startActivityForResult(intent, Codes.INTENT_REQUEST_CODE);
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.labels:
                startActivity(new Intent(this, LabelsActivity.class));
                return true;

            case R.id.notifications:
//                startActivity(new Intent(this, HistoryActivity.class));
                return true;

            case R.id.info:
//                startActivity(new Intent(this, HistoryActivity.class));
                return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}