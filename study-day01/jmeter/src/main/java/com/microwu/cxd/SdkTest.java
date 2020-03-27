package com.microwu.cxd;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.FileNotFoundException;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/22   14:44
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SdkTest implements JavaSamplerClient {
    private SampleResult result;
    private String a;
    private String b;
    private String fileName;

    /**
     *  初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行
     *
     * @author 成旭东               chengxudong@microwu.com
     * @date    2019/4/22  14:52
     *
     * @param   javaSamplerContext
     * @return  void
     */
    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        result = new SampleResult();
    }

    /**
     *  测试执行的循环体，根据线程数和循环次数的不同可执行多次
     *
     * @author 成旭东               chengxudong@microwu.com
     * @date    2019/4/22  15:01
     *
     * @param   javaSamplerContext
     * @return  org.apache.jmeter.samplers.SampleResult
     */
    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        a = javaSamplerContext.getParameter("a");
        b = javaSamplerContext.getParameter("b");
        fileName = javaSamplerContext.getParameter("fileName");
        result.sampleStart();
        OutputService outputService = new OutputService();
        try {
            outputService.output(fileName, a, b);
            result.setSuccessful(true);
        } catch (FileNotFoundException e) {
            result.setSuccessful(false);
            e.printStackTrace();
        }finally {
            result.sampleEnd();
        }

        return null;
    }

    @Override
    public void teardownTest(JavaSamplerContext javaSamplerContext) {

    }

    /**
     *  设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
     * @author 成旭东               chengxudong@microwu.com
     * @date    2019/4/22  14:49
     *
     * @param
     * @return  org.apache.jmeter.config.Arguments
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("a", "0");
        arguments.addArgument("b", "0");
        arguments.addArgument("fileName", "0");
        return null;
    }

    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        arguments.addArgument("a", "0");
        arguments.addArgument("b", "0");
        JavaSamplerContext javaSamplerContext = new JavaSamplerContext(arguments);
        SdkTest sdkTest = new SdkTest();
        sdkTest.setupTest(javaSamplerContext);
        sdkTest.runTest(javaSamplerContext);
        sdkTest.teardownTest(javaSamplerContext);
    }
}