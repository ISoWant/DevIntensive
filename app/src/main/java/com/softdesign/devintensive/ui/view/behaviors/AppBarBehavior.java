package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.softdesign.devintensive.utils.ConstantManager;

import static com.softdesign.devintensive.utils.UiUtils.getAppBarSize;
import static com.softdesign.devintensive.utils.UiUtils.getVewHeight;
import static com.softdesign.devintensive.utils.UiUtils.getStatusBarHeight;

class AppBarBehavior<LinearLayout extends View> extends CoordinatorLayout.Behavior<LinearLayout> {

    private final static String TAG = ConstantManager.TAG_PREFIX + "AppBarBehavior";
    private final Context mContext;
    private float mMinLayoutSize, mMaxLayoutSize;
    private float mMaxScroll;
    private float mMinScrollY;
    private float mExpPerc;

    public AppBarBehavior(Context context, AttributeSet attrs) {
        this.mContext = context;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, LinearLayout child) {
        Bundle bundle = new Bundle();
        bundle.putFloat("mExpPerc", this.mExpPerc);
        bundle.putFloat("mMinLayoutSize", this.mMinLayoutSize);
        bundle.putFloat("mMaxLayoutSize", this.mMaxLayoutSize);
        bundle.putFloat("mMaxScroll", this.mMaxScroll);
        bundle.putFloat("mMinScrollY", this.mMinScrollY);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, LinearLayout child, Parcelable state) {
        if (!(state instanceof Bundle)) return; // implicit null check

        Bundle bundle = (Bundle) state;
        this.mExpPerc = bundle.getFloat("mExpPerc");
        this.mMinLayoutSize = bundle.getFloat("mMinLayoutSize");
        this.mMaxLayoutSize = bundle.getFloat("mMaxLayoutSize");
        this.mMinScrollY = bundle.getFloat("mMinScrollY");
        this.mMaxScroll = bundle.getFloat("mMaxScroll");
        final CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        layoutParams.height = (int) Math.max(mMinLayoutSize, mMaxLayoutSize * mExpPerc);
        child.setLayoutParams(layoutParams);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        final CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        AppBarLayout appBarLayout;
        if (dependency instanceof AppBarLayout) {
            appBarLayout = (AppBarLayout) dependency;
            if (layoutParams.getAnchorId() != -1 && layoutParams.getAnchorId() != appBarLayout.getId()) {
                // The anchor ID doesn't match the dependency
                return false;
            }
        } else {
            return false;
        }

        if (mMinLayoutSize == 0.0f) {
            initProperties(parent, child, appBarLayout);
        }

        float curDependY = appBarLayout.getBottom() - mMinScrollY;
        mExpPerc = curDependY / mMaxScroll;
        layoutParams.height = (int) (mMinLayoutSize + (mMaxLayoutSize - mMinLayoutSize) * mExpPerc);

        child.setTranslationY(appBarLayout.getBottom());
        child.setLayoutParams(layoutParams);

        return true;
    }

    private void initProperties(CoordinatorLayout parent, LinearLayout child, AppBarLayout dependency) {  //расчет начальных параметров
        mMaxLayoutSize = child.getHeight();
        mMinLayoutSize = getVewHeight(child);  //найдет минимальную высоту child, которая полностью вместит его контент, т.е. высоту если будет wrap_content
        mMinScrollY = getStatusBarHeight(mContext) + getAppBarSize(mContext);
        mMaxScroll = dependency.getHeight() - mMinScrollY;
    }
}
