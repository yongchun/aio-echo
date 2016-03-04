package com.shannon.aio.client;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/2/28.
 * <p/>
 * 客户端
 */
public class EchoClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(EchoClient.class.getName());

    private final static int BUFFER_SIZE = 1024;

    private AsynchronousSocketChannel asc; // 客户端channel

    private String host;
    private int port;
    private String requset; // 客服端传递的字符串

    public EchoClient(String host, int port, String request) {
        this.host = host;
        this.port = port;
        this.requset = request;
        try {
            asc = AsynchronousSocketChannel.open(); // 打开AsynchronousSocketChannel
            asc.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
            asc.setOption(StandardSocketOptions.SO_RCVBUF, BUFFER_SIZE);
            asc.setOption(StandardSocketOptions.SO_SNDBUF, BUFFER_SIZE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("client start up");

    }

    public String getRequset() {
        return requset;
    }

    public AsynchronousSocketChannel getAsc() {
        return asc;
    }

    public void start() {
        doConnect();
    }

    private void doConnect() {
        asc.connect(new InetSocketAddress(host, port),
                this, new ConnectCompletionHandler());
    }

}
