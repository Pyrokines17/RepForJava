package xml.commands.files;

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class FileUploader {
    public Upload prepareUpload(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileContent = Files.readAllBytes(path);
        String encodedContent = Base64.getEncoder().encodeToString(fileContent);

        Upload upload = new Upload();

        upload.setFileName(path.getFileName().toString());
        upload.setMimeType(Files.probeContentType(path));
        upload.setEncoding("base64");
        upload.setContent(encodedContent);

        return upload;
    }
}
