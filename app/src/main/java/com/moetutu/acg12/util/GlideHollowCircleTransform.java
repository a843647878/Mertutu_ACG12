package com.moetutu.acg12.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideHollowCircleTransform extends BitmapTransformation {
    Context mContext;
    int mColor;
    int mAngle;
    int mWidth;
    int mImg;
    boolean isImg=false;
    boolean isLine=false;
    /*
    有描边和图标
     */
    public GlideHollowCircleTransform(Context context, int color, int width, int angle, int img) {
        super(context);
        isImg=true;
        isLine=true;
        mContext=context;
        mColor=color;
        mWidth=width;
        mAngle=angle;
        mImg=img;
    }

    /*
    只有图标
     */
    public GlideHollowCircleTransform(Context context, int angle, int img, boolean isImg) {
        super(context);
        isImg=isImg;
        mContext=context;
        mAngle=angle;
        mImg=img;
    }

    /*
    只有描边
     */
    public GlideHollowCircleTransform(Context context, int color, int width) {
        super(context);
        mContext=context;
        mColor=color;
        mWidth=width;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap toTransform) {
        if (toTransform == null) return null;
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);


        if(isLine){
            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeWidth(mWidth);
            paint2.setAntiAlias(true);
            paint2.setColor(ContextCompat.getColor(mContext, mColor));
            canvas.drawCircle(r,r,r-mWidth,paint2);
        }


        if(isImg){
            Paint paint3 = new Paint();
            paint3.setAntiAlias(true);
            paint3.setFilterBitmap(true);
            paint3.setDither(true);
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),mImg);
            float sw =0.2f; //10/bmp.getWidth();
            float sh =0.2f ;//10/bmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(sw, sh);
            Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            float bmpX   =  (float)( r   +   r   *   Math.cos(mAngle   *   Math.PI  /180   ));
            float bmpY   = (float) ( r   +   r   *   Math.sin(mAngle   *   Math.PI  /180   ));
            canvas.drawBitmap(newbm,bmpX-newbm.getWidth()/2,bmpY-newbm.getHeight()/2,paint3);
        }



        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}