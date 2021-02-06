package com.xinlan.imageeditlibrary.editimage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kessi.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;


public class MainMenuFragment extends BaseEditFragment implements View.OnClickListener {
    public static final int INDEX = ModuleConfig.INDEX_MAIN;

    public static final String TAG = MainMenuFragment.class.getName();
    private View mainView;

    private ImageView stickerBtn;//
    private ImageView fliterBtn;//
    private ImageView cropBtn;//
    private ImageView rotateBtn;//
    private ImageView mTextBtn;//
    private ImageView mPaintBtn;//
    private ImageView mBeautyBtn;//

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_main_menu,
                null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stickerBtn = mainView.findViewById(R.id.btn_stickers);
        fliterBtn = mainView.findViewById(R.id.btn_filter);
        cropBtn = mainView.findViewById(R.id.btn_crop);
        rotateBtn = mainView.findViewById(R.id.btn_rotate);
        mTextBtn = mainView.findViewById(R.id.btn_text);
        mPaintBtn = mainView.findViewById(R.id.btn_paint);
        mBeautyBtn = mainView.findViewById(R.id.btn_beauty);

        stickerBtn.setOnClickListener(this);
        fliterBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mPaintBtn.setOnClickListener(this);
        mBeautyBtn.setOnClickListener(this);
        setLay();
    }

    void setLay() {
        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                getActivity().getResources().getDisplayMetrics().widthPixels * 140 / 1080,
                getActivity().getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        paramsBtn.setMargins(15,0,15,0);
        stickerBtn.setLayoutParams(paramsBtn);
        fliterBtn.setLayoutParams(paramsBtn);
        cropBtn.setLayoutParams(paramsBtn);
        rotateBtn.setLayoutParams(paramsBtn);
        mTextBtn.setLayoutParams(paramsBtn);
        mPaintBtn.setLayoutParams(paramsBtn);
        mBeautyBtn.setLayoutParams(paramsBtn);
    }


    @Override
    public void onShow() {
        // do nothing
    }

    @Override
    public void backToMain() {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        if (v == stickerBtn) {
            onStickClick();
        } else if (v == fliterBtn) {
            onFilterClick();
        } else if (v == cropBtn) {
            onCropClick();
        } else if (v == rotateBtn) {
            onRotateClick();
        } else if (v == mTextBtn) {
            onAddTextClick();
        } else if (v == mPaintBtn) {
            onPaintClick();
        }else if(v == mBeautyBtn){
            onBeautyClick();
        }
    }


    private void onStickClick() {
        activity.bottomGallery.setCurrentItem(StickerFragment.INDEX);
        activity.mStickerFragment.onShow();
    }


    private void onFilterClick() {
        activity.bottomGallery.setCurrentItem(FilterListFragment.INDEX);
        activity.mFilterListFragment.onShow();
    }


    private void onCropClick() {
        activity.bottomGallery.setCurrentItem(CropFragment.INDEX);
        activity.mCropFragment.onShow();
    }


    private void onRotateClick() {
        activity.bottomGallery.setCurrentItem(RotateFragment.INDEX);
        activity.mRotateFragment.onShow();
    }


    private void onAddTextClick() {
        activity.bottomGallery.setCurrentItem(AddTextFragment.INDEX);
        activity.mAddTextFragment.onShow();
    }


    private void onPaintClick() {
        activity.bottomGallery.setCurrentItem(PaintFragment.INDEX);
        activity.mPaintFragment.onShow();
    }

    private void onBeautyClick(){
        activity.bottomGallery.setCurrentItem(BeautyFragment.INDEX);
        activity.mBeautyFragment.onShow();
    }

}// end class
