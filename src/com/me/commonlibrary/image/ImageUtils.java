package com.me.commonlibrary.image;
import com.me.commonlibrary.data.AppData;
import com.me.commonlibrary.utils.MyLog;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.content.res.Resources;

import javax.microedition.khronos.opengles.GL10;
public class ImageUtils {
    
        private static final MyLog LOG = new MyLog(ImageUtils.class);

        /**
         * Get the size in bytes of a bitmap.
         * 
         * @param bitmap
         * @return size in bytes
         */
      
        public static int getBitmapSize(Bitmap bitmap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                return bitmap.getByteCount();
            }
            // Pre HC-MR1
            return bitmap.getRowBytes() * bitmap.getHeight();
        }

        /**
         * Decode and sample down a bitmap from resources to the requested width and
         * height.
         * 
         * @param res The resources object containing the image data
         * @param resId The resource id of the image data
         * @param reqWidth The requested width of the resulting bitmap
         * @param reqHeight The requested height of the resulting bitmap
         * @return A bitmap sampled down from the original with the same aspect
         *         ratio and dimensions that are equal to or greater than the
         *         requested width and height(inMutable)
         */
        public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                int reqHeight) {
            return decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight, false);
        }

        /**
         * Decode and sample down a bitmap from resources to the requested width and
         * height.
         * 
         * @param res The resources object containing the image data
         * @param resId The resource id of the image data
         * @param reqWidth The requested width of the resulting bitmap
         * @param reqHeight The requested height of the resulting bitmap
         * @param isMutable 可编辑
         * @return A bitmap sampled down from the original with the same aspect
         *         ratio and dimensions that are equal to or greater than the
         *         requested width and height
         */
        public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                int reqHeight, boolean isMutable) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            if (isMutable && VERSION.SDK_INT >= 11) {
                options.inMutable = true;
            }
            Bitmap result = BitmapFactory.decodeResource(res, resId, options);
            if (isMutable) {
                result = createMutableBitmap(result);
            }
            return result;
        }

        public static Bitmap decodeSampledBitmapFromFile(String filePath, int sampledSize) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
            options.inSampleSize = sampledSize;

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }

        /**
         * Decode and sample down a bitmap from a file to the requested width and
         * height.
         * 
         * @param filePath The full path of the file to decode
         * @param reqWidth The requested width of the resulting bitmap
         * @param reqHeight The requested height of the resulting bitmap
         * @return A bitmap sampled down from the original with the same aspect
         *         ratio and dimensions that are equal to or greater than the
         *         requested width and height(inmutable)
         */
        public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
            return decodeSampledBitmapFromFile(filePath, reqWidth, reqHeight, false);
        }

        /**
         * Decode and sample down a bitmap from a file to the requested width and
         * height.
         * 
         * @param filePath The full path of the file to decode
         * @param reqWidth The requested width of the resulting bitmap
         * @param reqHeight The requested height of the resulting bitmap
         * @param isMutable 可编辑
         * @return A bitmap sampled down from the original with the same aspect
         *         ratio and dimensions that are equal to or greater than the
         *         requested width and height
         */
        public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight,
                boolean isMutable) {
            if (reqHeight == 0) {
                reqHeight = GL10.GL_MAX_TEXTURE_SIZE;
            }
            if (reqWidth == 0) {
                reqWidth = GL10.GL_MAX_TEXTURE_SIZE;
            }

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(filePath, options);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            if (isMutable && VERSION.SDK_INT >= 11) {
                options.inMutable = true;
            }

            Bitmap result = BitmapFactory.decodeFile(filePath, options);

            if (options.outHeight > GL10.GL_MAX_TEXTURE_SIZE
                    || options.outWidth > GL10.GL_MAX_TEXTURE_SIZE
                    || options.outHeight >= 3 * reqHeight || options.outWidth >= 3 * reqWidth) {
                result = cutBitmap(reqWidth, reqHeight, options, result);
            }

            if (isMutable) {
                result = createMutableBitmap(result);
            }

            return result;
        }

        private static Bitmap cutBitmap(int reqWidth, int reqHeight,
                final BitmapFactory.Options options, Bitmap result) {
            int cutWidth = options.outWidth;
            int cutHeight = options.outHeight;
            if (reqWidth < options.outWidth) {
                cutWidth = reqWidth;
            }
            if (reqHeight < options.outHeight) {
                cutHeight = reqHeight;
            }

            return Bitmap.createBitmap(result, 0, 0, cutWidth, cutHeight);
        }

        /**
         * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
         * object when decoding bitmaps using the decode* methods from
         * {@link BitmapFactory}. This implementation calculates the closest
         * inSampleSize that will result in the final decoded bitmap having a width
         * and height equal to or larger than the requested width and height. This
         * implementation does not ensure a power of 2 is returned for inSampleSize
         * which can be faster when decoding but results in a larger bitmap which
         * isn't as useful for caching purposes.
         * 
         * @param options An options object with out* params already populated (run
         *            through a decode* method with inJustDecodeBounds==true
         * @param reqWidth The requested width of the resulting bitmap
         * @param reqHeight The requested height of the resulting bitmap
         * @return The value to be used for inSampleSize
         */
        public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;

            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                int widthSampleSize = 0;
                int heightSampleSize = 0;
                if (reqWidth < width) {
                    widthSampleSize = Math.round((float) width / (float) reqWidth);
                }
                if (reqHeight < height) {
                    heightSampleSize = Math.round((float) height / (float) reqHeight);
                }
                inSampleSize = Math.max(widthSampleSize, heightSampleSize);
                // if (width > height) {
                // inSampleSize = Math.round((float) height / (float) reqHeight);
                // } else {
                // inSampleSize = Math.round((float) width / (float) reqWidth);
                // }

                // This offers some additional logic in case the image has a strange
                // aspect ratio. For example, a panorama may have a much larger
                // width than height. In these cases the total pixels might still
                // end up being too large to fit comfortably in memory, so we should
                // be more aggressive with sample down the image (=larger
                // inSampleSize).

                // final float totalPixels = width * height;
                //
                // // Anything more than 2x the requested pixels we'll sample down
                // // further.
                // float totalReqPixelsCap = reqWidth * reqHeight * 2;
                // while (totalPixels / (inSampleSize * inSampleSize) >
                // totalReqPixelsCap) {
                // inSampleSize++;
                // }
            }
            return inSampleSize;
        }

        /**
         * 通过srcbitmap 创建一个可编辑的bitmap
         * 
         * @param src
         * @return
         */
        public static Bitmap createMutableBitmap(Bitmap src) {
            Bitmap result = null;
            if (src == null) {
                return null;
            }
            if (src.isMutable()) {
                result = src;
            } else {
                result = src.copy(Config.ARGB_8888, true);
            }

            return result;
        }

        /**
         * 将subBmp图像合并到oriBmp中
         * 
         * @param oriBmp
         * @param subBmp
         * @param oriRect subBmp中取出的bitmap需要填充到oriRect中的区域
         * @param subRect 从subBmp中取出的区域
         * @param paint
         * @return
         */
        public static Bitmap mergeBitmap(Bitmap oriBmp, Bitmap subBmp, final Rect oriRect,
                final Rect subRect, final Paint paint) {
            if (subBmp == null) {
                return oriBmp;
            }

            if (oriBmp == null) {
                return null;
            }

            if (!oriBmp.isMutable()) {
                oriBmp = createMutableBitmap(oriBmp);
            }

            Canvas canvas = new Canvas(oriBmp);
            canvas.drawBitmap(subBmp, subRect, oriRect, paint);
            return oriBmp;
        }

        /**
         * 将subBmp图像合并到oriBmp中
         * 
         * @param oriBmp
         * @param subBmp
         * @param paint
         * @return oriBmp
         */
        public static Bitmap mergeBitmap(Bitmap oriBmp, Bitmap subBmp, final Paint paint) {
            if (subBmp == null) {
                return oriBmp;
            }

            if (oriBmp == null) {
                return null;
            }

            return mergeBitmap(oriBmp, subBmp, new Rect(0, 0, oriBmp.getWidth(), oriBmp.getHeight()),
                    new Rect(0, 0, subBmp.getWidth(), subBmp.getHeight()), paint);
        }

        private static final PorterDuffXfermode SRC_IN_MODE = new PorterDuffXfermode(
                PorterDuff.Mode.SRC_IN);

        /**
         * 遮罩图片
         * 
         * @param dstBmp
         * @param mask
         * @param paint
         * @return 遮罩后的图片
         */
        public static Bitmap maskBitmap(final Bitmap dstBmp, final Bitmap mask, final Paint paint) {
            if (dstBmp == null || mask == null) {
                return dstBmp;
            }
            Bitmap finalMask = convertToAlphaMask(mask);
            Bitmap result = Bitmap
                    .createBitmap(dstBmp.getWidth(), dstBmp.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,
                    Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            canvas.drawBitmap(finalMask, new Rect(0, 0, finalMask.getWidth(), finalMask.getHeight()),
                    new Rect(0, 0, dstBmp.getWidth(), dstBmp.getHeight()), paint);
            paint.setXfermode(SRC_IN_MODE);
            canvas.drawBitmap(dstBmp, 0, 0, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(sc);
            return result;
        }

        public static Bitmap convertToAlphaMask(Bitmap b) {
            Bitmap a = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ALPHA_8);
            Canvas c = new Canvas(a);
            c.drawBitmap(b, 0.0f, 0.0f, null);
            return a;
        }

        public static Bitmap decodeBitmapFromDrawableRes(int resId, final int width, final int height) {
            Drawable drawable = AppData.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.draw(canvas);
            return bitmap;
        }
}
