package com.kessi.photopipcollagemaker.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;

public class PhotoUtils {
    public static final String EDITED_WHITE_IMAGE_SUFFIX = "_white.jpg";
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;
    public static final String DRAWABLE_PREFIX = "drawable://";
    public static final String ASSET_PREFIX = "assets://";

    public static void addImageToGallery(final String filePath, final Context context) {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static Bitmap fillBackgroundColorToImage(Bitmap bitmap, int color) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(color);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        return result;
    }

    public static Bitmap rotateImage(Bitmap src, float degs, boolean flip) {
        if (degs == 0) return src;
        Bitmap bm = null;
        if (src != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degs);
            if (flip) {
                matrix.postScale(1, -1);
            }
            bm = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        }
        return bm;
    }

    public static Bitmap rotateImage(Bitmap src, float degs) {
        return rotateImage(src, degs, false);
    }

    public static int getCameraPhotoOrientation(Context context, String imagePath) {
        int rotate = 90;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static void loadImageWithGlide(Context context, ImageView imageView, String uri) {
        if (uri != null && uri.length() > 1) {
            if (uri.startsWith("http://") || uri.startsWith("https://")) {
                Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
            } else if (uri.startsWith(DRAWABLE_PREFIX)) {
                try {
                    int id = Integer.parseInt(uri.substring(DRAWABLE_PREFIX.length()));
                    Glide.with(context).load(id).into(imageView);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (uri.startsWith(ASSET_PREFIX)) {
                String file = uri.substring(ASSET_PREFIX.length());
                Glide.with(context).load(Uri.parse("file:///android_asset/".concat(file))).into(imageView);
            } else {
                Glide.with(context).load(new File(uri)).into(imageView);
            }
        }
    }

    public static Bitmap decodePNGImage(Context context, String uri) {
        if (uri.startsWith(DRAWABLE_PREFIX)) {
            try {
                int resId = Integer.parseInt(uri.substring(DRAWABLE_PREFIX.length()));
                return BitmapFactory.decodeResource(context.getResources(), resId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (uri.startsWith(ASSET_PREFIX)) {
            String path = uri.substring(ASSET_PREFIX.length());
            try {
                InputStream is = context.getAssets().open(path);
                return BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return BitmapFactory.decodeFile(uri);
        }

        return null;
    }

    /**
     * @param src
     * @param type is FLIP_VERTICAL or FLIP_HORIZONTAL
     * @return flipped image
     */
    public static Bitmap flip(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap blurImage(Bitmap bitmap, float radius) {
        StackBlurManager stackBlurManager = new StackBlurManager(bitmap);
        return stackBlurManager.processNatively((int) radius);
    }

    /**
     * @deprecated
     * @param sentBitmap
     * @param radius
     * @return blurred image
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        if (bitmap.isRecycled())
            return null;
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static float calculateScaleRatio(int imageWidth, int imageHeight, int viewWidth, int viewHeight) {
        float ratioWidth = ((float) imageWidth) / viewWidth;
        float ratioHeight = ((float) imageHeight) / viewHeight;
        return Math.max(ratioWidth, ratioHeight);
    }

    public static int[] calculateThumbnailSize(int imageWidth, int imageHeight, int viewWidth, int viewHeight) {
        int[] size = new int[2];
        float ratioWidth = ((float) imageWidth) / viewWidth;
        float ratioHeight = ((float) imageHeight) / viewHeight;
        float ratio = Math.max(ratioWidth, ratioHeight);
        if (ratio == ratioWidth) {
            size[0] = viewWidth;
            size[1] = (int) (imageHeight / ratio);
        } else {
            size[0] = (int) (imageWidth / ratio);
            size[1] = viewHeight;
        }

        return size;
    }

    /**
     * Remove all transparent color borders
     *
     * @param bitmap
     * @return
     */
    public static Bitmap cleanImage(Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        int top = 0, left = 0, right = width, bottom = height;
        for (int idx = 0; idx < width; idx++)
            if (!isTransparentColumn(bitmap, idx)) {
                left = idx;
                break;
            }
        for (int idx = width - 1; idx > left; idx--)
            if (!isTransparentColumn(bitmap, idx)) {
                right = idx;
                break;
            }
        for (int idx = 0; idx < height; idx++)
            if (!isTransparentRow(bitmap, idx)) {
                top = idx;
                break;
            }
        for (int idx = height - 1; idx > top; idx--)
            if (!isTransparentRow(bitmap, idx)) {
                bottom = idx;
                break;
            }
        return Bitmap.createBitmap(bitmap, left, top, right - left + 1, bottom - top + 1);
    }

    private static boolean isTransparentRow(Bitmap bitmap, int row) {
        for (int idx = 0; idx < bitmap.getWidth(); idx++)
            if (bitmap.getPixel(idx, row) != Color.TRANSPARENT) {
                return false;
            }
        return true;
    }

    private static boolean isTransparentColumn(Bitmap bitmap, int column) {
        for (int idx = 0; idx < bitmap.getHeight(); idx++)
            if (bitmap.getPixel(column, idx) != Color.TRANSPARENT) {
                return false;
            }
        return true;
    }

    public static Bitmap transparentPadding(Bitmap image, float ratioWidthPerHeight) throws OutOfMemoryError {
        try {
            final int width = image.getWidth();
            final int height = image.getHeight();
            int x = 0, y = 0;
            // desfault
            int destWidth = width;
            int destHeight = (int) (width / ratioWidthPerHeight);
            y = (destHeight - height) / 2;
            // else
            if (y < 0) {
                destHeight = height;
                destWidth = (int) (height * ratioWidthPerHeight);
                x = Math.max((destWidth - width) / 2, 0);
                y = 0;
            }

            Bitmap result = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            canvas.drawARGB(0x00, 0x00, 0x00, 0x00);
            canvas.drawBitmap(image, x, y, new Paint());
            return result;
        } catch (OutOfMemoryError err) {
            throw err;
        }
    }

    public static Bitmap createBitmapMask(final ArrayList<PointF> pointList, final int imageWidth,
                                          final int imageHeight) {
        Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0x00, 0x00, 0x00, 0x00);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);

        Path path = new Path();
        for (int i = 0; i < pointList.size(); i++) {
            if (i == 0) {
                path.moveTo(pointList.get(i).x, pointList.get(i).y);
            } else {
                path.lineTo(pointList.get(i).x, pointList.get(i).y);
            }
        }

        canvas.drawPath(path, paint);
        canvas.clipPath(path);

        return bitmap;

    }

    public static Bitmap cropImage(Bitmap mainImage, Bitmap mask, Matrix m) {
        Canvas canvas = new Canvas();
        Bitmap result = Bitmap.createBitmap(mainImage.getWidth(), mainImage.getHeight(), Bitmap.Config.ARGB_8888);

        canvas.setBitmap(result);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);

        canvas.drawBitmap(mainImage, m, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        return result;
    }

    public static Bitmap cropImage(Bitmap mainImage, Bitmap mask) {
        Canvas canvas = new Canvas();
        Bitmap result = Bitmap.createBitmap(mainImage.getWidth(), mainImage.getHeight(), Bitmap.Config.ARGB_8888);

        canvas.setBitmap(result);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);

        canvas.drawBitmap(mainImage, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        return result;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}

