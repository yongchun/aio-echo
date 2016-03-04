package com.shannon.aio.main;

import com.shannon.aio.server.EchoServer;

/**
 * Created by Shannon,chen on 16/2/28.
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        EchoServer echoServer = new EchoServer("127.0.0.1", 8002);
        echoServer.start();
        Thread.sleep(1000 * 60); // 等待客户端请求
    }
}
