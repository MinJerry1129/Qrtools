package com.kessi.photopipcollagemaker.Activities.custom;

import android.view.MotionEvent;

public class DoubleClickDetector {
	// DOUBLE CLICKS
	private static final long DOUBLE_CLICK_TIME_INTERVAL = 700;
	private int mSelectedCount = 0;
	private long mSelectedTime = System.currentTimeMillis();
	private float mOldX = 0;
	private float mOldY = 0;
	private float mTouchAreaInterval = 10;

	public void reset() {
		mSelectedCount = 0;
		mSelectedTime = System.currentTimeMillis();
		mOldX = 0;
		mOldY = 0;
	}

	public void setTouchAreaInterval(float touchAreaInterval) {
		mTouchAreaInterval = touchAreaInterval;
	}

	public boolean doubleClick(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_DOWN) {
			return false;
		}

		long currentTime = System.currentTimeMillis();
		if (mSelectedCount == 0) {
			mSelectedCount = 1;
			mSelectedTime = currentTime;
			mOldX = event.getX();
			mOldY = event.getY();
		} else {
			// check event
			if (currentTime - mSelectedTime < DOUBLE_CLICK_TIME_INTERVAL) {
				final float x = event.getX();
				final float y = event.getY();
				if (mOldX + mTouchAreaInterval > x
						&& mOldX - mTouchAreaInterval < x
						&& mOldY + mTouchAreaInterval > y
						&& mOldY - mTouchAreaInterval < y) {
					mSelectedCount++;
				} else {
					mSelectedCount = 1;
					mSelectedTime = currentTime;
					mOldX = event.getX();
					mOldY = event.getY();
				}
			} else {
				mSelectedCount = 1;
				mSelectedTime = currentTime;
				mOldX = event.getX();
				mOldY = event.getY();
			}
			// Double click
			if (mSelectedCount == 2) {
				mSelectedCount = 0;
				mOldX = 0;
				mOldY = 0;
				return true;
			}
		}

		return false;
	}

}
