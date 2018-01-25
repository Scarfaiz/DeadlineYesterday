package com.example.jeavie.deadlineyesterday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wafflecopter.charcounttextview.CharCountTextView;

import java.util.ArrayList;
import java.util.Calendar;

import mabbas007.tagsedittext.TagsEditText;

public class EditTaskActivity extends AppCompatActivity{

    TextView setDate;
    TextView setTime;
    int minute, hour, year, month, day;
    String format, summaryData, changedSummary, deadlineData, changedDeadline;
    ArrayList<String> tagsData, changedTags;

    int position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolbar_add_task_activity);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        Intent intent = getIntent();
        summaryData = intent.getStringExtra("summary_data");

        position = intent.getIntExtra(MainActivity.INTENT_POSITION, -1);
        EditText editTextSummary = findViewById(R.id.summary);
        editTextSummary.setText(summaryData);

        CharCountTextView charCountTextView = findViewById(R.id.tvTextCounter);
        charCountTextView.setEditText(editTextSummary);
        charCountTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                //stop?
            }
        });

        setDate = findViewById(R.id.setDate);

        Calendar currentDate = Calendar.getInstance();
        year = currentDate.get(Calendar.YEAR);
        month = currentDate.get(Calendar.MONTH) + 1;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        setDate.setText(day + "/" + month + "/" + year);
        month = month - 1;

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        setTime = findViewById(R.id.setTime);

        Calendar currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        selectedTimeFormat(hour);
        setTime.setText(hour + " : " + minute + " " + format);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hourOfDay = selectedTimeFormat(hourOfDay);
                        setTime.setText(hourOfDay + " : " + minute + " " + format);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

    public int selectedTimeFormat(int hour){
        if (hour == 0){
            hour += 12;
            format = "AM";
        } else if (hour == 12){
            format = "PM";
        } else if (hour > 12){
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return hour;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(MainActivity.INTENT_EMPTY_CODE, intent);
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.doneTask:

                EditText editTextSummary = findViewById(R.id.summary);
                changedSummary = editTextSummary.getText().toString();

                TextView textViewDate = findViewById(R.id.setDate);
                String deadline1 = textViewDate.getText().toString();
                TextView textViewTime = findViewById(R.id.setTime);
                String deadline2 = textViewTime.getText().toString();

                TagsEditText tagsEditText = findViewById(R.id.tags);
                tagsEditText.getTags();

                if (TextUtils.isEmpty(changedSummary.trim())) {
                    Toast.makeText(this, "You did not new enter a summary", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("changedSummary", changedSummary);
                    intent.putExtra(MainActivity.INTENT_POSITION, position);
                    setResult(MainActivity.INTENT_RESULT_CODE_TWO, intent);
                    finish();
                    return true;
                }

        }
        return super.onOptionsItemSelected(item);
    }


}
