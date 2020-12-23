package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.response.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 拷贝文件到本地
     * @param file
     */
    public static String transferTo(MultipartFile file, String basePath) {
        logger.info("basePath : " + basePath);
        String fileName = file.getOriginalFilename();
        File dest = new File(basePath + "/" + fileName);
        try {
            //判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                logger.info("dest.getParentFile() : "+dest.getParentFile().getParent());
                dest.getParentFile().mkdirs();
            }

            file.transferTo(dest);
            logger.info("文件拷贝成功");
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }

        return dest.getAbsolutePath();
    }

    /**
     * 写入文件
     * @param target
     * @param src
     * @throws IOException
     */
    public static void write(String target, InputStream src) throws IOException {
        OutputStream os = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = src.read(buf))) {
            os.write(buf,0,len);
        }
        os.flush();
        os.close();
    }

    /**
     * 分块写入文件
     * @param target
     * @param targetSize
     * @param src
     * @param srcSize
     * @param chunks
     * @param chunk
     * @throws IOException
     */
    public static void writeWithBlok(String target, Long targetSize, InputStream src, Long srcSize, Integer chunks, Integer chunk) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(target,"rw");
        randomAccessFile.setLength(targetSize);
        if (chunk == chunks - 1) {
            randomAccessFile.seek(targetSize - srcSize);
        } else {
            randomAccessFile.seek(chunk * srcSize);
        }
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = src.read(buf))) {
            randomAccessFile.write(buf,0,len);
        }
        randomAccessFile.close();
    }

    /**
     * 生成随机文件名
     * @return
     */
    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }
}
