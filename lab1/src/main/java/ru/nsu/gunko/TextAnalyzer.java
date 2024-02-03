package ru.nsu.gunko;

import java.io.IOException;
import java.io.Reader;

public class TextAnalyzer {
    private final StringBuilder string;
    private final Reader reader;
    private boolean end;

    TextAnalyzer(Reader newReader) {
        string = new StringBuilder();
        reader = newReader;
        end = false;
    }

    private void nextWord() throws IOException {
        int sym = reader.read();

        while (sym != -1 && !Character.isLetterOrDigit(sym)) {
            sym = reader.read();
        }

        while (sym != -1 && Character.isLetterOrDigit(sym)) {
            //ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(sym).flip().position(2);
            //String charBuffer = StandardCharsets.UTF_16.decode(byteBuffer).toString();

            String charBuffer = Character.toString(sym);
            string.append(charBuffer);
            sym = reader.read();
        }

        if (sym == -1) {
            end = true;
        }
    }

    public StringBuilder getWord() throws IOException {
        string.setLength(0);
        this.nextWord();
        return string;
    }

    public boolean checkEnd() {
        return end;
    }
}
