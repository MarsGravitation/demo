package com.microwu.cxd.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.microwu.cxd.netty.server.action.WorkAction;
import com.microwu.cxd.netty.server.action.param.Param;
import com.microwu.cxd.netty.server.action.param.ParamMap;
import com.microwu.cxd.netty.server.action.res.WorkRes;
import com.microwu.cxd.netty.server.config.AppConfig;
import com.microwu.cxd.netty.server.utils.ClassScanner;
import com.microwu.cxd.netty.server.utils.LoggerBuilder;
import com.microwu.cxd.netty.server.utils.PathUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   14:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerBuilder.getLogger(HttpHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DefaultHttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;

            // interceptor
//            List<CicadaInterceptor> interceptorList = new ArrayList<>();

            String uri = request.getUri();
            LOGGER.info("uri = [{}]", uri);
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(request.uri(), "utf-8"));

            // check Root Path
            AppConfig  appConfig = checkRootPath(uri, queryStringDecoder);

            // route Action
            Class<?> actionClazz = routeAction(queryStringDecoder, appConfig);

            // build paramMap
            Param paramMap = buildParamMap(queryStringDecoder);

            // interceptor before
//            interceptorBefore(interceptorList, actionClazz, paramMap);

            // execute Method
            WorkAction action = (WorkAction) actionClazz.newInstance();
            WorkRes execute = action.execute(paramMap);

            // interceptor fater
//            interceptorAfter(interceptorList, paramMap);

            // Response
            responseMsg(ctx, execute);
        }
    }

    private void responseMsg(ChannelHandlerContext ctx, WorkRes execute) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(JSON.toJSONString(execute), CharsetUtil.UTF_8));
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    private void buildHeader(DefaultFullHttpResponse response) {
        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    }

    private Param buildParamMap(QueryStringDecoder queryStringDecoder) {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        ParamMap paramMap = new ParamMap();
        for (Map.Entry<String, List<String>> stringListEntry : parameters.entrySet()) {
            String key = stringListEntry.getKey();
            List<String> value = stringListEntry.getValue();
            paramMap.put(key, value.get(0));
        }
        return paramMap;
    }

    private Class<?> routeAction(QueryStringDecoder queryStringDecoder, AppConfig appConfig) {
        String actionPath = PathUtils.getActionPath(queryStringDecoder.path());
        Map<String, Class<?>> cicadaAction = null;
        try {
            cicadaAction = ClassScanner.getCicadaAction(appConfig.getRootPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cicadaAction == null) {
            throw new RuntimeException("Must be configured WorkAction Object");
        }

        Class<?> actionClazz = cicadaAction.get(actionPath);
        if (actionClazz == null) {
            throw new RuntimeException(actionPath + " Not Found");
        }

        return actionClazz;
    }

    private AppConfig checkRootPath(String uri, QueryStringDecoder queryStringDecoder) {
        AppConfig appConfig = AppConfig.getInstance();
        if (!PathUtils.getRootPath(queryStringDecoder.path()).equals(appConfig.getRootPath())) {
            throw new RuntimeException(uri);
        }
        return appConfig;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        WorkRes<Object> workRes = new WorkRes<>();
        workRes.setCode("10000");
        workRes.setMessage("fail");

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString(workRes), CharsetUtil.UTF_8)) ;
        buildHeader(response);
        ctx.writeAndFlush(response);
    }
}