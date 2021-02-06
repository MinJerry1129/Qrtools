package com.mobiledevteam.qrtools.shape;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiledevteam.qrtools.R;
import com.mobiledevteam.qrtools.ShareActivity;
import com.mobiledevteam.qrtools.utils.AdManager;
import com.mobiledevteam.qrtools.utils.DateTimeUtils;
import com.mobiledevteam.qrtools.utils.ImageUtils;
import com.mobiledevteam.qrtools.utils.PhotoUtils;
import com.mobiledevteam.qrtools.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;


public class BodyShapeEditor extends AppCompatActivity implements OnClickListener {

    ImageView mainIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_shape);

        this.mainIV = (ImageView) findViewById(R.id.imageView1);
        Bitmap bitmap = Utils.mBitmap;
        Matrix matrix = new Matrix();

        matrix.setRectToRect(
                new RectF(0.0f, 0.0f, (float) bitmap.getWidth(),
                        (float) bitmap.getHeight()),
                new RectF(0.0f, 0.0f, (float) bitmap.getWidth(),
                        (float) bitmap.getHeight()), ScaleToFit.CENTER);

        this.mainIV.setImageBitmap(Utils.mBitmap);
        findViewById(R.id.btnSlim).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.waist).setOnClickListener(this);
        findViewById(R.id.hip).setOnClickListener(this);
        findViewById(R.id.chest).setOnClickListener(this);
        findViewById(R.id.btnFace).setOnClickListener(this);

        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(BodyShapeEditor.this);
            AdManager.loadInterAd(BodyShapeEditor.this);
        } else {
            //Fb banner Ads
            AdManager.loadFbInterAd(BodyShapeEditor.this);
        }
    }

    public void saveImage() {
        final Bitmap image = mainIVDraw(this.mainIV, this.mainIV.getWidth(), this.mainIV.getHeight());
        AsyncTask<Void, Void, File> task = new AsyncTask<Void, Void, File>() {
            Dialog dialog;
            String errMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(BodyShapeEditor.this, getString(R.string.app_name), getString(R.string.creating));
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
                    PhotoUtils.addImageToGallery(photoFile.getAbsolutePath(), BodyShapeEditor.this);
                    Utils.mediaScanner(BodyShapeEditor.this, ImageUtils.OUTPUT_COLLAGE_FOLDER+"/", fileName);
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
                    Intent intent = new Intent(BodyShapeEditor.this, ShareActivity.class);
                    intent.putExtra("path", file.getAbsolutePath());
                    intent.putExtra("isCreation", false);

                    if (!AdManager.isloadFbAd) {
                        AdManager.adCounter = 6;
                        AdManager.showInterAd(BodyShapeEditor.this, intent,0);
                    } else {
                        AdManager.adCounter = 6;
                        AdManager.showFbInterAd(BodyShapeEditor.this, intent,0);
                    }
                    Toast.makeText(BodyShapeEditor.this, "Saved Successfully....", Toast.LENGTH_LONG).show();
                } else if (errMsg != null) {
                    Toast.makeText(BodyShapeEditor.this, errMsg, Toast.LENGTH_LONG).show();
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                return;
            case R.id.btnSave:
                saveImage();
                return;
            case R.id.btnSlim:
                startSlim();
                return;

            case R.id.waist:
                startActivityes(new Intent(this, WaistActivity.class), 10);
                return;

            case R.id.hip:
                startActivityes(new Intent(this, HipActivity.class), 21);
                return;

            case R.id.chest:
                startActivityes(new Intent(this, ChestActivity.class), 22);
                return;

            case R.id.btnFace:
                startActivityes(new Intent(this, FaceActivity.class),23);
                return;

            default:
                return;
        }
    }

    void startActivityes(Intent intent, int requestCode) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(BodyShapeEditor.this, intent, requestCode);
        } else {
            AdManager.adCounter++;
            AdManager.showFbInterAd(BodyShapeEditor.this, intent, requestCode);
        }
    }

    private void startSlim() {

        this.mainIV.setDrawingCacheEnabled(true);
        Utils.mBitmap = Bitmap.createBitmap(this.mainIV
                .getDrawingCache());
        this.mainIV.setDrawingCacheEnabled(false);
        startActivityes(new Intent(getApplicationContext(),
                SlimActivity.class), 456);
    }


    public static Bitmap mainIVDraw(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (i == 456 || i == 123 || i == 213 || i == 10 || i == 21 || i == 22 || i == 23) {
                this.mainIV.setImageBitmap(Utils.mBitmap);
            }
        }
        super.onActivityResult(i, i2, intent);
    }
}
