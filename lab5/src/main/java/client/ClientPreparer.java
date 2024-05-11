package client;

import java.nio.*;

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

}
