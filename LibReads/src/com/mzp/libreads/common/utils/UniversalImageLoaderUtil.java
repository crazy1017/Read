package com.mzp.libreads.common.utils;

import com.mzp.libreads.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

public class UniversalImageLoaderUtil {

	private static UniversalImageLoaderUtil instance;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public static UniversalImageLoaderUtil getInstance() {
		if (instance == null) {
			instance = new UniversalImageLoaderUtil();
		}
		return instance;
	}

    private UniversalImageLoaderUtil() {
    }

    ;

    public void showAvatarByUrl(ImageView imageView, String url) {
        showImageByUrl(imageView,  url, R.drawable.ic_launcher);
    }

    public void showAvatarByUrl(ImageView imageView, String url,int defaultResID, @NonNull ImageLoadingListener callback) {
        showImageByUrl(imageView,  url, defaultResID, callback);
    }

	public void showAvatarByUrlss(ImageView imageView, String url) {
		showImageByUrl(imageView,  url, R.drawable.ic_launcher);
	}

	public void showImageByUrl(ImageView imageView, String url, int defaultImgId) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(defaultImgId).showImageForEmptyUri(defaultImgId)
				.showImageOnFail(defaultImgId).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		if (!TextUtils.isEmpty(url) && !url.contains("null")) {
			imageLoader.displayImage(DecodeUtil.StrEncodeOrDecode(url, false), imageView, options);
		} else
			imageLoader.displayImage("drawable://" + defaultImgId, imageView);
	}

    public void showImageByUrl(ImageView imageView, String url, int defaultImgId, @NonNull ImageLoadingListener callback) {
        options = new DisplayImageOptions.Builder().showImageOnLoading(defaultImgId).showImageForEmptyUri(defaultImgId)
                .showImageOnFail(defaultImgId).cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        if (!TextUtils.isEmpty(url) && !url.contains("null")) {
            imageLoader.displayImage(DecodeUtil.StrEncodeOrDecode(url, false), imageView, options, callback);
        } else
            imageLoader.displayImage("drawable://" + defaultImgId, imageView);
    }

    public void showAvatarByUrls(ImageView imageView, String url) {
        showImageByUrl(imageView,  url, R.drawable.ic_launcher);
    }
}
