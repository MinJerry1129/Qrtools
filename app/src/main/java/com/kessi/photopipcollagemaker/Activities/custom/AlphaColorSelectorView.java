package com.kessi.photopipcollagemaker.Activities.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.listener.OnChooseAlphaColorListener;


@SuppressLint("ClickableViewAccessibility")
public class AlphaColorSelectorView extends ImageView {
	private static final int MAX_ALPHA = 255;
	private static final int MIN_ALPHA = (int) (0.4 * MAX_ALPHA);

	private int mCircleColor;
	private float mStrokeWidth;
	private float mCircleSmallRadius;
	private float mCircleLargeRadius;
	private int mCircleBorderColor;
	private float mCurrentX;
	private int mOriginalRed;
	private int mOriginalGreen;
	private int mOriginalBlue;

	private Paint mPaint = new Paint();
	private OnChooseAlphaColorListener mListener;

	public AlphaColorSelectorView(Context context) {
		super(context);
		init();
	}

	public AlphaColorSelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AlphaColorSelectorView(Context context, AttributeSet attrs,
                                  int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getWidth() < 10) {
			return;
		}
		// draw color
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		final float range = getWidth() - 2 * mCircleLargeRadius;
		final float d = range / (MAX_ALPHA - MIN_ALPHA + 1);
		float left, right, top = getHeight() / 2 - (mCircleSmallRadius - 5), bottom = getHeight()
				/ 2 + (mCircleSmallRadius - 5);
		for (int idx = MAX_ALPHA; idx >= MIN_ALPHA; idx--) {
			left = d * (MAX_ALPHA - idx);
			right = left + d;
			int color = Color.argb(idx, mOriginalRed, mOriginalGreen,
					mOriginalBlue);
			mPaint.setColor(color);
			canvas.drawRect(left, top, right, bottom, mPaint);
		}
		// draw circle chooser
		// File circle
		if (mCurrentX > range) {
			mCurrentX = range;
		}

		if (mCurrentX < 0) {
			mCurrentX = 0;
		}

		final float mCurrentY = getHeight() / 2;
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
			mListener.onChooseColor(color);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			mCurrentX = event.getX();
			invalidate();
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

	public void setListener(OnChooseAlphaColorListener listener) {
		mListener = listener;
	}

	public void setOriginalColor(int originalColor) {
		mOriginalRed = Color.red(originalColor);
		mOriginalGreen = Color.green(originalColor);
		mOriginalBlue = Color.blue(originalColor);
		invalidate();
	}

	public int getSelectedColor() {
		int alpha = (int) (MAX_ALPHA - (mCurrentX - mCircleLargeRadius)
				/ (getWidth() - 2 * mCircleLargeRadius)
				* (MAX_ALPHA - MIN_ALPHA));
		if (alpha < MIN_ALPHA) {
			alpha = MIN_ALPHA;
		}

		if (alpha > MAX_ALPHA) {
			alpha = MAX_ALPHA;
		}

		return Color.argb(alpha, mOriginalRed, mOriginalGreen, mOriginalBlue);
	}

	private void init() {
		mCircleColor = getContext().getResources().getColor(
				R.color.color_chooser_cirlce);
		mCircleSmallRadius = getContext().getResources().getDimension(
				R.dimen.color_chooser_circle_radius_small);
		mCircleLargeRadius = getContext().getResources().getDimension(
				R.dimen.color_chooser_circle_radius_large);
		mCircleBorderColor = getContext().getResources().getColor(
				R.color.color_chooser_circle_border);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mStrokeWidth = getContext().getResources().getDimension(
				R.dimen.stroke_width);
		mPaint.setStrokeWidth(mStrokeWidth);

	}
}
