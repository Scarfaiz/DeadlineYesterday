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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;

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

    FloatingActionButton addTask;

    private ListCalendar listCalendar;

    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_week);

        //events.add(new EventModel());

        listCalendar = (ListCalendar) findViewById(R.id.listCalendar);

        ListCalendarConfiguration.Builder listBuilder = new ListCalendarConfiguration.Builder();

        EventDecoratorFactory eventDecoratorFactory = new EventDecoratorFactory() {
            @Override
            public EventDecorator createEventDecorator(View parent) {
                return new EventDecoratorImpl(parent);
            }
        };

        WeekDecoratorFactory weekDecoratorFactory = new WeekDecoratorFactory() {
            @Override
            public WeekDecorator createWeekDecorator(View parent) {
                return new WeeDecoratorImpl(parent);
            }
        };

        MonthDecoratorFactory monthDecoratorFactory = new MonthDecoratorFactory() {
            @Override
            public MonthDecorator createMonthDecorator(View parent) {
                return new MonthDecoratorImpl(parent);
            }
        };

        listBuilder.setDisplayPeriod(Calendar.MONTH, 3);
//        listBuilder.setEventLayout(R.layout.custom_event_layout, eventDecoratorFactory);
//        listBuilder.setWeekLayout(R.layout.custom_week_title_layout, weekDecoratorFactory);
//        listBuilder.setMonthLayout(R.layout.custom_month_calendar_event_layout, monthDecoratorFactory);
//        listBuilder.setEventsProcessor(new CustomEventProcessor());

        listCalendar.setCalendarPrepareCallback(this);
        listCalendar.prepareCalendar(listBuilder.build());
        listCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onEventClick(Event event, int position) {
                Log.i("onEventClick", String.valueOf(event));
            }

            @Override
            public void onSyncClick(Event event, int position) {
                Log.i("onSyncClick", String.valueOf(event));
            }

            @Override
            public void onEventLongClick(Event event, int position) {
                Log.i("onEventLongClick", String.valueOf(event));
            }
        });
        addTask=(FloatingActionButton)findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeekActivity.this, AddTaskActivity.class));
            }
        });
    }

    private class MonthDecoratorImpl implements MonthDecorator {

        private ImageView monthBackground;
        private TextView monthTitle;
        private Custom custom;

        public MonthDecoratorImpl(View parent) {
            monthBackground = (ImageView) parent.findViewById(R.id.month_background);
            monthTitle = (TextView) parent.findViewById(R.id.month_label);
        }

        @Override
        public void onBindMonthView(View view, Calendar month) {
            monthBackground.setImageDrawable(null);

            final int backgroundId = getBackgroundId(month.get(Calendar.MONTH));
            monthTitle.setText(month.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
//            Picasso.with(monthTitle.getContext().getApplicationContext()).load(backgroundId).into(monthBackground, new Callback() {
//                @Override
//                public void onSuccess() {
//                    if (Build.VERSION.SDK_INT > 13) {
//                        monthBackground.setScrollX(0);
//                        monthBackground.setScrollY(0);
//                    }
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
        }

        @NonNull
        @Override
        public RecyclerView.OnScrollListener getScrollListener() {
            custom = new Custom();
            return custom;
        }
    }

    private class EventDecoratorImpl implements EventDecorator {

        private TextView textView;

        public EventDecoratorImpl(View parent) {
            textView = (TextView) parent.findViewById(R.id.day_title);
        }

        @Override
        public void onBindEventView(View view, Event event, ListItemModel previous, int position) {
            view.setBackgroundColor(ContextCompat.getColor(WeekActivity.this, R.color.light_grey));
            textView.setText(event.getEventTitle() + "\n" + event.getEventStartDate());
        }
    }

    private class WeeDecoratorImpl implements WeekDecorator {

        private TextView title;

        public WeeDecoratorImpl(View parent) {
            title = (TextView) parent.findViewById(io.github.memfis19.cadar.R.id.week_title);
        }

        @Override
        public void onBindWeekView(View view, Pair<Calendar, Calendar> period) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(title.getContext().getString(io.github.memfis19.cadar.R.string.calendar_week));
            stringBuilder.append("custom ");
            stringBuilder.append(period.first.get(Calendar.WEEK_OF_YEAR));
            stringBuilder.append(", ");
            stringBuilder.append(DateFormat.format("dd MMM", period.first));
            stringBuilder.append(" - ");
            stringBuilder.append(DateFormat.format("dd MMM", period.second));

            final Spannable date = new SpannableString(stringBuilder.toString());

            title.setText(date);
        }
    }

    private int getBackgroundId(int month) {
        int backgroundId = io.github.memfis19.cadar.R.drawable.bkg_12_december;

        if (month == Calendar.JANUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_01_january;
        } else if (month == Calendar.FEBRUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_02_february;
        } else if (month == Calendar.MARCH) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_03_march;
        } else if (month == Calendar.APRIL) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_04_april;
        } else if (month == Calendar.MAY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_05_may;
        } else if (month == Calendar.JUNE) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_06_june;
        } else if (month == Calendar.JULY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_07_july;
        } else if (month == Calendar.AUGUST) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_08_august;
        } else if (month == Calendar.SEPTEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_09_september;
        } else if (month == Calendar.OCTOBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_10_october;
        } else if (month == Calendar.NOVEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_11_november;
        }

        return backgroundId;
    }

    private class Custom extends RecyclerView.OnScrollListener {

        private View monthBackground;

        Custom() {
        }

        public void setMonthBackground(View monthBackground) {
            this.monthBackground = monthBackground;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (monthBackground != null) monthBackground.scrollBy(dx, (-1) * (dy / 10));
        }
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

    class CustomEventProcessor extends EventsProcessor<Pair<Calendar, Calendar>, List<Event>> {

        public CustomEventProcessor() {
            super(false, null, true);
        }

        @Override
        protected void processEventsAsync(final Pair<Calendar, Calendar> target, final EventsProcessorCallback<Pair<Calendar, Calendar>, List<Event>> eventsProcessorCallback) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; ++i) {
                        //events.add(new EventModel());
                    }
                    eventsProcessorCallback.onEventsProcessed(target, events);
                }
            }, 3000);
        }
    }
}

