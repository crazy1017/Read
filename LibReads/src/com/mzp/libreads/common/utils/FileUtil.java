package com.mzp.libreads.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;


/**
 * The Class FileUtil.
 */
public class FileUtil {

    /** The Constant TAG. */
    private static final String TAG = "FileUtil";

    /** The event list. */
    public static List<IEvent> eventList = new LinkedList<IEvent>();

    /** The SDPATH. */
    private static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * Adds the event.
     * 
     * @param event
     *            the event
     * 
     */
    public static synchronized void addEvent(IEvent event) {
        eventList.add(0, event);

    }

    /**
     * Removes the event.
     * 
     * 
     */
    public static synchronized void removeEvent() {
        eventList.remove(0);
    }

    /**
     * Deal event.
     * 
     * 
     */
    public static synchronized void dealEvent() {
        if (eventList != null && eventList.size() > 0) {
            IEvent event = eventList.get(0);
            event.execute();
        }

    }

    /**
     * The Interface IEvent.
     */
    public interface IEvent {

        /**
         * Execute.
         * 
         * 
         */
        void execute();
    }

    /**
     * 私有文件保存内容.
     * 
     * @param context
     *            the context
     * @param fileName
     *            文件名称
     * @param content
     *            文件内容
     * @throws Exception
     *             the exception
     * 
     */
    public static void save(Context context, String fileName, String content) throws Exception {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(content.getBytes());
        fos.close();
    }
    
