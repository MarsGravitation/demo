package com.microwu.cxd.domain.http;

import lombok.Data;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   10:47
 * Update History:
 * Author        Time            Content
 */
@Data
public class ThirdPartResponse<T> {

    public static Object of(Object o) {
        return o;
    }

    public enum ThirdPartTransferResultEnum {
        UNCLEAR_FAILURE;
    }

}
