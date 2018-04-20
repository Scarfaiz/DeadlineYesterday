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
import android.widget.Toast;

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

        setBasicView();

        return v;
    }

    public void setBasicView(){
        recyclerView = v.findViewById(R.id.history_view);
        empty = v.findViewById(R.id.empty_history);

        db = new DbActivity(getContext());
        fullData = db.getAllData();
        deadlines = new ArrayList<>();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                do {
                    String check = fullData.getString(3);
//                    Toast.makeText(getContext(), check, Toast.LENGTH_SHORT).show();
                    if (check.startsWith("co")){
                        id = fullData.getString(1);
                        summary = fullData.getString(2);
                        date = fullData.getString(3);
                        time = fullData.getString(4);
                        deadline = "Completed";
                        labels = fullData.getString(5);
                        deadlines.add(new Deadline(id, summary, date, time, deadline,
                                labels));
                    }
                } while (fullData.moveToNext());
            }
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (deadlines.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        deadlineAdapter = new DeadlineAdapter(getContext(), deadlines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(deadlineAdapter);
    }

}