package com.kessi.photopipcollagemaker.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.listener.OnChooseColorListener;
import com.kessi.photopipcollagemaker.listener.OnShareImageListener;
import com.kessi.photopipcollagemaker.Activities.fragment.BaseFragment;
import com.kessi.photopipcollagemaker.Activities.fragment.ScrapBookFragment;

public class ScrapBookActivity extends BaseFragmentActivity implements
        OnShareImageListener, OnChooseColorListener {
    public static final int PHOTO_TYPE = 1;
    public static final int FRAME_TYPE = 2;
    public static final String EXTRA_CREATED_METHOD_TYPE = "methodType";

    private int mSelectedColor = Color.GREEN;
    private boolean mClickedShareButton = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_book);

        if (savedInstanceState == null) {

            BaseFragment fragment = new ScrapBookFragment();

            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        } else {
            mClickedShareButton = savedInstanceState.getBoolean("mClickedShareButton", false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mClickedShareButton", mClickedShareButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mClickedShareButton) {
            mClickedShareButton = false;

        }
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getVisibleFragment();
        if (fragment instanceof ScrapBookFragment) {
            super.onBackPressed();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onShareImage(String imagePath) {
        mClickedShareButton = true;
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager.findFragmentById(R.id.frame_container);
    }

    @Override
    public void onShareFrame(String imagePath) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Shared image frame: " + imagePath,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSelectedColor(int color) {
        mSelectedColor = color;
    }

    @Override
    public int getSelectedColor() {
        return mSelectedColor;
    }
}

