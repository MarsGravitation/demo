package com.microwu.cxd.service;

import com.microwu.cxd.domain.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/4   9:48
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public List<Post> getById(Long id) {
        logger.info("========博客列表======");
        Post post = new Post();
        post.setTitle("title");
        post.setContent("content");

        return Collections.singletonList(post);
    }
}