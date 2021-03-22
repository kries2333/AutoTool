package com.stardust.autojs.core.statistics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stardust.autojs.R;
import com.stardust.autojs.core.console.ConsoleImpl;
import com.stardust.autojs.core.console.ConsoleView;
import com.stardust.enhancedfloaty.ResizableExpandableFloatyWindow;

import java.lang.ref.WeakReference;

public class StatisticsView extends FrameLayout {

    private StatisticsImpl mStatistics;
    private RecyclerView mLogListRecyclerView;
    private ResizableExpandableFloatyWindow mWindow;

    public StatisticsView(Context context) {
        super(context);
        init(null);
    }

    public StatisticsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatisticsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.statistics_view, this);

        mLogListRecyclerView = findViewById(R.id.log_map);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mLogListRecyclerView.setLayoutManager(manager);
        mLogListRecyclerView.setAdapter(new Adapter());
    }

    public void setStatistics(StatisticsImpl statistics) {
        mStatistics = statistics;
        mStatistics.setStatisticsView(this);
    }

    public void setWindow(ResizableExpandableFloatyWindow window) {
        mWindow = window;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    private class Adapter extends RecyclerView.Adapter<StatisticsView.ViewHolder> {

        @Override
        public StatisticsView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StatisticsView.ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.console_view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(StatisticsView.ViewHolder holder, int position) {
//            ConsoleImpl.LogEntry logEntry = mLogEntries.get(position);
//            holder.textView.setText(logEntry.content);
//            holder.textView.setTextColor(mColors.get(logEntry.level));
        }

        @Override
        public int getItemCount() {
//            return mLogEntries.size();
            return 0;
        }
    }
}
