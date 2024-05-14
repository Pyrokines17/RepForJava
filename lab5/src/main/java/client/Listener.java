package client;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.channels.*;

public class Listener extends Thread {
    private final SocketChannel socketChannel;
    private boolean flag;

    public Listener(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.flag = true;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);

            SocketChannel keyChannel;
            ByteBuffer buffer;

            while (flag) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();

                    if (key.isReadable()) {
                        byte[] len = new byte[4];
                        keyChannel = (SocketChannel)key.channel();
                        keyChannel.read(ByteBuffer.wrap(len));
                        buffer = ByteBuffer.allocate(ByteBuffer.wrap(len).getInt());
                        keyChannel.read(buffer); buffer.flip();
                        System.out.println(Charset.defaultCharset().decode(buffer));
                    }

                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
    }

    public void stopListener() {
        flag = false;
    }
}
