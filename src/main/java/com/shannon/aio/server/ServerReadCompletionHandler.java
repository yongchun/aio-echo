package com.shannon.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.shannon.aio.bean.EchoRequest;

/**
 * Created by Shannon,chen on 16/3/4.
 * 监听 the read event of {@link AsynchronousSocketChannel}
 */
public class ServerReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerReadCompletionHandler.class.getName());

    private AsynchronousSocketChannel asc;

    public ServerReadCompletionHandler(AsynchronousSocketChannel asc) {
        this.asc = asc;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);
            String body = new String(bytes, "UTF-8");
            LOGGER.info("The echo server receive request :" + body);
            doWrite(body);
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

    private void doWrite(String response) throws Exception {
        EchoRequest echoRequest = JSON.parseObject(response, EchoRequest.class);

        int responseValue = echoRequest.getValue() * 100;
        echoRequest.setDesc("echo server response");
        echoRequest.setValue(responseValue);

        byte[] responseBytes = JSON.toJSONString(echoRequest).getBytes();
        final ByteBuffer writerBuffer = ByteBuffer.allocate(responseBytes.length);
        writerBuffer.put(responseBytes);
        writerBuffer.flip();
        asc.write(writerBuffer, writerBuffer, new ServerWriteCompletionHandler(asc, Boolean.FALSE));
    }
}
