package com.kessi.photopipcollagemaker.frame;

import android.graphics.Matrix;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class FrameEntity implements Parcelable {

	private Uri mImage;
	private Matrix mMatrix = new Matrix();

	public FrameEntity() {

	}

	private FrameEntity(Parcel in) {
		float[] values = new float[9];
		in.readFloatArray(values);
		mMatrix.setValues(values);
		mImage = in.readParcelable(Uri.class.getClassLoader());
	}

	public void setImage(Uri image) {
		mImage = image;
	}

	public Uri getImage() {
		return mImage;
	}

	public void setMatrix(Matrix matrix) {
		mMatrix.set(matrix);
	}

	public Matrix getMatrix() {
		return new Matrix(mMatrix);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		float[] values = new float[9];
		mMatrix.getValues(values);
		dest.writeFloatArray(values);
		dest.writeParcelable(mImage, flags);

	}

	public static final Parcelable.Creator<FrameEntity> CREATOR = new Parcelable.Creator<FrameEntity>() {
		@Override
		public FrameEntity createFromParcel(Parcel in) {
			return new FrameEntity(in);
		}

		@Override
		public FrameEntity[] newArray(int size) {
			return new FrameEntity[size];
		}
	};
}
