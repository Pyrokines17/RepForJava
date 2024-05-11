package client;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.nio.channels.*;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8008));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String message = "Hello from client";

        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }

        buffer.clear();
        socketChannel.close();
    }
}
