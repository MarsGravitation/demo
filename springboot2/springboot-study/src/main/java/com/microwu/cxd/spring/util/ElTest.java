package com.microwu.cxd.spring.util;

import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Description: EL 表达式
 * https://www.cnblogs.com/aligege/p/11799692.html
 *
 * 应用：解析 AOP 注解中的 EL 表达式
 * https://blog.csdn.net/u010351766/article/details/108861494
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/30   15:10
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ElTest {

    public static void main(String[] args) {
        // 设置文件模板，其中 #{} 表示表达式的起止，#name 是表达式字符串，表示引用一个变量
        String template = "hello #{#name} !!!";
        // 创建表达式解析器 - 线程安全
        SpelExpressionParser parser = new SpelExpressionParser();

        // 在上下文中设置变量
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("name", "world");

        // 解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文
        Expression expression = parser.parseExpression(template, new TemplateParserContext());

        // getValue 获取表达式的值，这里传入了上下文，第二个参数是类型参数
        System.out.println(expression.getValue(context, String.class));
    }

}