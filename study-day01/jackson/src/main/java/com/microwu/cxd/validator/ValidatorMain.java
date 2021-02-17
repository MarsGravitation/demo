package com.microwu.cxd.validator;

import javax.validation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/23   9:25
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ValidatorMain {

    public static void test() {
        Person person = new Person();
        person.setAge(-1);

        // 1. 使用默认配置得到一个校验工厂，这个配置可以来自于 provider，SPI 提供
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 2. 得到一个校验器
        Validator validator = validatorFactory.getValidator();
        // 3. 校验 Java Bean （解析注解）返回校验结果
        Set<ConstraintViolation<Person>> result = validator.validate(person);

        // 输出校验结果
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }

    /**
     * Java Bean 校验器
     * 线程安全，全局仅需一份
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/23  9:41
     *
     * @param
     * @return  javax.validation.Validator
     */
    private Validator obtainValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

    public void test02() {
        Validator validator = obtainValidator();

        Person person = new Person();
        person.setAge(-1);
        Set<ConstraintViolation<Person>> result = validator.validate(person);

        // 输出校验结果
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }

    /**
     * 方法校验的校验器
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/23  9:42
     *
     * @param
     * @return  javax.validation.executable.ExecutableValidator
     */
    private ExecutableValidator executableValidator() {
        return obtainValidator().forExecutables();
    }

    /**
     * 代码侵入性强，考虑使用 AOP
     *  1. 基于 Java EE 的 @Inteceptors 实现
     *  2. 基于 Spring 实现
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/23  9:52
     *
     * @param   	id
     * @param 		name
     * @return  com.microwu.cxd.validator.Person
     */
    Person getOne(@NotNull @Min(1) Integer id, String name) throws NoSuchMethodException {
        Method method = this.getClass().getMethod("getOne", Integer.class, String.class);
        Set<ConstraintViolation<ValidatorMain>> result = executableValidator().validateParameters(this, method, new Object[]{id, name});
        if (!result.isEmpty()) {
            result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
            throw new IllegalArgumentException("参数错误");
        }
        return null;
    }

    /**
     * Java Bean 作为入参如何校验
     *  1. person 不能为 null
     *      |- 和方法校验一样
     *  2. 是一个合法的 person 模型，也就是 person 里面的校验规则也需要遵守
     *      |- @Valid 注解用于验证【级联】的属性，方法参数或方法返回类型，比如你的属性是个 Java Bean，你想深入校验它里面的约束
     *          在属性头上标注次注解即可。
     *          另外，@Valid 可以实现【递归验证】，因此可以标注在 List，对它里面的每个对象都执行校验
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/23  9:58
     *
     * @param   	person
     * @return  void
     */
    void save(@NotNull @Valid Person person) throws NoSuchMethodException {
        Method method = this.getClass().getMethod("save", Person.class);
        Set<ConstraintViolation<ValidatorMain>> result = executableValidator().validateParameters(this, method, new Object[]{person});
        if (!result.isEmpty()) {
            result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
            throw new IllegalArgumentException("参数错误");
        }
    }
    
    /**
     * 1. Validator
     *  a. validate：校验 Java Bean，校验 Java Bean 对象上的所有约束（属性，类）
     *  b. validateProperty: 校验指定属性
     *  c. validateValue: 校验 value 值
     *  d. getConstraintsForClass: 获取 Class 类型描述信息
     *  e. forExecutables: 获取 Executable 校验器，用于校验方法，构造器的参数
     * 2. ConstraintViolation：约束违反详情
     *  此对象保存了【违反约束的上下文】以及描述信息，简单的说，它保存着执行完所有约束后的结果
     * 3. ValidatorContext：校验器上下文，根据此上下文创建 Validator 实例
     *  |- 自己 new
     *  |- 工厂生成 - 推荐
     *      Validator validator = ValidatorUtil.obtainValidatorFactory().usingContext()
     *             .parameterNameProvider(new DefaultParameterNameProvider())
     *             .clockProvider(DefaultClockProvider.INSTANCE)
     *             .getValidator();
     * 4. 获取 Validator
     *  1. 工厂直接获取
     *  2. 从上下文获取
     *          Validator validator = ValidatorUtil.obtainValidatorFactory().usingContext()
     *             .parameterNameProvider(new DefaultParameterNameProvider())
     *             .clockProvider(DefaultClockProvider.INSTANCE)
     *             .getValidator();
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/23  10:06
     *
     * @param   	
     * @return  void
     */
    public static void test03() {
        
    }

    /**
     * Validator 校验器的五大核心组件
     *  1. MessageInterpolator 消息插值器，简单的说就是对 message 内容进行格式化
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/23  10:49
     *
     * @param   	
     * @return  void
     */
    public static void test04() {
        
    }

    public static void main(String[] args) {
        test();
    }

}