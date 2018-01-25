package com.example.jeavie.deadlineyesterday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.wafflecopter.charcounttextview.CharCountTextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class AddTaskActivity extends AppCompatActivity {

    TextView setDate;
    TextView setTime;
    int minute, hour, year, month, day;
    String format;
    String summary;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
        currentDate.set(Calendar.DAY_OF_MONTH, day + 1);

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
                check = 1;
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
        if (TextUtils.isEmpty(summary.trim())) {
            return 2;
        } else if (check == 0){
            return 3;
        } else return 1;
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

                TextView textViewDate = findViewById(R.id.setDate);
                String date = textViewDate.getText().toString();

                TextView textViewTime = findViewById(R.id.setTime);
                String time = textViewTime.getText().toString();

                TagsEditText tagsEditText = findViewById(R.id.tags);
                List<String> tags = tagsEditText.getTags();
                MainActivity.INTENT_RESULT_CODE = codeToReturn();
                if (MainActivity.INTENT_RESULT_CODE == 1){
                    Intent intent = new Intent();
                    intent.putExtra("summary", summary);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putStringArrayListExtra("tags", (ArrayList<String>) tags);
                    setResult(MainActivity.INTENT_RESULT_CODE, intent);
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