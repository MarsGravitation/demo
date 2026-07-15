package com.microwu.cxd.rest;

import com.microwu.cxd.SpringMvcApplication;
import com.microwu.cxd.controller.rest.RestTemplateController;
import com.microwu.cxd.domain.Account;
import com.microwu.cxd.domain.Order;
import com.microwu.cxd.tool.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/26   17:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringMvcApplication.class)
public class RestTest {

    private static final Logger logger = LoggerFactory.getLogger(RestTest.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2022/5/26 17:20
     *
     * @param:
     * @return: void
     */
    @Test
    public void test() {
        String url = "http://httpbin.org/get";

        // 1. 直接在 url 拼参数，调的是方法 1
        ResponseEntity<String> entity = restTemplate.getForEntity(url + "?name=cxd", String.class);
        logger.info("entity:{}", entity.getBody());

        // 2. 通过可变参数，填充 url 的 ?，注意顺序
        entity = restTemplate.getForEntity(url + "?name={?}&age={?}", String.class, "cxd", 27);
        logger.info("entity:{}", entity.getBody());

        // 3. 参数放在 map 中，通过 map 中的 key 进行匹配
        entity = restTemplate.getForEntity(url + "?name={name}&age={age}", String.class, new HashMap<String, Object>() {
            {
                put("name", "cxd");
                put("age", 20);
            }
        });
        logger.info("entity:{}", entity.getBody());
    }

    /**
     * 我的猜测：如果将参数放到 LinkedMultiValueMap，就是 multipart/form-data，
     * 如果 LinkedMultiValueMap 为空，则为 application/x-www-form-urlencoded
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2022/5/26 17:25
     *
     * @param:  
     * @return: void
     */
    @Test
    public void test02() {
        String url = "http://httpbin.org/post";

        // 1. 提交表单参数
        // multipart/form-data
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", "cxd");
        params.add("age", 27);

        ResponseEntity<String> entity = restTemplate.postForEntity(url, params, String.class);
        logger.info("entity:{}", entity.getBody());

        // application/x-www-form-urlencoded;charset=UTF-8
        entity = restTemplate.postForEntity(url + "?name=cxd&age=27", new LinkedMultiValueMap<>(), String.class);
        logger.info("entity:{}", entity.getBody());

        // multipart/form-data
        entity = restTemplate.postForEntity(url + "?name=cxd&age=27", params, String.class);
        logger.info("entity:{}", entity.getBody());

        // 传对象？不行
//        Account account = new Account();
//        account.setAccount("cxd");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        HttpEntity<Object> httpEntity = new HttpEntity<>(account, headers);
//        entity = restTemplate.postForEntity(url, httpEntity, String.class);
//        logger.info("entity:{}", entity.getBody());
    }

    @Test
    public void test03() {
        String url = "http://httpbin.org/post";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Order order = new Order();
        order.setString("hello world!");
//        HttpEntity<Order> httpEntity = new HttpEntity<>(order, headers);
        HttpEntity<String> httpEntity = new HttpEntity<>(JacksonUtil.obj2String(order), headers);

        // application/json
        ResponseEntity<String> entity = restTemplate.postForEntity(url, httpEntity, String.class);
        logger.info("entity:{}", entity.getBody());
    }

}
