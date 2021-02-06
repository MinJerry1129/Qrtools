package com.kessi.photopipcollagemaker.multitouch.custom;


import com.kessi.photopipcollagemaker.multitouch.controller.MultiTouchEntity;

public interface OnDoubleClickListener {
	public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity);
	public void onBackgroundDoubleClick();
}