//public class WeekActivity extends AppCompatActivity implements CalendarPrepareCallback {
//
//    FloatingActionButton addTask;
//
//    private MonthCalendar monthCalendar;
//    private ListCalendar listCalendar;
//
//    private List<Event> events = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_week);
//
//
////        events.add(new EventModel());
//
//        monthCalendar = (MonthCalendar) findViewById(R.id.monthCalendar);
//        listCalendar = (ListCalendar) findViewById(R.id.listCalendar);
//
//        MonthCalendarConfiguration.Builder builder = new MonthCalendarConfiguration.Builder();
//        ListCalendarConfiguration.Builder listBuilder = new ListCalendarConfiguration.Builder();
//
//        monthCalendar.setCalendarPrepareCallback(this);
//        listCalendar.setCalendarPrepareCallback(this);
//
//        builder.setDisplayPeriod(Calendar.MONTH, 3);
//        listBuilder.setDisplayPeriod(Calendar.MONTH, 3);
////        builder.setEventsProcessor(new MonthCustomProcessor());
////        listBuilder.setEventsProcessor(new ListCustomEventProcessor());
//
//        monthCalendar.prepareCalendar(builder.build());
//        listCalendar.prepareCalendar(listBuilder.build());
//
//        monthCalendar.setOnDayChangeListener(new OnDayChangeListener() {
//            @Override
//            public void onDayChanged(Calendar calendar) {
//                listCalendar.setSelectedDay(DateUtils.setTimeToMidnight((Calendar) calendar.clone()), false);
//            }
//        });
//        monthCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
//            @Override
//            public void onMonthChanged(Calendar calendar) {
//                listCalendar.setSelectedDay(DateUtils.setTimeToMonthStart((Calendar) calendar.clone()), false);
//            }
//        });
//
//        listCalendar.setOnDayChangeListener(new OnDayChangeListener() {
//            @Override
//            public void onDayChanged(Calendar calendar) {
//                monthCalendar.setSelectedDay(calendar, false);
//            }
//        });
//
//        listCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
//            @Override
//            public void onMonthChanged(Calendar calendar) {
//                monthCalendar.setSelectedDay(calendar, true);
//            }
//        });
//
//        addTask=(FloatingActionButton)findViewById(R.id.addTask);
//        addTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(WeekActivity.this, AddTaskActivity.class));
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_week, menu);
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
//    private Handler waitHandler = new Handler();
//
//    class ListCustomEventProcessor extends EventsProcessor<Pair<Calendar, Calendar>, List<Event>> {
//
//        public ListCustomEventProcessor() {
//            super(false, null, true);
//        }
//
//        @Override
//        protected void processEventsAsync(final Pair<Calendar, Calendar> target, final EventsProcessorCallback<Pair<Calendar, Calendar>, List<Event>> eventsProcessorCallback) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    final List<Event> events = new ArrayList<>();
//                    for (int i = 0; i < 5; ++i) {
////                        events.add(new EventModel());
//                    }
//                    waitHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            eventsProcessorCallback.onEventsProcessed(target, events);
//                        }
//                    }, 100);
//                }
//            }, 3000);
//        }
//    }
//
//    class MonthCustomProcessor extends EventsProcessor<Calendar, SparseArray<List<Event>>> {
//
//        public MonthCustomProcessor() {
//            super(false, null, true);
//        }
//
//        @Override
//        protected void processEventsAsync(final Calendar target, final EventsProcessorCallback<Calendar, SparseArray<List<Event>>> eventsProcessorCallback) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    List<Event> events = new ArrayList<>();
//                    for (int i = 0; i < 5; ++i) {
////                        events.add(new EventModel());
//                    }
//                    final SparseArray<List<Event>> calendarEvents = new SparseArray<>();
//
//                    Calendar temp = DateUtils.getCalendarInstance();
//                    for (Event event : events) {
//                        temp.setTime(event.getEventStartDate());
//
//                        if (!DateUtils.isSameMonth(temp, target)) continue;
//
//                        List<Event> tmpEvents = calendarEvents.get(temp.get(Calendar.DAY_OF_MONTH), new ArrayList<Event>());
//                        tmpEvents.add(event);
//
//                        calendarEvents.put(temp.get(Calendar.DAY_OF_MONTH), tmpEvents);
//                    }
//                    events.clear();
//
//                    waitHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            eventsProcessorCallback.onEventsProcessed(target, calendarEvents);
//                        }
//                    }, 100);
//                }
//            }, 3000);
//        }
//    }
//
//    @Override
//    public void onCalendarReady(CalendarController calendar) {
//        if (calendar == monthCalendar) {
//            monthCalendar.displayEvents(new ArrayList<>(events), new DisplayEventCallback<Calendar>() {
//                @Override
//                public void onEventsDisplayed(Calendar month) {
//
//                }
//            });
//        } else if (calendar == listCalendar) {
//            listCalendar.displayEvents(new ArrayList<>(events), new DisplayEventCallback<Pair<Calendar, Calendar>>() {
//                @Override
//                public void onEventsDisplayed(Pair<Calendar, Calendar> period) {
//
//                }
//            });
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        monthCalendar.releaseCalendar();
//        listCalendar.releaseCalendar();
//    }
//
//}

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
//public class WeekActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_week);
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
//                Toast.makeText(WeekActivity.this, "" + date, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_week, menu);
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
