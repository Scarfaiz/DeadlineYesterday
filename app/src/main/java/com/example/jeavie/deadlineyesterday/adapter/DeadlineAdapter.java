package com.example.jeavie.deadlineyesterday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.data.Deadline;

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
    public void onBindViewHolder(mViewHolder holder, int position) {
        //holder.date.setText(deadlines.get(position).getDate());
        holder.summary.setText(deadlines.get(position).getSummary());
        holder.deadline.setText(deadlines.get(position).getDeadline());
        holder.labels.setText(String.valueOf(deadlines.get(position).getLabels()));
    }

    @Override
    public int getItemCount() {
        return deadlines.size();
    }

    public static class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        //private TextView date;
        private TextView summary;
        private TextView deadline;
        private TextView labels;

        public mViewHolder(View itemView) {
            super(itemView);

            //date = itemView.findViewById(R.id.date);
            summary = itemView.findViewById(R.id.customSummary);
            deadline = itemView.findViewById(R.id.customDeadline);
            labels = itemView.findViewById(R.id.customLabels);

        }
    }
}
