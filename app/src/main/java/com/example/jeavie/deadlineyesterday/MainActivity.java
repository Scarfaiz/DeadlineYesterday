package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jeavie.deadlineyesterday.abilities.AddTaskActivity;
import com.example.jeavie.deadlineyesterday.abilities.EditTaskActivity;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;
import com.example.jeavie.deadlineyesterday.adapters.ViewPagerAdapter;
import com.example.jeavie.deadlineyesterday.drawer.TagsActivity;
import com.example.jeavie.deadlineyesterday.fragments.HistoryFragment;
import com.example.jeavie.deadlineyesterday.fragments.HomeFragment;
import com.example.jeavie.deadlineyesterday.fragments.RecyclerViewFragment;

import java.util.List;
import java.util.Locale;

//TODO: vector images +
//TODO: SQLite database +

//TODO: history activity - delete full data btn +, clear history - snackbar: cancel, return to uncompleted?
//TODO: notifications activity
//TODO: about activity
//TODO: vector icon
//TODO: upd time in recyclerView or dates or smth?
//TODO: sort by order/deadlines/tags?


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int INTENT_REQUEST_CODE = 1;
    public final static int INTENT_REQUEST_CODE_TWO = 2;
    public static int INTENT_RESULT_CODE = 1;
    public static int INTENT_RESULT_CODE_TWO = 2;
    public final static int INTENT_EMPTY_CODE = 0;

    //Swiping
    private boolean mSwiping = false; // detects if user is swiping on ACTION_UP
    private boolean mItemPressed = false; // Detects if user is currently holding down a view

    private ListView listView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    List<Deadline> list;
    String summary, getData, getTime;

    public static FloatingActionButton addDeadline;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

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

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_deadlines:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_history:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        addDeadline = findViewById(R.id.addTask);
        addDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page ","" + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                switch (position) {
                    case 0:
                        addDeadline.show();
                        break;
                    case 1:
                        addDeadline.show();
                        break;
                    case 3:
                        addDeadline.hide();
                        break;
                    default:
                        addDeadline.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "");
        adapter.addFrag(new RecyclerViewFragment(), "");
        adapter.addFrag(new HistoryFragment(), "");
        viewPager.setAdapter(adapter);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.labels:
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

                        if (deltaX > v.getWidth() / 4) { // swipe to right
                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(v.getWidth()/4);

                            DbActivity db = new DbActivity(getApplicationContext());
                            int i = listView.getPositionForView(v);
                            String a = list.get(i).getId();
                            Cursor newDeadline = db.getData(a);
                            if(newDeadline.moveToFirst()) {
                                String id = newDeadline.getString(0);
                                summary=newDeadline.getString(1);
                                getData=newDeadline.getString(2);
                                getTime=newDeadline.getString(3);
                                String deadline=newDeadline.getString(4);
                                String tags=newDeadline.getString(5);
                                boolean isInserted = db.updateData(id, id, summary, getData, getTime, deadline, tags, "history");
                                if (isInserted)
                                    Toast.makeText(getApplicationContext(), "Deadline completed", Toast.LENGTH_SHORT).show();
                                list.remove(i);
                                //deadlineActivityAdapter.notifyDataSetChanged();
                            } else {
                                //code, if needed, to handle no row being found.
                            }



                            return true;
                        }
                        else if (deltaX < -1 * (v.getWidth() / 4)) { // swipe to left

                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;

                            v.animate().setDuration(300).translationX(-v.getWidth()/4);

                            DbActivity db = new DbActivity(getApplicationContext());
                            int i = listView.getPositionForView(v);
                            String a = list.get(i).getId();
                            Cursor newDeadline = db.getData(a);
                            if(newDeadline.moveToFirst()) {
                                String id = newDeadline.getString(0);
                                summary=newDeadline.getString(1);
                                getData=newDeadline.getString(2);
                                getTime=newDeadline.getString(3);
                                String deadline=newDeadline.getString(4);
                                String tags=newDeadline.getString(5);
                                boolean isInserted = db.updateData(id, id, summary, getData, getTime, deadline, tags, "history");
                                if (isInserted)
                                    Toast.makeText(getApplicationContext(), "Deadline completed", Toast.LENGTH_SHORT).show();
                                list.remove(i);
                                //deadlineActivityAdapter.notifyDataSetChanged();
                            } else {
                                //code, if needed, to handle no row being found.
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

                        DbActivity db = new DbActivity(getApplicationContext());
                        int i = listView.getPositionForView(v);
                        String a = list.get(i).getId();
                        Cursor newDeadline = db.getData(a);
                        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                        if(newDeadline.moveToFirst()) {
                            String id = newDeadline.getString(0);
                            Intent intent = new Intent();
                            intent.putExtra("number", id);
                            intent.setClass(MainActivity.this, EditTaskActivity.class);
                            startActivityForResult(intent, INTENT_REQUEST_CODE_TWO);
                        } else {
                            newDeadline.moveToFirst();
                            Toast.makeText(getApplicationContext(), "wat", Toast.LENGTH_SHORT).show();
                        }
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