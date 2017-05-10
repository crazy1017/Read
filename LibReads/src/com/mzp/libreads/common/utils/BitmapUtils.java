package com.mzp.libreads.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.mzp.libreads.common.AppCache;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

/**
 * The Class BitmapUtils.
 */
@SuppressLint("NewApi")
public class BitmapUtils {

    /** The be. */
    private static int be = 1;

    /** The Constant THUMB_LIMIT. */
    public static final int THUMB_LIMIT = 4 * 1024;

    /** The Constant maxWith. */
    private static final int maxWidth = 400;

    /** The Constant maxHeight. */
    private static final int maxHeight = 300;

    /** The Constant default_message_icon_select_video. */
    public static Bitmap default_message_icon_select_video = null;

    /** The Constant default_contacts_icon_person_image. */
    public static Bitmap default_contacts_icon_person_image = null;

    /**
     * fitSizeImage
     * 
     * @param path
     *            the path
     * @return the bitmap
     */
    public static Bitmap fitSizeImage(String path) {
        if (path == null || path.length() < 1) {
            return null;
        }
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();

        long fileLength = file.length();
        if (fileLength < 1024 * 1024) {
            be = 1;
        } else if (fileLength >= 1024 * 1024 && fileLength < 2 * 1024 * 1024) {
            be = 4;
        } else if (fileLength >= 2 * 1024 * 1024 && fileLength < 4 * 1024 * 1024) {
            be = 6;
        } else {
            be = 8;
        }

        if (be <= 0) {
            be = 1;
        }
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        opts.inSampleSize = be;

        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);

