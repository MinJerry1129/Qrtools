package com.kessi.photopipcollagemaker.multitouch.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;


public class TextEntity extends ImageEntity {
    private String mText;
    private int mTextColor = Color.BLACK;
    private float mTextSize = 18;
    private String mTypefacePath = "";
    private float mCurrentScale = 0;

    public TextEntity(String text, Resources res) {
        super(-1, res);
        mText = text;
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP ,
                36, res.getDisplayMetrics());
    }

    @Override
    protected Drawable createDrawableFromPrimaryInfo(Context context) {
        final TextDrawable textDrawable = new TextDrawable(mTextSize, mText);
        textDrawable.setTextSize(mTextSize);
        textDrawable.setTextColor(mTextColor);
        textDrawable.setTypefacePath(mTypefacePath, context);
        return textDrawable;
    }

    @Override
    public boolean isNull() {
        return mText == null || mText.trim().length() == 0;
    }

    public void setText(String text) {
        mText = text;
        if (getDrawable() != null)
            ((TextDrawable) getDrawable()).setText(mText);
    }

    public void setTextColor(int color) {
        mTextColor = color;
        if (getDrawable() != null)
            ((TextDrawable) getDrawable()).setTextColor(mTextColor);
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        if (getDrawable() != null)
            ((TextDrawable) getDrawable()).setTextSize(mTextSize);
    }

    public void setTypefacePath(String typefacePath, Context context) {
        mTypefacePath = typefacePath;
        if (getDrawable() != null)
            ((TextDrawable) getDrawable()).setTypefacePath(mTypefacePath, context);
    }

    @Override
    public void draw(Canvas canvas, float scale) {
        if (scale == 1) {
            super.draw(canvas, scale);
        } else {
            canvas.save();
            final TextDrawable textDrawable = (TextDrawable) getDrawable();
            float dx = scale * (mMaxX + mMinX) / 2;
            float dy = scale * (mMaxY + mMinY) / 2;
            float textSize = textDrawable.findSuitableTextSize(scale * mMaxX - scale * mMinX, scale * mMaxY - scale * mMinY, scale < 1);
            float oldTextSize = textDrawable.getTextSize();
            textDrawable.setTextSize(textSize);
            textDrawable.setBounds((int) (scale * mMinX), (int) (scale * mMinY), (int) (scale * mMaxX), (int) (scale * mMaxY));
            canvas.translate(dx, dy);
            canvas.rotate(mAngle * 180.0f / (float) Math.PI);
            canvas.translate(-dx, -dy);
            textDrawable.draw(canvas);
            canvas.restore();
            textDrawable.setTextSize(oldTextSize);
        }
    }

    @Override
    protected float[] calculateHalfDrawableSize(float scaleX, float scaleY) {
        TextDrawable drawable = (TextDrawable) getDrawable();
        if (mCurrentScale > 0) {
            float ts = 50 * (scaleX / mCurrentScale - 1);
            mTextSize = Math.max(12, mTextSize + ts);
            drawable.setTextSize(mTextSize);
        }
        mCurrentScale = scaleX;
        float[] size = new float[2];
        size[0] = drawable.getIntrinsicWidth() / 2;
        size[1] = drawable.getIntrinsicHeight() / 2;
        return size;
    }

    // parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mText);
        dest.writeInt(mTextColor);
        dest.writeFloat(mTextSize);
        dest.writeString(mTypefacePath);
    }

    public static final Parcelable.Creator<TextEntity> CREATOR = new Parcelable.Creator<TextEntity>() {
        @Override
        public TextEntity createFromParcel(Parcel in) {
            return new TextEntity(in);
        }

        @Override
        public TextEntity[] newArray(int size) {
            return new TextEntity[size];
        }
    };

    private TextEntity(Parcel in) {
        super(in);
        mText = in.readString();
        mTextColor = in.readInt();
        mTextSize = in.readFloat();
        mTypefacePath = in.readString();
    }
}
