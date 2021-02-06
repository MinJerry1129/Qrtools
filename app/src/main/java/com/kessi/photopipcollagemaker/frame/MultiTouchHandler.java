package com.kessi.photopipcollagemaker.frame;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;

public class MultiTouchHandler implements Parcelable {
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    // these matrices will be used to move and zoom image
    private Matrix mMatrix = new Matrix();
    private Matrix mSavedMatrix = new Matrix();

    private int mMode = NONE;
    // remember some things for zooming
    private PointF mStart = new PointF();
    private PointF mMid = new PointF();
    private float mOldDist = 1f;
    private float mD = 0f;
    private float mNewRot = 0f;
    private float[] mLastEvent = null;
    private boolean mEnableRotation = false;
    private boolean mEnableZoom = true;
    private boolean mEnableTranslateX = true;
    private boolean mEnableTranslateY = true;
    // for scale
    private float mScale = 1.0f;
    private Matrix mScaleMatrix = new Matrix();
    private Matrix mScaleSavedMatrix = new Matrix();
    private float mMaxPositionOffset = -1;
    private PointF mOldImagePosition = new PointF(0, 0);
    private PointF mCheckingPosition = new PointF(0, 0);

    public MultiTouchHandler() {

    }

    public void setMatrix(Matrix matrix) {
        this.mMatrix.set(matrix);
        mSavedMatrix.set(matrix);
        mScaleMatrix.reset();
        mScaleSavedMatrix.reset();
    }

    public void setMatrices(Matrix matrix, Matrix scaleMatrix) {
        this.mMatrix.set(matrix);
        mSavedMatrix.set(matrix);
        mScaleMatrix.set(scaleMatrix);
        mScaleSavedMatrix.set(scaleMatrix);
    }

    public void reset() {
        this.mMatrix.reset();
        this.mSavedMatrix.reset();
        mMode = NONE;
        mStart.set(0, 0);
        mMid.set(0, 0);
        mOldDist = 1f;
        mD = 0f;
        mNewRot = 0f;
        mLastEvent = null;
        mEnableRotation = false;
        // scale
        this.mScaleMatrix.reset();
        this.mScaleSavedMatrix.reset();
    }

    public void setMaxPositionOffset(float maxPositionOffset) {
        mMaxPositionOffset = maxPositionOffset;
    }

