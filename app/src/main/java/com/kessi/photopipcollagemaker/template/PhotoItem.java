package com.kessi.photopipcollagemaker.template;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoItem {
    public static final int SHRINK_METHOD_DEFAULT = 0;
    public static final int SHRINK_METHOD_3_3 = 1;
    public static final int SHRINK_METHOD_USING_MAP = 2;
    public static final int SHRINK_METHOD_3_6 = 3;
    public static final int SHRINK_METHOD_3_8 = 4;
    public static final int SHRINK_METHOD_COMMON = 5;
    public static final int CORNER_METHOD_DEFAULT = 0;
    public static final int CORNER_METHOD_3_6 = 1;
    public static final int CORNER_METHOD_3_13 = 2;
    //Primary info
    public float x = 0;
    public float y = 0;
    public int index = 0;
    public String imagePath;
    public String maskPath;
    //Using point list to construct view. All points and width, height are in [0, 1] range.
    public ArrayList<PointF> pointList = new ArrayList<>();
    public RectF bound = new RectF();
    //Using path to create
    public Path path = null;
    public RectF pathRatioBound = null;
    public boolean pathInCenterHorizontal = false;
    public boolean pathInCenterVertical = false;
    public boolean pathAlignParentRight = false;
    public float pathScaleRatio = 1;
    public boolean fitBound = false;
    //other info
    public boolean hasBackground = false;
    public int shrinkMethod = SHRINK_METHOD_DEFAULT;
    public int cornerMethod = CORNER_METHOD_DEFAULT;
    public boolean disableShrink = false;
    public HashMap<PointF, PointF> shrinkMap;
    //Clear polygon or arc area
    public ArrayList<PointF> clearAreaPoints;
    //Clear an area using path
    public Path clearPath = null;
    public RectF clearPathRatioBound = null;
    public boolean clearPathInCenterHorizontal = false;
    public boolean clearPathInCenterVertical = false;
    public boolean clearPathAlignParentRight = false;
    public float clearPathScaleRatio = 1;
    public boolean centerInClearBound = false;
}
