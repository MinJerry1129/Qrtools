package com.kessi.photopipcollagemaker.frame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.kessi.photopipcollagemaker.template.PhotoItem;
import com.kessi.photopipcollagemaker.utils.GeometryUtils;
import com.kessi.photopipcollagemaker.utils.ImageDecoder;
import com.kessi.photopipcollagemaker.utils.ImageUtils;
import com.kessi.photopipcollagemaker.utils.ResultContainer;

import java.util.ArrayList;
import java.util.List;

public class FrameImageView extends ImageView {
    private static final String TAG = FrameImageView.class.getSimpleName();

    public interface OnImageClickListener {
        void onLongClickImage(FrameImageView view);
    }

    private final GestureDetector mGestureDetector;
    private MultiTouchHandler mTouchHandler;
    private Bitmap mImage;
    private Paint mPaint;
    private Matrix mImageMatrix;
    private Matrix mScaleMatrix;
    private PhotoItem mPhotoItem;
    private float mViewWidth, mViewHeight;
    private float mOutputScale = 1;
    private OnImageClickListener mOnImageClickListener;
    private RelativeLayout.LayoutParams mOriginalLayoutParams;
    private boolean mEnableTouch = true;
    private float mCorner = 0;
    private float mSpace = 0;
    private Path mPath = new Path();
    private Path mBackgroundPath = new Path();
    private List<PointF> mPolygon = new ArrayList<>();
    private Rect mPathRect = new Rect(0, 0, 0, 0);
    private boolean mSelected = true;
    private List<PointF> mConvertedPoints = new ArrayList<>();

    private int mBackgroundColor = Color.WHITE;
    //Clear area
    private Path mClearPath = new Path();
    private List<PointF> mConvertedClearPoints = new ArrayList<>();

