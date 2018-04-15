package com.example.jeavie.deadlineyesterday.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.adapter.DeadlineAdapter;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment{

    DbActivity db;
    Cursor fullData;

    View v;

    ConstraintLayout addDeadline;
    CardView empty;

    RecyclerView recyclerView;
    List<Deadline> deadlines;
    DeadlineAdapter deadlineAdapter;

    String id, summary, date, time, deadline, labels;

    public HistoryFragment() { // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history, container, false);
        return v;
    }

    public void setBasicView(){
        recyclerView = v.findViewById(R.id.history_view);
        empty = v.findViewById(R.id.empty_history);

        db = new DbActivity(getContext());
        fullData = db.getAllData();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                deadlines = new ArrayList<>();
                int i = 1;
                do {
                    String check = fullData.getString(5);
                    if (!check.startsWith("co")){
                        id = fullData.getString(1);
                        summary = fullData.getString(2);
                        date = fullData.getString(3);
                        time = fullData.getString(4);
                        deadline = fullData.getString(5);
                        labels = fullData.getString(6);
                        deadlines.add(new Deadline(id, summary, date, time, deadline,
                                labels));
                        i++;
                    }
                } while (fullData.moveToNext());
            }
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            deadlines = new ArrayList<>();
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        deadlineAdapter = new DeadlineAdapter(getContext(), deadlines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(deadlineAdapter);
    }

}