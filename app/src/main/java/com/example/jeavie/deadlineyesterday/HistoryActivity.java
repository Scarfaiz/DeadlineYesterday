package com.example.jeavie.deadlineyesterday;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
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

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    DeadlineActivityAdapter deadlineActivityAdapter;
    List<DeadlineActivity> list;
    String summaryData, dateData, timeData;

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view

    DbActivity db;
    Cursor fullData;
    boolean full;
    public static int listNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DbActivity(this);

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
                        list.add(new DeadlineActivity(id, summaryData, dateData, timeData, deadline,
                                tags));
                        full = true;
                        listNumber++;
                    }
                } while (fullData.moveToNext());
            }
        }
        if (!full) list = new ArrayList<>();

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

        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list, mTouchListener);
        listView.setAdapter(deadlineActivityAdapter);
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
                //
                return true;

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

                        if (deltaX > v.getWidth() / 2) { // swipe to right
                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(v.getWidth()/2);

                            int i = listView.getPositionForView(v) + 1;
                            int j = i;
                            Cursor checkedDeadline = db.getAllData();
                            checkedDeadline.moveToFirst();
                            int k = 0, l = 0;
                            do{
                                String check = checkedDeadline.getString(6);
                                if (check.startsWith("li")) k++;
                                //if (del.contains(k+1)) l++;
                                if (k==j) break;
                            }while (checkedDeadline.moveToNext());
                            Toast.makeText(getApplicationContext(), "DELETE " + String.valueOf(k) + " + " + String.valueOf(l), Toast.LENGTH_SHORT).show();
                            //del.add(k + l);
                            int done = db.deleteData(String.valueOf(k + l));
                            //Toast.makeText(getApplicationContext(), "Deadline deleted", Toast.LENGTH_SHORT).show();
                            list.remove(j-1);
                            deadlineActivityAdapter.notifyDataSetChanged();

                            return true;
                        }
                        else if (deltaX < -1 * (v.getWidth() / 2)) { // swipe to left

                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(-v.getWidth()/2);

                            int i = listView.getPositionForView(v);

                            list.remove(i);
                            deadlineActivityAdapter.notifyDataSetChanged();

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
                                new ColorDrawable(getColor(R.color.grey)),
                                new ColorDrawable(getColor(R.color.dark_dark_grey))
                        };
                        TransitionDrawable trans = new TransitionDrawable(color);
                        v.setBackground(trans);
                        trans.startTransition(1000); // duration 2 seconds

                        // Go back to the default background color of Item
                        ColorDrawable[] color2 = {
                                new ColorDrawable(getColor(R.color.dark_dark_grey)),
                                new ColorDrawable(getColor(R.color.the_darkest_grey))
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
