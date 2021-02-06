package com.kessi.photopipcollagemaker.Activities.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.kessi.photopipcollagemaker.listener.OnChooseRGBListener;


@SuppressLint("ClickableViewAccessibility")
public class ColorChooserView extends ImageView {
	private static final String TAG = ColorChooserView.class.getSimpleName();
	private float mCircleSmallRadius;
	private float mCircleLargeRadius;
	private int mCircleBorderColor;
	private int mCircleColor;
	private float mStrokeWidth;

	private float mCurrentX;
	private float mCurrentY;
	private Bitmap mColorImage;
	private Paint mPaint = new Paint();
	private OnChooseRGBListener mListener;

	public ColorChooserView(Context context) {
		super(context);
		init();
	}

	public ColorChooserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorChooserView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// check recyled images
		Logger.d(TAG, "onDraw");
		Drawable d = getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap bm = ((BitmapDrawable) d).getBitmap();
			if (bm != null && bm.isRecycled())
				setImageBitmap(null);
		}

		d = getBackground();
		if (d instanceof BitmapDrawable) {
			Bitmap bm = ((BitmapDrawable) d).getBitmap();
			if (bm != null && bm.isRecycled())
				setBackgroundColor(Color.TRANSPARENT);
		}
		Logger.d(TAG, "start onDraw");
		// draw
		super.onDraw(canvas);
		if (getWidth() < 10 && getWidth() < 10) {
			return;
		}
		// correctness of current XY
		if (mCurrentX < 0) {
			mCurrentX = 0;
		}
		if (mCurrentX > getWidth()) {
			mCurrentX = getWidth();
		}

		if (mCurrentY < 0) {
			mCurrentY = 0;
		}

		if (mCurrentY > getHeight()) {
			mCurrentY = getHeight();
		}
		// File circle
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(mCircleBorderColor);
		canvas.drawCircle(mCurrentX, mCurrentY, mCircleLargeRadius, mPaint);
		int color = getSelectedColor();
		mPaint.setColor(color);
		canvas.drawCircle(mCurrentX, mCurrentY, mCircleSmallRadius, mPaint);
		// draw circle
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mCircleColor);
		canvas.drawCircle(mCurrentX, mCurrentY, mCircleLargeRadius, mPaint);
		canvas.drawCircle(mCurrentX, mCurrentY, mCircleSmallRadius, mPaint);

		if (mListener != null) {
			mListener.onChooseRGB(color);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			mCurrentX = event.getX();
			mCurrentY = event.getY();
			invalidate();
			return true;
		}

		return super.onTouchEvent(event);
	}

	public float getCurrentX() {
		return mCurrentX;
	}

	public float getCurrentY() {
		return mCurrentY;
	}

	public void setListener(OnChooseRGBListener listener) {
		mListener = listener;
	}

	public int getSelectedColor() {
		try {
			if (mColorImage == null) {
				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						R.mipmap.ic_launcher);
				mColorImage = Bitmap.createScaledBitmap(bm, getWidth(),
						getHeight(), false);
				if (bm != mColorImage) {
					bm.recycle();
					bm = null;
				}
			}
			int x = (int) Math.max(mCurrentX, 0);
			x = Math.min(x, mColorImage.getWidth() - 1);
			int y = (int) Math.max(mCurrentY, 0);
			y = Math.min(y, mColorImage.getHeight() - 1);

			return mColorImage.getPixel(x, y);
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
		}

		return 0;
	}

	private void init() {
		Logger.d(TAG, "start init");
		mCircleSmallRadius = getContext().getResources().getDimension(
				R.dimen.color_chooser_circle_radius_small);
		mCircleLargeRadius = getContext().getResources().getDimension(
				R.dimen.color_chooser_circle_radius_large);
		mCircleBorderColor = getContext().getResources().getColor(
				R.color.color_chooser_circle_border);
		mCircleColor = getContext().getResources().getColor(
				R.color.color_chooser_cirlce);
		setBackgroundResource(R.mipmap.ic_launcher);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mCurrentX = mCircleLargeRadius / 2;
		mCurrentY = mCircleLargeRadius / 2;
		mStrokeWidth = getContext().getResources().getDimension(
				R.dimen.stroke_width);
		mPaint.setStrokeWidth(mStrokeWidth);
		Logger.d(TAG, "end init");
	}

	public void recyleImages() {
		if (mColorImage != null && !mColorImage.isRecycled()) {
			mColorImage.recycle();
			mColorImage = null;
		}

		Drawable d = getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap bm = ((BitmapDrawable) d).getBitmap();
			setImageBitmap(null);
			if (bm != null && !bm.isRecycled()) {
				bm.recycle();
				bm = null;
			}
		}

	}
}
