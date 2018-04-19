package com.example.jeavie.deadlineyesterday.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeavie.deadlineyesterday.MainActivity;
import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.activities.AddDeadlineActivity;
import com.example.jeavie.deadlineyesterday.data.Codes;
import com.example.jeavie.deadlineyesterday.data.DbActivity;
import com.example.jeavie.deadlineyesterday.data.Deadline;
import com.example.jeavie.deadlineyesterday.interfaces.ItemClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jeavie on 10.02.2018.
 */

public class DeadlineAdapter extends RecyclerView.Adapter<DeadlineAdapter.mViewHolder> {

    Context context;
    List<Deadline> deadlines;

    public DeadlineAdapter(Context context, List<Deadline> deadlines) {
        this.context = context;
        this.deadlines = deadlines;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new mViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, int position) {

        //holder.date.setText(deadlines.get(position).getDate());
        holder.summary.setText(deadlines.get(position).getSummary());
        holder.deadline.setText(deadlines.get(position).getDeadline());
        holder.labels.setText(String.valueOf(deadlines.get(position).getLabels()));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                editDeadline(position);
            }
        });
    }

    private void editDeadline(int position) {
        Intent intent = new Intent(context, AddDeadlineActivity.class);
        intent.putExtra("id", deadlines.get(position).getId());
        intent.putExtra("summary", deadlines.get(position).getSummary());
        intent.putExtra("date", deadlines.get(position).getDate());
        intent.putExtra("time", deadlines.get(position).getTime());
        intent.putExtra("labels", deadlines.get(position).getLabels());
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public void removeDeadline(int position){
        deadlines.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreDeadline(Deadline deadline, int position){
        deadlines.add(position, deadline);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return deadlines.size();
    }

    public static class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ItemClickListener itemClickListener;

        private TextView summary;
        private TextView deadline;
        private TextView labels;
        public RelativeLayout viewBackground, viewForeground;


        public mViewHolder(View itemView) {
            super(itemView);

            summary = itemView.findViewById(R.id.customSummary);
            deadline = itemView.findViewById(R.id.customDeadline);
            labels = itemView.findViewById(R.id.customLabels);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    public void timer() {

        String deadline;
        for(int i = 0; i < deadlines.size(); i++) {
            String date = deadlines.get(i).getDate();
            String time = deadlines.get(i).getTime();

            String format = date + " " + time;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh : mm");
            Date cal1 = new Date();
            Date cal2 = null;
            try {
                cal2 = df.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //calculating time and choosing format
            long diff = cal2.getTime() - cal1.getTime();

            long diffSeconds = diff/(1000);

            long diffMinutes = diff / (60 * 1000);

            long diffHours = diff / (60 * 60 * 1000);

            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays>=1){
                deadline = String.valueOf(diffDays) + "d " + String.valueOf(diffHours - (diffDays*24)) + "h";
            } else if (diffHours>=1){
                deadline = String.valueOf(diffHours) + "h " + String.valueOf(diffMinutes - (diffHours*60)) + "m";
            } else if (diffSeconds > 0) //for debuging
                deadline = String.valueOf(diffSeconds) + "s ";
            else deadline = String.valueOf(diffMinutes) + "m ";

            //set new deadline to the deadline field
            deadlines.get(i).setDeadline(deadline);
            notifyDataSetChanged();
        }

    }
}
