package com.kessi.photopipcollagemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Field names are used in Gson. So Don't rename if file 'package.json' doesn't change field names.
 * @author vanhu_000
 *
 */
public class Language implements Parcelable {
	private String mName;
	private String mValue;

	public Language(){

	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setValue(String value) {
		mValue = value;
	}

	public String getValue() {
		return mValue;
	}

	public static final Parcelable.Creator<Language> CREATOR
			= new Parcelable.Creator<Language>() {
		public Language createFromParcel(Parcel in) {
			return new Language(in);
		}

		public Language[] newArray(int size) {
			return new Language[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mValue);
	}

	private Language(Parcel in) {
		mName = in.readString();
		mValue = in.readString();
	}
}
