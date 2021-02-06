package com.kessi.photopipcollagemaker.shape;

import android.graphics.Bitmap;

public class ExtendBitmap {
	private int j;

	public Bitmap extendBitmapPixels(Bitmap bitmap, int progress, int x, int y,
			int w, int h) {
		w += x;
		h += y;
		float middleX = ((float) (x + w)) / 2.0f;
		float middleY = ((float) (y + h)) / 2.0f;
		Bitmap bitmap1 = bitmap.copy(bitmap.getConfig(), true);
		int midStartX = (int) (middleX - ((float) progress));
		int midEndX = (int) (((float) progress) + middleX);
		int midStartY = (int) (middleY - ((float) progress));
		int midEndY = (int) (((float) progress) + middleY);
		for (int p = 0; p < progress / 2; p++) {
			int shiftPixel = (int) (((float) progress) * 1.0f);
			if (middleX > ((float) (midStartX + shiftPixel))) {
				midStartX += shiftPixel;
			}
			if (middleX < ((float) (midEndX + shiftPixel))) {
				midEndX -= shiftPixel;
			}
			midStartY -= shiftPixel;
			midEndY += shiftPixel;
			boolean shrink = true;
			int i = x;
			while (i <= midStartX) {
				this.j = y - shiftPixel;
				while (this.j < h + shiftPixel) {
					if (this.j < midStartY) {
						if (shrink
								&& !shiftPixel(bitmap1, i - 1, this.j, i,
										this.j)) {
							break;
						}
					} else if (this.j <= midStartY || this.j >= midEndY) {
						if (this.j > midEndY
								&& shrink
								&& !shiftPixel(bitmap1, i - 1, this.j, i,
										this.j)) {
							break;
						}
					} else if (!shiftPixel(bitmap1, i - 1, this.j, i, this.j)) {
						break;
					}
					this.j++;
				}
				if (shrink) {
					shrink = false;
				} else {
					shrink = true;
				}
				i++;
			}
			shrink = true;
			i = w;
			while (i >= midEndX) {
				this.j = y - shiftPixel;
				while (this.j < h + shiftPixel) {
					if (this.j < midStartY) {
						if (shrink
								&& !shiftPixel(bitmap1, i + 1, this.j, i,
										this.j)) {
							break;
						}
					} else if (this.j <= midStartY || this.j >= midEndY) {
						if (this.j > midEndY
								&& shrink
								&& !shiftPixel(bitmap1, i + 1, this.j, i,
										this.j)) {
							break;
						}
					} else if (!shiftPixel(bitmap1, i + 1, this.j, i, this.j)) {
						break;
					}
					this.j++;
				}
				if (shrink) {
					shrink = false;
				} else {
					shrink = true;
				}
				i--;
			}
		}

		return bitmap1;
	}

	private boolean shiftPixel(Bitmap bitmap, int x1, int y1, int x2, int y2) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (x1 <= 0 || x2 <= 0 || y1 <= 0 || y2 <= 0 || w <= x1 || w <= x2
				|| h <= y1 || h <= y2) {
			System.out
					.println("(" + x1 + "," + y1 + ")(" + x2 + "," + y2 + ")");
			return false;
		}
		bitmap.setPixel(x1, y1, bitmap.getPixel(x2, y2));
		return true;
	}

}
