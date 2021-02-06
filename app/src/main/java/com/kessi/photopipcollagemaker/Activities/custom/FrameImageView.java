package com.kessi.photopipcollagemaker.Activities.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.kessi.photopipcollagemaker.frame.FrameEntity;
import com.kessi.photopipcollagemaker.frame.FrameTouch;
import com.kessi.photopipcollagemaker.listener.OnFrameTouchListener;
import com.kessi.photopipcollagemaker.utils.ImageDecoder;

/**
 * @deprecated
 */
public class FrameImageView extends ImageView {
	// DOUBLE CLICKS
	private static final long DOUBLE_CLICK_TIME_INTERVAL = 700;
	private int mSelectedCount = 0;
	private long mSelectedTime = System.currentTimeMillis();
	private float mOldX = 0;
	private float mOldY = 0;
	private float mTouchAreaInterval = 10;
	// these matrices will be used to move and zoom image
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	// we can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	// remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	float d = 0f;
	float newRot = 0f;
	float[] lastEvent = null;
	// ////////////////////////////
	private RectF mImageBound;
	// ////////////////////////////////////////////
	private OnGetImageListener mGetImageListener;
	private boolean mGetImageMode = true;
	private OnFrameTouchListener mFrameTouchListener;
	private Uri mImageUri;
	private int mImageWidth = 0;
	private int mImageHeight = 0;
	private float mAddAreaSize = 50;
	private float mFramePositionX = 0;
	private float mFramePositionY = 0;

	public FrameImageView(Context context) {
		super(context);
		init();
	}

