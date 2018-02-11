package com.example.jeavie.deadlineyesterday.data;

import android.support.v7.app.AppCompatActivity;

public class Deadline extends AppCompatActivity {

    private String id;
    private String summary;
    private String date;
    private String time;
    private String deadline;
    private String labels;
    //private  ArrayList tagsArrList;

    public Deadline(String id, String summary, String date, String time, String deadline, String labels) {
        this.id = id;
        this.summary = summary;
        this.date = date;
        this.time = time;
        this.deadline = deadline;
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getDeadline(){
        return deadline;
    }

    public void setDeadline(String deadline){
        this.deadline = deadline;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

}
