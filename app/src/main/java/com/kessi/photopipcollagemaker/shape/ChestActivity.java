package com.kessi.photopipcollagemaker.shape;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.utils.Utils;


public class ChestActivity extends AppCompatActivity implements OnClickListener {

    FrameLayout frameLayout;
    ImageView imageView;

    LinearLayout start;

    private float pw;
    private float px;
    private float py;
    private float ratio;
    private float ph;

    SeekBar seekBar1;
    private OnSeekBarChangeListener seekBarChangeListener = new SeekChange();


    ViewOval breastView1, breastView2;

    Bitmap mBitmap;

    Bitmap saveBitmap;
    ImageView btnApply, btnBack;

    TextView tvTitle;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chest);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        this.breastView1 = findViewById(R.id.ovalView1);
        this.breastView2 = findViewById(R.id.ovalView2);

        mBitmap = Utils.mBitmap;

        this.breastView1.imageView = this.imageView;
        this.breastView2.imageView = this.imageView;
        imageView.setImageBitmap(mBitmap);
        getWaistViewHeight(breastView1);
        getWaistViewHeight(breastView2);

        start = findViewById(R.id.btnStart);
        start.setOnClickListener(this);


        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar1.setMax(15);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnApply = (ImageView) findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.mBitmap = saveBitmap;
                setResult(-1);
                finish();
            }
        });
    }

    class SeekChange implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            tvTitle.setText("Please wait....");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            createFilter(seekBar.getProgress());
            if (btnApply.getVisibility() == View.GONE) {
                btnApply.setVisibility(View.VISIBLE);
            }
        }

    }

    public void createFilter(int pro) {


        ExtendBitmap s = new ExtendBitmap();
        Bitmap b = s.extendBitmapPixels(mBitmap, pro, breastView1.f1030x,
                breastView1.f1031y, breastView1.f1029w, breastView1.f1028h);

        s = new ExtendBitmap();
        b = s.extendBitmapPixels(b, pro, breastView2.f1030x,
                breastView2.f1031y, breastView2.f1029w, breastView2.f1028h);

        imageView.setImageBitmap(b);
        saveBitmap = b;


        tvTitle.setText(getResources().getString(R.string.chest));
    }

    private void getWaistViewHeight(final View view) {
        if (view != null) {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver
                        .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                            @SuppressLint("NewApi")
                            public void onGlobalLayout() {
                                view.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                                if (view == breastView1) {
                                    breastView1
                                            .setScale(
                                                    0.2f,
                                                    (-((float) imageView
                                                            .getWidth())) / 4.0f);
                                } else if (view == breastView2) {
                                    breastView2
                                            .setScale(0.2f, ((float) imageView
                                                    .getWidth()) / 4.0f);
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                this.pw = ((float) this.breastView1.getWidth())
                        * this.breastView1.getScaleX();
                this.ph = ((float) this.breastView1.getHeight())
                        * this.breastView1.getScaleY();
                this.px = this.breastView1.getX()
                        + ((((float) this.breastView1.getWidth()) - this.pw) / 2.0f);
                this.py = this.breastView1.getY()
                        + ((((float) this.breastView1.getHeight()) - this.ph) / 2.0f);
                this.ratio = ((float) mBitmap.getHeight())
                        / ((float) this.frameLayout.getHeight());
                this.breastView1.f1029w = (int) (this.pw * this.ratio);
                this.breastView1.f1028h = (int) (this.ph * this.ratio);
                this.breastView1.f1030x = (int) (this.px * this.ratio);
                this.breastView1.f1031y = (int) (this.py * this.ratio);
                this.pw = ((float) this.breastView2.getWidth())
                        * this.breastView2.getScaleX();
                this.ph = ((float) this.breastView2.getHeight())
                        * this.breastView2.getScaleY();
                this.px = this.breastView2.getX()
                        + ((((float) this.breastView2.getWidth()) - this.pw) / 2.0f);
                this.py = this.breastView2.getY()
                        + ((((float) this.breastView2.getHeight()) - this.ph) / 2.0f);
                this.ratio = ((float) mBitmap.getHeight())
                        / ((float) this.frameLayout.getHeight());
                this.breastView2.f1029w = (int) (this.pw * this.ratio);
                this.breastView2.f1028h = (int) (this.ph * this.ratio);
                this.breastView2.f1030x = (int) (this.px * this.ratio);
                this.breastView2.f1031y = (int) (this.py * this.ratio);
                showSeekBarView();
                break;

            default:
                break;
        }
    }

    private void showSeekBarView() {
        start.setVisibility(View.GONE);
        seekBar1.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
