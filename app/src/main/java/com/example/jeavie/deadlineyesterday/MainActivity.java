package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//Action bar + Week view. Action bar consists of changing view and adding a new event activity
//Day view - no events - add a new or show a list and "+".
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

            case R.id.addEvent:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
