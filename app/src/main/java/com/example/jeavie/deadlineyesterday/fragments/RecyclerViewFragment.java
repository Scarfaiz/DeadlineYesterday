package com.example.jeavie.deadlineyesterday.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeavie.deadlineyesterday.abilities.EditTaskActivity;
import com.example.jeavie.deadlineyesterday.MainActivity;
import com.example.jeavie.deadlineyesterday.R;
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

    String summary, date, time;

    public RecyclerViewFragment() { // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        db = new DbActivity(getContext());
        fullData = db.getAllData();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                deadlines = new ArrayList<>();
                int i = 1;
                do {
                    String check = fullData.getString(7);
                    if (check.startsWith("li")){
                        String id = fullData.getString(1);
                        summary=fullData.getString(2);
                        date=fullData.getString(3);
                        time=fullData.getString(4);
                        String deadline=fullData.getString(5);
                        String tags=fullData.getString(6);
                        deadlines.add(new Deadline(id, summary, date, time, deadline,
                                tags));
                        i++;
                    }
                } while (fullData.moveToNext());
            }
        }
        else deadlines = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler_view);
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
        if (requestCode == MainActivity.INTENT_RESULT_CODE){
            if(resultCode == MainActivity.INTENT_RESULT_CODE) {
                DbActivity db = new DbActivity(getContext());
                Cursor newDeadline = db.getAllData();
                newDeadline.moveToLast();
                String id = newDeadline.getString(1);
                summary = newDeadline.getString(2);
                date = newDeadline.getString(3);
                time = newDeadline.getString(4);
                String deadline = newDeadline.getString(5);
                String tags = newDeadline.getString(6);
                deadlines.add(new Deadline(id, summary, date, time, deadline, tags));
                deadlineAdapter.notifyDataSetChanged();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if (requestCode == MainActivity.INTENT_RESULT_CODE_TWO){
            if (resultCode == MainActivity.INTENT_RESULT_CODE_TWO) {
                String a = deadlines.get(EditTaskActivity.number).getId();
                Cursor newDeadline = db.getData(a);
                String id = newDeadline.getString(0);
                summary=newDeadline.getString(1);
                date=newDeadline.getString(2);
                time=newDeadline.getString(3);
                String deadline=newDeadline.getString(4);
                String tags=newDeadline.getString(5);
                deadlines.remove(Integer.valueOf(id) - 1);
                deadlines.add(Integer.valueOf(id) - 1, new Deadline(id, summary, date, time, deadline, tags));
                deadlineAdapter.notifyDataSetChanged();
            }
        }
    }

}