package com.kessi.photopipcollagemaker.shape;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ScaleGestureDetector {
    private static final float PRESSURE_THRESHOLD = 0.67f;
    private static final String TAG = "ScaleGestureDetector";
    private boolean mActive0MostRecent;
    private int mActiveId0;
    private int mActiveId1;
    private MotionEvent mCurrEvent;
    private float mCurrFingerDiffX;
    private float mCurrFingerDiffY;
    private float mCurrLen;
    private float mCurrPressure;
    private Vector2D mCurrSpanVector = new Vector2D(0,0);
    private float mFocusX;
    private float mFocusY;
    private boolean mGestureInProgress;
    private boolean mInvalidGesture;
    private final OnScaleGestureListener mListener;
    private MotionEvent mPrevEvent;
    private float mPrevFingerDiffX;
    private float mPrevFingerDiffY;
    private float mPrevLen;
    private float mPrevPressure;
    private float mScaleFactor;
    private long mTimeDelta;

    public interface OnScaleGestureListener {
        boolean onScale(View view, ScaleGestureDetector scaleGestureDetector);

        boolean onScaleBegin(View view, ScaleGestureDetector scaleGestureDetector);

        void onScaleEnd(View view, ScaleGestureDetector scaleGestureDetector);
    }

    public static class SimpleOnScaleGestureListener implements OnScaleGestureListener {
        public boolean onScale(View view, ScaleGestureDetector detector) {
            return false;
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            return true;
        }

        public void onScaleEnd(View view, ScaleGestureDetector detector) {
        }
    }

    public ScaleGestureDetector(OnScaleGestureListener listener) {
        this.mListener = listener;
    }

    public boolean onTouchEvent(View view, MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 0) {
            reset();
        }
        if (this.mInvalidGesture) {
            return false;
        }
        if (this.mGestureInProgress) {
            switch (action) {
                case 1:
                    reset();
                    return true;
                case 2:
                    setContext(view, event);
                    if (this.mCurrPressure / this.mPrevPressure <= PRESSURE_THRESHOLD || !this.mListener.onScale(view, this)) {
                        return true;
                    }
                    this.mPrevEvent.recycle();
                    this.mPrevEvent = MotionEvent.obtain(event);
                    return true;
                case 3:
                    this.mListener.onScaleEnd(view, this);
                    reset();
                    return true;
                case 5:
                    this.mListener.onScaleEnd(view, this);
                    int oldActive0 = this.mActiveId0;
                    int oldActive1 = this.mActiveId1;
                    reset();
                    this.mPrevEvent = MotionEvent.obtain(event);
                    if (!this.mActive0MostRecent) {
                        oldActive0 = oldActive1;
                    }
                    this.mActiveId0 = oldActive0;
                    this.mActiveId1 = event.getPointerId(event.getActionIndex());
                    this.mActive0MostRecent = false;
                    if (event.findPointerIndex(this.mActiveId0) < 0 || this.mActiveId0 == this.mActiveId1) {
                        this.mActiveId0 = event.getPointerId(findNewActiveIndex(event, this.mActiveId1, -1));
                    }
                    setContext(view, event);
                    this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                    return true;
                case 6:
                    int pointerCount = event.getPointerCount();
                    int actionIndex = event.getActionIndex();
                    int actionId = event.getPointerId(actionIndex);
                    boolean gestureEnded = false;
                    if (pointerCount > 2) {
                        int newIndex;
                        if (actionId == this.mActiveId0) {
                            newIndex = findNewActiveIndex(event, this.mActiveId1, actionIndex);
                            if (newIndex >= 0) {
                                this.mListener.onScaleEnd(view, this);
                                this.mActiveId0 = event.getPointerId(newIndex);
                                this.mActive0MostRecent = true;
                                this.mPrevEvent = MotionEvent.obtain(event);
                                setContext(view, event);
                                this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                            } else {
                                gestureEnded = true;
                            }
                        } else if (actionId == this.mActiveId1) {
                            newIndex = findNewActiveIndex(event, this.mActiveId0, actionIndex);
                            if (newIndex >= 0) {
                                this.mListener.onScaleEnd(view, this);
                                this.mActiveId1 = event.getPointerId(newIndex);
                                this.mActive0MostRecent = false;
                                this.mPrevEvent = MotionEvent.obtain(event);
                                setContext(view, event);
                                this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                            } else {
                                gestureEnded = true;
                            }
                        }
                        this.mPrevEvent.recycle();
                        this.mPrevEvent = MotionEvent.obtain(event);
                        setContext(view, event);
                    } else {
                        gestureEnded = true;
                    }
                    if (!gestureEnded) {
                        return true;
                    }
                    int activeId;
                    setContext(view, event);
                    if (actionId == this.mActiveId0) {
                        activeId = this.mActiveId1;
                    } else {
                        activeId = this.mActiveId0;
                    }
                    int index = event.findPointerIndex(activeId);
                    this.mFocusX = event.getX(index);
                    this.mFocusY = event.getY(index);
                    this.mListener.onScaleEnd(view, this);
                    reset();
                    this.mActiveId0 = activeId;
                    this.mActive0MostRecent = true;
                    return true;
                default:
                    return true;
            }
        }
        switch (action) {
            case 0:
                this.mActiveId0 = event.getPointerId(0);
                this.mActive0MostRecent = true;
                return true;
            case 1:
                reset();
                return true;
            case 5:
                if (this.mPrevEvent != null) {
                    this.mPrevEvent.recycle();
                }
                this.mPrevEvent = MotionEvent.obtain(event);
                this.mTimeDelta = 0;
                int index1 = event.getActionIndex();
                int index0 = event.findPointerIndex(this.mActiveId0);
                this.mActiveId1 = event.getPointerId(index1);
                if (index0 < 0 || index0 == index1) {
                    this.mActiveId0 = event.getPointerId(findNewActiveIndex(event, this.mActiveId1, -1));
                }
                this.mActive0MostRecent = false;
                setContext(view, event);
                this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                return true;
            default:
                return true;
        }
    }

    private int findNewActiveIndex(MotionEvent ev, int otherActiveId, int removedPointerIndex) {
        int pointerCount = ev.getPointerCount();
        int otherActiveIndex = ev.findPointerIndex(otherActiveId);
        int i = 0;
        while (i < pointerCount) {
            if (i != removedPointerIndex && i != otherActiveIndex) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void setContext(View view, MotionEvent curr) {
        if (this.mCurrEvent != null) {
            this.mCurrEvent.recycle();
        }
        this.mCurrEvent = MotionEvent.obtain(curr);
        this.mCurrLen = -1.0f;
        this.mPrevLen = -1.0f;
        this.mScaleFactor = -1.0f;
        this.mCurrSpanVector.set(0.0f, 0.0f);
        MotionEvent prev = this.mPrevEvent;
        int prevIndex0 = prev.findPointerIndex(this.mActiveId0);
        int prevIndex1 = prev.findPointerIndex(this.mActiveId1);
        int currIndex0 = curr.findPointerIndex(this.mActiveId0);
        int currIndex1 = curr.findPointerIndex(this.mActiveId1);
        if (prevIndex0 < 0 || prevIndex1 < 0 || currIndex0 < 0 || currIndex1 < 0) {
            this.mInvalidGesture = true;
            Log.e(TAG, "Invalid MotionEvent stream detected.", new Throwable());
            if (this.mGestureInProgress) {
                this.mListener.onScaleEnd(view, this);
                return;
            }
            return;
        }
        float px0 = prev.getX(prevIndex0);
        float py0 = prev.getY(prevIndex0);
        float px1 = prev.getX(prevIndex1);
        float py1 = prev.getY(prevIndex1);
        float cx0 = curr.getX(currIndex0);
        float cy0 = curr.getY(currIndex0);
        float pvx = px1 - px0;
        float pvy = py1 - py0;
        float cvx = curr.getX(currIndex1) - cx0;
        float cvy = curr.getY(currIndex1) - cy0;
        this.mCurrSpanVector.set(cvx, cvy);
        this.mPrevFingerDiffX = pvx;
        this.mPrevFingerDiffY = pvy;
        this.mCurrFingerDiffX = cvx;
        this.mCurrFingerDiffY = cvy;
        this.mFocusX = (0.5f * cvx) + cx0;
        this.mFocusY = (0.5f * cvy) + cy0;
        this.mTimeDelta = curr.getEventTime() - prev.getEventTime();
        this.mCurrPressure = curr.getPressure(currIndex0) + curr.getPressure(currIndex1);
        this.mPrevPressure = prev.getPressure(prevIndex0) + prev.getPressure(prevIndex1);
    }

    private void reset() {
        if (this.mPrevEvent != null) {
            this.mPrevEvent.recycle();
            this.mPrevEvent = null;
        }
        if (this.mCurrEvent != null) {
            this.mCurrEvent.recycle();
            this.mCurrEvent = null;
        }
        this.mGestureInProgress = false;
        this.mActiveId0 = -1;
        this.mActiveId1 = -1;
        this.mInvalidGesture = false;
    }

    public boolean isInProgress() {
        return this.mGestureInProgress;
    }

    public float getFocusX() {
        return this.mFocusX;
    }

    public float getFocusY() {
        return this.mFocusY;
    }

    public float getCurrentSpan() {
        if (this.mCurrLen == -1.0f) {
            float cvx = this.mCurrFingerDiffX;
            float cvy = this.mCurrFingerDiffY;
            this.mCurrLen = (float) Math.sqrt((double) ((cvx * cvx) + (cvy * cvy)));
        }
        return this.mCurrLen;
    }

    public Vector2D getCurrentSpanVector() {
        return this.mCurrSpanVector;
    }

    public float getCurrentSpanX() {
        return this.mCurrFingerDiffX;
    }

    public float getCurrentSpanY() {
        return this.mCurrFingerDiffY;
    }

    public float getPreviousSpan() {
        if (this.mPrevLen == -1.0f) {
            float pvx = this.mPrevFingerDiffX;
            float pvy = this.mPrevFingerDiffY;
            this.mPrevLen = (float) Math.sqrt((double) ((pvx * pvx) + (pvy * pvy)));
        }
        return this.mPrevLen;
    }

    public float getPreviousSpanX() {
        return this.mPrevFingerDiffX;
    }

    public float getPreviousSpanY() {
        return this.mPrevFingerDiffY;
    }

    public float getScaleFactor() {
        if (this.mScaleFactor == -1.0f) {
            this.mScaleFactor = getCurrentSpan() / getPreviousSpan();
        }
        return this.mScaleFactor;
    }

    public long getTimeDelta() {
        return this.mTimeDelta;
    }

    public long getEventTime() {
        return this.mCurrEvent.getEventTime();
    }
}
