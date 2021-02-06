package com.kessi.photopipcollagemaker.shape;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.view.MotionEventCompat;

import com.kessi.photopipcollagemaker.R;


public class ViewOval extends FrameLayout implements OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    public int f1028h;
    private ImageView handleView;
    public ImageView imageView;
    public boolean isRotateEnabled;
    public boolean isScaleEnabled;
    public boolean isTranslateEnabled;
    private int mActivePointerId;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector;
    public float maximumScale;
    public float minimumScale;
    private ImageView ovalView;
    public int f1029w;
    public int f1030x;
    public int f1031y;

    class touchListener implements OnTouchListener {
        float centerX;
        float centerY;
        float startA;
        float startR;
        float startRotation;
        float startScale;
        float startX;
        float startY;

        touchListener() {
        }

        public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == 0) {
                this.centerX = ((float) (getLeft() + getRight())) / 2.0f;
                this.centerY = ((float) (getTop() + getBottom())) / 2.0f;
                this.startX = (e.getRawX() - handleView.getX()) + this.centerX;
                this.startY = (e.getRawY() - handleView.getY()) + this.centerY;
                this.startR = (float) Math.hypot((double) (e.getRawX() - this.startX), (double) (e.getRawY() - this.startY));
                this.startA = (float) Math.toDegrees(Math.atan2((double) (e.getRawY() - this.startY), (double) (e.getRawX() - this.startX)));
                this.startScale = getScaleX();
                this.startRotation = getRotation();
            } else if (e.getAction() == 2) {
                float newA = (float) Math.toDegrees(Math.atan2((double) (e.getRawY() - this.startY), (double) (e.getRawX() - this.startX)));
                float newScale = (((float) Math.hypot((double) (e.getRawX() - this.startX), (double) (e.getRawY() - this.startY))) / this.startR) * this.startScale;
                if (newScale >= minimumScale && newScale <= maximumScale) {
                    computeRenderOffset(ViewOval.this, 0.0f, 0.0f);
                    float newRotation = (newA - this.startA) + this.startRotation;
                    setScaleX(newScale);
                    setScaleY(newScale);
                    ovalView.setRotation(newRotation);
                    handleView.setScaleX(1.0f / newScale);
                    handleView.setScaleY(1.0f / newScale);
                    LayoutParams params = (LayoutParams) handleView.getLayoutParams();
                    params.setMargins(0, 0, (int) (30.0f / newScale), (int) (30.0f / newScale));
                    handleView.setLayoutParams(params);
                }
            } else if (e.getAction() == 1) {
            }
            return true;
        }
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D(0,0);
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            this.mPivotX = detector.getFocusX();
            this.mPivotY = detector.getFocusY();
            this.mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector detector) {
            float angle;
            float f = 0.0f;
            TransformInfo info = new TransformInfo();
            info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            if (isRotateEnabled) {
                angle = Vector2D.getAngle(this.mPrevSpanVector, detector.getCurrentSpanVector());
            } else {
                angle = 0.0f;
            }
            info.deltaAngle = angle;
            if (isTranslateEnabled) {
                angle = detector.getFocusX() - this.mPivotX;
            } else {
                angle = 0.0f;
            }
            info.deltaX = angle;
            if (isTranslateEnabled) {
                f = detector.getFocusY() - this.mPivotY;
            }
            info.deltaY = f;
            info.pivotX = this.mPivotX;
            info.pivotY = this.mPivotY;
            info.minimumScale = minimumScale;
            info.maximumScale = maximumScale;
            move(view, info);
            return false;
        }
    }

    public ViewOval(Context context) {
        this(context, null);
    }

    public ViewOval(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewOval(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isRotateEnabled = false;
        this.isTranslateEnabled = true;
        this.isScaleEnabled = false;
        this.minimumScale = 0.1f;
        this.maximumScale = 1.0f;
        this.mActivePointerId = -1;
        setImageRes();
    }

   
    @SuppressLint("NewApi") public ViewOval(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.isRotateEnabled = false;
        this.isTranslateEnabled = true;
        this.isScaleEnabled = false;
        this.minimumScale = 0.1f;
        this.maximumScale = 1.0f;
        this.mActivePointerId = -1;
        setImageRes();
    }

    private void setImageRes() {
        this.mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        this.ovalView = new ImageView(getContext());
        setOnTouchListener(this);
        this.ovalView.setAdjustViewBounds(true);
        this.ovalView.setImageResource(R.drawable.breastoval);
        this.ovalView.setLayoutParams(new LayoutParams(-1, -1));
        this.handleView = new ImageView(getContext());
        this.handleView.setAdjustViewBounds(true);
        this.handleView.setImageResource(R.drawable.scale);
        LayoutParams paramsHandleView = new LayoutParams(-2, -2, 85);
        paramsHandleView.setMargins(0, 0, 10, 10);
        this.handleView.setLayoutParams(paramsHandleView);
        addView(this.ovalView);
        addView(this.handleView);
        this.handleView.setOnTouchListener(new touchListener());
    }

    public void setScale(float newScale, float translate) {
        setScaleX(newScale);
        setScaleY(newScale);
        adjustTranslation(this, (translate * 1.0f) / newScale, 0.0f);
        this.handleView.setScaleX(1.0f / newScale);
        this.handleView.setScaleY(1.0f / newScale);
        LayoutParams params = (LayoutParams) this.handleView.getLayoutParams();
        params.setMargins(0, 0, (int) (30.0f / newScale), (int) (30.0f / newScale));
        this.handleView.setLayoutParams(params);
    }

    private float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            return degrees - 360.0f;
        }
        if (degrees < -180.0f) {
            return degrees + 360.0f;
        }
        return degrees;
    }

    private void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        boolean translated = adjustTranslation(view, info.deltaX, info.deltaY);
        float scale = Math.max(info.minimumScale, Math.min(info.maximumScale, view.getScaleX() * info.deltaScale));
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setRotation(adjustAngle(view.getRotation() + info.deltaAngle));
    }

    private boolean adjustTranslation(View view, float deltaX, float deltaY) {
        boolean translated = false;
        float[] deltaVector = new float[]{deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        float scaledWidth = (((float) view.getWidth()) - (((float) view.getWidth()) * getScaleX())) / 2.0f;
        float scaledHeight = (((float) view.getHeight()) - (((float) view.getHeight()) * getScaleY())) / 2.0f;
        float finalTransY = view.getTranslationY() + deltaVector[1];
        float finalTransX = view.getTranslationX() + deltaVector[0];
        float topLimit = ((float) (-view.getPaddingTop())) - scaledHeight;
        float bottomLimit = (((float) (this.imageView.getHeight() - view.getHeight())) + scaledHeight) + ((float) view.getPaddingBottom());
        float leftLimit = ((float) (-view.getPaddingLeft())) - scaledWidth;
        float rightLimit = (((float) (this.imageView.getWidth() - view.getWidth())) + scaledWidth) + ((float) view.getPaddingRight());
        if (finalTransY >= topLimit && finalTransY < bottomLimit) {
            translated = true;
            view.setTranslationY(finalTransY);
        } else if (finalTransY < topLimit) {
            view.setTranslationY(topLimit);
        } else if (finalTransY > bottomLimit) {
            view.setTranslationY(bottomLimit);
        }
        if (finalTransX >= leftLimit && finalTransX < rightLimit) {
            view.setTranslationX(finalTransX);
            return true;
        } else if (finalTransX < leftLimit) {
            view.setTranslationX(leftLimit);
            return translated;
        } else if (finalTransX <= rightLimit) {
            return translated;
        } else {
            view.setTranslationX(rightLimit);
            return translated;
        }
    }

    private void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() != pivotX || view.getPivotY() != pivotY) {
            float[] prevPoint = new float[]{0.0f, 0.0f};
            view.getMatrix().mapPoints(prevPoint);
            float[] currPoint = new float[]{0.0f, 0.0f};
            view.getMatrix().mapPoints(currPoint);
            float scaledWidth = (((float) view.getWidth()) - (((float) view.getWidth()) * getScaleX())) / 2.0f;
            float scaledHeight = (((float) view.getHeight()) - (((float) view.getHeight()) * getScaleY())) / 2.0f;
            float finalTransY = view.getTranslationY() + (currPoint[1] - prevPoint[1]);
            float finalTransX = view.getTranslationX() + (currPoint[0] - prevPoint[0]);
            float topLimit = ((float) (-view.getPaddingTop())) - scaledHeight;
            float bottomLimit = (((float) (this.imageView.getHeight() - view.getHeight())) + scaledHeight) + ((float) view.getPaddingBottom());
            float leftLimit = ((float) (-view.getPaddingLeft())) - scaledWidth;
            float rightLimit = (((float) (this.imageView.getWidth() - view.getWidth())) + scaledWidth) + ((float) view.getPaddingRight());
            if (finalTransY >= topLimit && finalTransY < bottomLimit) {
                view.setTranslationY(finalTransY);
            } else if (finalTransY < topLimit) {
                view.setTranslationY(topLimit);
            } else if (finalTransY > bottomLimit) {
                view.setTranslationY(bottomLimit);
            }
            if (finalTransX >= leftLimit && finalTransX < rightLimit) {
                view.setTranslationX(finalTransX);
            } else if (finalTransX < leftLimit) {
                view.setTranslationX(leftLimit);
            } else if (finalTransX > rightLimit) {
                view.setTranslationX(rightLimit);
            }
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        int newPointerIndex = 0;
        this.mScaleGestureDetector.onTouchEvent(view, event);
        if (this.isTranslateEnabled) {
            int action = event.getAction();
            int pointerIndex;
            switch (event.getActionMasked() & action) {
                case 0:
                    this.mPrevX = event.getX();
                    this.mPrevY = event.getY();
                    this.mActivePointerId = event.getPointerId(0);
                    break;
                case 1:
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    pointerIndex = event.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex != -1) {
                        float currX = event.getX(pointerIndex);
                        float currY = event.getY(pointerIndex);
                        if (!this.mScaleGestureDetector.isInProgress()) {
                            adjustTranslation(view, currX - this.mPrevX, currY - this.mPrevY);
                            break;
                        }
                    }
                    break;
                case 3:
                    this.mActivePointerId = -1;
                    break;
                case 6:
                    pointerIndex = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & action) >> 8;
                    if (event.getPointerId(pointerIndex) == this.mActivePointerId) {
                        if (pointerIndex == 0) {
                            newPointerIndex = 1;
                        }
                        this.mPrevX = event.getX(newPointerIndex);
                        this.mPrevY = event.getY(newPointerIndex);
                        this.mActivePointerId = event.getPointerId(newPointerIndex);
                        break;
                    }
                    break;
            }
        }
        return true;
    }
}
