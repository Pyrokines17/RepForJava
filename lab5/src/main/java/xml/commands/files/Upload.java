package xml.commands.files;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(propOrder = {"name", "fileName", "mimeType", "encoding", "content"})
public class Upload {
    private String name;
    private String fileName;
    private String mimeType;
    private String encoding;
    private String content;

    public Upload() {
        this.name = "upload";
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlElement(name = "mimeType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @XmlElement(name = "encoding")
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @XmlElement(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
