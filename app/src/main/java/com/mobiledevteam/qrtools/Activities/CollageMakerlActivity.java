package com.mobiledevteam.qrtools.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiledevteam.qrtools.adapter.ColorAdapter;
import com.mobiledevteam.qrtools.R;
import com.mobiledevteam.qrtools.adapter.StickerAdapter;
import com.mobiledevteam.qrtools.gallery.CustomGalleryActivity;
import com.mobiledevteam.qrtools.frame.FrameImageView;
import com.mobiledevteam.qrtools.frame.FramePhotoLayout;
import com.mobiledevteam.qrtools.model.TemplateItem;
import com.mobiledevteam.qrtools.multitouch.controller.ImageEntity;
import com.mobiledevteam.qrtools.utils.AdManager;
import com.mobiledevteam.qrtools.utils.FileUtils;
import com.mobiledevteam.qrtools.utils.ImageDecoder;
import com.mobiledevteam.qrtools.utils.ImageUtils;
import com.mobiledevteam.qrtools.utils.ResultContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class CollageMakerlActivity extends BaseTemplateDetailActivity implements FramePhotoLayout.OnQuickActionClickListener {
    private static final int REQUEST_SELECT_PHOTO = 1001;
    private static float MAX_SPACE;
    private static float MAX_CORNER;
    private static float DEFAULT_SPACE;
    private static final float MAX_SPACE_PROGRESS = 300.0f;
    private static final float MAX_CORNER_PROGRESS = 200.0f;

    private FrameImageView mSelectedFrameImageView;
    private FramePhotoLayout mFramePhotoLayout;
    private LinearLayout mSpaceLayout;
    private SeekBar mSpaceBar;
    private SeekBar mCornerBar;
    private float mSpace = DEFAULT_SPACE;
    private float mCorner = 0;
    //Background
    private int mBackgroundColor = Color.WHITE;
    private Bitmap mBackgroundImage;
    private Uri mBackgroundUri = null;
    //Saved instance state
    private Bundle mSavedInstanceState;

    private ImageView back, ratio, save;
    private LinearLayout layout, sticker, adjust, bgcolor, textBtn;
    private FrameLayout templateLayout;
    private RecyclerView stickerRecycler, bgColorRecycler;
    private String[] emojies;
    private String[] colors;

    @Override
    protected boolean isShowingAllTemplates() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MAX_SPACE = ImageUtils.pxFromDp(CollageMakerlActivity.this, 30);
        MAX_CORNER = ImageUtils.pxFromDp(CollageMakerlActivity.this, 60);
        DEFAULT_SPACE = ImageUtils.pxFromDp(CollageMakerlActivity.this, 2);


        //restore old params
        if (savedInstanceState != null) {
            mSpace = savedInstanceState.getFloat("mSpace");
            mCorner = savedInstanceState.getFloat("mCorner");
            mBackgroundColor = savedInstanceState.getInt("mBackgroundColor");
            mBackgroundUri = savedInstanceState.getParcelable("mBackgroundUri");
            mSavedInstanceState = savedInstanceState;
            if (mBackgroundUri != null)
                mBackgroundImage = ImageDecoder.decodeUriToBitmap(this, mBackgroundUri);
        }

        mSpaceBar = (SeekBar) findViewById(R.id.spaceBar);
        mSpaceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpace = MAX_SPACE * seekBar.getProgress() / MAX_SPACE_PROGRESS;
                if (mFramePhotoLayout != null)
                    mFramePhotoLayout.setSpace(mSpace, mCorner);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCornerBar = (SeekBar) findViewById(R.id.cornerBar);
        mCornerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCorner = MAX_CORNER * seekBar.getProgress() / MAX_CORNER_PROGRESS;
                if (mFramePhotoLayout != null)
                    mFramePhotoLayout.setSpace(mSpace, mCorner);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ratio = findViewById(R.id.ratio);
        ratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRatio();
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
        layout = findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV)).setColorFilter(ContextCompat.getColor(CollageMakerlActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt)).setTextColor(getResources().getColor(R.color.btn_icon_color));

                hideControls();
                templateLayout.setVisibility(View.VISIBLE);

                startActivityes(null,0);
            }
        });

        sticker = findViewById(R.id.sticker);
        sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV2)).setColorFilter(ContextCompat.getColor(CollageMakerlActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt2)).setTextColor(getResources().getColor(R.color.btn_icon_color));

                hideControls();
                stickerRecycler.setVisibility(View.VISIBLE);

                startActivityes(null,0);
            }
        });

        mSpaceLayout = findViewById(R.id.spaceLayout);
        adjust = findViewById(R.id.adjust);
        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV1)).setColorFilter(ContextCompat.getColor(CollageMakerlActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt1)).setTextColor(getResources().getColor(R.color.btn_icon_color));

                hideControls();
                mSpaceLayout.setVisibility(View.VISIBLE);

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
        StickerAdapter adapter = new StickerAdapter(CollageMakerlActivity.this, emojies);
        stickerRecycler.setAdapter(adapter);

        bgColorRecycler = findViewById(R.id.bgColorRecycler);
        bgColorRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colors = getResources().getStringArray(R.array.color_array);
        ColorAdapter cadapter = new ColorAdapter(CollageMakerlActivity.this, colors);
        bgColorRecycler.setAdapter(cadapter);

        bgcolor = findViewById(R.id.bgcolor);
        bgcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV3)).setColorFilter(ContextCompat.getColor(CollageMakerlActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt3)).setTextColor(getResources().getColor(R.color.btn_icon_color));

                hideControls();
                bgColorRecycler.setVisibility(View.VISIBLE);

                startActivityes(null,0);
            }
        });

        textBtn = findViewById(R.id.textBtn);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnpressBtn();
                ((ImageView)findViewById(R.id.tabIV4)).setColorFilter(ContextCompat.getColor(CollageMakerlActivity.this, R.color.btn_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                ((TextView)findViewById(R.id.tabTxt4)).setTextColor(getResources().getColor(R.color.btn_icon_color));

                hideControls();
                textButtonClick();
            }
        });
    }

    void startActivityes(Intent intent, int requestCode) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(CollageMakerlActivity.this, intent, requestCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(CollageMakerlActivity.this, intent, requestCode);
        }
    }

    void setUnpressBtn(){
        ((ImageView)findViewById(R.id.tabIV)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView)findViewById(R.id.tabTxt)).setTextColor(Color.WHITE);

        ((ImageView)findViewById(R.id.tabIV1)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView)findViewById(R.id.tabTxt1)).setTextColor(Color.WHITE);

        ((ImageView)findViewById(R.id.tabIV2)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView)findViewById(R.id.tabTxt2)).setTextColor(Color.WHITE);

        ((ImageView)findViewById(R.id.tabIV3)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView)findViewById(R.id.tabTxt3)).setTextColor(Color.WHITE);

        ((ImageView)findViewById(R.id.tabIV4)).setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView)findViewById(R.id.tabTxt4)).setTextColor(Color.WHITE);
    }

    void hideControls() {
        templateLayout.setVisibility(View.GONE);
        bgColorRecycler.setVisibility(View.GONE);
        mSpaceLayout.setVisibility(View.GONE);
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
                entity.load(CollageMakerlActivity.this,
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

    public void setBGColor(String color) {
        recycleBackgroundImage();
        mBackgroundColor = Color.parseColor(color);
        mContainerLayout.setBackgroundColor(mBackgroundColor);
        hideControls();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("mSpace", mSpace);
        outState.putFloat("mCornerBar", mCorner);
        outState.putInt("mBackgroundColor", mBackgroundColor);
        outState.putParcelable("mBackgroundUri", mBackgroundUri);
        if (mFramePhotoLayout != null) {
            mFramePhotoLayout.saveInstanceState(outState);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_collage_maker;
    }

    int width = 0, height = 0;

    @Override
    public Bitmap createOutputImage() throws OutOfMemoryError {
        try {
            width = mContainerLayout.getWidth();
            height = mContainerLayout.getHeight();
            Log.e("width * height", width + " * " + height);

            Bitmap template = mFramePhotoLayout.createImage();//viewToBitmap(mContainerLayout, width, height);
            Bitmap result = Bitmap.createBitmap(template.getWidth(), template.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
                canvas.drawBitmap(mBackgroundImage, new Rect(0, 0, mBackgroundImage.getWidth(), mBackgroundImage.getHeight()),
                        new Rect(0, 0, result.getWidth(), result.getHeight()), paint);
            } else {
                canvas.drawColor(mBackgroundColor);
            }

            canvas.drawBitmap(template, 0, 0, paint);
            template.recycle();
            template = null;
            Bitmap stickers = mPhotoView.getImage(mOutputScale);
            canvas.drawBitmap(stickers, 0, 0, paint);
            stickers.recycle();
            stickers = null;
            System.gc();
            return result;
        } catch (OutOfMemoryError error) {
            throw error;
        }
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void buildLayout(TemplateItem item) {
        mFramePhotoLayout = new FramePhotoLayout(this, item.getPhotoItemList());
        mFramePhotoLayout.setQuickActionClickListener(this);
        if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
            if (Build.VERSION.SDK_INT >= 16)
                mContainerLayout.setBackground(new BitmapDrawable(getResources(), mBackgroundImage));
            else
                mContainerLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), mBackgroundImage));
        } else {
            mContainerLayout.setBackgroundColor(mBackgroundColor);
        }

        int viewWidth = mContainerLayout.getWidth();
        int viewHeight = mContainerLayout.getHeight();
        if (mLayoutRatio == RATIO_SQUARE) {
            if (viewWidth > viewHeight) {
                viewWidth = viewHeight;
            } else {
                viewHeight = viewWidth;
            }
        } else if (mLayoutRatio == RATIO_GOLDEN) {
            final double goldenRatio = 1.61803398875;
            if (viewWidth <= viewHeight) {
                if (viewWidth * goldenRatio >= viewHeight) {
                    viewWidth = (int) (viewHeight / goldenRatio);
                } else {
                    viewHeight = (int) (viewWidth * goldenRatio);
                }
            } else if (viewHeight <= viewWidth) {
                if (viewHeight * goldenRatio >= viewWidth) {
                    viewHeight = (int) (viewWidth / goldenRatio);
                } else {
                    viewWidth = (int) (viewHeight * goldenRatio);
                }
            }
        }
        mOutputScale = ImageUtils.calculateOutputScaleFactor(viewWidth, viewHeight);
        mFramePhotoLayout.build(viewWidth, viewHeight, mOutputScale, mSpace, mCorner);
        if (mSavedInstanceState != null) {
            mFramePhotoLayout.restoreInstanceState(mSavedInstanceState);
            mSavedInstanceState = null;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContainerLayout.removeAllViews();
        mContainerLayout.addView(mFramePhotoLayout, params);
        //add sticker view
        mContainerLayout.removeView(mPhotoView);
        mContainerLayout.addView(mPhotoView, params);
        //reset space and corner seek bars
        mSpaceBar.setProgress((int) (MAX_SPACE_PROGRESS * mSpace / MAX_SPACE));
        mCornerBar.setProgress((int) (MAX_CORNER_PROGRESS * mCorner / MAX_CORNER));
    }

    @Override
    public void onEditActionClick(com.mobiledevteam.qrtools.frame.FrameImageView v) {
        mSelectedFrameImageView = v;
        if (v.getImage() != null && v.getPhotoItem().imagePath != null && v.getPhotoItem().imagePath.length() > 0) {
            Uri uri = Uri.fromFile(new File(v.getPhotoItem().imagePath));
//            requestEditingImage(uri);
        }
    }


    @Override
    public void onChangeActionClick(FrameImageView v) {
        mSelectedFrameImageView = v;
//        requestPhoto();
        Intent mIntent = new Intent(this, CustomGalleryActivity.class);
        mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MAX_IMAGE, 1);
        mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(mIntent, CustomGalleryActivity.PICKER_REQUEST_CODE);
    }


    @Override
    protected void resultEditImage(Uri uri) {
        if (mSelectedFrameImageView != null) {
            mSelectedFrameImageView.setImagePath(FileUtils.getPath(this, uri));
        }
    }

    @Override
    protected void resultFromPhotoEditor(Uri image) {
        if (mSelectedFrameImageView != null) {
            mSelectedFrameImageView.setImagePath(FileUtils.getPath(this, image));
        }
    }

    private void recycleBackgroundImage() {
        if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
            mBackgroundImage.recycle();
            mBackgroundImage = null;
            System.gc();
        }
    }

    @Override
    protected void resultBackground(Uri uri) {
        recycleBackgroundImage();
        mBackgroundUri = uri;
        mBackgroundImage = ImageDecoder.decodeUriToBitmap(this, uri);
        if (Build.VERSION.SDK_INT >= 16)
            mContainerLayout.setBackground(new BitmapDrawable(getResources(), mBackgroundImage));
        else
            mContainerLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), mBackgroundImage));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK) {
            ArrayList<String> mSelectedImages = data.getStringArrayListExtra("result");
            if (mSelectedFrameImageView != null && mSelectedImages != null && !mSelectedImages.isEmpty()) {
                mSelectedFrameImageView.setImagePath(mSelectedImages.get(0));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void finish() {
        recycleBackgroundImage();
        if (mFramePhotoLayout != null) {
            mFramePhotoLayout.recycleImages();
        }
        super.finish();
    }


}
