package com.microwu.cxd.service.http;

import com.microwu.cxd.annotation.HTTPMethod;
import com.microwu.cxd.domain.http.ThirdPartResponse;
import com.microwu.cxd.domain.http.WeiXinPayBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpOptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   10:43
 * Update History:
 * Author        Time            Content
 */
@Slf4j
public class WeiXinPayInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WeiXinPayBaseResponse response = null;
        try {
            // Http 处理逻辑
            response = realLogic(method, args[0]);
        } catch (Exception e) {
            // 请求失败处理
            return ThirdPartResponse.of(ThirdPartResponse.ThirdPartTransferResultEnum.UNCLEAR_FAILURE);
        }
        return ThirdPartResponse.of(response);
    }

    private WeiXinPayBaseResponse realLogic(Method method, Object args) {
        // 获取方法注解
        HTTPMethod httpMethod = method.getAnnotation(HTTPMethod.class);
        // 重试参数是网络连接重试
        HttpOptions httpOptions = httpOptionsBuild(httpMethod.retryCount());
        // 根据 url 和 方法参数构建请求体
        HttpRequest httpRequest = httpRequestBuild(httpMethod.url(), (WeiXinPayBaseResponse) args);
        // 获取请求类型
        HTTPMethod.HTTPRequestType httpRequestType = httpMethod.requestType();
        // 执行请求
        HttpResponse httpResponse = executeHttpRequest(httpOptions, httpRequest, httpRequestType);

        Type genericReturnType = method.getGenericReturnType();
        // 获取返回值的泛型参数
        if (genericReturnType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            genericReturnType = actualTypeArguments[0];
        }

        // 验签 + 解密 resData
//        String decodedData = DecodeUtil.decode(httpResponse.getResult());
//        return JacksonUtil.string2Obj(httpResponse.getEntity().toString(), genericReturnType.getClass());
        return new WeiXinPayBaseResponse();
    }

    private HttpResponse executeHttpRequest(HttpOptions httpOptions, HttpRequest httpRequest, HTTPMethod.HTTPRequestType httpRequestType) {
        HttpResponse httpResponse = null;
        try {
            switch (httpRequestType) {
//                case GET:
//                    httpResponse = HttpClientUtil.httpGet(httpRequest, httpOptions);
//                    break;
//                case POST:
//                    httpResponse = HttpClientUtil.httpPost(httpRequest.getUrl(), JSONObject.toJSONString(httpRequest.getParam()), httpOptions);
//                    break;
//                default:
//                    throw new ThirdPartHttpException(ThirdPartEnum.WeiXinPay, ReturnCodeEnum.HTTP_REQUEST_METHOD_NOT_MATCH);
            }
        } catch (Exception e) {
            throw new RuntimeException("[WeiXinPayInvocationHandler http execute error ]", e);
        }
        return httpResponse;
    }

    private HttpRequest httpRequestBuild(String url, WeiXinPayBaseResponse args) {

        HttpRequest httpRequest = null;

        // Apollo配置
//        WeiXinPayConfig config = WeiXinPayConfig.getConfig();
//
//        httpRequest.setUrl(config.getBaseUrl + url);
//        // 加密 + 签名
//        String data = EncodeUtil.encode(JSON.toJSONString(args));
//        String sign = SignUtil.sign(data);
//        WeiXinPayCommonRequest weiXinPayCommonRequest = WeiXinPayCommonRequest.builder()
//                .data(data)
//                .sign(sign).build();
//        httpRequest.setParam(weiXinPayCommonRequest);
        return httpRequest;
    }

    private HttpOptions httpOptionsBuild(int retryCount) {
        return null;
    }


}
