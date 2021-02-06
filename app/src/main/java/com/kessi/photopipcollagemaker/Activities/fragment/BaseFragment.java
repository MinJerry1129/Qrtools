package com.kessi.photopipcollagemaker.Activities.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.gallery.CustomGalleryActivity;
import com.kessi.photopipcollagemaker.Activities.AddTextItemActivity;


import java.io.File;
import java.util.ArrayList;


public class BaseFragment extends Fragment {
    protected static final int REQUEST_ADD_TEXT_ITEM = 1000;
    protected static final int REQUEST_PHOTO_EDITOR_CODE = 999;
    protected static final int PICK_IMAGE_REQUEST_CODE = 998;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 997;
    protected static final int PICK_STICKER_REQUEST_CODE = 996;
    protected static final int PICK_BACKGROUND_REQUEST_CODE = 995;
    protected static final int REQUEST_EDIT_IMAGE = 994;
    protected static final int PICK_MULTIPLE_IMAGE_REQUEST_CODE = 1001;
    protected static final int REQUEST_EDIT_TEXT_ITEM = 992;

    public static final int MAX_NEEDED_PHOTOS = 20;
    protected static final String CAPTURE_TITLE = "capture.jpg";
    protected static final int BACKGROUND_ITEM = 0;
    protected static final int STICKER_ITEM = 1;
    protected static final int NORMAL_IMAGE_ITEM = 2;
    protected Activity mActivity;
    private String mTitle = null;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        setTitle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle();
    }

    protected void setTitle() {
        if (this instanceof ScrapBookFragment) {
            mTitle = getString(R.string.collage);
        }
        setTitle(mTitle);
    }

    public void setTitle(String title) {
        mTitle = title;
        if (already()) {
            mActivity.setTitle(title);
            if (mActivity instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) mActivity;
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(mTitle);
                }
            }
        }
    }

    public void setTitle(int res) {
        mTitle = getString(res);
        setTitle(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    private void startPhotoEditor(Uri imageUri, boolean capturedFromCamera) {
        if (!already()) {
            return;
        }

    }


    public void pickMultipleImageFromGallery() {
        Intent mIntent = new Intent(getActivity(), CustomGalleryActivity.class);
        mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MAX_IMAGE, MAX_NEEDED_PHOTOS);
        mIntent.putExtra(CustomGalleryActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(mIntent, CustomGalleryActivity.PICKER_REQUEST_CODE);
    }



    public void addTextItem() {
        if (!already()) {
            return;
        }

        Intent intent = new Intent(getActivity(), AddTextItemActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TEXT_ITEM);
    }




    protected Uri getImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory()
                + "/DCIM", CAPTURE_TITLE);
        Uri imgUri = Uri.fromFile(file);

        return imgUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PHOTO_EDITOR_CODE:
                    // output image path
                    Uri uri = data.getData();
                    resultFromPhotoEditor(uri);
                    break;
                case PICK_IMAGE_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                        startPhotoEditor(uri, false);
                    }
                    break;
                case CAPTURE_IMAGE_REQUEST_CODE:
                    uri = getImageUri();
                    if (uri != null) {
                        startPhotoEditor(uri, true);
                    }
                    break;
                case PICK_BACKGROUND_REQUEST_CODE:

                    break;
                case REQUEST_EDIT_IMAGE:
                    uri = data.getData();
//                    resultEditImage(uri);
                    break;
                case PICK_STICKER_REQUEST_CODE:
                    break;
                case PICK_MULTIPLE_IMAGE_REQUEST_CODE:
                    ArrayList<String> allPaths = data.getStringArrayListExtra("result");
                    if (allPaths != null && allPaths.size() > 0) {
                        final int len = allPaths.size();
                        Uri[] result = new Uri[len];
                        for (int idx = 0; idx < len; idx++) {
                            uri = Uri.fromFile(new File(allPaths.get(idx)));
                            result[idx] = uri;
                        }

                        resultPickMultipleImages(result);
                    }
                    break;
                case REQUEST_EDIT_TEXT_ITEM:
                    break;
                case REQUEST_ADD_TEXT_ITEM:
                    String text = data.getStringExtra(AddTextItemActivity.EXTRA_TEXT_CONTENT);
                    String fontPath = data.getStringExtra(AddTextItemActivity.EXTRA_TEXT_FONT);
                    int color = data.getIntExtra(AddTextItemActivity.EXTRA_TEXT_COLOR, Color.BLACK);
                    resultAddTextItem(text, color, fontPath);
                    break;
            }

        }
    }


    protected void resultFromPhotoEditor(Uri image) {

    }

    protected void resultSticker(Uri uri) {

    }

    protected void resultStickers(Uri[] uri) {

    }

    protected void resultBackground(Uri uri) {

    }

    protected void resultEditImage(Uri uri) {

    }

    protected void resultAddTextItem(String text, int color, String fontPath) {

    }

    public void resultPickMultipleImages(Uri[] uri) {

    }

    public boolean already() {
        return (isAdded() && getActivity() != null);
    }

}
