package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//TODO: upd time in listview?
//TODO: history activity
//TODO: clear history - snackbar: cancel

//TODO: week activity
//TODO: notifications settings
//TODO: about activity
//TODO: sort by order/deadlines
//TODO: vector images

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int INTENT_REQUEST_CODE = 1;
    public final static int INTENT_REQUEST_CODE_TWO = 2;
    public final static int INTENT_REQUEST_CODE_THREE = 3;
    public static int INTENT_RESULT_CODE = 1;
    public static int INTENT_RESULT_CODE_TWO = 2;
    public static int INTENT_RESULT_CODE_THREE = 3;
    public final static int INTENT_EMPTY_CODE = 0;
    public final static String INTENT_POSITION = "position";

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view

    private ListView listView;
    DeadlineActivityAdapter deadlineActivityAdapter;
    int position;
    List<DeadlineActivity> list;
    String summary, getData, getTime;
    ArrayList <String> tagsArrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);// language configuration
        String lang = getResources().getConfiguration().locale.getDisplayLanguage(Locale.CHINESE);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        listView = findViewById(R.id.listDeadlines);

        TextView emptyText = findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

        list = new ArrayList<>();
        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list, mTouchListener);
        listView.setAdapter(deadlineActivityAdapter);

//        final Handler handler = new Handler();
//        handler.postDelayed( new Runnable() {
//            @Override
//            public void run() {
//                int i = listView.getCount();
//                if (i>0){
//                    list = changeDeadline(list);
//                    deadlineActivityAdapter.notifyDataSetChanged();
//                    handler.postDelayed( this, 1000 );
//                }
//
//            }
//        }, 1000 );

//        Thread timer = new Thread(){
//            @Override
//            public void run (){
//                try{
//                    while (!isInterrupted()){
//                        Thread.sleep(1000);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                for (int i = 0; i<=list.size(); i++){
//                                    position = i;
//                                    String s = list.get(position).getSummary();
//                                    String d = list.get(position).getDate();
//                                    String t = list.get(position).getTime();
//                                    String dd = getDeadline(d, t);
//                                    ArrayList tgarr = list.get(position).getTagsArrList();
//                                    String tg = getTags(tgarr);
//                                    list.remove(position);
//                                    list.add(new DeadlineActivity(s, getData, getTime, dd, tg, tgarr));
//                                    deadlineActivityAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//                    }
//                }
//                catch (InterruptedException e) {
//
//                }
//            }
//        };
//        timer.start();

    }

