package com.microwu.cxd.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/8   17:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@Document(indexName = "bank", type = "_doc")
public class Account {
    /**
     * id 要使用String类型, 官网这么说的
     */
    @Id
    private String id;

    @Field(value = "account_number", type = FieldType.Long)
    private Long accountNumber;

    @Field(value = "balance", type = FieldType.Long)
    private Long balance;

    @Field(value = "firstname", type = FieldType.Text)
    private String firstName;

    @Field(value = "lastname", type = FieldType.Text)
    private String lastName;

    @Field(value = "age", type = FieldType.Long)
    private Long age;

    @Field(value = "gender", type = FieldType.Text)
    private String gender;

    @Field(value = "address", type = FieldType.Text)
    private String address;

    @Field(value = "employer", type = FieldType.Text)
    private String employer;

    @Field(value = "email", type = FieldType.Text)
    private String email;

    @Field(value = "city", type = FieldType.Text)
    private String city;

    @Field(value = "state", type = FieldType.Text)
    private String state;

}