        return resizeBmp;
    }

    /**
     * 相片编码成base64 String.
     * 
     * @param bitmap
     *            the bitmap
     * @return the string
     */
    public static String bitmaptoString(Bitmap bitmap) {

        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(CompressFormat.PNG, 100, bStream);

        byte[] bytes = bStream.toByteArray();

        string = Base64.encodeToString(bytes, Base64.NO_WRAP);

        return string;

    }

    /**
     * Compute sample size.
     * 
     * @version
     * @param options
     *            the options
     * @param minSideLength
     *            the min side length
     * @param maxNumOfPixels
     *            the max num of pixels
     * @return the int
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     * Compute initial sample size.
     * 
     * @version
     * @param options
     *            the options
     * @param minSideLength
     *            the min side length
     * @param maxNumOfPixels
     *            the max num of pixels
     * @return the int
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 
     * @param options
     *            the options
     * @param reqWidth
     *            the reqWidth
     * @param reqHeight
     *            the reqHeight
     * @return int
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    /**
     * 
     * @param filePath
     *            the filePath
     * @param reqWidth
     *            the reqWidth
     * @param reqHeight
     *            the reqHeight
     * @return bitmap
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 
     * 获取高分辨率的缩略图 Gets the image thumbnail.
     * 
     * @param imagePath
     *            the image path
     * @return the image thumbnail
     */
    // public static Bitmap getImageThumbnail(String imagePath) {
    // Bitmap bitmap = null;
    // BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = true;
    // // 获取这个图片的宽和高，注意此处的bitmap为null
    // bitmap = BitmapFactory.decodeFile(imagePath, options);
    // options.inJustDecodeBounds = false; // 设为 false
    // // 计算缩放比
    // File file = new File(imagePath);
    // int be = 1;
    // if (file.exists()) {
    // long fileLength = file.length();
    // if (fileLength < 10 * 1024) {
    // be = 1;
    //
    // } else {
    // be = (int) fileLength / 10240;
    // }
    // }
    // if (be <= 0) {
    // be = 1;
    // }
    // options.inSampleSize = be;
    // // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
    // bitmap = BitmapFactory.decodeFile(imagePath, options);
    // if (bitmap != null) {
    // // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
    // bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(),
    // bitmap.getHeight(),
    // ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    // }
    // return bitmap;
    // }

    /**
     * 
     * @param imagePath
     *            the imagePath
     * @return bitmap
     */
    public static Bitmap getCreateThumbnailOfImage(String imagePath) {
        Bitmap localBitmap;

        // int degree = readPictureDegree(imagePath);
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, localOptions);
        // localOptions.inSampleSize = computeSampleSize(localOptions, -1,
        // PIXEL_LIMIT);
        localOptions.inSampleSize = computeSampleSize1(localOptions, maxWidth, maxHeight);
        localOptions.inJustDecodeBounds = false;
        try {
            localBitmap = BitmapFactory.decodeFile(imagePath, localOptions);
            // if (degree != 0)
            // localBitmap = rotaingImageView(degree, localBitmap);
            // ImageSize size = getImageSize(localBitmap, maxWidth, maxHeight,
            // maxWidth, maxHeight, false);
            // if (size != null) {
            // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
            // localBitmap = ThumbnailUtils.extractThumbnail(localBitmap,
            // maxWidth, maxHeight,
            // ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            localBitmap = BitmapUtils.newCreatemap(localBitmap, maxWidth, maxHeight);
            // }
        } catch (OutOfMemoryError localOutOfMemoryError) {
            //OperLog.info(TAG, "getOrCreateThumbnailOfImage out of memory");
            localBitmap = null;
        }
        return localBitmap;
    }

    /**
     * 
     * 获取图片缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @param reqWidth
     *            the reqWidth
     * @param reqHeight
     *            the reqHeight
     * @param maxSize
     *            the maxSize
     * 
     * @return the image thumbnail
     */
    public static Bitmap getImageThumb(String fullFileName, int reqWidth, int reqHeight, int maxSize) {
        if (StringUtil.isEmpty(fullFileName)) {
            return null;
        }
        Bitmap bitmap = getSmallBitmap(fullFileName, reqWidth, reqHeight);
        if (bitmap != null) {
//            bitmap = compressImage(bitmap, MessageDSConstantsCode.MESSAGE_LOCAL_THUMB_SIZE);

        }
        return bitmap;
    }

    /* 质量压缩方法 压缩到指定大小(xxk) */
    /**
     * 
     * @param image
     *            the image
     * @return inputstream
     */
    private static InputStream compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > THUMB_LIMIT) { // 循环判断如果压缩后图片是否大于xxkb,大于继续压缩
            options -= 5;// 每次都减少10
            if (options < 0) {
                break;
            }
            baos.reset();// 重置baos即清空baos

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
        // 把ByteArrayInputStream数据生成图片
        return isBm;
    }

    /* 质量压缩方法 压缩到指定大小(xxk) */
    /**
     * 
     * @param image
     *            the image
     * @param maxSize
     *            the maxSize
     * @return bitmap
     */
    private static Bitmap compressImage(Bitmap image, int maxSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > maxSize) { // 循环判断如果压缩后图片是否大于xxkb,大于继续压缩
            options -= 5;// 每次都减少10
            if (options < 0) {
                break;
            }
            baos.reset();// 重置baos即清空baos

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//

    }

    /**
     * 图片按比例大小压缩
     * 
     * @param imagePath
     *            the imagePath
     * 
     * @return the InputStream
     */
    public static InputStream compressBitmap(String imagePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = maxHeight;// 这里设置高度为800f
        float ww = maxWidth;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.outWidth = maxWidth;
        newOpts.outHeight = maxHeight;

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imagePath, newOpts);

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /* 图片按比例大小压缩 */
    /**
     * 
     * @param image
     *            the image
     * @return inputstream
     */
    public static InputStream compressBitmap(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) { // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = maxHeight;// 这里设置高度为800f
        float ww = maxWidth;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.outWidth = maxWidth;
        newOpts.outHeight = maxHeight;

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * Bitmap to file with limit.
     * 
     * @version
     * @param paramBitmap
     *            the param bitmap
     * @return the input stream
     */
    public static InputStream bitmapToFileWithLimit(Bitmap paramBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int quality = 100;
        while (quality >= 0) {
            baos.reset();
            paramBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 5;
            if (baos.size() < THUMB_LIMIT) {
                break;
            }
        }

        return new ByteArrayInputStream(baos.toByteArray());

    }

    /**
     * Bitmap to file with limit.
     * 
     * @version
     * @param paramBitmap
     *            the param bitmap
     * @param maxSize
     *            the maxSize
     * 
     * @return the input stream
     */
    public static InputStream bitmapToFileWithLimit(Bitmap paramBitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        while (quality >= 0) {
            baos.reset();
            paramBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 5;
            if (baos.size() < maxSize) {
                break;
            }
        }

        return new ByteArrayInputStream(baos.toByteArray());

    }

    /**
     * Gets the image thumbnail.
     * 
     * @param imagePath
     *            the image path
     * @return the image thumbnail
     */
    public static Bitmap getImageThumbnail(String imagePath) {
        if (StringUtil.isEmpty(imagePath)) {
            return null;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        File file = new File(imagePath);
        if (file.exists()) {
            long fileLength = file.length();
            if (fileLength < 1024 * 1024) {
                be = 1;
            } else if (fileLength >= 1024 * 1024 && fileLength < 2 * 1024 * 1024) {
                be = 4;
            } else if (fileLength >= 2 * 1024 * 1024 && fileLength < 4 * 1024 * 1024) {
                be = 6;
            } else {
                be = 8;
            }
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        if (bitmap != null) {
            // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            // bitmap = getCornerPhotoBitmap(bitmap, 30);
        }
        return bitmap;
    }

    /**
     * 
     * 获取带圆角的缩略图 Gets the image thumbnail.
     * 
     * @param imagePath
     *            the image path
     * @return the image thumbnail
     */
    public static Bitmap getCornerImageThumbnail(String imagePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inJustDecodeBounds = true;
        // // 获取这个图片的宽和高，注意此处的bitmap为null
        // bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // // 计算缩放比
        // File file = new File(imagePath);
        // if (file.exists()) {
        // long fileLength = file.length();
        // if (fileLength < 1024 * 1024) {
        // be = 1;
        // } else if (fileLength >= 1024 * 1024 && fileLength < 2 * 1024 * 1024)
        // {
        // be = 4;
        // } else if (fileLength >= 2 * 1024 * 1024 && fileLength < 4 * 1024 *
        // 1024) {
        // be = 6;
        // } else {
        // be = 8;
        // }
        // }
        // if (be <= 0) {
        // be = 1;
        // }
        // options.inSampleSize = be;
        // // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // if (bitmap != null) {
        // // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        // bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300,
        // ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        //
        // bitmap = getCornerPhotoBitmap(bitmap, 30);
        // }
        return bitmap;
    }

    /**
     * Gets the options.
     * 
     * @param filePath
     *            the file path
     * @return the options
     */
    public static Bitmap getImageThumb(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return null;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        File file = new File(filePath);
        FileInputStream f = null;
        try {
            f = new FileInputStream(file);
            FileDescriptor fd = f.getFD();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);

            options.inSampleSize = BitmapUtils.computeSampleSize(options, 100, 100 * 100);

            options.inJustDecodeBounds = false;
        } catch (FileNotFoundException e) {
            options.inSampleSize = 4;// 将图片大小改为原来的1/16
        } catch (IOException e) {
            options.inSampleSize = 4;// 将图片大小改为原来的1/16
        } finally {
            if (null != f) {
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        bitmap = BitmapFactory.decodeFile(filePath, options);

        return bitmap;
    }

    /**
     * 获取圆角位图的方法.
     * 
     * @param bitmap
     *            需要转化成圆角的位图
     * @param pixels
     *            圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap getCornerPhotoBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        // final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        // paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return output;
    }

    /**
     * 把bitmap转成圆形.
     * 
     * @version
     * @param bitmap
     *            the bitmap
     * @return the bitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        // 取最短边做边长
        if (width < height) {
            r = width;
        } else {
            r = height;
        }
        // 构建一个bitmap
        Bitmap backgroundBm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        // new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        // 设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        // 且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, p);
        // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }

    /**
     * Gets the mms image.
     * 
     * @param path
     *            the path
     * @return the mms image
     */
    public static Bitmap getMmsImage(String path) { // 读取图片附件
        Context context = AppCache.getContext();
        ContentResolver resolver = context.getContentResolver();
        Uri partURI = Uri.parse(path);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = resolver.openInputStream(partURI);
            if (is == null) {
                //OperLog.error(TAG, "whb:null InputStream");
                return bitmap;
            }
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            //OperLog.error(TAG, "whb:" + "读取图片异常" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //OperLog.error(TAG, "whb:" + "读取图片异常" + e.getMessage());
                }
            }
        }
        return bitmap;
    }

    /**
     * Bitmap2 input stream.
     * 
     * @version
     * @param bm
     *            the bm
     * @param quality
     *            the quality
     * @return the input stream
     */
    public static InputStream bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     * 
     * @param imagePath
     *            图像的路径
     * @param width
     *            指定输出图像的宽度
     * @param height
     *            指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        // int degree = readPictureDegree(imagePath);
        // if (degree > 0 && bitmap != null) {
        // bitmap = rotaingImageView(degree, bitmap);
        // }
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            default:
                break;
            }
        } catch (IOException e) {

        }
        return degree;
    }

    /**
     * 旋转图片
     * 
     * @param angle
     *            the angle
     * 
     * @param bitmap
     *            the bitmap
     * 
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。.
     * 
     * @param videoPath
     *            视频的路径
     * @param width
     *            指定输出视频缩略图的宽度
     * @param height
     *            指定输出视频缩略图的高度度
     * @param kind
     *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap == null) {
            bitmap = default_message_icon_select_video;
        } else {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * 
     * 获取图片缩略图路径.
     * 
     * @param videoPath
     *            the video path
     * @param maxSize
     *            the maxSize
     * 
     * @return the image thumbnail
     */
    public static Bitmap getVideoThumb(String videoPath, int maxSize) {
        if (StringUtil.isEmpty(videoPath)) {
            return null;
        }
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
        if (bitmap != null) {
            InputStream in = BitmapUtils.bitmapToFileWithLimit(bitmap, maxSize);
            bitmap = BitmapFactory.decodeStream(in, null, null);
        }
        return bitmap;
    }

    /**
     * 
     * 获取带圆角的缩略图 Gets the image thumbnail.
     * 
     * @param imagePath
     *            the image path
     * @return the image thumbnail
     */
    public static Bitmap getVideFirstThumbnail(String imagePath) {
        Bitmap bitmap = null;
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(imagePath);
            bitmap = media.getFrameAtTime();// 第一帧图像
        } catch (RuntimeException e) {

        }
        return bitmap;
    }

    /**
     * 
     * 获取视频缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @return the image thumbnail
     */
    public static String getVideoSendThumbpath(String fullFileName) {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        // InputStream in = bitmapToFileWithLimit(bitmap);
        InputStream in = newVideoCompresspath(fullFileName);
        if (in == null) {
            return "";
        }

//        String mFileThumbPath = SdkConstants.FULLPATH_RCS_THUMBNAIL_SEND_VIDEO;// 缩略图路径
        String mFileThumbName = "thumb_" + currentTime + ".jpg";// 缩略图文件名称
//        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径

        try {
            // 写收到的图片文件到指定路径
//            FileUtil.writeFile(mFileThumbPath, mFileThumbName, in);
        } catch (Exception e) {
            return "";
        }
//        return fullFileThumbPath;
        return null;
    }

    /**
     * 
     * 获取视频缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @param maxSize
     *            the maxSize
     * 
     * @return the image thumbnail
     */
    public static String getVideoSendThumbpath(String fullFileName, int maxSize) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        Bitmap bitmap = getVideFirstThumbnail(fullFileName);
        if (bitmap == null) {
            return "";
        }
        bitmap = newCreatemap(bitmap, 300, 200);
        InputStream in = bitmapToFileWithLimit(bitmap, maxSize);
