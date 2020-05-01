package com.microwu.cxd.file.controller;

import com.microwu.cxd.entity.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/29   16:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file")MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        File dest = new File("E:\\Project\\program-data\\file\\" + filename);
        file.transferTo(dest);

        Result<String> result = new Result<>();
        result.setData("success");
        return result;
    }
}