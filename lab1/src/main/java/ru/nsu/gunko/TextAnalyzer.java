package ru.nsu.gunko;

import java.io.*;

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