//        String mFileThumbPath = SdkConstants.FULLPATH_RCS_THUMBNAIL_SEND_VIDEO;// 缩略图路径
        String mFileThumbPath = null;// 缩略图路径
        String mFileThumbName = "thumb_" + currentTime + ".jpg";// 缩略图文件名称
        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径

        try {
            // 写收到的图片文件到指定路径
            FileUtil.writeFile(mFileThumbPath, mFileThumbName, in);
        } catch (IOException e) {
            return "";
        }
        return fullFileThumbPath;
    }

    /**
     * 
     * 获取视频缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @return the image thumbnail
     */
    public static String getVideoReceiveThumbpath(String fullFileName) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        Bitmap bitmap = getVideFirstThumbnail(fullFileName);
        if (bitmap == null) {
            return "";
        }
        bitmap = newCreatemap(bitmap, 300, 200);
//        InputStream in = bitmapToFileWithLimit(bitmap, MessageDSConstantsCode.MESSAGE_LOCAL_THUMB_SIZE);
        InputStream in = bitmapToFileWithLimit(bitmap);
        // InputStream in = BitmapUtils.bitmap2InputStream(bitmap, 100);
//        String mFileThumbPath = SdkConstants.FULLPATH_RCS_THUMBNAIL_RECIEVE_VIDEO;// 缩略图路径
        String mFileThumbPath = null;// 缩略图路径
        String mFileThumbName = "thumb_" + currentTime + ".jpg";// 缩略图文件名称
        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径

        try {
            // 写收到的图片文件到指定路径
            FileUtil.writeFile(mFileThumbPath, mFileThumbName, in);
        } catch (IOException e) {
            return "";
        }
        return fullFileThumbPath;
    }

    /**
     * 
     * 获取图片缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @return the image thumbnail
     */
    public static String getPhotoSendThumbpath(String fullFileName) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        // Bitmap bitmap = BitmapUtils.getCreateThumbnailOfImage(fullFileName);
        // if (bitmap == null) {
        // return "";
        // }
        // InputStream in = BitmapUtils.bitmapToFileWithLimit(bitmap);
        InputStream in = newPhotoCompresspath(fullFileName);
