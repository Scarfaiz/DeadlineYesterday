package com.example.jeavie.deadlineyesterday;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.memfis19.cadar.CalendarController;
import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.event.CalendarPrepareCallback;
import io.github.memfis19.cadar.event.DisplayEventCallback;
import io.github.memfis19.cadar.event.OnDayChangeListener;
import io.github.memfis19.cadar.event.OnMonthChangeListener;
import io.github.memfis19.cadar.internal.process.EventsProcessor;
import io.github.memfis19.cadar.internal.process.EventsProcessorCallback;
import io.github.memfis19.cadar.internal.utils.DateUtils;
import io.github.memfis19.cadar.settings.ListCalendarConfiguration;
import io.github.memfis19.cadar.settings.MonthCalendarConfiguration;
import io.github.memfis19.cadar.view.ListCalendar;
import io.github.memfis19.cadar.view.MonthCalendar;

public class MonthActivity extends AppCompatActivity implements CalendarPrepareCallback {

    FloatingActionButton addTask;

    private MonthCalendar monthCalendar;
    private ListCalendar listCalendar;

    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);


//        events.add(new EventModel());

        monthCalendar = (MonthCalendar) findViewById(R.id.monthCalendar);
        listCalendar = (ListCalendar) findViewById(R.id.listCalendar);

        MonthCalendarConfiguration.Builder builder = new MonthCalendarConfiguration.Builder();
        ListCalendarConfiguration.Builder listBuilder = new ListCalendarConfiguration.Builder();

        monthCalendar.setCalendarPrepareCallback(this);
        listCalendar.setCalendarPrepareCallback(this);

        builder.setDisplayPeriod(Calendar.MONTH, 3);
        listBuilder.setDisplayPeriod(Calendar.MONTH, 3);
//        builder.setEventsProcessor(new MonthCustomProcessor());
//        listBuilder.setEventsProcessor(new ListCustomEventProcessor());

        monthCalendar.prepareCalendar(builder.build());
        listCalendar.prepareCalendar(listBuilder.build());

        monthCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                listCalendar.setSelectedDay(DateUtils.setTimeToMidnight((Calendar) calendar.clone()), false);
            }
        });
        monthCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                listCalendar.setSelectedDay(DateUtils.setTimeToMonthStart((Calendar) calendar.clone()), false);
            }
        });

        listCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                monthCalendar.setSelectedDay(calendar, false);
            }
        });

        listCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                monthCalendar.setSelectedDay(calendar, true);
            }
        });

        addTask=(FloatingActionButton)findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MonthActivity.this, AddTaskActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_month, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.addTask:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private Handler waitHandler = new Handler();

    class ListCustomEventProcessor extends EventsProcessor<Pair<Calendar, Calendar>, List<Event>> {

        public ListCustomEventProcessor() {
            super(false, null, true);
        }

        @Override
        protected void processEventsAsync(final Pair<Calendar, Calendar> target, final EventsProcessorCallback<Pair<Calendar, Calendar>, List<Event>> eventsProcessorCallback) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final List<Event> events = new ArrayList<>();
                    for (int i = 0; i < 5; ++i) {
//                        events.add(new EventModel());
                    }
                    waitHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eventsProcessorCallback.onEventsProcessed(target, events);
                        }
                    }, 100);
                }
            }, 3000);
        }
    }

    class MonthCustomProcessor extends EventsProcessor<Calendar, SparseArray<List<Event>>> {

        public MonthCustomProcessor() {
            super(false, null, true);
        }

        @Override
        protected void processEventsAsync(final Calendar target, final EventsProcessorCallback<Calendar, SparseArray<List<Event>>> eventsProcessorCallback) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Event> events = new ArrayList<>();
                    for (int i = 0; i < 5; ++i) {
//                        events.add(new EventModel());
                    }
                    final SparseArray<List<Event>> calendarEvents = new SparseArray<>();

                    Calendar temp = DateUtils.getCalendarInstance();
                    for (Event event : events) {
                        temp.setTime(event.getEventStartDate());

                        if (!DateUtils.isSameMonth(temp, target)) continue;

                        List<Event> tmpEvents = calendarEvents.get(temp.get(Calendar.DAY_OF_MONTH), new ArrayList<Event>());
                        tmpEvents.add(event);

                        calendarEvents.put(temp.get(Calendar.DAY_OF_MONTH), tmpEvents);
                    }
                    events.clear();

                    waitHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eventsProcessorCallback.onEventsProcessed(target, calendarEvents);
                        }
                    }, 100);
                }
            }, 3000);
        }
    }

    @Override
    public void onCalendarReady(CalendarController calendar) {
        if (calendar == monthCalendar) {
            monthCalendar.displayEvents(new ArrayList<>(events), new DisplayEventCallback<Calendar>() {
                @Override
                public void onEventsDisplayed(Calendar month) {

                }
            });
        } else if (calendar == listCalendar) {
            listCalendar.displayEvents(new ArrayList<>(events), new DisplayEventCallback<Pair<Calendar, Calendar>>() {
                @Override
                public void onEventsDisplayed(Pair<Calendar, Calendar> period) {

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        monthCalendar.releaseCalendar();
        listCalendar.releaseCalendar();
    }

}

//import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.CalendarMode;
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
//import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import java.util.Calendar;
//
//public class MonthActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_month);
//
//        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.monthView);
//
//        materialCalendarView.state().edit()
//                .setFirstDayOfWeek(Calendar.MONDAY)
//                .setMinimumDate(CalendarDay.from(2000, 1, 1))
//                .setMaximumDate(CalendarDay.from(2200, 12, 31))
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .commit();
//
//        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                Toast.makeText(MonthActivity.this, "" + date, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_month, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case R.id.addTask:
//                startActivity(new Intent(this, AddTaskActivity.class));
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
