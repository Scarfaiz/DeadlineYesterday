package com.example.jeavie.deadlineyesterday;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class DeadlineActivity extends AppCompatActivity {

    private String summary;
    private String deadline;
    private String tags;

    //Constructor


    public DeadlineActivity(String summary, String dealine, String tags) {
        this.summary = summary;
        this.deadline = dealine;
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDealine(String dealine) {
        this.deadline = dealine;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