//        String mFileThumbPath = SdkConstants.FULLPATH_RCS_THUMBNAIL_SEND;// 缩略图路径
        String mFileThumbPath = null;// 缩略图路径
        String mFileThumbName = "thumb_" + currentTime + fullFileName.substring(fullFileName.lastIndexOf("."));// 缩略图路径
        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径
        try {
            // 写收到的图片文件到指定路径
            FileUtil.writeFile(mFileThumbPath, mFileThumbName, in);
        } catch (IOException e) {
            return "";
        }
        return fullFileThumbPath;
    }

    /**
     * 
     * 获取图片缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @return the image thumbnail
     */
    public static String getPhotoReceiveThumbpath(String fullFileName) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        Bitmap bitmap = BitmapUtils.getCreateThumbnailOfImage(fullFileName);
        int degree = readPictureDegree(fullFileName);
        if (degree > 0 && bitmap != null) {
            bitmap = rotaingImageView(degree, bitmap);
        }
        if (bitmap == null) {
            return "";
        }
//        InputStream in = BitmapUtils.bitmapToFileWithLimit(bitmap, MessageDSConstantsCode.MESSAGE_LOCAL_THUMB_SIZE);
        InputStream in = BitmapUtils.bitmapToFileWithLimit(bitmap);
//        String mFileThumbPath = SdkConstants.FULLPATH_RCS_THUMBNAIL_RECIEVE;// 缩略图路径
        String mFileThumbPath = null;// 缩略图路径
        String mFileThumbName = "thumb_" + currentTime + fullFileName.substring(fullFileName.lastIndexOf("."));// 缩略图路径
        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径
        try {
            // 写收到的图片文件到指定路径
            FileUtil.writeFile(mFileThumbPath, mFileThumbName, in);
        } catch (IOException e) {
            return "";
        }
        return fullFileThumbPath;
    }

    /*
     * public static String getPhotoReceiveThumbpath(String fullFileName) {
     * 
     * long currentTime = Calendar.getInstance().getTimeInMillis(); InputStream
     * in =BitmapUtils.newCompresspath(fullFileName); String mFileThumbPath =
     * SdkConstants.FULLPATH_RCS_THUMBNAIL_RECIEVE;// 缩略图路径 String
     * mFileThumbName = "thumb_" + currentTime +
     * fullFileName.substring(fullFileName.lastIndexOf("."));// 缩略图路径 String
     * fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径 try { //
     * 写收到的图片文件到指定路径 FileUtil.writeFile(mFileThumbPath, mFileThumbName, in); }
     * catch (IOException e) { return ""; } return fullFileThumbPath; }
     */

    /**
     * 
     * 获取图片缩略图路径.
     * 
     * @param fullFileName
     *            the image path
     * @param maxSize
     *            the maxSize
     * 
     * @return the image thumbnail
     */
    public static String getPhotoSendThumbpath(String fullFileName, int maxSize) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        Bitmap bitmap = BitmapUtils.getCreateThumbnailOfImage(fullFileName);
        if (bitmap == null) {
            return "";
        }
        InputStream in = BitmapUtils.bitmapToFileWithLimit(bitmap, maxSize);
