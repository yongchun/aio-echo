package com.shannon.aio.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/3/4.
 * <p/>
 * 监听 the read event of {@link AsynchronousSocketChannel}
 */
public class ClientReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientReadCompletionHandler.class.getName());

    private AsynchronousSocketChannel asc;

    public ClientReadCompletionHandler(AsynchronousSocketChannel asc) {
        this.asc = asc;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);
            String body = new String(bytes, "UTF-8");
            LOGGER.info("the echo cli receive from server is :" + body);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
