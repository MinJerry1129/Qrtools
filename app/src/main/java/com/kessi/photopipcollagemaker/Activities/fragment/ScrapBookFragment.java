package com.kessi.photopipcollagemaker.Activities.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.photopipcollagemaker.ShareActivity;
import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.StartActivity;
import com.kessi.photopipcollagemaker.adapter.ScrapBGAdapter;
import com.kessi.photopipcollagemaker.adapter.ScrapStickerAdapter;
import com.kessi.photopipcollagemaker.utils.Utils;
import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.kessi.photopipcollagemaker.appconfig.AppConstant;
import com.kessi.photopipcollagemaker.listener.OnChooseColorListener;
import com.kessi.photopipcollagemaker.multitouch.controller.ImageEntity;
import com.kessi.photopipcollagemaker.multitouch.controller.MultiTouchEntity;
import com.kessi.photopipcollagemaker.multitouch.controller.TextEntity;
import com.kessi.photopipcollagemaker.multitouch.custom.OnDoubleClickListener;
import com.kessi.photopipcollagemaker.multitouch.custom.PhotoView;
import com.kessi.photopipcollagemaker.quickaction.QuickAction;
import com.kessi.photopipcollagemaker.quickaction.QuickActionItem;
import com.kessi.photopipcollagemaker.utils.AdManager;
import com.kessi.photopipcollagemaker.utils.DateTimeUtils;
import com.kessi.photopipcollagemaker.utils.DialogUtils;
import com.kessi.photopipcollagemaker.utils.ImageUtils;
import com.kessi.photopipcollagemaker.utils.PhotoUtils;
import com.kessi.photopipcollagemaker.utils.ResultContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScrapBookFragment extends BaseFragment implements
        OnDoubleClickListener {
    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_CHANGE = 2;
    private static final int ID_DELETE = 3;
    private static final int ID_CANCEL = 4;


    private PhotoView mPhotoView;
    private ViewGroup mPhotoLayout;

    private int mItemType = AppConstant.NORMAL_IMAGE_ITEM;
    // edit image
    private ImageEntity mSelectedEntity = null;
    // use for animation, these views are found from dialogs

    private int mPhotoViewWidth;
    private int mPhotoViewHeight;
    private SharedPreferences mPreferences;
    private OnChooseColorListener mChooseColorListener;
    private int mCurrentColor = Color.WHITE;

    private QuickAction mTextQuickAction;
    private QuickAction mPhotoQuickAction;

    private RecyclerView stickerRecycler, bgImgRecycler;
    private String[] emojies;
    private String[] colors;

    private void createQuickAction() {
        QuickActionItem deleteItem = new QuickActionItem(ID_DELETE, mActivity.getString(R.string.delete), mActivity.getResources().getDrawable(R.drawable.menu_delete));
        QuickActionItem cancelItem = new QuickActionItem(ID_CANCEL, mActivity.getString(R.string.cancel), mActivity.getResources().getDrawable(R.drawable.menu_cancel));

        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        mTextQuickAction = new QuickAction(mActivity, QuickAction.HORIZONTAL);
        mPhotoQuickAction = new QuickAction(mActivity, QuickAction.HORIZONTAL);
        //add action items into QuickAction
        mTextQuickAction.addActionItem(deleteItem);
        mTextQuickAction.addActionItem(cancelItem);

        mPhotoQuickAction.addActionItem(deleteItem);
        mPhotoQuickAction.addActionItem(cancelItem);
        //Set listener for action item clicked
        mPhotoQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                QuickActionItem quickActionItem = mPhotoQuickAction.getActionItem(pos);
                mPhotoQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    mPhotoView.removeImageEntity(mSelectedEntity);
//            // save result
                    ResultContainer.getInstance().removeImageEntity(mSelectedEntity);
                } else if (actionId == ID_CANCEL) {

                }
            }
        });
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

