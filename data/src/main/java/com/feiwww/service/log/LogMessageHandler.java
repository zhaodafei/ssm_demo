package com.feiwww.service.log;

import com.feiwww.dao.log.LogDao;
import com.feiwww.model.log.Log;
import com.feiwww.rpc.mq.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * 日志处理器（更适合放在data层）
 * 因为：
 * 1、data依赖于rpc，而rpc不依赖于data，所以如果该类放在rpc层，并且该类需要用到数据库操作（eg.将日志写入数据库），那么就不好办了
 * 2、rpc层说白了，就是一些rpc工具类，实际上与业务无关，与业务有关的，我们可以抽取到该部分来
 */
@Component
public class LogMessageHandler implements MessageHandler{

    @Autowired
    private LogDao logDao;

    public void handle(Message message) {
        System.out.println(logDao);
        //TODO(这里怎么确定强转换类型？？？？)
        ObjectMessage objMsg = (ObjectMessage) message;
        try {
            //TODO(这里怎么确定强转换类型？？？？)
            Log log = (Log) objMsg.getObject();
            logDao.insertLog(log);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
