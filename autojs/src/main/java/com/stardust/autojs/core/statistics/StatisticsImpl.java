package com.stardust.autojs.core.statistics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import com.stardust.autojs.R;
import com.stardust.autojs.core.console.ConsoleImpl;
import com.stardust.autojs.core.console.ConsoleView;
import com.stardust.autojs.runtime.api.AbstractStatistics;
import com.stardust.autojs.runtime.api.Statistics;
import com.stardust.autojs.util.FloatingPermission;
import com.stardust.enhancedfloaty.FloatyService;
import com.stardust.enhancedfloaty.ResizableExpandableFloatyWindow;
import com.stardust.util.ScreenMetrics;
import com.stardust.util.UiHandler;

import java.lang.ref.WeakReference;

public class StatisticsImpl extends AbstractStatistics {
    private String TAG = "StatisticsImpl";

    private final Object WINDOW_SHOW_LOCK = new Object();
    private StatisticsFloaty mStatisticsFloaty;
    private WeakReference<ConsoleImpl.LogListener> mLogListener;
    private UiHandler mUiHandler;
    private ResizableExpandableFloatyWindow mFloatyWindow;
    private WeakReference<StatisticsView> mStatisticsView;
    private volatile boolean mShown = false;
    private int mX, mY;

    public StatisticsImpl(UiHandler uiHandler) {
        this(uiHandler, null);
    }

    public StatisticsImpl(UiHandler uiHandler, Statistics globalStatistics) {
        Log.d(TAG, "StatisticsImpl");
        mX = 10;
        mY = ScreenMetrics.getDeviceScreenHeight() / 2 + 200;
        mUiHandler = uiHandler;
        mStatisticsFloaty = new StatisticsFloaty(this);
        mFloatyWindow = new ResizableExpandableFloatyWindow(mStatisticsFloaty) {
            @Override
            public void onCreate(FloatyService service, WindowManager manager) {
                super.onCreate(service, manager);
                expand();
                mFloatyWindow.getWindowBridge().updatePosition(mX, mY);
                synchronized (WINDOW_SHOW_LOCK) {
                    mShown = true;
                    WINDOW_SHOW_LOCK.notifyAll();
                }
            }
        };
    }

    public void setStatisticsView(StatisticsView statisticsView) {
        mStatisticsView = new WeakReference<>(statisticsView);
//        setLogListener(statisticsView);
        synchronized (this) {
            this.notify();
        }
    }

//    public void setLogListener(Statistics.LogListener logListener) {
//        mLogListener = new WeakReference<>(logListener);
//    }

    @Override
    public void set(Object key, Object value) {
        Log.d(TAG, "set: key=" + key + " v=" + value);
    }

    @Override
    public void show() {
        Log.d(TAG, "show");
        if (mShown) {
            return;
        }
        if (!FloatingPermission.canDrawOverlays(mUiHandler.getContext())) {
            FloatingPermission.manageDrawOverlays(mUiHandler.getContext());
            mUiHandler.toast(R.string.text_no_floating_window_permission);
            return;
        }
        startFloatyService();
        mUiHandler.post(() -> {
            try {
                FloatyService.addWindow(mFloatyWindow);
                // SecurityException: https://github.com/hyb1996-guest/AutoJsIssueReport/issues/4781
            } catch (WindowManager.BadTokenException | SecurityException e) {
                e.printStackTrace();
                mUiHandler.toast(R.string.text_no_floating_window_permission);
            }
        });
        synchronized (WINDOW_SHOW_LOCK) {
            if (mShown) {
                return;
            }
            try {
                WINDOW_SHOW_LOCK.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startFloatyService() {
        Context context = mUiHandler.getContext();
        context.startService(new Intent(context, FloatyService.class));
    }
}
