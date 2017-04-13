/**   
 * @Title: TestConsumer.java
 * @Package com.tydic.lms.utils
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月21日 上午11:46:16
 * @version 
 */
package com.tydic.lms.utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringEncoder;

public class TestConsumer {

    static final String topic = "test";
    
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put("zookeeper.connect", "10.40.15.10:2181,10.40.15.11:2181,10.40.15.12:2181/kafka");
        prop.put("serializer.class", StringEncoder.class.getName());
        prop.put("metadata.broker.list", "10.40.15.11:9092,10.40.15.12:9092");
        prop.put("group.id", "group1");
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(prop));
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        final KafkaStream<byte[], byte[]> kafkaStream = messageStreams.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
        while (iterator.hasNext()) {
            String msg = new String(iterator.next().message());
            System.out.println("收到消息："+msg);
        }
    }

}