    /**
     * 读文件.
     * 
     * @param file
     *            file
     * @return StringBuffer
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * 
     */
    public static StringBuffer readFile(AssetManager assertmgr, String fileName) throws IOException {
        if (fileName == null) {
            Log.i(TAG, "Illegal  Argument!");
            throw new IllegalArgumentException("fileName is null.");
        }

        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferReader = null;
        try {
            InputStream in = assertmgr.open(fileName);
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            bufferReader = new BufferedReader(isr);
            // 缓冲数组
            char[] b = new char[1024 * 5];
            int len;
            while ((len = bufferReader.read(b)) != -1) {
                stringBuffer.append(new String(b));
                b = new char[1024 * 5];
            }

        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    // OperLog.error(TAG, "", e);
                } catch (RuntimeException e) {
                    // OperLog.error(TAG, "", e);
                }
            }
        }

        return stringBuffer;
    }

    /**
     * 读取文件内容.
     * 
     * @param context
     *            the context
     * @param fileName
     *            文件名称
     * @return the string
     * @throws Exception
     *             the exception
     * 
     */
    public static String readFile(Context context, String fileName) throws Exception {
        FileInputStream fis = context.openFileInput(fileName);
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray(); // 得到文件的二进制数据
        baos.close();
        fis.close();
        return new String(data, 0, data.length);
    }

    /**
     * 读取文件二进制内容.
     * 
     * @param fileName
     *            文件名称
     * @return the byte[]
     * 
     */
    public static byte[] readByteFile(String fileName) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] data = null;
        try {
            File file = new File(fileName);
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            baos = new ByteArrayOutputStream();

            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            // 得到文件的二进制数据
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 读取文本文件内容.
     * 
     * @param fileName
     *            文件名称
     * @return the String
     * 
     */
    public static String readTxtFile(String fileName) {
        InputStreamReader isr = null;
        String str = "";
        try {
            File urlFile = new File(fileName);
            isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String mimeTypeLine = null;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
            if(br != null){
            	br.close();
            }

        } catch (Exception e) {
            str = "";
        } finally {

            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (Exception e) {

            }
        }

        return str;
    }

    /**
     * 根据文件绝对路径获取路径.
     * 
     * @param filePath
     *            the file path
     * @return the file path
     * @return
     */
    public static String getFilePath(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        }
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    /**
     * 根据文件绝对路径获取文件名.
     * 
     * @param filePath
     *            the file path
     * @return the file name
     * @return
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名.
     * 
     * @param filePath
     *            the file path
     * @return the file name no format
     * @return
     */
    public static String getFileNameNoFormat(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);
    }

    /**
     * 获取文件扩展名.
     * 
     * @param fileName
     *            the file name
     * @return the file format
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return "";
        }

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 按照目录的路径删除文件夹.
     * 
     * @param dirPath
     *            目录的路径
     * 
     */
    public static void deleteDirectory(String dirPath) {
        File dirFile = new File(dirPath);

        // 检查目录是否存在及是否为目录
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }

        File[] fileList = dirFile.listFiles();
        if (fileList == null) {
            //OperLog.error(TAG, "deleteDirectory_fileList == null");
            return;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                deleteDirectory(fileList[i].getAbsolutePath());
            } else {
                deleteFile(fileList[i]);
            }
        }

        dirFile.delete();
    }

    /**
     * 在某个目录下删除特定后缀的文件.
     * 
     * @param fileDir
     *            the file dir
     * @param suffix
     *            the suffix
     * @return true, if successful
     * @return
     */
    public static boolean deleteDirectory(String fileDir, String suffix) {
        //OperLog.debug(TAG, "[deleteDirectory] fileDir=" + fileDir + "; suffix=" + suffix);
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileDir.equals("")) {
            File newPath = new File(fileDir);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listFile = newPath.list();
                try {
                    for (int i = 0; i < listFile.length; i++) {
                        String fileName = listFile[i];
                        if (fileName.lastIndexOf(suffix) > 0) {
                            String deleteFileName = fileDir + fileName;
                            File deletedFile = new File(deleteFileName);
                            if (deletedFile.isFile() && deletedFile.exists()) {
                                boolean ret = deletedFile.delete();
                                //OperLog.debug(TAG, "deleted file=" + deleteFileName + "; ret=" + ret);
                            }
                        }
                    }
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    /**
     * 按照目录的路径删除文件夹下的文件.
     * 
     * @param dirPath
     *            目录的路径
     * 
     */
    public static void deleteFilesInDirectory(String dirPath) {
        File dirFile = new File(dirPath);

        // 检查目录是否存在及是否为目录
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }

        File[] fileList = dirFile.listFiles();
        if (fileList == null) {
            //OperLog.error(TAG, "deleteFIleInDirectory_fileList == null");
            return;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (!fileList[i].isDirectory()) {
                deleteFile(fileList[i]);
            }
        }
    }

    /**
     * 按文件对象删除一个目录.
     * 
     * @param dirFile
     *            目录文件对象
     * 
     */
    public static void deleteDirectory(File dirFile) {
        if (dirFile == null) {
            return;
        }

        // 检查目录是否存在及是否为目录
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }

        File[] fileList = dirFile.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                deleteDirectory(fileList[i]);
            } else {
                deleteFile(fileList[i]);
            }
        }

        dirFile.delete();
    }

    /**
     * 按文件对象删除一个目录.
     * 
     * @param dirFile
     *            目录文件对象
     * 
     */
    public static void deleteFilesInDirectory(File dirFile) {
        if (dirFile == null) {
            return;
        }

        // 检查目录是否存在及是否为目录
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }

        File[] fileList = dirFile.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (!fileList[i].isDirectory()) {
                deleteFile(fileList[i]);
            }
        }
    }

    /**
     * 删除一个文件.
     * 
     * @param filePath
     *            文件路径
     * 
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * 删除一个文件.
     * 
     * @param file
     *            文件对象
     * 
     */
    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }

        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * 文件夹到文件夹的拷贝.
     * 
     * @param srcDirPath
     *            the src dir path
     * @param destDirPath
     *            the dest dir path
     * 
     */
    public static void copyDirToDir(String srcDirPath, String destDirPath) {
        File srcDirFile = new File(srcDirPath);
        // 判断原目录是否存在且是否为目录
        if (!srcDirFile.exists() || !srcDirFile.isDirectory()) {
            return;
        }

        File destDirFile = new File(destDirPath);
        // 判断目标目录是否存在,是否为目录
        if (destDirFile.exists()) {
            if (!destDirFile.isDirectory()) {
                return;
            }
        } else {
            destDirFile.mkdirs(); // 不存在就创建
        }

        String tempDestDirPath = destDirFile.getAbsolutePath();

        File[] files = srcDirFile.listFiles();
        if (files == null) {
            //OperLog.error(TAG, "copyDirToDir_files == null");
            return;
        }
        int count = files.length;
        for (int i = 0; i < count; i++) {
            // 是目录
            if (files[i].isDirectory()) {
                copyDirToDir(files[i].getAbsolutePath(), tempDestDirPath + "/" + files[i].getName());
            } else {
                copyFileToFile(files[i], tempDestDirPath + "/" + files[i].getName());
            }
        }

    }

    /**
     * 文件夹到文件夹的拷贝.
     * 
     * @param srcDirFile
     *            the src dir file
     * @param destDirPath
     *            the dest dir path
     * 
     */
    public static void copyDirToDir(File srcDirFile, String destDirPath) {
        if (srcDirFile == null) {
            return;
        }

        // 判断原目录是否存在且是否为目录
        if (!srcDirFile.exists() || !srcDirFile.isDirectory()) {
            return;
        }

        File destDirFile = new File(destDirPath);
        // 判断目标目录是否存在,是否为目录
        if (destDirFile.exists()) {
            if (!destDirFile.isDirectory()) {
                return;
            }
        } else {
            destDirFile.mkdirs(); // 不存在就创建
        }

        String tempDestDirPath = destDirFile.getAbsolutePath();

        File[] files = srcDirFile.listFiles();
        int count = files.length;
        for (int i = 0; i < count; i++) {
            // 是目录
            if (files[i].isDirectory()) {
                copyDirToDir(files[i], tempDestDirPath + "/" + files[i].getName());
            } else {
                copyFileToFile(files[i], tempDestDirPath + "/" + files[i].getName());
            }
        }

    }

    /**
     * 文件拷贝.
     * 
     * @param srcPath
     *            源文件路径
     * @param destPath
     *            the dest path
     * 
     */
    public static void copyFileToFile(String srcPath, String destPath) {
        int readBufferSize = 1024;

        File srcfile = new File(srcPath);
        if (!srcfile.exists()) {
            return;
        }

        File destfile = new File(destPath);

        // 源文件输入流
        BufferedInputStream bisFrom = null;
        // 目标文件输出流
        BufferedOutputStream bisTo = null;

        try {
            bisFrom = new BufferedInputStream(new FileInputStream(srcfile));
            bisTo = new BufferedOutputStream(new FileOutputStream(destfile));

            byte[] buffer = new byte[readBufferSize]; // byte buffer
            int bytes_read;

            while ((bytes_read = bisFrom.read(buffer)) != -1) {
                bisTo.write(buffer, 0, bytes_read);
            }
        } catch (IOException e) {
            //OperLog.error(TAG, "copyfile error = " + e.getMessage());
        } finally {
            try {
                // 关闭输入流
                if (bisFrom != null) {
                    bisFrom.close();
                }
                // 关闭输出流
                if (bisTo != null) {
                    bisTo.close();
                }

            } catch (IOException e) {
                // 如果出错,置为null,分配的内存由垃圾回收处理
                bisFrom = null;
                bisTo = null;
                //OperLog.error(TAG, "copyfile error = " + e.getMessage());
            }
        }
    }

    /**
     * 文件拷贝.
     * 
     * @param srcfile
     *            源文件
     * @param destfile
     *            目标文件
     * 
     */
    public static void copyFileToFile(File srcfile, File destfile) {
        if (srcfile == null || !srcfile.exists() || destfile == null) {
            return;
        }

        int readBufferSize = 1024;

        // 源文件输入流
        BufferedInputStream bisFrom = null;
        // 目标文件输出流
        BufferedOutputStream bisTo = null;

        try {
            bisFrom = new BufferedInputStream(new FileInputStream(srcfile));
            bisTo = new BufferedOutputStream(new FileOutputStream(destfile));

            byte[] buffer = new byte[readBufferSize]; // byte buffer
            int bytes_read;

            while ((bytes_read = bisFrom.read(buffer)) != -1) {
                bisTo.write(buffer, 0, bytes_read);
            }
        } catch (IOException e) {
            //OperLog.error(TAG, "copyfile error = " + e.getMessage());
        } finally {
            try {
                // 关闭输入流
                if (bisFrom != null) {
                    bisFrom.close();
                }
                // 关闭输出流
                if (bisTo != null) {
                    bisTo.close();
                }

            } catch (IOException e) {
                // 如果出错,置为null,分配的内存由垃圾回收处理
                bisFrom = null;
                bisTo = null;
                //OperLog.error(TAG, "copyfile error = " + e.getMessage());
            }
        }
    }

    /**
     * 文件拷贝.
     * 
     * @param srcfile
     *            源文件
     * @param destPath
     *            目标文件目录
     * 
     */
    public static void copyFileToFile(File srcfile, String destPath) {
        if (srcfile == null) {
            return;
        }

        int readBufferSize = 1024;

        File destfile = new File(destPath);

        // 源文件输入流
        BufferedInputStream bisFrom = null;
        // 目标文件输出流
        BufferedOutputStream bisTo = null;

        try {
            bisFrom = new BufferedInputStream(new FileInputStream(srcfile));
            bisTo = new BufferedOutputStream(new FileOutputStream(destfile));

            byte[] buffer = new byte[readBufferSize]; // byte buffer
            int bytes_read;

            while ((bytes_read = bisFrom.read(buffer)) != -1) {
                bisTo.write(buffer, 0, bytes_read);
            }
        } catch (IOException e) {
            //OperLog.error(TAG, "copyfile error = " + e.getMessage());
        } finally {
            try {
                // 关闭输入流
                if (bisFrom != null) {
                    bisFrom.close();
                }
                // 关闭输出流
                if (bisTo != null) {
                    bisTo.close();
                }

            } catch (IOException e) {
                // 如果出错,置为null,分配的内存由垃圾回收处理
                bisFrom = null;
                bisTo = null;
                //OperLog.error(TAG, "copyfile error = " + e.getMessage());
            }
        }
    }

    /**
     * 获取SD卡路径.
     * 
     * @return the sDPATH
     * @return
     */
    public static String getSDPATH() {
        return SDPATH;
    }

    /**
     * 创建文件.
     * 
     * @param fileName
     *            已经包含"/"
     * @return the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @return
     */
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 创建文件夹.
     * 
     * @param dirName
     *            已经包含"/"
     * @return the file
     * @return
     */
    public static File createSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 文件是否存在.
     * 
     * @param fileName
     *            the file name
     * @return true, if is file exist
     * @return
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + File.separator + fileName);
        return file.exists();
    }

    /**
     * Checks if is file exist.
     * 
     * @param path
     *            the path
     * @param fileName
     *            the file name
     * @return true, if is file exist
     */
    public static boolean isFileExist(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        return file.exists();
    }

    /**
     * 写文件到SD卡.
     * 
     * @param path
     *            the path
     * @param fileName
     *            the file name
     * @param input
     *            the input
     * @return the file
     * @return
     */
    public static File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 读取文件每一行.
     * 
     * @param txtFileName
     *            the txt file name
     * @return the lines from file
     * @return
     */
    public static ArrayList<String> getLinesFromFile(String txtFileName) {
        ArrayList<String> tempList = new ArrayList<String>();
        FileReader fr = null;
        try {
            fr = new FileReader(txtFileName);
            BufferedReader bufferedreader = new BufferedReader(fr);
            String tempString;
            while ((tempString = bufferedreader.readLine()) != null) {
                if (tempString.length() != 0) {
                    tempList.add(tempString);
                }
            }
            bufferedreader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempList;
    }

    /**
     * 生成vcard文件
     * 
     * @param fullpath
     * @param filename
     * @param contactinfo
     * @throws IOException
     */
   /* public static void createVCardFile(String fullpath, String filename, ContactStruct contact) throws IOException {
        if (fullpath == null || fullpath.trim().equals("") || filename == null || filename.trim().equals("")) {
            Log.i(TAG, "Illegal  Argument!");
            throw new IllegalArgumentException();
        }

        OutputStreamWriter writer = null;
        try {
            File p = new File(fullpath);
            if (!p.exists()) {
                p.mkdirs();
            }
            File file = new File(fullpath, filename);
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

            // create vCard representation
            VCardComposer composer = new VCardComposer();
            String vcardString;
            vcardString = composer.createVCard(contact, VCardComposer.VERSION_VCARD30_INT);
            // write vCard to the output stream
            writer.write(vcardString);
            writer.write("\n");
            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (VCardException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) { //
                e.printStackTrace();
            }
        }
    }*/
    /**
     * 获取文件带单位的大小
     */
    public static String getFormetFileSize(String filePath){
    	File file = new File(filePath);
        String size = ""; 
        if(file.exists() && file.isFile()){
        	long fileS = file.length();
        	DecimalFormat df = new DecimalFormat("#.00"); 
               if (fileS < 1024) {
                   size = df.format((double) fileS) + "BT";
               } else if (fileS < 1048576) {
                   size = df.format((double) fileS / 1024) + "KB";
               } else if (fileS < 1073741824) {
                   size = df.format((double) fileS / 1048576) + "MB";
               } else {
                   size = df.format((double) fileS / 1073741824) +"GB";
               }
        }else if(file.exists() && file.isDirectory()){
        	size = "未知";
        }else{
        	size = "0BT";
        }
        return size;
       }
    /**
     * 获取文件指定文件的指定单位的大小.
     * 
     * @param filePath
     *            文件路径
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 转换文件大小,指定转换的类型.
     * 
     * @param fileS
     *            the file s
     * @return the double
     */
    public static double FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
        return fileSizeLong;
    }

    /**
     * 获取指定文件夹.
     * 
     * @param f
     *            the f
     * @return the file sizes
     * @throws Exception
     *             the exception
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小.
     * 
     * @param file
     *            the file
     * @return the file size
     * @throws Exception
     *             the exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        FileInputStream fis = null;
        try {
            if (file.exists()) {

                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
            // TODO: handle exception
            //OperLog.error(TAG, "getFileSize " + e);
        } finally {
            if (null != fis) {
                fis.close();
            }

        }

        return size;
    }

   /* public static ContactStruct parseVCardFile(String vcardfilename) throws Exception {
        VCardParser parse = new VCardParser();
        VDataBuilder builder = new VDataBuilder();
        ContactStruct contactstruct = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(vcardfilename), "UTF-8"));

        String vcardString = "";
        String line;
        while ((line = reader.readLine()) != null) {
            vcardString += line + "\n";
        }
        reader.close();

        boolean parsed = parse.parse(vcardString, "UTF-8", builder);

        if (!parsed) {
            throw new VCardException("Could not parse vCard file: " + vcardfilename);
        }

        List<VNode> pimContacts = builder.vNodeList;

        for (VNode contact : pimContacts) {

            contactstruct = ContactStruct.constructContactFromVNode(contact, 1);
            return contactstruct;

        }
        return contactstruct;
    }
*/
    /**
     * 按规定的压缩原图质量大小后复制到目标地址.
     * 
     * @param sourceFile
     *            the source file
     * @param targetFile
     *            the target file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void copyFileWithLimitCompressionRatio(String sourceFullFile, String targetFile) throws IOException {
        if (sourceFullFile == null || targetFile == null) {
            Log.i(TAG, "Illegal  Argument!");
            throw new IllegalArgumentException();
        }
        File sourceFile = new File(sourceFullFile);
        float sourceFileSize = sourceFile.length();
        float imageCompressionRatio = 400;
        float limitSzie = imageCompressionRatio * 1024;
        if (sourceFileSize <= limitSzie) {
            copyFile(sourceFile, targetFile);
        } else {
            // FileInputStream fis = new FileInputStream(sourceFullFile);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(sourceFullFile, opts);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = (int) ((limitSzie / sourceFileSize) * 100);
            // OperLog.error(TAG, "options=" + options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

            File dir = new File(targetFile);
            if (!dir.getParentFile().exists()) {
                dir.getParentFile().mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(new File(targetFile));
            try {
                fos.write(baos.toByteArray());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }

            }
        }
    }

    /**
     * 保存图片到文件中
     * 
     * @param bitmap
     * @throws IOException
     */
    public static String saveBitmapToFile(Bitmap bitmap, String mFileThumbPath, String mFileThumbName)
            throws IOException {

        if (bitmap == null) {
            return "";
        }
        String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 头像全路径
        File p = new File(mFileThumbPath);
        if (!p.exists()) {
            p.mkdirs();
        }
        File file = new File(mFileThumbPath, mFileThumbName);
        if (!file.isFile()) {
            file.createNewFile();
        }

        // file.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullFileThumbPath;
    }
    /**
     * 保存图片到本地
     * 
     * @param bitmap
     * @throws IOException
     */
    public static String saveBitmapToImg(Bitmap bitmap, String mFileThumbPath, String mFileThumbName)
    		throws IOException {
    	
    	if (bitmap == null) {
    		return "";
    	}
    	String fullFileThumbPath = mFileThumbPath + mFileThumbName;// 头像全路径
    	File p = new File(mFileThumbPath);
    	if (!p.exists()) {
    		p.mkdirs();
    	}
    	File file = new File(mFileThumbPath, mFileThumbName);
    	if (!file.isFile()) {
    		file.createNewFile();
    	}
    	
    	// file.createNewFile();
    	FileOutputStream fOut = null;
    	try {
    		fOut = new FileOutputStream(file);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
    	try {
    		fOut.flush();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	try {
    		fOut.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return fullFileThumbPath;
    }

    public static Bitmap urlToBitmap(String urlpath) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            conn.connect();
            InputStream in;
            in = conn.getInputStream();

            bitmap = BitmapFactory.decodeStream(in);

        } catch (MalformedURLException e) {
            // TODO: handle exception
            //OperLog.error(TAG, "urlToBitmap  MalformedURLException failed", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //OperLog.error(TAG, "urlToBitmap  IOException failed", e);
        } catch (RuntimeException e) {
            // TODO: handle exception
            //OperLog.error(TAG, "urlToBitmap  RuntimeException failed", e);
        }
        return bitmap;
    }

    /**
     * 获取视频文件时长，单位:毫秒.
     * 
     * @param filePath
     *            the file path
     * @return the time
     */
    public static int getVideoTimeLength(String filePath) {
        int time = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            time = mediaPlayer.getDuration();

            mediaPlayer.release();
            mediaPlayer = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return time;
    }

    // 复制文件
    /**
     * Copy file.
     * 
     * @param sourceFile
     *            the source file
     * @param targetFile
     *            the target file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * 
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        if (sourceFile == null || targetFile == null) {
            Log.i(TAG, "Illegal  Argument!");
            throw new IllegalArgumentException();
        }
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }

    /**
     * Copy file.
     * 
     * @param sourceFile
     *            the source file
     * @param targetFile
     *            the target file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * 
     */
    public static void copyFile(File sourceFile, String targetFile) throws IOException {
        if (sourceFile == null || targetFile == null) {
            Log.i(TAG, "Illegal  Argument!");
            throw new IllegalArgumentException();
        }
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;

        String targetFilePath = targetFile.substring(0, targetFile.lastIndexOf("/"));
        File file = new File(targetFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {

            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(new File(targetFile)));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }

    /**
     * 写文件.
     * 
     * @param fullpath
     *            fullpath
     * @param filename
     *            filename
     * @param is
     *            InputStream
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * 
     */
    public static void writeFile(String fullpath, String filename, InputStream is) throws IOException {
        if (fullpath == null || fullpath.trim().equals("") || filename == null || filename.trim().equals("")
                || is == null) {
            Log.i(TAG, "Illegal  Argument!");
            throw new IllegalArgumentException();
        }
        FileOutputStream out = null;
        try {
            File p = new File(fullpath);
            if (!p.exists()) {
                p.mkdirs();
            }
            File file = new File(fullpath, filename);
            if (!file.isFile()) {
                file.createNewFile();
            }

            out = new FileOutputStream(file);

            int ch;
            byte[] b1 = new byte[1024];
            while ((ch = is.read(b1)) != -1) {
                out.write(b1, 0, ch);
                out.flush();
            }

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // OperLog.error(TAG, "", e);
            } catch (RuntimeException e) {
                // OperLog.error(TAG, "", e);
            }
            try {
                out.close();
            } catch (IOException e) {
                // OperLog.error(TAG, "", e);
            } catch (RuntimeException e) {
                // OperLog.error(TAG, "", e);
            }
        }
    }

    /**
     * 写文件.
     * 
     * @param fullpath
     *            fullpath
     * @param filename
     *            filename
     * @param str
     *            str
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * 
     * @return filepath
     * 
     */
    public static String writeFile(String fullpath, String filename, String str) throws IOException {

        FileOutputStream outStream = null;
        try {
            File p = new File(fullpath);
            if (!p.exists()) {
                p.mkdirs();
            }
            File file = new File(fullpath, filename);
            if (!file.isFile()) {
                file.createNewFile();
            }

            outStream = new FileOutputStream(file);

            outStream.write(str.getBytes());
            outStream.close();

        } finally {

        }

        return (fullpath + filename);

    }

    /**
     * File2byte.
     * 
     * @param filePath
     *            the file path
     * @return the byte[]
     * 
     */
    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 获取媒体文件大小. 单位:byte
     * 
     * @param filePath
     *            the file path
     * @return the size
     */
    public static long getFileLength(String filePath) {

        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        return file.length();

    }

    /**
     * 获取媒体文件显示大小. 按K，M,G分类
     * 
     * @param filePath
     *            the file path
     * @return the size
     */
    public static String getFileDisplaySize(String filePath) {

        File file = new File(filePath);
        long fileSize = 0;
        if (!file.exists()) {
            fileSize = 0;
        } else {
            fileSize = file.length();
        }
        if (fileSize > 1024 * 1024) {
            float size = fileSize / (1024f * 1024f);
            return new DecimalFormat("#.00").format(size) + "M";
        } else if (fileSize >= 1024) {
            float size = fileSize / 1024;
            return new DecimalFormat("#.00").format(size) + "K";
        } else {
            return fileSize + "B";
        }

    }
    
    public static String getFileVideoFormat(int fileSize){
    	if (fileSize > 1024 * 1024) {
            float size = fileSize / (1024f * 1024f);
            return new DecimalFormat("#.00").format(size) + "M";
        } else if (fileSize >= 1024) {
            float size = fileSize / 1024;
            return new DecimalFormat("#.00").format(size) + "K";
        } else {
            return fileSize + "B";
        }
    }

    /**
     * 获取媒体文件时长(语音或视频文本)，单位:毫秒.
     * 
     * @param filePath
     *            the file path
     * @return the time
     */
    public static int getMediaTimeLength(String filePath) {
        int time = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            time = mediaPlayer.getDuration();

            mediaPlayer.release();
            mediaPlayer = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return time;
    }

    /**
     * Gif type.
     * 
     * @param path
     *            the path
     * @return true, if successful
     */
    public static boolean gifType(String path) {
        boolean isgif = false;
        String gifstr = null;
        try {
            if (!StringUtil.isEmpty(path)) {
                gifstr = path.substring(path.lastIndexOf("."));
            }
        } catch (RuntimeException e) {

        }

        if (!StringUtil.isEmpty(gifstr) && gifstr.toUpperCase().equals(".GIF")) {
            isgif = true;
        }
        return isgif;
    }
}
