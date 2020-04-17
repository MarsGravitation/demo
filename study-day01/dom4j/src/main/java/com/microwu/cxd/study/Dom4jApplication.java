package com.microwu.cxd.study;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * https://blog.csdn.net/qq_41860497/article/details/84339091
 * https://blog.csdn.net/linmengmeng_1314/article/details/79975599
 * https://blog.csdn.net/m0_37499059/article/details/80505567
 *
 */
public class Dom4jApplication {

    /**
     * 读取XML 文档
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/10  10:06
     *
     * @param
     * @return  void
     */
    public static Document load() throws DocumentException {
        InputStream inputStream = Dom4jApplication.class.getResourceAsStream("/test.xml");
        Document document = null;
        SAXReader saxReader = new SAXReader();
        document = saxReader.read(inputStream);
        return document;
    }

    /**
     * 获取 root 节点
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/10  10:25
     *
     * @param
     * @return  org.dom4j.Element
     */
    public static Element getRoot() throws DocumentException {
        Document document = load();
        return document.getRootElement();
    }

    /**
     * 遍历
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/10  10:29
     *
     * @param
     * @return  void
     */
    public static void traverse() throws DocumentException {
//        Element root = getRoot();
//        Iterator iterator = root.elementIterator();
//        while (iterator.hasNext()) {
//            Element element = (Element) iterator.next();
//            System.out.println(element.getName());
//        }
//        treeWalk(getRoot());
        Element root = getRoot();
        root.accept(new VisitorSupport() {
            @Override
            public void visit(Element node) {
                System.out.println(node.getText());
            }

            @Override
            public void visit(Attribute node) {
                System.out.println(node.getName());
            }
        });
    }

    public static void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof  Element) {
                treeWalk((Element) node);
                System.out.println(node.getName());
            }
        }
    }

    /**
     * 选取节点 XPath 支持
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/10  10:51
     *
     * @param   	document
     * @return  org.dom4j.Node
     */
    public static Node bar(Document document) {
        List<Node> list = document.selectNodes("//content");
        return list.get(0);
    }

    /**
     * 修改节点信息
     * 经过测试：修改前后 对象并没有改变，只是属性和文本变化了
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/10  11:02
     *
     * @param   	document
     * @return  org.dom4j.Node
     */
    public static Document modifyNode(Document document, String name) {
        Node node = bar(document);
        node.setText(name);
        return document;
    }

    /**
     * 转换成 字符串
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/10  11:14
     *
     * @param   	document
     * @return  java.lang.String
     */
    public static String string (Document document) {
        return document.asXML();
    }

    /**
     * 将字符串转换成document 对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/17  10:19
     *
     * @param   	text
     * @return  org.dom4j.Document
     */
    public static Document stringToDocument(String text) throws DocumentException {
        return DocumentHelper.parseText(text);
    }

    public static void main(String[] args) throws DocumentException {
//        traverse();
//        Node node = bar(load());
//        System.out.println(node.getText());

        // 是两个对象
//        Document document = load();
//        Document document1 = load();
//        System.out.println(document + "," + document1);

//        Document document1 = modifyNode(document, "成旭东");
//        System.out.println(bar(document1).getText());
//
//        Document document2 = modifyNode(document, "成旭军");
//        System.out.println(bar(document2).getText());
//
//        System.out.println(document + "," + document1 + "," + document2);
//        modifyNode(document, "商家入驻邮箱验证码");
//
//        String string = string(document);
//        System.out.println(string);

        String text = "<state>        <msg>账户名称,关键字过滤状态,黑名单状态,sp号状态,接口编码,接口状态</msg>         <stateEncode>10010,000000,000000,000000,140000,000000</stateEncode>     </state>";
        Document document = stringToDocument(text);
        Node node = document.selectSingleNode("//msg");
        System.out.println(node.getText());
    }
}
