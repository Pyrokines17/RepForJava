package xml.events.files;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "file")
@XmlType(propOrder = {"filename", "id", "from", "size", "mimeType"})
public class ListFile {
    private String filename;
    private String id;
    private String from;
    private long size;
    private String mimeType;

    public ListFile() {}

    @XmlElement(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
