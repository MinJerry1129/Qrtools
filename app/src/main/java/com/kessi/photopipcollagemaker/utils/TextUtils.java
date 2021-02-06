package com.kessi.photopipcollagemaker.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;


import com.kessi.photopipcollagemaker.R;
import com.kessi.photopipcollagemaker.model.FontItem;

import java.util.ArrayList;
import java.util.List;



public class TextUtils {
    public static final String FONT_FOLDER = "fonts";

    private TextUtils() {

    }

    public static List<FontItem> loadFonts(Context context) {
        final List<FontItem> result = new ArrayList<FontItem>();
        FontItem defaultFont = new FontItem(context.getString(R.string.default_font), null);
        result.add(defaultFont);
        AssetManager am = context.getAssets();
        try {
            String[] fontNames = am.list(FONT_FOLDER);
            if (fontNames != null) {
                for (String name : fontNames) {
                    FontItem item = new FontItem(name.substring(0, name.length() - 4),
                            PhotoUtils.ASSET_PREFIX.concat(FONT_FOLDER).concat("/").concat(name));
                    result.add(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int[] findParagraphSize(final Paint paint,
                                          final String originalText, final String[] sentences) {
        final Rect textBound = new Rect();
        paint.getTextBounds(originalText, 0, originalText.length(), textBound);
        final int textHeight = textBound.height();
        paint.getTextBounds("m", 0, 1, textBound);
        final int delta = (int) (0.5 * textBound.height() + textHeight);
        final int pHeight = textHeight + (sentences.length - 1) * delta;
        int pWidth = 0, tmp = 0;
        for (String str : sentences) {
            tmp = (int) (paint.measureText(str, 0, str.length()) + .5);
            if (tmp > pWidth) {
                pWidth = tmp;
            }
        }

        return new int[]{pWidth, pHeight};
    }

    /**
     * This function is optimized for drawing.
     *
     * @param canvas
     * @param paint
     * @param centerX
     * @param centerY
     * @param originalText
     * @param sentences
     */
    public static void drawParagraph(final Canvas canvas, final Paint paint, final int centerX, final int centerY,
                                     final String originalText, final String[] sentences) {
        final Rect textBound = new Rect();
        paint.getTextBounds(originalText, 0, originalText.length(), textBound);
        final int textHeight = textBound.height();
        paint.getTextBounds("m", 0, 1, textBound);
        final int unitHeight = (int) (0.5 * textBound.height());
        final int delta = (int) (0.5 * textBound.height() + textHeight);
        final int pHeight = textHeight + (sentences.length - 1) * delta;
        int pWidth = 0, tmp = 0;
        for (String str : sentences) {
            tmp = (int) (paint.measureText(str, 0, str.length()) + .5);
            if (tmp > pWidth) {
                pWidth = tmp;
            }
        }

        final int top = centerY - pHeight / 2;
        final int left = centerX;
        //Draw every sentences
        int y = top + textHeight;
        int type = containHighCharacter(originalText);
        if (type == 1) {
            y = y - unitHeight;
        }

        for (String str : sentences) {
            canvas.drawText(str, left, y, paint);
            y += delta;
        }
    }

    private static int containHighCharacter(String text) {
        boolean q = false;
        boolean t = false;
        for (int idx = 0; idx < text.length(); idx++) {
            char c = text.charAt(idx);
            if (c == 'q' || c == 'y' || c == 'p' || c == 'g' || c == 'j') {
                q = true;
            } else if (c == 't' || c == 'd' || c == 'f' || c == 'h' || c == 'k' || c == 'l' || c == 'b') {
                t = true;
            }
        }

        if (q) {
            return 1;
        } else if (t) {
            return 2;
        } else {
            return 0;
        }
    }

    public static Typeface loadTypeface(Context context, String path) {
        if (path == null || path.length() < 1) {
            return Typeface.DEFAULT;
        } else if (path.startsWith(PhotoUtils.ASSET_PREFIX)) {
            String assetPath = path.substring(PhotoUtils.ASSET_PREFIX.length());
            return Typeface.createFromAsset(context.getAssets(), assetPath);
        } else {
            return Typeface.createFromFile(path);
        }
    }
}
