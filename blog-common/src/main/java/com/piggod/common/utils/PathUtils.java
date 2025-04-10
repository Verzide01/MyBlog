package com.piggod.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//对原始文件名进行修改文件名，并修改存放目录
public class PathUtils {

    public static String generateFilePath(String fileName){
        //根据日期生成路径   2025/1/15/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);

        // 拼接成这种格式 例如： 2025/1/15/uuid15666.jpg
        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}