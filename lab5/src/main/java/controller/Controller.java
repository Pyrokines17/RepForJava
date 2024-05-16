package controller;

import java.io.*;
import java.util.*;

public interface Controller {
    void setCommand(String line);
    String getLine() throws IOException;
    List<String> getSettings() throws IOException;
}
