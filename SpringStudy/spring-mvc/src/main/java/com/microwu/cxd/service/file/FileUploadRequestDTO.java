package com.microwu.cxd.service.file;

import lombok.Data;

import java.io.File;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/9/1   10:19
 * Update History:
 * Author        Time            Content
 */
@Data
public class FileUploadRequestDTO {

    private String path;

    private File file;

    private int chunk;

    private long chunkSize;

    private long chunks;
}
