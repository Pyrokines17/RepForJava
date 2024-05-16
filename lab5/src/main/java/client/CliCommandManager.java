package client;

import xml.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class CliCommandManager {
    private final XMLCreate xmlCreate;
    private final ClientPreparer preparer;
    private final SocketChannel socketChannel;

    private ByteBuffer buffer;
    private String message;
    private boolean flag;

    public CliCommandManager(SocketChannel socketChannel) {
        this.flag = true;
        xmlCreate = new XMLCreate();
        preparer = new ClientPreparer();
        this.socketChannel = socketChannel;
    }

    public void login(String username, String password) throws IOException {
        message = xmlCreate.getLogin(username, password);
        buffer = preparer.getFinalBuf(message);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public void list() throws IOException {
        message = xmlCreate.getList();
        buffer = preparer.getFinalBuf(message);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public void logout() throws IOException {
        message = xmlCreate.getLogout();
        buffer = preparer.getFinalBuf(message);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public void clientMes(String message) throws IOException {
        this.message = xmlCreate.getClientMes(message);
        buffer = preparer.getFinalBuf(this.message);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public void stop() {
        flag = false;
    }

    public boolean isFlag() {
        return flag;
    }
}
