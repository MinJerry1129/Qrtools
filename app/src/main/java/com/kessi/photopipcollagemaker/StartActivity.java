package com.kessi.photopipcollagemaker;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.kessi.photopipcollagemaker.gallery.CustomGalleryActivity;
import com.kessi.photopipcollagemaker.Activities.ScrapBookActivity;
import com.kessi.photopipcollagemaker.Activities.ThumbListActivity;
import com.kessi.photopipcollagemaker.shape.BodyShapeEditor;
import com.kessi.photopipcollagemaker.utils.AdManager;
import com.kessi.photopipcollagemaker.utils.DateTimeUtils;
import com.kessi.photopipcollagemaker.utils.ImageUtils;
import com.kessi.photopipcollagemaker.utils.Utils;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    int perRequest = 1;

    String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    ImageView menuBtn;
    ImageView clgMakerBtn;
    ImageView pipMakerBtn;
    ImageView scrapBtn;
    ImageView albumBtn;
    ImageView mainBG;
    ImageView pipImg;
    ImageView photoEditBtn;
    ImageView shapeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mainBG = findViewById(R.id.mainBG);
        Glide.with(StartActivity.this)
                .load(R.drawable.bg_main)
                .into(mainBG);

        pipImg = findViewById(R.id.pipImg);
        Glide.with(StartActivity.this)
                .load(R.drawable.banner)
                .into(pipImg);

        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(this);

        clgMakerBtn = findViewById(R.id.clgMakerBtn);
        clgMakerBtn.setOnClickListener(this);

        photoEditBtn = findViewById(R.id.photoEditBtn);
        photoEditBtn.setOnClickListener(this);

        pipMakerBtn = findViewById(R.id.pipMakerBtn);
        pipMakerBtn.setOnClickListener(this);

        scrapBtn = findViewById(R.id.scrapBtn);
        scrapBtn.setOnClickListener(this);

        albumBtn = findViewById(R.id.albumBtn);
        albumBtn.setOnClickListener(this);

        shapeBtn = findViewById(R.id.shapeBtn);
        shapeBtn.setOnClickListener(this);

        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(StartActivity.this);
            AdManager.loadInterAd(StartActivity.this);
        } else {
            //Fb banner Ads
            AdManager.loadFbInterAd(StartActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuBtn:
                showMenu();
                break;

            case R.id.clgMakerBtn:
                collageMaker();
                break;

            case R.id.photoEditBtn:
                photoEditor();
                break;

            case R.id.pipMakerBtn:
                pipMaker();
                break;

            case R.id.shapeBtn:
                shapeEditor();
                break;

            case R.id.scrapBtn:
                scrapBook();
                break;

            case R.id.albumBtn:
                gotoCreation();
                break;

            default:
                break;
        }
    }

    PopupWindow mPopupWindow;

    void showMenu()  {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.custom_popup, null);

        mPopupWindow = new PopupWindow(
                customView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }


        TextView rateTxt = customView.findViewById(R.id.rateTxt);
        rateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        TextView shareTxt = customView.findViewById(R.id.shareTxt);
        shareTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                Intent myapp = new Intent(Intent.ACTION_SEND);
                myapp.setType("text/plain");
                myapp.putExtra(Intent.EXTRA_TEXT, "Click here and check out this amazing app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
                startActivity(myapp);
            }
        });

        TextView moreTxt = customView.findViewById(R.id.moreTxt);
        moreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=7081479513420377164&hl=en")));
            }
        });

        TextView privacyTxt = customView.findViewById(R.id.privacyTxt);
        privacyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                startActivityes(new Intent(StartActivity.this, PrivacyActivity.class),0);
            }
        });

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(menuBtn, 0, 0);


    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(StartActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public void photoEditor(){
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(StartActivity.this, permissions, perRequest);
        } else {
            CustomGalleryActivity.isScrap = false;
            Intent mIntent = new Intent(this, CustomGalleryActivity.class);
            mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MAX_IMAGE, 1);
            mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MIN_IMAGE, 1);
            startActivityes(mIntent, CustomGalleryActivity.PICKER_REQUEST_CODE);
        }
    }

    private int ACTION_REQUEST_EDITIMAGE = 9;
    String fileName;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CustomGalleryActivity.PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                ArrayList<String> mSelectedImages = data.getStringArrayListExtra("result");
                fileName = DateTimeUtils.getCurrentDateTime().replaceAll(":", "-").concat(".png");
                File collageFolder = new File(ImageUtils.OUTPUT_COLLAGE_FOLDER);
                if (!collageFolder.exists()) {
                    collageFolder.mkdirs();
                }
                File outputFile = new File(collageFolder, fileName);

                EditImageActivity.start(this,mSelectedImages.get(0),outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if (requestCode == ACTION_REQUEST_EDITIMAGE && resultCode == RESULT_OK){
            handleEditorImage(data);
        }else if (requestCode == shapeRequest && resultCode == RESULT_OK){
            try {
                ArrayList<String> mSelectedImages = data.getStringArrayListExtra("result");
                loadImage(mSelectedImages.get(0));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    LoadImageTask mLoadImageTask;
    int imageWidth, imageHeight;
    public void loadImage(String filepath) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 2;
        imageHeight = metrics.heightPixels / 2;
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(filepath);
    }


    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        Dialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(StartActivity.this, getString(R.string.app_name), "Loading...");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth,
                    imageHeight);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            dialog.dismiss();
            Utils.mBitmap = result;
            startActivityes(new Intent(StartActivity.this, BodyShapeEditor.class),0);

        }
    }

    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        Utils.mediaScanner(StartActivity.this, ImageUtils.OUTPUT_COLLAGE_FOLDER+"/", fileName);
        Intent intent = new Intent(StartActivity.this, ShareActivity.class);
        intent.putExtra("path", newFilePath);
        intent.putExtra("isCreation", false);
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter = 6;
            AdManager.showInterAd(StartActivity.this, intent,0);
        } else {
            AdManager.adCounter = 6;
            AdManager.showFbInterAd(StartActivity.this, intent,0);
        }
        Toast.makeText(StartActivity.this, "Saved Successfully...", Toast.LENGTH_LONG).show();
    }

    public void collageMaker() {
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(StartActivity.this, permissions, perRequest);
        } else {
            CustomGalleryActivity.isScrap = false;
            Intent intent = new Intent(StartActivity.this, ThumbListActivity.class);
            intent.putExtra(ThumbListActivity.EXTRA_IS_FRAME_IMAGE, true);
            startActivityes(intent,0);
        }
    }

    public void pipMaker() {
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(StartActivity.this, permissions, perRequest);
        } else {
            CustomGalleryActivity.isScrap = false;
            Intent intent = new Intent(StartActivity.this, ThumbListActivity.class);
            intent.putExtra(ScrapBookActivity.EXTRA_CREATED_METHOD_TYPE, ScrapBookActivity.FRAME_TYPE);
            startActivityes(intent,0);
        }
    }

    int shapeRequest = 101;
    public void shapeEditor() {
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(StartActivity.this, permissions, perRequest);
        } else {
            CustomGalleryActivity.isScrap = false;
            Intent mIntent = new Intent(this, CustomGalleryActivity.class);
            mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MAX_IMAGE, 1);
            mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MIN_IMAGE, 1);
            startActivityes(mIntent, shapeRequest);
        }
    }

    public void scrapBook() {
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(StartActivity.this, permissions, perRequest);
        } else {
            CustomGalleryActivity.isScrap = true;
            Intent intent = new Intent(StartActivity.this, ScrapBookActivity.class);
            intent.putExtra(ScrapBookActivity.EXTRA_CREATED_METHOD_TYPE, ScrapBookActivity.PHOTO_TYPE);
            startActivityes(intent,0);
        }
    }

    public void gotoCreation() {
        if (hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(StartActivity.this, permissions, perRequest);
        } else {
            CustomGalleryActivity.isScrap = false;
            Intent intent = new Intent(StartActivity.this, CreationActivity.class);
            startActivityes(intent,0);
        }
    }

    void startActivityes(Intent intent, int requestCode) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(StartActivity.this, intent, requestCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(StartActivity.this, intent, requestCode);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000L);
    }

}
