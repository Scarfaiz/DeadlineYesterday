package com.example.jeavie.deadlineyesterday;

import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

public class DeadlineActivity extends AppCompatActivity {

    private String summary;
    private String date;
    private String time;
    private String deadline;
    private ArrayList<String> tags;

    public DeadlineActivity(String summary, String deadline, ArrayList<String> tags) {
        this.summary = summary;
//        this.date = date;
//        this.time = time;
        this.deadline = deadline;
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getTime(){
//        return time;
//    }
//
//    public void setTime(String time){
//        this.time = time;
//    }

    public String getDeadline(){
        return deadline;
    }

    public void setDeadline(String deadline){
        this.deadline = deadline;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
