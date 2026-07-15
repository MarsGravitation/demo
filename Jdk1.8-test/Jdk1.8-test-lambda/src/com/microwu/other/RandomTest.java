package com.microwu.other;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Description:
 *
 * https://www.jianshu.com/p/7619c4d76f07
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/6   14:40
 * Update History:
 * Author        Time            Content
 */
public class RandomTest {

    public static void main(String[] args) {
        Random random = new Random();
        int value = random.nextInt(20) % (20 - 5 + 1) + 5;
        System.out.println(value);

        SecureRandom secureRandom = new SecureRandom();
        value = secureRandom.nextInt(20) % (20 - 5 + 1) + 5;
        System.out.println(value);
    }

}
