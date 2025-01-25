package xml.events.files;

import jakarta.xml.bind.annotation.*;

import java.util.*;

@XmlRootElement(name = "files")
public class ListFiles {
    private List<ListFile> files;

    public ListFiles() {}

    public ListFiles(List<ListFile> files) {
        this.files = files;
    }

    @XmlElement(name = "file")
    public List<ListFile> getFiles() {
        return files;
    }

    public void setFiles(List<ListFile> files) {
        this.files = files;
    }

    public void addFile(ListFile file) {
        if (files == null) {
            files = new ArrayList<>();
        }

        files.add(file);
    }
}
