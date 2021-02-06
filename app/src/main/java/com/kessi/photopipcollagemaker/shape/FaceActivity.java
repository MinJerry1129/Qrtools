package com.kessi.photopipcollagemaker.shape;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.utils.Utils;


public class FaceActivity extends AppCompatActivity implements OnClickListener {

	FrameLayout frameLayout;
	ImageView imageView;
	ViewFace faceView;
	LinearLayout start;

	private float pw;
	private float px;
	private float py;
	private float ratio;
	private float ph;

	SeekBar seekBar1;
	private OnSeekBarChangeListener seekBarChangeListener = new seekbarListener();

	

	Bitmap saveBitmap;
	ImageView btnApply, btnBack;

	Bitmap mBitmap;

	TextView tvTitle;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_face);

		frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		imageView = (ImageView) findViewById(R.id.imageView);
		faceView = (ViewFace) findViewById(R.id.waistView);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		mBitmap = Utils.mBitmap;

		faceView.imageView = imageView;

		imageView.setImageBitmap(mBitmap);
		getWaistViewHeight(faceView);

		start = findViewById(R.id.btnStart);
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

		ShrinkBitmap s = new ShrinkBitmap();
		Bitmap b = s.shrinkBitmapPixels(mBitmap, pro, faceView.f1035x,
				faceView.f1036y, faceView.f1034w, faceView.f1033h);

		imageView.setImageBitmap(b);
		saveBitmap = b;

		tvTitle.setText(getResources().getString(R.string.face));
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
								if (view == faceView) {
									faceView.setScale(0.5f, 0.0f);
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
			this.pw = ((float) this.faceView.getWidth())
					* this.faceView.getScaleX();
			this.ph = ((float) this.faceView.getHeight())
					* this.faceView.getScaleY();
			this.px = this.faceView.getX()
					+ ((((float) this.faceView.getWidth()) - this.pw) / 2.0f);
			this.py = this.faceView.getY()
					+ ((((float) this.faceView.getHeight()) - this.ph) / 2.0f);
			this.ratio = ((float) mBitmap.getHeight())
					/ ((float) this.frameLayout.getHeight());
			this.faceView.f1034w = (int) (this.pw * this.ratio);
			this.faceView.f1033h = (int) (this.ph * this.ratio);
			this.faceView.f1035x = (int) (this.px * this.ratio);
			this.faceView.f1036y = (int) (this.py * this.ratio);

			showSeekBarView();
			break;

		default:
			break;
		}
	}

	private void showSeekBarView() {

		start.setVisibility(View.GONE);
		faceView.setVisibility(View.GONE);
		seekBar1.setVisibility(View.VISIBLE);

	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
