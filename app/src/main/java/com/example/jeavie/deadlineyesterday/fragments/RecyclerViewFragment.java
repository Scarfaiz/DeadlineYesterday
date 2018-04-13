package com.example.jeavie.deadlineyesterday.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jeavie.deadlineyesterday.activities.AddDeadlineActivity;
import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.Codes;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;
import com.example.jeavie.deadlineyesterday.adapter.DeadlineAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment{

    DbActivity db;
    Cursor fullData;

    View v;

    ConstraintLayout addDeadline;
    CardView empty;

    RecyclerView recyclerView;
    List<Deadline> deadlines;
    DeadlineAdapter deadlineAdapter;

    String id, summary, date, time, deadline, labels;

    public RecyclerViewFragment() { // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        setBasicView();
        addDeadline();

        return v;
    }

    public void setBasicView(){
        recyclerView = v.findViewById(R.id.recycler_view);
        empty = v.findViewById(R.id.empty);

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

    public void addDeadline(){
        addDeadline = v.findViewById(R.id.add_deadline);
        addDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), AddDeadlineActivity.class);
                startActivityForResult(intent, Codes.INTENT_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver,
                new IntentFilter("AddDeadline"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            empty.setVisibility(View.GONE);
            DbActivity db = new DbActivity(getContext());
            Cursor newDeadline = db.getAllData();
            int position = intent.getIntExtra("position", -1);
            try {
                if (position != -1){
                    newDeadline.moveToPosition(position);
                    id = newDeadline.getString(1);
                    summary = newDeadline.getString(2);
                    date = newDeadline.getString(3);
                    time = newDeadline.getString(4);
                    deadline = newDeadline.getString(5);
                    labels = newDeadline.getString(6);
                    deadlines.remove(position);
                    deadlines.add(position, new Deadline(String.valueOf(position), summary, date, time, deadline, labels));
                }
                else {
                    newDeadline.moveToLast();
                    id = newDeadline.getString(1);
                    summary = newDeadline.getString(2);
                    date = newDeadline.getString(3);
                    time = newDeadline.getString(4);
                    deadline = newDeadline.getString(5);
                    labels = newDeadline.getString(6);
                    deadlines.add(new Deadline(id, summary, date, time, deadline, labels));
                }
            }
            catch (NullPointerException e){
                newDeadline.moveToLast();
            }
            deadlineAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
//            Log.d("receiver", "Got message: " + "deadline added");
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}