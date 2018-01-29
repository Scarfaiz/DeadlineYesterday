package com.example.jeavie.deadlineyesterday;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.memfis19.cadar.CalendarController;
import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.event.CalendarPrepareCallback;
import io.github.memfis19.cadar.event.DisplayEventCallback;
import io.github.memfis19.cadar.event.OnEventClickListener;
import io.github.memfis19.cadar.internal.process.EventsProcessor;
import io.github.memfis19.cadar.internal.process.EventsProcessorCallback;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.EventDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.MonthDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.WeekDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.EventDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.MonthDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.WeekDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.model.ListItemModel;
import io.github.memfis19.cadar.settings.ListCalendarConfiguration;
import io.github.memfis19.cadar.view.ListCalendar;

public class WeekActivity extends AppCompatActivity implements CalendarPrepareCallback {

    private ListCalendar listCalendar;

    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_week);

        Toolbar toolbar = findViewById(R.id.toolbar_week);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listCalendar = findViewById(R.id.listCalendar);

        ListCalendarConfiguration.Builder listBuilder = new ListCalendarConfiguration.Builder();

        listBuilder.setDisplayPeriod(Calendar.MONTH, 3);

        listCalendar.setCalendarPrepareCallback(this);
        listCalendar.prepareCalendar(listBuilder.build());

        FloatingActionButton addTask=findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeekActivity.this, AddTaskActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_week, menu);
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


    @Override
    public void onCalendarReady(CalendarController calendar) {
        listCalendar.displayEvents(events, new DisplayEventCallback<Pair<Calendar, Calendar>>() {
            @Override
            public void onEventsDisplayed(Pair<Calendar, Calendar> period) {
                Log.d("", "");
                listCalendar.refresh();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listCalendar.releaseCalendar();
    }
}