package com.example.jeavie.deadlineyesterday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DeadlineActivityAdapter extends BaseAdapter {

    List<DeadlineActivity> mDeadlineList;
    private LayoutInflater layoutInflater;
    View.OnTouchListener mTouchListener;

    public DeadlineActivityAdapter(Context context, List<DeadlineActivity> mDeadlineList, View.OnTouchListener listener) {
        this.mDeadlineList = mDeadlineList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTouchListener = listener;
    }

    @Override
    public int getCount() {
        return mDeadlineList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeadlineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = layoutInflater.inflate(R.layout.activity_main_item, parent, false);
            view.setOnTouchListener(mTouchListener);
        }

        TextView customSummary = view.findViewById(R.id.customSummary);
        customSummary.setText(mDeadlineList.get(position).getSummary());

        TextView customDeadline = view.findViewById(R.id.customDeadline);
        customDeadline.setText(mDeadlineList.get(position).getDeadline());

        TextView customTags = view.findViewById(R.id.customTags);
        customTags.setText(String.valueOf(mDeadlineList.get(position).getLabels()));

        return view;
    }

}
