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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class EditTaskActivity extends AppCompatActivity{

    int minute, hour, year, month, day, position;
    String format, summaryData, changedSummary, dateData, changedDate, timeData, changedTime;
    TextView setDate, setTime;
    ArrayList<String> tagsData;


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

        Intent intent = getIntent();
        summaryData = intent.getStringExtra("summary_data");
        dateData = intent.getStringExtra("date_data");
        timeData = intent.getStringExtra("time_data");
        tagsData = intent.getStringArrayListExtra("tags_data");

        position = intent.getIntExtra(MainActivity.INTENT_POSITION, -1);

        EditText editTextSummary = findViewById(R.id.summary);
        editTextSummary.setText(summaryData);

        TagsEditText editTextTags = findViewById(R.id.tags);
        String[] tags = setTags(tagsData);
        editTextTags.setTags(tags);

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

    public String[] setTags(ArrayList<String> tags){
        String [] parsed = tags.toArray(new String[0]);
        return parsed;
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
                    Intent intent = new Intent();
                    intent.putExtra("changedSummary", changedSummary);
                    intent.putExtra("changedDate", changedDate);
                    intent.putExtra("changedTime", changedTime);
                    intent.putStringArrayListExtra("changedTags", (ArrayList<String>) tags);
                    intent.putExtra(MainActivity.INTENT_POSITION, position);
                    setResult(MainActivity.INTENT_RESULT_CODE_TWO, intent);
                    finish();
                } else if (MainActivity.INTENT_RESULT_CODE_TWO == 1) {
                    Toast.makeText(this, "You did not enter a summary", Toast.LENGTH_SHORT).show();
                } else if (MainActivity.INTENT_RESULT_CODE_TWO == 3){
                    Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
