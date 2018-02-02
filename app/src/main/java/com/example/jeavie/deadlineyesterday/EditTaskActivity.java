package com.example.jeavie.deadlineyesterday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import mabbas007.tagsedittext.TagsEditText;

public class EditTaskActivity extends AppCompatActivity{

    int minute, hour, year, month, day, number;
    String format, summaryData, changedSummary, dateData, changedDate, timeData, changedTime, deadline, tagsData;
    TextView setDate, setTime;

    DbActivity db;
    Cursor data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolbar_add_task_activity);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = new DbActivity(this);
        data = db.getData(String.valueOf(MainActivity.changingNumber + 1));
        summaryData = data.getString(0);
        dateData = data.getString(1);
        timeData = data.getString(2);
        deadline = data.getString(3);
        tagsData = data.getString(4);

        EditText editTextSummary = findViewById(R.id.summary);
        editTextSummary.setText(summaryData);

        TagsEditText editTextTags = findViewById(R.id.tags);
        if (!TextUtils.isEmpty(tagsData.trim())) {
            String[] tags = tagsData.split(", ");
            editTextTags.setTags(tags);
        } else {
            editTextTags.setHint(getResources().getString(R.string.addTags));
        }

        CharCountTextView charCountTextView = findViewById(R.id.tvTextCounter);
        charCountTextView.setEditText(editTextSummary);
        charCountTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                //stop?
            }
        });

        setDate = findViewById(R.id.setDate);

        final Calendar currentDate = Calendar.getInstance();
        year = currentDate.get(Calendar.YEAR);
        month = currentDate.get(Calendar.MONTH) + 1;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        setDate.setText(dateData);
        month -= 1;
        currentDate.set(Calendar.DAY_OF_MONTH, day);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        setDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis() - 1000);
                datePickerDialog.show();
            }
        });

        setTime = findViewById(R.id.setTime);

        Calendar currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        hour = selectedTimeFormat(hour);
        setTime.setText(timeData);

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
            format = "AM";
        } else if (hour >= 12){
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return hour;
    }

    public int codeToReturn(){
        EditText editTextSummary = findViewById(R.id.summary);
        changedSummary = editTextSummary.getText().toString();

        TextView textViewDate = findViewById(R.id.setDate);
        changedDate = textViewDate.getText().toString();

        TextView textViewTime = findViewById(R.id.setTime);
        changedTime = textViewTime.getText().toString();

        boolean check = isDateCorrect(changedDate, changedTime);
        if (!check){
            return 3;
        }
        if (TextUtils.isEmpty(changedSummary.trim())) {
            return 1;
        } else return 2;
    }

    public boolean isDateCorrect (String date, String time){
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
        return (diff > 0);
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

        long diffSeconds = diff/(1000);

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
        } else if (diffSeconds > 0)
            return String.valueOf(diffSeconds) + " sec";
        else return String.valueOf(diffMinutes) + " min";
    }

    public String getTags(List<String> tags){
        String parsedTags = String.valueOf(tags).replace("[", "").replace("]", "");
        return parsedTags;
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

                TagsEditText tagsEditText = findViewById(R.id.tags);
                List<String> tags = tagsEditText.getTags();

                MainActivity.INTENT_RESULT_CODE_TWO = codeToReturn();
                if (MainActivity.INTENT_RESULT_CODE_TWO == 2){
                    deadline = getDeadline(changedDate, changedTime);
                    String tagstostring = getTags(tags);
                    DbActivity db = new DbActivity(this);
                    boolean isInserted = db.updateData(String.valueOf(MainActivity.changingNumber + 1), changedSummary, changedDate, changedTime, deadline, tagstostring, "list");
                    if (isInserted)
                        Toast.makeText(this, "Deadline saved", Toast.LENGTH_SHORT).show();
                    setResult(MainActivity.INTENT_RESULT_CODE_TWO);
                    finish();
                } else if (MainActivity.INTENT_RESULT_CODE_TWO == 1) {
                    Toast.makeText(this, "You did not enter a summary", Toast.LENGTH_SHORT).show();
                } else if (MainActivity.INTENT_RESULT_CODE_TWO == 3) {
                    Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
