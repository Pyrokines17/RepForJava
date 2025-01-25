package xml.events.files;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "success")
public class LFSuccess {
    private ListFiles files;

    public LFSuccess() {}

    public LFSuccess(ListFiles files) {
        this.files = files;
    }

    @XmlElement(name = "files")
    public ListFiles getFiles() {
        return files;
    }

    public void setFiles(ListFiles files) {
        this.files = files;
    }
}
