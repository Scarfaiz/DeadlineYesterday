package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void toMonth(View v) {
        Intent month = new Intent(this, MonthActivity.class);
        startActivity(month);
    }
}
