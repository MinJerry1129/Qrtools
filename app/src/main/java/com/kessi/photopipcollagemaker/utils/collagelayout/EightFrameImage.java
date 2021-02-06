package com.kessi.photopipcollagemaker.utils.collagelayout;

import android.graphics.PointF;

import com.kessi.photopipcollagemaker.model.TemplateItem;
import com.kessi.photopipcollagemaker.template.PhotoItem;

import java.util.HashMap;

public class EightFrameImage {
    static TemplateItem collage_8_16() {
        TemplateItem item = FrameImageUtils.collage("collage_8_16.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.shrinkMethod = PhotoItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_15() {
        TemplateItem item = FrameImageUtils.collage("collage_8_15.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.6666f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.6666f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.6666f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.3333f, 0.6666f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_14() {
        TemplateItem item = FrameImageUtils.collage("collage_8_14.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.3333f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.3333f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.6666f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.3333f, 0.6666f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_13() {
        TemplateItem item = FrameImageUtils.collage("collage_8_13.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.75f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.3333f, 0.75f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_12() {
        TemplateItem item = FrameImageUtils.collage("collage_8_12.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.6666f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.6666f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 0.3333f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.3333f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.6666f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.6666f, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.75f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.3333f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_11() {
        TemplateItem item = FrameImageUtils.collage("collage_8_11.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.5f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.6666f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.6666f, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.75f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.3333f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_10() {
        TemplateItem item = FrameImageUtils.collage("collage_8_10.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.5f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.5f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.5f, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.75f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.5f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_9() {
        TemplateItem item = FrameImageUtils.collage("collage_8_9.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.25f, 0.3333f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.3333f, 0.25f, 0.6666f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.6666f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.5f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_8() {
        TemplateItem item = FrameImageUtils.collage("collage_8_8.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 0.6666f, 0.7f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.7f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.6666f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.6666f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.6666f, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_7() {
        TemplateItem item = FrameImageUtils.collage("collage_8_7.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3f, 0.2f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3f, 0, 0.6f, 0.2f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.2f, 0.3f, 0.6f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.3f, 0.2f, 0.6f, 0.6f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.6f, 0.6f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.6f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.6f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_6() {
        TemplateItem item = FrameImageUtils.collage("collage_8_6.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.6f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.6f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.6f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.3f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.3f, 0.5f, 0.6f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.75f, 0.3f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.3f, 0.75f, 0.6f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_5() {
        TemplateItem item = FrameImageUtils.collage("collage_8_5.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0, 0.75f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.75f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.25f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.25f, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.5f, 0.5f, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.75f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_4() {
        TemplateItem item = FrameImageUtils.collage("collage_8_4.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.25f, 0.25f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.25f, 0, 0.5f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.25f, 0.75f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.5f, 0, 0.75f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.5f, 0.25f, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.75f, 0, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.75f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_3() {
        TemplateItem item = FrameImageUtils.collage("collage_8_3.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.5f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.25f, 0.5f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.5f, 0.25f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.75f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.5f, 0.75f, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.75f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_2() {
        TemplateItem item = FrameImageUtils.collage("collage_8_2.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.3333f, 0.3333f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.6666f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.3333f, 0.5f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0.6666f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.6666f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_1() {
        TemplateItem item = FrameImageUtils.collage("collage_8_1.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.5f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0.5f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.6666f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.3333f, 0.6666f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItem collage_8_0() {
        TemplateItem item = FrameImageUtils.collage("collage_8_0.png");
        //first frame
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItem();
        photoItem.index = 3;
        photoItem.bound.set(0.5f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItem();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.5f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItem();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.75f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItem();
        photoItem.index = 6;
        photoItem.bound.set(0.5f, 0.5f, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItem();
        photoItem.index = 7;
        photoItem.bound.set(0.75f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }
}
