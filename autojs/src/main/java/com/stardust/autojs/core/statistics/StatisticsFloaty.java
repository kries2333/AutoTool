package com.stardust.autojs.core.statistics;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.stardust.autojs.R;
import com.stardust.autojs.core.console.ConsoleView;
import com.stardust.autojs.runtime.api.Statistics;
import com.stardust.enhancedfloaty.FloatyService;
import com.stardust.enhancedfloaty.ResizableExpandableFloaty;
import com.stardust.enhancedfloaty.ResizableExpandableFloatyWindow;
import com.stardust.util.ScreenMetrics;
import com.stardust.util.ViewUtil;

public class StatisticsFloaty extends ResizableExpandableFloaty.AbstractResizableExpandableFloaty {
    private ContextWrapper mContextWrapper;
    private View mResizer, mMoveCursor;
    private TextView mTitleView;
    private StatisticsImpl mStatistics;
    private CharSequence mTitle;
    private View mExpandedView;

    public StatisticsFloaty(StatisticsImpl statistics) {
        mStatistics = statistics;
        setShouldRequestFocusWhenExpand(false);
        setInitialX(500);
        setInitialY(1500);
        setCollapsedViewUnpressedAlpha(1.0f);
    }

    @Override
    public int getInitialWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT; //ScreenMetrics.getDeviceScreenWidth() * 2 / 3;
    }

    @Override
    public int getInitialHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;//ScreenMetrics.getDe
        // viceScreenHeight() / 3;
    }

    @Override
    public View inflateCollapsedView(FloatyService service, final ResizableExpandableFloatyWindow window) {
        ensureContextWrapper(service);
        return View.inflate(mContextWrapper, R.layout.floating_window_collapse, null);
    }

    private void ensureContextWrapper(Context context) {
        if (mContextWrapper == null) {
            mContextWrapper = new ContextThemeWrapper(context, R.style.ConsoleTheme);
        }
    }

    @Override
    public View inflateExpandedView(FloatyService service, ResizableExpandableFloatyWindow window) {
        ensureContextWrapper(service);
        View view = View.inflate(mContextWrapper, R.layout.floating_statistics_expand, null);
        setListeners(view, window);
        setUpStatistics(view, window);
        setInitialMeasure(view);
        mExpandedView = view;
        return view;
    }

    public View getExpandedView() {
        return mExpandedView;
    }

    private void setInitialMeasure(final View view) {
        int width = ScreenMetrics.getDeviceScreenWidth() * 2 / 3;
        int height = ScreenMetrics.getDeviceScreenHeight() / 3;
        view.post(() -> ViewUtil.setViewMeasure(view, width, height));
    }

    private void setListeners(final View view, final ResizableExpandableFloatyWindow window) {
        setWindowOperationIconListeners(view, window);
    }

    private void setUpStatistics(View view, ResizableExpandableFloatyWindow window) {
        StatisticsView statisticsView = view.findViewById(R.id.statistics);
        statisticsView.setStatistics(mStatistics);
        statisticsView.setWindow(window);
        initStatisticsTitle(view);
    }

    private void setWindowOperationIconListeners(View view, final ResizableExpandableFloatyWindow window) {
//        view.findViewById(R.id.statistics_close).setOnClickListener(v -> window.close());
//        view.findViewById(R.id.move_or_resize).setOnClickListener(v -> {
//            if (mMoveCursor.getVisibility() == View.VISIBLE) {
//                mMoveCursor.setVisibility(View.GONE);
//                mResizer.setVisibility(View.GONE);
//            } else {
//                mMoveCursor.setVisibility(View.VISIBLE);
//                mResizer.setVisibility(View.VISIBLE);
//            }
//        });
//        view.findViewById(R.id.statistics_minimize).setOnClickListener(v -> window.collapse());
    }

    private void initStatisticsTitle(View view) {
        mTitleView = view.findViewById(R.id.title);
        if (mTitle != null) {
            mTitleView.setText(mTitle);
        }
    }

    @Override
    public View getResizerView(View expandedView) {
        mResizer = expandedView.findViewById(R.id.statistics_resizer);
        return mResizer;
    }

    @Nullable
    @Override
    public View getMoveCursorView(View expandedView) {
        mMoveCursor = expandedView.findViewById(R.id.statistics_move_cursor);
        return mMoveCursor;
    }

    public void setTitle(final CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.post(() -> mTitleView.setText(title));
        }
    }
}
