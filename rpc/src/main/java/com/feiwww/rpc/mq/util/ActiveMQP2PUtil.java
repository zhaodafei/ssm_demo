package com.feiwww.rpc.mq.util;

import com.feiwww.rpc.mq.handler.MessageHandler;
import com.feiwww.util.PropUtil;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.Properties;

/**
 * activemq p2p 工具类
 */
public class ActiveMQP2PUtil {
    private static final String RPC_CONFIG_FILE = "rpc_config.properties";
    private static String queueURL;        //队列所在的URL
    private static String queueName;    //队列名称
    private static ConnectionFactory connectionFactory;//连接工厂

    static {
        Properties props = PropUtil.loadProps(RPC_CONFIG_FILE);
        queueURL = props.getProperty("activemq.queueURL", "tcp://127.0.0.1:61616");
        System.out.println(queueURL);
        queueName = props.getProperty("activemq.queueName", "adminQueue");
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                queueURL);
    }

    /**
     * 发送消息
     */
    public static void sendMessage(Serializable message) {
        Connection conn = null;
        try {
            conn = connectionFactory.createConnection(); //创建连接
            conn.start();//创建连接
            Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);//创建session
            // Queue queue = session.createQueue(queueName);  //TODO(默认是这个)
            Destination destination = session.createQueue(queueName);//创建队列
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//消息设置为非持久化
            ObjectMessage msg = session.createObjectMessage(message);//创建消息：createObjectMessage()
            producer.send(msg);
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 接收消息
     *
     * @param handler 自定义的消息处理器
     */
    public static void receiveMessage(MessageHandler handler) {
        Connection conn = null;
        try {
            conn = connectionFactory.createConnection();//创建连接
            conn.start();//启动连接
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);//创建session
            // Queue queue = session.createQueue(queueName);   //TODO(默认的是这个)
            Destination destination = session.createQueue(queueName);//创建队列
            MessageConsumer consumer = session.createConsumer(destination);//创建消息消费者
            while (true) {
                Message msg = consumer.receive();
                if (msg != null) {
                    handler.handle(msg);
                    // System.out.println(msg);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        sendMessage("hello word3");
    }

}
