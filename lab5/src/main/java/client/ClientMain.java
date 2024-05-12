package client;

import java.io.*;
import java.nio.*;
import java.net.*;
import jakarta.xml.bind.*;
import java.nio.channels.*;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        XMLCreate xmlCreate = new XMLCreate();
        ClientPreparer preparer = new ClientPreparer();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8008));

        String message;

        try {
            message = xmlCreate.getLogin("pyro", "wasd");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        ByteBuffer buffer = preparer.getFinalBuf(message);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }

        buffer.clear();
        socketChannel.close();
    }
}
