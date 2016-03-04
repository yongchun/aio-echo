package com.shannon.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/3/4.
 * 监听 the write event of {@link AsynchronousSocketChannel}
 */
public class ServerWriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerWriteCompletionHandler.class.getName());

    private AsynchronousSocketChannel asc;
    private volatile boolean isWrite = Boolean.TRUE; // 用户排除服务端的写事件,防止死循环

    public ServerWriteCompletionHandler(AsynchronousSocketChannel asc, boolean isWrite) {
        this.asc = asc;
        this.isWrite = isWrite;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        if (!isWrite) {
            if (attachment.hasRemaining()) {
                byte[] bytes = new byte[attachment.remaining()];

                attachment.get(bytes);
                try {
                    String body = new String(bytes, "UTF-8");
                    LOGGER.info("The echo server send response : " + body);
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
