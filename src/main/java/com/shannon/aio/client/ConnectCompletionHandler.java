package com.shannon.aio.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/3/4.
 * <p/>
 * 监听 the connect event of {@link AsynchronousSocketChannel}
 */
public class ConnectCompletionHandler implements CompletionHandler<Void, EchoClient> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConnectCompletionHandler.class.getName());

    @Override
    public void completed(Void result, EchoClient attachment) {
        AsynchronousSocketChannel asc = attachment.getAsc(); // 获取异步的SocketChannel

        byte[] reqBytes = attachment.getRequset().getBytes(); // 获取请求服务器的request字节
        ByteBuffer writeBuffer = ByteBuffer.allocate(reqBytes.length);
        writeBuffer.put(reqBytes);
        writeBuffer.flip();

        asc.write(writeBuffer, writeBuffer,
                new ClientWriteCompletionHandler(asc, Boolean.FALSE)); // 请求服务器
    }

    @Override
    public void failed(Throwable exc, EchoClient attachment) {
        try {
            attachment.getAsc().close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
