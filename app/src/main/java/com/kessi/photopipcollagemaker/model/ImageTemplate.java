package com.kessi.photopipcollagemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageTemplate extends ItemInfo {
    private Language[] mNames;
    private long mPackageId;
    private String mPreview;
    private String mTemplate;
    private String mChild;

    public ImageTemplate() {

    }

    public void setPackageId(long packageId) {
        mPackageId = packageId;
    }

    public long getPackageId() {
        return mPackageId;
    }

    public void setPreview(String preview) {
        mPreview = preview;
    }

    public String getPreview() {
        return mPreview;
    }

    public void setTemplate(String template) {
        mTemplate = template;
    }

    public String getTemplate() {
        return mTemplate;
    }

    public void setChild(String child) {
        mChild = child;
    }

    public String getChild() {
        return mChild;
    }

    public Language[] getLanguages() {
        return mNames;
    }

    public void setLanguages(Language[] names) {
        mNames = names;
    }

    public static final Parcelable.Creator<ImageTemplate> CREATOR
            = new Parcelable.Creator<ImageTemplate>() {
        public ImageTemplate createFromParcel(Parcel in) {
            return new ImageTemplate(in);
        }

        public ImageTemplate[] newArray(int size) {
            return new ImageTemplate[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        int len = 0;
        if (mNames != null && mNames.length > 0) {
            len = mNames.length;
        }
        dest.writeInt(len);
        if (mNames != null && len > 0) {
            dest.writeTypedArray(mNames, flags);
        }

        dest.writeLong(mPackageId);
        dest.writeString(mPreview);
        dest.writeString(mTemplate);
        dest.writeString(mChild);
    }

    protected ImageTemplate(Parcel in) {
        super(in);
        int len = in.readInt();
        if (len > 0) {
            mNames = new Language[len];
            in.readTypedArray(mNames, Language.CREATOR);
        }
        mPackageId = in.readLong();
        mPreview = in.readString();
        mTemplate = in.readString();
        mChild = in.readString();
    }
}
