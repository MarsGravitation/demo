package com.micruwo.cxd.rocketmq.batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * 1. 为什么要分批？
 *  批量发送消息提高了传递小消息的性能
 *
 * 2. 使用限制
 *  同一批次的消息应该具有：相同的主题，相同的 waitStoreMsgOK 并且不支持调度。
 *  此外，一批消息的总大小不应超过 1MB
 *
 * 3. 如何使用批处理
 *  List<Message> messages = new ArrayList<>();
 *  messages.add(...);
 *  messages.add(...);
 *
 *  producer.send(messages);
 *
 * 4. 拆分为列表
 *  只有当发送大批量并且可能不确定是否超过大小限制时，复杂性才会增加。
 *  这时候，最好拆分列表
 *
 *  ListSplitter splitter = new ListSplitter(messages);
 *  while (splitter.hasNext()) {
 *      List<Message> listItem = splitter.next();
 *      producer.send(listItem);
 *  }
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/25   14:00
 * Update History:
 * Author        Time            Content
 */
public class ListSplitter implements Iterator<List<Message>> {

    private final int SIZE_LIMIT = 1000 * 1000;
    private final List<Message> messages;
    private int currIndex;

    public ListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;
        for (; nextIndex < messages.size(); nextIndex++) {
            Message message = messages.get(nextIndex);
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            // for log overhead
            tmpSize = tmpSize + 20;
            if (tmpSize > SIZE_LIMIT) {
                // it is unexpected that single message exceeds the SIZE_LIMIT
                // here just let it go, otherwise it will block the splitting process
                if (nextIndex - currIndex == 0) {
                    // if the next sublist has no element, add this one and then break, otherwise just break
                    nextIndex++;
                }
                break;
            }
            if (tmpSize + totalSize > SIZE_LIMIT) {
                break;
            } else {
                totalSize += tmpSize;
            }
        }
        List<Message> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
