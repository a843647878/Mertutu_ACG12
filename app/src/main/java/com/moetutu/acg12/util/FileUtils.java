package com.moetutu.acg12.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {


    private FileUtils() {
    }

    public static final String DEF_FILEPATH = "/XhsEmoticonsKeyboard/Emoticons/";

    public static String getFolderPath(String folder) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + DEF_FILEPATH + folder;
    }

    public static void unzip(InputStream is, String dir) throws IOException {
        File dest = new File(dir);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        if (!dest.isDirectory())
            throw new IOException("Invalid Unzip destination " + dest);
        if (null == is) {
            throw new IOException("InputStream is null");
        }

        ZipInputStream zip = new ZipInputStream(is);

        ZipEntry ze;
        while ((ze = zip.getNextEntry()) != null) {
            final String path = dest.getAbsolutePath()
                    + File.separator + ze.getName();

            String zeName = ze.getName();
            char cTail = zeName.charAt(zeName.length() - 1);
            if (cTail == File.separatorChar) {
                File file = new File(path);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new IOException("Unable to create folder " + file);
                    }
                }
                continue;
            }

            FileOutputStream fout = new FileOutputStream(path);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = zip.read(bytes)) != -1) {
                fout.write(bytes, 0, c);
            }
            zip.closeEntry();
            fout.close();
        }
    }

    /**
     * 获取内置存储空间大小
     *
     * @return MB -1表示未挂载
     */
    public static long getAvaiableSpaceMB() {
        long avaiableSpace = getAvaiableSpace();
        return avaiableSpace > 0 ? avaiableSpace / (1024 * 1024) : avaiableSpace;
    }

    /**
     * 获取内置存储空间 单位b
     *
     * @return b -1表示未挂载
     */
    public static long getAvaiableSpace() {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String sdcard = Environment.getExternalStorageDirectory().getPath();
                StatFs statFs = new StatFs(sdcard);
                long blockSize = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = statFs.getBlockSizeLong();
                } else {
                    blockSize = statFs.getBlockSize();
                }
                long blocks = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blocks = statFs.getAvailableBlocksLong();
                } else {
                    blocks = statFs.getAvailableBlocks();
                }
                return (blocks * blockSize);
            }
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 判断是否剩余可用的空间[内置存储]
     *
     * @param sizeMb
     * @return
     */
    public static boolean isAvaiableSpace(int sizeMb) {
        return sizeMb >= getAvaiableSpaceMB();
    }


}

