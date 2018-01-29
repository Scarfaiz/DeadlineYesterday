package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
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
    ArrayList<String> tagsData;

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
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

        list = new ArrayList<>();
        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list, mTouchListener);
        listView.setAdapter(deadlineActivityAdapter);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MainActivity.INTENT_REQUEST_CODE_THREE){
//            Toast.makeText(this, "Well done", Toast.LENGTH_SHORT).show();
//            if(resultCode == MainActivity.INTENT_RESULT_CODE_THREE) {
//                Intent intent = getIntent();
//                summaryData = intent.getStringExtra("summary_data_to_history");
//                dateData = intent.getStringExtra("date_data_to_history");
//                timeData = intent.getStringExtra("time_data_to_history");
//                tagsData = intent.getStringArrayListExtra("tags_data_to_history");
//                String tags = getTags(tagsData);
//                list.add(new DeadlineActivity(summaryData, dateData, timeData, getResources().getString(R.string.completed), tags, tagsData));
//                deadlineActivityAdapter.notifyDataSetChanged();
//                super.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//    }

    public String getTags(ArrayList<String> tags){
        String parsedTags = String.valueOf(tags).replace("[", "").replace("]", "");
        return parsedTags;
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

                            int i = listView.getPositionForView(v);
                            list.remove(i);
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
