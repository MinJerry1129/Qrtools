package com.kessi.photopipcollagemaker.utils.collagelayout;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;


import com.kessi.photopipcollagemaker.model.TemplateItem;
import com.kessi.photopipcollagemaker.template.PhotoItem;
import com.kessi.photopipcollagemaker.utils.PhotoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FrameImageUtils {
    public static final String FRAME_FOLDER = "frame";

    protected static TemplateItem collage(String frameName) {
        TemplateItem item = new TemplateItem();
        item.setPreview(PhotoUtils.ASSET_PREFIX.concat(FrameImageUtils.FRAME_FOLDER).concat("/").concat(frameName));
        item.setTitle(frameName);
        return item;
    }

    private static TemplateItem collage_1_0() {
        TemplateItem item = collage("collage_1_0.png");
        PhotoItem photoItem = new PhotoItem();
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.index = 0;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    public static Path[] createTwoHeartItem() {
        Path[] result = new Path[2];
        Path t = new Path();
        t.moveTo(297.3F, 550.87F);
        t.cubicTo(283.52F, 535.43F, 249.13F, 505.34F, 220.86F, 483.99F);
        t.cubicTo(137.12F, 420.75F, 125.72F, 411.6F, 91.72F, 380.29F);
        t.cubicTo(29.03F, 322.57F, 2.41F, 264.58F, 2.5F, 185.95F);
        t.cubicTo(2.55F, 147.57F, 5.17F, 132.78F, 15.91F, 110.15F);
        t.cubicTo(34.15F, 71.77F, 61.01F, 43.24F, 95.36F, 25.8F);
        t.cubicTo(119.69F, 13.44F, 131.68F, 7.95F, 172.3F, 7.73F);
        t.cubicTo(214.8F, 7.49F, 223.74F, 12.45F, 248.74F, 26.18F);
        t.cubicTo(279.16F, 42.9F, 310.48F, 78.62F, 316.95F, 103.99F);
        t.lineTo(320.95F, 119.66F);
        result[0] = t;
        t = new Path();
        t.moveTo(320.95F, 119.66F);
        t.lineTo(330.81F, 98.08F);
        t.cubicTo(386.53F, -23.89F, 564.41F, -22.07F, 626.31F, 101.11F);
        t.cubicTo(645.95F, 140.19F, 648.11F, 223.62F, 630.69F, 270.62F);
        t.cubicTo(607.98F, 331.93F, 565.31F, 378.67F, 466.69F, 450.3F);
        t.cubicTo(402.01F, 497.27F, 328.8F, 568.35F, 323.71F, 578.33F);
        t.cubicTo(317.79F, 589.92F, 323.42F, 580.14F, 297.3F, 550.87F);
        result[1] = t;
        return result;
    }

    public static Path createHeartItem(float top, float size) {
        Path path = new Path();
        path.moveTo(top, top + size / 4);
        path.quadTo(top, top, top + size / 4, top);
        path.quadTo(top + size / 2, top, top + size / 2, top + size / 4);
        path.quadTo(top + size / 2, top, top + size * 3 / 4, top);
        path.quadTo(top + size, top, top + size, top + size / 4);
        path.quadTo(top + size, top + size / 2, top + size * 3 / 4, top + size * 3 / 4);
        path.lineTo(top + size / 2, top + size);
        path.lineTo(top + size / 4, top + size * 3 / 4);
        path.quadTo(top, top + size / 2, top, top + size / 4);
        return path;
    }

    public static Path createFatHeartItem() {
        Path path = new Path();
        path.moveTo(75, 40);
        path.cubicTo(75, 37, 70, 25, 50, 25);
        path.cubicTo(20, 25, 20, 62.5f, 20, 62.5f);
        path.cubicTo(20, 80, 40, 102, 75, 120);
        path.cubicTo(110, 102, 130, 80, 130, 62.5f);
        path.cubicTo(130, 62.5f, 130, 25, 100, 25);
        path.cubicTo(85, 25, 75, 37, 75, 40);
        Matrix m = new Matrix();
        m.postTranslate(-20, -25);
        path.transform(m);
        return path;
    }

    public static Path createHeartItem() {
        Path path = new Path();
        path.moveTo(256.0F, -7.47F);
        path.lineTo(225.07F, 20.69F);
        path.cubicTo(115.2F, 120.32F, 42.67F, 186.24F, 42.67F, 266.67F);
        path.cubicTo(42.67F, 332.59F, 94.29F, 384.0F, 160.0F, 384.0F);
        path.cubicTo(197.12F, 384.0F, 232.75F, 366.72F, 256.0F, 339.63F);
        path.cubicTo(279.25F, 366.72F, 314.88F, 384.0F, 352.0F, 384.0F);
        path.cubicTo(417.71F, 384.0F, 469.33F, 332.59F, 469.33F, 266.67F);
        path.cubicTo(469.33F, 186.24F, 396.8F, 120.32F, 286.93F, 20.69F);
        path.lineTo(256.0F, -7.47F);
        Matrix m = new Matrix();
        m.preScale(1, -1);
        m.postTranslate(-42, 384);
        path.transform(m);
        return path;
    }

    public static ArrayList<TemplateItem> loadFrameImages(Context context) {
        ArrayList<TemplateItem> templateItemList = new ArrayList<TemplateItem>();
        AssetManager am = context.getAssets();
        try {
            String[] frameNames = am.list(FRAME_FOLDER);
            templateItemList.clear();
            if (frameNames != null && frameNames.length > 0) {
                for (String str : frameNames) {
                    TemplateItem item = createTemplateItems(str);
                    if (item != null)
                        templateItemList.add(item);
                }

                Collections.sort(templateItemList, new Comparator<TemplateItem>() {
                    @Override
                    public int compare(TemplateItem lhs, TemplateItem rhs) {
                        return lhs.getPhotoItemList().size() - rhs.getPhotoItemList().size();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return templateItemList;
    }

    private static TemplateItem createTemplateItems(String frameName) {
        if (frameName.equals("collage_1_0.png")) {
            return collage_1_0();
        } else if (frameName.equals("collage_2_0.png")) {
            return TwoFrameImage.collage_2_0();
        } else if (frameName.equals("collage_2_1.png")) {
            return TwoFrameImage.collage_2_1();
        } else if (frameName.equals("collage_2_2.png")) {
            return TwoFrameImage.collage_2_2();
        } else if (frameName.equals("collage_2_3.png")) {
            return TwoFrameImage.collage_2_3();
        } else if (frameName.equals("collage_2_4.png")) {
            return TwoFrameImage.collage_2_4();
        } else if (frameName.equals("collage_2_5.png")) {
            return TwoFrameImage.collage_2_5();
        } else if (frameName.equals("collage_2_6.png")) {
            return TwoFrameImage.collage_2_6();
        } else if (frameName.equals("collage_2_7.png")) {
            return TwoFrameImage.collage_2_7();
        } else if (frameName.equals("collage_2_8.png")) {
            return TwoFrameImage.collage_2_8();
        } else if (frameName.equals("collage_2_9.png")) {
            return TwoFrameImage.collage_2_9();
        } else if (frameName.equals("collage_2_10.png")) {
            return TwoFrameImage.collage_2_10();
        } else if (frameName.equals("collage_2_11.png")) {
            return TwoFrameImage.collage_2_11();
        } else if (frameName.equals("collage_3_0.png")) {
            return ThreeFrameImage.collage_3_0();
        } else if (frameName.equals("collage_3_1.png")) {
            return ThreeFrameImage.collage_3_1();
        } else if (frameName.equals("collage_3_2.png")) {
            return ThreeFrameImage.collage_3_2();
        } else if (frameName.equals("collage_3_3.png")) {
            return ThreeFrameImage.collage_3_3();
        } else if (frameName.equals("collage_3_4.png")) {
            return ThreeFrameImage.collage_3_4();
        } else if (frameName.equals("collage_3_5.png")) {
            return ThreeFrameImage.collage_3_5();
        } else if (frameName.equals("collage_3_6.png")) {
            return ThreeFrameImage.collage_3_6();
        } else if (frameName.equals("collage_3_7.png")) {
            return ThreeFrameImage.collage_3_7();
        } else if (frameName.equals("collage_3_8.png")) {
            return ThreeFrameImage.collage_3_8();
        } else if (frameName.equals("collage_3_9.png")) {
            return ThreeFrameImage.collage_3_9();
        } else if (frameName.equals("collage_3_10.png")) {
            return ThreeFrameImage.collage_3_10();
        } else if (frameName.equals("collage_3_11.png")) {
            return ThreeFrameImage.collage_3_11();
        } else if (frameName.equals("collage_3_12.png")) {
            return ThreeFrameImage.collage_3_12();
        } else if (frameName.equals("collage_3_13.png")) {
            return ThreeFrameImage.collage_3_13();
        } else if (frameName.equals("collage_3_14.png")) {
            return ThreeFrameImage.collage_3_14();
        } else if (frameName.equals("collage_3_15.png")) {
            return ThreeFrameImage.collage_3_15();
        } else if (frameName.equals("collage_3_16.png")) {
            return ThreeFrameImage.collage_3_16();
        } else if (frameName.equals("collage_3_17.png")) {
            return ThreeFrameImage.collage_3_17();
        } else if (frameName.equals("collage_3_18.png")) {
            return ThreeFrameImage.collage_3_18();
        } else if (frameName.equals("collage_3_19.png")) {
            return ThreeFrameImage.collage_3_19();
        } else if (frameName.equals("collage_3_20.png")) {
            return ThreeFrameImage.collage_3_20();
        } else if (frameName.equals("collage_3_21.png")) {
            return ThreeFrameImage.collage_3_21();
        } else if (frameName.equals("collage_3_22.png")) {
            return ThreeFrameImage.collage_3_22();
        } else if (frameName.equals("collage_3_23.png")) {
            return ThreeFrameImage.collage_3_23();
        } else if (frameName.equals("collage_3_24.png")) {
            return ThreeFrameImage.collage_3_24();
        } else if (frameName.equals("collage_3_25.png")) {
            return ThreeFrameImage.collage_3_25();
        } else if (frameName.equals("collage_3_26.png")) {
            return ThreeFrameImage.collage_3_26();
        } else if (frameName.equals("collage_3_27.png")) {
            return ThreeFrameImage.collage_3_27();
        } else if (frameName.equals("collage_3_28.png")) {
            return ThreeFrameImage.collage_3_28();
        } else if (frameName.equals("collage_3_29.png")) {
            return ThreeFrameImage.collage_3_29();
        } else if (frameName.equals("collage_3_30.png")) {
            return ThreeFrameImage.collage_3_30();
        } else if (frameName.equals("collage_3_31.png")) {
            return ThreeFrameImage.collage_3_31();
        } else if (frameName.equals("collage_3_32.png")) {
            return ThreeFrameImage.collage_3_32();
        } else if (frameName.equals("collage_3_33.png")) {
            return ThreeFrameImage.collage_3_33();
        } else if (frameName.equals("collage_3_34.png")) {
            return ThreeFrameImage.collage_3_34();
        } else if (frameName.equals("collage_3_35.png")) {
            return ThreeFrameImage.collage_3_35();
        } else if (frameName.equals("collage_3_36.png")) {
            return ThreeFrameImage.collage_3_36();
        } else if (frameName.equals("collage_3_37.png")) {
            return ThreeFrameImage.collage_3_37();
        } else if (frameName.equals("collage_3_38.png")) {
            return ThreeFrameImage.collage_3_38();
        } else if (frameName.equals("collage_3_39.png")) {
            return ThreeFrameImage.collage_3_39();
        } else if (frameName.equals("collage_3_40.png")) {
            return ThreeFrameImage.collage_3_40();
        } else if (frameName.equals("collage_3_41.png")) {
            return ThreeFrameImage.collage_3_41();
        } else if (frameName.equals("collage_3_42.png")) {
            return ThreeFrameImage.collage_3_42();
        } else if (frameName.equals("collage_3_43.png")) {
            return ThreeFrameImage.collage_3_43();
        } else if (frameName.equals("collage_3_44.png")) {
            return ThreeFrameImage.collage_3_44();
        } else if (frameName.equals("collage_3_45.png")) {
            return ThreeFrameImage.collage_3_45();
        } else if (frameName.equals("collage_3_46.png")) {
            return ThreeFrameImage.collage_3_46();
        } else if (frameName.equals("collage_3_47.png")) {
            return ThreeFrameImage.collage_3_47();
        } else if (frameName.equals("collage_4_0.png")) {
            return FourFrameImage.collage_4_0();
        } else if (frameName.equals("collage_4_1.png")) {
            return FourFrameImage.collage_4_1();
        } else if (frameName.equals("collage_4_2.png")) {
            return FourFrameImage.collage_4_2();
        } else if (frameName.equals("collage_4_4.png")) {
            return FourFrameImage.collage_4_4();
        } else if (frameName.equals("collage_4_5.png")) {
            return FourFrameImage.collage_4_5();
        } else if (frameName.equals("collage_4_6.png")) {
            return FourFrameImage.collage_4_6();
        } else if (frameName.equals("collage_4_7.png")) {
            return FourFrameImage.collage_4_7();
        } else if (frameName.equals("collage_4_8.png")) {
            return FourFrameImage.collage_4_8();
        } else if (frameName.equals("collage_4_9.png")) {
            return FourFrameImage.collage_4_9();
        } else if (frameName.equals("collage_4_10.png")) {
            return FourFrameImage.collage_4_10();
        } else if (frameName.equals("collage_4_11.png")) {
            return FourFrameImage.collage_4_11();
        } else if (frameName.equals("collage_4_12.png")) {
            return FourFrameImage.collage_4_12();
        } else if (frameName.equals("collage_4_13.png")) {
            return FourFrameImage.collage_4_13();
        } else if (frameName.equals("collage_4_14.png")) {
            return FourFrameImage.collage_4_14();
        } else if (frameName.equals("collage_4_15.png")) {
            return FourFrameImage.collage_4_15();
        } else if (frameName.equals("collage_4_16.png")) {
            return FourFrameImage.collage_4_16();
        } else if (frameName.equals("collage_4_17.png")) {
            return FourFrameImage.collage_4_17();
        } else if (frameName.equals("collage_4_18.png")) {
            return FourFrameImage.collage_4_18();
        } else if (frameName.equals("collage_4_19.png")) {
            return FourFrameImage.collage_4_19();
        } else if (frameName.equals("collage_4_20.png")) {
            return FourFrameImage.collage_4_20();
        } else if (frameName.equals("collage_4_21.png")) {
            return FourFrameImage.collage_4_21();
        } else if (frameName.equals("collage_4_22.png")) {
            return FourFrameImage.collage_4_22();
        } else if (frameName.equals("collage_4_23.png")) {
            return FourFrameImage.collage_4_23();
        } else if (frameName.equals("collage_4_24.png")) {
            return FourFrameImage.collage_4_24();
        } else if (frameName.equals("collage_4_25.png")) {
            return FourFrameImage.collage_4_25();
        } else if (frameName.equals("collage_5_0.png")) {
            return FiveFrameImage.collage_5_0();
        } else if (frameName.equals("collage_5_1.png")) {
            return FiveFrameImage.collage_5_1();
        } else if (frameName.equals("collage_5_2.png")) {
            return FiveFrameImage.collage_5_2();
        } else if (frameName.equals("collage_5_3.png")) {
            return FiveFrameImage.collage_5_3();
        } else if (frameName.equals("collage_5_4.png")) {
            return FiveFrameImage.collage_5_4();
        } else if (frameName.equals("collage_5_5.png")) {
            return FiveFrameImage.collage_5_5();
        } else if (frameName.equals("collage_5_6.png")) {
            return FiveFrameImage.collage_5_6();
        } else if (frameName.equals("collage_5_7.png")) {
            return FiveFrameImage.collage_5_7();
        } else if (frameName.equals("collage_5_8.png")) {
            return FiveFrameImage.collage_5_8();
        } else if (frameName.equals("collage_5_9.png")) {
            return FiveFrameImage.collage_5_9();
        } else if (frameName.equals("collage_5_10.png")) {
            return FiveFrameImage.collage_5_10();
        } else if (frameName.equals("collage_5_11.png")) {
            return FiveFrameImage.collage_5_11();
        } else if (frameName.equals("collage_5_12.png")) {
            return FiveFrameImage.collage_5_12();
        } else if (frameName.equals("collage_5_13.png")) {
            return FiveFrameImage.collage_5_13();
        } else if (frameName.equals("collage_5_14.png")) {
            return FiveFrameImage.collage_5_14();
        } else if (frameName.equals("collage_5_15.png")) {
            return FiveFrameImage.collage_5_15();
        } else if (frameName.equals("collage_5_16.png")) {
            return FiveFrameImage.collage_5_16();
        } else if (frameName.equals("collage_5_17.png")) {
            return FiveFrameImage.collage_5_17();
        } else if (frameName.equals("collage_5_18.png")) {
            return FiveFrameImage.collage_5_18();
        } else if (frameName.equals("collage_5_19.png")) {
            return FiveFrameImage.collage_5_19();
        } else if (frameName.equals("collage_5_20.png")) {
            return FiveFrameImage.collage_5_20();
        } else if (frameName.equals("collage_5_21.png")) {
            return FiveFrameImage.collage_5_21();
        } else if (frameName.equals("collage_5_22.png")) {
            return FiveFrameImage.collage_5_22();
        } else if (frameName.equals("collage_5_23.png")) {
            return FiveFrameImage.collage_5_23();
        } else if (frameName.equals("collage_5_24.png")) {
            return FiveFrameImage.collage_5_24();
        } else if (frameName.equals("collage_5_25.png")) {
            return FiveFrameImage.collage_5_25();
        } else if (frameName.equals("collage_5_26.png")) {
            return FiveFrameImage.collage_5_26();
        } else if (frameName.equals("collage_5_27.png")) {
            return FiveFrameImage.collage_5_27();
        } else if (frameName.equals("collage_5_28.png")) {
            return FiveFrameImage.collage_5_28();
        } else if (frameName.equals("collage_5_29.png")) {
            return FiveFrameImage.collage_5_29();
        } else if (frameName.equals("collage_5_30.png")) {
            return FiveFrameImage.collage_5_30();
        } else if (frameName.equals("collage_5_31.png")) {
            return FiveFrameImage.collage_5_31();
        } else if (frameName.equals("collage_6_0.png")) {
            return SixFrameImage.collage_6_0();
        } else if (frameName.equals("collage_6_1.png")) {
            return SixFrameImage.collage_6_1();
        } else if (frameName.equals("collage_6_2.png")) {
            return SixFrameImage.collage_6_2();
        } else if (frameName.equals("collage_6_3.png")) {
            return SixFrameImage.collage_6_3();
        } else if (frameName.equals("collage_6_4.png")) {
            return SixFrameImage.collage_6_4();
        } else if (frameName.equals("collage_6_5.png")) {
            return SixFrameImage.collage_6_5();
        } else if (frameName.equals("collage_6_6.png")) {
            return SixFrameImage.collage_6_6();
        } else if (frameName.equals("collage_6_7.png")) {
            return SixFrameImage.collage_6_7();
        } else if (frameName.equals("collage_6_8.png")) {
            return SixFrameImage.collage_6_8();
        } else if (frameName.equals("collage_6_9.png")) {
            return SixFrameImage.collage_6_9();
        } else if (frameName.equals("collage_6_10.png")) {
            return SixFrameImage.collage_6_10();
        } else if (frameName.equals("collage_6_11.png")) {
            return SixFrameImage.collage_6_11();
        } else if (frameName.equals("collage_6_12.png")) {
            return SixFrameImage.collage_6_12();
        } else if (frameName.equals("collage_6_13.png")) {
            return SixFrameImage.collage_6_13();
        } else if (frameName.equals("collage_6_14.png")) {
            return SixFrameImage.collage_6_14();
        } else if (frameName.equals("collage_7_0.png")) {
            return SevenFrameImage.collage_7_0();
        } else if (frameName.equals("collage_7_1.png")) {
            return SevenFrameImage.collage_7_1();
        } else if (frameName.equals("collage_7_2.png")) {
            return SevenFrameImage.collage_7_2();
        } else if (frameName.equals("collage_7_3.png")) {
            return SevenFrameImage.collage_7_3();
        } else if (frameName.equals("collage_7_4.png")) {
            return SevenFrameImage.collage_7_4();
        } else if (frameName.equals("collage_7_5.png")) {
            return SevenFrameImage.collage_7_5();
        } else if (frameName.equals("collage_7_6.png")) {
            return SevenFrameImage.collage_7_6();
        } else if (frameName.equals("collage_7_7.png")) {
            return SevenFrameImage.collage_7_7();
        } else if (frameName.equals("collage_7_8.png")) {
            return SevenFrameImage.collage_7_8();
        } else if (frameName.equals("collage_7_9.png")) {
            return SevenFrameImage.collage_7_9();
        } else if (frameName.equals("collage_7_10.png")) {
            return SevenFrameImage.collage_7_10();
        } else if (frameName.equals("collage_8_0.png")) {
            return EightFrameImage.collage_8_0();
        } else if (frameName.equals("collage_8_1.png")) {
            return EightFrameImage.collage_8_1();
        } else if (frameName.equals("collage_8_2.png")) {
            return EightFrameImage.collage_8_2();
        } else if (frameName.equals("collage_8_3.png")) {
            return EightFrameImage.collage_8_3();
        } else if (frameName.equals("collage_8_4.png")) {
            return EightFrameImage.collage_8_4();
        } else if (frameName.equals("collage_8_5.png")) {
            return EightFrameImage.collage_8_5();
        } else if (frameName.equals("collage_8_6.png")) {
            return EightFrameImage.collage_8_6();
        } else if (frameName.equals("collage_8_7.png")) {
            return EightFrameImage.collage_8_7();
        } else if (frameName.equals("collage_8_8.png")) {
            return EightFrameImage.collage_8_8();
        } else if (frameName.equals("collage_8_9.png")) {
            return EightFrameImage.collage_8_9();
        } else if (frameName.equals("collage_8_10.png")) {
            return EightFrameImage.collage_8_10();
        } else if (frameName.equals("collage_8_11.png")) {
            return EightFrameImage.collage_8_11();
        } else if (frameName.equals("collage_8_12.png")) {
            return EightFrameImage.collage_8_12();
        } else if (frameName.equals("collage_8_13.png")) {
            return EightFrameImage.collage_8_13();
        } else if (frameName.equals("collage_8_14.png")) {
            return EightFrameImage.collage_8_14();
        } else if (frameName.equals("collage_8_15.png")) {
            return EightFrameImage.collage_8_15();
        } else if (frameName.equals("collage_8_16.png")) {
            return EightFrameImage.collage_8_16();
        } else if (frameName.equals("collage_9_0.png")) {
            return NineFrameImage.collage_9_0();
        } else if (frameName.equals("collage_9_1.png")) {
            return NineFrameImage.collage_9_1();
        } else if (frameName.equals("collage_9_2.png")) {
            return NineFrameImage.collage_9_2();
        } else if (frameName.equals("collage_9_3.png")) {
            return NineFrameImage.collage_9_3();
        } else if (frameName.equals("collage_9_4.png")) {
            return NineFrameImage.collage_9_4();
        } else if (frameName.equals("collage_9_5.png")) {
            return NineFrameImage.collage_9_5();
        } else if (frameName.equals("collage_9_6.png")) {
            return NineFrameImage.collage_9_6();
        } else if (frameName.equals("collage_9_7.png")) {
            return NineFrameImage.collage_9_7();
        } else if (frameName.equals("collage_9_8.png")) {
            return NineFrameImage.collage_9_8();
        } else if (frameName.equals("collage_9_9.png")) {
            return NineFrameImage.collage_9_9();
        } else if (frameName.equals("collage_9_10.png")) {
            return NineFrameImage.collage_9_10();
        } else if (frameName.equals("collage_9_11.png")) {
            return NineFrameImage.collage_9_11();
        } else if (frameName.equals("collage_10_0.png")) {
            return TenFrameImage.collage_10_0();
        } else if (frameName.equals("collage_10_1.png")) {
            return TenFrameImage.collage_10_1();
        } else if (frameName.equals("collage_10_2.png")) {
            return TenFrameImage.collage_10_2();
        } else if (frameName.equals("collage_10_3.png")) {
            return TenFrameImage.collage_10_3();
        } else if (frameName.equals("collage_10_4.png")) {
            return TenFrameImage.collage_10_4();
        } else if (frameName.equals("collage_10_5.png")) {
            return TenFrameImage.collage_10_5();
        } else if (frameName.equals("collage_10_6.png")) {
            return TenFrameImage.collage_10_6();
        } else if (frameName.equals("collage_10_7.png")) {
            return TenFrameImage.collage_10_7();
        } else if (frameName.equals("collage_10_8.png")) {
            return TenFrameImage.collage_10_8();
        }

        return null;
    }
}
