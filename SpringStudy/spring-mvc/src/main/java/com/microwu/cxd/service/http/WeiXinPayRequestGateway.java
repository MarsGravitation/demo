package com.microwu.cxd.service.http;

import com.microwu.cxd.annotation.HTTPController;
import com.microwu.cxd.annotation.HTTPMethod;
import com.microwu.cxd.domain.http.CustomerRegResponse;
import com.microwu.cxd.domain.http.CustomerRegV2Request;
import com.microwu.cxd.domain.http.ThirdPartResponse;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   10:39
 * Update History:
 * Author        Time            Content
 */
@HTTPController(desc = "微信支付"
, thirdPart = HTTPController.ThirdPartEnum.WeiXinPay
, baseUrl = ""
, invocationHandlerClass = WeiXinPayInvocationHandler.class)
public interface WeiXinPayRequestGateway {

    /**
     * 个人用户注册接口
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/1/31 10:42
     *
     * @param: request - [CustomerRegV2Request]
     * @return: ThirdPartResponse<CustomerRegResponse>
     */
    @HTTPMethod(url = "", requestType = HTTPMethod.HTTPRequestType.POST, retryCount = 0)
    ThirdPartResponse<CustomerRegResponse> pCustomerReg(CustomerRegV2Request request);

}
