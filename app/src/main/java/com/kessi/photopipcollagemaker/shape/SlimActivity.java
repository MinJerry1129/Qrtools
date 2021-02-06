package com.kessi.photopipcollagemaker.shape;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.utils.Utils;


@SuppressLint({ "ClickableViewAccessibility" })
public class SlimActivity extends AppCompatActivity implements OnSeekBarChangeListener,
		OnClickListener {
	public static int FH;
	public static int FLL = 0;
	public static int FLR = 150;
	public static int FLTOP = 180;
	public static int FW;
	public static int TLL = 0;
	public static int TLR = 150;
	public static int TRL = 180;
	private int _xDelta;
	private int _yDelta;
	Bitmap bmCenter;
	Bitmap bmpFace;
	Rect bmpRect;
	int bmpW;
	ImageView btnApply;
	ImageView btnBack;

	ImageView btnSlim;
	int extra_margin;

	FrameLayout flSlider;
	int fl_height;
	int fl_width;
	

	boolean isEditMode;
	ImageView ivBG;
	ImageView ivCenter;
	RelativeLayout ivLeft;
	RelativeLayout ivRight;
	int lastProg = -1;
	OnTouchListener leftTouch = new leftTouchListner();
	LinearLayout llEditor;
	LayoutParams lp;
	int newH;
	DisplayMetrics om;
	OnTouchListener rightTouch = new rightTouchListner();
	LinearLayout rlApply;
	RelativeLayout rlCapture;
	RelativeLayout rlCropper;
	RelativeLayout rlSeekbar;
	SeekBar sb;

	int shortLength;
	Bitmap src;
	ImageView thumb11;
	ImageView thumb21;
	TextView tvHeader;

	class leftTouchListner implements OnTouchListener {
		leftTouchListner() {
		}

		public boolean onTouch(View view, MotionEvent event) {
			int X = (int) event.getRawX();
			int Y = (int) event.getRawY();
			switch (event.getAction() & 255) {
			case 0:
				SlimActivity.this.thumb11
						.setBackgroundResource(R.drawable.right_arrow);
				FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view
						.getLayoutParams();
				SlimActivity.this._xDelta = X - lParams.leftMargin;
				SlimActivity.this._yDelta = Y - lParams.topMargin;
				break;
			case 1:
				SlimActivity.this.thumb11
						.setBackgroundResource(R.drawable.right_arrow);
				break;
			case 2:
				int leftM = X - SlimActivity.this._xDelta;
				if (leftM < SlimActivity.FLL - 30 && leftM > 0) {
					FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
							.getLayoutParams();
					SlimActivity.TLL = leftM;
					layoutParams.leftMargin = leftM;
					SlimActivity.TLR = SlimActivity.TLL;
					view.setLayoutParams(layoutParams);
					break;
				}
			}
			return true;
		}
	}

	class rightTouchListner implements OnTouchListener {
		rightTouchListner() {
		}

		public boolean onTouch(View view, MotionEvent event) {
			int X = (int) event.getRawX();
			int Y = (int) event.getRawY();
			switch (event.getAction() & 255) {
			case 0:
				SlimActivity.this.thumb21
						.setBackgroundResource(R.drawable.left_arrow);
				FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view
						.getLayoutParams();
				SlimActivity.this._xDelta = X - lParams.leftMargin;
				SlimActivity.this._yDelta = Y - lParams.topMargin;
				break;
			case 1:
				SlimActivity.this.thumb21
						.setBackgroundResource(R.drawable.left_arrow);
				break;
			case 2:
				int margin = X - SlimActivity.this._xDelta;
				int h = SlimActivity.this.ivRight.getWidth() + 6;
				if (margin > SlimActivity.this.ivBG.getWidth() - h) {
					margin = SlimActivity.this.ivBG.getWidth() - h;
				}
				if (margin > SlimActivity.FLR - 20
						&& margin < SlimActivity.this.bmpW) {
					FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
							.getLayoutParams();
					SlimActivity.TRL = margin;
					layoutParams.leftMargin = margin;
					view.setLayoutParams(layoutParams);
					break;
				}

			}
			return true;
		}
	}

	class setLayRunner implements Runnable {
		setLayRunner() {
		}

		public void run() {
			LayoutParams relativeParams = new LayoutParams(
					SlimActivity.this.ivBG.getWidth(),
					SlimActivity.this.ivBG.getHeight());
			relativeParams.addRule(13);
			SlimActivity.this.flSlider.setLayoutParams(relativeParams);
		}
	}

	class ivBgDrawListener implements OnPreDrawListener {
		ivBgDrawListener() {
		}

		public boolean onPreDraw() {
			SlimActivity.this.ivBG.getViewTreeObserver()
					.removeOnPreDrawListener(this);
			int h = SlimActivity.this.ivBG.getMeasuredHeight();
			int w = SlimActivity.this.ivBG.getMeasuredWidth();
			FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) SlimActivity.this.ivLeft
					.getLayoutParams();
			lp1.leftMargin = w / 3;
			SlimActivity.this.ivLeft.setLayoutParams(lp1);
			lp1 = (FrameLayout.LayoutParams) SlimActivity.this.ivRight
					.getLayoutParams();
			lp1.leftMargin = w / 2;
			SlimActivity.this.ivRight.setLayoutParams(lp1);
			return true;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slim_trim);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		this.om = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(this.om);
		this.fl_height = this.om.heightPixels;
		this.fl_width = this.om.widthPixels;
		Utils.screenWidth = this.fl_width;
		Utils.screenHeight = this.fl_height;
		Utils.slim = Boolean.valueOf(true);
		this.src = Utils.mBitmap;
		this.rlCapture = (RelativeLayout) findViewById(R.id.rl_capture);
		this.rlSeekbar = (RelativeLayout) findViewById(R.id.rlSeekbar);
		this.extra_margin = (Utils.screenWidth / 2) - (this.src.getWidth() / 2);
		this.tvHeader = (TextView) findViewById(R.id.screen_title);
		this.btnSlim = (ImageView) findViewById(R.id.btnSlim);
		this.btnBack = (ImageView) findViewById(R.id.btnBack);

		this.btnApply = (ImageView) findViewById(R.id.btnApply);
		this.btnBack.setOnClickListener(this);
		this.btnApply.setOnClickListener(this);

		this.btnSlim.setOnClickListener(this);
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		this.bmpW = this.src.getWidth() - (opts.outWidth / 2);
		this.ivLeft = (RelativeLayout) findViewById(R.id.thumbLeft);
		this.ivRight = (RelativeLayout) findViewById(R.id.thumbRight);
		this.thumb11 = (ImageView) findViewById(R.id.thumbLeft11);
		this.thumb21 = (ImageView) findViewById(R.id.thumbLeft21);

		this.ivLeft.setOnTouchListener(this.leftTouch);
		this.ivRight.setOnTouchListener(this.rightTouch);
		TLL = ((FrameLayout.LayoutParams) this.ivLeft.getLayoutParams()).leftMargin;
		TLR = TLL;
		this.flSlider = (FrameLayout) findViewById(R.id.flSlider);
		FLL = ((FrameLayout.LayoutParams) this.ivRight.getLayoutParams()).leftMargin;
		FLR = FLL + FW;

		this.ivBG = (ImageView) findViewById(R.id.imageView1);
		src = scaleDownLargeImageWithAspectRatio(src);
		this.rlCropper = (RelativeLayout) findViewById(R.id.rl_cropper);
		int dw = this.fl_width - src.getWidth();
		int pw = (dw * 100) / this.fl_width;
		int hi = this.fl_height - src.getHeight();
		int p1 = (hi * 100) / this.fl_height;

		if (src.getHeight() > src.getWidth()) {
			this.lp = (LayoutParams) this.rlCropper.getLayoutParams();
			this.lp.width = src.getWidth() + p1;
			this.lp.height = src.getHeight() + hi;

			this.lp.addRule(13);
			this.rlCropper.setLayoutParams(this.lp);
			this.ivBG.setImageBitmap(src);
			new Handler().postDelayed(new setLayRunner(), 500);
		} else {
			this.lp = (LayoutParams) this.rlCropper.getLayoutParams();
			this.lp.width = src.getWidth() + dw;
			this.lp.height = src.getHeight() + pw;

			this.lp.addRule(13);
			this.rlCropper.setLayoutParams(this.lp);
			this.ivBG.setImageBitmap(src);
		}
		this.rlApply = findViewById(R.id.rlApply);
		this.llEditor = (LinearLayout) findViewById(R.id.llEditor);
		this.sb = (SeekBar) findViewById(R.id.seekBar1);
		this.sb.setOnSeekBarChangeListener(this);
		this.ivBG.getViewTreeObserver().addOnPreDrawListener(new ivBgDrawListener());
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		this.tvHeader.setText(progress + "%");
		this.shortLength = progress / 4;
		RectF target = new RectF(0.0f, 0.0f,
				(float) (this.bmCenter.getWidth() - this.shortLength),
				(float) this.bmCenter.getHeight());
		Bitmap targetBmp = Bitmap.createBitmap((int) target.right,
				(int) target.bottom, Config.ARGB_8888);
		new Canvas(targetBmp).drawBitmap(this.bmCenter, this.bmpRect, target,
				null);
		this.ivCenter.setImageBitmap(targetBmp);
		int newL = FLL
				- ((this.bmCenter.getWidth() - targetBmp.getWidth()) / 2);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FW, FH);
		params.topMargin = FLTOP;
		params.leftMargin = newL;
		
		params.leftMargin = newL;
		params.rightMargin = this.newH;
		this.lastProg = progress;
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		this.tvHeader.setText(getResources().getString(R.string.make_me_slim));
	}

	public void onBackPressed() {
		if (this.isEditMode) {
			this.sb.setProgress(0);
			this.btnApply.setVisibility(View.GONE);
			this.btnApply.setAlpha(0.5f);
			this.tvHeader.setText(getResources().getString(R.string.slim));
			this.isEditMode = false;
			this.rlApply.setVisibility(View.VISIBLE);
			this.btnBack.setVisibility(View.VISIBLE);

			this.rlCropper.setVisibility(View.VISIBLE);
			this.flSlider.setVisibility(View.VISIBLE);
			this.ivBG.setVisibility(View.VISIBLE);
			this.rlSeekbar.setVisibility(View.GONE);
			this.llEditor.setVisibility(View.GONE);
			
			return;
		}
		super.onBackPressed();
		Utils.slim = Boolean.valueOf(false);
	}

	Bitmap scaleDownLargeImageWithAspectRatio(Bitmap image) {
		Matrix m = new Matrix();
		if (image.getHeight() > Utils.screenHeight || image.getWidth() > Utils.screenWidth) {
			m.setRectToRect(new RectF(0.0f, 0.0f, (float) image.getWidth(),
					(float) image.getHeight()), new RectF(0.0f, 0.0f,
					(float) Utils.screenWidth, ((float) Utils.screenHeight)
							- getResources().getDimension(R.dimen.mar_pic)),
					ScaleToFit.CENTER);
		} else {
			m.setRectToRect(new RectF(0.0f, 0.0f, (float) image.getWidth(),
					(float) image.getHeight()), new RectF(0.0f, 0.0f,
					(float) image.getWidth(), (float) image.getHeight()),
					ScaleToFit.CENTER);
		}
		return Bitmap.createBitmap(image, 0, 0, image.getWidth(),
				image.getHeight(), m, true);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnApply:
			this.rlCapture.setDrawingCacheEnabled(true);
			Bitmap cap = this.rlCapture.getDrawingCache();
			src = Bitmap
					.createBitmap(cap, this.shortLength / 2, 0, cap.getWidth()
							- (this.shortLength / 2), cap.getHeight());
			this.rlCapture.setDrawingCacheEnabled(false);
			src = scaleDownLargeImageWithAspectRatio(src);
			Utils.mBitmap = src;
			Utils.slim = Boolean.valueOf(false);
			setResult(-1);
			finish();
			return;
		case R.id.btnBack:
			onBackPressed();
			return;

		case R.id.btnSlim:
			this.ivBG.setDrawingCacheEnabled(true);
			src = Bitmap.createBitmap(this.ivBG
					.getDrawingCache());
			this.ivBG.setDrawingCacheEnabled(false);
			this.src = src;
			try {
				this.btnApply.setVisibility(View.VISIBLE);
				this.btnApply.setAlpha(1.0f);
				this.rlApply.setVisibility(View.GONE);
				this.flSlider.setVisibility(View.GONE);
				this.btnBack.setVisibility(View.GONE);

				this.rlCropper.setVisibility(View.GONE);
				this.llEditor.setVisibility(View.VISIBLE);
				this.isEditMode = true;
				this.rlSeekbar.setVisibility(View.VISIBLE);
				int dy0 = TLR;
				int dy1 = TRL;
				if (dy0 < 1) {
					dy0 = 1;
				}
				if (dy1 < 1) {
					dy1 = 1;
				}
				Bitmap bbTop = Bitmap.createBitmap(this.src, 0, 0, dy0,
						this.src.getHeight());
				this.bmCenter = Bitmap.createBitmap(this.src, dy0, 0,
						dy1 - dy0, this.src.getHeight());
				this.bmpRect = new Rect(0, 0, this.bmCenter.getWidth(),
						this.bmCenter.getHeight());
				Bitmap bbBottom = Bitmap.createBitmap(this.src, dy1, 0,
						this.src.getWidth() - dy1, this.src.getHeight());
				ImageView ivTop = (ImageView) findViewById(R.id.ivTop);
				this.ivCenter = (ImageView) findViewById(R.id.ivCenter);
				ImageView ivBottom = (ImageView) findViewById(R.id.ivBottom);
				ivTop.setImageBitmap(bbTop);
				this.ivCenter.setImageBitmap(this.bmCenter);
				ivBottom.setImageBitmap(bbBottom);
				FLL = FLL < 1 ? 1 : FLL;
				FLR = FLR < 1 ? 1 : FLR;
				FLTOP = FLTOP < 1 ? 1 : FLTOP;
				FW = FW < 1 ? 1 : FW;
				FH = FH < 1 ? 1 : FH;
				this.bmpFace = Bitmap.createBitmap(src, FLL,
						FLTOP, FW, FH);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						FW, FH);
				params.topMargin = FLTOP;
				params.leftMargin = FLL;
				
				return;
			} catch (Exception e) {
				Toast.makeText(this,
						"error. Please Correct the bounds and try again", Toast.LENGTH_LONG)
						.show();
				onBackPressed();
				return;
			}
		default:
			return;
		}
	}
}
