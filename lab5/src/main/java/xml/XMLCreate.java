package xml;

import java.io.*;
import xml.events.*;
import xml.commands.*;
import xml.events.Error;
import xml.events.files.LFSuccess;
import xml.events.files.ListFiles;
import xml.events.list.*;
import jakarta.xml.bind.*;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import xml.commands.files.*;
import xml.events.files.NewFile;
import xml.events.files.FileSuccess;

public class XMLCreate {
    StringWriter writer = new StringWriter();

    public String getLogin(String username, String password) {
        writer.getBuffer().setLength(0);
        Login login = new Login(username, password);

        try {
            JAXBContext context = JAXBContext.newInstance(Login.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(login, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getList() {
        writer.getBuffer().setLength(0);
        List list = new List();

        try {
            JAXBContext context = JAXBContext.newInstance(List.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(list, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getClientMes(String message) {
        writer.getBuffer().setLength(0);
        ClientMes clientMessage = new ClientMes(message);

        try {
            JAXBContext context = JAXBContext.newInstance(ClientMes.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(clientMessage, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getLogout() {
        writer.getBuffer().setLength(0);
        Logout logout = new Logout();

        try {
            JAXBContext context = JAXBContext.newInstance(Logout.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(logout, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getUserlogout(String username) {
        writer.getBuffer().setLength(0);
        Userlogout userlogout = new Userlogout(username);

        try {
            JAXBContext context = JAXBContext.newInstance(Userlogout.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userlogout, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getUserlogin(String username) {
        writer.getBuffer().setLength(0);
        Userlogin userlogin = new Userlogin(username);

        try {
            JAXBContext context = JAXBContext.newInstance(Userlogin.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userlogin, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getServerMes(String username, String message) {
        writer.getBuffer().setLength(0);
        ServerMes serverMessage = new ServerMes(username, message);

        try {
            JAXBContext context = JAXBContext.newInstance(ServerMes.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(serverMessage, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getSuccess() {
        writer.getBuffer().setLength(0);
        Success success = new Success();

        try {
            JAXBContext context = JAXBContext.newInstance(Success.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(success, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getError(String errorMes) {
        writer.getBuffer().setLength(0);
        Error error = new Error(errorMes);

        try {
            JAXBContext context = JAXBContext.newInstance(Error.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(error, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getListSuccess(ListUsers listUsers) {
        writer.getBuffer().setLength(0);
        ListSuccess listSuccess = new ListSuccess(listUsers);

        try {
            JAXBContext context = JAXBContext.newInstance(ListSuccess.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(listSuccess, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getSend(String path) throws IOException {
        writer.getBuffer().setLength(0);
        FileUploader uploader = new FileUploader();
        Upload upload = uploader.prepareUpload(path);

        try {
            JAXBContext context = JAXBContext.newInstance(Upload.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(upload, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getFileSuccess(FileSuccess fileSuccess) {
        writer.getBuffer().setLength(0);

        try {
            JAXBContext context = JAXBContext.newInstance(FileSuccess.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(fileSuccess, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getNewFile(String uuid, Upload upload, SelectionKey key, long size) {
        writer.getBuffer().setLength(0);
        NewFile newFile = new NewFile();
        Login login = (Login) key.attachment();

        newFile.setId(uuid);
        newFile.setFrom(login.getUsername());
        newFile.setFileName(upload.getFileName());
        newFile.setSize(size);
        newFile.setMimeType(upload.getMimeType());

        try {
            JAXBContext context = JAXBContext.newInstance(NewFile.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(newFile, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getDownload(String uuid) {
        writer.getBuffer().setLength(0);
        Download download = new Download();
        download.setId(uuid);

        try {
            JAXBContext context = JAXBContext.newInstance(Download.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(download, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getAnsDownload(xml.events.files.Download download) {
        writer.getBuffer().setLength(0);

        try {
            JAXBContext context = JAXBContext.newInstance(xml.events.files.Download.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(download, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getFileList() {
        writer.getBuffer().setLength(0);
        FileList fileList = new FileList();

        try {
            JAXBContext context = JAXBContext.newInstance(FileList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(fileList, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getFLSuccess(ListFiles listFiles) {
        writer.getBuffer().setLength(0);
        LFSuccess lfSuccess = new LFSuccess(listFiles);

        try {
            JAXBContext context = JAXBContext.newInstance(LFSuccess.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(lfSuccess, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getShowProfile(String name) {
        writer.getBuffer().setLength(0);
        ShowProfile showProfile = new ShowProfile(name);

        try {
            JAXBContext context = JAXBContext.newInstance(ShowProfile.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(showProfile, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getSaveProfile(String username, String status, String avatarPath) throws IOException {
        writer.getBuffer().setLength(0);

        Path path = Path.of(avatarPath);
        File file = path.toFile();

        String filename = file.getName();
        String encoding = "base64";

        byte[] fileContent = Files.readAllBytes(file.toPath());
        String content = Base64.getEncoder().encodeToString(fileContent);

        SaveProfile saveProfile = new SaveProfile(username, status, filename, encoding, content);

        try {
            JAXBContext context = JAXBContext.newInstance(SaveProfile.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(saveProfile, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public String getProfile(Profile profile) {
        writer.getBuffer().setLength(0);

        try {
            JAXBContext context = JAXBContext.newInstance(Profile.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(profile, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }
}
