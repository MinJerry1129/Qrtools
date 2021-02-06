package com.kessi.photopipcollagemaker.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kessi.photopipcollagemaker.adapter.PipStickerAdapter;
import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.model.TemplateItem;
import com.kessi.photopipcollagemaker.multitouch.controller.ImageEntity;
import com.kessi.photopipcollagemaker.template.ItemImageView;
import com.kessi.photopipcollagemaker.template.PhotoItem;
import com.kessi.photopipcollagemaker.template.PhotoLayout;
import com.kessi.photopipcollagemaker.Activities.custom.TransitionImageView;
import com.kessi.photopipcollagemaker.utils.AdManager;
import com.kessi.photopipcollagemaker.utils.FileUtils;
import com.kessi.photopipcollagemaker.utils.PhotoUtils;
import com.kessi.photopipcollagemaker.utils.ResultContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class PIPActivity extends BaseTemplateDetailActivity implements PhotoLayout.OnQuickActionClickListener {
    private PhotoLayout mPhotoLayout;
    private ItemImageView mSelectedItemImageView;
    private TransitionImageView mBackgroundImageView;

    private ImageView back, save;
    LinearLayout pipBtn, sticker, textBtn;
    private FrameLayout templateLayout;
    private RecyclerView stickerRecycler;
    private String[] emojies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (PhotoItem item : mSelectedTemplateItem.getPhotoItemList())
            if (item.imagePath != null && item.imagePath.length() > 0) {
                mSelectedPhotoPaths.add(item.imagePath);
            }


        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSaveAndShare();
            }
        });

        templateLayout = findViewById(R.id.templateLayout);
        pipBtn = findViewById(R.id.pipBtn);
        pipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV1)).setColorFilter(ContextCompat.getColor(PIPActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt1)).setTextColor(getResources().getColor(R.color.btn_icon_color));


                hideControls();
                templateLayout.setVisibility(View.VISIBLE);
                startActivityes(null,0);

            }
        });

        stickerRecycler = findViewById(R.id.stickerRecycler);
        stickerRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        try {
            emojies = getAssets().list("stickers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PipStickerAdapter adapter = new PipStickerAdapter(PIPActivity.this, emojies);
        stickerRecycler.setAdapter(adapter);

        sticker = findViewById(R.id.sticker);
        sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV)).setColorFilter(ContextCompat.getColor(PIPActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt)).setTextColor(getResources().getColor(R.color.btn_icon_color));


                hideControls();
                stickerRecycler.setVisibility(View.VISIBLE);

                startActivityes(null,0);
            }
        });

        textBtn = findViewById(R.id.textBtn);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV2)).setColorFilter(ContextCompat.getColor(PIPActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt2)).setTextColor(getResources().getColor(R.color.btn_icon_color));


                hideControls();
                textButtonClick();
            }
        });
    }

    void startActivityes(Intent intent, int requestCode) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(PIPActivity.this, intent, requestCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(PIPActivity.this, intent, requestCode);
        }
    }

    void setUnpressBtn() {
        ((ImageView) findViewById(R.id.tabIV)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) findViewById(R.id.tabTxt)).setTextColor(Color.WHITE);

        ((ImageView) findViewById(R.id.tabIV1)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) findViewById(R.id.tabTxt1)).setTextColor(Color.WHITE);

        ((ImageView) findViewById(R.id.tabIV2)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) findViewById(R.id.tabTxt2)).setTextColor(Color.WHITE);
    }

    void hideControls() {
        templateLayout.setVisibility(View.GONE);
        stickerRecycler.setVisibility(View.GONE);
    }

    public void setEmojiesSticker(String name) {
        InputStream inputStream = null;
        try {
            // get input stream
            inputStream = getAssets().open("stickers/" + name);
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
                entity.load(PIPActivity.this,
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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pip;
    }

    @Override
    public Bitmap createOutputImage() {
        Bitmap template = mPhotoLayout.createImage();
        Bitmap result = Bitmap.createBitmap(template.getWidth(), template.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(template, 0, 0, paint);
        template.recycle();
        template = null;
        Bitmap stickers = mPhotoView.getImage(mOutputScale);
        canvas.drawBitmap(stickers, 0, 0, paint);
        stickers.recycle();
        stickers = null;
        System.gc();
        return result;
    }

    @Override
    public void onEditActionClick(ItemImageView v) {
        mSelectedItemImageView = v;
        if (v.getImage() != null && v.getPhotoItem().imagePath != null && v.getPhotoItem().imagePath.length() > 0) {
            Uri uri = Uri.fromFile(new File(v.getPhotoItem().imagePath));
            requestEditingImage(uri);
        }
    }

    @Override
    public void onChangeActionClick(ItemImageView v) {
        mSelectedItemImageView = v;
    }

    @Override
    public void onChangeBackgroundActionClick(TransitionImageView v) {
        mBackgroundImageView = v;
    }

    @Override
    protected void resultEditImage(Uri uri) {
        if (mBackgroundImageView != null) {
            mBackgroundImageView.setImagePath(FileUtils.getPath(this, uri));
            mBackgroundImageView = null;
        } else if (mSelectedItemImageView != null) {
            mSelectedItemImageView.setImagePath(FileUtils.getPath(this, uri));
        }
    }

    @Override
    protected void resultFromPhotoEditor(Uri image) {
        if (mBackgroundImageView != null) {
            mBackgroundImageView.setImagePath(FileUtils.getPath(this, image));
            mBackgroundImageView = null;
        } else if (mSelectedItemImageView != null) {
            mSelectedItemImageView.setImagePath(FileUtils.getPath(this, image));
        }
    }

    @Override
    protected void resultBackground(Uri uri) {
        if (mBackgroundImageView != null) {
            mBackgroundImageView.setImagePath(FileUtils.getPath(this, uri));
            mBackgroundImageView = null;
        } else if (mSelectedItemImageView != null) {
            mSelectedItemImageView.setImagePath(FileUtils.getPath(this, uri));
        }
    }

    @Override
    protected void buildLayout(TemplateItem templateItem) {
        Bitmap backgroundImage = null;
        if (mPhotoLayout != null) {
            backgroundImage = mPhotoLayout.getBackgroundImage();
            mPhotoLayout.recycleImages(false);
        }

        final Bitmap frameImage = PhotoUtils.decodePNGImage(this, templateItem.getTemplate());
        int[] size = calculateThumbnailSize(frameImage.getWidth(), frameImage.getHeight());
        //Photo Item item_quick_action must be descended by index before creating photo layout.
        mPhotoLayout = new PhotoLayout(this, templateItem.getPhotoItemList(), frameImage);
        mPhotoLayout.setBackgroundImage(backgroundImage);
        mPhotoLayout.setQuickActionClickListener(this);
        mPhotoLayout.build(size[0], size[1], mOutputScale);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size[0], size[1]);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContainerLayout.removeAllViews();
        mContainerLayout.addView(mPhotoLayout, params);
        //add sticker view
        mContainerLayout.removeView(mPhotoView);
        mContainerLayout.addView(mPhotoView, params);
    }

    @Override
    public void finish() {
        if (mPhotoLayout != null) {
            mPhotoLayout.recycleImages(true);
        }
        super.finish();
    }
}
