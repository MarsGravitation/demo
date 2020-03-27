package com.microwu.cxd.controller;

import com.microwu.cxd.domain.Account;
import com.microwu.cxd.domain.Person;
import com.microwu.cxd.repository.HelloRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/8   17:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/")
public class HelloController {
    private ElasticsearchOperations elasticsearchOperations;
    private HelloRepository helloRepository;

    public HelloController(ElasticsearchOperations elasticsearchOperations, HelloRepository helloRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.helloRepository = helloRepository;
    }

    @PostMapping("/account")
    public String save(@RequestBody Person person) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(person.getId().toString())
                .withObject(person)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery);
        return documentId;
    }

    @GetMapping("/account/{id}")
    public Account findById(@PathVariable("id") String id) {
//        Account account = helloRepository.findById(id).get();
//        return account;
        GetQuery getQuery = new GetQuery();
//        getQuery.setId(String.valueOf(id));
        getQuery.setId(id);
        Account account = elasticsearchOperations.queryForObject(getQuery, Account.class);
        return account;
    }

}