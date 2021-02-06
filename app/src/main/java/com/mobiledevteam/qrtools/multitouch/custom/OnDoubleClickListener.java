package com.mobiledevteam.qrtools.multitouch.custom;


import com.mobiledevteam.qrtools.multitouch.controller.MultiTouchEntity;

public interface OnDoubleClickListener {
	public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity);
	public void onBackgroundDoubleClick();
}
