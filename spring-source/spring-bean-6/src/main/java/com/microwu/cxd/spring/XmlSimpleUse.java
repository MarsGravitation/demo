package com.microwu.cxd.spring;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   11:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class XmlSimpleUse {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        // 读取 xml 文件
        URL url = Thread.currentThread().getContextClassLoader().getResource("test-xml-read.xml");
        InputStream inputStream = url.openStream();
        // 将流转换成 InputSource，在后续 XML 解析使用
        InputSource inputSource = new InputSource(inputStream);
        DocumentBuilderFactory factory = createDocumentBuilder();

        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        // 可选，设置实体解析器
        documentBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        });
        // 设置回调处理器，当解析出现错误时
        documentBuilder.setErrorHandler(null);
        // 解析XML 文件，获取document，代表了整个文件
        Document document = documentBuilder.parse(inputSource);
        // 获取根元素
        Element root = document.getDocumentElement();
        System.out.println(root);

        // 获取根元素下的每个 child 元素
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                System.out.println(ele);
            }
        }

    }

    protected static DocumentBuilderFactory createDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        return factory;
    }
}