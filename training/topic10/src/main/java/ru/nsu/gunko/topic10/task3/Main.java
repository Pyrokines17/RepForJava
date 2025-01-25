package ru.nsu.gunko.topic10.task3;

import com.google.common.io.Files;

import java.io.File;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        File root = new File(args[0]);
        URI base = root.toURI();

        for (File entry : Files.fileTraverser().depthFirstPreOrder(root)) {
            String resName = new File(base.relativize(entry.toURI()).getPath()).getPath();
            System.out.println(resName);
        }
    }
}