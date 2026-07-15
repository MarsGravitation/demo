package com.microwu.cxd.service.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/9/1   10:18
 * Update History:
 * Author        Time            Content
 */
public abstract class SliceUploadTemplate implements SliceUploadStrategy {

    public abstract boolean upload(FileUploadRequestDTO param);

    protected File createTmpFile(FileUploadRequestDTO param) {
//        param.setPath(FileUtil.withoutHeadAndTailDiagonal(param.getPath()));
        String filename = param.getFile().getName();
        // filePathUtil.getPath(param)
        String uploadDirPath = "";
        String tempFileName = filename + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        return tmpFile;
    }

//    @Override
//    public FileUploadDTO sliceUpload(FileUploadRequestDTO param) {
//        boolean isOk = this.upload(param);
//        if (isOk) {
//            File tmpFile = this.createTmpFile(param);
//            FileUploadDTO fileUploadDTO = this.saveAndFileUploadDTO(param.getFile().getName(), tmpFile);
//            return fileUploadDTO;
//        }
//        // String md5 = FileMD5Util.getFileMD5(param.getFile());
//        String md5 = "";
//
//        Map<Integer, String> map = new HashMap<>();
//        map.put(param.getChunk(), md5);
//        FileUploadDTO fileUploadDTO = new FileUploadDTO();
//        fileUploadDTO.setChunkMd5Info(map);
//        return fileUploadDTO;
//    }

//    public boolean checkAndSetUploadProgress(FileUploadRequestDTO param, String uploadDirPath) {
//        String filename = param.getFile().getName();
//        File confFile = new File(uploadDirPath, filename + ".conf");
//        byte isComplete = 0;
//        RandomAccessFile accessFile = null;
//
//        try {
//            accessFile = new RandomAccessFile(confFile, "rw");
//            // true 表示完成
//            System.out.println("set part " + param.getChunk() + " complete");
//            // 创建 conf 文件长度为总分片数，每上传一个分块即向 conf 文件中写入一个 127
//            accessFile.setLength(param.getChunks());
//            accessFile.seek(param.getChunk());
//            accessFile.write(Byte.MAX_VALUE);
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
