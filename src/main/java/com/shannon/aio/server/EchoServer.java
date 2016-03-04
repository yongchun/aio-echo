package com.shannon.aio.server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/2/26.
 * <p/>
 * 服务端
 */
public class EchoServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(EchoServer.class.getName());

    private AsynchronousServerSocketChannel assc;

    public AsynchronousServerSocketChannel getAssc() {
        return assc;
    }

    public EchoServer(String host, int port) {

        try {

            assc = AsynchronousServerSocketChannel.open(); // 打开AsynchronousServerSocketChannel

            assc.bind(new InetSocketAddress(host, port)); // 绑定监听地址和端口

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("server start up");
    }

    public void start() {
        doAccept();
    }

    private void doAccept() {
        assc.accept(this, new AcceptCompletionHandler());
    }
}