//        String mFileThumbPath = SdkConstants.FULLPATH_RCS_THUMBNAIL_SEND;// 缩略图路径
        String mFileThumbPath = null;// 缩略图路径
        String mFileThumbName = "thumb_" + currentTime + fullFileName.substring(fullFileName.lastIndexOf("."));// 缩略图路径
        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 缩略图全路径
        try {
            // 写收到的图片文件到指定路径
            FileUtil.writeFile(mFileThumbPath, mFileThumbName, in);
        } catch (IOException e) {
            return "";
        }
        return fullFileThumbPath;
    }

    /**
     * 处理图片
     * 
     * @param bm
     *            所要转换的bitmap
     * @param newWidth
     *            新的宽
     * @param newHeight
     *            新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 
     * @param bkg
     *            the bkg
     * @param view
     *            the view
     * @param context
     *            the context
     * @return bitmap
     */
    public static Bitmap blur(Bitmap bkg, View view, Context context) {
        Bitmap overlay = null;
        try {
            long startMs = System.currentTimeMillis();
            float scaleFactor = 1;//
            float radius = 20;//
            overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(0, 0);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);

//            overlay = FastBlur.doBlur(overlay, (int) radius, true);
            view.setBackground(new BitmapDrawable(context.getResources(), overlay));

            Log.i("jerome", "blur time:" + (System.currentTimeMillis() - startMs));
            return overlay;
        } catch (Exception e) {
            // TODO: handle exception
            //OperLog.error(TAG, "blur" + e);
        } finally {
            overlay = null;
        }
        return overlay;
    }

    /**
     * 
     * 获得圆角图片
     * 
     * 
     * Gets the corner photo bitmap.
     * 
     * @param bitmap
     *            the bitmap
     * @param roundPx
     *            the round px
     * @return the corner photo bitmap
     */
    public static Bitmap getCornerPhotoBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 相片按相框的比例动态缩放
     * 
     * @param bmp
     *            the bmp
     * @param width
     *            模板宽度
     * @param height
     *            模板高度
     * @return bitmap
     */
    public static Bitmap scaleImageSize(Bitmap bmp, int width, int height) {
        if (bmp == null) {
            return null;
        }
        // 计算比例
        float scaleX = (float) width / bmp.getWidth();// 宽的比例
        float scaleY = (float) height / bmp.getHeight();// 高的比例
        // 新的宽高
        int newW = 0;
        int newH = 0;
        if (scaleX > scaleY) {
            newW = (int) (bmp.getWidth() * scaleX);
            newH = (int) (bmp.getHeight() * scaleX);
        } else if (scaleX <= scaleY) {
            newW = (int) (bmp.getWidth() * scaleY);
            newH = (int) (bmp.getHeight() * scaleY);
        }
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }

    /**
     * 在二维码中间添加Logo图案
     */
    /**
     * 
     * @param src
     *            the src
     * @param logo
     *            the logo
     * @return bitmap
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (OutOfMemoryError e) {
            System.gc();
            //OperLog.error(TAG, "OutOfMemoryError " + e);
        } catch (RuntimeException e) {
            // TODO: handle exception
           // OperLog.error(TAG, "RuntimeException " + e);
        }

        return bitmap;
    }

    /**
     * 转二维码
     * 
     * @param matrix
     *            the matrix
     * @return bitmap
     */
