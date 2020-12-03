package com.tudie.photopickerview;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SDCardUtils {

    public static final String rootFilder = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();
    public static final String packagename = "testapp";
    public static final String Pdath = rootFilder + File.separator + packagename;//建立根目录
    public static final String Image = Pdath + File.separator + "Image";//图片保存位置
    public static final String DownLoad = Pdath + File.separator + "down";//下载文件
    public static final String ErorLog = Pdath + File.separator + "ErorLog";//Log保存位置
    public static final String Data = Pdath + File.separator + "data";//Log保存位置
    public static final String Log = ErorLog + "/log.txt";//异常日志
    public static SDCardUtils sdCardUtils;

    /**
     * [Summary]
     * 建立系统文件夹
     */
    private void createFolder() {

        File dirI = new File(Image);
        if (!dirI.exists()) {
            dirI.mkdirs();
        }
        File dird = new File(DownLoad);
        if (!dird.exists()) {
            dird.mkdirs();
        }
        File dirL = new File(ErorLog);
        if (!dirL.exists()) {
            dirL.mkdirs();
        }
    }

    private SDCardUtils() {
        createFolder();
    }

    public static SDCardUtils instance() {
        if (sdCardUtils == null)
            sdCardUtils = new SDCardUtils();
        return sdCardUtils;
    }

    /**
     * [Summary]
     * 创建错误日志报告
     *
     * @throws IOException
     */

    private static void createText() throws IOException {
        File dir = new File(Log);
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    /**
     * [Summary]
     * 写入错误日志报告
     *
     * @param str
     */
    public static void printf(String str) {
        try {
            createText();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(Log, true);
            bw = new BufferedWriter(fw);
            String myreadline = "[" + "2020" + "]" + str;
            bw.write(myreadline + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); //刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
            }
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param filepath file 要删除的根目录
     */
    public static void RecursionDeleteFile(String filepath) {
        deleteAllFiles(new File(filepath));
    }

    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 删除目录下文件
     *
     * @param filepath file 要删除的根目录
     */

    public static void deleteAllFiles(String filepath) {
         deleteAllFiles(new File(filepath));
    }

    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    /**
     * 获取文件夹大小
     *
     * @param filepath File实例
     * @return long
     */
    public static long getFolderSize(String filepath) {
        return getFolderSize(new File(filepath));
    }

    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }


}
