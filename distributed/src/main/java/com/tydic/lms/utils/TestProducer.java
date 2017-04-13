/**   
 * @Title: TestProducer.java
 * @Package com.tydic.lms.utils
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月21日 上午11:48:12
 * @version 
 */
package com.tydic.lms.utils;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

public class TestProducer {

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.put("zookeeper.connect", "10.40.15.10:2181,10.40.15.11:2181,10.40.15.12:2181/kafka");
        prop.put("metadata.broker.list", "10.40.15.11:9092,10.40.15.12:9092");
        prop.put("serializer.class", StringEncoder.class.getName());
        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(prop));
        int i = 0;
        while(true){
            producer.send(new KeyedMessage<String, String>("test", "msg:"+i++));
            Thread.sleep(1000);
        }
    }

}