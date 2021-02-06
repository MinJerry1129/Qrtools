package com.kessi.photopipcollagemaker.template;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.kessi.photopipcollagemaker.model.ImageTemplate;
import com.kessi.photopipcollagemaker.Activities.custom.TransitionImageView;
import com.kessi.photopipcollagemaker.utils.ImageDecoder;
import com.kessi.photopipcollagemaker.utils.ImageUtils;
import com.kessi.photopipcollagemaker.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PhotoLayout extends RelativeLayout implements ItemImageView.OnImageClickListener {
    private static final String TAG = PhotoLayout.class.getSimpleName();

    public interface OnQuickActionClickListener {
        void onEditActionClick(ItemImageView v);

        void onChangeActionClick(ItemImageView v);

        void onChangeBackgroundActionClick(TransitionImageView v);
    }

    View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    Logger.i("Drag Event", "Entered: x=" + event.getX() + ", y=" + event.getY());
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Logger.i("Drag Event", "Exited: x=" + event.getX() + ", y=" + event.getY());
                    break;

                case DragEvent.ACTION_DROP:
                    ItemImageView target = (ItemImageView) v;
                    ItemImageView dragged = (ItemImageView) event.getLocalState();
                    String targetPath = "", draggedPath = "";
                    if (target.getPhotoItem() != null)
                        targetPath = target.getPhotoItem().imagePath;
                    if (dragged.getPhotoItem() != null)
                        draggedPath = dragged.getPhotoItem().imagePath;
                    if (targetPath == null) targetPath = "";
                    if (draggedPath == null) draggedPath = "";
                    if (!targetPath.equals(draggedPath))
                        target.swapImage(dragged);
                    break;
            }

            return true;
        }
    };

    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_CHANGE = 2;
    private static final int ID_DELETE = 3;
    private static final int ID_CANCEL = 4;

    private List<PhotoItem> mPhotoItems;
    private int mImageWidth, mImageHeight;
    private List<ItemImageView> mItemImageViews;
    private TransitionImageView mBackgroundImageView;
    private int mViewWidth, mViewHeight;
    private float mInternalScaleRatio = 1;
    private float mOutputScaleRatio = 1;
    private Bitmap mTemplateImage;
    private OnQuickActionClickListener mQuickActionClickListener;

    private ProgressBar mProgressBar;
    private Bitmap mBackgroundImage;

    public static List<PhotoItem> parseImageTemplate(ImageTemplate template) {
        List<PhotoItem> photoItems = new ArrayList<>();
        try {
            String[] childTexts = template.getChild().split(";");
            if (childTexts != null) {
                for (String child : childTexts) {
                    String[] properties = child.split(",");
                    if (properties != null) {
                        PhotoItem item = new PhotoItem();
                        item.index = Integer.parseInt(properties[0]);
                        item.x = Integer.parseInt(properties[1]);
                        item.y = Integer.parseInt(properties[2]);
                        item.maskPath = properties[3];
                        photoItems.add(item);
                    }
                }
                //Sort via index
                Collections.sort(photoItems, new Comparator<PhotoItem>() {
                    @Override
                    public int compare(PhotoItem lhs, PhotoItem rhs) {
                        return rhs.index - lhs.index;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return photoItems;
    }

    public PhotoLayout(Context context, ImageTemplate template) {
        super(context);
        Bitmap templateImage = PhotoUtils.decodePNGImage(context, template.getTemplate());
        List<PhotoItem> photoItems = parseImageTemplate(template);
        init(photoItems, templateImage);
    }

    public PhotoLayout(Context context, List<PhotoItem> photoItems, Bitmap templateImage) {
        super(context);
        init(photoItems, templateImage);
    }

    private void init(List<PhotoItem> photoItems, Bitmap templateImage) {
        mPhotoItems = photoItems;
        mTemplateImage = templateImage;
        mImageWidth = mTemplateImage.getWidth();
        mImageHeight = mTemplateImage.getHeight();
        mItemImageViews = new ArrayList<>();
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public void setQuickActionClickListener(OnQuickActionClickListener quickActionClickListener) {
        mQuickActionClickListener = quickActionClickListener;
    }

    public Bitmap getTemplateImage() {
        return mTemplateImage;
    }

    public TransitionImageView getBackgroundImageView() {
        return mBackgroundImageView;
    }

    public void setBackgroundImage(Bitmap image) {
        mBackgroundImage = image;
    }

    public Bitmap getBackgroundImage() {
        return mBackgroundImageView.getImage();
    }

    private void asyncCreateBackgroundImage(final String path) {
        Logger.d(TAG, "asyncCreateBackgroundImage");
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(VISIBLE);
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    Bitmap image = ImageDecoder.decodeFileToBitmap(path);
                    if (image != null) {
                        Bitmap result = PhotoUtils.fastblur(image, 10);
                        if (image != result) {
                            image.recycle();
                            image = null;
                            System.gc();
                        }
                        return result;
                    }
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                mProgressBar.setVisibility(GONE);
                if (result != null)
                    mBackgroundImageView.init(result, mViewWidth, mViewHeight, mOutputScaleRatio);
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void build(final int viewWidth, final int viewHeight, final float outputScaleRatio) {
        if (viewWidth < 1 || viewHeight < 1) {
            return;
        }
        //add children views
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mOutputScaleRatio = outputScaleRatio;
        mItemImageViews.clear();
        mInternalScaleRatio = 1.0f / PhotoUtils.calculateScaleRatio(mImageWidth, mImageHeight, viewWidth, viewHeight);
        for (PhotoItem item : mPhotoItems) {
            mItemImageViews.add(addPhotoItemView(item, mInternalScaleRatio, mOutputScaleRatio));
        }
        //add template image
        final ImageView templateImageView = new ImageView(getContext());
        if (Build.VERSION.SDK_INT >= 16) {
            templateImageView.setBackground(new BitmapDrawable(getResources(), mTemplateImage));
        } else {
            templateImageView.setBackgroundDrawable(new BitmapDrawable(getResources(), mTemplateImage));
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(templateImageView, params);
        //Create progress bar
        mProgressBar = new ProgressBar(getContext());
        params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setVisibility(View.GONE);
        addView(mProgressBar, params);
        //add background image
        mBackgroundImageView = new TransitionImageView(getContext());
        params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mBackgroundImageView, 0, params);
        //Create background
        mBackgroundImageView.setOnImageClickListener(new TransitionImageView.OnImageClickListener() {
            @Override
            public void onLongClickImage(TransitionImageView view) {

            }

            @Override
            public void onDoubleClickImage(TransitionImageView v) {
                if ((v.getImage() == null || v.getImage().isRecycled()) && mQuickActionClickListener != null) {
                    mQuickActionClickListener.onChangeBackgroundActionClick(v);
                } else {
                }
            }
        });

        if (mBackgroundImage == null || mBackgroundImage.isRecycled()) {
            if (mPhotoItems.size() > 0 && mPhotoItems.get(0).imagePath != null && mPhotoItems.get(0).imagePath.length() > 0) {
                asyncCreateBackgroundImage(mPhotoItems.get(0).imagePath);
            }
        } else {
            mBackgroundImageView.init(mBackgroundImage, mViewWidth, mViewHeight, mOutputScaleRatio);
        }
    }

    private ItemImageView addPhotoItemView(PhotoItem item, float internalScale, float outputScaleRatio) {
        if (item == null || item.maskPath == null) {
            return null;
        }
        Logger.d("PhotoLayout", "addPhotoItemView, item.x=" + item.x + ", item.y=" + item.y + ", scale=" + internalScale);
        final ItemImageView imageView = new ItemImageView(getContext(), item);
        final float viewWidth = internalScale * imageView.getMaskImage().getWidth();
        final float viewHeight = internalScale * imageView.getMaskImage().getHeight();
        imageView.init(viewWidth, viewHeight, outputScaleRatio);
        imageView.setOnImageClickListener(this);
        if (mPhotoItems.size() > 1)
            imageView.setOnDragListener(mOnDragListener);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
        params.leftMargin = (int) (internalScale * item.x);
        params.topMargin = (int) (internalScale * item.y);
        imageView.setOriginalLayoutParams(params);
        addView(imageView, params);
        return imageView;
    }

    public Bitmap createImage() {
        Bitmap template = Bitmap.createBitmap((int) (mOutputScaleRatio * mViewWidth), (int) (mOutputScaleRatio * mViewHeight), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(template);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (mBackgroundImageView.getImage() != null && !mBackgroundImageView.getImage().isRecycled()) {
            canvas.drawBitmap(mBackgroundImageView.getImage(), mBackgroundImageView.getScaleMatrix(), paint);
        }

        canvas.saveLayer(0, 0, template.getWidth(), template.getHeight(), paint, Canvas.ALL_SAVE_FLAG);

        for (ItemImageView view : mItemImageViews)
            if (view.getImage() != null && !view.getImage().isRecycled()) {
                final int left = (int) (view.getLeft() * mOutputScaleRatio);
                final int top = (int) (view.getTop() * mOutputScaleRatio);
                final int width = (int) (view.getWidth() * mOutputScaleRatio);
                final int height = (int) (view.getHeight() * mOutputScaleRatio);
                canvas.saveLayer(left, top, left + width, top + height, paint, Canvas.ALL_SAVE_FLAG);
                //draw image
                canvas.save();
                canvas.translate(left, top);
                canvas.clipRect(0, 0, width, height);
                canvas.drawBitmap(view.getImage(), view.getScaleMatrix(), paint);
                canvas.restore();
                //draw mask
                canvas.save();
                canvas.translate(left, top);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                canvas.drawBitmap(view.getMaskImage(), view.getScaleMaskMatrix(), paint);
                paint.setXfermode(null);
                canvas.restore();
                canvas.restore();
            }
        //draw frame
        if (mTemplateImage != null) {
            canvas.drawBitmap(mTemplateImage,
                    ImageUtils.createMatrixToDrawImageInCenterView(mOutputScaleRatio * mViewWidth, mOutputScaleRatio * mViewHeight,
                            mTemplateImage.getWidth(), mTemplateImage.getHeight()), paint);
        }

        canvas.restore();

        return template;
    }

    public void recycleImages(final boolean recycleBackground) {
        Logger.d(TAG, "recycleImages, recycleBackground=" + recycleBackground);
        if (recycleBackground) {
            mBackgroundImageView.recycleImages();
        }

        for (ItemImageView view : mItemImageViews) {
            view.recycleImages(recycleBackground);
        }
        if (mTemplateImage != null && !mTemplateImage.isRecycled()) {
            mTemplateImage.recycle();
            mTemplateImage = null;
        }
        System.gc();
    }

    @Override
    public void onLongClickImage(ItemImageView v) {
        if (mPhotoItems.size() > 1) {
            v.setTag("x=" + v.getPhotoItem().x + ",y=" + v.getPhotoItem().y + ",path=" + v.getPhotoItem().imagePath);
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
            v.startDrag(dragData, myShadow, v, 0);
        }
    }

    @Override
    public void onDoubleClickImage(ItemImageView v) {
        if ((v.getImage() == null || v.getImage().isRecycled()) && mQuickActionClickListener != null) {
            mQuickActionClickListener.onChangeActionClick(v);
        } else {
        }
    }
}
