package com.example.jeavie.deadlineyesterday.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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
    TagsEditText labelsEditText;

    List<String> labels;
    String format, summary, date, time;
    int minute, hour, year, month, day, position;
    boolean dataIsSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadline);

        setToolbar();
        setViews();
        setCharCountTextView();

        dataIsSet = setDataFromRecyclerView();
        setDate();
        setTime();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar_add_task_activity);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitleTextColor(getResources().getColor(R.color.WHITE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setViews(){
        editText = findViewById(R.id.summary);
        charCountTextView = findViewById(R.id.tvTextCounter);
        dateTextView = findViewById(R.id.setDate);
        timeTextView = findViewById(R.id.setTime);
        labelsEditText = findViewById(R.id.labels);
    }

    public void setCharCountTextView() {

        charCountTextView.setEditText(editText);
        charCountTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                //stop?
            }
        });
    }

    public boolean setDataFromRecyclerView(){
        Intent intent = getIntent();
        summary = intent.getStringExtra("summary");
        try {
            if (!TextUtils.isEmpty(summary.trim())) {
                position = intent.getIntExtra("position", -1);
                date = intent.getStringExtra("date");
                time = intent.getStringExtra("time");
                String ls = intent.getStringExtra("labels");

                editText.setText(summary);
                dateTextView.setText(date);
                timeTextView.setText(time);
                try {
                    if (!TextUtils.isEmpty(ls.trim())) {
                        String[] label = ls.split(", ");
                        labelsEditText.setTags(label);
                    } else {
                        labelsEditText.setHint(getResources().getString(R.string.addLabels));
                    }
                } catch (NullPointerException e) {
                    // yeah, it's empty
                }
                return true;
            }
        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }

    public void setDate(){
        final Calendar currentDate = Calendar.getInstance();
        year = currentDate.get(Calendar.YEAR);
        month = currentDate.get(Calendar.MONTH) + 1;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        if(!dataIsSet) dateTextView.setText(day + "/" + month + "/" + year);
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

        Calendar currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        hour = selectedTimeFormat(hour);
        if(!dataIsSet) timeTextView.setText(hour + " : " + minute + " " + format);

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

        summary = editText.getText().toString();
        date = dateTextView.getText().toString();
        time = timeTextView.getText().toString();
        labels = labelsEditText.getTags();

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

                Codes.INTENT_RESULT_CODE = codeToReturn();
                if (Codes.INTENT_RESULT_CODE == 1){

                    String deadline = getDeadline(date, time);
                    String labelsToString = getLabels(labels);
                    DbActivity db = new DbActivity(this);
                    boolean isInserted;
                    String id;
                    if(!dataIsSet) {
                        try{
                            Cursor newDeadline = db.getAllData();
                            newDeadline.moveToLast();
                            id = String.valueOf(Integer.valueOf(newDeadline.getString(1)) + 1);
                        }catch (CursorIndexOutOfBoundsException e){
                            id = String.valueOf(Codes.ID);
                        }
                        isInserted = db.insertData(id, summary, date, time, deadline, labelsToString);
                    } else {
                        id = String.valueOf(position + 1);
                        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                        isInserted = db.updateData(id, id, summary, date, time, deadline, labelsToString);
                    }
                    newDeadlineToFragment();
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

    private void newDeadlineToFragment() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("AddDeadline");
        if (dataIsSet) {
            intent.putExtra("position", position);
            intent.putExtra("upd", "upd");
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}