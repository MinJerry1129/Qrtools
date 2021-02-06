package com.kessi.photopipcollagemaker.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.kessi.photopipcollagemaker.appconfig.Logger;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//

public class ImageUtils {
    public static class MemoryInfo {
        public long availMem = 0;
        public long totalMem = 0;
    }

    public static final String OUTPUT_COLLAGE_FOLDER = Environment.getExternalStorageDirectory().toString().concat("/").concat("CollageMaker");
    private static final float MIN_OUTPUT_IMAGE_SIZE = 640.0f;

    public static void loadImageWithPicasso(final Context context, final ImageView imageView, final String uri) {
        if (uri != null && uri.length() > 1) {
            if (uri.startsWith("http://") || uri.startsWith("https://")) {
                Picasso.with(context).load(uri).into(imageView);
            } else if (uri.startsWith(PhotoUtils.DRAWABLE_PREFIX)) {
                try {
                    int id = Integer.parseInt(uri.substring(PhotoUtils.DRAWABLE_PREFIX.length()));
                    Picasso.with(context).load(id).into(imageView);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (uri.startsWith(PhotoUtils.ASSET_PREFIX)) {
                String file = uri.substring(PhotoUtils.ASSET_PREFIX.length());
                Picasso.with(context).load(Uri.parse("file:///android_asset/".concat(file))).into(imageView);
            } else {
                Picasso.with(context).load(new File(uri)).into(imageView);
            }
        }
    }

    public static MemoryInfo getMemoryInfo(Context context) {
        final MemoryInfo info = new MemoryInfo();
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        info.availMem = memInfo.availMem;

        if (Build.VERSION.SDK_INT >= 16) {
            info.totalMem = memInfo.totalMem;
        } else {
            try {
                final RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
                final String load = reader.readLine();
                // Get the Number value from the string
                Pattern p = Pattern.compile("(\\d+)");
                Matcher m = p.matcher(load);
                String value = "";
                while (m.find()) {
                    value = m.group(1);
                }
                reader.close();
                info.totalMem = (long) Double.parseDouble(value) * 1024;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Logger.d("ImageUtils", "getMemoryInfo, availMem=" + info.availMem + ", totalMem=" + info.totalMem);
        return info;
    }

    public static float calculateOutputScaleFactor(int viewWidth, int viewHeight) {
        float ratio = Math.min(viewWidth, viewHeight) / MIN_OUTPUT_IMAGE_SIZE;
        if (ratio < 1 && ratio > 0) {
            ratio = 1.0f / ratio;
        } else {
            ratio = 1;
        }
        Logger.d("ImageUtils", "calculateOutputScaleFactor, viewWidth=" + viewWidth + ", viewHeight=" + viewHeight + ", ratio=" + ratio);
        return ratio;
    }

    public static void saveAndShare(Context context, Bitmap image) {
        try {
            String fileName = DateTimeUtils.getCurrentDateTime().replaceAll(":", "-").concat(".png");
            File collageFolder = new File(OUTPUT_COLLAGE_FOLDER);
            if (!collageFolder.exists()) {
                collageFolder.mkdirs();
            }
            File photoFile = new File(collageFolder, fileName);
            image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
            PhotoUtils.addImageToGallery(photoFile.getAbsolutePath(), context);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(photoFile.getAbsolutePath())));
//            context.startActivity(Intent.createChooser(share, context.getString(R.string.photo_editor_share_image)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Matrix createMatrixToDrawImageInCenterView(final float viewWidth, final float viewHeight, final float imageWidth, final float imageHeight) {
        final float ratioWidth = ((float) viewWidth) / imageWidth;
        final float ratioHeight = ((float) viewHeight) / imageHeight;
        final float ratio = Math.max(ratioWidth, ratioHeight);
        final float dx = (viewWidth - imageWidth) / 2.0f;
        final float dy = (viewHeight - imageHeight) / 2.0f;
        Matrix result = new Matrix();
        result.postTranslate(dx, dy);
        result.postScale(ratio, ratio, viewWidth / 2, viewHeight / 2);
        return result;
    }

    public static long getUsedMemorySize() {

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;

    }

    public static void recycleView(View iv) {
        if (iv == null) {
            return;
        }

        Drawable background = iv.getBackground();
        iv.setBackgroundColor(Color.TRANSPARENT);

        if (background != null && background instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) background).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
    }

    public static void recycleImageView(ImageView iv) {
        if (iv == null) {
            return;
        }

        Drawable background = iv.getBackground();
        Drawable d = iv.getDrawable();
        iv.setBackgroundColor(Color.TRANSPARENT);
        iv.setImageBitmap(null);

        if (background != null && background instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) background).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }

        if (d != null && d instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) d).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
    }

    public static long getSizeInBytes(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * @param v
     * @return
     * @throws OutOfMemoryError
     * @deprecated
     */
    public static Bitmap loadBitmapFromView(View v) throws OutOfMemoryError {
        try {
            final int width = v.getMeasuredWidth();
            final int height = v.getMeasuredHeight();
            final Drawable bg = v.getBackground();
            v.setBackgroundColor(Color.TRANSPARENT);
            v.layout(0, 0, width, height);
            Bitmap returnedBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(returnedBitmap);
            if (bg != null) {
                bg.draw(c);
            }

            v.draw(c);
            if (Build.VERSION.SDK_INT >= 16) {
                v.setBackground(bg);
            } else {
                v.setBackgroundDrawable(bg);
            }

            return returnedBitmap;
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
            throw err;
        }
    }

    /**
     * @param view
     * @param outputImagePath
     * @throws OutOfMemoryError
     * @deprecated
     */
    public static void takeScreen(View view, final String outputImagePath)
            throws OutOfMemoryError {
        try {
            Bitmap bitmap = loadBitmapFromView(view);
            File imageFile = new File(outputImagePath);
            imageFile.getParentFile().mkdirs();
            OutputStream fout = null;

            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
            throw err;
        }
    }

    public static void saveBitmap(Bitmap bitmap, final String path) {
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getImageOrientation(Context context, String imagePath) {
        int orientation = getOrientationFromExif(imagePath);
        if (orientation < 0) {
            Uri uri = Uri.fromFile(new File(imagePath));
            orientation = getOrientationFromMediaStore(context, uri);
        }

        return orientation;
    }

    // private static String getExifTag(ExifInterface exif,String tag){
    // String attribute = exif.getAttribute(tag);
    //
    // return (null != attribute ? attribute : "");
    // }
    private static int getOrientationFromExif(String imagePath) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            // StringBuilder builder = new StringBuilder();
            //
            // builder.append("Date & Time: " +
            // getExifTag(exif,ExifInterface.TAG_DATETIME) + "\n\n");
            // builder.append("Flash: " +
            // getExifTag(exif,ExifInterface.TAG_FLASH) + "\n");
            // builder.append("Focal Length: " +
            // getExifTag(exif,ExifInterface.TAG_FOCAL_LENGTH) + "\n\n");
            // builder.append("GPS Datestamp: " +
            // getExifTag(exif,ExifInterface.TAG_FLASH) + "\n");
            // builder.append("GPS Latitude: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LATITUDE) + "\n");
            // builder.append("GPS Latitude Ref: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LATITUDE_REF) + "\n");
            // builder.append("GPS Longitude: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LONGITUDE) + "\n");
            // builder.append("GPS Longitude Ref: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LONGITUDE_REF) + "\n");
            // builder.append("GPS Processing Method: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_PROCESSING_METHOD) + "\n");
            // builder.append("GPS Timestamp: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_TIMESTAMP) + "\n\n");
            // builder.append("Image Length: " +
            // getExifTag(exif,ExifInterface.TAG_IMAGE_LENGTH) + "\n");
            // builder.append("Image Width: " +
            // getExifTag(exif,ExifInterface.TAG_IMAGE_WIDTH) + "\n\n");
            // builder.append("Camera Make: " +
            // getExifTag(exif,ExifInterface.TAG_MAKE) + "\n");
            // builder.append("Camera Model: " +
            // getExifTag(exif,ExifInterface.TAG_MODEL) + "\n");
            // builder.append("Camera Orientation: " +
            // getExifTag(exif,ExifInterface.TAG_ORIENTATION) + "\n");
            // builder.append("Camera White Balance: " +
            // getExifTag(exif,ExifInterface.TAG_WHITE_BALANCE) + "\n");
            // builder.append("Camera orientation=" + getExifTag(exif,
            // ExifInterface.TAG_ORIENTATION));
            // ALog.d("ImageUtils.getOrientationFromExif", "exif=" +
            // builder.toString());
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;

                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    private static int getOrientationFromMediaStore(Context context,
                                                    Uri imageUri)// (Context context, String imagePath)
    {
        // Uri imageUri = getImageContentUri(context, imagePath);
        if (imageUri == null) {
            return -1;
        }

        String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri,
                projection, null, null, null);

        int orientation = -1;
        if (cursor != null && cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
            cursor.close();
        }

        return orientation;
    }

    //
    // private static Uri getImageContentUri(Context context, String imagePath)
    // {
    // String[] projection = new String[] {MediaStore.Images.Media._ID};
    // String selection = MediaStore.Images.Media.DATA + "=? ";
    // String[] selectionArgs = new String[] {imagePath};
    // Cursor cursor = context.getContentResolver().query(IMAGE_PROVIDER_URI,
    // projection,
    // selection, selectionArgs, null);
    //
    // if (cursor != null && cursor.moveToFirst()) {
    // int imageId = cursor.getInt(0);
    // cursor.close();
    //
    // return Uri.withAppendedPath(IMAGE_PROVIDER_URI,
    // Integer.toString(imageId));
    // }
    //
    // if (new File(imagePath).exists()) {
    // ContentValues values = new ContentValues();
    // values.put(MediaStore.Images.Media.DATA, imagePath);
    //
    // return context.getContentResolver().insert(
    // MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    // }
    //
    // return null;
    // }

    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012
        //

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

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
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

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

    public static Bitmap getCircularArea(Bitmap bitmap, int circleX,
                                         int circleY, int radius) {
        Bitmap output = Bitmap.createBitmap(2 * radius, 2 * radius,
                Bitmap.Config.ARGB_8888);

        Rect rect = new Rect();
        rect.top = circleY - radius;
        rect.bottom = rect.top + 2 * radius;
        rect.left = circleX - radius;
        rect.right = rect.left + 2 * radius;

        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(output.getWidth() / 2, output.getHeight() / 2,
                radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, new Rect(0, 0, output.getWidth(),
                output.getHeight()), paint);

        return output;
    }

    public static Bitmap focus(Bitmap src, Bitmap filtratedBitmap, Rect rect,
                               boolean isCircle) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        Bitmap focusBitmap = null;
        Bitmap area = Bitmap.createBitmap(rect.right - rect.left, rect.bottom
                - rect.top, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(area);
        canvas.drawBitmap(src, rect,
                new Rect(0, 0, area.getWidth(), area.getHeight()), paint);

        if (isCircle) {
            focusBitmap = getCircularBitmap(area);
            area.recycle();
            area = null;
        } else {
            focusBitmap = area;
            area = null;
        }

        Bitmap result = Bitmap.createBitmap(filtratedBitmap.getWidth(),
                filtratedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(result);
        canvas.drawBitmap(focusBitmap, new Rect(0, 0, focusBitmap.getWidth(),
                focusBitmap.getHeight()), rect, paint);

        focusBitmap.recycle();
        focusBitmap = null;

        return result;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, r,
                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
