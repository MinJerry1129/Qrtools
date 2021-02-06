package com.kessi.photopipcollagemaker.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.concurrent.Callable;


/**
 * @see JavaBlurProcess Blur using the NDK and native code.
 */
class NativeBlurProcess implements BlurProcess {
	private static native void functionToBlur(Bitmap bitmapOut, int radius,
			int threadCount, int threadIndex, int round);

	static {
		System.loadLibrary("img_ps");
	}

	@Override
	public Bitmap blur(Bitmap original, float radius) {
		Bitmap bitmapOut = original.copy(Bitmap.Config.ARGB_8888, true);

		int cores = StackBlurManager.EXECUTOR_THREADS;

		ArrayList<NativeTask> horizontal = new ArrayList<NativeTask>(cores);
		ArrayList<NativeTask> vertical = new ArrayList<NativeTask>(cores);
		for (int i = 0; i < cores; i++) {
			horizontal
					.add(new NativeTask(bitmapOut, (int) radius, cores, i, 1));
			vertical.add(new NativeTask(bitmapOut, (int) radius, cores, i, 2));
		}

		try {
			StackBlurManager.EXECUTOR.invokeAll(horizontal);
		} catch (InterruptedException e) {
			return bitmapOut;
		}

		try {
			StackBlurManager.EXECUTOR.invokeAll(vertical);
		} catch (InterruptedException e) {
			return bitmapOut;
		}
		return bitmapOut;
	}

	private static class NativeTask implements Callable<Void> {
		private final Bitmap mBitmapOut;
		private final int mRadius;
		private final int mTotalCores;
		private final int mCoreIndex;
		private final int mRound;

		public NativeTask(Bitmap bitmapOut, int radius, int totalCores,
				int coreIndex, int round) {
			mBitmapOut = bitmapOut;
			mRadius = radius;
			mTotalCores = totalCores;
			mCoreIndex = coreIndex;
			mRound = round;
		}

		@Override
		public Void call() throws Exception {
			functionToBlur(mBitmapOut, mRadius, mTotalCores, mCoreIndex, mRound);
			return null;
		}

	}
}
