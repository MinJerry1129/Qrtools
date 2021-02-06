package com.kessi.photopipcollagemaker.multitouch.controller;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;


import java.util.ArrayList;
import java.util.List;

public abstract class MultiTouchEntity implements Parcelable {

    protected boolean mFirstLoad = true;

    protected transient Paint mPaint = new Paint();

    protected int mWidth;
    protected int mHeight;

    // width/height of screen
    protected int mDisplayWidth;
    protected int mDisplayHeight;

    protected float mCenterX;
    protected float mCenterY;
    protected float mScaleX;
    protected float mScaleY;
    protected float mAngle;

    protected float mMinX;
    protected float mMaxX;
    protected float mMinY;
    protected float mMaxY;

    // area of the entity that can be scaled/rotated
    // using single touch (grows from bottom right)
    protected final static int GRAB_AREA_SIZE = 40;
    protected boolean mIsGrabAreaSelected = false;
    protected boolean mIsLatestSelected = false;

    protected float mGrabAreaX1;
    protected float mGrabAreaY1;
    protected float mGrabAreaX2;
    protected float mGrabAreaY2;

    protected float mStartMidX;
    protected float mStartMidY;

    private static final int UI_MODE_ROTATE = 1;
    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    protected int mUIMode = UI_MODE_ROTATE;

    //detect touch
    Matrix matrix = new Matrix();
    float[] point = new float[2];
    List<PointF> mappedPoints = new ArrayList<>();

    public MultiTouchEntity() {
    }

    public MultiTouchEntity(Resources res) {
        getMetrics(res);
    }