    public FrameImageView(final Context context, PhotoItem photoItem) {
        super(context);
        mPhotoItem = photoItem;
        if (photoItem.imagePath != null && photoItem.imagePath.length() > 0) {
            mImage = ResultContainer.getInstance().getImage(photoItem.imagePath);
            if (mImage == null || mImage.isRecycled()) {
                try {
                    mImage = ImageDecoder.decodeFileToBitmap(photoItem.imagePath);
                } catch (OutOfMemoryError err) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getContext().getApplicationContext(), "waring_out_of_memory", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }
                }
                ResultContainer.getInstance().putImage(photoItem.imagePath, mImage);
                Logger.d(TAG, "create FrameImageView, decode image");
            } else {
                Logger.d(TAG, "create FrameImageView, use decoded image");
            }
        }

        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        setScaleType(ScaleType.MATRIX);
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        mImageMatrix = new Matrix();
        mScaleMatrix = new Matrix();

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onLongClickImage(FrameImageView.this);
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mOnImageClickListener != null) {
//                    mOnImageClickListener.onDoubleClickImage(FrameImageView.this);
                }
                return true;
            }
        });
    }

    public void saveInstanceState(Bundle outState) {
        final int index = mPhotoItem.index;
        float[] values = new float[9];
        mImageMatrix.getValues(values);
        outState.putFloatArray("mImageMatrix_" + index, values);
        values = new float[9];
        mScaleMatrix.getValues(values);
        outState.putFloatArray("mScaleMatrix_" + index, values);
        outState.putFloat("mViewWidth_" + index, mViewWidth);
        outState.putFloat("mViewHeight_" + index, mViewHeight);
        outState.putFloat("mOutputScale_" + index, mOutputScale);
        outState.putFloat("mCorner_" + index, mCorner);
        outState.putFloat("mSpace_" + index, mSpace);
        outState.putInt("mBackgroundColor_" + index, mBackgroundColor);
    }

    /**
     * Called after init() function
     *
     * @param savedInstanceState
     */
    public void restoreInstanceState(Bundle savedInstanceState) {
        final int index = mPhotoItem.index;
        float[] values = savedInstanceState.getFloatArray("mImageMatrix_" + index);
        if (values != null) {
            mImageMatrix.setValues(values);
        }
        values = savedInstanceState.getFloatArray("mScaleMatrix_" + index);
        if (values != null) {
            mScaleMatrix.setValues(values);
        }
        mViewWidth = savedInstanceState.getFloat("mViewWidth_" + index, 1);
        mViewHeight = savedInstanceState.getFloat("mViewHeight_" + index, 1);
        mOutputScale = savedInstanceState.getFloat("mOutputScale_" + index, 1);
        mCorner = savedInstanceState.getFloat("mCorner_" + index, 0);
        mSpace = savedInstanceState.getFloat("mSpace_" + index, 0);
        mBackgroundColor = savedInstanceState.getInt("mBackgroundColor_" + index, Color.WHITE);
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        mTouchHandler.setScale(mOutputScale);
        setSpace(mSpace, mCorner);
    }

    public void swapImage(FrameImageView view) {
        if (mImage != null && view.getImage() != null) {
            Bitmap temp = view.getImage();
            view.setImage(mImage);
            mImage = temp;

            String tmpPath = view.getPhotoItem().imagePath;
            view.getPhotoItem().imagePath = mPhotoItem.imagePath;
            mPhotoItem.imagePath = tmpPath;
            resetImageMatrix();
            view.resetImageMatrix();
        }
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

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        invalidate();
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

    @Override
    public Matrix getImageMatrix() {
        return mImageMatrix;
    }

    public float getViewWidth() {
        return mViewWidth;
    }

    public float getViewHeight() {
        return mViewHeight;
    }

    public void init(final float viewWidth, final float viewHeight, final float scale, final float space, final float corner) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mOutputScale = scale;
        mSpace = space;
        mCorner = corner;

        if (mImage != null) {
            mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(viewWidth, viewHeight, mImage.getWidth(), mImage.getHeight()));
            mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(scale * viewWidth, scale * viewHeight, mImage.getWidth(), mImage.getHeight()));
        }

        mTouchHandler = new MultiTouchHandler();
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        mTouchHandler.setScale(scale);
        mTouchHandler.setEnableRotation(true);

        setSpace(mSpace, mCorner);
    }

    public void init(final float viewWidth, final float viewHeight, final float scale) {
        init(viewWidth, viewHeight, scale, 0, 0);
    }

    public float getSpace() {
        return mSpace;
    }

    public float getCorner() {
        return mCorner;
    }

    public void setSpace(float space, float corner) {
        mSpace = space;
        mCorner = corner;
        setSpace(mViewWidth, mViewHeight, mPhotoItem,
                mConvertedPoints, mConvertedClearPoints,
                mPath, mClearPath, mBackgroundPath, mPolygon, mPathRect, space, corner);
        invalidate();
    }

    private static void setSpace(final float viewWidth, final float viewHeight, final PhotoItem photoItem,
                                 final List<PointF> convertedPoints,
                                 final List<PointF> convertedClearPoints,
                                 final Path path,
                                 final Path clearPath,
                                 final Path backgroundPath,
                                 final List<PointF> polygon,
                                 final Rect pathRect,
                                 final float space, final float corner) {

        if (photoItem.pointList != null && convertedPoints.isEmpty()) {
            for (PointF p : photoItem.pointList) {
                PointF convertedPoint = new PointF(p.x * viewWidth, p.y * viewHeight);
                convertedPoints.add(convertedPoint);
                if (photoItem.shrinkMap != null) {
                    photoItem.shrinkMap.put(convertedPoint, photoItem.shrinkMap.get(p));
                }
            }
        }

        if (photoItem.clearAreaPoints != null && photoItem.clearAreaPoints.size() > 0) {
            clearPath.reset();
            if (convertedClearPoints.isEmpty())
                for (PointF p : photoItem.clearAreaPoints) {
                    convertedClearPoints.add(new PointF(p.x * viewWidth, p.y * viewHeight));
                }
            GeometryUtils.createPathWithCircleCorner(clearPath, convertedClearPoints, corner);
        } else if (photoItem.clearPath != null) {
            clearPath.reset();
            buildRealClearPath(viewWidth, viewHeight, photoItem, clearPath, corner);
        }

        if (photoItem.path != null) {
            buildRealPath(viewWidth, viewHeight, photoItem, path, space, corner);
            polygon.clear();
        } else {
            List<PointF> shrunkPoints;
            if (photoItem.shrinkMethod == PhotoItem.SHRINK_METHOD_3_3) {
                int centerPointIdx = findCenterPointIndex(photoItem);
                shrunkPoints = GeometryUtils.shrinkPathCollage_3_3(convertedPoints, centerPointIdx, space, photoItem.bound);
            } else if (photoItem.shrinkMethod == PhotoItem.SHRINK_METHOD_USING_MAP && photoItem.shrinkMap != null) {
                shrunkPoints = GeometryUtils.shrinkPathCollageUsingMap(convertedPoints, space, photoItem.shrinkMap);
            } else if (photoItem.shrinkMethod == PhotoItem.SHRINK_METHOD_COMMON && photoItem.shrinkMap != null) {
                shrunkPoints = GeometryUtils.commonShrinkPath(convertedPoints, space, photoItem.shrinkMap);
            } else {
                if (photoItem.disableShrink) {
                    shrunkPoints = GeometryUtils.shrinkPath(convertedPoints, 0, photoItem.bound);
                } else {
                    shrunkPoints = GeometryUtils.shrinkPath(convertedPoints, space, photoItem.bound);
                }
            }
            polygon.clear();
            polygon.addAll(shrunkPoints);
            GeometryUtils.createPathWithCircleCorner(path, shrunkPoints, corner);
            if (photoItem.hasBackground) {
                backgroundPath.reset();
                GeometryUtils.createPathWithCircleCorner(backgroundPath, convertedPoints, corner);
            }
        }

        pathRect.set(0, 0, 0, 0);
    }

    private static int findCenterPointIndex(PhotoItem photoItem) {
        int centerPointIdx = 0;
        if (photoItem.bound.left == 0 && photoItem.bound.top == 0) {
            float minX = 1;
            for (int idx = 0; idx < photoItem.pointList.size(); idx++) {
                PointF p = photoItem.pointList.get(idx);
                if (p.x > 0 && p.x < 1 && p.y > 0 && p.y < 1 && p.x < minX) {
                    centerPointIdx = idx;
                    minX = p.x;
                }
            }
        } else {
            float maxX = 0;
            for (int idx = 0; idx < photoItem.pointList.size(); idx++) {
                PointF p = photoItem.pointList.get(idx);
                if (p.x > 0 && p.x < 1 && p.y > 0 && p.y < 1 && p.x > maxX) {
                    centerPointIdx = idx;
                    maxX = p.x;
                }
            }
        }

        return centerPointIdx;
    }

    private static void buildRealPath(final float viewWidth, final float viewHeight,
                                      final PhotoItem photoItem, final Path outPath,
                                      float space, final float corner) {
        if (photoItem.path != null) {
            RectF rect = new RectF();
            photoItem.path.computeBounds(rect, true);
            final float pathWidthPixels = rect.width();
            final float pathHeightPixels = rect.height();
            space = 2 * space;
            outPath.set(photoItem.path);
            Matrix m = new Matrix();
            float ratioX = 1, ratioY = 1;
            if (photoItem.fitBound) {
                ratioX = photoItem.pathScaleRatio * (viewWidth * photoItem.pathRatioBound.width() - 2 * space) / pathWidthPixels;
                ratioY = photoItem.pathScaleRatio * (viewHeight * photoItem.pathRatioBound.height() - 2 * space) / pathHeightPixels;
            } else {
                float ratio = Math.min(photoItem.pathScaleRatio * (viewHeight - 2 * space) / pathHeightPixels,
                        photoItem.pathScaleRatio * (viewWidth - 2 * space) / pathWidthPixels);
                ratioX = ratio;
                ratioY = ratio;
            }
            m.postScale(ratioX, ratioY);
            outPath.transform(m);
            RectF bound = new RectF();
            if (photoItem.cornerMethod == PhotoItem.CORNER_METHOD_3_6) {
                outPath.computeBounds(bound, true);
                GeometryUtils.createRegularPolygonPath(outPath, Math.min(bound.width(), bound.height()), 6, corner);
                outPath.computeBounds(bound, true);
            } else if (photoItem.cornerMethod == PhotoItem.CORNER_METHOD_3_13) {
                outPath.computeBounds(bound, true);
                GeometryUtils.createRectanglePath(outPath, bound.width(), bound.height(), corner);
                outPath.computeBounds(bound, true);
            } else {
                outPath.computeBounds(bound, true);
            }

            float x = 0, y = 0;
            if (photoItem.shrinkMethod == PhotoItem.SHRINK_METHOD_3_6 || photoItem.shrinkMethod == PhotoItem.SHRINK_METHOD_3_8) {
                x = viewWidth / 2 - bound.width() / 2;
                y = viewHeight / 2 - bound.height() / 2;
                m.reset();
                m.postTranslate(x, y);
                outPath.transform(m);
            } else {
                if (photoItem.pathAlignParentRight) {
                    x = photoItem.pathRatioBound.right * viewWidth - bound.width() - space / ratioX;
                    y = photoItem.pathRatioBound.top * viewHeight + space / ratioY;
                } else {
                    x = photoItem.pathRatioBound.left * viewWidth + space / ratioX;
                    y = photoItem.pathRatioBound.top * viewHeight + space / ratioY;
                }

                if (photoItem.pathInCenterHorizontal) {
                    x = viewWidth / 2.0f - bound.width() / 2.0f;
                }

                if (photoItem.pathInCenterVertical) {
                    y = viewHeight / 2.0f - bound.height() / 2.0f;
                }

                m.reset();
                m.postTranslate(x, y);
                outPath.transform(m);
            }
        }
    }

    private static Path buildRealClearPath(final float viewWidth, final float viewHeight, final PhotoItem photoItem, final Path clearPath, final float corner) {
        if (photoItem.clearPath != null) {
            RectF rect = new RectF();
            photoItem.clearPath.computeBounds(rect, true);
            final float clearPathWidthPixels = rect.width();
            final float clearPathHeightPixels = rect.height();

            clearPath.set(photoItem.clearPath);
            Matrix m = new Matrix();
            float ratioX = 1, ratioY = 1;
            if (photoItem.fitBound) {
                ratioX = photoItem.clearPathScaleRatio * viewWidth * photoItem.clearPathRatioBound.width() / clearPathWidthPixels;
                ratioY = photoItem.clearPathScaleRatio * viewHeight * photoItem.clearPathRatioBound.height() / clearPathHeightPixels;
            } else {
                float ratio = Math.min(photoItem.clearPathScaleRatio * viewHeight / clearPathHeightPixels,
                        photoItem.clearPathScaleRatio * viewWidth / clearPathWidthPixels);
                ratioX = ratio;
                ratioY = ratio;
            }
            m.postScale(ratioX, ratioY);
            clearPath.transform(m);
            RectF bound = new RectF();
            if (photoItem.cornerMethod == PhotoItem.CORNER_METHOD_3_6) {
                clearPath.computeBounds(bound, true);
                GeometryUtils.createRegularPolygonPath(clearPath, Math.min(bound.width(), bound.height()), 6, corner);
                clearPath.computeBounds(bound, true);
            } else if (photoItem.cornerMethod == PhotoItem.CORNER_METHOD_3_13) {
                clearPath.computeBounds(bound, true);
                GeometryUtils.createRectanglePath(clearPath, bound.width(), bound.height(), corner);
                clearPath.computeBounds(bound, true);
            } else {
                clearPath.computeBounds(bound, true);
            }

            float x = 0, y = 0;
            if (photoItem.shrinkMethod == PhotoItem.SHRINK_METHOD_3_6) {
                if (photoItem.clearPathRatioBound.left > 0) {
                    x = viewWidth - bound.width() / 2;
                } else {
                    x = -bound.width() / 2;
                }
                y = viewHeight / 2 - bound.height() / 2;
            } else {
                if (photoItem.centerInClearBound) {
                    x = photoItem.clearPathRatioBound.left * viewWidth + (viewWidth / 2 - bound.width() / 2);
                    y = photoItem.clearPathRatioBound.top * viewHeight + (viewHeight / 2 - bound.height() / 2);
                } else {
                    x = photoItem.clearPathRatioBound.left * viewWidth;
                    y = photoItem.clearPathRatioBound.top * viewHeight;
                    if (photoItem.clearPathInCenterHorizontal) {
                        x = viewWidth / 2.0f - bound.width() / 2.0f;
                    }
                    if (photoItem.clearPathInCenterVertical) {
                        y = viewHeight / 2.0f - bound.height() / 2.0f;
                    }
                }
            }

            m.reset();
            m.postTranslate(x, y);
            clearPath.transform(m);
            return clearPath;
        } else {
            return null;
        }
    }

    public void setImagePath(String imagePath) {
        mPhotoItem.imagePath = imagePath;
        recycleImage();
        try {
            mImage = ImageDecoder.decodeFileToBitmap(imagePath);
            mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mViewWidth, mViewHeight, mImage.getWidth(), mImage.getHeight()));
            mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mOutputScale * mViewWidth, mOutputScale * mViewHeight, mImage.getWidth(), mImage.getHeight()));
            mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
            invalidate();
            ResultContainer.getInstance().putImage(mPhotoItem.imagePath, mImage);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void resetImageMatrix() {
        mImageMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mViewWidth, mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mScaleMatrix.set(ImageUtils.createMatrixToDrawImageInCenterView(mOutputScale * mViewWidth, mOutputScale * mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        invalidate();
    }

    public void clearMainImage() {
        mPhotoItem.imagePath = null;
        recycleImage();
        invalidate();
    }

    public void recycleImage() {
        if (mImage != null) {
            mImage.recycle();
            mImage = null;
            System.gc();
        }
    }

    public PointF getCenterPolygon() {
        if (mPolygon != null && mPolygon.size() > 0) {
            PointF result = new PointF();
            for (PointF p : mPolygon) {
                result.x += p.x;
                result.y += p.y;
            }
            result.x = result.x / mPolygon.size();
            result.y = result.y / mPolygon.size();
            return result;
        } else {
            return null;
        }
    }

    private void drawCenterLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, mViewHeight / 2, mViewWidth, mViewHeight / 2, paint);
        canvas.drawRect(0, 0, mViewWidth, mViewHeight, paint);
    }

    public boolean isSelected(float x, float y) {
        boolean result = GeometryUtils.contains(mPolygon, new PointF(x, y));
        Logger.d(TAG, "isSelected, x=" + x + ", y=" + y + ", result=" + result);
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawImage(canvas, mPath, mPaint, mPathRect, mImage, mImageMatrix,
                getWidth(), getHeight(), mBackgroundColor, mBackgroundPath,
                mClearPath, mPolygon);
    }

    public void drawOutputImage(Canvas canvas) {
        final float viewWidth = mViewWidth * mOutputScale;
        final float viewHeight = mViewHeight * mOutputScale;
        final Path path = new Path();
        final Path clearPath = new Path();
        final Path backgroundPath = new Path();
        final Rect pathRect = new Rect();
        final List<PointF> polygon = new ArrayList<>();
        setSpace(viewWidth, viewHeight, mPhotoItem, new ArrayList<PointF>(),
                new ArrayList<PointF>(), path, clearPath, backgroundPath, polygon, pathRect,
                mSpace * mOutputScale, mCorner * mOutputScale);
        drawImage(canvas, path, mPaint, pathRect, mImage, mScaleMatrix,
                viewWidth, viewHeight, mBackgroundColor, backgroundPath, clearPath, polygon);
    }

    private static void drawImage(Canvas canvas, Path path, Paint paint, Rect pathRect, Bitmap image, Matrix imageMatrix,
                                  float viewWidth, float viewHeight,
                                  int color, Path backgroundPath,
                                  Path clearPath, List<PointF> touchPolygon) {
        if (image != null && !image.isRecycled()) {
            canvas.drawBitmap(image, imageMatrix, paint);
        }
        //clip outside
        if (pathRect.left == pathRect.right) {
            canvas.save();
            canvas.clipPath(path);
            pathRect.set(canvas.getClipBounds());
            canvas.restore();
        }

        canvas.save();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawARGB(0x00, 0x00, 0x00, 0x00);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, viewWidth, pathRect.top, paint);
        canvas.drawRect(0, 0, pathRect.left, viewHeight, paint);
        canvas.drawRect(pathRect.right, 0, viewWidth, viewHeight, paint);
        canvas.drawRect(0, pathRect.bottom, viewWidth, viewHeight, paint);
        paint.setXfermode(null);
        canvas.restore();
        //clip inside
        canvas.save();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawARGB(0x00, 0x00, 0x00, 0x00);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        final Path.FillType currentFillType = path.getFillType();
        path.setFillType(Path.FillType.INVERSE_WINDING);
        canvas.drawPath(path, paint);
        paint.setXfermode(null);
        canvas.restore();
        path.setFillType(currentFillType);
        //clear area
        if (clearPath != null) {
            canvas.save();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawARGB(0x00, 0x00, 0x00, 0x00);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(clearPath, paint);
            paint.setXfermode(null);
            canvas.restore();
        }
        //draw out side
        if (backgroundPath != null) {
            canvas.save();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.drawARGB(0x00, 0x00, 0x00, 0x00);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(backgroundPath, paint);
            paint.setXfermode(null);
            canvas.restore();
        }
        //touch polygon
        if (touchPolygon != null && touchPolygon.isEmpty()) {
            touchPolygon.add(new PointF(pathRect.left, pathRect.top));
            touchPolygon.add(new PointF(pathRect.right, pathRect.top));
            touchPolygon.add(new PointF(pathRect.right, pathRect.bottom));
            touchPolygon.add(new PointF(pathRect.left, pathRect.bottom));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableTouch) {
            return super.onTouchEvent(event);
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (GeometryUtils.contains(mPolygon, new PointF(event.getX(), event.getY()))) {
                    mSelected = true;
                } else {
                    mSelected = false;
                }
            }

            if (mSelected) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mSelected = false;
                }

                mGestureDetector.onTouchEvent(event);
                if (mTouchHandler != null && mImage != null && !mImage.isRecycled()) {
                    mTouchHandler.touch(event);
                    mImageMatrix.set(mTouchHandler.getMatrix());
                    mScaleMatrix.set(mTouchHandler.getScaleMatrix());
                    invalidate();
                }
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }
    }

    public void setEnableTouch(boolean enableTouch) {
        mEnableTouch = enableTouch;
    }
}
