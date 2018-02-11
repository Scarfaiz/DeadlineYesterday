package com.example.jeavie.deadlineyesterday.bin;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ConstraintLayout constraintLayout;
    List<Deadline> list;
    String summaryData, dateData, timeData;

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view

    DbActivity db;
    Cursor fullData;
    boolean undo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        constraintLayout = findViewById(R.id.historyActivity);

        db = new DbActivity(this);
        int i = 1;
        fullData = db.getAllData();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                list = new ArrayList<>();
                do {
                    String check = fullData.getString(7);
                    if (check.startsWith("hi")){
                        String id = fullData.getString(1);
                        summaryData=fullData.getString(2);
                        dateData=fullData.getString(3);
                        timeData=fullData.getString(4);
                        String deadline=fullData.getString(5);
                        String tags=fullData.getString(6);
                        list.add(new Deadline(id, summaryData, dateData, timeData, deadline,
                                tags));
                        i++;
                    }
                } while (fullData.moveToNext());
            }
        }
        else list = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = findViewById(R.id.listCompletedDeadlines);

        TextView emptyText = findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

//        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list, mTouchListener);
//        listView.setAdapter(deadlineActivityAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                return true;

            case R.id.clearHistory:
                DbActivity db = new DbActivity(getApplicationContext());
                fullData = db.getAllData();

                if (fullData.getCount() == 0) return true;
//                deadlineActivityAdapter.notifyDataSetChanged();
                Snackbar snackbar = Snackbar.make(constraintLayout, "History is cleaned", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undo = true;
                                Toast.makeText(getApplicationContext(),"CLICKED", Toast.LENGTH_SHORT).show();
                                DbActivity db = new DbActivity(getApplicationContext());
                                int i = 1;
                                fullData = db.getAllData();
                                if (fullData.getCount() > 0){
                                    if (fullData.moveToFirst()) {
                                        list = new ArrayList<>();
                                        do {
                                            String check = fullData.getString(7);
                                            if (check.startsWith("hi")){
                                                String id = fullData.getString(1);
                                                summaryData=fullData.getString(2);
                                                dateData=fullData.getString(3);
                                                timeData=fullData.getString(4);
                                                String deadline=fullData.getString(5);
                                                String tags=fullData.getString(6);
                                                list.add(new Deadline(id, summaryData, dateData, timeData, deadline,
                                                        tags));
                                                i++;
                                            }
                                        } while (fullData.moveToNext());
                                    }
                                }
                                else list = new ArrayList<>();
                                ListView view = findViewById(R.id.listCompletedDeadlines);
//                                view.setAdapter(deadlineActivityAdapter);
//                                deadlineActivityAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"CLICKED", Toast.LENGTH_SHORT).show();
                            }
                        });
                snackbar.show();
                if (undo) return true;
                else {
                    Toast.makeText(getApplicationContext(),"WAT", Toast.LENGTH_SHORT).show();
                    list.clear();
                    DbActivity dtb = new DbActivity(getApplicationContext());
                    fullData = dtb.getAllData();
                    if (fullData.getCount() > 0){
                        if (fullData.moveToFirst()) {
                            do {
                                String check = fullData.getString(7);
                                if (check.startsWith("hi")){
                                    String id = fullData.getString(1);
                                    int done = dtb.deleteData(id);
                                }
                            } while (fullData.moveToNext());
                        }
                    }
                    else if (fullData.getCount() == 0) {
                        db.getAllData();
                        db.close();
                        getApplicationContext().deleteDatabase(DbActivity.DB_NAME);
                    }
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener()
    {
        float mDownX;
        private int mSwipeSlop = -1;
        boolean swiped;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(HistoryActivity.this).getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) return false; // Doesn't allow swiping two items at same time
                    mItemPressed = true;
                    mDownX = event.getX();
                    swiped = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);

                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true; // tells if user is actually swiping or just touching in sloppy manner
                            listView.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (mSwiping && !swiped) { // Need to make sure the user is both swiping and has not already completed a swipe action (hence mSwiping and swiped)
                        v.setTranslationX((x - mDownX)); // moves the view as long as the user is swiping and has not already swiped

                        if (deltaX > v.getWidth() / 4) { // swipe to right
                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(v.getWidth()/4);

                            int i = listView.getPositionForView(v);
                            String a = list.get(i).getId();
                            Cursor newDeadline = db.getData(a);
                            if(newDeadline.moveToFirst()) {
                                String id = newDeadline.getString(0);
                                int done = db.deleteData(id);
                                Toast.makeText(getApplicationContext(), "Deadline deleted", Toast.LENGTH_SHORT).show();
                                list.remove(i);
                                //deadlineActivityAdapter.notifyDataSetChanged();
                            } else {
                                //code, if needed, to handle no row being found.
                            }

                            Cursor cursor = db.getAllData();
                            if (cursor.getCount() == 0){
                                db.close();
                                getApplicationContext().deleteDatabase(DbActivity.DB_NAME);
                            }

                            return true;
                        }
                        else if (deltaX < -1 * (v.getWidth() / 4)) { // swipe to left

                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(-v.getWidth()/4);

                            int i = listView.getPositionForView(v);
                            String a = list.get(i).getId();
                            Cursor newDeadline = db.getData(a);
                            if(newDeadline.moveToFirst()) {
                                String id = newDeadline.getString(0);
                                int done = db.deleteData(id);
                                Toast.makeText(getApplicationContext(), "Deadline deleted", Toast.LENGTH_SHORT).show();
                                list.remove(i);
                                //deadlineActivityAdapter.notifyDataSetChanged();
                            } else {
                                //code, if needed, to handle no row being found.
                            }

                            Cursor cursor = db.getAllData();

                            if (cursor.getCount() == 0){
                                db.close();
                                getApplicationContext().deleteDatabase(DbActivity.DB_NAME);
                            }

                            return true;
                        }
                    }

                }
                break;
                case MotionEvent.ACTION_UP: {
                    if (mSwiping) { // if the user was swiping, don't go to the and just animate the view back into position
                        v.animate().setDuration(300).translationX(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mSwiping = false;
                                mItemPressed = false;
                                listView.setEnabled(true);
                            }
                        });
                    }
                    else { // user was not swiping; registers as a click

                        int i = listView.getPositionForView(v);

                        //set item click "animation"
                        ColorDrawable[] color = {
                                new ColorDrawable(getColor(R.color.GREY)),
                                new ColorDrawable(getColor(R.color.LIGHT))
                        };
                        TransitionDrawable trans = new TransitionDrawable(color);
                        v.setBackground(trans);
                        trans.startTransition(1000); // duration 2 seconds

                        // Go back to the default background color of Item
                        ColorDrawable[] color2 = {
                                new ColorDrawable(getColor(R.color.LIGHT)),
                                new ColorDrawable(getColor(R.color.WHITE))
                        };
                        TransitionDrawable trans2 = new TransitionDrawable(color2);
                        v.setBackground(trans2);
                        trans2.startTransition(1000); // duration 2 seconds

                        mItemPressed = false;
                        listView.setEnabled(true);

                        return true;
                    }
                }
                default:
                    return false;
            }
            return true;
        }
    };

}