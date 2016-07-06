package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Behavior to link Nested scroll to bottom edge of LinearLayout
 */
class NestedScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

    private final static String TAG = ConstantManager.TAG_PREFIX + "NSBehavior";

    public NestedScrollBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Log.d(TAG, "onDependentViewChanged");
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        LinearLayout linearLayout;
        if (dependency instanceof LinearLayout) {
            linearLayout = (LinearLayout) dependency;
            if (lp.getAnchorId() != -1 && lp.getAnchorId() != linearLayout.getId()) {
                return false;
            }
        } else {
            return false;
        }
        child.setY(dependency.getTranslationY() + dependency.getHeight());
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
