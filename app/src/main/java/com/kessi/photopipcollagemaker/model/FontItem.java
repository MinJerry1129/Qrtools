package com.kessi.photopipcollagemaker.model;


public class FontItem {
    private String mFontName;
    private String mFontPath;

    public FontItem(String fontName, String fontPath) {
        mFontName = fontName;
        mFontPath = fontPath;
    }

    public String getFontName() {
        return mFontName;
    }

    public String getFontPath() {
        return mFontPath;
    }
}
