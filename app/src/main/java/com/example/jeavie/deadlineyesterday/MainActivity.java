package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Locale;

//TODO: vector images +
//TODO: SQLite database +-

//TODO: history activity - delete full data btn, clear history - snackbar: cancel, return to uncompleted?
//TODO: notifications settings activity
//TODO: about activity
//TODO: vector icon
//TODO: upd time in listview or dates or smth?
//TODO: sort by order/deadlines/tags?
//TODO: week activity - all?


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int INTENT_REQUEST_CODE = 1;
    public final static int INTENT_REQUEST_CODE_TWO = 2;
    public static int INTENT_RESULT_CODE = 1;
    public static int INTENT_RESULT_CODE_TWO = 2;
    public final static int INTENT_EMPTY_CODE = 0;
    public static Integer listNumber = 0;
    public static Integer dataNumber = 1;
    public static Integer editNumber = 1;

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view

    private ListView listView;
    DeadlineActivityAdapter deadlineActivityAdapter;
    List<DeadlineActivity> list;
    String summary, getData, getTime;

    DbActivity db;
    Cursor fullData;
    boolean full;
    public List<Integer> del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        db = new DbActivity(this);

        fullData = db.getAllData();
        if (fullData.getCount() > 0){
            if (fullData.moveToFirst()) {
                list = new ArrayList<>();
            do {
                String check = fullData.getString(7);
                if (check.startsWith("li")){
                    summary=fullData.getString(2);
                    getData=fullData.getString(3);
                    getTime=fullData.getString(4);
                    String deadline=fullData.getString(5);
                    String tags=fullData.getString(6);
                    list.add(new DeadlineActivity(String.valueOf(dataNumber - 1), summary, getData, getTime, deadline,
                         tags));
                    full = true;
                    listNumber++;
                }
            } while (fullData.moveToNext());
            }
        }
        if (!full) list = new ArrayList<>();

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

        deadlineActivityAdapter = new DeadlineActivityAdapter(this, list, mTouchListener);
        listView.setAdapter(deadlineActivityAdapter);

        del = new ArrayList<>();

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

//    public String[] setTags(ArrayList<String> tags){
//        String [] parsed = tags.toArray(new String[0]);
//        return parsed;
//    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_RESULT_CODE){
            if(resultCode == INTENT_RESULT_CODE) {
                Cursor newDeadline = db.getAllData();
                newDeadline.moveToLast();
                summary=newDeadline.getString(2);
                getData=newDeadline.getString(3);
                getTime=newDeadline.getString(4);
                String deadline=newDeadline.getString(5);
                String tags=newDeadline.getString(6);
                list.add(new DeadlineActivity(String.valueOf(dataNumber), summary, getData, getTime, deadline,
                         tags));
                dataNumber++;
                deadlineActivityAdapter.notifyDataSetChanged();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if (requestCode == INTENT_RESULT_CODE_TWO){
            if (resultCode == INTENT_RESULT_CODE_TWO) {
                Cursor newDeadline = db.getData(String.valueOf(editNumber + 1));
                String id = newDeadline.getString(0);
                summary=newDeadline.getString(1);
                getData=newDeadline.getString(2);
                getTime=newDeadline.getString(3);
                String deadline=newDeadline.getString(4);
                String tags=newDeadline.getString(5);
                list.add(Integer.valueOf(id), new DeadlineActivity(id, summary, getData, getTime, deadline, tags));
                list.remove(Integer.valueOf(id) + 1);
                deadlineActivityAdapter.notifyDataSetChanged();
            }
        }
    }

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

                        if (deltaX > v.getWidth() / 3) { // swipe to right
                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(v.getWidth()/3);

                            int i = listView.getPositionForView(v);
                            String a = list.get(i).getId();
                            Cursor newDeadline = db.getData(a);
                            String id = newDeadline.getString(0);
                            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                            summary=newDeadline.getString(1);
                            getData=newDeadline.getString(2);
                            getTime=newDeadline.getString(3);
                            String deadline=newDeadline.getString(4);
                            String tags=newDeadline.getString(5);
                            boolean isInserted = db.updateData(id, id, summary, getData, getTime, deadline, tags, "history");
                            if (isInserted)
                                Toast.makeText(getApplicationContext(), "Deadline completed", Toast.LENGTH_SHORT).show();
                            list.remove(i);
                            deadlineActivityAdapter.notifyDataSetChanged();

                            return true;
                        }
                        else if (deltaX < -1 * (v.getWidth() / 3)) { // swipe to left

                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(-v.getWidth()/3);

                            int i = listView.getPositionForView(v);
                            String a = list.get(i).getId();
                            Cursor newDeadline = db.getData(a);
                            String id = newDeadline.getString(0);
                            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                            summary=newDeadline.getString(1);
                            getData=newDeadline.getString(2);
                            getTime=newDeadline.getString(3);
                            String deadline=newDeadline.getString(4);
                            String tags=newDeadline.getString(5);
                            boolean isInserted = db.updateData(id, id, summary, getData, getTime, deadline, tags, "history");
                            if (isInserted)
                                Toast.makeText(getApplicationContext(), "Deadline completed", Toast.LENGTH_SHORT).show();
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

                        editNumber = listView.getPositionForView(v);
                        String a = list.get(editNumber).getId();
                        Cursor newDeadline = db.getData(a);
                        String id = newDeadline.getString(0);
                        Intent intent = new Intent();
                        intent.putExtra("number", id);
                        intent.setClass(MainActivity.this, EditTaskActivity.class);
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