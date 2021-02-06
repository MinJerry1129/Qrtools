package com.xinlan.imageeditlibrary.editimage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kessi.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.utils.PaintUtil;

public class CropImageView extends View {
	private static int STATUS_IDLE = 1;//
	private static int STATUS_MOVE = 2;//
	private static int STATUS_SCALE = 3;//

	private int CIRCLE_WIDTH = 46;
	private Context mContext;
	private float oldx, oldy;
	private int status = STATUS_IDLE;
	private int selectedControllerCicle;
	private RectF backUpRect = new RectF();//
	private RectF backLeftRect = new RectF();//
	private RectF backRightRect = new RectF();//
	private RectF backDownRect = new RectF();//

	private RectF cropRect = new RectF();//

	private Paint mBackgroundPaint;//
	private Bitmap circleBit;
	private Rect circleRect = new Rect();
	private RectF leftTopCircleRect;
	private RectF rightTopCircleRect;
	private RectF leftBottomRect;
	private RectF rightBottomRect;

	private RectF imageRect = new RectF();//
	private RectF tempRect = new RectF();//

	private float ratio = -1;//

	public CropImageView(Context context) {
		super(context);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mBackgroundPaint = PaintUtil.newBackgroundPaint(context);
		circleBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.round);
		circleRect.set(0, 0, circleBit.getWidth(), circleBit.getHeight());
		leftTopCircleRect = new RectF(0, 0, CIRCLE_WIDTH, CIRCLE_WIDTH);
		rightTopCircleRect = new RectF(leftTopCircleRect);
		leftBottomRect = new RectF(leftTopCircleRect);
		rightBottomRect = new RectF(leftTopCircleRect);
	}


	public void setCropRect(RectF rect) {
		imageRect.set(rect);
		cropRect.set(rect);
		scaleRect(cropRect, 0.5f);
		invalidate();
	}

	public void setRatioCropRect(RectF rect, float r) {
		this.ratio = r;
		if (r < 0) {
			setCropRect(rect);
			return;
		}

		imageRect.set(rect);
		cropRect.set(rect);
		// setCropRect(rect);


		float h, w;
		if (cropRect.width() >= cropRect.height()) {// w>=h
			h = cropRect.height() / 2;
			w = this.ratio * h;
		} else {// w<h
			w = rect.width() / 2;
			h = w / this.ratio;
		}// end if
		float scaleX = w / cropRect.width();
		float scaleY = h / cropRect.height();
		scaleRect(cropRect, scaleX, scaleY);
		invalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		int w = getWidth();
		int h = getHeight();
		if (w <= 0 || h <= 0)
			return;


		backUpRect.set(0, 0, w, cropRect.top);
		backLeftRect.set(0, cropRect.top, cropRect.left, cropRect.bottom);
		backRightRect.set(cropRect.right, cropRect.top, w, cropRect.bottom);
		backDownRect.set(0, cropRect.bottom, w, h);

		canvas.drawRect(backUpRect, mBackgroundPaint);
		canvas.drawRect(backLeftRect, mBackgroundPaint);
		canvas.drawRect(backRightRect, mBackgroundPaint);
		canvas.drawRect(backDownRect, mBackgroundPaint);


		int radius = CIRCLE_WIDTH >> 1;
		leftTopCircleRect.set(cropRect.left - radius, cropRect.top - radius,
				cropRect.left + radius, cropRect.top + radius);
		rightTopCircleRect.set(cropRect.right - radius, cropRect.top - radius,
				cropRect.right + radius, cropRect.top + radius);
		leftBottomRect.set(cropRect.left - radius, cropRect.bottom - radius,
				cropRect.left + radius, cropRect.bottom + radius);
		rightBottomRect.set(cropRect.right - radius, cropRect.bottom - radius,
				cropRect.right + radius, cropRect.bottom + radius);

		canvas.drawBitmap(circleBit, circleRect, leftTopCircleRect, null);
		canvas.drawBitmap(circleBit, circleRect, rightTopCircleRect, null);
		canvas.drawBitmap(circleBit, circleRect, leftBottomRect, null);
		canvas.drawBitmap(circleBit, circleRect, rightBottomRect, null);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = super.onTouchEvent(event);//
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			int selectCircle = isSeletedControllerCircle(x, y);
			if (selectCircle > 0) {//
				ret = true;
				selectedControllerCicle = selectCircle;//
				status = STATUS_SCALE;//
			} else if (cropRect.contains(x, y)) {//
				ret = true;
				status = STATUS_MOVE;//
			} else {//

			}// end if
			break;
		case MotionEvent.ACTION_MOVE:
			if (status == STATUS_SCALE) {//

				scaleCropController(x, y);
			} else if (status == STATUS_MOVE) {//

				translateCrop(x - oldx, y - oldy);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			status = STATUS_IDLE;//
			break;
		}// end switch


		oldx = x;
		oldy = y;

		return ret;
	}


	private void translateCrop(float dx, float dy) {
		tempRect.set(cropRect);//

		translateRect(cropRect, dx, dy);

		float mdLeft = imageRect.left - cropRect.left;
		if (mdLeft > 0) {
			translateRect(cropRect, mdLeft, 0);
		}
		float mdRight = imageRect.right - cropRect.right;
		if (mdRight < 0) {
			translateRect(cropRect, mdRight, 0);
		}
		float mdTop = imageRect.top - cropRect.top;
		if (mdTop > 0) {
			translateRect(cropRect, 0, mdTop);
		}
		float mdBottom = imageRect.bottom - cropRect.bottom;
		if (mdBottom < 0) {
			translateRect(cropRect, 0, mdBottom);
		}

		this.invalidate();
	}


	private static final void translateRect(RectF rect, float dx, float dy) {
		rect.left += dx;
		rect.right += dx;
		rect.top += dy;
		rect.bottom += dy;
	}


	private void scaleCropController(float x, float y) {
		tempRect.set(cropRect);//
		switch (selectedControllerCicle) {
		case 1://
			cropRect.left = x;
			cropRect.top = y;
			break;
		case 2://
			cropRect.right = x;
			cropRect.top = y;
			break;
		case 3://
			cropRect.left = x;
			cropRect.bottom = y;
			break;
		case 4://
			cropRect.right = x;
			cropRect.bottom = y;
			break;
		}// end switch

		if (ratio < 0) {//
			//
			validateCropRect();
			invalidate();
		} else {
			//
			//
			switch (selectedControllerCicle) {
			case 1://
			case 2://
				cropRect.bottom = (cropRect.right - cropRect.left) / this.ratio
						+ cropRect.top;
				break;
			case 3://
			case 4://
				cropRect.top = cropRect.bottom
						- (cropRect.right - cropRect.left) / this.ratio;
				break;
			}// end switch

			// validateCropRect();
			if (cropRect.left < imageRect.left
					|| cropRect.right > imageRect.right
					|| cropRect.top < imageRect.top
					|| cropRect.bottom > imageRect.bottom
					|| cropRect.width() < CIRCLE_WIDTH
					|| cropRect.height() < CIRCLE_WIDTH) {
				cropRect.set(tempRect);
			}
			invalidate();
		}// end if
	}


	private void validateCropRect() {
		if (cropRect.width() < CIRCLE_WIDTH) {
			cropRect.left = tempRect.left;
			cropRect.right = tempRect.right;
		}
		if (cropRect.height() < CIRCLE_WIDTH) {
			cropRect.top = tempRect.top;
			cropRect.bottom = tempRect.bottom;
		}
		if (cropRect.left < imageRect.left) {
			cropRect.left = imageRect.left;
		}
		if (cropRect.right > imageRect.right) {
			cropRect.right = imageRect.right;
		}
		if (cropRect.top < imageRect.top) {
			cropRect.top = imageRect.top;
		}
		if (cropRect.bottom > imageRect.bottom) {
			cropRect.bottom = imageRect.bottom;
		}
	}


	private int isSeletedControllerCircle(float x, float y) {
		if (leftTopCircleRect.contains(x, y))//
			return 1;
		if (rightTopCircleRect.contains(x, y))//
			return 2;
		if (leftBottomRect.contains(x, y))//
			return 3;
		if (rightBottomRect.contains(x, y))//
			return 4;
		return -1;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	public RectF getCropRect() {
		return new RectF(this.cropRect);
	}


	private static void scaleRect(RectF rect, float scaleX, float scaleY) {
		float w = rect.width();
		float h = rect.height();

		float newW = scaleX * w;
		float newH = scaleY * h;

		float dx = (newW - w) / 2;
		float dy = (newH - h) / 2;

		rect.left -= dx;
		rect.top -= dy;
		rect.right += dx;
		rect.bottom += dy;
	}


	private static void scaleRect(RectF rect, float scale) {
		scaleRect(rect, scale, scale);
	}

	public float getRatio() {
		return ratio;
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

}// end class