	public FrameImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FrameImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setScaleType(ScaleType.MATRIX);
		// photocollage
		mTouchAreaInterval = getResources().getDimension(
				R.dimen.touch_area_interval);
		mAddAreaSize = getResources().getDimension(R.dimen.add_area_size);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable d = getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap bm = ((BitmapDrawable) d).getBitmap();
			if (bm != null && bm.isRecycled()) {
				return;
			}
		}

		super.onDraw(canvas);
	}

	public void loadImage(Uri uri, Matrix m) {
		recycleImage();

		Bitmap bm = ImageDecoder.decodeUriToBitmap(getContext(), uri);
		matrix.set(m);
		setImageMatrix(matrix);
		setImageBitmap(bm);

		this.mImageUri = uri;
		this.mGetImageMode = false;
	}

	public void loadImage(Uri uri, boolean center) {
		recycleImage();

		Bitmap bm = ImageDecoder.decodeUriToBitmap(getContext(), uri);
		if (center) {
			matrix.reset();
			if (bm != null) {
				float dx = (getWidth() - bm.getWidth()) / 2.0f;
				float dy = (getHeight() - bm.getHeight()) / 2.0f;
				matrix.postTranslate(dx, dy);
			}
		}

		setImageMatrix(matrix);
		setImageBitmap(bm);

		this.mImageUri = uri;
		this.mGetImageMode = false;
	}

	public void setStartedImage(int resId) {
		if (!mGetImageMode) {
			return;
		}

		this.mImageUri = null;
		recycleImage();
		Bitmap bm = ImageDecoder.decodeResource(getContext(), resId);
		setImageInCenter(bm);
	}

	public void unloadImage() {
		Drawable d = getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap bm = ((BitmapDrawable) d).getBitmap();
			if (bm != null && !bm.isRecycled()) {
				bm.recycle();
				bm = null;
			}
		}
	}

	private void setImageInCenter(Bitmap bm) {
		recycleImage();
		matrix.reset();
		if (bm != null) {
			float dx = (getWidth() - bm.getWidth()) / 2.0f;
			float dy = (getHeight() - bm.getHeight()) / 2.0f;
			matrix.postTranslate(dx, dy);
		}

		setImageMatrix(matrix);
		setImageBitmap(bm);
	}

	public FrameEntity getFrameEntity() {
		FrameEntity entity = new FrameEntity();
		entity.setImage(mImageUri);
		entity.setMatrix(matrix);
		return entity;
	}

	public Uri getImageUri() {
		return mImageUri;
	}

	public void setGetImageListener(OnGetImageListener listener) {
		mGetImageListener = listener;
	}

	public void setGetImageMode(boolean b) {
		mGetImageMode = b;
	}

	public void setFrameTouchListener(OnFrameTouchListener frameTouchListener) {
		mFrameTouchListener = frameTouchListener;
	}

	public void setImageBound(RectF imageBound) {
		mImageBound = imageBound;
	}

	public void setFramePositionX(float frameLocationX) {
		mFramePositionX = frameLocationX;
	}

	public void setFramePositionY(float frameLocationY) {
		mFramePositionY = frameLocationY;
	}

	public void setFramePosition(float x, float y) {
		mFramePositionX = x;
		mFramePositionY = y;
	}

	public void touch(MotionEvent event) {
		if (mGetImageMode
				&& isTouchAddArea(event.getX() - mFramePositionX, event.getY()
						- mFramePositionY)) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:
				if (mGetImageListener != null) {
					mGetImageListener.onFirstTouch(this);
				}
			}

			return;
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			boolean touched = isTouchImage(event.getX(), event.getY());
			Logger.d("FrameImageView.touch", "isTouchedImage=" + touched);
			doubleClick(event);
			// //////////////////////////////////////
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			lastEvent = null;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			lastEvent = new float[4];
			lastEvent[0] = event.getX(0);
			lastEvent[1] = event.getX(1);
			lastEvent[2] = event.getY(0);
			lastEvent[3] = event.getY(1);
			d = rotation(event);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			lastEvent = null;
			if (mFrameTouchListener != null
					&& mFrameTouchListener instanceof FrameTouch) {
				((FrameTouch) mFrameTouchListener).setImageFrameMoving(false);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mFrameTouchListener != null
					&& mFrameTouchListener instanceof FrameTouch) {
				((FrameTouch) mFrameTouchListener).setImageFrameMoving(true);
			}

			if (mode == DRAG) {
				matrix.set(savedMatrix);
				float dx = event.getX() - start.x;
				float dy = event.getY() - start.y;
				matrix.postTranslate(dx, dy);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = (newDist / oldDist);
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
				if (lastEvent != null && event.getPointerCount() == 3) {
					newRot = rotation(event);
					float r = newRot - d;
					float[] values = new float[9];
					matrix.getValues(values);
					float tx = values[2];
					float ty = values[5];
					float sx = values[0];
					float xc = (getWidth() / 2.0f) * sx;
					float yc = (getHeight() / 2.0f) * sx;
					matrix.postRotate(r, tx + xc, ty + yc);
				}
			}
			break;
		}

		setImageMatrix(matrix);
	}

	private void doubleClick(MotionEvent event) {
		long currentTime = System.currentTimeMillis();
		if (mSelectedCount == 0) {
			mSelectedCount = 1;
			mSelectedTime = currentTime;
			mOldX = event.getX();
			mOldY = event.getY();
		} else {
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
				if (mFrameTouchListener != null) {
					mFrameTouchListener.onFrameDoubleClick(event);
				}
				mSelectedCount = 0;
				mOldX = 0;
				mOldY = 0;
			}
		}
	}

	private void recycleImage() {
		Drawable d = getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap bm = ((BitmapDrawable) d).getBitmap();
			if (bm != null && !bm.isRecycled()) {
				setImageBitmap(null);
				bm.recycle();
				bm = null;
			}
		}
	}

	private boolean isTouchAddArea(float x, float y) {
		final int width = getWidth();
		final int heigh = getHeight();
		return ((width / 2 - mAddAreaSize / 2) < x)
				&& (x < (width / 2 + mAddAreaSize / 2))
				&& ((heigh / 2 - mAddAreaSize / 2) < y)
				&& (y < (heigh / 2 + mAddAreaSize / 2));
	}

	private boolean isTouchImage(float x, float y) {
		float[] A = { 0, 0 };
		float[] B = { mImageWidth, 0 };
		float[] C = { mImageWidth, mImageHeight };
		float[] D = { 0, mImageHeight };

		matrix.mapPoints(A);
		matrix.mapPoints(B);
		matrix.mapPoints(C);
		matrix.mapPoints(D);

		final int nvert = 4;
		final float[] vertx = { A[0], B[0], C[0], D[0] };
		final float[] verty = { A[1], B[1], C[1], D[1] };

		return pnpoly(nvert, vertx, verty, x, y);
	}

	/**
	 * 
	 * @param nvert
	 *            Number of vertices in the polygon. Whether to repeat the first
	 *            vertex at the end is discussed below.
	 * @param vertx
	 *            Arrays containing the x- and y-coordinates of the polygon's
	 *            vertices.
	 * @param verty
	 *            Arrays containing the x- and y-coordinates of the polygon's
	 *            vertices.
	 * @param testx
	 *            X- and y-coordinate of the test point.
	 * @param testy
	 *            X- and y-coordinate of the test point.
	 * @return true if in otherwise return false
	 */
	private boolean pnpoly(int nvert, float[] vertx, float[] verty,
			float testx, float testy) {
		int i, j;
		boolean c = false;
		for (i = 0, j = nvert - 1; i < nvert; j = i++) {
			if (((verty[i] > testy) != (verty[j] > testy))
					&& (testx < (vertx[j] - vertx[i]) * (testy - verty[i])
							/ (verty[j] - verty[i]) + vertx[i]))
				c = !c;
		}

		return c;
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
		if (mImageBound != null) {
			point.set(x / 2 - mImageBound.left, y / 2 - mImageBound.top);
		}
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

	public interface OnGetImageListener {
		public void onFirstTouch(View v);
	}
}
