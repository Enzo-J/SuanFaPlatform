package com.zkwg.modelmanager.service.file;

import com.google.common.collect.ImmutableMap;
import com.zkwg.modelmanager.config.UploadConfig;
import com.zkwg.modelmanager.entity.File;
import com.zkwg.modelmanager.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.zkwg.modelmanager.utils.FileUtils.generateFileName;
import static com.zkwg.modelmanager.utils.UploadUtils.*;

/**
 * 文件上传服务
 */
@Service
public class FileService {
//    @Autowired
//    private FileDao fileDao;


    /**
     * 上传文件
     * @param md5
     * @param file
     */
    public Map<String,String> upload(String name,
                       String md5,
                       MultipartFile file) throws IOException {
        String path = UploadConfig.path + generateFileName();
        FileUtils.write(path, file.getInputStream());
//        fileDao.save(new File(name, md5, path, new Date()));
        return ImmutableMap.of("path", path);
    }

    /**
     * 分块上传文件
     * @param md5
     * @param size
     * @param chunks
     * @param chunk
     * @param file
     * @throws IOException
     */
    public Map<String,String> uploadWithBlock(String name,
                                              String md5,
                                              Long size,
                                              Integer chunks,
                                              Integer chunk,
                                              MultipartFile file) throws IOException {
        String fileName = getFileName(md5, chunks);
        FileUtils.writeWithBlok(UploadConfig.path + fileName, size, file.getInputStream(), file.getSize(), chunks, chunk);
        addChunk(md5,chunk);
        if (isUploaded(md5)) {
            removeKey(md5);
//            fileDao.save(new File(name, md5,UploadConfig.path + fileName, new Date()));
        }
        return ImmutableMap.of("path", UploadConfig.path + fileName);
    }

    /**
     * 获取文件后缀
     * @param file
     * @return
     */
    private String getFileType(MultipartFile file) {
        int begin = file.getOriginalFilename().indexOf(".");
        int last = file.getOriginalFilename().length();
        //获得文件后缀名
        return begin > 0 ? file.getOriginalFilename().substring(begin, last) : "";
    }

    /**
     * 检查Md5判断文件是否已上传
     * @param md5
     * @return
     */
    public boolean checkMd5(String md5) {
        File file = new File();
        file.setMd5(md5);
        return false;
//        return fileDao.getByFile(file) == null;
    }
}
