package xml.events.files;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "event")
@XmlType(propOrder = {"name", "id", "from", "fileName", "size", "mimeType"})
public class NewFile {
    private String name;
    private String id;
    private String from;
    private String fileName;
    private long size;
    private String mimeType;

    public NewFile() {
        this.name = "file";
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "from")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @XmlElement(name = "name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlElement(name = "size")
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @XmlElement(name = "mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
