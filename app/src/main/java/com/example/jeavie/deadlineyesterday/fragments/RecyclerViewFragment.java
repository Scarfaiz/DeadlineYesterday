package com.example.jeavie.deadlineyesterday.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jeavie.deadlineyesterday.activities.AddDeadlineActivity;
import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.Codes;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;
import com.example.jeavie.deadlineyesterday.adapter.DeadlineAdapter;
import com.example.jeavie.deadlineyesterday.helper.RecyclerViewSwipeHelper;
import com.example.jeavie.deadlineyesterday.interfaces.RecyclerViewSwipeHelperListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements RecyclerViewSwipeHelperListener {

    DbActivity db;
    Cursor fullData;

    View v;

    ConstraintLayout addDeadline;
    RelativeLayout rootRecyclerViewLayout;
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
        setHandler();

        return v;
    }

    public void setBasicView(){
        rootRecyclerViewLayout = v.findViewById(R.id.rootRecyclerViewLayout);
        recyclerView = v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewSwipeHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        empty = v.findViewById(R.id.empty);

        db = new DbActivity(getContext());
        fullData = db.getAllData();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                deadlines = new ArrayList<>();
                int i = 1;
                do {
                    String check = fullData.getString(3);
                    if (!check.startsWith("co")){
                        id = fullData.getString(1);
                        summary = fullData.getString(2);
                        date = fullData.getString(3);
                        time = fullData.getString(4);
                        deadline = getDeadline(date, time);
                        labels = fullData.getString(5);
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

    public void setHandler(){
        final Handler handler = new Handler();
        final int delay = 1000 ; //minutes * 60

        handler.postDelayed(new Runnable(){
            public void run(){
                deadlineAdapter.timer();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public long correctDate (String date, String time){
        String format = date + " " + time;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh : mm");
        Date cal1 = new Date();
        Date cal2 = null;
        try {
            cal2 = df.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = cal2.getTime() - cal1.getTime();
        return diff;
    }

    public String getDeadline (String date, String time){

        long diff = correctDate(date, time);

        long diffSeconds = diff/(1000);

        long diffMinutes = diff / (60 * 1000);

        long diffHours = diff / (60 * 60 * 1000);

        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays>=1){
            return String.valueOf(diffDays) + "d " + String.valueOf(diffHours - (diffDays*24)) + "h";
        } else if (diffHours>=1){
            return String.valueOf(diffHours) + "h " + String.valueOf(diffMinutes - (diffHours*60)) + "m";
        } else if (diffSeconds > 0) //for debuging
            return String.valueOf(diffSeconds) + "s ";
        else return String.valueOf(diffMinutes) + "m ";
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
                    deadline = getDeadline(date, time);
                    labels = newDeadline.getString(5);
                    deadlines.remove(position);
                    deadlines.add(position, new Deadline(String.valueOf(position), summary, date, time, deadline, labels));
                }
                else {
                    newDeadline.moveToLast();
                    id = newDeadline.getString(1);
                    summary = newDeadline.getString(2);
                    date = newDeadline.getString(3);
                    time = newDeadline.getString(4);
                    deadline = getDeadline(date, time);
                    labels = newDeadline.getString(5);
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DeadlineAdapter.mViewHolder){
            final int index = viewHolder.getAdapterPosition();

            final Deadline deletedDeadline = deadlines.get(index);
            Toast.makeText(getContext(), String.valueOf(deadlines.get(index).getId()), Toast.LENGTH_SHORT).show();

            deadlineAdapter.removeDeadline(index);

            DbActivity db = new DbActivity(getContext());
            //Cursor swipedDeadline = db.getAllData(); // change date string to send it to history
            //swipedDeadline.moveToPosition(index);

//            db.deleteData(String.valueOf(index));


            Snackbar snackbar = Snackbar.make(rootRecyclerViewLayout, "Completed", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            FrameLayout snackBarView = (FrameLayout) snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
            params.setMargins(params.leftMargin,
                    params.topMargin,
                    params.rightMargin,
                    params.bottomMargin + 10);

            snackBarView.getChildAt(0).setLayoutParams(params);
            snackbarView.setMinimumHeight(145);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deadlineAdapter.restoreDeadline(deletedDeadline, index);
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.RED400));
            snackbar.show();
        }
    }
}