    protected void getMetrics(Resources res) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        mDisplayWidth = (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? Math
                .max(metrics.widthPixels, metrics.heightPixels) : Math.min(
                metrics.widthPixels, metrics.heightPixels);
        mDisplayHeight = (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? Math
                .min(metrics.widthPixels, metrics.heightPixels) : Math.max(
                metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Set the position and scale of an image in screen coordinates
     */
    public boolean setPos(MultiTouchController.PositionAndScale newImgPosAndScale) {
        float newScaleX;
        float newScaleY;

        if ((mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0) {
            newScaleX = newImgPosAndScale.getScaleX();
        } else {
            newScaleX = newImgPosAndScale.getScale();
        }

        if ((mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0) {
            newScaleY = newImgPosAndScale.getScaleY();
        } else {
            newScaleY = newImgPosAndScale.getScale();
        }

        return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(),
                newScaleX, newScaleY, newImgPosAndScale.getAngle());
    }

    /**
     * Set the position and scale of an image in screen coordinates
     */
    protected boolean setPos(float centerX, float centerY, float scaleX,
                             float scaleY, float angle) {
        float[] size = calculateHalfDrawableSize(scaleX, scaleY);
        float ws = size[0];//(mWidth / 2) * scaleX;
        float hs = size[1];//(mHeight / 2) * scaleY;

        mMinX = centerX - ws;
        mMinY = centerY - hs;
        mMaxX = centerX + ws;
        mMaxY = centerY + hs;

        mGrabAreaX1 = mMaxX - GRAB_AREA_SIZE;
        mGrabAreaY1 = mMaxY - GRAB_AREA_SIZE;
        mGrabAreaX2 = mMaxX;
        mGrabAreaY2 = mMaxY;

        mCenterX = centerX;
        mCenterY = centerY;
        mScaleX = scaleX;
        mScaleY = scaleY;
        mAngle = angle;

        return true;
    }

    protected float[] calculateHalfDrawableSize(float scaleX,
                                                float scaleY) {
		float[] size = new float[2];
        float ws = (mWidth / 2) * scaleX;
        float hs = (mHeight / 2) * scaleY;
        size[0] = ws;
        size[1] = hs;
        return size;
    }

    /**
     * @deprecated
     * Return whether or not the given screen coords are inside this image
     */
    public boolean containsPoint(float touchX, float touchY) {
        return (touchX >= mMinX && touchX <= mMaxX && touchY >= mMinY && touchY <= mMaxY);
    }

    /**
     * Return whether or not the given screen coords are inside this image
     */
    public boolean contain(float touchX, float touchY) {
        final float dx = (mMaxX + mMinX) / 2;
        final float dy = (mMaxY + mMinY) / 2;

        matrix.reset();
        matrix.setRotate(mAngle * 180.0f / (float) Math.PI, dx, dy);
        //draw mapped points
        mappedPoints.clear();
        point[0] = mMinX;
        point[1] = mMinY;
        matrix.mapPoints(point);
        mappedPoints.add(new PointF(point[0], point[1]));

        point[0] = mMaxX;
        point[1] = mMinY;
        matrix.mapPoints(point);
        mappedPoints.add(new PointF(point[0], point[1]));

        point[0] = mMaxX;
        point[1] = mMaxY;
        matrix.mapPoints(point);
        mappedPoints.add(new PointF(point[0], point[1]));

        point[0] = mMinX;
        point[1] = mMaxY;
        matrix.mapPoints(point);
        mappedPoints.add(new PointF(point[0], point[1]));
        return contains(mappedPoints, new PointF(touchX, touchY));
    }
    /**
     * Return true if the given point is contained inside the boundary.
     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     *
     * @param test The point to check
     * @return true if the point is inside the boundary, false otherwise
     */
    public static boolean contains(List<PointF> points, PointF test) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).y > test.y) != (points.get(j).y > test.y) &&
                    (test.x < (points.get(j).x - points.get(i).x) * (test.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

    public boolean grabAreaContainsPoint(float touchX, float touchY) {
        return (touchX >= mGrabAreaX1 && touchX <= mGrabAreaX2
                && touchY >= mGrabAreaY1 && touchY <= mGrabAreaY2);
    }

    public void reload(Context context) {
        mFirstLoad = false; // Let the load know properties have changed so
        // reload those,
        // don't go back and start with defaults
        load(context, mCenterX, mCenterY);
    }

    public abstract void draw(Canvas canvas);

    public abstract void load(Context context, float startMidX, float startMidY);

    public abstract void load(Context context);

    public abstract void unload();

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public float getCenterX() {
        return mCenterX;
    }

    public float getCenterY() {
        return mCenterY;
    }

    public float getScaleX() {
        return mScaleX;
    }

    public float getScaleY() {
        return mScaleY;
    }

    public float getAngle() {
        return mAngle;
    }

    public float getMinX() {
        return mMinX;
    }

    public float getMaxX() {
        return mMaxX;
    }

    public float getMinY() {
        return mMinY;
    }

    public float getMaxY() {
        return mMaxY;
    }

    public void setIsGrabAreaSelected(boolean selected) {
        mIsGrabAreaSelected = selected;
    }

    public boolean isGrabAreaSelected() {
        return mIsGrabAreaSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(new boolean[]{mFirstLoad, mIsGrabAreaSelected,
                mIsLatestSelected});
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        dest.writeInt(mDisplayWidth);
        dest.writeInt(mDisplayHeight);
        dest.writeFloat(mCenterX);
        dest.writeFloat(mCenterY);
        dest.writeFloat(mScaleX);
        dest.writeFloat(mScaleY);
        dest.writeFloat(mAngle);
        dest.writeFloat(mMinX);
        dest.writeFloat(mMaxX);
        dest.writeFloat(mMinY);
        dest.writeFloat(mMaxY);
        dest.writeFloat(mGrabAreaX1);
        dest.writeFloat(mGrabAreaY1);
        dest.writeFloat(mGrabAreaX2);
        dest.writeFloat(mGrabAreaY2);
        dest.writeFloat(mStartMidX);
        dest.writeFloat(mStartMidY);
        dest.writeInt(mUIMode);
    }

    public void readFromParcel(Parcel in) {
        boolean[] val = new boolean[3];
        in.readBooleanArray(val);
        mFirstLoad = val[0];
        mIsGrabAreaSelected = val[1];
        mIsLatestSelected = val[2];
        mWidth = in.readInt();
        mHeight = in.readInt();
        mDisplayWidth = in.readInt();
        mDisplayHeight = in.readInt();
        mCenterX = in.readFloat();
        mCenterY = in.readFloat();
        mScaleX = in.readFloat();
        mScaleY = in.readFloat();
        mAngle = in.readFloat();
        mMinX = in.readFloat();
        mMaxX = in.readFloat();
        mMinY = in.readFloat();
        mMaxY = in.readFloat();
        mGrabAreaX1 = in.readFloat();
        mGrabAreaY1 = in.readFloat();
        mGrabAreaX2 = in.readFloat();
        mGrabAreaY2 = in.readFloat();
        mStartMidX = in.readFloat();
        mStartMidY = in.readFloat();
        mUIMode = in.readInt();
    }
}
