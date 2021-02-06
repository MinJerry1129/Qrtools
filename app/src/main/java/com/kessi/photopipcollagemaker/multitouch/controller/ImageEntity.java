package com.kessi.photopipcollagemaker.multitouch.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.utils.ImageDecoder;

public class ImageEntity extends MultiTouchEntity {
    private double mInitScaleFactor = 0.25;

    private transient Drawable mDrawable;

    private Uri mImageUri = null;
    private int mResourceId = -1;
    private boolean mDrawImageBorder = false;
    private int mBorderColor = Color.GREEN;
    private float mBorderSize = 3;
    private RectF mBoundRect = new RectF();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mSticker = true;
    private GradientDrawable mGradientDrawable = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR, new int[]{Color.TRANSPARENT,
            Color.GRAY});
    private boolean mDrawShadow = false;
    private int mShadowSize = 0;

    public ImageEntity(int resourceId, Resources res) {
        super(res);
        mResourceId = resourceId;
        mImageUri = null;
        loadConfigs(res);
    }

    public ImageEntity(Uri image, Resources res) {
        super(res);
        mImageUri = image;
        mResourceId = -1;
        loadConfigs(res);
    }

    public ImageEntity(ImageEntity e, Resources res) {
        super(res);
        mDrawable = e.mDrawable;
        mResourceId = e.mResourceId;
        mScaleX = e.mScaleX;
        mScaleY = e.mScaleY;
        mCenterX = e.mCenterX;
        mCenterY = e.mCenterY;
        mAngle = e.mAngle;
        mImageUri = e.mImageUri;
        loadConfigs(res);
    }

    protected void loadConfigs(Resources res) {
        mBorderSize = res.getDimension(R.dimen.image_border_size);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderSize);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public void setDrawImageBorder(boolean drawImageBorder) {
        mDrawImageBorder = drawImageBorder;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public boolean isDrawImageBorder() {
        return mDrawImageBorder;
    }

    public void setSticker(boolean sticker) {
        mSticker = sticker;
    }

    public boolean isSticker() {
        return mSticker;
    }

    public void setShadowSize(int shadowSize) {
        mShadowSize = shadowSize;
    }

    public void setDrawShadow(boolean drawShadow) {
        mDrawShadow = drawShadow;
    }

    public void setInitScaleFactor(double initScaleFactor) {
        mInitScaleFactor = initScaleFactor;
    }

    public void setBorderSize(float borderSize) {
        mBorderSize = borderSize;
        mPaint.setStrokeWidth(mBorderSize);
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public void setImageUri(Context context, Uri imageUri) {
        unload();
        mImageUri = imageUri;
        load(context);
    }

    public int getResourceId() {
        return mResourceId;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public void draw(Canvas canvas) {
        draw(canvas, 1);
    }

    public void draw(Canvas canvas, float scale) {
        canvas.save();
        if (mDrawable == null) {
            return;
        }

        if (mDrawable instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) mDrawable).getBitmap();
            if (bm == null || bm.isRecycled()) {
                return;
            }
        }

        float dx = scale * (mMaxX + mMinX) / 2;
        float dy = scale * (mMaxY + mMinY) / 2;

        mDrawable.setBounds((int) (scale * mMinX), (int) (scale * mMinY), (int) (scale * mMaxX), (int) (scale * mMaxY));

        canvas.translate(dx, dy);
        canvas.rotate(mAngle * 180.0f / (float) Math.PI);
        canvas.translate(-dx, -dy);

        if (mDrawShadow && !mSticker && mShadowSize > 1) {
            drawShadow(canvas, scale);
        }

        mDrawable.draw(canvas);
        // Draw bound
//        if (mDrawImageBorder && !mSticker) {
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(mBorderSize);
//            mPaint.setColor(mBorderColor);
//            mBoundRect.set(scale * mMinX, scale * mMinY, scale * mMaxX, scale * mMaxY);
//            canvas.drawRect(mBoundRect, mPaint);
//        }

        canvas.restore();
    }

    private void drawShadow(Canvas canvas, float scale) {
        mGradientDrawable.setBounds((int) (scale * (mMinX + mShadowSize)), (int) (scale * (mMinY
                + mShadowSize)), (int) (scale * (mMaxX + mShadowSize)), (int) (scale * (mMaxY
                + mShadowSize)));
        mGradientDrawable.setCornerRadius(5);
        mGradientDrawable.draw(canvas);
    }

    public boolean isNull() {
        if (mImageUri != null || mResourceId > 0) {
            return false;
        }

        return true;
    }

    /**
     * Called by activity's onPause() method to free memory used for loading the
     * images
     */
    @Override
    public void unload() {
        if ((mDrawable instanceof BitmapDrawable) && mDrawable != null) {
            Bitmap bm = ((BitmapDrawable) mDrawable).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }

        this.mDrawable = null;
    }

    @Override
    public void load(Context context) {
        Resources res = context.getResources();
        getMetrics(res);

        if (mDrawable == null) {
            mDrawable = createDrawableFromPrimaryInfo(context);
        }
        if (mDrawable == null) {
            if (mImageUri != null) {
                resetPrimaryInfo();
            }
            return;
        }

        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();

        setPos(mCenterX, mCenterY, mScaleX, mScaleY, mAngle);
    }

    /**
     * Called by activity's onResume() method to load the images
     */
    public void load(Context context, float startMidX, float startMidY,
                     float startAngle) {
        Resources res = context.getResources();
        getMetrics(res);

        mStartMidX = startMidX;
        mStartMidY = startMidY;
        if (mDrawable == null) {
            mDrawable = createDrawableFromPrimaryInfo(context);
        }

        if (mDrawable == null) {
            if (mImageUri != null) {
                resetPrimaryInfo();
            }
            return;
        }

        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();

        float centerX;
        float centerY;
        float scaleX;
        float scaleY;
        // float angle;
        if (mFirstLoad) {
            centerX = startMidX;
            centerY = startMidY;

            float scaleFactor = (float) (Math
                    .min(mDisplayWidth, mDisplayHeight)
                    / (float) Math.max(mWidth, mHeight) * mInitScaleFactor);
            scaleX = scaleY = scaleFactor;
            mAngle = startAngle;

            mFirstLoad = false;
        } else {
            centerX = mCenterX;
            centerY = mCenterY;
            scaleX = mScaleX;
            scaleY = mScaleY;
            // angle = mAngle;
        }
        setPos(centerX, centerY, scaleX, scaleY, mAngle);
    }

    @Override
    public void load(Context context, float startMidX, float startMidY) {
        load(context, startMidX, startMidY, 0);
    }

    protected Drawable createDrawableFromPrimaryInfo(Context context) {
        Resources res = context.getResources();
        Drawable drawable = null;
        if (mImageUri != null) {
            drawable = ImageDecoder
                    .decodeUriToDrawable(context, mImageUri);
        } else if (mResourceId > 0) {
            drawable = res.getDrawable(mResourceId);
        }

        return drawable;
    }

    protected void resetPrimaryInfo() {
        mImageUri = null;
        mResourceId = -1;
    }

    // parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(mInitScaleFactor);
        dest.writeParcelable(mImageUri, flags);
        dest.writeInt(mResourceId);
        dest.writeBooleanArray(new boolean[]{mDrawImageBorder, mSticker});
        dest.writeInt(mBorderColor);
        dest.writeFloat(mBorderSize);
        dest.writeParcelable(mBoundRect, flags);
    }

    public static final Parcelable.Creator<ImageEntity> CREATOR = new Parcelable.Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel in) {
            return new ImageEntity(in);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mInitScaleFactor = in.readDouble();
        mImageUri = in.readParcelable(Uri.class.getClassLoader());
        mResourceId = in.readInt();
        boolean[] val = new boolean[2];
        in.readBooleanArray(val);
        mDrawImageBorder = val[0];
        mSticker = val[1];
        mBorderColor = in.readInt();
        mBorderSize = in.readFloat();
        mBoundRect = in.readParcelable(RectF.class.getClassLoader());
    }

    protected ImageEntity(Parcel in) {
        readFromParcel(in);
    }
}
