package client;

import view.*;
import view.Window;
import javax.swing.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.nio.channels.*;
import java.util.logging.Logger;

public class Listener extends Thread {
    private final SocketChannel socketChannel;
    private final Window window;
    private boolean flag;
    private String path;

    public Listener(SocketChannel socketChannel, Window window) {
        this.socketChannel = socketChannel;
        this.window = window;
        this.flag = true;
        this.path = null;

        if (window != null) {
            this.window.setListener(this);
        }
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

                        while (buffer.hasRemaining()) {
                            keyChannel.read(buffer);
                        } buffer.flip();

                        String message = EventParser.parse(buffer, path, window);

                        if (!message.equals("Success")) {
                            if (window == null) {
                                System.out.println(message);
                            } else {
                                if (message.contains("Message from")) {
                                    window.updateChat(message);
                                } else {
                                    SwingUtilities.invokeLater(() ->
                                            Notification.show("Notify", message));
                                }
                            }
                        } else {
                            Logger.getGlobal().info("Success");
                        }
                    }

                    iter.remove();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (CancelledKeyException ignored) {}
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void stopListener() {
        flag = false;
    }
}
