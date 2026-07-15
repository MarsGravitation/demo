package com.microwu.cxd.service.factory;

import com.microwu.cxd.domain.factory.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/1   15:05
 * Update History:
 * Author        Time            Content
 */
@Service("normalUserCart")
public class NormalUserCart extends AbstractCart {
    @Override
    protected void processDeliveryPrice(long userId, Item item) {
        item.setCouponPrice(BigDecimal.ZERO);
    }

    @Override
    protected void processCouponPrice(long userId, Item item) {
        item.setDeliveryPrice(item.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .multiply(new BigDecimal("0.1")));
    }
}
