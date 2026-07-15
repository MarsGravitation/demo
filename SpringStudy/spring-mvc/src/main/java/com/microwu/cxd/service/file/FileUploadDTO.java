package com.microwu.cxd.service.file;

import lombok.Data;

import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/9/1   10:24
 * Update History:
 * Author        Time            Content
 */
@Data
public class FileUploadDTO {

    private Map<Integer, String> chunkMd5Info;

}
