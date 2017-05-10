package com.mzp.libreads.player;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mzp.libreads.R;
import com.mzp.libreads.common.log.OperLog;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

@SuppressWarnings("deprecation")
public class VideoActivity extends AppCompatActivity implements OnClickListener, Callback//, SensorEventListener 
{

	private final static String TAG = VideoActivity.class.getName();
	private SurfaceView video_sfv_show;
	private Button video_btn_back;
	private Button video_btn_change;
	private Button video_btn_start;
	private int cameraPosition = CameraInfo.CAMERA_FACING_BACK;//0 后置摄像头
	private SurfaceHolder mHolder;
	private Camera camera;
	private String filepath;
	private boolean initFirstSensor = true;
	private boolean mStartedFlg = false;
	private MediaRecorder  mRecorder ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		verifyPermission(new String[]{Manifest.permission.CAMERA}); 
		getSupportActionBar().hide();
//		requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);//由重力感应器来决定屏幕的朝向
	        //SCREEN_ORIENTATION_BEHIND： 继承Activity堆栈中当前Activity下面的那个Activity的方向
	        //SCREEN_ORIENTATION_LANDSCAPE： 横屏(风景照) ，显示时宽度大于高度 
	        //SCREEN_ORIENTATION_PORTRAIT： 竖屏 (肖像照) ， 显示时高度大于宽度 
	        //SCREEN_ORIENTATION_SENSOR  由重力感应器来决定屏幕的朝向,它取决于用户如何持有设备,当设备被旋转时方向会随之在横屏与竖屏之间变化
	        //SCREEN_ORIENTATION_NOSENSOR： 忽略物理感应器——即显示方向与物理感应器无关，不管用户如何旋转设备显示方向都不会随着改变("unspecified"设置除外)
	        //SCREEN_ORIENTATION_UNSPECIFIED： 未指定，此为默认值，由Android系统自己选择适当的方向，选择策略视具体设备的配置情况而定，因此不同的设备会有不同的方向选择
	        //SCREEN_ORIENTATION_USER： 用户当前的首选方向
		setContentView(R.layout.activity_video);
		
