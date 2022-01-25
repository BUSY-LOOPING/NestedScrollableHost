package com.busy.looping.myapplication;

import static androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;


/**
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 * <p>
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 */

public class NestedScrollableHost extends FrameLayout {
    private int touchSlop = 0;
    private float initialX = 0f;
    private float initialY = 0f;
    private View child;
    public ViewPager2 parentViewPager;
    private final Context context;

    public ViewPager2 get() {
        ViewParent viewParent = null;
        viewParent = this.getParent();

        View view = (View) viewParent;
        while (view != null && !(view instanceof ViewPager2)) {
            view = (View) view.getParent();
        }
        return (ViewPager2) view;
    }

    public void init() {
        parentViewPager = get();
//        if (parentViewPager == null) {
//            Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
//        }
        if (getChildCount() > 0) {
            child = getChildAt(0);
        } else {
            child = null;
        }
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init(); //INIT SHOULD ONLY BE CALLED AFTER ONATTACHTOWINDOW else getParent will always return null
    }

    public NestedScrollableHost(@NonNull Context context) {
        super(context);
        this.context = context;
//        init();
    }

    public NestedScrollableHost(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public NestedScrollableHost(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public NestedScrollableHost(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public boolean canChildScroll(int orientation, float delta) {
        int direction = (int) delta;
        switch (orientation) {
            case 0:
                return child.canScrollHorizontally(direction);
            case 1:
                return child.canScrollVertically(direction);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        handleInterceptTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    private void handleInterceptTouchEvent(MotionEvent e) {
        if (parentViewPager == null) {
            return;
        }
        int orientation = parentViewPager.getOrientation();

        // Early return if child can't scroll in same direction as parent
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return;
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            initialX = e.getX();
            initialY = e.getY();
            requestDisallowInterceptTouchEvent(true);
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            final float dx = e.getX() - initialX;
            final float dy = e.getY() - initialY;
            boolean isVpHorizontal = orientation == ORIENTATION_HORIZONTAL;

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            final float scaledDx = Math.abs(dx) * (isVpHorizontal ? 0.5f : 1f);
            final float scaledDy = Math.abs(dy) * (isVpHorizontal ? 1f : 0.5f);

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept
                    requestDisallowInterceptTouchEvent(false);
                } else {
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (canChildScroll(orientation, (isVpHorizontal ? dx : dy))) {
                        // Child can scroll, disallow all parents to intercept
                        requestDisallowInterceptTouchEvent(true);
                    } else {
                        // Child cannot scroll, allow all parents to intercept
                        requestDisallowInterceptTouchEvent(false);
                    }
                }
            }
        }
    }
}

