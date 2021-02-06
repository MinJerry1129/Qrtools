package com.kessi.photopipcollagemaker.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.adapter.AlbumAdapter;
import com.kessi.photopipcollagemaker.adapter.ListAlbumAdapter;
import com.kessi.photopipcollagemaker.model.ImageModel;
import com.kessi.photopipcollagemaker.myinterface.IHandler;
import com.kessi.photopipcollagemaker.myinterface.OnAlbum;
import com.kessi.photopipcollagemaker.myinterface.OnListAlbum;
import com.kessi.photopipcollagemaker.utils.AdManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomGalleryActivity extends AppCompatActivity implements
        OnClickListener,
        OnAlbum, OnListAlbum {
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";
    public static final int PICKER_REQUEST_CODE = 1001;
    private final String TAG = "PickImageActivity";
    AlbumAdapter albumAdapter;
    ArrayList<ImageModel> dataAlbum = new ArrayList();
    ArrayList<ImageModel> dataListPhoto = new ArrayList();
    GridView gridViewAlbum;
    GridView gridViewListAlbum;
    HorizontalScrollView horizontalScrollView;
    LinearLayout layoutListItemSelect;
    int limitImageMax = 15;
    int limitImageMin = 2;
    ListAlbumAdapter listAlbumAdapter;
    ArrayList<ImageModel> listItemSelect = new ArrayList();
    int pWHBtnDelete;
    int pWHItemSelected;
    ArrayList<String> pathList = new ArrayList();
    AlertDialog sortDialog;
    TextView txtTotalImage;
    private Handler mHandler;
    private ProgressDialog pd;
    private int position = 0;
    private static final int READ_STORAGE_CODE = 1001;
    private static final int WRITE_STORAGE_CODE = 1002;

    ProgressBar progress;

    public static boolean isScrap = false;

    public void onBackButtoclick(View view) {
       onBackPressed();
    }


    private class GetItemAlbum extends AsyncTask<Void, Void, String> {
        private GetItemAlbum() {
        }

        protected String doInBackground(Void... params) {
            Cursor cursor = CustomGalleryActivity.this.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
            if (cursor != null) {
                int column_index_data = cursor.getColumnIndexOrThrow("_data");
                while (cursor.moveToNext()) {
                    String pathFile = cursor.getString(column_index_data);
                    File file = new File(pathFile);
                    if (file.exists()) {
                        boolean check = CustomGalleryActivity.this.checkFile(file);
                        if (!CustomGalleryActivity.this.Check(file.getParent(), CustomGalleryActivity.this.pathList) && check) {
                            CustomGalleryActivity.this.pathList.add(file.getParent());
                            CustomGalleryActivity.this.dataAlbum.add(new ImageModel(file.getParentFile().getName(), pathFile, file.getParent()));
                        }
                    }
                }
                cursor.close();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            CustomGalleryActivity.this.gridViewAlbum.setAdapter(CustomGalleryActivity.this.albumAdapter);
            progress.setVisibility(View.GONE);
        }

        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetItemListAlbum extends AsyncTask<Void, Void, String> {
        String pathAlbum;

        GetItemListAlbum(String pathAlbum) {
            this.pathAlbum = pathAlbum;
        }

        protected String doInBackground(Void... params) {
            File file = new File(this.pathAlbum);
            if (file.isDirectory()) {
                for (File fileTmp : file.listFiles()) {
                    if (fileTmp.exists()) {
                        boolean check = CustomGalleryActivity.this.checkFile(fileTmp);
                        if (!fileTmp.isDirectory() && check) {
                            CustomGalleryActivity.this.dataListPhoto.add(new ImageModel(fileTmp.getName(), fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath()));
                            publishProgress(new Void[0]);
                        }
                    }
                }
            }
            return "";
        }

        protected void onPostExecute(String result) {
            try {
                Collections.sort(CustomGalleryActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                    @Override
                    public int compare(ImageModel item, ImageModel t1) {
                        File fileI = new File(item.getPathFolder());
                        File fileJ = new File(t1.getPathFolder());
                        if (fileI.lastModified() > fileJ.lastModified()) {
                            return -1;
                        }
                        if (fileI.lastModified() < fileJ.lastModified()) {
                            return 1;
                        }
                        return 0;
                    }
                });
            } catch (Exception e) {
            }
            CustomGalleryActivity.this.listAlbumAdapter.notifyDataSetChanged();
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piclist_activity_album);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = findViewById(R.id.progress);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.limitImageMax = bundle.getInt(KEY_LIMIT_MAX_IMAGE, 15);
            this.limitImageMin = bundle.getInt(KEY_LIMIT_MIN_IMAGE, 0);
            if (this.limitImageMin > this.limitImageMax) {
                finish();
            }
            if (this.limitImageMin < 1) {
                finish();
            }
            Log.e("PickImageActivity", "limitImageMin = " + this.limitImageMin);
            Log.e("PickImageActivity", "limitImageMax = " + this.limitImageMax);
        }
        this.pWHItemSelected = (((int) ((((float) getDisplayInfo(this).heightPixels) / 100.0f) * 25.0f)) / 100) * 90;
        this.pWHBtnDelete = (this.pWHItemSelected / 100) * 25;
        //  this.txtTitle = (TextView) findViewById(R.id.txtTitle);

//        getSupportActionBar().hide();


        this.gridViewListAlbum = (GridView) findViewById(R.id.gridViewListAlbum);
        this.txtTotalImage = (TextView) findViewById(R.id.txtTotalImage);

        ((ImageView) findViewById(R.id.btnDone)).setOnClickListener(this);
        this.layoutListItemSelect = (LinearLayout) findViewById(R.id.layoutListItemSelect);
        this.horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        this.horizontalScrollView.getLayoutParams().height = this.pWHItemSelected;
        this.gridViewAlbum = (GridView) findViewById(R.id.gridViewAlbum);

        pd = new ProgressDialog(CustomGalleryActivity.this);
        pd.setIndeterminate(true);
        pd.setMessage("Loading...");

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };

        try {
            Collections.sort(this.dataAlbum, new Comparator<ImageModel>() {
                @Override
                public int compare(ImageModel lhs, ImageModel rhs) {
                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.albumAdapter = new AlbumAdapter(this, R.layout.piclist_row_album, this.dataAlbum);
        this.albumAdapter.setOnItem(this);

        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new GetItemAlbum().execute(new Void[0]);
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_CODE);
        }
        updateTxtTotalImage();

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(CustomGalleryActivity.this);
            AdManager.loadBannerAd(CustomGalleryActivity.this, adContainer);
        } else {
            //Fb banner Ads
            AdManager.fbBannerAd(CustomGalleryActivity.this, adContainer);
        }
    }

    @Override
    protected void onDestroy() {
        AdManager.destroyFbAd();
        super.onDestroy();
    }

    private boolean isPermissionGranted(String permission) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, permission);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //Requesting permission
    private void requestPermission(String permission, int code) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{permission}, code);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == READ_STORAGE_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new GetItemAlbum().execute(new Void[0]);
            } else {
                CustomGalleryActivity.this.finish();
            }
        } else if (requestCode == WRITE_STORAGE_CODE) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

            }
        }
    }

    private boolean Check(String a, ArrayList<String> list) {
        if (!list.isEmpty() && list.contains(a)) {
            return true;
        }
        return false;
    }

    public void showDialogSortAlbum() {
        CharSequence[] items = getResources().getStringArray(R.array.array_sort_value);
        final Builder builder = new Builder(this);
        builder.setTitle("dialog_sort_by_album");
        Log.e("TAG", "showDialogSortAlbum");
        builder.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                switch (i) {
                    case 0:
                        position = i;
                        Collections.sort(CustomGalleryActivity.this.dataAlbum, new Comparator<ImageModel>() {
                            @Override
                            public int compare(ImageModel lhs, ImageModel rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });
                        CustomGalleryActivity.this.refreshGridViewAlbum();
                        Log.e("TAG", "showDialogSortAlbum by NAME");
                        break;
                    case 1:
                        position = i;
                        doinBackground();
                        Log.e("TAG", "showDialogSortAlbum by Size");
                        break;
                    case 2:
                        position = i;
                        Collections.sort(CustomGalleryActivity.this.dataAlbum, new Comparator<ImageModel>() {
                            @Override
                            public int compare(ImageModel lhs, ImageModel rhs) {
                                File fileI = new File(lhs.getPathFolder());
                                File fileJ = new File(rhs.getPathFolder());
                                long totalSizeFileI = CustomGalleryActivity.getFolderSize(fileI);
                                long totalSizeFileJ = CustomGalleryActivity.getFolderSize(fileJ);
                                if (totalSizeFileI > totalSizeFileJ) {
                                    return -1;
                                }
                                if (totalSizeFileI < totalSizeFileJ) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                        CustomGalleryActivity.this.refreshGridViewAlbum();
                        Log.e("TAG", "showDialogSortAlbum by Date");
                        break;
                }
                CustomGalleryActivity.this.sortDialog.dismiss();
            }
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewAlbum() {
        this.albumAdapter = new AlbumAdapter(this, R.layout.piclist_row_album, this.dataAlbum);
        this.albumAdapter.setOnItem(this);
        this.gridViewAlbum.setAdapter(this.albumAdapter);
        this.gridViewAlbum.setVisibility(View.GONE);
        this.gridViewAlbum.setVisibility(View.VISIBLE);
    }

    public void showDialogSortListAlbum() {
        CharSequence[] items = getResources().getStringArray(R.array.array_sort_value);
        Builder builder = new Builder(this);
        builder.setTitle("sort_by_photo");
        builder.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        position = i;
                        doinBackgroundPhoto(i);
                    case 1:
                        position = i;
                        doinBackgroundPhoto(i);
                    case 2:
                        position = i;
                        doinBackgroundPhoto(i);
                }
                CustomGalleryActivity.this.sortDialog.dismiss();
            }
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewListAlbum() {
        this.listAlbumAdapter = new ListAlbumAdapter(this, R.layout.piclist_row_list_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewListAlbum.setAdapter(this.listAlbumAdapter);
        this.gridViewListAlbum.setVisibility(View.GONE);
        this.gridViewListAlbum.setVisibility(View.VISIBLE);
    }

    public static long getFolderSize(File directory) {
        long length = 0;
        if (directory == null) {
            return 0;
        }
        if (!directory.exists()) {
            return 0;
        }
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    boolean isCheck = false;
                    for (int k = 0; k < ImgConstants.FORMAT_IMAGE.size(); k++) {
                        if (file.getName().endsWith((String) ImgConstants.FORMAT_IMAGE.get(k))) {
                            isCheck = true;
                            break;
                        }
                    }
                    if (isCheck) {
                        length++;
                    }
                }
            }
        }
        return length;
    }


    void addItemSelect(final ImageModel item) {
        item.setId(this.listItemSelect.size());
        this.listItemSelect.add(item);
        updateTxtTotalImage();
        final View viewItemSelected = View.inflate(this, R.layout.piclist_item_selected, null);
        ImageView imageItem = (ImageView) viewItemSelected.findViewById(R.id.imageItem);
        ImageView btnDelete = (ImageView) viewItemSelected.findViewById(R.id.btnDelete);

//        imageItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        btnDelete.getLayoutParams().width = this.pWHBtnDelete;
//        btnDelete.getLayoutParams().height = this.pWHBtnDelete;
        Glide.with((Activity) this).load(item.getPathFile()).placeholder(R.drawable.piclist_icon_default).into(imageItem);


        btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CustomGalleryActivity.this.layoutListItemSelect.removeView(viewItemSelected);
                CustomGalleryActivity.this.listItemSelect.remove(item);
                CustomGalleryActivity.this.updateTxtTotalImage();
            }
        });

        CustomGalleryActivity.this.layoutListItemSelect.addView(viewItemSelected);
        viewItemSelected.startAnimation(AnimationUtils.loadAnimation(CustomGalleryActivity.this, R.anim.abc_fade_in));
        CustomGalleryActivity.this.sendScroll();

    }

    void updateTxtTotalImage() {
        this.txtTotalImage.setText(String.format(getResources().getString(R.string.text_images), new Object[]{Integer.valueOf(this.listItemSelect.size())}));
    }

    private void sendScroll() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CustomGalleryActivity.this.horizontalScrollView.fullScroll(66);
                    }
                });
            }
        }).start();
    }

    void showListAlbum(String pathAlbum) {
//        getSupportActionBar().setTitle(new File(pathAlbum).getName());
        this.listAlbumAdapter = new ListAlbumAdapter(this, R.layout.piclist_row_list_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewListAlbum.setAdapter(this.listAlbumAdapter);
        this.gridViewListAlbum.setVisibility(View.VISIBLE);
        new GetItemListAlbum(pathAlbum).execute(new Void[0]);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnDone) {
            ArrayList<String> listString = getListString(this.listItemSelect);
            if (isScrap){
                if (listString.size() > 0) {
                    done(listString);
                } else {
                    Toast.makeText(this, "Please select at least " + limitImageMin + " images", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (listString.size() == limitImageMax) {
                    done(listString);
                } else {
                    Toast.makeText(this, "Please select at least " + limitImageMax + " images", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void done(ArrayList<String> listString) {
        Intent mIntent = new Intent();
        setResult(Activity.RESULT_OK, mIntent);
        mIntent.putStringArrayListExtra("result", listString);
//        mIntent.putStringArrayListExtra("result1", listString);
//        mIntent.putStringArrayListExtra("result", listString);
//        startActivity(mIntent);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pick_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.btnSort) {
            if (this.gridViewListAlbum.getVisibility() == View.GONE) {
                Log.d("tag", "1");
                showDialogSortAlbum();
            } else {
                showDialogSortListAlbum();
                Log.d("tag", "2");
            }
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    ArrayList<String> getListString(ArrayList<ImageModel> listItemSelect) {
        ArrayList<String> listString = new ArrayList();
        for (int i = 0; i < listItemSelect.size(); i++) {
            listString.add(((ImageModel) listItemSelect.get(i)).getPathFile());
        }
        return listString;
    }

    private boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;
        for (int k = 0; k < ImgConstants.FORMAT_IMAGE.size(); k++) {
            if (name.endsWith((String) ImgConstants.FORMAT_IMAGE.get(k))) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    public void onBackPressed() {
        if (this.gridViewListAlbum.getVisibility() == View.VISIBLE) {
            this.dataListPhoto.clear();
            this.listAlbumAdapter.notifyDataSetChanged();
            this.gridViewListAlbum.setVisibility(View.GONE);
            //   this.txtTitle.setText(getResources().getString(R.string.text_title_activity_album));

//            getSupportActionBar().setTitle("album");
            return;
        }
        super.onBackPressed();
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    public void handlerDoWork(IHandler mIHandler) {
        mHandler.sendMessage(this.mHandler.obtainMessage(0, mIHandler));
    }

    private void doinBackgroundPhoto(final int position) {

        new AsyncTask<String, String, Void>() {
            @Override
            protected void onPreExecute() {
                pd.show();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... strings) {

                Log.d("tag", "Postion" + position);

                if (position == 0) {
                    try {
                        Collections.sort(CustomGalleryActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                            @Override
                            public int compare(ImageModel lhs, ImageModel rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });

                    } catch (Exception e) {

                    }
                } else if (position == 1) {
                    Collections.sort(CustomGalleryActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                        @Override
                        public int compare(ImageModel lhs, ImageModel rhs) {
                            File fileI = new File(lhs.getPathFolder());
                            File fileJ = new File(rhs.getPathFolder());
                            long totalSizeFileI = CustomGalleryActivity.getFolderSize(fileI);
                            long totalSizeFileJ = CustomGalleryActivity.getFolderSize(fileJ);
                            if (totalSizeFileI > totalSizeFileJ) {
                                return -1;
                            }
                            if (totalSizeFileI < totalSizeFileJ) {
                                return 1;
                            }
                            return 0;
                        }
                    });
                } else if (position == 2) {
                    try {
                        Collections.sort(CustomGalleryActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                            @Override
                            public int compare(ImageModel lhs, ImageModel rhs) {
                                File fileI = new File(lhs.getPathFolder());
                                File fileJ = new File(rhs.getPathFolder());
                                if (fileI.lastModified() > fileJ.lastModified()) {
                                    return -1;
                                }
                                if (fileI.lastModified() < fileJ.lastModified()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                    } catch (Exception e3) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                CustomGalleryActivity.this.refreshGridViewListAlbum();
                pd.dismiss();
            }
        }.execute();
    }

    private void doinBackground() {

        new AsyncTask<String, String, Void>() {
            @Override
            protected void onPreExecute() {
                pd.show();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... strings) {
                Collections.sort(CustomGalleryActivity.this.dataAlbum, new Comparator<ImageModel>() {
                    @Override
                    public int compare(ImageModel item, ImageModel t1) {
                        File fileI = new File(item.getPathFolder());
                        File fileJ = new File(t1.getPathFolder());
                        if (fileI.lastModified() > fileJ.lastModified()) {
                            return -1;
                        }
                        if (fileI.lastModified() < fileJ.lastModified()) {
                            return 1;
                        }
                        return 0;
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                CustomGalleryActivity.this.refreshGridViewAlbum();
                pd.dismiss();
            }
        }.execute();
    }

    public void OnItemAlbumClick(int position) {
        showListAlbum(((ImageModel) this.dataAlbum.get(position)).getPathFolder());
    }

    public void OnItemListAlbumClick(ImageModel item) {
        if (this.listItemSelect.size() < limitImageMax) {
            addItemSelect(item);
        } else {
            Toast.makeText(this, "Limit " + limitImageMax + " images", Toast.LENGTH_SHORT).show();
        }
    }

}
