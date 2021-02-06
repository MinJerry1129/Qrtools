package com.kessi.photopipcollagemaker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;



public abstract class BasePhotoActivity extends BaseFragmentActivity {
    protected static final int REQUEST_ADD_TEXT_ITEM = 10000;
    protected static final int REQUEST_PHOTO_EDITOR_CODE = 9990;
    protected static final int PICK_IMAGE_REQUEST_CODE = 9980;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 9970;
    protected static final int PICK_STICKER_REQUEST_CODE = 9960;
    protected static final int PICK_BACKGROUND_REQUEST_CODE = 9950;
    protected static final int REQUEST_EDIT_IMAGE = 9940;
    protected static final int PICK_MULTIPLE_IMAGE_REQUEST_CODE = 9930;
    protected static final int REQUEST_EDIT_TEXT_ITEM = 9920;

    private Uri mCapturedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCapturedImageUri = savedInstanceState.getParcelable("mCapturedImageUri");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("mCapturedImageUri", mCapturedImageUri);
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
                        resultFromPhotoEditor(uri);
                    }
                    break;
                case CAPTURE_IMAGE_REQUEST_CODE:
                    if (mCapturedImageUri != null) {
                        startPhotoEditor(mCapturedImageUri, true);
                    }
                    break;
                case PICK_BACKGROUND_REQUEST_CODE:
                    ArrayList<String> allPaths = data.getStringArrayListExtra("result");;
                    if (allPaths != null && allPaths.size() > 0) {
                        uri = Uri.fromFile(new File(allPaths.get(0)));
                        resultBackground(uri);
                    }
                    break;
                case REQUEST_EDIT_IMAGE:
                    uri = data.getData();
                    resultEditImage(uri);
                    break;
                case PICK_STICKER_REQUEST_CODE:

                    break;
                case PICK_MULTIPLE_IMAGE_REQUEST_CODE:
                    allPaths = data.getStringArrayListExtra("result");
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

    private void startPhotoEditor(Uri imageUri, boolean capturedFromCamera) {

    }

    public void addTextItem() {
        Intent intent = new Intent(this, AddTextItemActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TEXT_ITEM);
    }


    public void requestEditingImage(Uri imageUri) {
    }

    protected void resultFromPhotoEditor(Uri image) {

    }

    protected void resultSticker(Uri uri) {

    }

    protected void resultBackground(Uri uri) {

    }

    protected void resultEditImage(Uri uri) {

    }

    protected void resultAddTextItem(String text, int color, String fontPath) {

    }

    protected void resultEditTextItem(String text, int color, String fontPath) {

    }

    public void resultPickMultipleImages(Uri[] uri) {

    }

    public void resultStickers(Uri[] uri) {

    }
}