//    public List<DeadlineActivity> changeDeadline(List<DeadlineActivity> arrayList){
//        for (int i = listView.getPositionForView(listView); i <= arrayList.size(); i++){
//            String s = list.get(i).getSummary();
//            String d = list.get(i).getDate();
//            String t = list.get(i).getTime();
//            String dd = getDeadline(d, t);
//            ArrayList tgarr = list.get(i).getTagsArrList();
//            String tg = getTags(tgarr);
//            list.remove(i);
//            list.add(i, new DeadlineActivity(s, d, t, dd, tg, tgarr));
//            Toast.makeText(this, "UPD", Toast.LENGTH_SHORT).show();
//        }
//        return arrayList;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_RESULT_CODE){
            if(resultCode == INTENT_RESULT_CODE) {
                summary = data.getStringExtra("summary");
                getData = data.getStringExtra("date");
                getTime = data.getStringExtra("time");
                tagsArrList = data.getStringArrayListExtra("tags");
                String tags = getTags(tagsArrList);
                String deadline = getDeadline(getData, getTime);
                list.add(new DeadlineActivity(summary, getData, getTime, deadline, tags, tagsArrList));
                deadlineActivityAdapter.notifyDataSetChanged();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if (requestCode == INTENT_RESULT_CODE_TWO){
            if (resultCode == INTENT_RESULT_CODE_TWO) {
                Toast.makeText(this, "Deadline upd", Toast.LENGTH_SHORT).show();
                summary = data.getStringExtra("changedSummary");
                getData = data.getStringExtra("changedDate");
                getTime = data.getStringExtra("changedTime");
                tagsArrList = data.getStringArrayListExtra("changedTags");
                String tags = getTags(tagsArrList);
                String deadline = getDeadline(getData, getTime);
                position = data.getIntExtra(INTENT_POSITION, -1);
                list.remove(position);
                list.add(position, new DeadlineActivity(summary, getData, getTime, deadline, tags, tagsArrList));
                deadlineActivityAdapter.notifyDataSetChanged();
            }
        }
    }

    public String getDeadline (String date, String time){
        String format = date + " " + time;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh : mm a");
        Date cal1 = new Date();
        Date cal2 = null;
        try {
            cal2 = df.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = cal2.getTime() - cal1.getTime();

        long diffSeconds = diff/(1000);

        long diffMinutes = diff / (60 * 1000);

        long diffHours = diff / (60 * 60 * 1000);

        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays>1 && diffHours > 24){
            return String.valueOf(diffDays) + " days " + String.valueOf(diffHours - (diffDays*24)) + " hrs";
        } else if (diffDays==1 && diffHours > 24) {
            return String.valueOf(diffDays) + " day " + String.valueOf(diffHours - (diffDays*24)) + " hrs";
        }else if (diffHours>1){
            return String.valueOf(diffHours) + " hrs";
        }else if (diffHours==1){
            return String.valueOf(diffHours) + " hour";
        } else if (diffSeconds > 0)
            return String.valueOf(diffSeconds) + " sec";
        else return String.valueOf(diffMinutes) + " min";
    }

    public String getTags(ArrayList<String> tags){
        String parsedTags = String.valueOf(tags).replace("[", "").replace("]", "");
        return parsedTags;
    }

//    public Intent getIntent(Activity from, int i) {
//        Intent intent = new Intent();
//        intent.setClass(from, HistoryActivity.class);
//        intent.putExtra("summary_data_to_history", list.get(i).getSummary());
//        intent.putExtra("date_data_to_history", list.get(i).getDate());
//        intent.putExtra("time_data_to_history", list.get(i).getTime());
//        intent.putStringArrayListExtra("tags_data_to_history", list.get(i).getTagsArrList());
//        intent.putExtra(INTENT_POSITION, i);
//        setResult(INTENT_RESULT_CODE_THREE, intent);
//        return intent;
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.toWeek:
                startActivity(new Intent(this, WeekActivity.class));
                return true;

            case R.id.history:
                startActivity(new Intent(this, HistoryActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.tags:
                startActivity(new Intent(this, TagsActivity.class));
                return true;

            case R.id.notifications:
//                startActivity(new Intent(this, HistoryActivity.class));
                return true;

            case R.id.info:
//                startActivity(new Intent(this, HistoryActivity.class));
                return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener()
    {
        float mDownX;
        private int mSwipeSlop = -1;
        boolean swiped;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(MainActivity.this).getScaledTouchSlop();
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
                            //data to history
//                            intentHistory = new Intent();
//                            intentHistory.setClass(MainActivity.this, HistoryActivity.class);
//                            intentHistory.putExtra("summary_data_to_history", list.get(i).getSummary());
//                            intentHistory.putExtra("date_data_to_history", list.get(i).getDate());
//                            intentHistory.putExtra("time_data_to_history", list.get(i).getTime());
//                            intentHistory.putStringArrayListExtra("tags_data_to_history", list.get(i).getTagsArrList());
//                            intentHistory.putExtra(INTENT_POSITION, i);
//                            setResult(INTENT_RESULT_CODE_THREE, intentHistory);

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

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, EditTaskActivity.class);
                        intent.putExtra("summary_data", list.get(i).getSummary());
                        intent.putExtra("date_data", list.get(i).getDate());
                        intent.putExtra("time_data", list.get(i).getTime());
                        intent.putStringArrayListExtra("tags_data", list.get(i).getTagsArrList());
                        intent.putExtra(INTENT_POSITION, i);
                        startActivityForResult(intent, INTENT_REQUEST_CODE_TWO);

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