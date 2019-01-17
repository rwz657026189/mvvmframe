package com.rwz.commonmodule.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  多渠道打包文件创建
 */
public class CreateChannelFile {

    public static void main(String[] args) {
        final String saveFile = "D:/rwz/channel.txt";
        File file = createFile(saveFile);
        inputChannelName(file, "tunan_", 0, 1000);
    }

    /**
     * @param filePath 保存文件的目录
     */
    private static File createFile(String filePath){
        File file = new File(filePath);
        if (file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 填充渠道名
     */
    private static void inputChannelName(File file, String baseName, int start, int end) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (int i = start; i < end; i++) {
                String name = baseName + i + "\r\n";
                fos.write(name.getBytes());
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
