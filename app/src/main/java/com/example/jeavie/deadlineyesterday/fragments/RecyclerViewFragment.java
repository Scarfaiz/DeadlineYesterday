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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    String summary, date, time, deadline, labels;

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
                    String check = fullData.getString(4);
                    if (!check.startsWith("co")){
                        summary = fullData.getString(1);
                        date = fullData.getString(2);
                        time = fullData.getString(3);
                        deadline = fullData.getString(4);
                        labels = fullData.getString(5);
                        deadlines.add(new Deadline(summary, date, time, deadline,
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
                new IntentFilter("AddDeadlineActivity"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            DbActivity db = new DbActivity(getContext());
            Cursor newDeadline = db.getAllData();
            newDeadline.moveToLast();
            summary = newDeadline.getString(1);
            date = newDeadline.getString(2);
            time = newDeadline.getString(3);
            deadline = newDeadline.getString(4);
            labels = newDeadline.getString(5);
            deadlines.add(new Deadline(summary, date, time, deadline, labels));
            deadlineAdapter.notifyDataSetChanged();
            //String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + "fake message");

        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

//    @Override
//    public void onActivityResult (int requestCode, int resultCode, Intent data) {
//        if (requestCode == Codes.INTENT_RESULT_CODE){
//            if(resultCode == Codes.INTENT_RESULT_CODE) {
//
//                super.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//        else if (requestCode == Codes.INTENT_RESULT_CODE_TWO){
//            if (resultCode == Codes.INTENT_RESULT_CODE_TWO) {
//                String a = deadlines.get(EditTaskActivity.number).getId();
//                Cursor newDeadline = db.getData(a);
//                String id = newDeadline.getString(0);
//                summary=newDeadline.getString(1);
//                date=newDeadline.getString(2);
//                time=newDeadline.getString(3);
//                String deadline=newDeadline.getString(4);
//                String tags=newDeadline.getString(5);
//                deadlines.remove(Integer.valueOf(id) - 1);
//                deadlines.add(Integer.valueOf(id) - 1, new Deadline(summary, date, time, deadline, tags));
//                deadlineAdapter.notifyDataSetChanged();
//            }
//        }
//    }

}