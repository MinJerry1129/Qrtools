package com.kessi.photopipcollagemaker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kessi.photopipcollagemaker.utils.AdManager;
import com.kessi.photopipcollagemaker.utils.Utils;

import java.io.File;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView myImg, btnShare, btnDelete, btnBack;
    File file;
    String path;
    boolean isCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        myImg = findViewById(R.id.myImg);
        isCreation = getIntent().getExtras().getBoolean("isCreation");
        path = getIntent().getExtras().getString("path");
        file = new File(path);

        Glide.with(this).load(Uri.fromFile(file)).into(myImg);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        LinearLayout adContainer = findViewById(R.id.banner_container);

        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(ShareActivity.this);
            AdManager.adptiveBannerAd(ShareActivity.this, adContainer);
            AdManager.loadInterAd(ShareActivity.this);
        } else {
            //Fb banner Ads
            AdManager.fbAdaptiveBannerAd(ShareActivity.this, adContainer);
            AdManager.loadFbInterAd(ShareActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (isCreation) {
            super.onBackPressed();
        } else {
            Utils.gotoMain(ShareActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;

            case R.id.btnShare:
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(ShareActivity.this, null,0);
                } else {
                    AdManager.adCounter++;
                    AdManager.showFbInterAd(ShareActivity.this, null,0);
                }
                Utils.mShare(path, ShareActivity.this);
                break;

            case R.id.btnDelete:
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(ShareActivity.this, null,0);
                } else {
                    AdManager.adCounter++;
                    AdManager.showFbInterAd(ShareActivity.this, null,0);
                }
                mDelete(file);
                break;
        }
    }


    public void mDelete(final File mFile) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShareActivity.this);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are you Sure, You want to Delete this Image?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mFile.exists()) {
                    boolean del = mFile.delete();
                    onBackPressed();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}