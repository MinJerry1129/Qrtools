package com.kessi.photopipcollagemaker.utils;


import com.kessi.photopipcollagemaker.model.TemplateItem;
import com.kessi.photopipcollagemaker.template.PhotoItem;

import java.util.ArrayList;



public class TemplateImageUtils {
    private static final String TEMPLATE_FOLDER = "template";
    private static final String PREVIEW_POSTFIX = "_preview.png";
    private static final String TEMPLATE_POSTFIX = "_fg.png";

    private static TemplateItem template_2_4() {
        final String templateName = "2_4";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 52;
        photoItem.y = 180;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 215;
        photoItem.y = 360;
        item.getPhotoItemList().add(photoItem);
        //third item
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.maskPath = createMaskPath(templateName, 2);
        photoItem.x = 378;
        photoItem.y = 206;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_2_3() {
        final String templateName = "2_3";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 0;
        photoItem.y = 287;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 167;
        photoItem.y = 193;
        item.getPhotoItemList().add(photoItem);
        //third item
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.maskPath = createMaskPath(templateName, 2);
        photoItem.x = 399;
        photoItem.y = 99;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_2_2() {
        final String templateName = "2_2";
        final TemplateItem item = template(templateName);
        //third item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.maskPath = createMaskPath(templateName, 2);
        photoItem.x = 8;
        photoItem.y = 3;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 306;
        photoItem.y = 143;
        item.getPhotoItemList().add(photoItem);
        //first item
        photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 110;
        photoItem.y = 290;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_2_1() {
        final String templateName = "2_1";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 10;
        photoItem.y = 127;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 282;
        photoItem.y = 0;
        item.getPhotoItemList().add(photoItem);
        //third item
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.maskPath = createMaskPath(templateName, 2);
        photoItem.x = 286;
        photoItem.y = 329;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_2_0() {
        final String templateName = "2_0";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 46;
        photoItem.y = 12;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 173;
        photoItem.y = 249;
        item.getPhotoItemList().add(photoItem);
        //third item
        photoItem = new PhotoItem();
        photoItem.index = 2;
        photoItem.maskPath = createMaskPath(templateName, 2);
        photoItem.x = 336;
        photoItem.y = 29;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_19() {
        final String templateName = "1_19";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 70;
        photoItem.y = 611;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 399;
        photoItem.y = 611;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_18() {
        final String templateName = "1_18";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 165;
        photoItem.y = 537;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 308;
        photoItem.y = 248;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_17() {
        final String templateName = "1_17";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 37;
        photoItem.y = 177;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 313;
        photoItem.y = 414;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_16() {
        final String templateName = "1_16";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 72;
        photoItem.y = 191;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 285;
        photoItem.y = 315;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_15() {
        final String templateName = "1_15";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 79;
        photoItem.y = 596;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 320;
        photoItem.y = 458;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_14() {
        final String templateName = "1_14";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 268;
        photoItem.y = 405;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 89;
        photoItem.y = 691;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_13() {
        final String templateName = "1_13";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 98;
        photoItem.y = 333;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 396;
        photoItem.y = 472;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_12() {
        final String templateName = "1_12";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 52;
        photoItem.y = 356;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 336;
        photoItem.y = 15;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_11() {
        final String templateName = "1_11";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 87;
        photoItem.y = 365;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 426;
        photoItem.y = 349;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_10() {
        final String templateName = "1_10";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 128;
        photoItem.y = 551;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 336;
        photoItem.y = 566;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_9() {
        final String templateName = "1_9";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 87;
        photoItem.y = 378;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 354;
        photoItem.y = 396;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_8() {
        final String templateName = "1_8";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 68;
        photoItem.y = 364;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 316;
        photoItem.y = 425;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_7() {
        final String templateName = "1_7";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 8;
        photoItem.y = 231;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 247;
        photoItem.y = 40;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_6() {
        final String templateName = "1_6";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 91;
        photoItem.y = 313;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 316;
        photoItem.y = 313;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_5() {
        final String templateName = "1_5";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 61;
        photoItem.y = 151;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 334;
        photoItem.y = 156;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_4() {
        final String templateName = "1_4";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 84;
        photoItem.y = 18;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 96;
        photoItem.y = 330;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_3() {
        final String templateName = "1_3";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 22;
        photoItem.y = 202;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 346;
        photoItem.y = 212;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_2() {
        final String templateName = "1_2";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 42;
        photoItem.y = 243;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 301;
        photoItem.y = 145;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_1() {
        final String templateName = "1_1";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 88;
        photoItem.y = 197;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 289;
        photoItem.y = 173;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_1_0() {
        final String templateName = "1_0";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 35;
        photoItem.y = 340;
        item.getPhotoItemList().add(photoItem);
        //second item
        photoItem = new PhotoItem();
        photoItem.index = 1;
        photoItem.maskPath = createMaskPath(templateName, 1);
        photoItem.x = 350;
        photoItem.y = 340;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_20() {
        final String templateName = "0_20";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 140;
        photoItem.y = 371;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_19() {
        final String templateName = "0_19";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 29;
        photoItem.y = 129;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_18() {
        final String templateName = "0_18";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 75;
        photoItem.y = 141;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_17() {
        final String templateName = "0_17";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 43;
        photoItem.y = 87;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_16() {
        final String templateName = "0_16";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 59;
        photoItem.y = 247;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_15() {
        final String templateName = "0_15";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 30;
        photoItem.y = 146;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_14() {
        final String templateName = "0_14";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 150;
        photoItem.y = 319;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_13() {
        final String templateName = "0_13";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 21;
        photoItem.y = 179;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_12() {
        final String templateName = "0_12";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 169;
        photoItem.y = 161;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_11() {
        final String templateName = "0_11";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 138;
        photoItem.y = 62;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_10() {
        final String templateName = "0_10";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 61;
        photoItem.y = 216;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_9() {
        final String templateName = "0_9";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 83;
        photoItem.y = 183;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_8() {
        final String templateName = "0_8";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 232;
        photoItem.y = 215;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_7() {
        final String templateName = "0_7";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 84;
        photoItem.y = 74;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_6() {
        final String templateName = "0_6";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 110;
        photoItem.y = 425;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_5() {
        final String templateName = "0_5";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 78;
        photoItem.y = 444;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_4() {
        final String templateName = "0_4";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 63;
        photoItem.y = 68;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_3() {
        final String templateName = "0_3";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 28;
        photoItem.y = 62;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_2() {
        final String templateName = "0_2";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 75;
        photoItem.y = 103;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_1() {
        final String templateName = "0_1";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 60;
        photoItem.y = 238;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    private static TemplateItem template_0_0() {
        final String templateName = "0_0";
        final TemplateItem item = template(templateName);
        //first item
        PhotoItem photoItem = new PhotoItem();
        photoItem.index = 0;
        photoItem.maskPath = createMaskPath(templateName, 0);
        photoItem.x = 344;
        photoItem.y = 120;
        item.getPhotoItemList().add(photoItem);
        return item;
    }


    public static ArrayList<TemplateItem> loadTemplates() {
        ArrayList<TemplateItem> templateItemList = new ArrayList<TemplateItem>();
        templateItemList.add(template_0_0());
        templateItemList.add(template_0_1());
        templateItemList.add(template_0_2());
        templateItemList.add(template_0_3());
        templateItemList.add(template_0_4());
        templateItemList.add(template_0_5());
        templateItemList.add(template_0_6());
        templateItemList.add(template_0_7());
        templateItemList.add(template_0_8());
        templateItemList.add(template_0_9());
        templateItemList.add(template_0_10());
        templateItemList.add(template_0_11());
        templateItemList.add(template_0_12());
        templateItemList.add(template_0_13());
        templateItemList.add(template_0_14());
        templateItemList.add(template_0_15());
        templateItemList.add(template_0_16());
        templateItemList.add(template_0_17());
        templateItemList.add(template_0_18());
        templateItemList.add(template_0_19());
        templateItemList.add(template_0_20());
        templateItemList.add(template_1_0());
        templateItemList.add(template_1_1());
        templateItemList.add(template_1_2());
        templateItemList.add(template_1_3());
        templateItemList.add(template_1_4());
        templateItemList.add(template_1_5());
        templateItemList.add(template_1_6());
        templateItemList.add(template_1_7());
        templateItemList.add(template_1_8());
        templateItemList.add(template_1_9());
        templateItemList.add(template_1_10());
        templateItemList.add(template_1_11());
        templateItemList.add(template_1_12());
        templateItemList.add(template_1_13());
        templateItemList.add(template_1_14());
        templateItemList.add(template_1_15());
        templateItemList.add(template_1_16());
        templateItemList.add(template_1_17());
        templateItemList.add(template_1_18());
        templateItemList.add(template_1_19());
        templateItemList.add(template_2_0());
        templateItemList.add(template_2_1());
        templateItemList.add(template_2_2());
        templateItemList.add(template_2_3());
        templateItemList.add(template_2_4());
        return templateItemList;
    }

    private static TemplateItem template(String templateName) {
        TemplateItem item = new TemplateItem();
        item.setPreview(PhotoUtils.ASSET_PREFIX.concat(TEMPLATE_FOLDER).concat("/").concat(templateName).concat(PREVIEW_POSTFIX));
        item.setTemplate(PhotoUtils.ASSET_PREFIX.concat(TEMPLATE_FOLDER).concat("/").concat(templateName).concat(TEMPLATE_POSTFIX));
        item.setTitle(templateName);
        return item;
    }

    private static String createMaskPath(String templateName, int maskIdx) {
        return PhotoUtils.ASSET_PREFIX.concat(TEMPLATE_FOLDER).concat("/").concat(templateName).concat("_") + maskIdx + ".png";
    }
}
