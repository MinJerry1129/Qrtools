package com.kessi.photopipcollagemaker.Activities.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;


import com.kessi.photopipcollagemaker.frame.MultiTouchHandler;
import com.kessi.photopipcollagemaker.utils.ImageDecoder;
import com.kessi.photopipcollagemaker.utils.ImageUtils;


public class TransitionImageView extends ImageView {
    public interface OnImageClickListener {
        void onLongClickImage(TransitionImageView view);

        void onDoubleClickImage(TransitionImageView view);
    }

    private final GestureDetector mGestureDetector;
    private MultiTouchHandler mTouchHandler;
    private Matrix mImageMatrix;
    private Matrix mScaleMatrix;
    private Bitmap mImage;
    private Paint mPaint;
    private OnImageClickListener mOnImageClickListener;
    private int mViewWidth, mViewHeight;
    private float mScale = 1;

    public TransitionImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mImageMatrix = new Matrix();
        mScaleMatrix = new Matrix();
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onLongClickImage(TransitionImageView.this);
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onDoubleClickImage(TransitionImageView.this);
                }
                return true;
            }
        });
    }

    public void reset() {
        mTouchHandler = null;
        setScaleType(ScaleType.MATRIX);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    public void recycleImages() {
        if (mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
            mImage = null;
            System.gc();
            invalidate();
        }
    }

    public void setImagePath(String path) {
        recycleImages();
        Bitmap image = ImageDecoder.decodeFileToBitmap(path);
        if (image != null)
            init(image, mViewWidth, mViewHeight, mScale);
    }

    public void init(final Bitmap image, final int viewWidth, final int viewHeight, final float scale) {
        mImage = image;
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mScale = scale;
        if (mImage != null) {
            mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(viewWidth, viewHeight, mImage.getWidth(), mImage.getHeight()));
            mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(scale * viewWidth, scale * viewHeight, mImage.getWidth(), mImage.getHeight()));
        }

        mTouchHandler = new MultiTouchHandler();
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        mTouchHandler.setScale(scale);
        mTouchHandler.setEnableRotation(false);
        mTouchHandler.setEnableZoom(false);
        final float ratioWidth = ((float) viewWidth) / mImage.getWidth();
        final float ratioHeight = ((float) viewHeight) / mImage.getHeight();
        if (ratioWidth > ratioHeight) {
            mTouchHandler.setEnableTranslateX(false);
            float maxOffset = (ratioWidth * mImage.getHeight() - viewHeight) / 2.0f;
            mTouchHandler.setMaxPositionOffset(maxOffset);
        } else {
            mTouchHandler.setEnableTranslateY(false);
            float maxOffset = (ratioHeight * mImage.getWidth() - viewWidth) / 2.0f;
            mTouchHandler.setMaxPositionOffset(maxOffset);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mImage != null && !mImage.isRecycled()) {
            canvas.drawBitmap(mImage, mImageMatrix, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        if (mTouchHandler != null && mImage != null && !mImage.isRecycled()) {
            mTouchHandler.touch(event);
            mImageMatrix.set(mTouchHandler.getMatrix());
            mScaleMatrix.set(mTouchHandler.getScaleMatrix());
            invalidate();
        }
        return true;
    }

    @Override
    public Matrix getImageMatrix() {
        return mImageMatrix;
    }

    public Matrix getScaleMatrix() {
        return mScaleMatrix;
    }

    public Bitmap getImage() {
        return mImage;
    }
}
