package com.example.jeavie.deadlineyesterday.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.Codes;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.wafflecopter.charcounttextview.CharCountTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class AddDeadlineActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText editText;
    CharCountTextView charCountTextView;
    TextView dateTextView, timeTextView;
    TagsEditText tagsEditText;

    List<String> labels;
    String format, summary, date, time;
    int minute, hour, year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadline);

        setToolbar();
        setEditText();
        setCharCountTextView();
        setDate();
        setTime();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar_add_task_activity);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setEditText() {
        editText = findViewById(R.id.summary);
    }

    public void setCharCountTextView() {
        charCountTextView = findViewById(R.id.tvTextCounter);
        charCountTextView.setEditText(editText);
        charCountTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                //stop?
            }
        });
    }

    public void setDate(){
        dateTextView = findViewById(R.id.setDate);

        final Calendar currentDate = Calendar.getInstance();
        year = currentDate.get(Calendar.YEAR);
        month = currentDate.get(Calendar.MONTH) + 1;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        dateTextView.setText(day + "/" + month + "/" + year);
        month -= 1;
        currentDate.set(Calendar.DAY_OF_MONTH, day);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDeadlineActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        dateTextView.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    public void setTime(){
        timeTextView = findViewById(R.id.setTime);

        Calendar currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        hour = selectedTimeFormat(hour);
        timeTextView.setText(hour + " : " + minute + " " + format);

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddDeadlineActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hourOfDay = selectedTimeFormat(hourOfDay);
                        timeTextView.setText(hourOfDay + " : " + minute + " " + format);
                    }
                }, hour, minute, true);
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
        } else
            format = "AM";
        return hour;
    }

    public int codeToReturn(){
        editText = findViewById(R.id.summary);
        summary = editText.getText().toString();

        dateTextView = findViewById(R.id.setDate);
        date = dateTextView.getText().toString();

        timeTextView = findViewById(R.id.setTime);
        time = timeTextView.getText().toString();

        if (!(correctDate(date, time) > 0)) return 3;

        if (TextUtils.isEmpty(summary.trim())) return 2;
            else return 1;
    }

    public long correctDate (String date, String time){
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
        return diff;
    }

    public String getDeadline (String date, String time){

        long diff = correctDate(date, time);

        long diffSeconds = diff/(1000);

        long diffMinutes = diff / (60 * 1000);

        long diffHours = diff / (60 * 60 * 1000);

        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays>=1){
            return String.valueOf(diffDays) + "d " + String.valueOf(diffHours - (diffDays*24)) + "h";
        } else if (diffHours>=1){
            return String.valueOf(diffHours) + "h " + String.valueOf(diffMinutes - (diffHours*60)) + "m";
        } else if (diffSeconds > 0) //for debuging
            return String.valueOf(diffSeconds) + "s ";
        else return String.valueOf(diffMinutes) + "m ";
    }

    public String getLabels(List<String> labels){
        String parsedLabels = String.valueOf(labels).replace("[", "").replace("]", "");
        return parsedLabels;
    }

    public void setLabels(){
        tagsEditText = findViewById(R.id.labels);
        labels = tagsEditText.getTags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        super.setResult(Codes.INTENT_EMPTY_CODE, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.doneTask:
                setLabels();
                Codes.INTENT_RESULT_CODE = codeToReturn();
                if (Codes.INTENT_RESULT_CODE == 1){

                    String deadline = getDeadline(date, time);
                    String labelsToString = getLabels(labels);
                    DbActivity db = new DbActivity(this);
                    boolean isInserted = db.insertData(summary, date, time, deadline, labelsToString);
                    if (isInserted)
                        sendMessage();
                    super.setResult(Codes.INTENT_RESULT_CODE);
                    finish();
                } else if (Codes.INTENT_RESULT_CODE == 2) {
                    Toast.makeText(this, "You did not enter a summary", Toast.LENGTH_SHORT).show();
                } else if (Codes.INTENT_RESULT_CODE == 3) {
                    Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("AddDeadlineActivity");
        //include some extra data.
        //intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}