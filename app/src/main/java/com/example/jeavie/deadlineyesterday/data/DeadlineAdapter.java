package com.example.jeavie.deadlineyesterday.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeavie.deadlineyesterday.R;
import com.example.jeavie.deadlineyesterday.RecyclerViewFragment;

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
        holder.summary.setText(deadlines.get(position).getSummary());
        holder.deadline.setText(deadlines.get(position).getDeadline());
        holder.labels.setText(String.valueOf(deadlines.get(position).getLabels()));
    }

    @Override
    public int getItemCount() {
        return deadlines.size();
    }

    public static class mViewHolder extends RecyclerView.ViewHolder{

        private TextView summary;
        private TextView deadline;
        private TextView labels;

        public mViewHolder(View itemView) {
            super(itemView);

            summary = itemView.findViewById(R.id.customSummary);
            deadline = itemView.findViewById(R.id.customDeadline);
            labels = itemView.findViewById(R.id.customTags);
//            itemView.setOnClickListener((View.OnClickListener) this);

        }
    }
}