    public void touch(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mSavedMatrix.set(mMatrix);
                mScaleSavedMatrix.set(mScaleMatrix);
                mStart.set(event.getX(), event.getY());
                mOldImagePosition.set(mCheckingPosition.x, mCheckingPosition.y);
                mMode = DRAG;
                mLastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDist = spacing(event);
                if (mOldDist > 10f) {
                    mSavedMatrix.set(mMatrix);
                    mScaleSavedMatrix.set(mScaleMatrix);
                    midPoint(mMid, event);
                    mMode = ZOOM;
                }
                mLastEvent = new float[4];
                mLastEvent[0] = event.getX(0);
                mLastEvent[1] = event.getX(1);
                mLastEvent[2] = event.getY(0);
                mLastEvent[3] = event.getY(1);
                mD = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mMode = NONE;
                mLastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == DRAG) {
                    mMatrix.set(mSavedMatrix);
                    mScaleMatrix.set(mScaleSavedMatrix);
                    mCheckingPosition.set(mOldImagePosition.x, mOldImagePosition.y);

                    float dx = event.getX() - mStart.x;
                    float dy = event.getY() - mStart.y;

                    mCheckingPosition.x += dx;
                    mCheckingPosition.y += dy;
                    if (!mEnableTranslateX) {
                        dx = 0;
                        if (mCheckingPosition.y > mMaxPositionOffset) {
                            dy = dy - (mCheckingPosition.y - mMaxPositionOffset);
                            mCheckingPosition.y = mMaxPositionOffset;
                        } else if (mCheckingPosition.y < -mMaxPositionOffset) {
                            dy = dy - (mCheckingPosition.y + mMaxPositionOffset);
                            mCheckingPosition.y = -mMaxPositionOffset;
                        }
                    }

                    if (!mEnableTranslateY) {
                        dy = 0;
                        if (mCheckingPosition.x > mMaxPositionOffset) {
                            dx = dx - (mCheckingPosition.x - mMaxPositionOffset);
                            mCheckingPosition.x = mMaxPositionOffset;
                        } else if (mCheckingPosition.x < -mMaxPositionOffset) {
                            dx = dx - (mCheckingPosition.x + mMaxPositionOffset);
                            mCheckingPosition.x = -mMaxPositionOffset;
                        }
                    }

                    mMatrix.postTranslate(dx, dy);
                    mScaleMatrix.postTranslate(dx * mScale, dy * mScale);
                } else if (mMode == ZOOM && mEnableZoom) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        mMatrix.set(mSavedMatrix);
                        mScaleMatrix.set(mScaleSavedMatrix);
                        float scale = (newDist / mOldDist);
                        mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                        mScaleMatrix.postScale(scale, scale, mMid.x * mScale, mMid.y * mScale);
                    }

                    if (mEnableRotation && mLastEvent != null && event.getPointerCount() == 2) {
                        mNewRot = rotation(event);
                        midPoint(mMid, event);
                        float r = mNewRot - mD;
                        mMatrix.postRotate(r, mMid.x, mMid.y);
                        mScaleMatrix.postRotate(r, mMid.x * mScale, mMid.y * mScale);
                    }
                }
                break;
        }
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public Matrix getScaleMatrix() {
        return mScaleMatrix;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public void setEnableRotation(boolean enableRotation) {
        mEnableRotation = enableRotation;
    }

    public void setEnableZoom(boolean enableZoom) {
        mEnableZoom = enableZoom;
    }

    public void setEnableTranslateX(boolean enableTranslateX) {
        mEnableTranslateX = enableTranslateX;
    }

    public void setEnableTranslateY(boolean enableTranslateY) {
        mEnableTranslateY = enableTranslateY;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public static final Creator<MultiTouchHandler> CREATOR = new Creator<MultiTouchHandler>() {
        public MultiTouchHandler createFromParcel(Parcel in) {
            return new MultiTouchHandler(in);
        }

        public MultiTouchHandler[] newArray(int size) {
            return new MultiTouchHandler[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private MultiTouchHandler(Parcel in) {
        float[] values = new float[9];
        in.readFloatArray(values);
        mMatrix = new Matrix();
        mMatrix.setValues(values);

        values = new float[9];
        in.readFloatArray(values);
        mSavedMatrix = new Matrix();
        mSavedMatrix.setValues(values);

        mMode = in.readInt();
        mStart = in.readParcelable(PointF.class.getClassLoader());
        mMid = in.readParcelable(PointF.class.getClassLoader());
        mOldDist = in.readFloat();
        mD = in.readFloat();
        mNewRot = in.readFloat();
        boolean[] b = new boolean[4];
        in.readBooleanArray(b);
        mEnableRotation = b[0];
        mEnableZoom = b[1];
        mEnableTranslateX = b[2];
        mEnableTranslateY = b[3];
        mScale = in.readFloat();

        values = new float[9];
        in.readFloatArray(values);
        mScaleMatrix = new Matrix();
        mScaleMatrix.setValues(values);

        values = new float[9];
        in.readFloatArray(values);
        mScaleSavedMatrix = new Matrix();
        mScaleSavedMatrix.setValues(values);

        mMaxPositionOffset = in.readFloat();
        mOldImagePosition = in.readParcelable(PointF.class.getClassLoader());
        mCheckingPosition = in.readParcelable(PointF.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        float[] values = new float[9];
        mMatrix.getValues(values);
        dest.writeFloatArray(values);

        values = new float[9];
        mSavedMatrix.getValues(values);
        dest.writeFloatArray(values);

        dest.writeInt(mMode);
        dest.writeParcelable(mStart, flags);
        dest.writeParcelable(mMid, flags);
        dest.writeFloat(mOldDist);
        dest.writeFloat(mD);
        dest.writeFloat(mNewRot);

        boolean[] b = {mEnableRotation, mEnableZoom, mEnableTranslateX, mEnableTranslateY};
        dest.writeBooleanArray(b);
        dest.writeFloat(mScale);

        values = new float[9];
        mScaleMatrix.getValues(values);
        dest.writeFloatArray(values);

        values = new float[9];
        mScaleSavedMatrix.getValues(values);
        dest.writeFloatArray(values);

        dest.writeFloat(mMaxPositionOffset);
        dest.writeParcelable(mOldImagePosition, flags);
        dest.writeParcelable(mCheckingPosition, flags);
    }
}
