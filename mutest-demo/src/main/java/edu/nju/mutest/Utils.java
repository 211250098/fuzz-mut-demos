package edu.nju.mutest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    /**
     * 文件byte[]类型转File
     *
     * @param bytes     bytes
     * @param outPath   输出目录
     * @param fileName  文件名
     * @return
     */
    public static File bytesToFile(byte[] bytes, String outPath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(outPath);
            if (!dir.exists() && dir.isDirectory()) { //判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(outPath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

}