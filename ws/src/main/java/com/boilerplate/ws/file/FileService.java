package com.boilerplate.ws.file;

import com.boilerplate.ws.configuration.application_properties.BoilerplateProperties;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private BoilerplateProperties boilerplateProperties;

    @Autowired
    private Tika tika;

    public String saveProfileImage(String image) {
        String filename = UUID.randomUUID().toString();
        Path path = Paths.get(
                boilerplateProperties.getStorageProperties().getRoot(),
                boilerplateProperties.getStorageProperties().getProfile(),
                filename);

        try {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            outputStream.write(decodeBase64Image(image));
            outputStream.close();
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image");
        }
    }

    public void deleteProfileImage(String filename){
        Path path = Paths.get(
                boilerplateProperties.getStorageProperties().getRoot(),
                boilerplateProperties.getStorageProperties().getProfile(),
                filename);
        try {
            File file = path.toFile();
            if (file.exists()) {
                boolean isDeleted = file.delete();
                if (!isDeleted) {
                    throw new RuntimeException("Error deleting image");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting image");
        }
    }

    public String detectFileType(String value) {
        byte[] decodedBase64Image = decodeBase64Image(value);
        return tika.detect(decodedBase64Image);
    }

    public byte[] decodeBase64Image(String image){
        String encodedImageBase64 = image.split(",")[1];
        return Base64.getDecoder().decode(encodedImageBase64);
    }


}
