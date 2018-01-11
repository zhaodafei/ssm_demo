package com.feiwww.web.mq;

import com.feiwww.rpc.mq.util.ActiveMQP2PUtil;
import com.feiwww.service.log.LogMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用于接收消息的测试类
 */
@Controller
@RequestMapping("/mq")
public class MessageReceiver {

    @Autowired
    private LogMessageHandler handler;

    @RequestMapping("/receive")
    public void receiveMessage(){
        ActiveMQP2PUtil.receiveMessage(handler);
    }
}