		initView();
		initData();
	}

	private void initView() {
		video_sfv_show = (SurfaceView) findViewById(R.id.video_sfv_show);
		video_btn_back = (Button) findViewById(R.id.video_btn_back);
		video_btn_change = (Button) findViewById(R.id.video_btn_change);
		video_btn_start = (Button) findViewById(R.id.video_btn_start);
	}
	
	private void initData() {
		mHolder = video_sfv_show.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
		video_btn_back.setOnClickListener(this);
		video_btn_change.setOnClickListener(this);
		video_btn_start.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.video_btn_back://返回
			finish();
			break;
		case R.id.video_btn_change://切换摄像头
			//切换前后摄像头
            int cameraCount = 0;
            CameraInfo cameraInfo = new CameraInfo();
            cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

            for(int i = 0; i < cameraCount; i++  ) {
                Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                if(cameraPosition == 1) {
                    //现在是后置，变更为前置
                    if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                        camera.stopPreview();//停掉原来摄像头的预览
                        camera.release();//释放资源
                        camera = null;//取消原来摄像头
                        camera = Camera.open(i);//打开当前选中的摄像头
                        try {
                            camera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        camera.setDisplayOrientation(getPreviewDegree(VideoActivity.this));
                        camera.startPreview();//开始预览
                        cameraPosition = 0;
                        break;
                    }
                } else {
                    //现在是前置， 变更为后置
                    if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                        camera.stopPreview();//停掉原来摄像头的预览
                        camera.release();//释放资源
                        camera = null;//取消原来摄像头
                        camera = Camera.open(i);//打开当前选中的摄像头
                        try {
                            camera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        camera.setDisplayOrientation(getPreviewDegree(VideoActivity.this));
                        camera.startPreview();//开始预览
                        cameraPosition = 1;
                        break;
                    }
                }

            }
			break;
		case R.id.video_btn_start://开始
			initCamera();
			verifyPermission(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO });
			// 设置参数，并录像
			if (!mStartedFlg) {
				if (mRecorder == null) {
					mRecorder = new MediaRecorder();
				}
				try {
//					camera.stopPreview();
					camera.unlock();
//					mRecorder.reset();
					mRecorder.setCamera(camera);
					// Set audio and video source and encoder
					// 这两项需要放在setOutputFormat之前
					mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
					// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4  
					mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
					CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
					mRecorder.setProfile(camcorderProfile);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					//设置audio的编码格式
		            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					 // 设置录制的视频编码h263 h264  
					mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
					//设置video的编码格式
//		            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		            //设置录制的视频编码比特率
		            mRecorder.setVideoEncodingBitRate(1024 * 1024);
		            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错 
		            //设置录制的视频帧率,注意文档的说明:
		            mRecorder.setVideoFrameRate(30);
//		            Parameters parameters = camera.getParameters();
//		            List<Size> supportedVideoSizes = parameters.getSupportedVideoSizes();
//		            List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
//		            //设置要捕获的视频的宽度和高度
//		            mHolder.setFixedSize(supportedPreviewSizes.get(0).width, supportedPreviewSizes.get(0).height);//最高只能设置640x480
//		            mRecorder.setVideoSize(supportedVideoSizes.get(0).width, supportedVideoSizes.get(0).height);//最高只能设置640x480
					mRecorder.setPreviewDisplay(mHolder.getSurface());

					// Set output file path
					String path = getSDPath();
					if (path != null) {

						File dir = new File(path + "/VideoRecorderTest");
						if (!dir.exists()) {
							dir.mkdir();
						}
						path = dir + "/" + getDate() + ".mp4";
						mRecorder.setOutputFile(path);
						OperLog.error(TAG, "bf mRecorder.prepare()");
						mRecorder.prepare();
						OperLog.error(TAG, "af mRecorder.prepare()");
						mRecorder.start(); // Recording is now
											// started
						OperLog.error(TAG, "af mRecorder.start()");
						mStartedFlg = true;
						video_btn_start.setText("stop");
					}
				} catch (Exception e) {
					e.printStackTrace();
					OperLog.error(TAG, e.toString());
				}
			} else {
				// stop
				if (mStartedFlg) {
					try {
						mRecorder.stop();
						mRecorder.reset();
					} catch (Exception e) {
						e.printStackTrace();
						OperLog.error(TAG, e.toString());
					}
					video_btn_start.setText("start");
				}
				mStartedFlg = false;
			}
			break;

		default:
			break;
		}
	}
	
	/**初始化相机*/
	private void initCamera(){
		if(camera == null){
			camera = Camera.open();
            OperLog.error(TAG, "camera.open");
        }
        if(camera != null ) {
            try {
            	Parameters params = camera.getParameters();
            	params.setPictureFormat(PixelFormat.JPEG);//图片格式
            	int PreviewWidth = 0;  
            	int PreviewHeight = 0; 
            	List<Camera.Size> sizeList = params.getSupportedPreviewSizes();  
            	
            	// 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择  
            	if (sizeList.size() > 1) {  
            		Iterator<Camera.Size> itor = sizeList.iterator();  
            		while (itor.hasNext()) {  
            			Camera.Size cur = itor.next();  
            			if (cur.width >= PreviewWidth  && cur.height >= PreviewHeight) {  
            				PreviewWidth = cur.width;  
            				PreviewHeight = cur.height;  
            				break;
            			}  
            		}  
            	}  
            	params.setPreviewSize(PreviewWidth, PreviewHeight);//预览图片大小
//                params.setPreviewFrameRate(5);  //设置每秒显示4帧  
            	if (Build.VERSION.SDK_INT >22) {
            		params.setPictureSize(PreviewWidth, PreviewHeight);//保存图片大小
            	}
            	params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//录像模式
            	params.setJpegQuality(100); 
//                params.set("jpeg-quality", 100);
            	camera.setParameters(params);//将参数设置到我的camera
            	camera.setPreviewDisplay(mHolder);
            	camera.setDisplayOrientation(getPreviewDegree(VideoActivity.this));
            	camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                OperLog.error(TAG, e.toString());
            }
        }
        
	}
	
	/**
     * 获取SD path
     */
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist){
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }
    
    /**
     * 获取系统时间，保存文件以系统时间戳命名
     */
    public static String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);          
        int month = ca.get(Calendar.MONTH);         
        int day = ca.get(Calendar.DATE);            
        int minute = ca.get(Calendar.MINUTE);       
        int hour = ca.get(Calendar.HOUR);         
        int second = ca.get(Calendar.SECOND);     

        String date = "" + year + (month + 1 )+ day + hour + minute + second;
        OperLog.error(TAG, "date:" + date);

        return date;
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//当surfaceview创建时开启相机
        mHolder = holder;
        initCamera();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//当surfaceview关闭时，关闭预览并释放资源
        camera.stopPreview();
        camera.release();
        camera = null;
        holder = null;
        video_sfv_show = null;
	}
	// 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度  
    public  int getPreviewDegree(Activity activity) {  
        // 获得手机的方向  
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();  
        int degree = 0;  
        // 根据手机的方向计算相机预览画面应该选择的角度  
        switch (rotation) {  
        case Surface.ROTATION_0:  
            degree = 90;  
            break;  
        case Surface.ROTATION_90:  
            degree = 0;  
            break;  
        case Surface.ROTATION_180:  
            degree = 270;  
            break;  
        case Surface.ROTATION_270:  
            degree = 180;  
            break;  
        }  
        return degree;  
    }  
	
	 //创建jpeg图片回调数据对象
    PictureCallback jpeg = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            try {
            	verifyPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE});
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //自定义文件保存路径  以拍摄时间区分命名
                filepath = Environment.getExternalStorageDirectory()+"/messages/"  + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +  ".jpg";
                File file = new File(filepath);
                
                if (!new File(Environment.getExternalStorageDirectory()+"/messages").exists()) {
                	new File(Environment.getExternalStorageDirectory()+"/messages").mkdirs();
				}
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                 // 定义矩阵对象  
                Matrix matrix = new Matrix();  
                // 缩放原图  
                matrix.postScale(1f, 1f);  
                // 向左旋转45度，参数为正则向右旋转  
                matrix.postRotate(90);  
                
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                createBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
                bos.flush();// 刷新此缓冲区的输出流
                bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
                camera.stopPreview();//关闭预览 处理数据
                camera.startPreview();//数据处理完后继续开始预览
                bitmap.recycle();//回收bitmap空间
                createBitmap.recycle();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                OperLog.error(TAG, e.toString());
                
            }
        }
    };
	

	public void verifyPermission(String[] permissions) {  
        if (permissions != null) {  
            List<String> lists = new ArrayList<>();  
            for (int i = 0; i < permissions.length; i++) {  
                if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {  
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {  
  
                    }  
                    lists.add(permissions[i]);  
                }  
            }  
            if (lists.size() > 0) {  
                String[] ps = new String[lists.size()];  
                for (int i = 0; i < lists.size(); i++) {  
                    ps[i] = lists.get(i);  
                }  
                ActivityCompat.requestPermissions(this, ps, 1);  
            }  
        }  
    }  
}
