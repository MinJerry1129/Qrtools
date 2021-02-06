package com.kessi.photopipcollagemaker.utils;

import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StackBlurManager {
    static final int EXECUTOR_THREADS = Runtime.getRuntime()
            .availableProcessors();
    static ExecutorService EXECUTOR = Executors
            .newFixedThreadPool(EXECUTOR_THREADS);

    /**
     * Original image
     */
    private final Bitmap mImage;

    /**
     * Most recent result of blurring
     */
    private Bitmap mResult;

    /**
     * Method of blurring
     */
    private final BlurProcess mBlurProcess;

    /**
     * Constructor method (basic initialization and construction of the pixel
     * array)
     *
     * @param image
     *            The image that will be analyed
     */
    public StackBlurManager(Bitmap image) {
        if (EXECUTOR == null || EXECUTOR.isShutdown()) {
            EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);
        }

        mImage = image;
        mBlurProcess = new JavaBlurProcess();
    }

    public static void shutdownExecutor() {
        if (EXECUTOR != null && !EXECUTOR.isShutdown()) {
            EXECUTOR.shutdown();
        }
    }

    /**
     * Process the image on the given radius. Radius must be at least 1
     *
     * @param radius
     */
    public Bitmap process(int radius) {
        mResult = mBlurProcess.blur(mImage, radius);
        return mResult;
    }

    /**
     * Returns the blurred image as a bitmap
     *
     * @return blurred image
     */
    public Bitmap returnBlurredImage() {
        return mResult;
    }

    /**
     * Save the image into the file system
     *
     * @param path
     *            The path where to save the image
     */
    public void saveIntoFile(String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            mResult.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the original image as a bitmap
     *
     * @return the original bitmap image
     */
    public Bitmap getImage() {
        return this.mImage;
    }

    /**
     * Process the image using a native library
     */
    public Bitmap processNatively(int radius) {
        NativeBlurProcess blur = new NativeBlurProcess();
        mResult = blur.blur(mImage, radius);
        return mResult;
    }
}

