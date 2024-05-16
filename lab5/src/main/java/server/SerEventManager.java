package server;

import xml.*;
import java.io.*;
import java.nio.*;

import java.util.*;
import xml.commands.*;
import xml.events.list.*;
import java.nio.channels.*;
import java.util.concurrent.*;

public class SerEventManager {
    private String error = null;
    XMLCreate xmlCreate;

    public SerEventManager() {
        xmlCreate = new XMLCreate();
    }

    public void sendSuccess(SelectionKey key)
            throws IOException {

        SocketChannel socketChannel = (SocketChannel)key.channel();
        String xmlString = xmlCreate.getSuccess();
        writeAnswer(xmlString, socketChannel);
    }

    public void sendListSuccess(SelectionKey key, ConcurrentMap<SelectionKey, Login> activeUsers)
            throws IOException {

        SocketChannel socketChannel = (SocketChannel)key.channel();
        ListUsers listUsers = new ListUsers();

        for (Login login : activeUsers.values()) {
            listUsers.addUser(login);
        }

        String xmlString = xmlCreate.getListSuccess(listUsers);
        writeAnswer(xmlString, socketChannel);
    }

    public void sendError(SelectionKey key)
            throws IOException {

        SocketChannel socketChannel = (SocketChannel)key.channel();
        String xmlString = xmlCreate.getError(error);
        writeAnswer(xmlString, socketChannel);
    }

    private void writeAnswer(String xmlString, SocketChannel socketChannel)
            throws IOException {

        int len = 4 + xmlString.getBytes().length;
        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.clear();
        buffer.putInt(xmlString.length());
        buffer.put(xmlString.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public void broadCast(String message, ConcurrentMap<SelectionKey, Login> activeUsers) {
        int len = 4 + message.getBytes().length;
        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.clear();
        buffer.putInt(message.length());
        buffer.put(message.getBytes());
        buffer.flip();

        for (SelectionKey key : activeUsers.keySet()) {
            try {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
                buffer.rewind();
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }
    }

    public void sendMessages(Queue<ConnectionOfMessages> bufferOfStr, SelectionKey key)
            throws IOException {

        SocketChannel socketChannel = (SocketChannel)key.channel();

        for (ConnectionOfMessages connect : bufferOfStr) {
            String xmlString = xmlCreate.getServerMes(connect.sender(), connect.message());
            writeAnswer(xmlString, socketChannel);
        }
    }

    public void setError(String error) {
        this.error = error;
    }
}