//        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
//        //by clicking the area outside the dialog.
        mTextQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPreferences = activity.getSharedPreferences(AppConstant.PREF_NAME,
                Context.MODE_PRIVATE);
        try {

            if (activity instanceof OnChooseColorListener) {
                mChooseColorListener = (OnChooseColorListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            ResultContainer.getInstance().restoreFromBundle(savedInstanceState);
            mPhotoViewWidth = savedInstanceState
                    .getInt(AppConstant.PHOTO_VIEW_WIDTH_KEY);
            mPhotoViewHeight = savedInstanceState
                    .getInt(AppConstant.PHOTO_VIEW_HEIGHT_KEY);
        }
        createQuickAction();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ResultContainer.getInstance().saveToBundle(outState);
        outState.putInt(AppConstant.PHOTO_VIEW_WIDTH_KEY, mPhotoViewWidth);
        outState.putInt(AppConstant.PHOTO_VIEW_HEIGHT_KEY, mPhotoViewHeight);
        outState.putParcelableArrayList("imageEntities", mPhotoView.getImageEntities());
        outState.putParcelable("backgroundUri", mPhotoView.getPhotoBackgroundUri());
        outState.putParcelable("mSelectedEntity", mSelectedEntity);
    }

    ImageView back, ratio, save;
    LinearLayout gallery, bgBtn, sticker, textBtn;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d("PhotoCollageFragment.onCreateView", "onCreateView");
        final View rootView = inflater.inflate(R.layout.fragment_scrapbook,
                container, false);
        mPhotoLayout = (ViewGroup) rootView.findViewById(R.id.photoLayout);

        mPhotoView = new PhotoView(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mPhotoLayout.addView(mPhotoView, params);
        mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        mPhotoViewWidth = mPhotoView.getWidth();
                        mPhotoViewHeight = mPhotoView.getHeight();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mPhotoView.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            mPhotoView.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }

                    }
                });

        mPhotoView.setOnDoubleClickListener(this);
        // set title
        mActivity = getActivity();
        mActivity.setTitle(
                R.string.create_from_photo);


        gallery = rootView.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn(rootView);
                ((ImageView)rootView.findViewById(R.id.tabIV)).setColorFilter(ContextCompat.getColor(getActivity(), R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)rootView.findViewById(R.id.tabTxt)).setTextColor(getActivity().getResources().getColor(R.color.btn_icon_color));

                hideControls();
                mItemType = AppConstant.NORMAL_IMAGE_ITEM;
                pickMultipleImageFromGallery();
            }
        });

        back = rootView.findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });

        ratio = rootView.findViewById(R.id.ratio);
        ratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDeleteCurrentPhotoView();
            }
        });

        save = rootView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickShareView();
            }
        });

        stickerRecycler = rootView.findViewById(R.id.stickerRecycler);
        stickerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        try {
            emojies = getActivity().getAssets().list("stickers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScrapStickerAdapter adapter = new ScrapStickerAdapter(getActivity(), emojies, ScrapBookFragment.this);
        stickerRecycler.setAdapter(adapter);

        sticker = rootView.findViewById(R.id.sticker);
        sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn(rootView);
                ((ImageView)rootView.findViewById(R.id.tabIV1)).setColorFilter(ContextCompat.getColor(getActivity(), R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)rootView.findViewById(R.id.tabTxt1)).setTextColor(getActivity().getResources().getColor(R.color.btn_icon_color));

                hideControls();
                stickerRecycler.setVisibility(View.VISIBLE);

                startActivityes(null,0);

            }
        });

        bgImgRecycler = rootView.findViewById(R.id.bgImgRecycler);
        bgImgRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        try {
            colors = getActivity().getAssets().list("backgrounds_image");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScrapBGAdapter cadapter = new ScrapBGAdapter(getActivity(), colors, ScrapBookFragment.this);
        bgImgRecycler.setAdapter(cadapter);

        bgBtn = rootView.findViewById(R.id.bgBtn);
        bgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn(rootView);
                ((ImageView)rootView.findViewById(R.id.tabIV2)).setColorFilter(ContextCompat.getColor(getActivity(), R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)rootView.findViewById(R.id.tabTxt2)).setTextColor(getActivity().getResources().getColor(R.color.btn_icon_color));

                hideControls();
                bgImgRecycler.setVisibility(View.VISIBLE);

                startActivityes(null,0);
            }
        });

        textBtn = rootView.findViewById(R.id.textBtn);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn(rootView);
                ((ImageView)rootView.findViewById(R.id.tabIV3)).setColorFilter(ContextCompat.getColor(getActivity(), R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)rootView.findViewById(R.id.tabTxt3)).setTextColor(getActivity().getResources().getColor(R.color.btn_icon_color));


                hideControls();
                mItemType = AppConstant.TEXT_ITEM;
                addTextItem();
            }
        });

        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(getActivity());
            AdManager.loadInterAd(getActivity());
        } else {
            //Fb banner Ads
            AdManager.loadFbInterAd(getActivity());
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    void startActivityes(Intent intent, int requestCode) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(getActivity(), intent, requestCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(getActivity(), intent, requestCode);
        }
    }

    void setUnpressBtn(View view) {
        ((ImageView) view.findViewById(R.id.tabIV)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) view.findViewById(R.id.tabTxt)).setTextColor(Color.WHITE);

        ((ImageView) view.findViewById(R.id.tabIV1)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) view.findViewById(R.id.tabTxt1)).setTextColor(Color.WHITE);

        ((ImageView) view.findViewById(R.id.tabIV2)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) view.findViewById(R.id.tabTxt2)).setTextColor(Color.WHITE);

        ((ImageView) view.findViewById(R.id.tabIV3)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) view.findViewById(R.id.tabTxt3)).setTextColor(Color.WHITE);
    }

    void hideControls() {
        stickerRecycler.setVisibility(View.GONE);
        bgImgRecycler.setVisibility(View.GONE);
    }

    public void setEmojiesSticker(String name) {
        InputStream inputStream = null;
        try {
            // get input stream
            inputStream = getActivity().getAssets().open("stickers/" + name);
            // load image as Drawable
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File path = new File(Environment.getExternalStorageDirectory() + "/stickers");
            if (!path.isDirectory()) {
                path.mkdirs();
            }
            File mypath = new File(path.getAbsolutePath(), name);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                ImageEntity entity = new ImageEntity(Uri.fromFile(mypath), getResources());
                entity.setInitScaleFactor(0.5f);
                entity.setSticker(false);
                entity.load(getActivity(),
                        (mPhotoView.getWidth() - entity.getWidth()) / 2,
                        (mPhotoView.getHeight() - entity.getHeight()) / 2, 0);
                mPhotoView.addImageEntity(entity);
                if (ResultContainer.getInstance().getImageEntities() != null) {
                    ResultContainer.getInstance().getImageEntities().add(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            return;
        }
        hideControls();
    }

    public void setBackground(String name) {
        InputStream inputStream = null;
        try {
            // get input stream
            inputStream = getActivity().getAssets().open("backgrounds_image/" + name);
            // load image as Drawable
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File path = new File(Environment.getExternalStorageDirectory() + "/backgrounds_image");
            if (!path.isDirectory()) {
                path.mkdirs();
            }
            File mypath = new File(path.getAbsolutePath(), name);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                mPhotoView.setPhotoBackground(Uri.fromFile(mypath));
                ResultContainer.getInstance().setPhotoBackgroundImage(Uri.fromFile(mypath));
                mItemType = AppConstant.NORMAL_IMAGE_ITEM;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            return;
        }
        hideControls();
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
        mPhotoView.loadImages(getActivity());
        mPhotoView.invalidate();
    }

    public void clickDeleteCurrentPhotoView() {
        if (!already()) {
            return;
        }

        DialogUtils.showConfirmDialog(getActivity(), R.string.confirm,
                R.string.confirm_delete_photo,
                new DialogUtils.ConfirmDialogOnClickListener() {

                    @Override
                    public void onOKButtonOnClick() {
                        ResultContainer.getInstance().clearAll();
                        mPhotoView.clearAllImageEntities();
                        mPhotoView.destroyBackground();
                    }

                    @Override
                    public void onCancelButtonOnClick() {

                    }
                });
    }

    @Override
    protected void resultAddTextItem(String text, int color, String fontPath) {
        final TextEntity entity = new TextEntity(text, getResources());
        entity.setTextColor(color);
        entity.setTypefacePath(fontPath, getActivity());
        entity.load(getActivity(),
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
    public void resultFromPhotoEditor(Uri uri) {
        super.resultFromPhotoEditor(uri);

        Logger.d("PhotoCollageFragment.resultFromPhotoEditor", "uri=" + uri.toString());
        if (!already()) {
            return;
        }

        if (mItemType != AppConstant.BACKGROUND_ITEM) {
            ImageEntity entity = new ImageEntity(uri, getResources());
            entity.setSticker(false);
            entity.load(getActivity(),
                    (mPhotoViewWidth - entity.getWidth()) / 2,
                    (mPhotoViewHeight - entity.getHeight()) / 2);
            mPhotoView.addImageEntity(entity);
            if (ResultContainer.getInstance().getImageEntities() != null) {
                ResultContainer.getInstance().getImageEntities().add(entity);
            }

        } else {
            mPhotoView.setPhotoBackground(uri);
            ResultContainer.getInstance().setPhotoBackgroundImage(uri);
        }
    }

    @Override
    protected void resultSticker(Uri uri) {
        super.resultSticker(uri);
        ImageEntity entity = new ImageEntity(uri, getResources());
        entity.load(getActivity(),
                (mPhotoView.getWidth() - entity.getWidth()) / 2,
                (mPhotoView.getHeight() - entity.getHeight()) / 2);
        entity.setSticker(true);
        mPhotoView.addImageEntity(entity);
        if (ResultContainer.getInstance().getImageEntities() != null) {
            ResultContainer.getInstance().getImageEntities().add(entity);
        }
    }

    @Override
    protected void resultBackground(Uri uri) {
        super.resultBackground(uri);
        mPhotoView.setPhotoBackground(uri);
        ResultContainer.getInstance().setPhotoBackgroundImage(uri);
        mItemType = AppConstant.NORMAL_IMAGE_ITEM;
    }

    @Override
    protected void resultEditImage(Uri uri) {
        super.resultEditImage(uri);
        mSelectedEntity.setImageUri(getActivity(), uri);
        mPhotoView.invalidate();
    }

    @Override
    protected void resultStickers(Uri[] uri) {
        super.resultStickers(uri);
        if (!already()) {
            return;
        }
        final int size = uri.length;

        for (int idx = 0; idx < size; idx++) {
            float angle = (float) (idx * Math.PI / 20);

            ImageEntity entity = new ImageEntity(uri[idx], getResources());
            entity.load(getActivity(),
                    (mPhotoViewWidth - entity.getWidth()) / 2,
                    (mPhotoViewHeight - entity.getHeight()) / 2, angle);
            mPhotoView.addImageEntity(entity);
            if (ResultContainer.getInstance().getImageEntities() != null) {
                ResultContainer.getInstance().getImageEntities().add(entity);
            }
        }
    }

    @Override
    public void resultPickMultipleImages(Uri[] uri) {
        super.resultPickMultipleImages(uri);
        if (!already()) {
            return;
        }
        final int size = uri.length;

        for (int idx = 0; idx < size; idx++) {
            float angle = (float) (idx * Math.PI / 20);

            ImageEntity entity = new ImageEntity(uri[idx], getResources());
            entity.setInitScaleFactor(0.5f);
            entity.setSticker(false);
            entity.load(getActivity(),
                    (mPhotoViewWidth - entity.getWidth()) / 2,
                    (mPhotoViewHeight - entity.getHeight()) / 2, angle);
            mPhotoView.addImageEntity(entity);
            if (ResultContainer.getInstance().getImageEntities() != null) {
                ResultContainer.getInstance().getImageEntities().add(entity);
            }
        }
    }

    public void clickShareView() {
        if (!already()) {
            return;
        }
        mActivity = getActivity();
        final Bitmap image = mPhotoView.getImage(ImageUtils.calculateOutputScaleFactor(mPhotoView.getWidth(), mPhotoView.getHeight()));
        AsyncTask<Void, Void, File> task = new AsyncTask<Void, Void, File>() {
            Dialog dialog;
            String errMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(mActivity, getString(R.string.app_name), getString(R.string.creating));
            }

            @Override
            protected File doInBackground(Void... params) {
                try {
                    String fileName = DateTimeUtils.getCurrentDateTime().replaceAll(":", "-").concat(".png");
                    File collageFolder = new File(ImageUtils.OUTPUT_COLLAGE_FOLDER);
                    if (!collageFolder.exists()) {
                        collageFolder.mkdirs();
                    }
                    File photoFile = new File(collageFolder, fileName);
                    image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
                    PhotoUtils.addImageToGallery(photoFile.getAbsolutePath(), mActivity);
                    Utils.mediaScanner(mActivity, ImageUtils.OUTPUT_COLLAGE_FOLDER+"/", fileName);
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
                dialog.dismiss();
                if (file != null) {
                    Intent intent = new Intent(getActivity(), ShareActivity.class);
                    intent.putExtra("path", file.getAbsolutePath());
                    intent.putExtra("isCreation", false);

                    if (!AdManager.isloadFbAd) {
                        AdManager.adCounter = 6;
                        AdManager.showInterAd(getActivity(), intent,0);
                    } else {
                        AdManager.adCounter = 6;
                        AdManager.showFbInterAd(getActivity(), intent,0);
                    }
                    Toast.makeText(mActivity, "Saved Successfully....", Toast.LENGTH_LONG).show();
                } else if (errMsg != null) {
                    Toast.makeText(mActivity, errMsg, Toast.LENGTH_LONG).show();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity) {
        if (!already()) {
            return;
        }
        mSelectedEntity = (ImageEntity) entity;
        if (mSelectedEntity.isSticker()) {
        } else if (mSelectedEntity instanceof TextEntity) {
            mTextQuickAction.show(mPhotoView, (int) entity.getCenterX(), (int) entity.getCenterY());
        } else {
            mPhotoQuickAction.show(mPhotoView, (int) entity.getCenterX(), (int) entity.getCenterY());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("PhotoCollageFragment.onDestroyView", "Destroy view");
        mPhotoView.unloadImages();
        mPhotoView.setImageEntities(null);
        mPhotoView.destroyBackground();
    }

    private void drawImageBounds(int color) {
        mPhotoView.setBorderColor(color);
    }

    @Override
    public void onBackgroundDoubleClick() {
    }

}
