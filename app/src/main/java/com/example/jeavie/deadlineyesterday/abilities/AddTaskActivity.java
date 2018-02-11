package com.example.jeavie.deadlineyesterday.abilities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import com.example.jeavie.deadlineyesterday.MainActivity;
import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.wafflecopter.charcounttextview.CharCountTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class AddTaskActivity extends AppCompatActivity {

    int minute, hour, year, month, day, check;
    String format, summary, date, time;
    TextView setDate, setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        final EditText editText = findViewById(R.id.summary);

        CharCountTextView charCountTextView = findViewById(R.id.tvTextCounter);
        charCountTextView.setEditText(editText);
        charCountTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                //stop?
            }
        });

        check = 0;
        setDate = findViewById(R.id.setDate);

        final Calendar currentDate = Calendar.getInstance();
        year = currentDate.get(Calendar.YEAR);
        month = currentDate.get(Calendar.MONTH) + 1;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        setDate.setText(day + "/" + month + "/" + year);
        month -= 1;
        currentDate.set(Calendar.DAY_OF_MONTH, day);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        setDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
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
        setTime.setText(hour + " : " + minute + " " + format);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        summary = editTextSummary.getText().toString();

        TextView textViewDate = findViewById(R.id.setDate);
        date = textViewDate.getText().toString();

        TextView textViewTime = findViewById(R.id.setTime);
        time = textViewTime.getText().toString();

        boolean check = isDateCorrect(date, time);
        if (!check){
            return 3;
        }
        if (TextUtils.isEmpty(summary.trim())) {
            return 2;
        } else return 1;
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
        }else if (diffHours>=1){
            return String.valueOf(diffHours) + " h";
        } else if (diffSeconds > 0)
            return String.valueOf(diffSeconds) + " s";
        else return String.valueOf(diffMinutes) + " m";
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
                TagsEditText tagsEditText = findViewById(R.id.labels);
                List<String> tags = tagsEditText.getTags();

                MainActivity.INTENT_RESULT_CODE = codeToReturn();
                if (MainActivity.INTENT_RESULT_CODE == 1){
                    String deadline = getDeadline(date, time);
                    String tagstostring = getTags(tags);
                    DbActivity db = new DbActivity(this);
                    Cursor fullData = db.getAllData();
                    String id;
                    if (fullData.getCount() > 0){
                        fullData.moveToLast();
                        id = fullData.getString(1);
                        int i = Integer.valueOf(id) + 1;
                        id = String.valueOf(i);
                    } else id = "1";
                    boolean isInserted = db.insertData(id, summary, date, time, deadline, tagstostring, "list");
                    if (isInserted)
                        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                    setResult(MainActivity.INTENT_RESULT_CODE);
                    finish();
                } else if (MainActivity.INTENT_RESULT_CODE == 2) {
                    Toast.makeText(this, "You did not enter a summary", Toast.LENGTH_SHORT).show();
                } else if (MainActivity.INTENT_RESULT_CODE == 3) {
                    Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

}