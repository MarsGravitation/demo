package com.microwu.cxd.service.file;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/9/1   10:18
 * Update History:
 * Author        Time            Content
 */
public interface SliceUploadStrategy {
    FileUploadDTO sliceUpload(FileUploadRequestDTO param);
}