//    public static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
//        int w = matrix.getWidth();
//        int h = matrix.getHeight();
//        int[] rawData = new int[w * h];
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                int color = Color.WHITE;
//                if (matrix.get(i, j)) {
//                    color = Color.BLACK;
//                }
//                rawData[i + (j * w)] = color;
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.RGB_565);
//        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
//        return bitmap;
//    }

    /**
     * 生成二维码
     * 
     * @param content
     *            the content
     * @return bitmap
     */
//    public static Bitmap generateQRCode(String content) {
//        try {
//            // 内容转码的工作在传入参数前执行
//            QRCodeWriter writer = new QRCodeWriter();
//
//            // Hashtable hints = new Hashtable();
//            // hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//            // MultiFormatWriter writer = new MultiFormatWriter();
//            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 500, 500);
//            return bitMatrix2Bitmap(matrix);
//        } catch (WriterException e) {
//            //OperLog.error(TAG, "WriterException " + e);
//        }
//        return null;
//    }

    /**
     * 根据原始图片数据，计算需要生成的缩略图大小(宽、高)
     * 
     * @param bitmap
     *            图片数据
     * @param maxWidth
     *            the maxWidth
     * @param maxHeight
     *            the maxHeight
     * @param minWidth
     *            the minWidth
     * @param minHeight
     *            the minHeight
     * @param videoflag
     *            the videoflag
     * 
     * @return imagesize
     */
    public static ImageSize getImageSize(Bitmap bitmap, int maxWidth, int maxHeight, int minWidth, int minHeight,
            boolean videoflag) {
        ImageSize imageSize = new ImageSize();
        if (null == bitmap || bitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteTmp = baos.toByteArray();

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteTmp, 0, byteTmp.length, bitmapOptions);
        int outWidth = bitmapOptions.outWidth;
        int outHeight = bitmapOptions.outHeight;
        imageSize.setWidth(outWidth);
        imageSize.setHeight(outHeight);
        /*
         * if (outWidth / maxWidth > outHeight / maxHeight) {// if (outWidth >=
         * maxWidth) {// imageSize.setWidth(maxWidth);
         * imageSize.setHeight(outHeight * maxWidth / outWidth); } else {
         * imageSize.setWidth(outWidth); imageSize.setHeight(outHeight); } if
         * (outHeight < minHeight) { imageSize.setHeight(minHeight); int width =
         * outWidth * minHeight / outHeight; if (width > maxWidth) {
         * imageSize.setWidth(maxWidth); } else { imageSize.setWidth(width); } }
         * } else { if (outHeight >= maxHeight) {
         * imageSize.setHeight(maxHeight); imageSize.setWidth(outWidth *
         * maxHeight / outHeight); } else { imageSize.setHeight(outHeight);
         * imageSize.setWidth(outWidth); } if (outWidth < minWidth) {
         * imageSize.setWidth(minWidth); int height = outHeight * minWidth /
         * outWidth; if (height > maxHeight) { imageSize.setHeight(maxHeight); }
         * else { imageSize.setHeight(height); } } }
         * 
         * if (videoflag) { if (imageSize.getHeight() < minHeight) {
         * imageSize.setHeight(minHeight); } if (imageSize.getWidth() <
         * minWidth) { imageSize.setWidth(minWidth); } }
         */

        return imageSize;
    }

    /**
     * Gets the round corner image.
     * huo qu yuan jue tu xiang
     * @param bitmap_bg
     *            the bitmap_bg
     * @param bitmap_in
     *            the bitmap_in
     * @return the round corner image
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap_bg, Bitmap bitmap_in) {
        Bitmap roundConcerImage = null;
        try {
            roundConcerImage = Bitmap.createBitmap(bitmap_in.getWidth(), bitmap_in.getHeight(), Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            while (roundConcerImage == null) {
                System.gc();
                System.runFinalization();
                return null;
            }
        }
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap_in, rectF, rect, paint);
        return roundConcerImage;
    }

    /**
     * Gets the shard image.
     * 
     * @param bitmap_bg
     *            the bitmap_bg
     * @param bitmap_in
     *            the bitmap_in
     * @return the shard image
     */
    public static Bitmap getShardImage(Bitmap bitmap_bg, Bitmap bitmap_in) {
        Bitmap roundConcerImage = Bitmap.createBitmap(bitmap_in.getWidth(), bitmap_in.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        Rect rect2 = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        canvas.drawBitmap(bitmap_in, rect, rect2, paint);
        return roundConcerImage;
    }

    /**
     * 
     * @param beforebitmap
     *            the beforebitmap
     * 
     * @param newwidth
     *            the newwidth
     * 
     * @param newheight
     *            the newheight
     * 
     * @return bitmap
     */
    public static Bitmap newCreatemap(Bitmap beforebitmap, int newwidth, int newheight) {
        int beforewidth = beforebitmap.getWidth();
        int beforeheight = beforebitmap.getHeight();
        float scalewidth = 0;
        float scaleheight = 0;
        if (beforewidth > beforeheight) {
            scalewidth = ((float) newwidth) / beforewidth;
            scaleheight = ((float) newheight) / beforeheight;
        } else {
            scalewidth = ((float) newwidth) / beforeheight;
            scaleheight = ((float) newheight) / beforewidth;
        }
        Matrix matrix = new Matrix();
        if (scalewidth > scaleheight) {
            matrix.postScale(scalewidth, scalewidth);
        } else {
            matrix.postScale(scaleheight, scaleheight);
        }
        Bitmap afterbitmap = Bitmap.createBitmap(beforebitmap, 0, 0, beforewidth, beforeheight, matrix, true);
        return afterbitmap;
    }

    /**
     * 
     * @param srcPath
     *            the srcPath
     * 
     * @return inputstream
     */
    public static InputStream newVideoCompresspath(String srcPath) {
        // DisplayMetrics dm = new DisplayMetrics();
        // WindowManager windowManager = (WindowManager)
        // Context.getSystemService(Context.WINDOW_SERVICE);
        // getWindowManager().getDefaultDisplay().getMetrics(dm);

        Bitmap bitmap = getVideFirstThumbnail(srcPath);
        if (bitmap == null) {
            return null;
        }
        bitmap = newCreatemap(bitmap, 300, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > THUMB_LIMIT) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 5;
        }
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return isBm;
    }

    /**
     * 
     * @param srcPath
     *            the srcPath
     * @return inputstream
     */
    public static InputStream newPhotoCompresspath(String srcPath) {
        // DisplayMetrics dm = new DisplayMetrics();
        // WindowManager windowManager = (WindowManager)
        // Context.getSystemService(Context.WINDOW_SERVICE);
        // getWindowManager().getDefaultDisplay().getMetrics(dm);
        float hh = maxHeight;
        float ww = maxWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
            
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        // 对图片角度进行读取
        // int degree = readPictureDegree(srcPath);
        // if (degree != 0) {
        // bitmap = rotaingImageView(degree, bitmap);
        // }
        bitmap = newCreatemap(bitmap, 300, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > THUMB_LIMIT && quality>0) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 5;
        }
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return isBm;
    }

    /**
     * 
     * @param options
     *            the options
     * @param maxWidth
     *            the maxWidth
     * @param maxHeight
     *            the maxHeight
     * @return int
     */
    public static int computeSampleSize1(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        float hh = maxHeight;
        float ww = maxWidth;
        int w = options.outWidth;
        int h = options.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        return size;
    }

    /**
     * 
     * @param fullFileName
     *            the fullFileName
     * @return inputstream
     */
    public static InputStream sourceBitmap(String fullFileName) {
        int degeree = BitmapUtils.readPictureDegree(fullFileName);
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fullFileName, localOptions);
        localOptions.inSampleSize = 2;
        localOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(fullFileName, localOptions);
        if (degeree != 0) {
            bitmap = BitmapUtils.rotaingImageView(degeree, bitmap);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        InputStream in = new ByteArrayInputStream(baos.toByteArray());
        return in;
    }

    /**
     * Drawable → Bitmap
     */

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

        drawable.getIntrinsicWidth(),

        drawable.getIntrinsicHeight(),

        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        // canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
}
