package com.microwu.cxd.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/19   17:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demo01Message {

    public static final String TOPIC = "DEMO_01";

    /**
     * id
     */
    private Integer id;

}
