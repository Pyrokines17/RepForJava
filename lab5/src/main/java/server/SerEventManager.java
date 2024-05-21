package server;

import xml.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import xml.commands.*;
import java.nio.file.*;
import xml.events.list.*;
import xml.events.files.*;
import java.nio.channels.*;
import java.util.concurrent.*;
import xml.events.files.Download;

public class SerEventManager {
    private final static String BASE = "src/main/java/server/files/";
    private final XMLCreate xmlCreate;
    private String error = null;

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

    public void sendFileSuccess(SelectionKey key, String uuid)
        throws IOException {

        SocketChannel socketChannel = (SocketChannel)key.channel();
        FileSuccess fileSuccess = new FileSuccess(uuid);

        String xmlString = xmlCreate.getFileSuccess(fileSuccess);
        writeAnswer(xmlString, socketChannel);
    }

    public void sendDownload(SelectionKey key, String uuid, ConcurrentMap<String, String> files)
            throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        Download download = new Download();

        Path path = Paths.get(BASE+files.get(uuid));
        byte[] fileContent = Files.readAllBytes(path);
        String encodedContent = Base64.getEncoder().encodeToString(fileContent);

        download.setId(uuid);
        download.setName(files.get(uuid));
        download.setMimeType(Files.probeContentType(path));
        download.setEncoding("base64");
        download.setContent(encodedContent);

        String xmlString = xmlCreate.getAnsDownload(download);
        writeAnswer(xmlString, socketChannel);
    }
}
