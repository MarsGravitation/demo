package com.micruwo.cxd.rocketmq.order;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Description: RocketMQ 使用 FIFO 顺序提供有序消息
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/20   9:32
 * Update History:
 * Author        Time            Content
 */
public class OrderedProducer {

    /**
     * "E:\Program Files\Java\jdk1.8.0_333\bin\java.exe" "-javaagent:E:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2022.1.1\lib\idea_rt.jar=55638:E:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2022.1.1\bin" -Dfile.encoding=UTF-8 -classpath "E:\Program Files\Java\jdk1.8.0_333\jre\lib\charsets.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\deploy.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\access-bridge-64.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\cldrdata.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\dnsns.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\jaccess.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\jfxrt.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\localedata.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\nashorn.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\sunec.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\sunjce_provider.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\sunmscapi.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\sunpkcs11.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\ext\zipfs.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\javaws.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\jce.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\jfr.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\jfxswt.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\jsse.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\management-agent.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\plugin.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\resources.jar;E:\Program Files\Java\jdk1.8.0_333\jre\lib\rt.jar;D:\WorkSpace\demo\study-day01\rocketmq\target\classes;E:\Program Files\apache-maven-3.6.3\repo\org\apache\rocketmq\rocketmq-client\4.9.4\rocketmq-client-4.9.4.jar;E:\Program Files\apache-maven-3.6.3\repo\org\apache\rocketmq\rocketmq-common\4.9.4\rocketmq-common-4.9.4.jar;E:\Program Files\apache-maven-3.6.3\repo\org\apache\rocketmq\rocketmq-remoting\4.9.4\rocketmq-remoting-4.9.4.jar;E:\Program Files\apache-maven-3.6.3\repo\com\alibaba\fastjson\1.2.69_noneautotype\fastjson-1.2.69_noneautotype.jar;E:\Program Files\apache-maven-3.6.3\repo\io\netty\netty-all\4.1.65.Final\netty-all-4.1.65.Final.jar;E:\Program Files\apache-maven-3.6.3\repo\org\apache\rocketmq\rocketmq-logging\4.9.4\rocketmq-logging-4.9.4.jar;E:\Program Files\apache-maven-3.6.3\repo\commons-validator\commons-validator\1.7\commons-validator-1.7.jar;E:\Program Files\apache-maven-3.6.3\repo\commons-beanutils\commons-beanutils\1.9.4\commons-beanutils-1.9.4.jar;E:\Program Files\apache-maven-3.6.3\repo\commons-digester\commons-digester\2.1\commons-digester-2.1.jar;E:\Program Files\apache-maven-3.6.3\repo\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;E:\Program Files\apache-maven-3.6.3\repo\commons-collections\commons-collections\3.2.2\commons-collections-3.2.2.jar;E:\Program Files\apache-maven-3.6.3\repo\com\github\luben\zstd-jni\1.5.2-2\zstd-jni-1.5.2-2.jar;E:\Program Files\apache-maven-3.6.3\repo\org\lz4\lz4-java\1.8.0\lz4-java-1.8.0.jar;E:\Program Files\apache-maven-3.6.3\repo\org\apache\commons\commons-lang3\3.4\commons-lang3-3.4.jar" com.micruwo.cxd.rocketmq.order.OrderedProducer
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60090000, offsetMsgId=C0A88B8100002A9F000000000000E70E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=75]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60160001, offsetMsgId=C0A88B8100002A9F000000000000E7D6, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=75]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60190002, offsetMsgId=C0A88B8100002A9F000000000000E89E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=75]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF601C0003, offsetMsgId=C0A88B8100002A9F000000000000E966, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=75]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF601F0004, offsetMsgId=C0A88B8100002A9F000000000000EA2E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=76]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60240005, offsetMsgId=C0A88B8100002A9F000000000000EAF6, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=76]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60270006, offsetMsgId=C0A88B8100002A9F000000000000EBBE, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=76]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF602B0007, offsetMsgId=C0A88B8100002A9F000000000000EC86, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=76]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF602E0008, offsetMsgId=C0A88B8100002A9F000000000000ED4E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=77]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60330009, offsetMsgId=C0A88B8100002A9F000000000000EE16, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=77]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF603A000A, offsetMsgId=C0A88B8100002A9F000000000000EEDE, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=78]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF603F000B, offsetMsgId=C0A88B8100002A9F000000000000EFA8, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=78]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6043000C, offsetMsgId=C0A88B8100002A9F000000000000F072, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=77]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6047000D, offsetMsgId=C0A88B8100002A9F000000000000F13C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=77]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6055000E, offsetMsgId=C0A88B8100002A9F000000000000F206, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=79]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6058000F, offsetMsgId=C0A88B8100002A9F000000000000F2D0, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=79]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF605D0010, offsetMsgId=C0A88B8100002A9F000000000000F39A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=78]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60610011, offsetMsgId=C0A88B8100002A9F000000000000F464, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=78]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60660012, offsetMsgId=C0A88B8100002A9F000000000000F52E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=80]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF606A0013, offsetMsgId=C0A88B8100002A9F000000000000F5F8, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=80]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF606E0014, offsetMsgId=C0A88B8100002A9F000000000000F6C2, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=81]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60740015, offsetMsgId=C0A88B8100002A9F000000000000F78C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=81]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60780016, offsetMsgId=C0A88B8100002A9F000000000000F856, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=79]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF607C0017, offsetMsgId=C0A88B8100002A9F000000000000F920, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=79]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60810018, offsetMsgId=C0A88B8100002A9F000000000000F9EA, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=82]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60840019, offsetMsgId=C0A88B8100002A9F000000000000FAB4, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=82]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6088001A, offsetMsgId=C0A88B8100002A9F000000000000FB7E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=80]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6092001B, offsetMsgId=C0A88B8100002A9F000000000000FC48, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=80]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6096001C, offsetMsgId=C0A88B8100002A9F000000000000FD12, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=83]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6099001D, offsetMsgId=C0A88B8100002A9F000000000000FDDC, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=83]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF609D001E, offsetMsgId=C0A88B8100002A9F000000000000FEA6, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=84]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60A4001F, offsetMsgId=C0A88B8100002A9F000000000000FF70, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=84]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60AA0020, offsetMsgId=C0A88B8100002A9F000000000001003A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=81]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60B20021, offsetMsgId=C0A88B8100002A9F0000000000010104, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=81]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60B50022, offsetMsgId=C0A88B8100002A9F00000000000101CE, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=85]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60B90023, offsetMsgId=C0A88B8100002A9F0000000000010298, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=85]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60BC0024, offsetMsgId=C0A88B8100002A9F0000000000010362, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=82]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60C00025, offsetMsgId=C0A88B8100002A9F000000000001042C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=82]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60C40026, offsetMsgId=C0A88B8100002A9F00000000000104F6, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=86]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60C70027, offsetMsgId=C0A88B8100002A9F00000000000105C0, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=86]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60CB0028, offsetMsgId=C0A88B8100002A9F000000000001068A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=87]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60D30029, offsetMsgId=C0A88B8100002A9F0000000000010754, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=87]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60D6002A, offsetMsgId=C0A88B8100002A9F000000000001081E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=83]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60DA002B, offsetMsgId=C0A88B8100002A9F00000000000108E8, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=83]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60DE002C, offsetMsgId=C0A88B8100002A9F00000000000109B2, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=88]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60E2002D, offsetMsgId=C0A88B8100002A9F0000000000010A7C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=88]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60F4002E, offsetMsgId=C0A88B8100002A9F0000000000010B46, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=84]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60F7002F, offsetMsgId=C0A88B8100002A9F0000000000010C10, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=84]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60FB0030, offsetMsgId=C0A88B8100002A9F0000000000010CDA, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=89]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF60FF0031, offsetMsgId=C0A88B8100002A9F0000000000010DA4, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=89]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61030032, offsetMsgId=C0A88B8100002A9F0000000000010E6E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=90]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61060033, offsetMsgId=C0A88B8100002A9F0000000000010F38, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=90]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF610B0034, offsetMsgId=C0A88B8100002A9F0000000000011002, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=85]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61170035, offsetMsgId=C0A88B8100002A9F00000000000110CC, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=85]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61190036, offsetMsgId=C0A88B8100002A9F0000000000011196, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=91]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61240037, offsetMsgId=C0A88B8100002A9F0000000000011260, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=91]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61270038, offsetMsgId=C0A88B8100002A9F000000000001132A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=86]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF612C0039, offsetMsgId=C0A88B8100002A9F00000000000113F4, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=86]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6130003A, offsetMsgId=C0A88B8100002A9F00000000000114BE, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=92]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6134003B, offsetMsgId=C0A88B8100002A9F0000000000011588, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=92]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6137003C, offsetMsgId=C0A88B8100002A9F0000000000011652, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=93]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF613B003D, offsetMsgId=C0A88B8100002A9F000000000001171C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=93]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6140003E, offsetMsgId=C0A88B8100002A9F00000000000117E6, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=87]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6143003F, offsetMsgId=C0A88B8100002A9F00000000000118B0, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=87]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61470040, offsetMsgId=C0A88B8100002A9F000000000001197A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=94]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF614A0041, offsetMsgId=C0A88B8100002A9F0000000000011A44, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=94]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF614F0042, offsetMsgId=C0A88B8100002A9F0000000000011B0E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=88]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61520043, offsetMsgId=C0A88B8100002A9F0000000000011BD8, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=88]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61550044, offsetMsgId=C0A88B8100002A9F0000000000011CA2, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=95]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF615C0045, offsetMsgId=C0A88B8100002A9F0000000000011D6C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=95]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61600046, offsetMsgId=C0A88B8100002A9F0000000000011E36, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=96]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61640047, offsetMsgId=C0A88B8100002A9F0000000000011F00, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=96]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61680048, offsetMsgId=C0A88B8100002A9F0000000000011FCA, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=89]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF616D0049, offsetMsgId=C0A88B8100002A9F0000000000012094, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=89]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6184004A, offsetMsgId=C0A88B8100002A9F000000000001215E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=97]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6187004B, offsetMsgId=C0A88B8100002A9F0000000000012228, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=97]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF618C004C, offsetMsgId=C0A88B8100002A9F00000000000122F2, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=90]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF618F004D, offsetMsgId=C0A88B8100002A9F00000000000123BC, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=90]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6193004E, offsetMsgId=C0A88B8100002A9F0000000000012486, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=98]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF6196004F, offsetMsgId=C0A88B8100002A9F0000000000012550, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=98]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61990050, offsetMsgId=C0A88B8100002A9F000000000001261A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=99]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF619D0051, offsetMsgId=C0A88B8100002A9F00000000000126E4, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=99]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61A10052, offsetMsgId=C0A88B8100002A9F00000000000127AE, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=91]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61AC0053, offsetMsgId=C0A88B8100002A9F0000000000012878, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=91]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61B20054, offsetMsgId=C0A88B8100002A9F0000000000012942, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=100]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61B50055, offsetMsgId=C0A88B8100002A9F0000000000012A0C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=100]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61B80056, offsetMsgId=C0A88B8100002A9F0000000000012AD6, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=92]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61BC0057, offsetMsgId=C0A88B8100002A9F0000000000012BA0, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=92]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61BF0058, offsetMsgId=C0A88B8100002A9F0000000000012C6A, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=101]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61C30059, offsetMsgId=C0A88B8100002A9F0000000000012D34, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=101]
     * arg: 0
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61C6005A, offsetMsgId=C0A88B8100002A9F0000000000012DFE, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=102]
     * arg: 1
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61D7005B, offsetMsgId=C0A88B8100002A9F0000000000012EC8, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=102]
     * arg: 2
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61E0005C, offsetMsgId=C0A88B8100002A9F0000000000012F92, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=93]
     * arg: 3
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61E6005D, offsetMsgId=C0A88B8100002A9F000000000001305C, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=93]
     * arg: 4
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61EA005E, offsetMsgId=C0A88B8100002A9F0000000000013126, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=103]
     * arg: 5
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61ED005F, offsetMsgId=C0A88B8100002A9F00000000000131F0, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=103]
     * arg: 6
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61F70060, offsetMsgId=C0A88B8100002A9F00000000000132BA, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=2], queueOffset=94]
     * arg: 7
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61FA0061, offsetMsgId=C0A88B8100002A9F0000000000013384, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=3], queueOffset=94]
     * arg: 8
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF61FD0062, offsetMsgId=C0A88B8100002A9F000000000001344E, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=0], queueOffset=104]
     * arg: 9
     * SendResult [sendStatus=SEND_OK, msgId=7F000001B21818B4AAC263EF62010063, offsetMsgId=C0A88B8100002A9F0000000000013518, messageQueue=MessageQueue [topic=TopicTest, brokerName=master, queueId=1], queueOffset=104]
     *
     * Process finished with exit code 0
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/20 9:46
     *
     * @param: args - [java.lang.String]
     * @return: void
     */
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        // Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("example_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.139.129:9876");
        producer.setSendMsgTimeout(5000);
        // Launch the instance.
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10;
            // Create a message instance, specifying topic, tag and message body.
            // String topic, String tags, String keys, byte[] body
            Message msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    Integer id = (Integer) o;
                    System.out.println("arg: " + o);
                    int index = id % list.size();
                    return list.get(index);
                }
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }
        // server shutdown
        producer.shutdown();
    }
}
