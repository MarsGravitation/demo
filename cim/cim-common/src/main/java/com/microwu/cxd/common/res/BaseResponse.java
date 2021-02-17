package com.microwu.cxd.common.res;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   13:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class BaseResponse<T> {

    private String code;

    private String message;

    private String reqNo;

    private T dataBody;

}