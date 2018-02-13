package com.example.jeavie.deadlineyesterday.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jeavie.deadlineyesterday.abilities.EditTaskActivity;
import com.example.jeavie.deadlineyesterday.MainActivity;
import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.Codes;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;
import com.example.jeavie.deadlineyesterday.adapters.DeadlineAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment{

    View v;
    private RecyclerView recyclerView;
    private List<Deadline> deadlines;
    DeadlineAdapter deadlineAdapter;

    DbActivity db;
    Cursor fullData;
    LinearLayout empty;

    String summary, date, time;

    public RecyclerViewFragment() { // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        empty = v.findViewById(R.id.empty);

        db = new DbActivity(getContext());
        fullData = db.getAllData();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                deadlines = new ArrayList<>();
                int i = 1;
                do {
                    String check = fullData.getString(4);
                    if (!check.startsWith("co")){
                        summary = fullData.getString(1);
                        date = fullData.getString(2);
                        time = fullData.getString(3);
                        String deadline = fullData.getString(4);
                        String labels = fullData.getString(5);
                        //String labels = "wtf";
                        deadlines.add(new Deadline(summary, date, time, deadline,
                                labels));
                        i++;
                    }
                } while (fullData.moveToNext());
            }
            empty.setVisibility(View.GONE);
        }
        else {
            deadlines = new ArrayList<>();
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        deadlineAdapter = new DeadlineAdapter(getContext(), deadlines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(deadlineAdapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == Codes.INTENT_RESULT_CODE){
            if(resultCode == Codes.INTENT_RESULT_CODE) {
                DbActivity db = new DbActivity(getContext());
                Cursor newDeadline = db.getAllData();
                newDeadline.moveToLast();
                String id = newDeadline.getString(1);
                summary = newDeadline.getString(2);
                date = newDeadline.getString(3);
                time = newDeadline.getString(4);
                String deadline = newDeadline.getString(5);
                String tags = newDeadline.getString(6);
                deadlines.add(new Deadline(summary, date, time, deadline, tags));
                deadlineAdapter.notifyDataSetChanged();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if (requestCode == Codes.INTENT_RESULT_CODE_TWO){
            if (resultCode == Codes.INTENT_RESULT_CODE_TWO) {
                String a = deadlines.get(EditTaskActivity.number).getId();
                Cursor newDeadline = db.getData(a);
                String id = newDeadline.getString(0);
                summary=newDeadline.getString(1);
                date=newDeadline.getString(2);
                time=newDeadline.getString(3);
                String deadline=newDeadline.getString(4);
                String tags=newDeadline.getString(5);
                deadlines.remove(Integer.valueOf(id) - 1);
                deadlines.add(Integer.valueOf(id) - 1, new Deadline(summary, date, time, deadline, tags));
                deadlineAdapter.notifyDataSetChanged();
            }
        }
    }

}