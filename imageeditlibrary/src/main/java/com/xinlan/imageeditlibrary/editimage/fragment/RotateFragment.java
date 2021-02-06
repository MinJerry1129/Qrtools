package com.xinlan.imageeditlibrary.editimage.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.kessi.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.view.RotateImageView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;


public class RotateFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_ROTATE;
    public static final String TAG = RotateFragment.class.getName();
    private View mainView;
    private View backToMenu;//
    public SeekBar mSeekBar;//
    private RotateImageView mRotatePanel;//

    public static RotateFragment newInstance() {
        RotateFragment fragment = new RotateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_rotate, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backToMenu = mainView.findViewById(R.id.back_to_main);
        mSeekBar = (SeekBar) mainView.findViewById(R.id.rotate_bar);
        mSeekBar.setProgress(0);

        this.mRotatePanel = ensureEditActivity().mRotatePanel;
        backToMenu.setOnClickListener(new BackToMenuClick());//
        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                getActivity().getResources().getDisplayMetrics().widthPixels * 100 / 1080,
                getActivity().getResources().getDisplayMetrics().heightPixels * 166 / 1920);
//        paramsBtn.gravity = Gravity.BOTTOM;
        backToMenu.setLayoutParams(paramsBtn);
        mSeekBar.setOnSeekBarChangeListener(new RotateAngleChange());
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_ROTATE;
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setVisibility(View.GONE);

        activity.mRotatePanel.addBit(activity.getMainBit(),
                activity.mainImage.getBitmapRect());
        activity.mRotateFragment.mSeekBar.setProgress(0);
        activity.mRotatePanel.reset();
        activity.mRotatePanel.setVisibility(View.VISIBLE);
        activity.bannerFlipper.showNext();
    }


    private final class RotateAngleChange implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int angle,
                                      boolean fromUser) {
            // System.out.println("progress--->" + progress);
            mRotatePanel.rotateImage(angle);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }// end inner class


    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end class


    @Override
    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(0);
        activity.mainImage.setVisibility(View.VISIBLE);
        this.mRotatePanel.setVisibility(View.GONE);
        activity.bannerFlipper.showPrevious();
    }


    public void applyRotateImage() {

        if (mSeekBar.getProgress() == 0 || mSeekBar.getProgress() == 360) {//
            backToMain();
            return;
        } else {//
            SaveRotateImageTask task = new SaveRotateImageTask();
            task.execute(activity.getMainBit());
        }// end if
    }


    private final class SaveRotateImageTask extends
            AsyncTask<Bitmap, Void, Bitmap> {
        //private Dialog dialog;

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            RectF imageRect = mRotatePanel.getImageNewRect();
            Bitmap originBit = params[0];
            Bitmap result = Bitmap.createBitmap((int) imageRect.width(),
                    (int) imageRect.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int w = originBit.getWidth() >> 1;
            int h = originBit.getHeight() >> 1;
            float centerX = imageRect.width() / 2;
            float centerY = imageRect.height() / 2;

            float left = centerX - w;
            float top = centerY - h;

            RectF dst = new RectF(left, top, left + originBit.getWidth(), top
                    + originBit.getHeight());
            canvas.save();

//            canvas.scale(mRotatePanel.getScale(), mRotatePanel.getScale(),
//                    imageRect.width() / 2, imageRect.height() / 2);
            canvas.rotate(mRotatePanel.getRotateAngle(), imageRect.width() / 2,
                    imageRect.height() / 2);

            canvas.drawBitmap(originBit, new Rect(0, 0, originBit.getWidth(),
                    originBit.getHeight()), dst, null);
            canvas.restore();
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            //dialog.dismiss();
            if (result == null)
                return;

            //
            activity.changeMainBitmap(result,true);
            backToMain();
        }
    }// end inner class
}// end class
