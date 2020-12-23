package com.zkwg.modelmanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {

    private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

    public static String unZipFiles(File zipFile, String descDir) throws Exception {

        log.info("文件:{}. 解压路径:{}. 解压开始.");
        long start = System.currentTimeMillis();
        String newBasePath = "";
        try{
            File pathFile = new File(descDir);
            if (!pathFile.exists() || !pathFile.isDirectory()) {
                pathFile.mkdir();
            }
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            Enumeration entries = zip.entries();

            // 如果第一个是目录，修改descDir
            if(zip.entries().hasMoreElements()) {
                ZipEntry entry = zip.entries().nextElement();
                String zipEntryName = entry.getName();
                if(entry.isDirectory()) {
//                    new File(descDir + File.separator + zipEntryName).mkdir();
                    newBasePath = descDir + File.separator + zipEntryName;
                }
            }

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                if(entry.isDirectory()) {
                    new File(descDir + File.separator + zipEntryName).mkdir();
                    continue;
                }
                // 输出文件路径信息
                InputStream in = zip.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + File.separator + zipEntryName);
                byte[] buf1 = new byte[2048];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }

            log.info("文件:{}. 解压路径:{}. 解压完成. 耗时:{}ms. ",zipFile.getName(),descDir,System.currentTimeMillis()-start);
            return "".equals(newBasePath) ? descDir : newBasePath;
        }catch(Exception e){
            log.info("文件:{}. 解压路径:{}. 解压异常:{}. 耗时:{}ms. ",zipFile.getName(),descDir,e,System.currentTimeMillis()-start);
            throw new IOException(e);
        }
    }

    // 删除文件或文件夹以及文件夹下所有文件
    public static void delDir(String dirPath) throws IOException {
        log.info("删除文件开始:{}.",dirPath);
        long start = System.currentTimeMillis();
        try{
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                return;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                return;
            }
            File[] files = dirFile.listFiles();
            if(files==null){
                return;
            }
            for (int i = 0; i < files.length; i++) {
                delDir(files[i].toString());
            }
            dirFile.delete();
            log.info("删除文件:{}. 耗时:{}ms. ",dirPath,System.currentTimeMillis()-start);
        }catch(Exception e){
            log.info("删除文件:{}. 异常:{}. 耗时:{}ms. ",dirPath,e,System.currentTimeMillis()-start);
            throw new IOException("删除文件异常.");
        }
    }

}
