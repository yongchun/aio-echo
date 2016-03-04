package com.shannon.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Shannon,chen on 16/3/4.
 * 监听 the accept event of {@link AsynchronousSocketChannel}
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, EchoServer> {
    private final static Logger LOGGER = LoggerFactory.getLogger(AcceptCompletionHandler.class.getName());

    @Override
    public void completed(AsynchronousSocketChannel result, EchoServer attachment) {
        attachment.getAssc().accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ServerReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, EchoServer attachment) {
        try {
            attachment.getAssc().close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
