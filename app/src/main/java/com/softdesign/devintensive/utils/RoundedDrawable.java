package com.softdesign.devintensive.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;


public class RoundedDrawable extends Drawable {
    private final Bitmap mBitmap;
    private final Paint mPaint;
    private final RectF mRectF;
    private final int mBitmapWidth;
    private final int mBitmapHeight;


    public RoundedDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        mPaint = new Paint();
        mRectF = new RectF();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        final BitmapShader mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);

        //we assume bitmap is properly scaled to current density
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawOval(mRectF, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mRectF.set(bounds);
    }

    @Override
    public void setAlpha(int alpha) {
        if (mPaint.getAlpha() != alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public void setAntiAlias(boolean antiAlias) {
        mPaint.setAntiAlias(antiAlias);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mPaint.setDither(dither);
        invalidateSelf();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
