package com.kessi.photopipcollagemaker.shape;

import android.annotation.SuppressLint;
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


public class HipActivity extends AppCompatActivity implements OnClickListener {

	FrameLayout frameLayout;
	ImageView imageView;
	ViewWaist waistView;
	LinearLayout start;

	private float pw;
	private float px;
	private float py;
	private float ratio;
	private float ph;

	SeekBar seekBar1;
	private OnSeekBarChangeListener seekBarChangeListener = new seekbarListener();

	Bitmap mBitmap;

	Bitmap saveBitmap;
	ImageView btnApply, btnBack;

	TextView tvTitle;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_hip);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		imageView = (ImageView) findViewById(R.id.imageView);
		waistView = (ViewWaist) findViewById(R.id.waistView);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		mBitmap = Utils.mBitmap;

		waistView.imageView = imageView;
		this.waistView.setImageResource(R.drawable.icon_hip);

		imageView.setImageBitmap(mBitmap);
		getWaistViewHeight(waistView);

		start =  findViewById(R.id.btnStart);
		start.setOnClickListener(this);

		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		seekBar1.setOnSeekBarChangeListener(seekBarChangeListener);
		seekBar1.setMax(15);

		btnApply = (ImageView) findViewById(R.id.btnApply);
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.mBitmap = saveBitmap;
				setResult(-1);
				finish();
			}
		});

		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	class seekbarListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			tvTitle.setText("please wait....");
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
		Bitmap b = s.extendBitmapPixels(mBitmap, pro, waistView.f1035x,
				waistView.f1036y, waistView.f1034w, waistView.f1033h);

		imageView.setImageBitmap(b);
		saveBitmap = b;

		tvTitle.setText("Hip");
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
								if (view == waistView) {
									waistView.setScale(0.5f, 0.0f);
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
			this.pw = ((float) this.waistView.getWidth())
					* this.waistView.getScaleX();
			this.ph = ((float) this.waistView.getHeight())
					* this.waistView.getScaleY();
			this.px = this.waistView.getX()
					+ ((((float) this.waistView.getWidth()) - this.pw) / 2.0f);
			this.py = this.waistView.getY()
					+ ((((float) this.waistView.getHeight()) - this.ph) / 2.0f);
			this.ratio = ((float) mBitmap.getHeight())
					/ ((float) this.frameLayout.getHeight());
			this.waistView.f1034w = (int) (this.pw * this.ratio);
			this.waistView.f1033h = (int) (this.ph * this.ratio);
			this.waistView.f1035x = (int) (this.px * this.ratio);
			this.waistView.f1036y = (int) (this.py * this.ratio);

			showSeekBarView();
			break;

		default:
			break;
		}
	}

	private void showSeekBarView() {
		start.setVisibility(View.GONE);
		waistView.setVisibility(View.GONE);
		seekBar1.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
