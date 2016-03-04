package com.shannon.aio.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/3/4.
 * <p/>
 * 监听 the write event of {@link AsynchronousSocketChannel}
 */
public class ClientWriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientWriteCompletionHandler.class.getName());

    private AsynchronousSocketChannel asc;

    private volatile boolean isWrite = Boolean.TRUE; // 用户排除客户端的写事件,防止死循环

    public ClientWriteCompletionHandler(AsynchronousSocketChannel asc, boolean isWrite) {
        this.asc = asc;
        this.isWrite = isWrite;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        if (isWrite) { // 接收到服务器端响应内容，即the write event of {@link AsynchronousSocketChannel},此处会有一次浪费
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            asc.read(readBuffer, readBuffer, new ClientReadCompletionHandler(asc));
        } else { // 客户端连接上服务器后自己写的内容
            if (attachment.hasRemaining()) {
                byte[] bytes = new byte[attachment.remaining()];

                attachment.get(bytes);
                try {
                    String body = new String(bytes, "UTF-8");
                    LOGGER.info("The echo cli send request : " + body);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
                this.isWrite = Boolean.TRUE; // 防止循环发送数据
                asc.write(attachment, attachment, this);
            }
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            asc.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
