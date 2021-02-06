package com.kessi.photopipcollagemaker.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.kessi.photopipcollagemaker.frame.MultiTouchHandler;
import com.kessi.photopipcollagemaker.utils.ImageDecoder;
import com.kessi.photopipcollagemaker.utils.ImageUtils;
import com.kessi.photopipcollagemaker.utils.PhotoUtils;
import com.kessi.photopipcollagemaker.utils.ResultContainer;


public class ItemImageView extends ImageView {
    private static final String TAG = ItemImageView.class.getSimpleName();

    public interface OnImageClickListener {
        void onLongClickImage(ItemImageView view);

        void onDoubleClickImage(ItemImageView view);
    }

    private final GestureDetector mGestureDetector;
    private MultiTouchHandler mTouchHandler;
    private Bitmap mImage;
    private Bitmap mMaskImage;
    private Paint mPaint;
    private Matrix mImageMatrix;
    private Matrix mScaleMatrix;
    private Matrix mMaskMatrix;
    private Matrix mScaleMaskMatrix;
    private PhotoItem mPhotoItem;
    private float mViewWidth, mViewHeight;
    private float mOutputScale = 1;
    private OnImageClickListener mOnImageClickListener;
    private RelativeLayout.LayoutParams mOriginalLayoutParams;
    private boolean mEnableTouch = true;

    public ItemImageView(Context context, PhotoItem photoItem) {
        super(context);
        mPhotoItem = photoItem;
        if (photoItem.imagePath != null && photoItem.imagePath.length() > 0) {
            mImage = ResultContainer.getInstance().getImage(photoItem.imagePath);
            if (mImage == null || mImage.isRecycled()) {
                mImage = ImageDecoder.decodeFileToBitmap(photoItem.imagePath);
                ResultContainer.getInstance().putImage(photoItem.imagePath, mImage);
                Logger.d(TAG, "create ItemImageView, decode image");
            } else {
                Logger.d(TAG, "create ItemImageView, use decoded image");
            }
        }

        if (photoItem.maskPath != null && photoItem.maskPath.length() > 0) {
            mMaskImage = ResultContainer.getInstance().getImage(photoItem.maskPath);
            if (mMaskImage == null || mMaskImage.isRecycled()) {
                mMaskImage = PhotoUtils.decodePNGImage(context, photoItem.maskPath);
                ResultContainer.getInstance().putImage(photoItem.maskPath, mMaskImage);
                Logger.d(TAG, "create ItemImageView, decode mask image");
            } else {
                Logger.d(TAG, "create ItemImageView, use decoded mask image");
            }
        }

        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        setScaleType(ScaleType.MATRIX);
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);
        mImageMatrix = new Matrix();
        mScaleMatrix = new Matrix();
        mMaskMatrix = new Matrix();
        mScaleMaskMatrix = new Matrix();

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onLongClickImage(ItemImageView.this);
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }
        });
    }

    public void swapImage(ItemImageView view) {
        Bitmap temp = view.getImage();
        view.setImage(mImage);
        mImage = temp;

        String tmpPath = view.getPhotoItem().imagePath;
        view.getPhotoItem().imagePath = mPhotoItem.imagePath;
        mPhotoItem.imagePath = tmpPath;
        resetImageMatrix();
        view.resetImageMatrix();
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    public void setOriginalLayoutParams(RelativeLayout.LayoutParams originalLayoutParams) {
        mOriginalLayoutParams = new RelativeLayout.LayoutParams(originalLayoutParams.width, originalLayoutParams.height);
        mOriginalLayoutParams.leftMargin = originalLayoutParams.leftMargin;
        mOriginalLayoutParams.topMargin = originalLayoutParams.topMargin;
    }

    public RelativeLayout.LayoutParams getOriginalLayoutParams() {
        if (mOriginalLayoutParams != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mOriginalLayoutParams.width, mOriginalLayoutParams.height);
            params.leftMargin = mOriginalLayoutParams.leftMargin;
            params.topMargin = mOriginalLayoutParams.topMargin;
            return params;
        } else {
            return (RelativeLayout.LayoutParams) getLayoutParams();
        }
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    public PhotoItem getPhotoItem() {
        return mPhotoItem;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public Bitmap getMaskImage() {
        return mMaskImage;
    }

    @Override
    public Matrix getImageMatrix() {
        return mImageMatrix;
    }

    public Matrix getMaskMatrix() {
        return mMaskMatrix;
    }

    public Matrix getScaleMatrix() {
        return mScaleMatrix;
    }

    public Matrix getScaleMaskMatrix() {
        return mScaleMaskMatrix;
    }

    public float getViewWidth() {
        return mViewWidth;
    }

    public float getViewHeight() {
        return mViewHeight;
    }

    public void init(final float viewWidth, final float viewHeight, final float scale) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mOutputScale = scale;
        if (mImage != null) {
            mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(viewWidth, viewHeight, mImage.getWidth(), mImage.getHeight()));
            mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(scale * viewWidth, scale * viewHeight, mImage.getWidth(), mImage.getHeight()));
        }
        //mask
        if (mMaskImage != null) {
            mMaskMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(viewWidth, viewHeight, mMaskImage.getWidth(), mMaskImage.getHeight()));
            mScaleMaskMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(scale * viewWidth, scale * viewHeight, mMaskImage.getWidth(), mMaskImage.getHeight()));
        }

        mTouchHandler = new MultiTouchHandler();
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        mTouchHandler.setScale(scale);
        mTouchHandler.setEnableRotation(true);
        invalidate();
    }

    public void setImagePath(String imagePath) {
        mPhotoItem.imagePath = imagePath;
        recycleMainImage();
        mImage = ImageDecoder.decodeFileToBitmap(imagePath);
        mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mViewWidth, mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mOutputScale * mViewWidth, mOutputScale * mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        invalidate();
        ResultContainer.getInstance().putImage(mPhotoItem.imagePath, mImage);
    }

    public void resetImageMatrix() {
        mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mViewWidth, mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mOutputScale * mViewWidth, mOutputScale * mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        invalidate();
    }

    public void clearMainImage() {
        mPhotoItem.imagePath = null;
        recycleMainImage();
        invalidate();
    }

    private void recycleMainImage() {
        if (mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
            mImage = null;
            System.gc();
        }
    }

    private void recycleMaskImage() {
        if (mMaskImage != null && !mMaskImage.isRecycled()) {
            mMaskImage.recycle();
            mMaskImage = null;
            System.gc();
        }
    }

    public void recycleImages(boolean recycleMainImage) {
        Logger.d(TAG, "recycleImages, recycleMainImage=" + recycleMainImage);
        if (recycleMainImage) {
            recycleMainImage();
        }
        recycleMaskImage();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mImage != null && !mImage.isRecycled() && mMaskImage != null && !mMaskImage.isRecycled()) {
            canvas.drawBitmap(mImage, mImageMatrix, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mMaskImage, mMaskMatrix, mPaint);
            mPaint.setXfermode(null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableTouch) {
            return super.onTouchEvent(event);
        } else {
            mGestureDetector.onTouchEvent(event);
            if (mTouchHandler != null && mImage != null && !mImage.isRecycled()) {
                mTouchHandler.touch(event);
                mImageMatrix.set(mTouchHandler.getMatrix());
                mScaleMatrix.set(mTouchHandler.getScaleMatrix());
                invalidate();
                return true;
            } else {
                return true;
            }
        }
    }

    public void setEnableTouch(boolean enableTouch) {
        mEnableTouch = enableTouch;
    }
}
