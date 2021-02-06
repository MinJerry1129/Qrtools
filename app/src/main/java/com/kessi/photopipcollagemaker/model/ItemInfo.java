package com.kessi.photopipcollagemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemInfo implements Parcelable {
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_DELETED = "deleted";

    public static final int NORMAL_ITEM_TYPE = 0;
    public static final int PACKAGE_ITEM_TYPE = 1;
    public static final int ADD_ITEM_TYPE = 2;
    public static final int SQUARE_CROP_TYPE = 3;
    public static final int CUSTOM_CROP_TYPE = 4;
    public static final int DRAW_CROP_TYPE = 5;

    private String mTitle;
    private String mThumbnail;
    private String mSelectedThumbnail;
    private boolean mSelected = false;
    // To be used to display
    private int mShowingType = NORMAL_ITEM_TYPE;
    // To be used in database
    private String mLastModified;
    private String mStatus;
    private long mId;

    public ItemInfo() {

    }

    public void setLastModified(String lastModified) {
        mLastModified = lastModified;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getStatus() {
        return mStatus;
    }

    public long getId() {
        return mId;
    }

    public String getLastModified() {
        return mLastModified;
    }

    public void setTitle(String name) {
        mTitle = name;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setShowingType(int type) {
        mShowingType = type;
    }

    public int getShowingType() {
        return mShowingType;
    }

    public void setThumbnail(String thumbnailPath) {
        mThumbnail = thumbnailPath;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setSelectedThumbnail(String selectedThumbnail) {
        mSelectedThumbnail = selectedThumbnail;
    }

    public String getSelectedThumbnail() {
        return mSelectedThumbnail;
    }

    public static final Parcelable.Creator<ItemInfo> CREATOR
            = new Parcelable.Creator<ItemInfo>() {
        public ItemInfo createFromParcel(Parcel in) {
            return new ItemInfo(in);
        }

        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mThumbnail);
        dest.writeString(mSelectedThumbnail);
        dest.writeBooleanArray(new boolean[]{mSelected});
        // To be used to display
        dest.writeInt(mShowingType);
        // To be used in database
        dest.writeString(mLastModified);
        dest.writeString(mStatus);
        dest.writeLong(mId);
    }

    protected ItemInfo(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mSelectedThumbnail = in.readString();
        boolean[] b = new boolean[1];
        in.readBooleanArray(b);
        mSelected = b[0];
        mShowingType = in.readInt();
        mLastModified = in.readString();
        mStatus = in.readString();
        mId = in.readLong();
    }
}
