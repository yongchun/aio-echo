package com.shannon.aio.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.shannon.aio.bean.EchoRequest;
import com.shannon.aio.client.EchoClient;

/**
 * Created by Shannon,chen on 16/2/28.
 */
public class ClientMain {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientMain.class.getName());

    public static void main(String[] args) {
        EchoRequest request = new EchoRequest();
        request.setDesc("echo client request");
        request.setValue(12);
        String content = JSON.toJSONString(request);

        EchoClient echoClient = new EchoClient("127.0.0.1", 8002, content);
        echoClient.start();

    }
}
