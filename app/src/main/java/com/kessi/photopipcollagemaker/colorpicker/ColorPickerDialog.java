package com.kessi.photopipcollagemaker.colorpicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kessi.photopipcollagemaker.R;


public class ColorPickerDialog extends Dialog implements
		ColorPickerView.OnColorChangedListener, View.OnClickListener {

	private ColorPickerView mColorPicker;

	private TextView mOldColor;
	private TextView mNewColor;

	private OnColorChangedListener mListener;

	public interface OnColorChangedListener {
		public void onColorChanged(int color);
	}

	public ColorPickerDialog(Context context, int initialColor) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		init(initialColor);
	}

	private void init(int color) {
		// To fight color banding.
		getWindow().setFormat(PixelFormat.RGBA_8888);

		setUp(color);

	}

	private void setUp(int color) {

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.dialog_color_picker, null);

		setContentView(layout);

		setTitle(R.string.photo_editor_dialog_color_picker);

		mColorPicker = (ColorPickerView) layout
				.findViewById(R.id.color_picker_view);
		mOldColor = layout
				.findViewById(R.id.old_color_panel);
		mNewColor = layout
				.findViewById(R.id.new_color_panel);


		((LinearLayout) mOldColor.getParent()).setPadding(
				Math.round(mColorPicker.getDrawingOffset()), 0,
				Math.round(mColorPicker.getDrawingOffset()), 0);

		mOldColor.setOnClickListener(this);
		mNewColor.setOnClickListener(this);
		mColorPicker.setOnColorChangedListener(this);
		mColorPicker.setColor(color, true);

	}

	int oldColor;
	public void setOldColor(int color){
		oldColor = color;
		mColorPicker.setColor(color, true);
	}

	int newColor;
	@Override
	public void onColorChanged(int color) {
		newColor = color;
	}

	/**
	 * Set a OnColorChangedListener to get notified when the color selected by
	 * the user has changed.
	 * 
	 * @param listener
	 */
	public void setOnColorChangedListener(OnColorChangedListener listener) {
		mListener = listener;
	}

	public int getColor() {
		return mColorPicker.getColor();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.new_color_panel) {
			if (mListener != null) {
				mListener.onColorChanged(newColor);
			}
		}
		dismiss();
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt("old_color", oldColor);
		state.putInt("new_color", newColor);
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mColorPicker.setColor(savedInstanceState.getInt("new_color"), true);
	}


}
