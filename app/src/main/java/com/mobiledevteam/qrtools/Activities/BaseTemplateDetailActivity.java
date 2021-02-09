package com.mobiledevteam.qrtools.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevteam.qrtools.ShareActivity;
import com.mobiledevteam.qrtools.R;
import com.mobiledevteam.qrtools.utils.Utils;
import com.mobiledevteam.qrtools.adapter.HorizontalPreviewTemplateAdapter;
import com.mobiledevteam.qrtools.appconfig.Logger;
import com.mobiledevteam.qrtools.appconfig.AppConstant;
import com.mobiledevteam.qrtools.model.TemplateItem;
import com.mobiledevteam.qrtools.multitouch.controller.ImageEntity;
import com.mobiledevteam.qrtools.multitouch.controller.MultiTouchEntity;
import com.mobiledevteam.qrtools.multitouch.controller.TextEntity;
import com.mobiledevteam.qrtools.multitouch.custom.OnDoubleClickListener;
import com.mobiledevteam.qrtools.multitouch.custom.PhotoView;
import com.mobiledevteam.qrtools.quickaction.QuickAction;
import com.mobiledevteam.qrtools.quickaction.QuickActionItem;
import com.mobiledevteam.qrtools.template.PhotoItem;
import com.mobiledevteam.qrtools.utils.AdManager;
import com.mobiledevteam.qrtools.utils.DateTimeUtils;
import com.mobiledevteam.qrtools.utils.ImageUtils;
import com.mobiledevteam.qrtools.utils.ResultContainer;
import com.mobiledevteam.qrtools.utils.TemplateImageUtils;
import com.mobiledevteam.qrtools.utils.collagelayout.FrameImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseTemplateDetailActivity extends BasePhotoActivity implements HorizontalPreviewTemplateAdapter.OnPreviewTemplateClickListener, OnDoubleClickListener {
    private static final String TAG = BaseTemplateDetailActivity.class.getSimpleName();
    private static final String PREF_NAME = "templateDetailPref";
    private static final String RATIO_KEY = "ratio";
    protected static final int RATIO_SQUARE = 0;
    protected static final int RATIO_FIT = 1;
    protected static final int RATIO_GOLDEN = 2;
    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_DELETE = 2;
    private static final int ID_CANCEL = 3;

    protected RelativeLayout mContainerLayout;
    protected RecyclerView mTemplateView;
    protected PhotoView mPhotoView;
    protected float mOutputScale = 1;
    protected View mAddImageView;
    protected Animation mAnimation;
    protected int mItemType = AppConstant.NORMAL_IMAGE_ITEM;
    protected TemplateItem mSelectedTemplateItem;
    protected ArrayList<TemplateItem> mTemplateItemList = new ArrayList<>();
    private int mImageInTemplateCount = 0;

    protected HorizontalPreviewTemplateAdapter mTemplateAdapter;
    protected List<String> mSelectedPhotoPaths = new ArrayList<>();
    private Dialog mRatioDialog;
    private SharedPreferences mPref;
    protected int mLayoutRatio = RATIO_SQUARE;
    private ImageEntity mSelectedEntity = null;
    private QuickAction mTextQuickAction;
    private QuickAction mStickerQuickAction;
    protected SharedPreferences mPreferences;
    private boolean mIsFrameImage = true;
    private boolean mClickedShareButton = false;

    //abstract methods
    protected abstract int getLayoutId();

    protected abstract void buildLayout(TemplateItem templateItem);

    protected abstract Bitmap createOutputImage();

    protected boolean isShowingAllTemplates() {
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "onCreate, savedInstanceState=" + savedInstanceState);
        setContentView(getLayoutId());
        mPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mLayoutRatio = mPref.getInt(RATIO_KEY, RATIO_SQUARE);
        mImageInTemplateCount = getIntent().getIntExtra(ThumbListActivity.EXTRA_IMAGE_IN_TEMPLATE_COUNT, 0);
        mIsFrameImage = getIntent().getBooleanExtra(ThumbListActivity.EXTRA_IS_FRAME_IMAGE, true);
        final int selectedItemIndex = getIntent().getIntExtra(ThumbListActivity.EXTRA_SELECTED_TEMPLATE_INDEX, 0);
        final ArrayList<String> extraImagePaths = getIntent().getStringArrayListExtra(ThumbListActivity.EXTRA_IMAGE_PATHS);
        //pref
        mPreferences = getSharedPreferences(AppConstant.PREF_NAME, Context.MODE_PRIVATE);
        mContainerLayout = (RelativeLayout) findViewById(R.id.containerLayout);
        mTemplateView = (RecyclerView) findViewById(R.id.templateView);
        mPhotoView = new PhotoView(this);
        mPhotoView.setOnDoubleClickListener(this);
        createQuickAction();

        mAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        //loading data
        if (savedInstanceState != null) {
            mClickedShareButton = savedInstanceState.getBoolean("mClickedShareButton", false);
            final int idx = savedInstanceState.getInt("mSelectedTemplateItemIndex", 0);
            mImageInTemplateCount = savedInstanceState.getInt("mImageInTemplateCount", 0);
            mIsFrameImage = savedInstanceState.getBoolean("mIsFrameImage", false);
            loadFrameImages(mIsFrameImage);
            Logger.d(TAG, "onCreate, mTemplateItemList size=" + mTemplateItemList.size() + ", selected idx=" + idx + ", mImageInTemplateCount=" + mImageInTemplateCount);
            if (idx < mTemplateItemList.size() && idx >= 0)
                mSelectedTemplateItem = mTemplateItemList.get(idx);
            if (mSelectedTemplateItem != null) {
                ArrayList<String> imagePaths = savedInstanceState.getStringArrayList("photoItemImagePaths");
                if (imagePaths != null) {
                    int size = Math.min(imagePaths.size(), mSelectedTemplateItem.getPhotoItemList().size());
                    for (int i = 0; i < size; i++)
                        mSelectedTemplateItem.getPhotoItemList().get(i).imagePath = imagePaths.get(i);
                }
            }
            ArrayList<MultiTouchEntity> entities = savedInstanceState.getParcelableArrayList("mPhotoViewImageEntities");
            if (entities != null) {
                mPhotoView.setImageEntities(entities);
            }
        } else {
            loadFrameImages(mIsFrameImage);
            mSelectedTemplateItem = mTemplateItemList.get(selectedItemIndex);
            mSelectedTemplateItem.setSelected(true);
            if (extraImagePaths != null) {
                int size = Math.min(extraImagePaths.size(), mSelectedTemplateItem.getPhotoItemList().size());
                for (int i = 0; i < size; i++)
                    mSelectedTemplateItem.getPhotoItemList().get(i).imagePath = extraImagePaths.get(i);
            }
        }

        mTemplateAdapter = new HorizontalPreviewTemplateAdapter(mTemplateItemList, this, mIsFrameImage);
        //Show templates
        mTemplateView.setHasFixedSize(true);
        mTemplateView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTemplateView.setAdapter(mTemplateAdapter);
        //Create after initializing
        mContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mOutputScale = ImageUtils.calculateOutputScaleFactor(mContainerLayout.getWidth(), mContainerLayout.getHeight());
                buildLayout(mSelectedTemplateItem);
                // remove listener
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContainerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mContainerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        //Scroll to selected item
        if (mTemplateItemList != null && selectedItemIndex >= 0 && selectedItemIndex < mTemplateItemList.size()) {
            mTemplateView.scrollToPosition(selectedItemIndex);
        }

//        if (!AdManager.isloadFbAd) {
//            //admob
//            AdManager.initAd(BaseTemplateDetailActivity.this);
//            AdManager.loadInterAd(BaseTemplateDetailActivity.this);
//        } else {
//            //Fb banner Ads
//            AdManager.loadFbInterAd(BaseTemplateDetailActivity.this);
//        }
    }

    @Override
    protected void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    private void loadFrameImages(boolean isFrameImage) {
        ArrayList<TemplateItem> mAllTemplateItemList = new ArrayList<>();
        if (!isFrameImage) {
            mAllTemplateItemList.addAll(TemplateImageUtils.loadTemplates());
        } else {
            mAllTemplateItemList.addAll(FrameImageUtils.loadFrameImages(this));
        }

        mTemplateItemList = new ArrayList<>();
        if (mImageInTemplateCount > 0) {
            for (TemplateItem item : mAllTemplateItemList)
                if (item.getPhotoItemList().size() == mImageInTemplateCount) {
                    mTemplateItemList.add(item);
                }
        } else {
            mTemplateItemList.addAll(mAllTemplateItemList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int idx = mTemplateItemList.indexOf(mSelectedTemplateItem);
        if (idx < 0) idx = 0;
        Logger.d(TAG, "onSaveInstanceState, idx=" + idx);
        outState.putInt("mSelectedTemplateItemIndex", idx);
        //saved all image path of template item
        ArrayList<String> imagePaths = new ArrayList<>();
        for (PhotoItem item : mSelectedTemplateItem.getPhotoItemList()) {
            if (item.imagePath == null) item.imagePath = "";
            imagePaths.add(item.imagePath);
        }
        outState.putStringArrayList("photoItemImagePaths", imagePaths);
        outState.putParcelableArrayList("mPhotoViewImageEntities", mPhotoView.getImageEntities());
        outState.putInt("mImageInTemplateCount", mImageInTemplateCount);
        outState.putBoolean("mIsFrameImage", mIsFrameImage);
        outState.putBoolean("mClickedShareButton", mClickedShareButton);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("PhotoCollageFragment.onPause",
                "onPause: width=" + mPhotoView.getWidth() + ", height = "
                        + mPhotoView.getHeight());
        mPhotoView.unloadImages();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("PhotoCollageFragment.onResume",
                "onResume: width=" + mPhotoView.getWidth() + ", height = "
                        + mPhotoView.getHeight());
        mPhotoView.loadImages(this);
        mPhotoView.invalidate();
        if (mClickedShareButton) {
            mClickedShareButton = false;

        }
    }

    private void createQuickAction() {
        QuickActionItem deleteItem = new QuickActionItem(ID_DELETE, getString(R.string.delete), getResources().getDrawable(R.drawable.menu_delete));
        QuickActionItem cancelItem = new QuickActionItem(ID_CANCEL, getString(R.string.cancel), getResources().getDrawable(R.drawable.menu_cancel));

        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        mTextQuickAction = new QuickAction(this, QuickAction.HORIZONTAL);
        mStickerQuickAction = new QuickAction(this, QuickAction.HORIZONTAL);
        //add action items into QuickAction
        mTextQuickAction.addActionItem(deleteItem);
        mTextQuickAction.addActionItem(cancelItem);
        mStickerQuickAction.addActionItem(deleteItem);
        mStickerQuickAction.addActionItem(cancelItem);
        //Set listener for action item clicked
        mTextQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                QuickActionItem quickActionItem = mTextQuickAction.getActionItem(pos);
                mTextQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    mPhotoView.removeImageEntity(mSelectedEntity);
                } else if (actionId == ID_CANCEL) {

                }
            }
        });
        //Set listener for action item clicked
        mStickerQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                QuickActionItem quickActionItem = mStickerQuickAction.getActionItem(pos);
                mStickerQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    mPhotoView.removeImageEntity(mSelectedEntity);
                } else if (actionId == ID_CANCEL) {

                }
            }
        });
        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
        mTextQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

    }

    @Override
    public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity) {
        mSelectedEntity = (ImageEntity) entity;
        if (mSelectedEntity instanceof TextEntity) {
            mTextQuickAction.show(view, (int) mSelectedEntity.getCenterX(), (int) mSelectedEntity.getCenterY());
        } else {
            mStickerQuickAction.show(view, (int) mSelectedEntity.getCenterX(), (int) mSelectedEntity.getCenterY());
        }
    }

    @Override
    public void onBackgroundDoubleClick() {

    }

    @Override
    public void onPreviewTemplateClick(TemplateItem item) {
        mSelectedTemplateItem.setSelected(false);
        for (int idx = 0; idx < mSelectedTemplateItem.getPhotoItemList().size(); idx++) {
            PhotoItem photoItem = mSelectedTemplateItem.getPhotoItemList().get(idx);
            if (photoItem.imagePath != null && photoItem.imagePath.length() > 0) {
                if (idx < mSelectedPhotoPaths.size()) {
                    mSelectedPhotoPaths.add(idx, photoItem.imagePath);
                } else {
                    mSelectedPhotoPaths.add(photoItem.imagePath);
                }
            }
        }

        final int size = Math.min(mSelectedPhotoPaths.size(), item.getPhotoItemList().size());
        for (int idx = 0; idx < size; idx++) {
            PhotoItem photoItem = item.getPhotoItemList().get(idx);
            if (photoItem.imagePath == null || photoItem.imagePath.length() < 1) {
                photoItem.imagePath = mSelectedPhotoPaths.get(idx);
            }
        }

        mSelectedTemplateItem = item;
        mSelectedTemplateItem.setSelected(true);
        mTemplateAdapter.notifyDataSetChanged();

        buildLayout(item);

    }


    public void clickRatio(){
        if (mRatioDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String[] layoutRatioName = new String[]{getString(R.string.photo_editor_square), getString(R.string.fit),
                    getString(R.string.golden_ratio),};

            builder.setTitle(R.string.select_ratio);
            builder.setSingleChoiceItems(layoutRatioName, mPref.getInt(RATIO_KEY, 0),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPref.edit().putInt(RATIO_KEY, which).commit();
                            mLayoutRatio = which;
                            dialog.dismiss();
                            buildLayout(mSelectedTemplateItem);
                        }
                    });
            mRatioDialog = builder.create();
        }
        mRatioDialog.show();
    }


    public void asyncSaveAndShare() {
        AsyncTask<Void, Void, File> task = new AsyncTask<Void, Void, File>() {
            Dialog dialog;
            String errMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(BaseTemplateDetailActivity.this, getString(R.string.app_name), getString(R.string.creating));
            }

            @Override
            protected File doInBackground(Void... params) {
                try {
                    Bitmap image = createOutputImage();
                    String fileName = DateTimeUtils.getCurrentDateTime().replaceAll(":", "-").concat(".png");
                    File collageFolder = new File(ImageUtils.OUTPUT_COLLAGE_FOLDER);
                    if (!collageFolder.exists()) {
                        collageFolder.mkdirs();
                    }
                    File photoFile = new File(collageFolder, fileName);
                    image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
                    Utils.mediaScanner(BaseTemplateDetailActivity.this, ImageUtils.OUTPUT_COLLAGE_FOLDER+"/", fileName);
                    return photoFile;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errMsg = ex.getMessage();
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                    errMsg = err.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                try {
                    dialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (file != null) {
                    Intent intent = new Intent(BaseTemplateDetailActivity.this, ShareActivity.class);
                    intent.putExtra("path", file.getAbsolutePath());
                    intent.putExtra("isCreation", false);

//                    if (!AdManager.isloadFbAd) {
//                        AdManager.adCounter = 6;
//                        AdManager.showInterAd(BaseTemplateDetailActivity.this, intent,0);
//                    } else {
//                        AdManager.adCounter = 6;
//                        AdManager.showFbInterAd(BaseTemplateDetailActivity.this, intent,0);
//                    }
                    Toast.makeText(BaseTemplateDetailActivity.this, "Salvo com Sucesso...", Toast.LENGTH_LONG).show();
                } else if (errMsg != null) {
                    Toast.makeText(BaseTemplateDetailActivity.this, errMsg, Toast.LENGTH_LONG).show();
                }
                //log
                Bundle bundle = new Bundle();
                if (mIsFrameImage) {
                    String[] layoutRatioName = new String[]{"square", "fit", "golden"};
                    String ratio = "";
                    if (mLayoutRatio < layoutRatioName.length)
                        ratio = layoutRatioName[mLayoutRatio];
                } else {

                }

            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public int[] calculateThumbnailSize(int imageWidth, int imageHeight) {
        int[] size = new int[2];
        float ratioWidth = ((float) imageWidth) / getPhotoViewWidth();
        float ratioHeight = ((float) imageHeight) / getPhotoViewHeight();
        float ratio = Math.max(ratioWidth, ratioHeight);
        if (ratio == ratioWidth) {
            size[0] = getPhotoViewWidth();
            size[1] = (int) (imageHeight / ratio);
        } else {
            size[0] = (int) (imageWidth / ratio);
            size[1] = getPhotoViewHeight();
        }

        return size;
    }

    private int getPhotoViewWidth() {
        return mContainerLayout.getWidth();
    }

    private int getPhotoViewHeight() {
        return mContainerLayout.getHeight();
    }

    public void textButtonClick(){
        mItemType = AppConstant.TEXT_ITEM;
        addTextItem();
    }

    @Override
    public void resultStickers(Uri[] uri) {
        super.resultPickMultipleImages(uri);
        final int size = uri.length;

        for (int idx = 0; idx < size; idx++) {
            float angle = (float) (idx * Math.PI / 20);

            ImageEntity entity = new ImageEntity(uri[idx], getResources());
            entity.setInitScaleFactor(0.25);
            entity.load(this,
                    (mPhotoView.getWidth() - entity.getWidth()) / 2,
                    (mPhotoView.getHeight() - entity.getHeight()) / 2, angle);
            mPhotoView.addImageEntity(entity);
            if (ResultContainer.getInstance().getImageEntities() != null) {
                ResultContainer.getInstance().getImageEntities().add(entity);
            }
        }
    }

    @Override
    protected void resultAddTextItem(String text, int color, String fontPath) {
        final TextEntity entity = new TextEntity(text, getResources());
        entity.setTextColor(color);
        entity.setTypefacePath(fontPath, BaseTemplateDetailActivity.this);
        entity.load(this,
                (mPhotoView.getWidth() - entity.getWidth()) / 2,
                (mPhotoView.getHeight() - entity.getHeight()) / 2);
        entity.setSticker(false);
        entity.setDrawImageBorder(true);
        mPhotoView.addImageEntity(entity);
        if (ResultContainer.getInstance().getImageEntities() != null) {
            ResultContainer.getInstance().getImageEntities().add(entity);
        }
    }

    @Override
    protected void resultEditTextItem(String text, int color, String fontPath) {
        if (mSelectedEntity instanceof TextEntity) {
            TextEntity textEntity = (TextEntity) mSelectedEntity;
            textEntity.setTextColor(color);
            textEntity.setTypefacePath(fontPath, BaseTemplateDetailActivity.this);
            textEntity.setText(text);
        }
    }

}
