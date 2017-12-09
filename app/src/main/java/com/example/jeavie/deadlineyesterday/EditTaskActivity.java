package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jeavie on 09/12/2017.
 */

public class EditTaskActivity extends AppCompatActivity{
    String summaryData;
    int position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toast.makeText(this, "STARTED", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        summaryData = intent.getStringExtra("summary_data");
        position = intent.getIntExtra(MainActivity.INTENT_POSITION, -1);
        EditText editTextSummary = (EditText)findViewById(R.id.summary);
        editTextSummary.setHint(summaryData);
    }
}
