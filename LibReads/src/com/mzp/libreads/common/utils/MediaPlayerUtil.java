package com.mzp.libreads.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.mzp.libreads.common.log.OperLog;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.SurfaceHolder;


/**
 * The Class MediaPlayerUtil.
 */
public class MediaPlayerUtil {

	/** The Constant TAG. */
	private static final String TAG = MediaPlayerUtil.class.getName();

	/** The mediaplayer. */
	private MediaPlayer mediaplayer = null;

	private static MediaPlayerUtil instance = new MediaPlayerUtil();

	/**
	 * 获取工厂类的单例对象.
	 * 
	 * @return single instance of RcsManagerFactory
	 */
	public static MediaPlayerUtil getInstance() {
		return instance;
	}

	/**
	 * 构造方法.
	 */
	private MediaPlayerUtil() {

	}

	/**
	 * 
	 * 播放来电音
	 * 
	 * Start in coming ring.
	 */
	public void startInComingRing(Context context) {
		String ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
		init(ringtone, context);
		startPlayingAudio(true);
	}

	/**
	 * 根据手机号查询来电铃声类型 缺省类型为1
	 * 
	 * @param context
	 * @param phoneNum
	 */
	public void startInComingRingFromPhoneNum(Context context, String phoneNum) {
		int ringType = 1;
		if (!TextUtils.isEmpty(phoneNum)) {
			if (phoneNum.startsWith("+86")) {
				phoneNum = phoneNum.replace("+86", "");
			}
//			ContactVos cv = ContactCacheUtil.getInstance().searchContactByPhone(phoneNum);
//			if (cv != null) {
//				ringType = cv.getRingintype();
//			}
		}
		setCustomRingType(context, ringType);
	}
	
	public void setCustomRingType(Context context, int ringType){
		String ringPath = "";
		switch (ringType) {
		case 2:
			ringPath = "ringintype_2.wav";
			break;

		case 3:
			ringPath = "ringintype_3.wav";
			break;

		default:
			ringPath = "ringin.wav";
			break;
		}
		
		if (mediaplayer != null) {
			return;
		}
		mediaplayer = new MediaPlayer();
		AssetManager assetManager = context.getAssets();
		AssetFileDescriptor afd;
		try {
			afd = assetManager.openFd(ringPath);
			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
			mediaplayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		startPlayingAudio(true);
	}

	/**
	 * 播放去电音
	 * 
	 * Start out going ring.
	 */
	public void startOutGoingRing(Context context) {
		String ringFilePath = context.getFilesDir().getAbsolutePath() + "/ringback.wav";
		OperLog.info(TAG, "ringFilePath=" + ringFilePath);

		init(ringFilePath, context);
		startPlayingAudio(true);
	}

	/**
	 * 播放去电广告音
	 * 
	 * Start out going advert ring.
	 */
	public void startOutGoingAdvertRing(Context context) {
		String ringFilePath = context.getFilesDir().getAbsolutePath() + "/call_wait.wav";
		OperLog.info(TAG, "ringFilePath=" + ringFilePath);

		init(ringFilePath, context);
		startPlayingAudio(true);
	}

	/**
	 * 播放呼叫等待来电提示音
	 * 
	 * Start waiting ring.
	 */
	public void startWaitingRing(Context context) {
		String ringFilePath = context.getFilesDir().getAbsolutePath() + "/call_wait.wav";
		OperLog.info(TAG, "ringFilePath=" + ringFilePath);

		init(ringFilePath, context);
		startPlayingAudio(true);
	}

	public void startVideo(SurfaceHolder sh, String videoFile, Context context) {
		OperLog.info(TAG, "videoFile=" + videoFile);

		init(videoFile, context);
		startPlayingVideo(sh, false);
	}

	/**
	 * Inits the.
	 * 
	 * @param mediaPath
	 *            the media path
	 */
	private void init(String mediaPath, Context context) {
		FileInputStream fis = null;
		try {
			OperLog.info(TAG, "MediaPlayerUtil---->path=" + mediaPath);

			// if (mediaplayer != null) {
			// stopPlaying();
			// }
			if (mediaplayer != null) {
				return;
			}
			mediaplayer = new MediaPlayer();
			if (mediaPath.startsWith("content://")) {
				mediaplayer.setDataSource(context, Uri.parse(mediaPath));
				mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			} else {
				File file = new File(mediaPath);
				fis = new FileInputStream(file);

				mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaplayer.setDataSource(fis.getFD());
			}

			mediaplayer.prepare();
			if (fis != null) {
				fis.close();
			}

		} catch (Exception e) {
			OperLog.error(TAG, TAG + e.getMessage(), e);
			mediaplayer = null;
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					OperLog.error(TAG, TAG + "fis.close();" + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Sets the data source.
	 * 
	 * @param mediaPath
	 *            the new data source
	 */
	private void setDataSource(String mediaPath) {
		try {
			OperLog.info(TAG, "MediaPlayerUtil---->path=" + mediaPath);
			if (null != mediaPath) {
				mediaplayer.setAudioStreamType(AudioManager.STREAM_RING);
				mediaplayer.setDataSource(mediaPath);
				mediaplayer.prepare();
			}
		} catch (Exception e) {
			OperLog.error(TAG, TAG + "setDataSource Exception " + e.getMessage(), e);
		}
	}

	/**
	 * Start playing audio.
	 * 
	 * @param isLooping
	 *            the is looping
	 */
	private void startPlayingAudio(boolean isLooping) {
		try {
			if (null != mediaplayer) {
				mediaplayer.setLooping(isLooping);
				mediaplayer.start();
			}
		} catch (Exception e) {
			OperLog.error(TAG, TAG + "startPlayingAudio Exception " + e.getMessage(), e);
		}
	}

	/**
	 * Start playing video.
	 * 
	 * @param sh
	 *            the sh
	 * @param isLooping
	 *            the is looping
	 */
	private void startPlayingVideo(SurfaceHolder sh, boolean isLooping) {
		try {
			if (null != mediaplayer) {
				mediaplayer.setDisplay(sh);
				mediaplayer.setLooping(isLooping);
				mediaplayer.start();
			}
		} catch (Exception e) {
			OperLog.error(TAG, TAG + "startPlayingVideo Exception " + e.getMessage(), e);
		}
	}

	/**
	 * Stop playing.
	 */
	public void stopPlaying() {
		OperLog.info(TAG, "stopPlaying...");
		if (null != mediaplayer) {
			// try {
			// mediaplayer.stop();
			// mediaplayer.release();
			// mediaplayer = null;
			// } catch (Exception e) {
			// OperLog.error(TAG, TAG + "stopPlaying Exception " +
			// e.getMessage(), e);
			// }
			try {
				mediaplayer.setLooping(false);
				mediaplayer.stop();
			} catch (RuntimeException e) {
				OperLog.error(TAG, TAG + "mediaplayer.stop error. " + e.getMessage(), e);
			}

			try {
				mediaplayer.release();
			} catch (RuntimeException e) {
				OperLog.error(TAG, TAG + "mediaplayer.release error. " + e.getMessage(), e);
			} finally {
				mediaplayer = null;
				// ringFilePath = "";
			}
		}
	}
}
