package com.microwu.cxd.netty.example.action;

import com.microwu.cxd.netty.example.res.DemoResVO;
import com.microwu.cxd.netty.server.action.WorkAction;
import com.microwu.cxd.netty.server.action.param.Param;
import com.microwu.cxd.netty.server.action.res.WorkRes;
import com.microwu.cxd.netty.server.annotation.CicadaAction;
import com.microwu.cxd.netty.server.utils.LoggerBuilder;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@CicadaAction(value = "demoAction")
public class DemoAction implements WorkAction {

    private static final Logger LOGGER = LoggerBuilder.getLogger(DemoAction.class);

    private static AtomicLong index = new AtomicLong();

    @Override
    public WorkRes<DemoResVO> execute(Param param) throws Exception {
        String name = param.getString("name");
        Integer id = param.getInteger("id");
        LOGGER.info("name = [{}], id = [{}]", name, id);

        DemoResVO demoResVO = new DemoResVO();
        demoResVO.setIndex(index.incrementAndGet());

        WorkRes<DemoResVO> workRes = new WorkRes<>();
        workRes.setCode("1");
        workRes.setMessage("success");
        workRes.setDataBody(demoResVO);

        return workRes;
    }
}