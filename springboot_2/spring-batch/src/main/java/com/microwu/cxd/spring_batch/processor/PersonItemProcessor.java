package com.microwu.cxd.spring_batch.processor;

import com.microwu.cxd.spring_batch.pojo.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/30   11:25
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {
    private static final Logger logger = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) throws Exception {
        logger.info("进行装换...");
        return person;
    }
}