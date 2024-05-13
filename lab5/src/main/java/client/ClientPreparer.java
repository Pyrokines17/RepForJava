package client;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ClientPreparer {

    public ByteBuffer getFinalBuf(String message) {
        int len = 4 + message.getBytes().length;
        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.clear();
        buffer.putInt(message.length());
        buffer.put(message.getBytes());
        buffer.flip();

        return buffer;
    }

    public ByteBuffer getAnswer(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();

        ByteBuffer answer = ByteBuffer.allocate(byteBuffer.getInt());
        socketChannel.read(answer);
        answer.flip();
        return answer;
    }

}
