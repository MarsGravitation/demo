package com.microwu.cxd.spring_batch.config;

import com.microwu.cxd.spring_batch.pojo.Person;
import com.microwu.cxd.spring_batch.processor.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.validation.BindException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/30   11:28
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /**
     *  第一步定义输入,处理器和输出
     *      1.reader创造了一个ItemReader.将每行信息转换为Person
     *      2.processor
     *      3.writer写入mongodb
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/5/30  14:51
     *
     * @param   	
     * @return  org.springframework.batch.item.file.FlatFileItemReader<com.microwu.cxd.spring_batch.pojo.Person>
     */
    @Bean
    public FlatFileItemReader<Person> reader() throws FileNotFoundException {
        FlatFileItemReader<Person> fileRead = new FlatFileItemReader<>();
        fileRead.setEncoding("UTF-8");
        fileRead.setResource(new FileSystemResource(new File("E:\\work-note\\Products\\springboot_2\\spring-batch\\file\\mongo_batch.txt")));
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer(","));
        lineMapper.setFieldSetMapper(new FieldSetMapper<Person>() {
            @Override
            public Person mapFieldSet(FieldSet fieldSet) throws BindException {
                Person person = new Person();
                person.setName(fieldSet.readRawString(0));
                person.setAge(fieldSet.readInt(1));
                person.setGender(fieldSet.readString(2));
                return person;
            }
        });
        fileRead.setLineMapper(lineMapper);
        return fileRead;
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public MongoItemWriter<Person> writer() {
        MongoItemWriter<Person> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setTemplate(mongoTemplate);
        mongoItemWriter.setCollection("spring_batch_test");
        return mongoItemWriter;
    }

    /**
     *  作业配置
     *      第一种定义作业,第二种方法定义单个步骤.作业是根据步骤构建的,其中每个步骤都涉及阅读器,处理器和编写器
     *      在此作业定义中,需要增量器,因为作业使用数据库来维护执行状态.然后列出每个步骤,此作业只有一个步骤
     *
     *      在步骤定义中,可以定义一次写入的数据量
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/5/30  14:56
     *
     * @param   	step1
     * @return  org.springframework.batch.core.Job
     */
    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(MongoItemWriter<Person> writer) throws FileNotFoundException {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}