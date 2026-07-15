package com.microwu.cxd;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;

/**
 * Description:
 * https://blog.csdn.net/wangmx1993328/article/details/81662001/
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/8/21   14:42
 * Update History:
 * Author        Time            Content
 */
public class DemoAutoNewsCrawler extends BreadthCrawler {

    public DemoAutoNewsCrawler(String crawlpath, boolean autoParse) {
        super(crawlpath, autoParse);

        // 设置爬取的网址地址
        // addSeed 表示添加种子
        // 种子链接会在爬虫启动之前加入到爬取信息中并标记为未抓取状态，这个过程称为注入
        this.addSeed("");

        // 循环添加了4个种子，其实就是分页

        // addRegex 参数为一个 url 正则表达式，可以用于过滤不必抓取的链接
        this.addRegex("");
        /**
         * 过滤 jpg|png|gif 等图片地址 时：
         * this.addRegex("-.*\\.(jpg|png|gif).*");
         * 过滤 链接值为 "#" 的地址时：
         * this.addRegex("-.*#.*");
         */

        // 设置线程数
        setThreads(1);
        getConf().setTopN(2);

        // 是否进行断电爬取，默认为 false

    }

    /**
     * 必须重写 visit 方法，作用是：
     * 在整个抓取过程中，只要抓到符合要求的页面，webCollector 就会回调该方法，
     * 并传入一个包含了页面所有信息的 page 对象
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2024/8/21 14:48
     *
     * @param: page - [cn.edu.hfut.dmic.webcollector.model.Page]
     * @param: crawlDatums - [cn.edu.hfut.dmic.webcollector.model.CrawlDatums]
     * @return: void
     */
    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {

    }
}
